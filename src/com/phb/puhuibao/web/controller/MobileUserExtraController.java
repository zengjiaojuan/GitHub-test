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
import com.phb.puhuibao.entity.MobileUserExtra;

@Controller
@RequestMapping(value = "/mobileUserExtra")
public class MobileUserExtraController extends BaseController<MobileUserExtra, String> {
	@Resource(name = "mobileUserExtraService")
	public void setBaseService(IBaseService<MobileUserExtra, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@RequestMapping(value="getExtra")
	@ResponseBody
	public Map<String, Object> getExtra(@RequestParam String muid) {
		MobileUserExtra entity = this.getBaseService().getById(muid);
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
