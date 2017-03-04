package com.cddgg.p2p.huitou.spring.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.huitou.spring.annotation.Logrecord;

/**
 * 日志记录
 * 
 * @author 刘道冬
 * 
 */
@Aspect
@Component
public class JournalDayAspect {

    /**
     * 执行成功
     * 
     * @param jp
     *            切入点
     * @param log
     *            日志
     */
    @AfterReturning("within(com.cddgg.p2p..*) && @annotation(log)")
    public void executeSuccessful(JoinPoint jp, Logrecord log) {
        LOG.info("ol.........cg");
    }

    /**
     * 发生异常
     * 
     * @param jp
     *            切入点
     * @param log
     *            日志
     * @param ex
     *            异常
     */
    @AfterThrowing(pointcut = "within(com.cddgg.p2p..*) && @annotation(log)", throwing = "ex")
    public void executeUnSuccessful(JoinPoint jp, Logrecord log, Exception ex) {
        LOG.info("ol.........ex");
    }
}
