package com.cddgg.p2p.huitou.spring.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cddgg.base.model.PageModel;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.MyExpensesService;

/**
 * 我的收支单
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/expenses")
@CheckLogin(value = CheckLogin.WEB)
@SuppressWarnings("rawtypes")
public class MyExpensesController {
	
	/**
	 * 注入Service
	 */
	@Resource
	MyExpensesService myExpensesService;
	
	/**
	 * 我的收支单
	 * @param beginTime
	 * @param endTime
	 * @param typeId 事件类型
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping("myexpenses.htm")
	public String queryExpenses(String beginTime,String endTime,Long typeId,
			HttpServletRequest request,Integer pageNum){
		Userbasicsinfo user=
				(Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		PageModel page=new PageModel();
		page.setPageNum(pageNum==null?1:pageNum);
		//记录条数
		Object obj=myExpensesService.getCount(beginTime, endTime, typeId,user.getId());
		page.setTotalCount(Integer.parseInt(obj.toString()));
		//收支记录集合
		if("".equals(page.getPageNum())){
			page.setPageNum(1);
		}
		List expenses=myExpensesService.queryExpense(user.getId(), beginTime, endTime, typeId, page);
		//事件类型
		List types=myExpensesService.queryType(user.getId());
		
		request.setAttribute("page", page);
		request.setAttribute("expenses", expenses);
		request.setAttribute("types", types);
		request.setAttribute("beginTime", beginTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("typeId", typeId);
		return "WEB-INF/views/member/expenses";
	}
}
