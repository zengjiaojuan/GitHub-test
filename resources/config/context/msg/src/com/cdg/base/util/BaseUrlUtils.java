package com.cddgg.base.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取根目录
 * 
 * @author ransheng
 * 
 */
public class BaseUrlUtils {

    /**
     * 获取根目录
     * 
     * @param request
     *            request
     * @return 根目录
     */
    public static String rootDirectory(HttpServletRequest request) {
        String basePath = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath()
                + "/";
        return basePath;
    }
}
