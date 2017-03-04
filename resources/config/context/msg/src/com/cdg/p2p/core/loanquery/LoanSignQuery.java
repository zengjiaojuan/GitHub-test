package com.cddgg.p2p.core.loanquery;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.core.loansignfund.LoanSignFund;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Costratio;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Loansignbasics;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.util.Arith;

/**
 * <p>
 * Title:LoanSignQuery
 * </p>
 * <p>
 * Description: 标的通用查询
 * </p>
 * 
 *         <p>
 *         date 2014年2月14日
 *         </p>
 */
@Service
public class LoanSignQuery {

    /**
     * 通用dao
     */
    @Resource
    HibernateSupport dao;

    /**
     * 通用标查询
     */
    @Autowired
    private LoanSignFund loanSignFund;

    /**
     * 根据sql语句查询出条数
     * 
     * @param sql
     *            执行的sql语句
     * @return 执行结果
     */
    public int queryCount(String sql) {
        Object object = dao.findObjectBySql(sql, null);
        return object != null ? Integer.parseInt(object.toString()) : 0;
    }

    /**
     * 查询到该标的剩余份数
     * 
     * @param loansign
     *            借款标对象
     * @return 剩余份数
     */
    public int queryCopies(Loansign loansign) {
        StringBuffer sb = new StringBuffer(
                "SELECT (ls.issueLoan-(SELECT IFNULL(sum(tenderMoney),0) from loanrecord where isSucceed=1 and  loanSign_id=ls.id))/ls.loanUnit from loansign ls where ls.id=")
                .append(loansign.getId());
        Object obje = dao.findObjectBySql(sb.toString(), null);
        return obje != null ? Integer.parseInt(obje.toString()) : 0;
    }

    /**
     * 求到该用户这期该收多少钱=所得利息
     * 
     * @param repaymentRecord
     *            还款记录对象
     * @param tenderMoney
     *            借款总额
     * @param loansign
     *            借款标对象
     * @param reallyDay
     *            实际使用天数
     * @return 所得利息
     */
    public BigDecimal queryInterest(Repaymentrecord repaymentRecord,
            double tenderMoney, Loansign loansign, Integer reallyDay) {
        BigDecimal interest = new BigDecimal(0.00);
        if (loansign.getLoanType() == Constant.STATUES_ONE
                && loansign.getRefundWay() == Constant.STATUES_ONE) {
            interest = Arith.div(repaymentRecord.getPreRepayMoney()
                    * tenderMoney, loansign.getIssueLoan());
        } else if (loansign.getLoanType() == Constant.STATUES_ONE
                && loansign.getRefundWay() == Constant.STATUES_THERE) {
            if (repaymentRecord.getRepayState() == Constant.STATUES_FIVE) {
                interest = loanSignFund.advanceInterest(new BigDecimal(
                        tenderMoney), loansign.getRate(), reallyDay, loansign
                        .getLoanType());
            } else {
                interest = loanSignFund.instalmentInterest(new BigDecimal(
                        tenderMoney), loansign.getRate(), loansign.getMonth(),
                        loansign.getLoanType());
            }
        } else {
            interest = loanSignFund.instalmentInterest(new BigDecimal(
                    tenderMoney), loansign.getRate(), reallyDay, loansign
                    .getLoanType());
        }
        return interest;
    }

    /**
     * 求到该用户这期该收多少钱=认购金额+所得利息
     * 
     * @param repaymentRecord
     *            回款记录对象
     * @param tenderMoney
     *            借款金额
     * @param loansign
     *            借款标对象
     * @param reallyDay
     *            实际使用天数
     * @return 本息
     */
    public BigDecimal queryMoney(Repaymentrecord repaymentRecord,
            double tenderMoney, Loansign loansign, Integer reallyDay) {
        BigDecimal money = new BigDecimal(0.00);
        if (loansign.getLoanType() == Constant.STATUES_ONE
                && loansign.getRefundWay() == Constant.STATUES_ONE) {
            money = loanSignFund.total(new BigDecimal(tenderMoney),
                    loansign.getRate(), loansign.getMonth());
        } else {
            money = queryInterest(repaymentRecord, tenderMoney, loansign,
                    reallyDay);
            if (repaymentRecord.getPeriods() == loansign.getMonth()
                    || loansign.getLoanType() == Constant.STATUES_TWO
                    || loansign.getLoanType() == Constant.STATUES_THERE) {
                money = money.add(new BigDecimal(tenderMoney));
            }
        }
        return money;
    }

