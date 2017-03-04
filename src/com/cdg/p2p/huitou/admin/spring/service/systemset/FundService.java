package com.cddgg.p2p.huitou.admin.spring.service.systemset;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;

/**
 * 资金统计
 * 
 * @author ransheng
 * 
 */

@Service
@SuppressWarnings(value = { "rawtypes" })
public class FundService {

    /**
     * 通用dao
     */
    @Resource
    private HibernateSupport commonDao;

    /**
     * 得到流水号类型
     * 
     * @return 资金类型
     */
    public List quryType() {
        String sql = "SELECT type.id,type.name FROM accountinfo acinfo,accounttype type "
                + "WHERE acinfo.accounttype_id=type.id GROUP BY type.id";
        List list = commonDao.findBySql(sql);
        return list;
    }

    /**
     * 查询条数
     * 
     * @param beginDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @param typeid
     *            资金类型编号
     * @return 资金流动信息
     */
    public Object getcount(String beginDate, String endDate, String typeid) {
        StringBuilder sql = new StringBuilder(
                "SELECT count(1) FROM accountinfo info,accounttype type WHERE  info.accounttype_id=type.id");

        if (!"".equals(typeid.trim())) {
            sql.append(" AND info.accounttype_id =" + typeid);
        }
        if (beginDate != null && !"".equals(beginDate.trim())) {
            sql.append(" AND DATE_FORMAT(info.time, '%Y-%m-%d %H:%i:%s')>=DATE_FORMAT('"
                    + beginDate + "', '%Y-%m-%d %H:%i:%s')");
        }
        if (endDate != null && !"".equals(endDate.trim())) {
            sql.append(" AND DATE_FORMAT(info.time, '%Y-%m-%d %H:%i:%s')<=DATE_FORMAT('"
                    + endDate + "', '%Y-%m-%d %H:%i:%s')");
        }

        Object obj = commonDao.findObjectBySql(sql.toString());
        return obj;
    }

    /**
     * 流水明细
     * 
     * @param beginDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @param typeid
     *            流水账号类型编号
     * @param page
     *            分页对象
     * @return 资金流动信息
     */
    public List queryAccountinfo(String beginDate, String endDate,
            String typeid, PageModel page) {
        String sql = "SELECT info.id,DATE_FORMAT(info.time, '%Y-%m-%d %H:%i:%s'),type.`name`,info.expenditure,"
                + " info.income,IFNULL(info.money,0) FROM accountinfo info, accounttype type WHERE info.accounttype_id= type.id ";
        if (!"".equals(typeid.trim())) {
            sql = sql + " AND info.accounttype_id =" + typeid;
        }
        if (beginDate != null && !"".equals(beginDate.trim())) {
            sql = sql
                    + " AND DATE_FORMAT(info.time, '%Y-%m-%d %H:%i:%s')>=DATE_FORMAT('"
                    + beginDate + "', '%Y-%m-%d %H:%i:%s')";
        }
        if (endDate != null && !"".equals(endDate.trim())) {
            sql = sql
                    + " AND DATE_FORMAT(info.time, '%Y-%m-%d %H:%i:%s')<=DATE_FORMAT('"
                    + endDate + "', '%Y-%m-%d %H:%i:%s')";
        }
        sql = sql + " ORDER BY info.id DESC  LIMIT " + page.firstResult() + ","
                + page.getNumPerPage();
        List list = commonDao.findBySql(sql);
        return list;
    }

    /**
     * 资金明细
     * 
     * @return 将查询到的值保存为数组返回
     */
    public Object[] queryBasis() {
        Object[] obj = new Object[10];
        // 会员统计
        String sql = "select count(*) from userbasicsinfo";// 会员总数
        Object userCount = commonDao.findObjectBySql(sql);
        obj[0] = userCount;
        sql = "SELECT COUNT(DISTINCT vinfo.user_id) FROM vipinfo vinfo WHERE "
                + " DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i:%s')< DATE_FORMAT(vinfo.endtime,'%Y-%m-%d %H:%i:%s')";
        Object vipCount = commonDao.findObjectBySql(sql);// vip会员
        obj[1] = vipCount;

        // 投资者收益统计
        sql = "SELECT IFNULL(SUM(tenderMoney),0) FROM loanrecord WHERE isSucceed=1";// 成功借出总额
        Object successTotal = commonDao.findObjectBySql(sql);
        obj[2] = successTotal;
        sql = "SELECT IFNULL(SUM(income),0) FROM accountinfo  WHERE accounttype_id=9";// 成功借款投标奖励总额
        Object tenderAwardsTotal = commonDao.findObjectBySql(sql);
        obj[3] = tenderAwardsTotal;
        sql = "SELECT IFNULL(SUM(income),0) FROM accountinfo  WHERE accounttype_id=6";// 推广奖金总额
        Object spreadTotal = commonDao.findObjectBySql(sql);
        obj[4] = spreadTotal;

        // 网站收益统计
        sql = "SELECT  IFNULL(sum(expenditure),0) FROM accountinfo a WHERE explan='管理费'";// 投资者管理费用
        Object investmentManagementFees = commonDao.findObjectBySql(sql);
        obj[5] = investmentManagementFees;
        sql = "SELECT IFNULL(sum(expenditure),0) FROM accountinfo a WHERE  explan='借款人管理费'";// 借款者管理费用
        Object borrowingManagementFees = commonDao.findObjectBySql(sql);
        obj[6] = borrowingManagementFees;
        sql = "SELECT IFNULL(SUM(type.money),0) FROM vipinfo v,viptype type WHERE v.viptype_id=type.id";// 特权会员总额
        Object vipTotalFees = commonDao.findObjectBySql(sql);
        obj[7] = vipTotalFees;

        // 资金进出统计
        sql = "SELECT IFNULL(SUM(rechargeAmount),0) FROM recharge";// 线上充值
        Object onLineRecharge = commonDao.findObjectBySql(sql);
        obj[8] = onLineRecharge;
        sql = "SELECT ROUND(IFNULL(SUM(withdrawAmount),0),2) FROM withdraw";// 成功提现
        Object succcessWithdraw = commonDao.findObjectBySql(sql);
        obj[9] = succcessWithdraw;

        return obj;
    }

