package com.cddgg.p2p.huitou.spring.service.invest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cddgg.p2p.huitou.spring.service.borrow.BorrowRecordService;
import com.cddgg.p2p.huitou.spring.service.borrow.RepayPlanService;
import com.cddgg.p2p.huitou.spring.util.Arith;
import com.cddgg.p2p.huitou.spring.util.LoanNew;
import com.cddgg.p2p.huitou.util.BasicData;
import com.cddgg.p2p.huitou.util.LoanUncollected;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.normal.Validate;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.CalculateLoan;
import com.cddgg.p2p.huitou.entity.Loanrecord;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.core.loansignfund.LoanSignFund;
import com.cddgg.p2p.core.service.LoanrecordService;
import com.cddgg.p2p.core.userinfo.UserInfoQuery;

@Service
public class InvestService {
    /**
     * 注入HibernateSupport
     */
    @Resource
    private HibernateSupport dao;
    /**
     * 注入BorrowRecordService
     */
    @Resource
    private BorrowRecordService borrowrecord;
    /**
     * 注入UserInfoQuery
     */
    @Resource
    private UserInfoQuery userInfoQuery;
    /**
     * 注入RepayPlanService
     */
    @Resource
    private RepayPlanService repayPlanService;

    @Resource
    private LoanSignFund signFund;

    /**
     * 注入HibernateSupport
     */
    @Resource
    private LoanrecordService loanrecordService;

    public List<Loanrecord> getLoanRecord(int state, Long userid, int no,
            int states) {
        // 根据用户和标状态查新投资标信息
        String sql = "select * from loanrecord dlr where dlr.userbasicinfo_id="
                + userid + " and dlr.Loansign_id in (select ls.id from "
                + " Loansign ls where ls.loanstate=" + state + ")";
        // 当状态为0时，查询用户所有的投标信息
        if (states == 1) {
            sql = "select * from loanrecord dlr where dlr.userbasicinfo_id="
                    + userid + " and dlr.Loansign_id in (select ls.id from "
                    + " Loansign ls where ls.loanstate=" + state
                    + " and ls.loanType!=4)";
        } else if (states == 2) {
            sql = "select * from loanrecord dlr where dlr.userbasicinfo_id="
                    + userid
                    + " and dlr.Loansign_id in (select ls.id from "
                    + " Loansign ls where ls.loanstate="
                    + state
                    + " or (ls.loanType=4 and ls.loanstate=2 or ls.loanstate=3))";
        }
        if (0 == state) {
            sql = "select * from loanrecord dlr where dlr.userbasicinfo_id="
                    + userid;
        }
        sql = sql + " LIMIT " + no + ",10";
        List<Loanrecord> loanrecordlist = dao.findBySql(sql, Loanrecord.class);

        return loanrecordlist;
    }

