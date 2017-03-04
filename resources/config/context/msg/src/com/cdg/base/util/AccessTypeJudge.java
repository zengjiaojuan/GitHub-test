package com.cddgg.base.util;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Title:AccessTypeJudge
 * </p>
 * <p>
 * Description: 判断用户的请求是否是ajax请求
 * </p>
 * 
 *         <p>
 *         date 2014年2月13日
 *         </p>
 */
public class AccessTypeJudge {

    /**
     * <p>
     * Title: isAjax
     * </p>
     * <p>
     * Description: 判断客户端发送的是否是ajax请求
     * </p>
     * 
     * @param request
     *            HttpServletRequest
     * @return <font color="red">true</font>表示是ajax请求<br/><font color="red">flase</font>表示不是ajax请求
     */
    public static boolean isAjax(HttpServletRequest request) {

        boolean result=false;
        if (request != null
                && "XMLHttpRequest".equalsIgnoreCase(request
                        .getHeader("X-Requested-With"))) {
            result= true;
        }

        return result;

    }

}
