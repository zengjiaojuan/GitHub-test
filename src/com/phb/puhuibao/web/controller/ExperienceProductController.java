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
import com.phb.puhuibao.entity.ExperienceProduct;

@Controller
@RequestMapping(value = "/experienceProduct")
public class ExperienceProductController extends BaseController<ExperienceProduct, String> {
	@Resource(name = "experienceProductService")
	public void setBaseService(IBaseService<ExperienceProduct, String> baseService) {
		super.setBaseService(baseService);
	}

	@RequestMapping(value="findList")
	@ResponseBody
	protected Map<String, Object> findList() {
		Map<String,Object> params = new HashMap<String,Object>();
		List<ExperienceProduct> result = this.getBaseService().findList(params);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
}
