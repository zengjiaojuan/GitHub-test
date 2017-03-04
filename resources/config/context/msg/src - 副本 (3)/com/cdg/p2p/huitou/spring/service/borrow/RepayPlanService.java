package com.cddgg.p2p.huitou.spring.service.borrow;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Loanrecord;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.util.Arith;

@Resource
public class RepayPlanService {

    /**
     * 注入HibernateSupport
     */
    @Resource
    private HibernateSupport dao;
    
    private static SimpleDateFormat getDateParser(String pattern) {
        return new SimpleDateFormat(pattern);
    }
    public static String getDate(Date date,String format){
        return getDateParser(format).format(date);
    }
    
    /**
     * 
     * 比较时间大小,返回
     * 0 时间相等
     * 小于0 开始时间小于结束时间
     * 大于0 开始时间大于结束时间
     * 
     */
    public static int timeDifference(String beginTime, String endTime) {
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
     * 计算两个日期的天数差并返回，日期格式yyyy-MM-dd
     * 
     */
    public static int getIntervalDays(String beginTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long millisecond = 0L;
        try {
            millisecond = format.parse(endTime).getTime() - format.parse(beginTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = (int) (millisecond / 24L / 60L / 60L / 1000L);
        return day;   
    }
    
    /**
     * 当前日期的月份+月份
     * @return
     * @author hulicheng
     * 2013-7-9
     * String
     */
    public static String getNowDateAddMonth(int month){
        Calendar cd= Calendar.getInstance();
        cd.setTime(new Date());
        cd.add(Calendar.MONTH, month);
        return getDate(cd.getTime(), "yyyy-MM-dd");
        
    }
    
    public void repaymentPlanDetail(Integer no, String beginDate, String endDate, Model model, HttpServletRequest request) {
        PageModel page = new PageModel();
        page.setPageNum(no > 1 ? no : 1);
        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        String sql = " from Repaymentrecord t where t.loansign.userbasicsinfo.id="
                + user.getId()
                + " and (t.repayState=1 or t.repayState is null or t.repayState=3)";
        // 默认查询一个月的
        if (null != endDate && !"".equals(endDate)) {
            sql += " and " + "t.preRepayDate>='" + beginDate
                    + "' and t.preRepayDate<='" + endDate + "'";
        } else {
            sql += " and " + "t.preRepayDate>='" + beginDate + "'";
        }
        sql += " order by t.loansign.id,t.preRepayDate";

        List recordPlanDetail = dao.pageListByHql(page, sql, false);
        request.setAttribute("pager", page);
        model.addAttribute("planDetail", convertTomap(recordPlanDetail));
    }
    
    /**
     * 创建方法剪list转换为map
     */
    public List<Map> convertTomap(List<Repaymentrecord> list) {
        List<Map> map = new ArrayList<Map>();
        if (null != list) {
            for (Repaymentrecord record : list) {
                Map<String, Object> recodemap = new LinkedHashMap<String, Object>();
                recodemap.put("loanNumber", record.getLoansign().getLoansignbasics().getLoanNumber());// 借款标号
                
                recodemap.put("loanTitle", record.getLoansign().getLoansignbasics().getLoanTitle());// 借款标标题
                recodemap.put("loanType", record.getLoansign().getLoanType());// 借款标类型
                recodemap.put("rate", record.getLoansign().getRate());// 年化利率
                recodemap.put("loanType", record.getLoansign().getLoanType());// 借款标类型
                if(record.getLoansign().getLoanType() == 2 || record.getLoansign().getLoanType() == 3
                        && record.getLoansign().getUseDay() != null){
                    recodemap.put("month", record.getLoansign().getUseDay());// 借款期限
                }else if(record.getLoansign().getLoanType() == 1 || record.getLoansign().getLoanType() == 5
                        && record.getLoansign().getMonth() != null){
                    recodemap.put("month", record.getLoansign().getMonth());// 借款期限
                }else{
                    recodemap.put("month", 0);// 借款期限
                }
                recodemap.put("refundWay", record.getLoansign().getRefundWay());// 借款期限
                
                recodemap.put("preRepayDate", record.getPreRepayDate());// 还款日期
                recodemap.put("preRepayMoney", getTotalMoneyAndRate(record));
                recodemap.put("bjAndLx", getTotalMoneyAndRate(record));// 本金+利息
                recodemap.put("periods", record.getPeriods());// 期数
                int totalPeriods = 0;
                if (record.getLoansign().getLoanType() == 1) {
                    // 普通标
                    totalPeriods = record.getLoansign().getRefundWay() == 3 ? 1
                            : record.getLoansign().getMonth();
                } else if (record.getLoansign().getLoanType() == 2) {
                    // 天标
                    totalPeriods = record.getLoansign().getRefundWay() == 3 ? 1
                            : record.getLoansign().getUseDay();// 预计天数

                } else if (record.getLoansign().getLoanType() == 4) {// 流转标

                    totalPeriods = record.getLoansign().getMonth();

                }
                // 总期数
                recodemap.put("totalPeriods", totalPeriods);
                map.add(recodemap);
            }
        }
        return map;
    }
    
    /**
     * 普通标 ： 计算要偿还的总金额和利息 每月还本息：本金/款期限(月) + （年利润/12）*本金
     * 一次性付完：本金+本金*借款期限(月)*（年利润/12） 每月还利息 到期还本：每月的利息算法 （年利润/12）*本金，最后一期 直接加上本金
     * 
     * @return
     * 
     *         天标
     * 
     *         //还款金额 = 借款金额 + 借款金额 * 天利率 * 预计借款天数 //若提前还款了公式为： 还款金额 = 借款金额 +
     *         借款金额 * 天利率 * 实际借款天数
     * 
     *         秒标 //预计收益及本金（未扣除佣金） = 认购金额 + 认购金额 * 利率
     * 
     *         2013-7-9 String
     */
    public Map<String, Double> getTotalMoneyAndRate(Repaymentrecord recode) {
        Loansign entity = recode.getLoansign();
        int refundWay = 0; //还款方式
        // 还款方式：1每月还本息、2每月付息到期还本、3到期一次性还本息(只有普通标才有还款方式)
        if(entity.getLoanType() == 1){
            refundWay = entity.getRefundWay();
        }

        // 借款表类型
        int loanType = entity.getLoanType();

        Map<String, Double> res = new HashMap<String, Double>();

        // 贷款总额
        Double totalPrice = entity.getIssueLoan();
        // 年化利率
        Double rateYear = entity.getRate();

        // 还款期限-月份
        int refundDeadline = 0;
        if (null != entity.getMonth()) {
            refundDeadline = entity.getMonth();
        }
        double mPrice = 0;
        double lx = 0;
        // 普通标
        if (loanType == 1) {
            // 每月还本息
            if (refundWay == 1) {
                // //每月本金
                mPrice = new BigDecimal(totalPrice / refundDeadline).setScale(
                        4, BigDecimal.ROUND_HALF_UP).doubleValue();
                // //每月利息
                // lx = new BigDecimal((rateYear)/12*totalPrice).setScale(4,
                // BigDecimal.ROUND_HALF_UP).doubleValue();
                // mPrice=;
                lx = Arith.reimbursement(entity, entity.getMonth()) - mPrice;
            } else if (refundWay == 2) {// 每月付息到期还本
                // 每月利息
                lx = new BigDecimal((rateYear) / 12 * totalPrice).setScale(4,
                        BigDecimal.ROUND_HALF_UP).doubleValue();
                mPrice = 0;
                // 如果还款期数和当前计划还款期数相同，则说明是最后一个月，此时需要还本月利息和本金
                if (entity.getMonth().equals(recode.getPeriods())) {
                    mPrice = totalPrice;
                }
            } else if (refundWay == 3) {
                // 本金+本金*借款期限(月)*（年利润/12）
                // if(refundDeadline==Integer.parseInt(recode.getPeriods())){
                mPrice = totalPrice;
                lx = new BigDecimal(totalPrice * refundDeadline
                        * ((rateYear) / 12)).setScale(4,
                        BigDecimal.ROUND_HALF_UP).doubleValue();
                // }
            }
        } else if (loanType == 2) {// 天标
            // 还款金额 = 借款金额 + 借款金额 * 天利率 * 预计借款天数
            // 若提前还款了公式为： 还款金额 = 借款金额 + 借款金额 * 天利率 * 实际借款天数
            int userDay = 0;
            if (null != entity.getUseDay()) {
                userDay = entity.getUseDay();
            }
            mPrice = totalPrice;
            lx = new BigDecimal(totalPrice * userDay * rateYear).setScale(4,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
            int real = 0;
            if (null != entity.getRealDay()) {
                real = entity.getRealDay();
                // 提前还款
                if (real < userDay) {
                    lx = new BigDecimal(totalPrice * real * rateYear).setScale(
                            4, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }

        } else if (loanType == 3)// 秒表
        {
            // 预计收益及本金（未扣除佣金） = 认购金额 + 认购金额 * 利率
            mPrice = totalPrice;
            lx = new BigDecimal(totalPrice * rateYear).setScale(4,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
        } else if (loanType == 4) {// 流转标
            String hql = "from Loanrecord l where l.loansign.id="
                    + entity.getId();
            List<Loanrecord> list = dao.find(hql);
            for (int i = 0; i < list.size(); i++) {
                Loanrecord loan = list.get(i);
                if (entity.getMonth() == recode.getPeriods()) {
                    mPrice = mPrice + loan.getTenderMoney();
                }
                double interest = 0.00;
                if (timeDifference(recode.getPreRepayDate()
                        + " 00:00:00", loan.getTenderTime()) > 0) {
                    int times = getIntervalDays(loan
                            .getTenderTime().substring(0, 10), recode
                            .getPreRepayDate());
                    if (times > 30) {
                        times = 30;
                    }
                    interest = loan.getTenderMoney() * times * entity.getRate()
                            / 365;
                }
                lx = lx + interest;
            }
            // entity
            // lx = recode.
        }

        res.put("bj", mPrice);
        res.put("lx", lx);
        res.put("total", mPrice + lx);
        return res;
    }
}
