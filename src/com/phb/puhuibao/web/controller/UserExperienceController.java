package com.phb.puhuibao.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.UserExperience;

@Controller
@RequestMapping(value = "/userExperience")
public class UserExperienceController extends BaseController<UserExperience, String> {
	@Resource(name = "userExperienceService")
	public void setBaseService(IBaseService<UserExperience, String> baseService) {
		super.setBaseService(baseService);
	}
	
	/**
	 * 体验金
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="myExperienceAmount")
	@ResponseBody
	public Map<String, Object> myExperienceAmount(@RequestParam String muid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("status", 1);
		params.put("gelastDate", new Date());
		List<UserExperience> result = this.getBaseService().findList(params);
		int amount = 0;
		for (UserExperience experience : result) {
			amount += experience.getExperienceAmount();
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", amount);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam int experienceAmount, @RequestParam int status, @RequestParam Date lastDate) {
		UserExperience entity = new UserExperience();
		entity.setmUserId(muid);
		entity.setExperienceAmount(experienceAmount);
		entity.setStatus(status);
		entity.setLastDate(lastDate);

		Map<String, Object> data = new HashMap<String, Object>();
		try {
		    this.getBaseService().save(entity);
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
