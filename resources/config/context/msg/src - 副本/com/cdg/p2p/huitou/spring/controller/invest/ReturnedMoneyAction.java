package com.cddgg.p2p.huitou.spring.controller.invest;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.util.StringUtil;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.ReturnedMoneyService;
import com.cddgg.p2p.huitou.util.DateUtil;

@Controller
@CheckLogin(value=CheckLogin.WEB)
@RequestMapping("/contract")
public class ReturnedMoneyAction {

   /** returnedMoneyService 回款计划明细查询*/
   @Resource
   private ReturnedMoneyService returnedMoneyService;

   
    /**
    * <p>Title: getReceivablePlan</p>
    * <p>Description: 回款计划明细查询</p>
    * @param request HttpServletRequest
    * @param minMoeny 最小金额
    * @param maxMoney 最大金额
    * @param minTime 最小时间
    * @param maxTime 最大时间
    * @param pageModel 分页模型
    * @return 数据展示页面
    */
    @RequestMapping("/query_page")
    public String getReceivablePlan(HttpServletRequest request,String minMoeny,String maxMoney,String minTime,String maxTime,PageModel pageModel){
        
        Userbasicsinfo userbasicsinfo=(Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
        
        if(StringUtil.isBlank(minMoeny)){
            minMoeny="0";
        }
        if(StringUtil.isBlank(maxMoney)){
            maxMoney="2000000";
        }
        if(StringUtil.isBlank(minTime)){
            minTime=DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), -12);
            if(StringUtil.isBlank(maxTime)){
                maxTime=DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), 36);
            }
        }else if("a".equals(minTime)){
            minTime=DateUtil.format("yyyy-MM-dd");
            maxTime=DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), 1);
            request.setAttribute("quick", "a");
        }else if("b".equals(minTime)){
            minTime=DateUtil.format("yyyy-MM-dd");
            maxTime=DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), 3);
            request.setAttribute("quick", "b");
        }else if("c".equals(minTime)){
            minTime=DateUtil.format("yyyy-MM-dd");
            maxTime=DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), 6);
            request.setAttribute("quick", "c");
        }else if("d".equals(minTime)){
            minTime=DateUtil.format("yyyy-MM-dd");
            maxTime=DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), 36);
            request.setAttribute("quick", "d");
        }
        if(StringUtil.isBlank(maxTime)){
            maxTime=DateUtil.getSpecifiedMonthAfter(minTime, 12);
        }
        
        List dataList=returnedMoneyService.getReceivablePlan(userbasicsinfo.getId().toString(), minMoeny, maxMoney, minTime, maxTime, pageModel);
        
        request.setAttribute("datalist", dataList);
        request.setAttribute("page", pageModel);
        
        returnedMoneyService.amountMoney(request);
        request.setAttribute("minTime", minTime);
        request.setAttribute("minMoeny", minMoeny);
        request.setAttribute("maxTime", maxTime);
        request.setAttribute("maxMoney", maxMoney);
        
        return "/WEB-INF/views/member/investor";
    }
}