    /**
     * 分类统计标四中类型的信息
     * 
     * @param id
     *            类型 1、助人贷，2、助企贷 3、企业群联保贷 4、投资人周转贷
     * @return 数组
     */
    public Object[] queryBasisType(Long id) {
        Object[] obj = new Object[7];
        String sql = "SELECT IFNULL(SUM(a.issueLoan),0) FROM loansign a WHERE a.loansignType_id=?  and a.loanstate>2";
        obj[0] = commonDao.findObjectBySql(sql, id);// 借出总金额
        sql = "SELECT IFNULL(SUM(b.money),0) FROM loansign a,repaymentrecord b WHERE a.loansignType_id=? "
                + "AND a.id=b.loanSign_id AND (b.repayState=2 OR repayState=4 OR repayState=5)";// 已还金额(不包括利息)
        obj[1] = commonDao.findObjectBySql(sql, id);
        sql = "SELECT IFNULL(SUM(b.money),0) FROM loansign a,repaymentrecord b WHERE a.loansignType_id=? "
                + "AND a.id=b.loanSign_id AND (b.repayState=1 OR repayState=3)";// 未还金额(不包括利息)
        obj[2] = commonDao.findObjectBySql(sql, id);
        sql = "SELECT IFNULL(SUM(b.realMoney),0) FROM loansign a,repaymentrecord b WHERE a.loansignType_id=? "
                + "AND a.id=b.loanSign_id AND (b.repayState=2 OR repayState=4 OR repayState=5)";// 已支付利息
        obj[3] = commonDao.findObjectBySql(sql, id);
        sql = "SELECT IFNULL(SUM(b.preRepayMoney),0) FROM loansign a,repaymentrecord b WHERE a.loansignType_id=? "
                + "AND a.id=b.loanSign_id AND (b.repayState=1 OR repayState=3)";// 未支付利息
        obj[4] = commonDao.findObjectBySql(sql, id);
        sql = "SELECT IFNULL(SUM(b.preRepayMoney)+SUM(b.money),0) FROM loansign a,repaymentrecord b WHERE "
                + "a.loansignType_id=? AND a.id=b.loanSign_id AND b.repayState=4";// 逾期还款总额
        obj[5] = commonDao.findObjectBySql(sql, id);
        sql = "SELECT IFNULL(SUM(b.money + b.realMoney +b.overdueInterest),0) FROM loansign a,repaymentrecord b WHERE "
                + "a.loansignType_id=? AND a.id=b.loanSign_id AND b.repayState=5";// 提前还款总额
        obj[6] = commonDao.findObjectBySql(sql, id);
        return obj;
    }

    /**
     * 统计秒标信息
     * 
     * @return 数组
     */
    public Object[] queryBasisSecond() {
        Object[] obj = new Object[7];
        String sql = "SELECT IFNULL(SUM(a.issueLoan),0) FROM loansign a WHERE a.loanType=3  and a.loanstate>2";
        obj[0] = commonDao.findObjectBySql(sql);// 借出总金额
        sql = "SELECT IFNULL(SUM(b.money),0) FROM loansign a,repaymentrecord b WHERE a.loanType=3 "
                + "AND a.id=b.loanSign_id AND (b.repayState=2 OR repayState=4 OR repayState=5)";// 已还金额(不包括利息)
        obj[1] = commonDao.findObjectBySql(sql);
        sql = "SELECT IFNULL(SUM(b.money),0) FROM loansign a,repaymentrecord b WHERE a.loanType=3 "
                + "AND a.id=b.loanSign_id AND (b.repayState=1 OR repayState=3)";// 未还金额(不包括利息)
        obj[2] = commonDao.findObjectBySql(sql);
        sql = "SELECT IFNULL(SUM(b.realMoney),0) FROM loansign a,repaymentrecord b WHERE a.loanType=3 "
                + "AND a.id=b.loanSign_id AND (b.repayState=2 OR repayState=4 OR repayState=5)";// 已支付利息
        obj[3] = commonDao.findObjectBySql(sql);
        sql = "SELECT IFNULL(SUM(b.preRepayMoney),0) FROM loansign a,repaymentrecord b WHERE a.loanType=3 "
                + "AND a.id=b.loanSign_id AND (b.repayState=1 OR repayState=3)";// 未支付利息
        obj[4] = commonDao.findObjectBySql(sql);
//        sql = "SELECT IFNULL(SUM(b.preRepayMoney)+SUM(b.money),0) FROM loansign a,repaymentrecord b WHERE "
//                + "a.loanType=3 AND a.id=b.loanSign_id AND b.repayState=4";// 逾期还款总额
//        obj[5] = commonDao.findObjectBySql(sql);
//        sql = "SELECT IFNULL(SUM(b.preRepayMoney)+SUM(b.money),0) FROM loansign a,repaymentrecord b WHERE "
//                + "a.loanType=3 AND a.id=b.loanSign_id AND b.repayState=5";// 提前还款总额
//        obj[6] = commonDao.findObjectBySql(sql);
        return obj;
    }
}