    /**
     * 借款标详情
     * 
     * @param loanlist
     * @return
     * @throws ParseException 
     */
    public List<Object> getLoanGlF(List<Object> loanlist) throws ParseException {
        List<Object> loanuplist = new ArrayList<Object>();
        for (int i = 0; i < loanlist.size(); i++) {
            Loanrecord loanRecord = (Loanrecord) loanlist.get(i);
            Loansign Loansign = loanRecord.getLoansign();
            LoanNew loannew = new LoanNew();
            //借款标id
            loannew.setLoanId(loanRecord.getLoansign().getId());
            // 借款标号
            loannew.setLoanNumber(loanRecord.getLoansign().getLoansignbasics()
                    .getLoanNumber());
            // 借款标题
            loannew.setLoanTitle(loanRecord.getLoansign().getLoansignbasics()
                    .getLoanTitle());

            //判断是否是进行中的标
            if(Loansign.getLoanstate() == 2 && null != Loansign.getEndTime()){
                if(Loansign.getLoanType() == 3){//秒标
                    loannew.setLastDays("满标回放");//剩余标期
                }else{
                    int days = DateUtils.differenceDate("yyyy-MM-dd", DateUtils.format("yyyy-MM-dd"), Loansign.getEndTime());
                    loannew.setLastDays(days > 0 ? days+"天" : "0天");//剩余标期
                }
             }
            
            // 借款者
            if (loanRecord.getLoansign().getLoanType() == 1
                    || loanRecord.getLoansign().getLoanType() == 2
                    || loanRecord.getLoansign().getLoanType() == 3
                    || loanRecord.getLoansign().getLoanType() == 4) {
                loannew.setBorrowername(loanRecord.getLoansign()
                        .getUserbasicsinfo().getUserName());
            } else {
                loannew.setBorrowername("");
            }

            // 标的金额
            loannew.setIssueLoan(loanRecord.getLoansign().getIssueLoan());

            // 利率
            loannew.setInterestRate(loanRecord.getLoansign().getRate());
            // 投标金额
            loannew.setTenderMoney(loanRecord.getTenderMoney());

            // 投标日期
            loannew.setTenderTime(loanRecord.getTenderTime());

            // 进度
            double per = loanrecordService.getLoanrecordSum(Loansign.getId())
                    / Loansign.getIssueLoan();
            if (per >= 1) {
                loannew.setSchedule(100);
            } else {
                loannew.setSchedule(Math.ceil(per * 100));
            }

            // 状态
            if (Loansign.getLoanstate() == 2) {
                loannew.setStatus("进行中");
            } else if (Loansign.getLoanstate() == 3) {
                loannew.setStatus("回款中");
            } else if (Loansign.getLoanstate() == 4) {
                loannew.setStatus("已完成");
            }

            // 借款金额
            loannew.setMoney(loanRecord.getLoansign().getIssueLoan());
            // 投资收益
            double tzsy = 0.00;
            // 管理费
            double tzyj = 0.00;
            if (loanRecord.getLoansign().getLoanType() == 1) {
                tzyj = loanRecord.getTenderMoney()
                        * loanRecord.getLoansign().getRate() / 12
                        * loanRecord.getLoansign().getMonth();

            } else if (loanRecord.getLoansign().getLoanType() == 2) {
                tzyj = loanRecord.getTenderMoney()
                        * loanRecord.getLoansign().getRate()
                        * loanRecord.getLoansign().getUseDay();
            } else if (loanRecord.getLoansign().getLoanType() == 4) {
                tzyj = loanRecord.getTenderMoney()
                        * loanRecord.getLoansign().getRate() / 365
                        * loanRecord.getLoansign().getMonth();
            } else {
                tzyj = loanRecord.getTenderMoney()
                        * loanRecord.getLoansign().getRate();
            }
            loannew.setTzyj(borrowrecord.round(tzyj, 2) + "");
            // 期限
            if (loanRecord.getLoansign().getLoanType() == 1) {
                loannew.setDeadline(loanRecord.getLoansign().getMonth() + "月");
                tzsy = loanRecord.getTenderMoney()
                        * loanRecord.getLoansign().getRate() / 12
                        * loanRecord.getLoansign().getMonth();
            } else if (loanRecord.getLoansign().getLoanType() == 2) {
                //借款标状态为回款中，并且预计使用天数不为空
                if(loanRecord.getLoansign().getLoanstate() == 3 && loanRecord.getLoansign().getUseDay() != null){
                    tzsy = loanRecord.getTenderMoney()
                            * loanRecord.getLoansign().getRate()
                            * loanRecord.getLoansign().getUseDay();
                }else if (Loansign.getLoanstate() == 4 && loanRecord.getLoansign().getRealDay() != null) {
                    loannew.setDeadline(loanRecord.getLoansign().getRealDay()
                            + "天");
                    tzsy = loanRecord.getTenderMoney()
                            * loanRecord.getLoansign().getRate()
                            * loanRecord.getLoansign().getRealDay();
                } else {
                    loannew.setDeadline(loanRecord.getLoansign().getUseDay()
                            + "天");
                    tzsy = loanRecord.getTenderMoney()
                            * loanRecord.getLoansign().getRate()
                            * loanRecord.getLoansign().getUseDay();
                }
            } else if (loanRecord.getLoansign().getLoanType() == 4) {
                loannew.setDeadline(loanRecord.getLoansign().getMonth() + "月");
                tzsy = loanRecord.getTenderMoney()
                        * loanRecord.getLoansign().getRate() / 365
                        * loanRecord.getLoansign().getMonth();
            } else {
                loannew.setDeadline("满标回款");
                tzsy = loanRecord.getTenderMoney()
                        * loanRecord.getLoansign().getRate();
            }
            //投资金额所占借款金额比例
            double bl = loanRecord.getTenderMoney()
                    / loanRecord.getLoansign().getIssueLoan();
            //查询该标实际还款利息
            String sql = "SELECT SUM(realMoney) FROM repaymentrecord WHERE loanSign_id="+loanRecord.getLoansign().getId();
            List list = dao.findBySql(sql);
            //实际还款利息
            double realInterest = 0.00;
            if(list.size()>0 && list.get(0) != null){
                realInterest = Double.valueOf(list.get(0)+"")*bl;
            }
            //实际还款本息
            loannew.setRealTotal(loanRecord.getTenderMoney()+realInterest);
            // 应收本息
            if (loanRecord.getLoansign().getLoanType() == 4) {
                loannew.setPrincipalandInterest(getinterest(loanRecord)
                        + loanRecord.getTenderMoney() + "");
            } else if (loanRecord.getLoansign().getLoanType() == 1) {
                if (loanRecord.getLoansign().getRefundWay() == 1) {
                    loannew.setPrincipalandInterest(Arith.reimbursement(
                            loanRecord.getLoansign(), loanRecord.getLoansign()
                                    .getMonth())
                            * loanRecord.getLoansign().getMonth() * bl + "");
                } else {
                    loannew.setPrincipalandInterest(Arith.round(
                            loanRecord.getTenderMoney() + tzsy, 2)
                            + "");
                }
            } else {
                loannew.setPrincipalandInterest(Arith.round(
                        loanRecord.getTenderMoney() + tzsy, 2)
                        + "");
            }
            loanuplist.add(loannew);
        }
        return loanuplist;
    }

    /**
     * 
     * 得到流转标的利息
     * 
     * @return
     */
    public double getinterest(Loanrecord loanRecord) {
        double interest = 0.00;
        // 得到流转标的还款计划
        String sql = "select * from Repaymentrecord where Loansign_id="
                + loanRecord.getLoansign().getId();
        List<Repaymentrecord> list = dao.findBySql(sql, Repaymentrecord.class);
        // 得到用户是从第几期开始买的
        String sqls = "select * from Repaymentrecord where Loansign_id="
                + loanRecord.getLoansign().getId() + " and preRepayDate>'"
                + loanRecord.getTenderTime() + "'";
        List<Repaymentrecord> re = dao.findBySql(sql, Repaymentrecord.class);
        for (int i = 0; i < list.size(); i++) {
            Repaymentrecord repay = (Repaymentrecord) list.get(i);
            if (((Repaymentrecord) re.get(0)).getPeriods() <= repay
                    .getPeriods()) {
                if (((Repaymentrecord) re.get(0)).getPeriods() == repay
                        .getPeriods()) {
                    int times = repayPlanService.getIntervalDays(loanRecord
                            .getTenderTime().substring(0, 10), repay
                            .getPreRepayDate().substring(0, 10));
                    interest += loanRecord.getLoansign().getRate() / 365
                            * times * loanRecord.getTenderMoney();
                } else {
                    interest += loanRecord.getLoansign().getRate() / 365 * 30
                            * loanRecord.getTenderMoney();
                }
            }
        }
        return interest;
    }

