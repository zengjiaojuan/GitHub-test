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
import com.phb.puhuibao.entity.ResourceType;

@Controller
@RequestMapping(value = "/resourceType")
public class ResourceTypeController extends BaseController<ResourceType, String> {
	@Resource(name = "resourceTypeService")
	public void setBaseService(IBaseService<ResourceType, String> baseService) {
		super.setBaseService(baseService);
	}

	/**
	 * findList
	 * @param type, 类型：0=供，1=需
	 * @return
	 */
	@RequestMapping(value="findList")
	@ResponseBody
	public Map<String, Object> findList() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderBy", "type_id");
		List<ResourceType> result = this.getBaseService().findList(params);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

}
