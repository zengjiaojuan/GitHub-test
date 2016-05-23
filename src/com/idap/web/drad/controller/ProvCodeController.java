package com.idap.web.drad.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idap.drad.entity.ProvCode;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;

@Controller
@RequestMapping(value = "/provCode")
public class ProvCodeController extends BaseController<ProvCode, String> {
	@Resource(name = "provCodeService")
	public void setBaseService(IBaseService<ProvCode, String> baseService) {
		super.setBaseService(baseService);
	}

	@RequestMapping(value="findList")
	@ResponseBody
	protected Map<String, Object> findList(@RequestParam String keyword) {
		Map<String,Object> map=new HashMap<String,Object>();
		if (keyword.length() > 0) {
			map.put("querykeyword", keyword);
		}
		List<ProvCode> result = this.getBaseService().findList(map);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	@RequestMapping(value="getProvCode")
	@ResponseBody
	public Map<String, Object> getProvCode(@RequestParam String name) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("name", name);
		ProvCode entity = this.getBaseService().unique(params);
		Map<String, Object> data = new HashMap<String, Object>();
		if (entity == null) {
			data.put("result", "11");
			data.put("message", "查询失败，返回默认值！");
			data.put("status", 0);			
		} else {
			data.put("result", entity.getId());
			data.put("message", "查询成功！");
			data.put("status", 1);
		}
		return data;
	}
}