    public Long getLoanRecord(int state, Long userid) {
        // 根据用户和标状态查新投资标信息
        String sql = "select count(dlr.id) from loanrecord dlr where dlr.userbasicinfo_id="
                + userid
                + " and dlr.Loansign_id in (select ls.id from "
                + " Loansign ls where ls.loanState=" + state + ")";
        // 当状态为0时，查询用户所有的投标信息
        if (0 == state) {
            sql = "select count(dlr.id) from loanrecord dlr where dlr.userbasicinfo_id="
                    + userid;
        }
        Long loanrecordlist = dao.queryNumberSql(sql).longValue();
        return loanrecordlist;
    }
    
    /**
     * 根据传入的投标中记录,获取总投资金额
     * 
     * @param list
     * @return
     */
    public double getSumTenderMoney(List<Object> list) {
        double money = 0;
        for (int i = 0; i < list.size(); i++) {

            double m = Double.valueOf(((LoanNew) list.get(i))
                    .getTenderMoney());
            // 计算完成的金额
            money = money + Double.valueOf(m);
        }
        return money;
    }

    /**
     * 根据传入的出借记录,获取总金额数
     * 
     * @param list
     * @return
     */
    public double getMoney(List<Object> list) {
        double money = 0;
        for (int i = 0; i < list.size(); i++) {

            double m = Double.valueOf(((LoanNew) list.get(i))
                    .getPrincipalandInterest());
            // 计算完成的金额
            money = money + Double.valueOf(m);
        }
        return money;
    }
    
    /**
     * 统计已还清项目的实际收款总额
     * 
     * @param list
     * @return
     */
    public double getRealMoney(List<Object> list) {
        double money = 0;
        for (int i = 0; i < list.size(); i++) {

            double m = Double.valueOf(((LoanNew) list.get(i)).getRealTotal());
            // 计算完成的金额
            money = money + Double.valueOf(m);
        }
        return money;
    }

    /**
     * 根据传入的未收款记录,获取总金额数
     * 
     * @param list
     * @return
     */
    public double getTotal(List<LoanUncollected> list) {
        double money = 0;
        for (int i = 0; i < list.size(); i++) {

            LoanUncollected m = list.get(i);
            // 计算完成的金额
            money = money + Double.valueOf(m.getTotal());
        }
        return money;
    }

    /**
     *统计借出明细中的投资总额
     * 
     * @param list
     * @return
     */
    public double getTenderMoney(List<LoanUncollected> list) {
        double money = 0;
        for (int i = 0; i < list.size(); i++) {

            LoanUncollected m = list.get(i);
            // 计算完成的金额
            money = money+Double.valueOf(m.getTenderMoney());
        }
        return money;
    }
    
