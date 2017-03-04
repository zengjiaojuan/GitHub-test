package com.cddgg.p2p.huitou.spring.controller.borrow;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.MemberCenterService;
import com.cddgg.p2p.huitou.spring.service.borrow.BorrowRecordService;
import com.cddgg.p2p.huitou.spring.service.borrow.RepayPlanService;

/**
 * 借入记录
 * 
 * @author My_Ascii
 * 
 */
@Controller
@RequestMapping("borrower_record")
@CheckLogin(value = CheckLogin.WEB)
public class BorrowRecordController {

    /**
     * 注入HibernateSupport
     */
    @Resource
    private HibernateSupport otherDao;
    /**
     * 注入BorrowRecordService
     */
    @Resource
    private BorrowRecordService borrowrecord;
    /**
     * 注入MemberCenterService
     */
    @Resource
    private MemberCenterService memberCenterService;
    /**
     * 注入RepayPlanService
     */
    @Resource
    private RepayPlanService repayservice;

    /**
     * 创建借入记录的方法
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/toRecord")
    public String showopenrecord(Model model, HttpServletRequest request,
            HttpServletResponse response) {

        // 发标中借入金额
        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        String sql = "select sum(t.issueLoan),count(t.id) from loansign t where t.userbasicinfo_id="
                + user.getId() + " and t.loanstate=2";
        Object[] res1 = (Object[]) otherDao.findObjectBySql(sql);
        String val = borrowrecord.formateNumber(res1[0]);
        res1[0] = val;
        model.addAttribute("row_1", res1);

        // 未发布的借款标的借入总金额
        String sql02 = "select sum(t.issueLoan),count(t.id) from loansign t where t.userbasicinfo_id="
                + user.getId() + " and t.loanstate=1";
        Object[] unpublish = (Object[]) otherDao.findObjectBySql(sql02);
        String unpublish_0 = borrowrecord.formateNumber(unpublish[0]);
        unpublish[0] = unpublish_0;
        model.addAttribute("unpublish", unpublish);

        // 偿还中借入金额
        sql = "select sum(t.issueLoan),count(t.id) from loansign t where t.userbasicinfo_id="
                + user.getId()
                + " and ((t.loanstate=3)or(t.loanstate=4 and (SELECT count(1) from repaymentrecord where loanSign_id=t.id and repayState=3)>0))";
        Object[] res2 = (Object[]) otherDao.findObjectBySql(sql);

        double val_2 = 0;
        if (res2[0] != null)
            val_2 = Double.parseDouble(res2[0].toString());
        String val2 = borrowrecord.formateNumber(res2[0]);
        res2[0] = val2;
        model.addAttribute("row_2", res2);

        // 还清的借入
        sql = "select sum(t.issueLoan),count(t.id) from loansign t where t.userbasicinfo_id="
                + user.getId()
                + " and t.loanstate=4 and (SELECT count(1) from repaymentrecord where loanSign_id=t.id and repayState=3)=0";
        Object[] res3 = (Object[]) otherDao.findObjectBySql(sql);
        double val_3 = 0;
        if (res3[0] != null)
            val_3 = Double.parseDouble(res3[0].toString());

        String val3 = borrowrecord.formateNumber(res3[0]);
        res3[0] = val3;
        model.addAttribute("row_3", res3);

        String now = DateUtils.format("yyyy-MM");
        // 当前垫付
        sql = "select sum(t.preRepayMoney+t.money) from repaymentrecord t,loansign l where t.loanSign_id=l.id and l.userbasicinfo_id="
                + user.getId()
                + " and t.preRepayDate like '"
                + now
                + "%' and t.repayState =3";
        Object res4 = otherDao.findObjectBySql(sql);

        String val4 = borrowrecord.formateNumber(res4);
        res4 = val4;
        model.addAttribute("row_4", res4);

        // 累计垫付
        sql = "select sum(t.preRepayMoney+t.money) from repaymentrecord t,loansign l where t.loanSign_id=l.id and l.userbasicinfo_id="
                + user.getId() + "  and t.repayState =3";
        Object res5 = otherDao.findObjectBySql(sql);
        String val5 = borrowrecord.formateNumber(res5);
        res5 = val5;
        model.addAttribute("row_5", res5);

        // 当前逾期(逾期未还款)
        sql = "SELECT sum(preRepayMoney+money) FROM repaymentrecord rr,loansign ls where rr.loansign_id=ls.id and rr.repayState=3 and ls.userbasicinfo_id="
                + user.getId();
        Object res6 = otherDao.findObjectBySql(sql);
        String val6 = borrowrecord.formateNumber(res6);
        res6 = val6;
        model.addAttribute("row_6", res6);

        // 累计逾期
        sql = "SELECT sum(preRepayMoney+money),count(rr.id) FROM repaymentrecord rr,loansign ls where rr.loansign_id=ls.id and rr.repayState=4 and ls.userbasicinfo_id="
                + user.getId();
        // Object res7 = otherDao.findObjectBySql(sql);
        Object[] res7 = (Object[]) otherDao.findObjectBySql(sql);
        String res7_0 = borrowrecord.formateNumber(res7[0]);
        res7[0] = res7_0;
        model.addAttribute("row_7", res7);

        // 累计借出奖励：
        sql = "select sum(t1.issueLoan*t2.reward) from loansign t1 INNER JOIN loansignbasics t2 ON t1.id = t2.id where t1.userbasicinfo_id="
                + user.getId() + " and t1.loanstate in ('3','4')";
        Object res8 = otherDao.findObjectBySql(sql);
        val = borrowrecord.formateNumber(res8);
        res8 = val;
        model.addAttribute("row_8", res8);

        // 支付p2p网贷平台
        sql = "SELECT SUM(mgtMoney) from loansignbasics a INNER JOIN loansign b ON a.id = b.id WHERE  b.loanstate>2 and b.loanstate<5 and  b.userbasicinfo_id="
                + user.getId();
        Object res9 = otherDao.findObjectBySql(sql);
        val = borrowrecord.formateNumber(res9);
        res9 = val;
        model.addAttribute("row_9", res9);

        // 累计借入金额
        sql = "SELECT SUM(tenderMoney) FROM loanrecord where loanSign_id in (SELECT id from loansign where loanstate>2 and loanstate<5 and  userbasicinfo_id="
                + user.getId() + ")";
        Object res10 = otherDao.findObjectBySql(sql);
        res10 = borrowrecord.formateNumber(res10);
        model.addAttribute("row_10", res10);
        // 累计利息成本
        Double res11 = Double.parseDouble(memberCenterService.interest(
                user.getId()).toString());
        model.addAttribute("row_11", res11);
        // 借入利息成本
        Double res12 = 0.0;
        if (res11 == 0 || res10.toString() == "0") {

        } else {
            res12 = res11
                    / Double.parseDouble(res10.toString().replaceAll(",", ""))
                    * 100;
        }
        model.addAttribute("row_12",
                borrowrecord.toJsonString(borrowrecord.round(res12, 2)));
       
        double total = 0;
        String sql03 = "from Repaymentrecord t where t.loansign.userbasicsinfo.id="
                + user.getId()
                + " and (t.repayState=1 or t.repayState is null or t.repayState=3)";
        // 目前应还款总额(未还款，空，逾期未还款)
        List<Repaymentrecord> list4 = otherDao.find(sql03);
        for (Repaymentrecord recode : list4) {
            Map<String, Double> res = repayservice.getTotalMoneyAndRate(recode);
            total = total + res.get("total");
        }
        model.addAttribute("unrepay", total);
        total = 0;
        
        // 未来30天待还本息总额
        String beginDate = DateUtils.format("yyyy-MM-dd");
        String endDate = borrowrecord.getNowDateAddDays(30);
        // 预计还款日期大于等于当前月份
        String hql = sql03 + "  and t.preRepayDate>='" + beginDate + "'";

        // 未来30天还款
        List<Repaymentrecord> list = otherDao.find(hql
                + " and  t.preRepayDate<='" + endDate + "'");
        for (Repaymentrecord recode : list) {
            Map<String, Double> res = repayservice.getTotalMoneyAndRate(recode);
            total = total + res.get("total");
        }
        model.addAttribute("future_threety", total);
        total = 0;
        
        //近10天借款总额及借款数量
        endDate = borrowrecord.getNowDateAddDays(10);
        List<Repaymentrecord> list02 = otherDao.find(hql
                + " and  t.preRepayDate<='" + endDate + "'");
        for (Repaymentrecord recode : list02) {
            Map<String, Double> res = repayservice.getTotalMoneyAndRate(recode);
            total = total + res.get("total");
        }
        model.addAttribute("future_ten", total);
        String sql04 = "select COUNT(DISTINCT t.loanSign_id) from  loansign t2 ,repaymentrecord t "
        +"where t.loanSign_id=t2.id and t2.userbasicinfo_id=? "
        +"and (t.repayState=1 or t.repayState is null or t.repayState=3)"
        +"and t.preRepayDate>='" + beginDate + "'"
        +" and  t.preRepayDate<='" + endDate + "'";
        int count = otherDao.queryNumberSql(sql04,user.getId()).intValue();
        model.addAttribute("future_ten_count", count);
        total = 0;
        
        return "/WEB-INF/views/member/borrow/borrowers_info_record";
    }

    /**
     * 创建方法求发标中借款列表
     */
    @RequestMapping("/showList1")
    public String showList1(Model model, HttpServletRequest request) {

        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        String hql = "from Loansign t where t.userbasicsinfo.id="
                + user.getId() + " and loanstate=2";
        List list = otherDao.find(hql);
        model.addAttribute("list", list);
        model.addAttribute("tp", 1);
        return "/WEB-INF/views/member/borrow/loanSignList";
    }

