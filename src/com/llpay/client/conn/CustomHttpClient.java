package com.llpay.client.conn;

/**
 * 
 */

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * 线程安全的HttpCLient,单例模式，支持http、https协议
 * 
 * @author linys
 */
public class CustomHttpClient{
    private static HttpClient customHttpClient          = httpClientInstance();
    private static final int  TIME_OUT                  = 1000 * 60;
    private static final int  MAX_CONNECTIONS_TOTAL     = 200;
    private static final int  MAX_CONNECTIONS_PER_ROUTE = 50;

    private CustomHttpClient() {
    }

    public static HttpClient GetHttpClient() {
        return customHttpClient;
    }

    private static HttpClient httpClientInstance() {
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schReg.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        PoolingClientConnectionManager conMgr = new PoolingClientConnectionManager(schReg);
        conMgr.setMaxTotal(MAX_CONNECTIONS_TOTAL);
        conMgr.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);
        customHttpClient = new DefaultHttpClient(conMgr);
        HttpParams params = customHttpClient.getParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
        HttpConnectionParams.setSoTimeout(params, TIME_OUT);
        return customHttpClient;
    }
}
