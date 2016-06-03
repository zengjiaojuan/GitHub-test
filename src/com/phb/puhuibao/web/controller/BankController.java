package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.Bank;

@Controller
@RequestMapping(value = "/bank")
public class BankController extends BaseController<Bank, String> {
	@Override
	@Resource(name = "bankService")
	public void setBaseService(IBaseService<Bank, String> baseService) {
		super.setBaseService(baseService);
	}

	/**
	 * 银行列表
	 * @return
	 */
	@RequestMapping(value="findList")
	@ResponseBody
	public Map<String, Object> findList() {
		Map<String,Object> params = new HashMap<String,Object>();
		List<Bank> result = this.getBaseService().findList(params);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 银行列表
	 * @return
	 */
	@RequestMapping(value="bankLimitation")
	@ResponseBody
	public Map<String, Object> bankLimitation() {
		Map<String,Object> params = new HashMap<String,Object>();
		List<Bank> result = this.getBaseService().findList(params);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
 
	
}
