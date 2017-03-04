package com.cddgg.p2p.huitou.spring.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.StringUtil;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Userloginlog;
import com.cddgg.p2p.huitou.entity.Usermessage;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.MemberCenterService;
import com.cddgg.p2p.huitou.spring.service.MyMoneyService;
import com.cddgg.p2p.huitou.spring.service.VipInfoService;

/**
 * 会员中心首页
 * 
 * @author ransheng
 * 
 */
@Controller
@RequestMapping(value = { "member_index", "/" })
@CheckLogin(value = CheckLogin.WEB)
@SuppressWarnings("rawtypes")
public class MemberCenterController {

    /**
     * 会员中心首页接口
     */
    @Resource
    private MemberCenterService memberCenterService;
    
    /**
     * vip会员接口
     */
    @Resource
    private VipInfoService vipInfoService;
    
    /**
     * 资金统计接口
     */
    @Resource
    private MyMoneyService myMoneyService;

    @Resource
    private HibernateSupport dao;
    
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
     * 会员中心首页基本信息
     * 
     * @param request
     *            请求
     * @return 返回.jsp
     */
    @RequestMapping("/member_center")
    public ModelAndView memberCenter(HttpServletRequest request) {
        // 取到登录用户sesssion
        Userbasicsinfo user = queryUser(request);
        // 查询用户基本信息
        user = memberCenterService.queryById(user.getId());
        // 查询用户是否设置安全问题
        boolean bool = memberCenterService.isSecurityproblem(user.getId());
        //是否为vip用户
        Object isVip=vipInfoService.isVip(user.getId());
        //查询用户积分
        Object score=memberCenterService.score(user.getId());
        request.setAttribute("bool", bool);
        request.setAttribute("user", user);
        //重新保存session的值
        request.getSession().setAttribute(Constant.SESSION_USER, user);
        request.setAttribute("isVip", isVip);
        request.setAttribute("score", score==null?0:score);
        
        //待收本金
        request.setAttribute("toBeClosed", myMoneyService.toBeClosed(user.getId()));
        //待收利息总额
        request.setAttribute("interestToBe", myMoneyService.interestToBe(user.getId()));
        //待付本息金额
        request.setAttribute("colltionPrinInterest", myMoneyService.colltionPrinInterest(user.getId()));
        //竞标中投资
        request.setAttribute("lentBid", memberCenterService.investmentRecords(user.getId(), 2));
        //累计逾期
        request.setAttribute("overude", myMoneyService.overude(user.getId()));
        //冻结金额
        request.setAttribute("netMark", myMoneyService.netMark(user.getId()));
        //待确认充值
        request.setAttribute("toRecharge", memberCenterService.rechargeTobe(user.getId()));
        //待确认提现
        request.setAttribute("toWithdraw", memberCenterService.noTransfer(user.getId()));
        //累计投资金额
        request.setAttribute("totalInvest", myMoneyService.investmentRecords(user.getId()));
        //回收中投资
        request.setAttribute("goingBack", memberCenterService.investmentRecords(user.getId(), 3));
        //收完的投资
        request.setAttribute("endBack", memberCenterService.investmentRecords(user.getId(), 4));
        //累计利息
        request.setAttribute("netInterest", myMoneyService.netInterest(user.getId()));
        //发标中借款
        request.setAttribute("goingLent", memberCenterService.borrowing(user.getId(), 2));
        //偿还中借款
        request.setAttribute("goingLented", memberCenterService.borrowing(user.getId(), 3));
        //还清的借款
        request.setAttribute("endLent", memberCenterService.borrowing(user.getId(), 4));
        //累计利息成本
        request.setAttribute("interest", memberCenterService.interest(user.getId()));
        //已用额度
        request.setAttribute("usedAmount", memberCenterService.usedAmount(user.getId()));
        
        
        return new ModelAndView("/WEB-INF/views/member/memberCenter");
    }

    /**
     * 查询用户系统消息
     * 
     * @param page
     *            分页对象
     * @param id
     *            消息id
     * @param unRead
     *            是否已读
     * @param request
     *            请求
     * @return 返回.jsp
     */
    @RequestMapping("/system_message")
    public ModelAndView querySystemMessage(
            @ModelAttribute("PageModel") PageModel page,
            @RequestParam(value = "id", defaultValue = "", required = false) Long id,
            HttpServletRequest request) {
        // 取到登录用户sesssion
        Userbasicsinfo user = queryUser(request);
        // 查询用户已读消息条数
        Object read = memberCenterService.queryIsReadCount(user.getId(), 1);
        // 查询用户系统消息
        List list = memberCenterService.queryUserMessage(user.getId(), page);
        //查询用户登录日志
        List logs=memberCenterService.queryLog(user.getId());
        request.setAttribute("list", list);
        request.setAttribute("page", page);
        request.setAttribute("count",page.getTotalCount());
        request.getSession().setAttribute("messagecount",page.getTotalCount()-Integer.parseInt(read.toString()));
        request.setAttribute("read", read);
        request.setAttribute("logs", logs);
        return new ModelAndView("/WEB-INF/views/member/systemMessage");
    }
    
