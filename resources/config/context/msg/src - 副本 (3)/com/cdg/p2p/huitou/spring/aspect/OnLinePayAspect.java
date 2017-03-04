package com.cddgg.p2p.huitou.spring.aspect;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.cddgg.base.spring.exception.ResponseException;
import com.cddgg.base.spring.exception.ResponseExceptionFactory;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.view.AjaxResponseView;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.constant.ErrorNumber;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.exception.ProductPayException;
import com.cddgg.p2p.huitou.spring.annotation.OnLinePayVerification;

/**
 * 日志记录
 * 
 * @author 刘道冬
 * 
 */
@Aspect
@Component
public class OnLinePayAspect {

    /**
     * HibernateSupport
     */
    @Resource
    private HibernateSupport dao;

    /**
     * ResponseExceptionFactory
     */
    @Resource
    ResponseExceptionFactory factory;

    /**
     * AjaxResponseView
     */
    @Resource
    private AjaxResponseView view;

    /**
     * 执行之前
     * 
     * @param jp
     *            切入点
     * @param p
     *            产品
     * @throws ResponseException
     *             响应式异常
     */
    @Before("within(com.cddgg.p2p.huitou..*) && @annotation(p)")
    public void executeBefore(JoinPoint jp, OnLinePayVerification p)
            throws ResponseException {

        Object[] objs = jp.getArgs();

        double money = (Double) objs[1];

        Userbasicsinfo user = null;

        if (objs[2] instanceof HttpServletRequest) {
            user = (Userbasicsinfo) ((HttpServletRequest) objs[2]).getSession()
                    .getAttribute(Constant.SESSION_USER);
        } else if (objs[2] instanceof Userbasicsinfo) {
            user = (Userbasicsinfo) objs[2];
        } else {

            throw factory.bornAjax(ErrorNumber.NO_3);
        }

        if (user.getUserfundinfo().getCashBalance() < money) {
            throw new ProductPayException("您的余额不足 " + money
                    + " ,请充值后在购买该产品！<br/>当前可用余额:"
                    + user.getUserfundinfo().getCashBalance(), view);
        }

    }

}
