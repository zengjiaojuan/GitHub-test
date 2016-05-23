package com.phb.puhuibao.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.ResourceNotice;
import com.phb.puhuibao.entity.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
@Controller
@RequestMapping(value = "/resourceNotice")
public class ResourceNoticeController extends BaseController<ResourceNotice, String> {
	private static final Log log = LogFactory.getLog(ResourceNoticeController.class);
	
	@javax.annotation.Resource(name = "resourceNoticeService")
	public void setBaseService(IBaseService<ResourceNotice, String> baseService) {
		super.setBaseService(baseService);
	}
 
	@javax.annotation.Resource(name = "resourceService")
	private IBaseService<Resource, String> resourceService;
 
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	

 
	
	
	/**
                       当前用户的任务消息
	 */

	@RequestMapping(value="getNotices")
	@ResponseBody
	public Map<String, Object> getNotices(@RequestParam int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("noticeUser", muid);
		params.put("orderBy", "notice_time");
		params.put("order", "desc");
		try {
			List<ResourceNotice> notices = this.getBaseService().findList(params);
			data.put("result", notices);
			data.put("message", "");
			data.put("status", 1);
			return data;
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}
	}
 
	/**
                    点击 "阅读"
	 */

	@RequestMapping(value="read")
	@ResponseBody
	public Map<String, Object> read(@RequestParam int noticeId) {
		Map<String, Object> data = new HashMap<String, Object>();
		    ResourceNotice entity = new ResourceNotice();
		    entity.setNoticeId(noticeId);
		    entity.setIsRead(1);
			try {
				this.getBaseService().update(entity);
			} catch (Exception e) {
				data.put("message", "网络异常！");
				data.put("status", 0);
				return data;
			}
			data.put("result", "");
			data.put("message", "阅读成功！");
			data.put("status", 1);
		return data;
	}
	
	
	 
	
	
	/**
                    删除 通知
	 */
	@RequestMapping(value="deleteNotice")
	@ResponseBody
	public Map<String, Object> deleteNotice(@RequestParam int noticeId) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("noticeId", noticeId);
		int count = this.getBaseService().delete(params);
		Map<String, Object> data = new HashMap<String, Object>();
		if (count == 0) {
			data.put("message", "已经删除!");
			data.put("status", 0);
		} else {
			data.put("message", "删除成功！");			
			data.put("status", 1);
		}
		return data;
	}
	 
 
 
}
