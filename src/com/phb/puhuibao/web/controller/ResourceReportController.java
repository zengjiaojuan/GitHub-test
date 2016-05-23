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
import com.phb.puhuibao.entity.ResourceReport;

@Controller
@RequestMapping(value = "/resourceReport")
public class ResourceReportController extends BaseController<ResourceReport, String> {
	@javax.annotation.Resource(name = "resourceReportService")
	public void setBaseService(IBaseService<ResourceReport, String> baseService) {
		super.setBaseService(baseService);
	}

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;	
	
	@RequestMapping(value="getReportCauses")
	@ResponseBody
	public Map<String, Object> getReportCauses() {
		String sql = "select cause_id,content from phb_resource_report_cause";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam int resourceId, @RequestParam int causeId, @RequestParam String content) {
		ResourceReport report = new ResourceReport();
		report.setmUserId(muid);
		report.setResourceId(resourceId);
		report.setCauseId(causeId);
		report.setContent(content);
		this.getBaseService().save(report);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
}
