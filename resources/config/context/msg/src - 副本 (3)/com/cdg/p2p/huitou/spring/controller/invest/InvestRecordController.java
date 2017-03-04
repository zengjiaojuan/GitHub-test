package com.cddgg.p2p.huitou.spring.controller.invest;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cddgg.base.model.PageModel;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Loanrecord;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.service.borrow.RepayPlanService;
import com.cddgg.p2p.huitou.spring.service.invest.InvestService;
import com.cddgg.p2p.huitou.spring.util.Arith;
import com.cddgg.p2p.huitou.util.LoanUncollected;


/**
 * 投资记录
 * @author My_Ascii
 *
 */
@Controller
@RequestMapping("depositshistory")
@CheckLogin(value = CheckLogin.WEB)
public class InvestRecordController {
    
    /**
     * 注入InvestService
     */
    @Resource
    private InvestService investService;

    /**
     * 注入InvestService
     */
    @Resource
    private RepayPlanService repayPlanService;
    /**
     * 初始化投资记录——投标中的项目
     * @throws ParseException 
     * @throws Exception 
     */
    @RequestMapping("init_two")
    public String initshowtwo(Model model, HttpServletRequest request,@RequestParam(value="no",required=false,defaultValue="1")Integer no) throws ParseException{
        
        //获取user信息
        Userbasicsinfo user= (Userbasicsinfo) request.getSession().getAttribute("session_user");
        //获取竞标中列表
        List loanrecordlist=investService.getLoanRecord(2,user.getId(),(no-1)*10,1);
        
        List<Object> loannlist=investService.getLoanGlF(loanrecordlist);
        
        PageModel pager = getPager(no, investService.getLoanRecord(2,user.getId()));
        request.setAttribute("loanrecordlist", loannlist);
        request.setAttribute("pager", pager);
        //总标数
        model.addAttribute("count", loanrecordlist.size());
        double count_money=investService.getSumTenderMoney(loannlist);
        //总投资额
        model.addAttribute("count_money", Arith.round(count_money,2));
        return "/WEB-INF/views/member/invest/Count_invest_list1";
    }
    
    /**
     * 得到分页对象
     * @param curPage
     * @param total
     * @return
     * @author hulicheng
     * 2013-5-9
     * Page
     */
    private PageModel getPager(int curPage, long total){
        PageModel pager = new PageModel();
        pager.setPageNum(curPage);
        pager.setNumPerPage(Constant.PAGE_SIZE_RECHARGE_RECORD);
        pager.setTotalCount(Integer.parseInt(total+""));
        return pager;
    }
    
    /**
     * 初始化投资记录——收款中的项目
     * @throws ParseException 
     * @throws Exception 
     */
    @RequestMapping("init_three")
    public String initshowthree(Model model, HttpServletRequest request,@RequestParam(value="no",required=false,defaultValue="1")Integer no) throws ParseException{
        
        //获取user信息
        Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute("session_user");
        List loanrecordlist=investService.getLoanRecord(3, user.getId(), (no-1)*10, 2);
        
        List<Object> loannlist=investService.getLoanGlF(loanrecordlist);
        PageModel pager = getPager(no, investService.getLoanRecord(3, user.getId()));
        request.setAttribute("loanrecordlist", loannlist);
        request.setAttribute("pager", pager);
        //总标数
        model.addAttribute("count", loanrecordlist.size());
        double count_money=investService.getMoney(loannlist);
        //总投资额
        model.addAttribute("count_money", Arith.round(count_money,2));
        
        //平均收益率
        double pjsyl=investService.getInterests_pj(loanrecordlist,request);
        model.addAttribute("pjsyl", Arith.round(pjsyl,2));
        //最后一次还款计划
        Repaymentrecord rr=investService.getLastRuturnRepay(loanrecordlist);
        model.addAttribute("lastRepay", rr);
        
        return "/WEB-INF/views/member/invest/Count_invest_list2";
    }
    
