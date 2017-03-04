package com.cddgg.p2p.huitou.spring.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.base.model.PageModel;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.MemberCenterService;
import com.cddgg.p2p.huitou.spring.service.MyMoneyService;
import com.cddgg.p2p.huitou.spring.service.UserBaseInfoService;

/**
 * 资金统计
 * 
 * @author ransheng
 * 
 */
@Controller
@CheckLogin(value = CheckLogin.WEB)
@RequestMapping("/my_money")
@SuppressWarnings("rawtypes")
public class MyMoneyController {

    /**
     * 用户基本信息接口
     */
    @Resource
    private UserBaseInfoService userBaseInfoService;

    /**
     * 部分资金接口
     */
    @Resource
    private MemberCenterService memberCenterService;

    /**
     * 资金统计接口
     */
    @Resource
    private MyMoneyService myMoneyService;

    /**
     * 登录用户session
     * 
     * @param request
     *            请求
     * @return 用户基本信息
     */
    public Userbasicsinfo queryUser(HttpServletRequest request) {
        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        return user;
    }

    /**
     * 资金统计
     * 
     * @param request
     *            request
     * @param response
     *            response
     * @return jsp
     */
    @RequestMapping("/my_money_sum")
    public String myMoneySum(HttpServletRequest request,
            HttpServletResponse response) {
        Userbasicsinfo user = queryUser(request);
        user = userBaseInfoService.queryUserById(user.getId());
        request.setAttribute("user", user);
        // 用户的待收本金金额
        Object toBeClosed = myMoneyService.toBeClosed(user.getId());
        request.setAttribute("toBeClosed", toBeClosed);
        // 待付本息金额
        Object colltionPrinInterest = myMoneyService.colltionPrinInterest(user
                .getId());
        request.setAttribute("colltionPrinInterest", colltionPrinInterest);
        // 待确认投标
        Object lentBid = memberCenterService.investmentRecords(user.getId(), 2);
        request.setAttribute("lentBid", lentBid);
        // 逾期总额
        Object overude = myMoneyService.overude(user.getId());
        request.setAttribute("overude", overude);
        // 净值标总额（冻结金额）
        Object netMark = myMoneyService.netMark(user.getId());
        request.setAttribute("netMark", netMark);
        // 累计奖励
        Object accumulative = myMoneyService.accumulative(user.getId());
        request.setAttribute("accumulative", accumulative);
        // 平台累计支付
        Object adminAccumulative = myMoneyService.adminAccumulative(user
                .getId());
        request.setAttribute("adminAccumulative", adminAccumulative);

        // 净赚利息
        Object netInterest = myMoneyService.netInterest(user.getId());
        request.setAttribute("netInterest", netInterest);
        // 净付利息
        Object netInterestPaid = myMoneyService.netInterestPaid(user.getId());
        request.setAttribute("netInterestPaid", netInterestPaid);
        
        //逾期还款违约金
        Object latePayment = myMoneyService.latePayment(user.getId());
        request.setAttribute("latePayment", latePayment);
        
        //提前还款违约金
        Object prepayment = myMoneyService.prepayment(user.getId());
        request.setAttribute("prepayment", prepayment);
        
        
        // 累计支付会员费
        Object vipSum = myMoneyService.vipSum(user.getId());
        request.setAttribute("vipSum", vipSum);
        // 累计提现手续费
        Object witharwDeposit = myMoneyService.witharwDeposit(user.getId());
        request.setAttribute("witharwDeposit", witharwDeposit);
        // 累计充值手续费
        Object rechargeDeposit = myMoneyService.rechargeDeposit(user.getId());
        request.setAttribute("rechargeDeposit", rechargeDeposit);

        // 累计投资金额
        Object investmentRecords = myMoneyService.investmentRecords(user
                .getId());
        request.setAttribute("investmentRecords", investmentRecords);
        // 累计借入金额
        Object borrowing = myMoneyService.borrowing(user.getId());
        request.setAttribute("borrowing", borrowing);
        // 借款人管理费
        Object borrowersFee = myMoneyService.borrowersFee(user.getId());
        request.setAttribute("borrowersFee", borrowersFee);
        // 累计充值金额
        Object rechargeSuccess = myMoneyService.rechargeSuccess(user.getId());
        request.setAttribute("rechargeSuccess", rechargeSuccess);
        // 累计提现金额
        Object withdrawSucess = myMoneyService.withdrawSucess(user.getId());
        request.setAttribute("withdrawSucess", withdrawSucess);
        // 投资人累计支付佣金
        Object commission = myMoneyService.commission(user.getId());
        request.setAttribute("commission", commission);
        // 待收利息总额
        Object interestToBe = myMoneyService.interestToBe(user.getId());
        request.setAttribute("interestToBe", interestToBe);
        
        //待扣借出服务费
        double lendingFees=myMoneyService.lendingFees(user.getId());
        request.setAttribute("lendingFees", lendingFees);
        // 待付利息总额
        Object colltionInterest = myMoneyService.colltionInterest(user.getId());
        request.setAttribute("colltionInterest", colltionInterest);
        return "/WEB-INF/views/member/myMoney/myMoney_sum";
    }

    /**
     * 查询流水类型
     * 
     * @param request
     *            request
     * @return jsp
     */
    @RequestMapping("/my_money_sum_details")
    public String queryAccountType(HttpServletRequest request) {
        List list = myMoneyService.queryAccountType();
        request.setAttribute("list", list);
        return "/WEB-INF/views/member/myMoney/myMoney_sum_details";
    }

    /**
     * 分页查询资金历史记录
     * 
     * @param page
     *            分页对象
     * @param typeId
     *            类型编号
     * @param request
     *            request
     * @param response
     *            response
     * @return jsp
     */
    @RequestMapping("/query_fund_page")
    public String queryFundPage(@ModelAttribute("PageModel") PageModel page,
            String typeId, HttpServletRequest request,
            HttpServletResponse response) {
        Userbasicsinfo user = queryUser(request);
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        // 查询条数
        Integer count = myMoneyService.fundCount(user.getId(), typeId,
                beginDate, endDate);
        page.setTotalCount(count);
        // 查询记录
        List list = myMoneyService.queryFund(page, user.getId(), typeId,
                beginDate, endDate);
        request.setAttribute("page", page);
        request.setAttribute("list", list);
        return "/WEB-INF/views/member/myMoney/details";
    }
}
