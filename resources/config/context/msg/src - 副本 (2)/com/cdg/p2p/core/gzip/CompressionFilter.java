package com.cddgg.p2p.core.gzip;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**   
 * Filename:    CompressionFilter.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年4月9日 下午2:22:31   
 * Description:  
 *   
 */

public class CompressionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        boolean compress = false;
        if (request instanceof HttpServletRequest) {
            Enumeration headers = httpRequest.getHeaders("Accept-Encoding");
            while (headers.hasMoreElements()) {
                String value = (String) headers.nextElement();
                if (value.indexOf("gzip") != -1) {
                    compress = true;
                }
            }
        }
        if (compress) {// 如果浏览器支持则压缩
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.addHeader("Content-Encoding", "gzip");
            httpResponse.setCharacterEncoding("UTF-8");
            CompressionResponse compressionResponse = new CompressionResponse(
                    httpResponse);
            
            compressionResponse.setCharacterEncoding("UTF-8");
            chain.doFilter(request, compressionResponse);
            compressionResponse.close();
        } else {// 如果浏览器不支持则不压缩
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