    /**
     * 
     * 初始化投资记录——未收款明细
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("init_Four")
    public String initshowFour(Model model, HttpServletRequest request,@RequestParam(value="no",required=false,defaultValue="1")Integer no){
        //获取user信息
        Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute("session_user");
        PageModel pager = getPager(no,investService.getNumrepauy(user.getId()));
        List<Object[]> loanlist=investService.getRepay(user.getId(),(no-1)*10);
        List<LoanUncollected> loannlist=investService.getLoanUncollected(loanlist);
        //总标数
        model.addAttribute("count", loannlist.size());
        //总投资额
        double count_money=investService.getTotal(loannlist);
        model.addAttribute("count_money", Arith.round(count_money,2));
        model.addAttribute("loannlist", loannlist);
        request.setAttribute("loannlist", loannlist);
        request.setAttribute("pager", pager);
        return "/WEB-INF/views/member/invest/Count_invest_list3";
    }
    
    /**
     * 
     * 初始化投资记录——已收款明细
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("init_five")
    public String initshowFive(Model model, HttpServletRequest request,@RequestParam(value="no",required=false,defaultValue="1")Integer no){
        //获取user信息
        Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute("session_user");
        //---竞标中数据开始--
        List<Object[]> loanlist=investService.getRepayW(user.getId(), (no-1)*10);
        List<LoanUncollected> loannlist=investService.getAllLoanDetails(loanlist);
        PageModel pager = getPager(no,investService.getNum(user.getId()));
        //总标数
        model.addAttribute("count", loannlist.size());
        //总投资额
        double count_money=investService.getTotal(loannlist);
        model.addAttribute("count_money", Arith.round(count_money,2));
        request.setAttribute("loannlist", loannlist);
        request.setAttribute("pager", pager);
        return "/WEB-INF/views/member/invest/Count_invest_list4";
    }
    
    /**
     * 
     * 初始化投资记录——借出明细
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("init_six")
    public String initshowSix(Model model, HttpServletRequest request,@RequestParam(value="no",required=false,defaultValue="1")Integer no){
        //获取user信息
        Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute("session_user");
        List<Loanrecord> loanrecordlist=investService.getLoanRecord(0,user.getId(),(no-1)*10,4);
        model.addAttribute("loanrecordlist", loanrecordlist);
        
        //获取借出明细
        List<LoanUncollected> loannlist=investService.getLentDetails(loanrecordlist);
        
        PageModel pager = getPager(no, investService.getLoanRecord(0,user.getId()));
        //总标数
        model.addAttribute("count", loannlist.size());
        //总投资额
        double count_money=investService.getTenderMoney(loannlist);
        model.addAttribute("count_money", Arith.round(count_money,2));
        request.setAttribute("loannlist",loannlist);
        request.setAttribute("pager", pager);
        return "/WEB-INF/views/member/invest/Count_invest_list5";
    }
    
    /**
     * 初始化投资记录——已还清项目
     * @throws ParseException 
     * @throws Exception 
     */
    @RequestMapping("init_seven")
    public String initshowfour(Model model, HttpServletRequest request,@RequestParam(value="no",required=false,defaultValue="1")Integer no) throws ParseException{
        
        //获取user信息
        Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute("session_user");
        
        List loanrecordlist=investService.getLoanRecord(4,user.getId(),(no-1)*10,3);
        
        loanrecordlist=investService.getLoanGlF(loanrecordlist);
        
        PageModel pager = getPager(no,investService.getLoanRecord(4, user.getId()));
        //总标数
        model.addAttribute("count", loanrecordlist.size());
        double count_money=investService.getRealMoney(loanrecordlist);
        //总投资额
        model.addAttribute("count_money", Arith.round(count_money,2));
        request.setAttribute("loanrecordlist", loanrecordlist);
        request.setAttribute("pager", pager);
        return "/WEB-INF/views/member/invest/Count_invest_list6";
    }
}