    /**
     * 获取平均收益
     * 
     * @param list
     * @return
     */
    public double getInterests_pj(List<Loanrecord> list,
            HttpServletRequest request) {
        double interests = 0;
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            Loanrecord loanr = ((Loanrecord) list.get(i));
            double m = loanr.getLoansign().getRate();

            // 判断是否是提前还款
            String sql = "select * from Repaymentrecord drt where drt.Loansign_id="
                    + loanr.getLoansign().getId();
            List<Repaymentrecord> rtlist = dao.findBySql(sql,
                    Repaymentrecord.class);
            Repaymentrecord rt = null;
            if (rtlist.size() > 0) {
                rt = (Repaymentrecord) rtlist.get(0);
            }
            if (rt != null) {

                if (null != rt.getRepayState() && 5 == rt.getRepayState()) {
                    if (rt.getRepayTime() != null
                            && rt.getLoansign().getLoansignbasics()
                                    .getCreditTime() != null) {
                        String begintime = rt.getLoansign().getLoansignbasics()
                                .getCreditTime().substring(0, 11);
                        int days = repayPlanService.getIntervalDays(begintime,
                                rt.getRepayTime());
                        interests = interests + rt.getPreRepayMoney()
                                * Double.valueOf(m) / 365 * days;
                    } else {
                        // 计算累计利息
                        if (loanr.getLoansign().getLoanType() == 1) {
                            interests = interests
                                    + rt.getPreRepayMoney()
                                    * Double.valueOf(m)
                                    / 12
                                    * Double.valueOf(loanr.getLoansign()
                                            .getMonth());
                        } else if (loanr.getLoansign().getLoanType() == 2) {
                            interests = interests
                                    + rt.getPreRepayMoney()
                                    * Double.valueOf(m)
                                    * Double.valueOf(loanr.getLoansign()
                                            .getUseDay());
                        } else if (loanr.getLoansign().getLoanType() == 4) {
                            interests = interests
                                    + rt.getPreRepayMoney()
                                    * Double.valueOf(m)
                                    / 365
                                    * Double.valueOf(loanr.getLoansign()
                                            .getMonth());
                        }
                    }
                } else {
                    // 计算累计利息
                    if (loanr.getLoansign().getLoanType() == 1) {
                        interests = interests
                                + rt.getPreRepayMoney()
                                * Double.valueOf(m)
                                / 12
                                * Double.valueOf(loanr.getLoansign().getMonth());
                    } else if (loanr.getLoansign().getLoanType() == 2) {
                        interests = interests
                                + rt.getPreRepayMoney()
                                * Double.valueOf(m)
                                * Double.valueOf(loanr.getLoansign()
                                        .getUseDay());
                    } else if (loanr.getLoansign().getLoanType() == 4) {
                        interests = interests
                                + rt.getPreRepayMoney()
                                * Double.valueOf(m)
                                / 365
                                * Double.valueOf(loanr.getLoansign().getMonth());
                    } else {
                        interests = interests + rt.getPreRepayMoney()
                                * Double.valueOf(m);
                    }
                }
            }
        }
        // }
        // 计算平均
        if (list.size() > 0) {

            interests = interests / list.size();
        }
        return interests;
    }

    /**
     * 传入用户id,根据用户id查询最后还款时间
     * 
     * @param userid
     * @return
     */
    public Repaymentrecord getLastRuturnRepay(List<Loanrecord> loanrecordlist) {

        String properties = "";

        for (int i = 0; i < loanrecordlist.size(); i++) {
            Loanrecord loanr = ((Loanrecord) loanrecordlist.get(i));
            if (i == 0) {
                properties = loanr.getLoansign().getId().toString();
            } else {
                properties = properties + "," + loanr.getLoansign().getId();
            }
        }

        if ("".equals(properties)) {
            return new Repaymentrecord();
        }

        String sql = "select * from Repaymentrecord gr where gr.Loansign_id in ("
                + properties
                + ") and gr.preRepayDate in (select max(dgrr.preRepayDate) from Repaymentrecord dgrr where dgrr.Loansign_id in ("
                + properties + "))";

        List<Repaymentrecord> repayList = dao.findBySql(sql,
                Repaymentrecord.class);
        Repaymentrecord rr = new Repaymentrecord();
        if (repayList.size() > 0) {
            rr = (Repaymentrecord) repayList.get(0);
        }
        return rr;
    }

    /**
     * 
     * 获取未收款所有条数
     * 
     * @param userId
     * @return
     */
    public Long getNumrepauy(Long userId) {
        StringBuffer str = new StringBuffer("select count(a.id) FROM");
        str.append(" loanrecord a INNER JOIN Loansign b ON a.Loansign_id = b.id INNER JOIN Repaymentrecord c ON b.id=c.Loansign_id INNER JOIN userbasicsinfo u ON u.id=b.userbasicinfo_id");
        str.append(" WHERE  a.userbasicinfo_id = ");
        str.append(userId);
        str.append("  AND c.repayTime IS NULL AND c.repayState=1 AND ( b.loanState = 3 OR (b.loanType = 4 AND b.loanState != 4))");
        String sql = str.toString();
        Long num = dao.queryNumberSql(sql).longValue();
        return num;
    }

    /**
     * 
     * 得到未还款明细
     * 
     * @param userId
     * @return
     */
    public List<Object[]> getRepay(Long userId, int no) {
        StringBuffer str = new StringBuffer(
                "SELECT b.rate,b.issueLoan,b.loanType,(SELECT loanNumber FROM loansignbasics WHERE id = b.id),"
                        + "b.refundWay,b.month,a.tenderMoney,a.isPrivilege,a.tenderTime,c.preRepayDate,c.periods,c.preRepayMoney,"
                        + "c.repayTime,c.repayState,u.userName,b.pmfeeratio,b.pmfeetop,b.mfeeratio,b.mfeetop,b.useDay,"
                        + "(SELECT loanTitle FROM loansignbasics WHERE id = b.id),b.id FROM");
        str.append(" loanrecord a INNER JOIN Loansign b ON a.Loansign_id = b.id INNER JOIN Repaymentrecord c ON b.id=c.Loansign_id INNER JOIN userbasicsinfo u ON u.id=b.userbasicinfo_id");
        str.append(" WHERE  a.userbasicinfo_id = ");
        str.append(userId);
        str.append("  AND c.repayTime IS NULL AND c.repayState=1 AND  ( b.loanState = 3 OR(b.loanType = 4 AND b.loanState != 4))");
        str.append(" LIMIT ");
        str.append(no);
        str.append(",10");
        String sql = str.toString();
        List<Object[]> list = dao.findBySql(sql, null);
        return list;
    }

    /**
     * 
     * 获取未收款明细
     * 
     * @param loanlist
     * @return
     */
    public List<LoanUncollected> getLoanUncollected(List<Object[]> loanlist) {
        List<LoanUncollected> list = new ArrayList<LoanUncollected>();
        // 循环标的基础数据
        for (int i = 0; i < loanlist.size(); i++) {
            BasicData obj = convertObject(loanlist.get(i));
            if (obj.getRepayTime().equals("11")) {
                if (obj.getLoanType() == 4) {
                    if (timeDifference(obj.getPreRepayDate() + " 00:00:00",
                            obj.getTenderTime()) < 0) {
                        continue;
                    }
                }
                LoanUncollected loanun = getLoanUncollected(obj);
                double interest = 0.00;
                if (obj.getLoanType() == 4) {
                    if (timeDifference(obj.getPreRepayDate() + " 00:00:00",
                            obj.getTenderTime()) > 0) {
                        int times = repayPlanService.getIntervalDays(obj
                                .getTenderTime().substring(0, 10), obj
                                .getPreRepayDate());
                        if (times > 30) {
                            times = 30;
                        }
                        interest = obj.getTenderMoney() * times * obj.getRate() / 365;
                    }
                } else if (obj.getLoanType() == 1) {
                    if (obj.getRefundWay() == 1 || obj.getRefundWay() == 2) {
                        interest = obj.getTenderMoney() * obj.getRate() / 12;
                    } else {
                        interest = obj.getTenderMoney() * obj.getRate() / 12
                                * obj.getMonth();
                    }
                } else if(obj.getLoanType() == 3){
                    interest = obj.getTenderMoney() * obj.getRate();
                }else {
                    interest = obj.getTenderMoney() * obj.getRate() * obj.getUseDay();
                }
             // 收款总额
                double total = 0.00;
                double principalReceivables = 0.00;
                if (obj.getLoanType() == 1) {
                    if (obj.getRefundWay() == 1) {
                        Loansign loan = new Loansign();
                        loan.setIssueLoan(obj.getIssueLoan());
                        loan.setRate(obj.getRate());
                        loan.setMonth(obj.getMonth());
                        List<CalculateLoan> calcu = Arith.loanCalculate(loan);
                        for (int j = 0; j < calcu.size(); j++) {
                            CalculateLoan c = calcu.get(j);
                            if (obj.getPeriods() == j + 1) {
                                double bl = obj.getTenderMoney() / obj.getIssueLoan();
                                total = (c.getLixi() + c.getBenjin()) * bl;
                                interest = c.getLixi() * bl;
                                principalReceivables = c.getBenjin() * bl;
                            }
                        }
                    } else {
                        if (obj.getMonth() == obj.getPeriods()) {
                            total = obj.getTenderMoney() + interest;
                            principalReceivables = obj.getTenderMoney();
                        } else {
                            total = obj.getTenderMoney() + interest;
                            principalReceivables = 0.00;
                        }
                    }
                } else if (obj.getLoanType() == 2 || obj.getLoanType() == 3) {
                    total = obj.getTenderMoney() + interest;
                    principalReceivables = obj.getTenderMoney();
                } else {
                    if (obj.getMonth() == obj.getPeriods()) {
                        total = obj.getTenderMoney() + interest;
                        principalReceivables = obj.getTenderMoney();
                    } else {
                        total = obj.getTenderMoney() + interest;
                        principalReceivables = 0.00;
                    }
                }
                loanun.setTotal(total);
                // 应收利息
                loanun.setShould(interest);
                // 管理费
                double managementFee = 0.00;
                if(obj.getLoanType() != 3){
                    if (obj.getIsPrivilege() == 1) {// 会员
                        managementFee = interest * obj.getPmfeeratio();
                    } else {// 非会员
                        managementFee = interest * obj.getMfeeratio();
                    }
                }
                loanun.setTzyj(managementFee + "");
                // 实得利息
                loanun.setInterest((interest - managementFee) + "");
                list.add(loanun);
            }
        }
        return list;
    }

    /**
     * 
     * 将Object对象转换成BasicDate对象
     * 
     * @param obj
     * @return
     */
    public BasicData convertObject(Object[] obj) {
        BasicData data = new BasicData();
        data.setRate(Double.parseDouble(obj[0].toString()));
        data.setIssueLoan(Double.parseDouble(obj[1].toString()));
        data.setLoanType(Integer.parseInt(obj[2].toString()));
        data.setLoanNumber(obj[3].toString());
        data.setRefundWay(null == obj[4] ? 0 : Integer.parseInt(obj[4]
                .toString()));
        data.setMonth(null == obj[5] ? 1 : Integer.parseInt(obj[5].toString()));
        data.setTenderMoney(Double.parseDouble(obj[6].toString()));
        data.setIsPrivilege(Integer.parseInt(obj[7].toString()));
        data.setTenderTime(obj[8].toString());
        data.setPreRepayDate(obj[9].toString());
        data.setPeriods(Integer.parseInt(obj[10].toString()));
        data.setPreRepayMoney(Double.parseDouble(obj[11].toString()));
        data.setRepayTime(null == obj[12] ? "11" : obj[12].toString());
        data.setReparyState(Integer.parseInt(obj[13].toString()));
        data.setUserName(obj[14].toString());
        data.setPmfeeratio(Double.parseDouble(obj[15].toString()));
        data.setPmfeetop(obj[16] == null ? 0.00 : Double.parseDouble(obj[16]
                .toString()));
        data.setMfeeratio(Double.parseDouble(obj[17].toString()));
        data.setMfeetop(obj[18] == null ? 0.00 : Double.parseDouble(obj[18]
                .toString()));
        data.setUseDay(Integer.parseInt(null == obj[19] ? "0" : obj[19]
                .toString()));
        data.setLoanTitle(obj[20].toString());
        data.setLoansignId(Long.valueOf(obj[21].toString()));
        return data;
    }

    /**
     * 
     * 比较时间大小,返回 0 时间相等 小于0 开始时间小于结束时间 大于0 开始时间大于结束时间
     * 
     */
    public static int timeDifference(String beginTime, String endTime) {
        java.text.DateFormat df = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(beginTime));
            c2.setTime(df.parse(endTime));
        } catch (java.text.ParseException e) {
            System.err.println("格式不正确");
        }
        return c1.compareTo(c2);
    }

    /**
     * 
     * 得到详细信息
     * 
     * @param loanRecord
     * @param sql
     * @return
     */
    public LoanUncollected getLoanUncollected(BasicData obj) {
        LoanUncollected loanun = new LoanUncollected();
        //标的id
        loanun.setLoanId(obj.getLoansignId());
        // 借款标号
        loanun.setLoanNumber(obj.getLoanNumber());
        // 借款标题
        loanun.setLoanTitle(obj.getLoanTitle());
        // 借款者
        loanun.setBorrowername(obj.getUserName());
        // 应收日期
        loanun.setRepayTime(obj.getPreRepayDate());
        // 第几期/总期数
        loanun.setRefundDeadline(obj.getPeriods() + "/" + obj.getMonth());
        //认购金额
        loanun.setTenderMoney(obj.getTenderMoney());
        

        return loanun;
    }

    /**
     * 
     * 得到已还款明细
     * 
     * @param userId
     * @return
     */
    public List<Object[]> getRepayW(Long userId, int no) {
        StringBuffer str = new StringBuffer(
                "SELECT b.rate,b.issueLoan,b.loanType,(SELECT loanNumber FROM loansignbasics WHERE id = b.id),"
        +"b.refundWay,b.month,a.tenderMoney,a.isPrivilege,a.tenderTime,c.preRepayDate,c.periods,c.preRepayMoney,"
                        +"c.repayTime,c.repayState,u.userName,b.pmfeeratio,b.pmfeetop,b.mfeeratio,b.mfeetop,b.useDay,"
        +"(SELECT loanTitle FROM loansignbasics WHERE id = b.id),b.id FROM");
        str.append(" loanrecord a INNER JOIN loansign b ON a.loanSign_id = b.id INNER JOIN Repaymentrecord c ON b.id=c.loanSign_id INNER JOIN userbasicsinfo u ON u.id=b.userbasicinfo_id");
        str.append(" WHERE  a.userbasicinfo_id = ");
        str.append(userId);
        str.append("  AND (c.repayTime IS NOT NULL OR c.repayState!=1) AND  ( b.loanState = 3 OR(b.loanType = 4 AND b.loanState != 4))");
        str.append(" LIMIT ");
        str.append(no);
        str.append(",10");
        String sql = str.toString();
        List<Object[]> list = dao.findBySql(sql, null);
        return list;
    }

    /**
     * 
     * 获取已收款明细
     * 
     * @param loanlist
     * @return
     */
    public List<LoanUncollected> getAllLoanDetails(List<Object[]> loanlist) {
        List<LoanUncollected> list = new ArrayList<LoanUncollected>();
        // 循环标的基础数据
        for (int i = 0; i < loanlist.size(); i++) {
            BasicData obj = convertObject(loanlist.get(i));
            if (!obj.getRepayTime().equals("11") || obj.getReparyState() == 3) {
                if (obj.getLoanType() == 4) {
                    if (timeDifference(obj.getPreRepayDate() + " 00:00:00",
                            obj.getTenderTime()) < 0) {
                        continue;
                    }
                }
                LoanUncollected loanun = getLoanUncollected(obj);
                //统计该标的总实际还款利息
                String sql="SELECT SUM(realMoney) from repaymentrecord WHERE loanSign_id=?";
                List sum_realmoney = dao.findBySql(sql, obj.getLoansignId());
                //投资人投资金额所占标的金额的比例
                double bili = obj.getTenderMoney() / obj.getIssueLoan();
                //计算投资人实际所得利息
                double realmoney = 0.00;
                if(sum_realmoney.get(0) != null && sum_realmoney.get(0) != ""){
                    realmoney = Double.valueOf(sum_realmoney.get(0)+"")*bili;
                }
                //已收本息
                loanun.setPandInterest(obj.getTenderMoney()+realmoney);
                // 管理费 = 实得利息 x 管理费比例
                double managementFee = 0.00;
                if (obj.getIsPrivilege() == 1) {// 会员
                    managementFee = realmoney * obj.getPmfeeratio();
                } else {// 非会员
                    managementFee = realmoney * obj.getMfeeratio();
                }
                loanun.setTzyj(managementFee + "");
                // 实得利息
                loanun.setInterest(realmoney + "");

                list.add(loanun);
            }
        }
        return list;
    }

    /**
     * 获取已收款所有条数
     * 
     * @param userId
     * @return
     */
    public Long getNum(Long userId) {
        StringBuffer str = new StringBuffer("select count(a.id) FROM");
        str.append(" loanrecord a INNER JOIN loansign b ON a.loanSign_id = b.id INNER JOIN Repaymentrecord c ON b.id=c.loanSign_id INNER JOIN userbasicsinfo u ON u.id=b.userbasicinfo_id");
        str.append(" WHERE  a.userbasicinfo_id = ");
        str.append(userId);
        str.append("  AND c.repayTime IS NOT NULL AND c.repayState!=1 AND ( b.loanState = 3 OR (b.loanType = 4 AND b.loanState != 4))");
        String sql = str.toString();
        Long num = dao.queryNumberSql(sql).longValue();
        return num;
    }

    /**
     * 
     * 获取借出明细
     * 
     * @param loanlist
     * @return
     */
    public List<LoanUncollected> getLentDetails(List<Loanrecord> loanlist) {
        List<LoanUncollected> list = new ArrayList<LoanUncollected>();

        for (int i = 0; i < loanlist.size(); i++) {
            Loanrecord loan = (Loanrecord) loanlist.get(i);
            LoanUncollected loanun = new LoanUncollected();
            if (loan.getLoansign().getLoanType() == 4) {
                loanun = getInterval(loan);
                list.add(loanun);
                continue;
            }
            String sql = "SELECT SUM(r.money) AS a, SUM(r.preRepayMoney) AS b FROM repaymentrecord r WHERE r.loanSign_id = ? AND (r.repayState = 1 OR r.repayState = 3)";

            String sql1 = "SELECT SUM(r.money) AS c, SUM(r.realMoney),  SUM(r.preRepayMoney) AS d FROM repaymentrecord r WHERE r.loanSign_id = ? AND r.repayState != 1 AND r.repayState != 3";
            // 获取未收
            List<Object[]> lists = dao.findBySql(sql, loan.getLoansign()
                    .getId());
            // 获取已收
            List<Object[]> listss = dao.findBySql(sql1, loan.getLoansign()
                    .getId());

            // 借款标编号
            loanun.setLoanId(loan.getLoansign().getId());
            // 借款标题
            loanun.setLoanTitle(loan.getLoansign().getLoansignbasics()
                    .getLoanTitle());
            // 借款标号
            loanun.setLoanNumber(loan.getLoansign().getLoansignbasics()
                    .getLoanNumber());

            // 借款者
            if (loan.getLoansign().getLoanType() == 1
                    || loan.getLoansign().getLoanType() == 2
                    || loan.getLoansign().getLoanType() == 3
                    || loan.getLoansign().getLoanType() == 4) {
                loanun.setBorrowername(loan.getLoansign().getUserbasicsinfo()
                        .getUserName());
            } else {
                loanun.setBorrowername("");
            }

            // 借出总额
            loanun.setTenderMoney(loan.getTenderMoney());

            double bili = loan.getTenderMoney()
                    / loan.getLoansign().getIssueLoan();
            // 收款总额
            double moneysum = loan.getTenderMoney();
            // 代收利息
            double daishou = 0.00;
            // 代收本金
            double money = 0.00;
            // 已收利息
            double yihuan = 0.00;
            // 已收本金
            double benjin = 0.00;
            if (loan.getLoansign().getLoanstate().equals(Constant.STATUES_TWO)) {
                if (loan.getLoansign().getLoanType()
                        .equals(Constant.STATUES_ONE)) {
                    if (loan.getLoansign().getRefundWay()
                            .equals(Constant.STATUES_ONE)) {
                        List<CalculateLoan> calcu = Arith.loanCalculate(loan
                                .getLoansign());
                        for (int j = 0; j < calcu.size(); j++) {
                            daishou += Arith.mul(calcu.get(j).getLixi(), bili);
                            money += Arith.mul(calcu.get(j).getBenjin(), bili);
                        }
                    } else {
                        daishou = signFund.instalmentInterest(
                                new BigDecimal(loan.getTenderMoney()),
                                loan.getLoansign().getRate(),
                                loan.getLoansign().getMonth(),
                                Constant.STATUES_ONE).doubleValue()*loan.getLoansign().getMonth();
                        money = loan.getTenderMoney();
                    }
                } else if (loan.getLoansign().getLoanType()
                        .equals(Constant.STATUES_THERE)) {
                    daishou = loan.getTenderMoney()
                            * loan.getLoansign().getRate();
                    money = loan.getTenderMoney();
                    
                }else {
                    daishou = loan.getTenderMoney()
                            * loan.getLoansign().getRate()
                            * loan.getLoansign().getUseDay();
                    money = loan.getTenderMoney();
                }
            } else {
                if (lists.size() > 0 && null != lists) {
                    daishou = bili
                            * Double.parseDouble(lists.get(0)[1] == null ? "0"
                                    : lists.get(0)[1].toString());
                    money = bili
                            * Double.parseDouble(lists.get(0)[0] == null ? "0"
                                    : lists.get(0)[0].toString());
                }
                if (loan.getLoansign().getLoanType()
                        .equals(Constant.STATUES_THERE)) {
                    daishou = loan.getTenderMoney()
                            * loan.getLoansign().getRate();
                    money = loan.getTenderMoney();
                    
                }
                if (listss.size() > 0 && null != listss) {
                    yihuan = bili
                            * Double.parseDouble(listss.get(0)[1] == null ? "0"
                                    : listss.get(0)[1].toString());
                    benjin = bili
                            * Double.parseDouble(listss.get(0)[0] == null ? "0"
                                    : listss.get(0)[0].toString());
                }
            }
            //预计还款利息
            String sql2 = "SELECT SUM(r.preRepayMoney) AS sumPreMoney FROM repaymentrecord r WHERE r.loanSign_id = ?";
            double preMoney = dao.queryNumberSql(sql2, loan.getLoansign().getId());
            
            loanun.setPreInterest(bili*preMoney);
            // 收益总额
            double sum = daishou + yihuan;

            // 已收总额
            double count = 0.00;
            // 已还款期数
            int num = 0;

            // 利息总和
            loanun.setTotal(Arith.round(sum, 2));
            loanun.setTotalReceived(Arith.round(daishou, 2));// 待收
            loanun.setPandInterest(Arith.round(yihuan + benjin, 2)); // 已收
            // 待收利息
            loanun.setInterestReceivable(Arith.round(daishou, 2));
            // 待收总额
            if(loan.getLoansign().getLoanstate()==3 || loan.getLoansign().getLoanstate()==4){
                loanun.setTotalReceived(Arith.round(daishou + money, 2));
            }else{
                loanun.setTotalReceived(0.00);
            }
            loanun.setInterest(Arith.round(yihuan, 2) + "");
            list.add(loanun);
        }
        return list;
    }

    /**
     * 
     * 获取流转标的已收总额和未收总额
     * 
     * @param loanRecord
     *            出借记录
     * @return
     */
    public LoanUncollected getInterval(Loanrecord loanRecord) {
        LoanUncollected loanun = new LoanUncollected();
        // 得到流转标的还款计划
        String sql = "select * from Repaymentrecord where loanSign_id="
                + loanRecord.getLoansign().getId();
        List<Repaymentrecord> list = dao.findBySql(sql, Repaymentrecord.class);
        // 得到用户是从第几期开始买的
        String sqls = "select * from Repaymentrecord where loanSign_id="
                + loanRecord.getLoansign().getId() + " and preRepayDate>'"
                + loanRecord.getTenderTime() + "'";
        List<Repaymentrecord> re = dao.findBySql(sqls, Repaymentrecord.class);
        loanun.setLoanId(loanRecord.getId());
        // 借款标号
        loanun.setLoanNumber(loanRecord.getLoansign().getLoansignbasics()
                .getLoanNumber());
        // 借款者
        loanun.setBorrowername(loanRecord.getLoansign().getUserbasicsinfo()
                .getUserName());
        // 借出总额
        loanun.setTenderMoney(loanRecord.getTenderMoney());

        // 总的收益
        double inter = 0.00;
        // 收款总额
        double sum = 0.00;
        // 待收总额
        double daishou = 0.00;
        // 已收总额
        double yishou = 0.00;
        // 已收利息
        double yishouInter = 0.00;
        // 待收利息
        double daishouInter = 0.00;
        // 利息
        double lixi = 0.00;
        // 费用比例设置
        double feiyong = 0.00;
        double fgsx = 0.00;
        // 判断是否是特权会员
        boolean flag = userInfoQuery
                .isPrivilege(loanRecord.getUserbasicsinfo());
        if (flag) {
            feiyong = loanRecord.getLoansign().getPmfeeratio();
            fgsx = loanRecord.getLoansign().getPmfeetop();
        } else {
            feiyong = loanRecord.getLoansign().getMfeeratio();
            fgsx = loanRecord.getLoansign().getMfeetop();
        }
        // 管理费
        double guanlifei = 0.00;

        for (int i = 0; i < list.size(); i++) {
            Repaymentrecord repay = (Repaymentrecord) list.get(i);
            if (((Repaymentrecord) re.get(0)).getPeriods() <= repay
                    .getPeriods()) {
                // 得到第一期
                if (i + 1 == ((Repaymentrecord) re.get(0)).getPeriods()) {
                    int times = repayPlanService.getIntervalDays(loanRecord
                            .getTenderTime().substring(0, 10), repay
                            .getPreRepayDate().substring(0, 10));
                    lixi = loanRecord.getLoansign().getRate() / 365 * times
                            * loanRecord.getTenderMoney();
                } else {
                    lixi = loanRecord.getLoansign().getRate() / 365 * 30
                            * loanRecord.getTenderMoney();
                }

                guanlifei = lixi * feiyong / 100;
                if (guanlifei > fgsx) {
                    guanlifei = fgsx;
                }
                if (null != repay.getRepayTime() || repay.getRepayState() != 1) {
                    sum += lixi;
                    if (loanRecord.getLoansign().getLoanstate() == 4
                            && loanRecord.getLoansign().getMonth() == repay
                                    .getPeriods()) {
                        yishou += loanRecord.getTenderMoney();
                    }
                    yishou += lixi;
                    yishouInter += lixi;
                } else {
                    sum += lixi;
                    if (loanRecord.getLoansign().getLoanstate() == 4
                            && loanRecord.getLoansign().getMonth() == repay
                                    .getPeriods()) {
                        daishou = 0.00;
                        daishouInter = 0.00;
                    } else {
                        daishou += lixi;
                        daishouInter += lixi;
                    }
                }
            }
            loanun.setShould(inter);
            // 收款总额
            loanun.setTotal(sum);
            // 待收总额
            loanun.setTotalReceived(daishou + loanRecord.getTenderMoney());
            // 已收总额
            loanun.setPandInterest(yishou);
            // 已收利息
            loanun.setInterest(yishouInter + "");
            // 待收利息
            loanun.setInterestReceivable(daishouInter);
        }
        return loanun;
    }

    /**
     * 
     * 计算获得的收益
     * 
     * @param loanrecord
     * @return
     * 
     */
    public double interest(Repaymentrecord rr, Loanrecord loanrecord) {
        // 投资收益
        double investmentIncome = 0.00;
        // 判断是否提前还款
        if (5 == rr.getRepayState()) {
            if (rr.getRepayTime() != null
                    && rr.getLoansign().getLoansignbasics().getCreditTime() != null) {
                String begintime = rr.getLoansign().getLoansignbasics()
                        .getCreditTime().substring(0, 11);
                int days = repayPlanService.getIntervalDays(begintime,
                        rr.getRepayTime());
                if (rr.getLoansign().getLoanType() == 1) {
                    investmentIncome = Double.valueOf(loanrecord
                            .getTenderMoney())
                            * Double.valueOf(loanrecord.getLoansign().getRate())
                            / 365 * days;
                } else {
                    investmentIncome = Double.valueOf(loanrecord
                            .getTenderMoney())
                            * Double.valueOf(loanrecord.getLoansign().getRate())
                            * days;
                }
            }
        } else if (2 == rr.getRepayState()) { // 按时还款
            // 判断标的类型
            if (rr.getLoansign().getLoanType() == 1) {
                if (rr.getLoansign().getRefundWay() == 3) {
                    investmentIncome = Double.valueOf(loanrecord
                            .getTenderMoney())
                            * Double.valueOf(loanrecord.getLoansign().getRate())
                            / 12
                            * Integer.valueOf(loanrecord.getLoansign()
                                    .getMonth());
                } else {
                    investmentIncome = Double.valueOf(loanrecord
                            .getTenderMoney())
                            * Double.valueOf(loanrecord.getLoansign().getRate())
                            / 12
                            * Integer.valueOf(loanrecord.getLoansign()
                                    .getMonth())
                            / loanrecord.getLoansign().getMonth();
                }
            } else if (rr.getLoansign().getLoanType() == 2) {
                investmentIncome = Double.valueOf(loanrecord.getTenderMoney())
                        * Double.valueOf(loanrecord.getLoansign().getRate())
                        * rr.getLoansign().getRealDay();
            } else if (rr.getLoansign().getLoanType() == 4) {
                investmentIncome = Double.valueOf(loanrecord.getTenderMoney())
                        * Double.valueOf(loanrecord.getLoansign().getRate())
                        / 365
                        * Integer.valueOf(loanrecord.getLoansign().getMonth())
                        / loanrecord.getLoansign().getMonth();
            } else {
                investmentIncome = Double.valueOf(loanrecord.getTenderMoney())
                        * Double.valueOf(loanrecord.getLoansign().getRate());
            }
        } else {
            // 获得逾期天数
            int date = 0;
            try {
                // 判断是否为天标和秒标
                if (rr.getLoansign().getLoanType() == 1
                        || rr.getLoansign().getLoanType() == 2) {
                    date = repayPlanService.getIntervalDays(
                            rr.getPreRepayDate(),
                            DateUtils.format("yyyy-MM-dd"));
                }
                if (date < 0) {
                    date = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 计算逾期收益
            // 普通标
            if (rr.getLoansign().getLoanType() == 1) {
                // 判断还款类型 每月还本息和每月还息到期还本
                if (rr.getLoansign().getRefundWay() == 1
                        || rr.getLoansign().getRefundWay() == 2) {
                    investmentIncome = rr.getPreRepayMoney() * 0.002 * 0
                            + loanrecord.getLoansign().getRate() / 12
                            * loanrecord.getTenderMoney();
                } else { // 到期一次性还本息
                    investmentIncome = rr.getPreRepayMoney() * 0.002 * 0
                            + loanrecord.getLoansign().getRate() / 12
                            * loanrecord.getTenderMoney()
                            * loanrecord.getLoansign().getMonth();
                }
            } else if (rr.getLoansign().getLoanType() == 2) { // 天标
                investmentIncome = rr.getPreRepayMoney() * 0.002 * 0
                        + loanrecord.getLoansign().getRate()
                        * loanrecord.getTenderMoney()
                        * loanrecord.getLoansign().getUseDay();
            } else if (rr.getLoansign().getLoanType() == 4) { // 流转标
                investmentIncome = rr.getPreRepayMoney() * 0.002 * 0
                        + loanrecord.getLoansign().getRate() / 365
                        * loanrecord.getTenderMoney();

            } else { // 天标
                investmentIncome = Double.valueOf(loanrecord.getTenderMoney())
                        * Double.valueOf(loanrecord.getLoansign().getRate());
            }
        }
        return investmentIncome;
    }
}
