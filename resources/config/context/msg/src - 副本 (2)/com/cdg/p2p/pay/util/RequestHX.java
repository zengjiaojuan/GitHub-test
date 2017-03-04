package com.cddgg.p2p.pay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 请求环讯
 * @author RanQiBing 2014-01-06
 *
 */
public class RequestHX {
	/**
	 * 将平台信息发送给环讯
	 * @param reqUrl 返回地址
	 * @param parameters 信息集合
	 * @throws UnsupportedEncodingException 
	 */
	public static void sendPostRequest(String reqUrl, Map<String,String> parameters) throws UnsupportedEncodingException{

        //将参数拼接成起来
		StringBuffer params = new StringBuffer();
        for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();){
            Entry<String, String> element = (Entry<String, String> ) iter.next();
            params.append(element.getKey().toString());
            params.append("=");
            params.append(URLEncoder.encode(element.getValue().toString(),"UTF-8"));
            params.append("&");
        }
        if (params.length() > 0){
            params = params.deleteCharAt(params.length() - 1);
        }
        try {

            URL url = new URL(reqUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            writer.write(params.toString());
            writer.flush();

            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
