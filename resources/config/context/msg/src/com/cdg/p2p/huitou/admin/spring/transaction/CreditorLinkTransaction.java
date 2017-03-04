package com.cddgg.p2p.huitou.admin.spring.transaction;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.cddgg.base.spring.exception.ResponseException;
import com.cddgg.base.spring.exception.ResponseExceptionFactory;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.huitou.constant.ErrorNumber;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;

/**
 * 债权关联表长事务
 * @author ldd
 *
 */
@Service
public class CreditorLinkTransaction {

    /**
     * HibernateSupport
     */
    @Resource
    HibernateSupport dao;
    
    /**
     * ResponseExceptionFactory
     */
    @Resource
    ResponseExceptionFactory factory;
    
    /**
     * 更新债权关联表,释放锁定
     * @param creditorLinkIds   债权关联
     * @param moneys            需要被扣除的金额
     * @param user  锁定用户
     * @throws ResponseException 更新债权信息失败，购买失败！
     */
    public void updateCreditorLink(Long[] creditorLinkIds,Double[] moneys,Userbasicsinfo user) throws ResponseException{
        
        Session session = dao.getSession();
        
        //该修改存在触发器，触发器用于维护balance是否小于0,当小于零时数据库会抛出一个查询异常
        for(int i=0;i<creditorLinkIds.length;i++){
            
            //是否修改成功
            if(dao.fillQuery(session.createSQLQuery("UPDATE creditor_link a SET a.balance=(a.balance-?),a.time_frost=null,a.id_frost=null WHERE a.id=? AND a.id_frost=?"),moneys[i],creditorLinkIds[i],user.getId()).executeUpdate()!=1){
                
                throw factory.bornAjax(ErrorNumber.NO_9);
            }
            
        }
        
    }
}
