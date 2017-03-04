package com.cddgg.base.spring.quartz;

import javax.annotation.Resource;

import com.cddgg.base.spring.service.BlackIPService;

/**
 * 黑名单刷新定时器（测试）
 * 
 * @author ldd
 * 
 */
public class BlackIPQuartz {

    /**
     * 注入黑名单服务
     */
    @Resource
    private BlackIPService serviceBlackIps;

    /**
     * 运行
     */
    public void run() {

        serviceBlackIps.init();

    }

}
