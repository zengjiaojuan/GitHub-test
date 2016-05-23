package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.ConsumeDiscount;

@Controller
@RequestMapping(value = "/consumeDiscount")
public class ConsumeDiscountController extends BaseController<ConsumeDiscount, String> {
	@Resource(name = "consumeDiscountService")
	public void setBaseService(IBaseService<ConsumeDiscount, String> baseService) {
		super.setBaseService(baseService);
	}

	@RequestMapping(value="getDiscount")
	@ResponseBody
	public Map<String, Object> getDiscount(@RequestParam String appreciationId, @RequestParam int level) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("appreciationId", appreciationId);
		params.put("level", level);
		ConsumeDiscount entity = this.getBaseService().unique(params);
		Map<String, Object> data = new HashMap<String, Object>();
		if (entity == null) {
			data.put("message", "读取失败！");
			data.put("status", 0);
		} else {
			data.put("result", entity);
			data.put("message", "成功");
			data.put("status", 1);
		}
		return data;
	}
}
