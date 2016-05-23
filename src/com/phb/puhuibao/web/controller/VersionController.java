package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.Version;

@Controller
@RequestMapping(value = "/version")
public class VersionController extends BaseController<Version, String> {
	@Resource(name = "versionService")
	public void setBaseService(IBaseService<Version, String> baseService) {
		super.setBaseService(baseService);
	}

	/**
	 * 最新版本
	 * @return
	 */
	@RequestMapping(value="getVersion")
	@ResponseBody
	public Map<String, Object> getVersion() {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("orderBy", "create_time");
		params.put("order", "desc");
		Version result = this.getBaseService().unique(params);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}	
}
