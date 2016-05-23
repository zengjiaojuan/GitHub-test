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
import com.phb.puhuibao.entity.MobileUserHexagram;

@Controller
@RequestMapping(value = "/mobileUserHexagram")
public class MobileUserHexagramController extends BaseController<MobileUserHexagram, String> {
	@Resource(name = "mobileUserHexagramService")
	public void setBaseService(IBaseService<MobileUserHexagram, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@javax.annotation.Resource(name = "mobileUserHexagramService")
	private IBaseService<MobileUserHexagram, String> mobileUserHexagramService;
	
	/**
	        获取用户六芒星数据
	* @param  muid 用户id
	*/
	@RequestMapping(value="getUserHexagram")
	@ResponseBody
	public Map<String, Object> getUserHexagram(@RequestParam String muid) {
	Map<String, Object> data = new HashMap<String, Object>();
	try {
		MobileUserHexagram hexagram = mobileUserHexagramService.getById(muid);
		data.put("result", hexagram);
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
