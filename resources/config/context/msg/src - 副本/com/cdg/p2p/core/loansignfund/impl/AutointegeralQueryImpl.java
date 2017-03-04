package com.cddgg.p2p.core.loansignfund.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.core.loansignfund.AutointegralQuery;
import com.cddgg.p2p.core.loansignfund.BorrowersQuery;
import com.cddgg.p2p.huitou.entity.Manualintegral;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;

/**
 * 
 * 积分通用接口的查询的实现
 * 
 * @author Administrator
 * 
 */
@Service
public class AutointegeralQueryImpl implements AutointegralQuery {

    /** dao */
    @Resource
    private HibernateSupport dao;

    /** borrowersQuery */
    @Autowired
    private BorrowersQuery borrowersQuery;

    /**
     * 通过还款金额求出对应的积分值
     * 
     * @param money  还款金额
     * @return 积分
     */
    public int calculationIntegral(double money) {
        return (int) money / 100;
    }

    /*****
     * 通过管理员勾选的信息，进行计算手动积分合计
     * 
     * @param manualin  手动积分对象
     * @return 积分合计
     */
    public int getALLBYOneSerch(Manualintegral manualin) {
        int amountpoints = 0;
        if (manualin.getCkVaule() != null
                && !"".endsWith(manualin.getCkVaule())) {
            String[] mastring = manualin.getCkVaule().split(",");
            String[] lmastring = null;
            for (int i = 0; i < mastring.length; i++) {
                lmastring = mastring[i].split("-");
                amountpoints += lmastring[1] != null ? Integer
                        .parseInt(lmastring[1] + "") : 0;
            }
        }
        amountpoints += manualin.getHouseCardPoints() != null ? manualin
                .getHouseCardPoints() : 0;
        amountpoints += manualin.getBankWaterPoints() != null ? manualin
                .getBankWaterPoints() : 0;
        amountpoints += manualin.getSocialPoints() != null ? manualin
                .getSocialPoints() : 0;
        amountpoints += manualin.getCreditCardPoints() != null ? manualin
                .getCreditCardPoints() : 0;
        amountpoints += manualin.getSalesContractInvoicePoints() != null ? manualin
                .getSalesContractInvoicePoints() : 0;
        return amountpoints;
    }
    /**
     * 通过会员编号查询到会员的积分总额 =自动积分总和+手动积分
     * 
     * @param userbasicsinfo 用户对象
     * @return 积分总额
     */
    public int queryAllIntegral(Userbasicsinfo userbasicsinfo) {
        int allIntegral = 0;

        if (borrowersQuery.isBorrowsByUser(userbasicsinfo)) {
            allIntegral = queryAutoSUMIntegral(userbasicsinfo);
            StringBuffer sb = new StringBuffer(
                    "SELECT amountPoints  from manualintegral ate where ate.user_id=")
                    .append(userbasicsinfo.getId());
            Object object1 = dao.findObjectBySql(sb.toString());
            allIntegral += object1 != null ? Integer.parseInt(object1
                    .toString()) : 0;
        }
        return allIntegral;
    }
    /**
     * 求到该用户的自动积分总和
     * 
     * @param userbasicsinfo 用户对象
     * @return 积分总和
     */
    public int queryAutoSUMIntegral(Userbasicsinfo userbasicsinfo) {
        StringBuffer sb = new StringBuffer(
                "select SUM(realityintegral) from autointegral where user_id=")
                .append(userbasicsinfo.getId());
        Object object = dao.findObjectBySql(sb.toString());
        return object != null ? Integer.parseInt(object.toString()) : 0;
    }
    /**
     * 通过用户找到他的手动积分记录
     * 
     * @param userbasicsinfo 用户对象
     * @return 积分
     */
    public Manualintegral queryManuaByuser(Userbasicsinfo userbasicsinfo) {

        StringBuffer sb = new StringBuffer(
                "select * from manualintegral where user_id=")
                .append(userbasicsinfo.getId());
        List<Manualintegral> mlist = dao.findBySql(sb.toString(),
                Manualintegral.class, null);
        if (mlist.size() > 0) {
            return mlist.get(0);
        }
        return null;
    }

}
