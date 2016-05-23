package com.llpay.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llpay.client.security.TraderRSAUtil;

/**
* 常用工具函数
* @author guoyx e-mail:guoyx@lianlian.com
* @date:2013-6-25 下午05:23:05
* @version :1.0
*
*/
public class LLPayUtil {
    /**
     * str空判断
     * @param str
     * @return
     * @author guoyx
     */
    public static boolean isnull(String str) {
        if (null == str || str.equalsIgnoreCase("null") || str.equals("")) {
            return true;
        } else
            return false;
    }

    /**
     * 获取当前时间str，格式yyyyMMddHHmmss
     * @return
     * @author guoyx
     */
    public static String getCurrentDateTimeStr() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeString;
    }

    /**
     * 
     * 功能描述：获取真实的IP地址
     * @param request
     * @return
     * @author guoyx
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!isnull(ip) && ip.contains(",")) {
            String[] ips = ip.split(",");
            ip = ips[ips.length - 1];
        }
        //转换IP 格式
        if(!isnull(ip)) {
            ip=ip.replace(".", "_");
        }
        return ip;
    }

    /**
     * 生成待签名串
     * @param paramMap
     * @return
     * @author guoyx
     */
    public static String genSignData(JSONObject jsonObject) {
        StringBuffer content = new StringBuffer();
        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(jsonObject.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key)) {
                continue;
            }
            String value = jsonObject.getString(key);
            // 空串不参与签名
            if (isnull(value)) {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&")) {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
    }

    /**
     * 加签
     * 
     * @param reqObj
     * @param rsa_private
     * @param md5_key
     * @return
     * @author guoyx
     */
    public static String addSign(JSONObject reqObj, String rsa_private) {
        if (reqObj == null) {
            return "";
        }
        return addSignRSA(reqObj, rsa_private);
    }

    /**
     * 签名验证
     * 
     * @param reqStr
     * @return
     */
    public static boolean checkSign(String reqStr, String rsa_public) {
        JSONObject reqObj = JSON.parseObject(reqStr);
        if (reqObj == null) {
            return false;
        }
        return checkSignRSA(reqObj, rsa_public);
    }

    /**
     * RSA签名验证
     * 
     * @param reqObj
     * @return
     * @author guoyx
     */
    private static boolean checkSignRSA(JSONObject reqObj, String rsa_public) {
        System.out.println("进入商户[" + reqObj.getString("oid_partner") + "]RSA签名验证");
        String sign = reqObj.getString("sign");
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        System.out.println("商户[" + reqObj.getString("oid_partner") + "]待签名原串" + sign_src);
        System.out.println("商户[" + reqObj.getString("oid_partner") + "]签名串" + sign);
        try {
            if (TraderRSAUtil.checksign(rsa_public, sign_src, sign)) {
                System.out.println("商户[" + reqObj.getString("oid_partner") + "]RSA签名验证通过");
                return true;
            } else {
                System.out.println("商户[" + reqObj.getString("oid_partner") + "]RSA签名验证未通过");
                return false;
            }
        } catch (Exception e) {
            System.out.println("商户[" + reqObj.getString("oid_partner") + "]RSA签名验证异常" + e.getMessage());
            return false;
        }
    }

    /**
     * RSA加签名
     * 
     * @param reqObj
     * @param rsa_private
     * @return
     * @author guoyx
     */
    private static String addSignRSA(JSONObject reqObj, String rsa_private) {
        System.out.println("进入商户[" + reqObj.getString("oid_partner") + "]RSA加签名");
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        System.out.println("商户[" + reqObj.getString("oid_partner") + "]加签原串" + sign_src);
        try {
            return TraderRSAUtil.sign(rsa_private, sign_src);
        } catch (Exception e) {
            System.out.println("商户[" + reqObj.getString("oid_partner") + "]RSA加签名异常" + e.getMessage());
            return "";
        }
    }

    /**
     * 读取request流
     * @param req
     * @return
     * @author guoyx
     */
    public static String readReqStr(HttpServletRequest request) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {

            }
        }
        return sb.toString();
    }
}
