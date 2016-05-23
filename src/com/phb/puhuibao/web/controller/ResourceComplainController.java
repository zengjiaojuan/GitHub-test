package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.ResourceComplain;

@Controller
@RequestMapping(value = "/resourceComplain")
public class ResourceComplainController extends BaseController<ResourceComplain, String> {
	@javax.annotation.Resource(name = "resourceComplainService")
	public void setBaseService(IBaseService<ResourceComplain, String> baseService) {
		super.setBaseService(baseService);
	}

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;	
	
	@RequestMapping(value="getComplainCauses")
	@ResponseBody
	public Map<String, Object> getComplainCauses() {
		String sql = "select cause_id,content from phb_resource_complain_cause";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam int resourceId, @RequestParam int causeId, @RequestParam String content, @RequestParam String pictures) {
		ResourceComplain complain = new ResourceComplain();
		complain.setmUserId(muid);
		complain.setResourceId(resourceId);
		complain.setCauseId(causeId);
		complain.setContent(content);
		complain.setPictures(pictures);
		this.getBaseService().save(complain);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
}