    /**
     * <p>
     * Title: getLoansignById
     * </p>
     * <p>
     * Description: 通过loansign编号查询Loansign
     * </p>
     * 
     * @param loanSignId
     *            借款编号
     * @return 借款标对象
     */
    public Loansign getLoansignById(String loanSignId) {
        try {
            return dao.get(Loansign.class, Long.valueOf(loanSignId));
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * <p>
     * Title: getcreditTime
     * </p>
     * <p>
     * Description: 得到该标的放款时间
     * </p>
     * 
     * @param loansignId
     *            借款id
     * @return 放款时间
     */
    public String getcreditTime(Long loansignId) {
        StringBuffer sb = new StringBuffer(
                "SELECT creditTime from loansignbasics where loansign_id=")
                .append(loansignId);
        return dao.findObjectBySql(sb.toString()).toString();
    }

    /**
     * <p>
     * Title: getLoansignbasicsById
     * </p>
     * <p>
     * Description: 通过loansign编号查询Loansignbasics
     * </p>
     * 
     * @param loanSignId
     *            借款id
     * @return 借款标基础信息
     */
    public Loansignbasics getLoansignbasicsById(String loanSignId) {
        StringBuffer sb = new StringBuffer(
                "From Loansignbasics where loansign.id=").append(loanSignId);
        List<Loansignbasics> lsbList = dao.find(sb.toString());
        return lsbList.size() > 0 ? lsbList.get(0) : null;
    }

    /**
     * <p>
     * Title: getRepaymentByLSId
     * </p>
     * <p>
     * Description: 通过loansign编号查询回款计划（适用于天标 ，秒标和到期一次性还款）
     * </p>
     * 
     * @param loanSignId
     *            借款标号
     * @return 还款计划对象
     */
    public Repaymentrecord getRepaymentByLSId(String loanSignId) {
        StringBuffer sb = new StringBuffer(
                "From Repaymentrecord where loansign.id=").append(loanSignId);
        List<Repaymentrecord> rmList = dao.find(sb.toString());
        return rmList.size() > 0 ? rmList.get(0) : null;
    };

    /**
    * <p>Title: checkRepayOrder</p>
    * <p>Description: 判断是否按期数还款，若未按期数依次还款，返回true，否则返回false</p>
    * @param repay 还款记录
    * @return  成功或失败
    */
    public boolean checkRepayOrder(Repaymentrecord repay) {
        StringBuffer sb = new StringBuffer(
                "select count(1) from repaymentrecord where periods<")
                .append(repay.getPeriods())
                .append(" and repayState=1 and  loanSign_id=")
                .append(repay.getLoansign().getId());
        Object object=dao.findObjectBySql(sb.toString()).toString();
        return Integer.parseInt(object.toString()) > 0?true:false;
    }

    /**
    * <p>Title: isFull</p>
    * <p>Description: 判断该借款标是否满标</p>
    * @param loansignId 借款编号
    * @return boolean
    */
    public boolean isFull(String loansignId) {
        StringBuffer sb = new StringBuffer(
                "SELECT  ls.issueLoan-(SELECT SUM(tenderMoney) from  loanrecord where isSucceed=1 and loansign_id=ls.id) FROM loansign ls where  ls.id=")
                .append(loansignId);
        Object object = dao.findObjectBySql(sb.toString());
        return object != null ? (Double.valueOf(object.toString()) == 0 ? true
                : false) : false;
    }

    /**
    * <p>Title: queryCostratio</p>
    * <p>Description:   查询到平台当前的费用比例</p>
    * @return  Costratio对象
    */
    public Costratio queryCostratio() {
        List<Costratio> costratioList = (List<Costratio>) dao
                .find("From Costratio");
        return costratioList.size() == 1 ? costratioList.get(0) : null;
    };

    /**
    * <p>Title: isExceed</p>
    * <p>Description:  判断该标是否逾期</p>
    * @param loansignId  借款编号
    * @return true or false
    */
    public boolean isExceed(long loansignId) {
        StringBuffer sb = new StringBuffer(
                "SELECT COUNT(1) from repaymentrecord where (repayState=3||repayState=4) and  loanSign_id=")
                .append(loansignId);
        Object object = dao.findObjectBySql(sb.toString());
        return object != null ? (Integer.parseInt(object.toString()) > 0 ? true
                : false) : false;
    }
    /**
     * 获取该标的购买金额的总和
     * @param id 标号
     * @return 借款金额
     */
    public Double getLoanrecordMoneySum(Long id){
    	String sql = "select SUM(tenderMoney) from loanrecord where loanSign_id=?";
    	Double money = dao.queryNumberSql(sql, id);
    	if(null!=money){
    		return money;
    	}
    	return 0.00;
    }
    
    /**
     * 获取未还款的还款记录
     * @return
     */
    public List<Repaymentrecord> getRepaymentList(Long loanid){
    	String hql = "from Repaymentrecord r where r.loansign.id=? and r.repayState=1";
    	return dao.find(hql,loanid);
    }
    /**
     * 
     * @return
     */
    public List<Repaymentrecord> getRepaymentList(Long loanid,String ipsNumber){
    	String hql = "from Repaymentrecord r where r.loansign.id=? and r.pIpsBillNo=?";
    	return dao.find(hql,loanid,ipsNumber);
    }
}