    /**
     * ajax阅读消息
     * @param id
     * @param unRead 阅读状态
     * @param request
     * @return
     */
    @RequestMapping("/ajaxRead")
    @ResponseBody
    public boolean ajaxReadMsg(@RequestParam(value = "id", defaultValue = "", required = false) Long id,
            @RequestParam(value = "unRead", defaultValue = "", required = false) Integer unRead,
            HttpServletRequest request){
    	Userbasicsinfo user = queryUser(request);
        // 如果查看单条信息
        if (id != null && unRead != null && !id.toString().trim().equals("")
                && !unRead.toString().trim().equals("")) {
            Usermessage message = memberCenterService.queryById(id, unRead);
            request.getSession().setAttribute("messagecount",memberCenterService.queryIsReadCount(user.getId(), 0));
            if(message!=null){
            	 return true;
            }
        }
        return false;
    }

    /**
     * 根据编号删除系统消息
     * 
     * @param page
     *            分页对象
     * @param ids
     *            多个会员编号，以逗号分开
     * @param request
     *            请求
     * @return 返回视图.jsp
     */
    @RequestMapping("/deletes")
    public ModelAndView deletes(
            @ModelAttribute("PageModel") PageModel page,
            @RequestParam(value = "ids", defaultValue = "", required = false) String ids,
            HttpServletRequest request) {
        try {
            // 删除系统消息
            memberCenterService.deletes(ids);
        } catch (Exception e) {
            e.getMessage();
        }
        return querySystemMessage(page, null, request);
    }
    
    /**
     * ajax加载我的首页数据
     * @param request
     * @param response
     */
    @RequestMapping("/ajaxRecord")
    public void ajaxRecord(HttpServletRequest request,HttpServletResponse response){
    	JSONObject json=new JSONObject();
    	PrintWriter out=null;
    	try {
    		Userbasicsinfo user = queryUser(request);
			response.setContentType("text/html;charset=UTF-8");
			out=response.getWriter();
	        //累积支付(账号资金总额=可用现金金额+待确认投标+待确认提现-累计支付)
	        Object payment=memberCenterService.payment(user.getId());
	        json.accumulate("payment", payment);
	        //待确认提现(冻结金额=竞标中借出+待确认提现)
	        Object noTransfer=memberCenterService.noTransfer(user.getId());
	        json.accumulate("noTransfer", noTransfer);
	        
	        //投资记录 1未发布、2进行中、3回款中、4已完成
	        //竞标中投资
	        Object lentBid=memberCenterService.investmentRecords(user.getId(), 2);
	        json.accumulate("lentBid", lentBid);
	        //回收中投资
	        Object recoveryLoan=memberCenterService.investmentRecords(user.getId(), 3);
	        json.accumulate("recoveryLoan", recoveryLoan);
	        //收完的投资
	        Object harvestedLoan=memberCenterService.investmentRecords(user.getId(), 4);
	        json.accumulate("harvestedLoan", harvestedLoan);
	        //累计收益
	        Object inCome=myMoneyService.netInterest(user.getId());
	        json.accumulate("inCome", inCome);
	        
	        //借款记录 1未发布、2进行中、3回款中、4已完成
	        //发标中借款
	        Object issuingThe=memberCenterService.borrowing(user.getId(), 2);
	        json.accumulate("issuingThe", issuingThe);
	        //偿还中借款
	        Object repaymentThe=memberCenterService.borrowing(user.getId(), 3);
	        json.accumulate("repaymentThe", repaymentThe);
	        //还清的借款
	        Object borrowed=memberCenterService.borrowing(user.getId(), 4);
	        json.accumulate("borrowed", borrowed);
	        //累计利息成本
	        Object interest=memberCenterService.interest(user.getId());
	        json.accumulate("interest", interest);
	        //已用额度
	        Object usedAmount=memberCenterService.usedAmount(user.getId());
	        json.accumulate("usedAmount", usedAmount);
			out.print(json);
		} catch (Throwable e) {
			json.element("msg","读取数据出错！");
			out.print(json);
		}
    }
    
    @ResponseBody
    @RequestMapping("/latest_user")
    public String queryLoginUser(HttpServletRequest request){
        // 取到登录用户sesssion
        Userbasicsinfo user = queryUser(request);
        
        // 查询用户基本信息
        user = memberCenterService.queryById(user.getId());
        
        return user.getUserfundinfo()==null?"0.00":""+user.getUserfundinfo().getCashBalance();
        
    }
}
