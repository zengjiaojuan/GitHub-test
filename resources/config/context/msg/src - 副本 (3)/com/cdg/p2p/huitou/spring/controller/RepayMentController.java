package com.cddgg.p2p.huitou.spring.controller;

import java.text.ParseException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.RepayMentServices;

/**
 * 前台还款
 * @author RanQiBing
 * 2014-05-14
 *
 */
@Controller
@CheckLogin(value = CheckLogin.WEB)
@RequestMapping("/repayments")
public class RepayMentController {

	@Resource
	private RepayMentServices repayMentServices;
	/**
	 * 还款 
	 * @param id 还款记录编号
	 * @return 返回路径
	 */
	@RequestMapping("repayment.htm")
	public String repayment(HttpServletRequest request){
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		//提前还款
		try {
			request.setAttribute("advance",repayMentServices.advanceRepayment(user.getId()));
			request.setAttribute("schedule",repayMentServices.scheduleRepayment(user.getId()));
		} catch (ParseException e) {
			e.printStackTrace();
			request.setAttribute("advance",null);
			request.setAttribute("schedule",null);
		}
		//按时还款
		//逾期还款
		request.setAttribute("overdue",repayMentServices.overdueRepayment(user.getId()));
		//借款总额
		request.setAttribute("money",repayMentServices.getMoney(user.getId()));
		//还款标总数
		request.setAttribute("num",repayMentServices.getNum(user.getId()));
		return "WEB-INF/views/member/loanmanagement/repayment";
	}
}
