package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.CalculateFormula;


@Controller
@RequestMapping(value = "/calculateFormula")
public class CalculateFormulaController extends BaseController<CalculateFormula, String> {
	private static final Log log = LogFactory.getLog(CalculateFormulaController.class);
	
	@Resource(name = "calculateFormulaService")
	public void setBaseService(IBaseService<CalculateFormula, String> baseService) {
		super.setBaseService(baseService);
	}
 
	/**
             获得公式
	 */

	@RequestMapping(value="getFormula")
	@ResponseBody
	public Map<String, Object> getFormula(@RequestParam String formulaId) {
		Map<String, Object> data = new HashMap<String, Object>();
 
		
		try {
			CalculateFormula formula = this.getBaseService().getById(formulaId);
			if(formula==null){
				data.put("formula", "");
			}else{
				data.put("formula", formula.getFormulaText());
			}
			
			data.put("message", "");
			data.put("status", 1);
			return data;
		
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}
		
		
		 
	}
 
	 
	 
 
 
}