    /**
     * 创建方法求发标中借款列表
     */
    @RequestMapping("/showList2")
    public String showList2(Model model, HttpServletRequest request) {

        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        String hql = "select * from loansign t where t.userbasicinfo_id= ? and ((t.loanstate=3)or(t.loanstate=4 and (SELECT count(1) from repaymentrecord where loanSign_id=t.id and loanstate=3)>0))";
        List list = otherDao.findBySql(hql, Loansign.class, user.getId());
        model.addAttribute("list", list);
        model.addAttribute("tp", 2);
        return "/WEB-INF/views/member/borrow/loanSignList";

    }

    /**
     * 创建方法查询未发布借款标列表
     */
    @RequestMapping("/showList3")
    public String showList3(Model model, HttpServletRequest request) {

        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        String hql = "from Loansign t where t.userbasicsinfo.id="
                + user.getId() + " and loanstate=1";
        List list = otherDao.find(hql);
        model.addAttribute("list", list);
        model.addAttribute("tp", 3);
        return "/WEB-INF/views/member/borrow/loanSignList";
    }

    /**
     * 创建方法查询所有逾期借款标列表
     */
    @RequestMapping("/showList4")
    public String showList4(Model model, HttpServletRequest request) {

        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        String sql = "SELECT c.loanNumber, a.preRepayDate, (a.preRepayMoney+a.money),"
                + " b.rate, a.periods, (SELECT COUNT(*) FROM repaymentrecord a WHERE a.loanSign_id = b.id)"
                + "  FROM repaymentrecord a INNER JOIN loansign b ON a.loanSign_id = b.id INNER JOIN loansignbasics c ON b.id = c.id "
                + "WHERE b.userbasicinfo_id = ? AND a.repayState=4";
        List list = otherDao.findBySql(sql, user.getId());
        model.addAttribute("list", list);
        model.addAttribute("tp", 4);
        return "/WEB-INF/views/member/borrow/loanSignList";
    }

    /**
     * 查询近10天需归还借款标的详细信息。
     */
    @RequestMapping("/repaymentPlanDetail")
    public String repaymentPlanDetail(
            @RequestParam(value = "no", required = false, defaultValue = "1") Integer no,
            String beginDate,
            @RequestParam(value = "endDate", required = false) String endDate, Model model, HttpServletRequest request) {
        repayservice.repaymentPlanDetail(no, beginDate, endDate, model, request);
        return "/WEB-INF/views/member/borrow/repayment_plan_detail02";
    }
}
