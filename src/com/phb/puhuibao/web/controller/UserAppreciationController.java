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
import com.phb.puhuibao.entity.UserAppreciation;

@Controller
@RequestMapping(value = "/userAppreciation")
public class UserAppreciationController extends BaseController<UserAppreciation, String> {
	@Resource(name = "userAppreciationService")
	public void setBaseService(IBaseService<UserAppreciation, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam int appreciationId) {
		Map<String, Object> data = new HashMap<String, Object>();

		UserAppreciation entity = new UserAppreciation();
		entity.setmUserId(muid);
		entity.setAppreciationId(appreciationId);
		
		try {
		    entity = this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}

		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}

}
