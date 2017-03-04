package com.cddgg.base.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;

import com.cddgg.commons.log.LOG;

/**
 * 本地工具
 * @author ldd
 *
 */
public class LocalUtil {

    /**
     * 得到地址
     * @param ip    ip
     * @param key   key
     * @return      地址
     */
    public static String getAddress(String ip, String key) {

        String address = "未知";

        URL url = null;
        try {
            url = new URL("http://api.map.baidu.com/location/ip?ak=" + key
                    + "&ip=" + ip);
        } catch (MalformedURLException e) {
            LOG.error("不能连接到百度服务器", e);
            return address;
        }

        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            LOG.error("不能连接到百度服务器", e);
            return address;
        }

        StringBuffer sb = new StringBuffer();

        BufferedReader br = new BufferedReader(isr);
        String str;
        try {
            while ((str = br.readLine()) != null) {
                sb.append(str.trim());
            }
        } catch (IOException e) {
            LOG.error("从服务器返回的流无法读取!", e);
            return address;
        } finally {
            try {
                br.close();
                isr.close();
            } catch (IOException e) {
                LOG.error("关闭输入流失败！", e);
            }
        }

        if (sb != null && sb.length() > 10) {

            JSONObject json = JSONObject.fromObject(sb.toString());

            if ("0".equals(json.get("status").toString())) {
                json = json.getJSONObject("content");
                json = json.getJSONObject("address_detail");
                address = json.getString("city");
                if (address != null && !"".equals(address)
                        && address.length() > 1) {
                    address = address.substring(0, address.length() - 1);
                }
            }
        }

        return address;

    }

}
