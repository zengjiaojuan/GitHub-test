package com.cddgg.p2p.huitou.spring.aspect;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.cddgg.base.spring.exception.ResponseException;
import com.cddgg.base.spring.exception.ResponseExceptionFactory;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.view.AjaxResponseView;
import com.cddgg.base.view.UrlResponseView;
import com.cddgg.p2p.huitou.constant.ErrorNumber;
import com.cddgg.p2p.huitou.constant.enums.ENUM_PUBLISH_STATE;
import com.cddgg.p2p.huitou.constant.enums.ENUM_SHOW_STATE;
import com.cddgg.p2p.huitou.entity.Product;
import com.cddgg.p2p.huitou.exception.ProductPayException;
import com.cddgg.p2p.huitou.spring.annotation.ProductVerification;

/**
 * 日志记录
 * 
 * @author 刘道冬
 * 
 */
@Aspect
@Component
public class ProductAspect {

    /**
     * HibernateSupport
     */
    @Resource
    private HibernateSupport dao;
    
    /**
     * HibernateSupport
     */
    @Resource
    private AjaxResponseView ajaxView;
    
    /**
     * HibernateSupport
     */
    @Resource
    private UrlResponseView urlView;
    
    /**
     * ResponseExceptionFactory
     */
    @Resource
    ResponseExceptionFactory factory;
    
    /**
     * 执行之前
     * @param jp    切入点
     * @param p 产品
     * @throws ResponseException 
     */
    @Before("within(com.cddgg.p2p.huitou..*) && @annotation(p)")
    public void executeBefore(JoinPoint jp, ProductVerification p) throws ResponseException{
        
        Object[] objs = jp.getArgs();
        
        Product product = dao.get(Product.class,((Product)objs[0]).getId());
        
        if(product==null){//判断产品是否存在
            
            throw factory.bornUrl(ErrorNumber.NO_2, null, null);
        }
        
        if (product.getStatus() == ENUM_PUBLISH_STATE.UNPUBLISH.ordinal()) {// 判断产品是否发布
            throw new ProductPayException("该产品尚未发布，敬请期待！",ajaxView);
        }

        if (product.getShows() == ENUM_SHOW_STATE.FALSE.ordinal()) {// 判断产品是否隐藏
            throw new ProductPayException("该产品已经下架，无法再次购买！",ajaxView);
        }

    }

    
}
