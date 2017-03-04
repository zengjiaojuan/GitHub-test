package com.cddgg.p2p.huitou.exception;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cddgg.base.spring.exception.ResponseException;
import com.cddgg.base.spring.exception.ResponseExceptionFactory;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.AnnotationUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.constant.enums.ENUM_FINANCIAL_EXCEPTION;
import com.cddgg.p2p.huitou.entity.ExceptionNoteInfo;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.service.EmailService;

/**
 * 资金异常记录者
 * @author ldd
 *
 */
@Component
public class FinancialExceptionNotes {

    /**
     * EmailService
     */
    @Resource
    private EmailService serviceEmail;
    
    /**
     * ResponseExceptionFactory
     */
    @Resource
    private ResponseExceptionFactory factory;
    
    /**
     * HibernateSupport
     */
    @Resource
    private HibernateSupport dao;
    
    /**
     * 记录
     * @param type  类型
     * @param remark    备注
     * @param user  用户
     * @param exchange  流动资金
     * @param balance   余额
     * @param json  json
     * @throws ResponseException 响应式异常
     */
    public void note(ENUM_FINANCIAL_EXCEPTION type,String remark,Userbasicsinfo user,String exchange,String balance,String json) throws ResponseException{
        
        user = dao.get(Userbasicsinfo.class, user.getId());
        
        ExceptionNoteInfo info = new ExceptionNoteInfo();
        
        info.setCurTime(DateUtils.formatSimple());
        info.setErrorType(AnnotationUtil.getFieldConfigValue(type));
        info.setMoneyExchange(exchange);
        info.setRemark(remark);
        info.setStatus(0);
        info.setUserCurBalance(String.valueOf(user.getUserfundinfo().getCashBalance()));
        info.setUserAimBalance(balance);
        info.setUserIps(user.getUserfundinfo().getpIdentNo());
        info.setUserLoginName(user.getUserName());
        info.setUserRealName(user.getName());
        
        dao.save(info);//向数据库插入数据
        
        serviceEmail.sendEmail(Constant.PROJECT_NAME+"-异常邮件", info.toString(), Constant.SYSTEM_EXCEPTION_RECEIVE);
        
        throw factory.born(type.toString(), AnnotationUtil.getFieldConfigValue(type)+"失败！",null, null, remark, json);
    }
}
