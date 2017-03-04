package com.cddgg.p2p.huitou.spring.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.p2p.core.entity.CalculateLoan;
import com.cddgg.p2p.huitou.admin.spring.service.ColumnManageService;
import com.cddgg.p2p.huitou.entity.Deputysection;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.util.Arith;

/**   
 * Filename:    ArithAction.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年3月25日 下午2:56:55   
 * Description:  
 *      
 */

@Controller
@RequestMapping("/arith")
public class ArithAction {
    
    @Resource
    ColumnManageService columnservice;
    
    /**
    * <p>Title: loanCalculate</p>
    * <p>Description: 借贷工具计算器</p>
    * @param loansign 计算必需的参数
    * @param request HttpServletRequest
    * @return 结果展示页面
    */
    @RequestMapping("/loan_Calculate")
    public String loanCalculate(Loansign loansign,HttpServletRequest request){
        loansign.setRate(loansign.getRate()/100);

        double sumMoney=0.00;
        List<CalculateLoan> datalist=new ArrayList<CalculateLoan>();
        //等额本息
        if(loansign.getRefundWay().intValue()==1){
            
            datalist=Arith.loanCalculate(loansign);
            request.setAttribute("datalist",datalist );
           
        }else if(loansign.getRefundWay().intValue()==2){//每月付息，到期还本
            datalist=Arith.getInterest(loansign);
            request.setAttribute("datalist",datalist);
        }else{//一次性还本息
            CalculateLoan calculateLoan=new CalculateLoan();
            calculateLoan.setBenjin(Arith.round(new BigDecimal(loansign.getIssueLoan()), 2));
            calculateLoan.setCount(1);
            calculateLoan.setLixi(Arith.round(Arith.mul(Arith.div(Arith.mul(loansign.getIssueLoan(), loansign.getRate()).doubleValue(), 12.0, 2).doubleValue(), loansign.getMonth().doubleValue()), 2));
            calculateLoan.setNotReturn(new BigDecimal(0.00));
            datalist.add(calculateLoan);
            request.setAttribute("datalist", datalist);
        }
        
        if(!datalist.isEmpty()){
            sumMoney=Arith.mul(Arith.add(datalist.get(0).getBenjin().doubleValue(),datalist.get(0).getLixi().doubleValue()).doubleValue(), loansign.getMonth().doubleValue()).doubleValue();
            
            //如果是每月付息到期还本
            if(loansign.getRefundWay().intValue()==2){
                if(datalist.size()>1){
                    sumMoney+=loansign.getIssueLoan();
                }
            }else if(loansign.getRefundWay().intValue()==3){
             
                sumMoney=datalist.get(0).getBenjin().doubleValue()+datalist.get(0).getLixi().doubleValue();
            }
            request.setAttribute("monthMoney", Arith.round(Arith.add(datalist.get(0).getBenjin().doubleValue(), datalist.get(0).getLixi().doubleValue()), 2));
        }
        
        if(loansign.getPmfeeratio()!=null && !"".equals(loansign.getPmfeeratio()) && loansign.getPmfeeratio()>0){
            loansign.setPmfeeratio(loansign.getPmfeeratio()/100);
            request.setAttribute("jiangli", Arith.round(Arith.mul(loansign.getIssueLoan(), loansign.getPmfeeratio()), 2));
        }
        
        if(loansign.getMfeeratio()!=null && !"".equals(loansign.getMfeeratio()) && loansign.getMfeeratio()>0){
            loansign.setMfeeratio(loansign.getMfeeratio()/100);
            request.setAttribute("jiangli2", Arith.round(Arith.mul(loansign.getIssueLoan(), loansign.getMfeeratio()), 2));
        }
        request.setAttribute("sumMoney", Arith.round(new BigDecimal(sumMoney+""), 2));
        request.setAttribute("loansign", loansign);
        request.setAttribute("topicId", 1);// 一级栏目id
        request.setAttribute("deputyId", 86);// 二级栏目id
        Deputysection deputy = columnservice.queryDeputyById(1);// 根据id查询二级栏目
        request.setAttribute("deputy", deputy);
        return "/WEB-INF/views/investor_tools";
    }
    
    /**
    * <p>Title: showPage</p>
    * <p>Description: 打开借贷工具页面</p>
    * @return借贷工具页面
    */
    @RequestMapping("/investor_tools.htm")
    public String showPage(HttpServletRequest request){
        
        Deputysection deputy = columnservice.queryDeputyById(1);// 根据id查询二级栏目
        request.setAttribute("topicId", 1);// 一级栏目id
        request.setAttribute("deputyId", 86);// 二级栏目id
        request.setAttribute("deputy", deputy);
        return "/WEB-INF/views/investor_tools";
    }
}
