package com.cddgg.base.spring.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cddgg.base.filter.BlackIPFilter;
import com.cddgg.base.model.Visitor;
import com.cddgg.commons.log.LOG;

/**
 * 恶意请求判断服务（测试）
 * 
 * @author 刘道冬
 * 
 */
@Service("venomRequestService")
public class VenomRequestService {

    /**
     * 请求者s
     */
    private Map<String, Visitor> mapVisitor = new HashMap<String, Visitor>();

    /**
     * BlackIPService
     */
    @Resource
    private BlackIPService serviceBlackIp;

    /**
     * 判断请求
     * 
     * @param request
     *            请求
     * @return      是否允许
     */
    public boolean checkRequest(HttpServletRequest request){

        return checkRequestByIP(request);

    }

    /**
     * checkRequestByIP
     * @param request       请求
     * @return              是否允许
     */
    private boolean checkRequestByIP(HttpServletRequest request){

        boolean result = true;

        String ip = request.getRemoteAddr();

        Visitor vis = mapVisitor.get(ip);

        if (vis != null) {

            synchronized (vis) {

                if (mapVisitor.get(ip) == null){
                    return false;
                }
                
                if (vis.isQuickRequest()) {

                    vis = (Visitor) mapVisitor.get(ip);
                    if (vis != null) {

                        // 将该用户加入黑名单
                        serviceBlackIp.autoBlackedIP(ip);
                        mapVisitor.remove(ip);
                        BlackIPFilter.MAP_BLACK_IPS.put(ip, null);
                        result = false;

                    }
                }

            }

        } else {

            // 当前访问者为初次访问
            vis = new Visitor();
            vis.isQuickRequest();

            LOG.debug("--->用户初次访问");

            mapVisitor.put(ip, vis);

        }

        return result;

    }
}
