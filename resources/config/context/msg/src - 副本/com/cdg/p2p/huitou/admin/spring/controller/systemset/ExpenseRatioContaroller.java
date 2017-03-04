package com.cddgg.p2p.huitou.admin.spring.controller.systemset;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cddgg.p2p.huitou.admin.spring.service.systemset.ExpenseRatioService;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Costratio;
import com.cddgg.p2p.huitou.util.Arith;
import com.cddgg.p2p.huitou.util.DwzResponseUtil;

/**
 * 费用比例设置
 * @author RanQiBing 2014-02-24
 *
 */
@Controller
@RequestMapping("/expenseRatio")
public class ExpenseRatioContaroller {
    
    @Resource
    private ExpenseRatioService expenseRatioService;
    
    /**
     * 打开费用比例设置页面
     * @param request request
     * @return 返回费用比例设置路径
     */
    @RequestMapping("openRatio.htm")
    public String openRatio(HttpServletRequest request){
        request.setAttribute("costratio", expenseRatioService.findCostratio());
        return "WEB-INF/views/admin/fund/expense_ratio";
    }
    /**
     * 添加或修改
     * @param id 编号
     * @param oneyear 第一年
     * @param twoyear 第二年
     * @param threeyear 第三年
     * @return 返回处理信息
     */ 
    @RequestMapping("add.htm")
    @ResponseBody
    public JSONObject addCostratio(Costratio costratio){
        JSONObject json = new JSONObject();
        costratio.setDayRate(Arith.div(costratio.getDayRate(), 1000, 4).doubleValue());
        costratio.setMfeeratio(Arith.div(costratio.getMfeeratio(), 100, 4).doubleValue());
        costratio.setOther(Arith.div(costratio.getOther(), 1000, 4).doubleValue());
        costratio.setOverdueRepayment(Arith.div(costratio.getOverdueRepayment(), 100, 4).doubleValue());
        costratio.setWithinTwoMonths(Arith.div(costratio.getWithinTwoMonths(), 1000, 4).doubleValue());
        costratio.setPmfeeratio(Arith.div(costratio.getPmfeeratio(), 100, 4).doubleValue());
        costratio.setPrepaymentRate(Arith.div(costratio.getPrepaymentRate(), 100, 4).doubleValue());
        costratio.setOneyear(Arith.div(costratio.getOneyear(), 100, 4).doubleValue());
//        costratio.setRecharge(Arith.div(costratio.getRecharge(), 100, 4).doubleValue());
//        costratio.setViprecharge(Arith.div(costratio.getViprecharge(), 100, 4).doubleValue());
//        costratio.setWithdraw(Arith.div(costratio.getWithdraw(), 100, 4).doubleValue());
//        costratio.setVipwithdraw(Arith.div(costratio.getVipwithdraw(), 100, 4).doubleValue());
        try {
        	if(null!=costratio.getId()){
        		expenseRatioService.update(costratio);
        	}else{
        		expenseRatioService.save(costratio);
        	}
        	return DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS, "保存成功","#main31",null);
		} catch (Exception e) {
			return DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS, "保存失败","#main31",null);
		}	
    }
}
