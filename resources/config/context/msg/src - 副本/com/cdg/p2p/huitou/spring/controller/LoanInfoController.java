package com.cddgg.p2p.huitou.spring.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.util.StringUtil;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.huitou.admin.spring.service.UserInfoServices;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.service.LoanInfoService;
import com.cddgg.p2p.huitou.spring.service.LoanManageService;
import com.cddgg.p2p.huitou.util.Arith;

/**
 * 获取标的详细信息
 * @author RanQiBing 
 * 2014-04-11
 *
 */
@Controller
@RequestMapping("/loaninfo")
public class LoanInfoController {
	
	@Resource
	private LoanSignQuery loanSignQuery;
	
	@Resource
	private LoanInfoService loanInfoService;

	@Resource
	private UserInfoServices userInfoServices;

	@Resource
	private LoanManageService loanManageService;
	
	private Long loanId;

	/**
	 * 获取标的详细信息
	 * @param id 标编号
	 * @return 返回标详细信息页面
	 */
	@RequestMapping("getLoanInfo.htm")
	public String getLoanInfo(String id,HttpServletRequest request){
	    
	    if(StringUtil.isNotBlank(id)&&StringUtil.isNumberString(id)){
	        long idNum=0;
	        idNum=Long.parseLong(id);
	        Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
	        //获取标的详细信息
	        Loansign loan = loanSignQuery.getLoansignById(idNum+"");
	        
	        if(null==loan){
	            request.setAttribute("msg", "查询参数错误！！！！");
	            return "error-404";
	        }
	        //获取标的认购金额
	        request.setAttribute("scale",loanSignQuery.getLoanrecordMoneySum(idNum));
	        request.setAttribute("loan",loan);
	        //得到结束时间
//	      request.setAttribute("time",DateUtils.format("yyyy-MM-dd HH:mm:ss"));
//	      request.setAttribute("attachment",loanInfoService.getAttachment(id));
	        //得到还款记录
	        
	        request.setAttribute("creditRating", loanInfoService.getCreditRating(loan.getUserbasicsinfo().getId()));
	        if(null!=user){
	            //获取当前用户的最新信息
	            Userbasicsinfo userinfo = userInfoServices.queryBasicsInfoById(user.getId().toString());
	            
	            //判断当前用户最大认购份数
	            int maxcount=loanInfoService.getCount(loan, userinfo);
	            
	            request.setAttribute("maxMoney", maxcount*loan.getLoanUnit());
	            request.setAttribute("maxCopies", maxcount);
	            request.setAttribute("money",userinfo.getUserfundinfo().getMoney());
	            request.setAttribute("logo","logo");
	        }else{
	            request.setAttribute("maxMoney", 0);
	            request.setAttribute("maxCopies", 0);
	            request.setAttribute("money",0);
	            request.setAttribute("logo","logoNot");
	        }
	        
	        return "WEB-INF/views/member/loan/loaninfo";
	    }else{
	        request.setAttribute("msg", "查询参数错误！！！！");
	        return "error-404";
	    }
		
	}
	/**
	 * 查询该标的所有购买记录
	 * @param id 标编号
	 * @return 查询结果
	 */
	@RequestMapping("loanRecord.htm")
	public String getLoanrecord(Long id,Integer pageno,HttpServletRequest request){
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		if(null!=id){
			loanId = id;
		}
		PageModel page = new PageModel();
		page.setPageNum(pageno);
		page = loanInfoService.getLoanRecord(loanId, page);
		if(null!=user){
			request.setAttribute("page",page);
		}else{
			request.setAttribute("page",null);
		}
		return "WEB-INF/views/member/loan/loanrecord";
	}
	/**
	 * 获取该标的借款人的所有借款历史记录
	 * @param id 当前借款标号
	 * @param pageNo 当前页
	 * @return 返回页面路径
	 */
	@RequestMapping("loanSignRecord.htm")
	public String getLoanSignRecord(Long id,Integer pageNo,HttpServletRequest request){
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		if(null!=id){
			loanId = id;
		}
		PageModel page = new PageModel();
		page.setPageNum(pageNo);
		page = loanInfoService.getLoanSignRecord(loanId, page);
		if(null!=user){
			request.setAttribute("page",page);
		}else{
			request.setAttribute("page",null);
		}
		return "WEB-INF/views/member/loan/loansignrecord";
	}
	/**
	 * 查询所有标信息
	 * @param money 借款标金额
	 * @param month 借款标期限
	 * @param type 还款类型
	 * @param rank 信用度
	 * @param loanType 借款标类型
	 * @return 返回分页内容
	 */
	@RequestMapping("loanList.htm")
	public String getLoanlist(HttpServletRequest request,String money,String month,String type,String rank,String loanType,Integer no){
		PageModel page = new PageModel();
		page.setPageNum(no);
		page = loanManageService.getLoanList(money, month, type, rank, loanType, page);
		List<Object[]> objList = new ArrayList<Object[]>();
		if(null!=page.getList()&&page.getList().size()>0){
			for(int i=0;i<page.getList().size();i++){
				Object[] obj = (Object[]) page.getList().get(i);
				obj[2] = loanInfoService.getCreditRating(Long.parseLong(obj[2].toString()));
				objList.add(obj);
			}
		}
		page.setList(objList);
		request.setAttribute("page",page);
		return "WEB-INF/views/member/loan/loantable";
	}
	/**
	 * 打开借款标列表页面
	 * @return
	 */
	@RequestMapping("openLoan.htm")
	public String opLoanList(){
		return "WEB-INF/views/member/loan/loanlist";
	}
}
