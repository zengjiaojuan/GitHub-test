package com.phb.puhuibao.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.context.AppContext;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserExtra;
import com.phb.puhuibao.entity.MobileUserSignin;
import com.phb.puhuibao.entity.UserExperience;

@Controller
@RequestMapping(value = "/mobileUserSignin")
public class MobileUserSigninController extends BaseController<MobileUserSignin, String> {
	private final static boolean[] CHANCE = {true, true, true, false, false, false, false, false, false, false};
	
	@Resource(name = "mobileUserSigninService")
	public void setBaseService(IBaseService<MobileUserSignin, String> baseService) {
		super.setBaseService(baseService);
	}

	@Resource(name = "mobileUserExtraService")
	private IBaseService<MobileUserExtra, String> mobileUserExtraService;

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "userExperienceService")
	private IBaseService<UserExperience, String> userExperienceService;

	@Resource(name = "appContext")
	private AppContext appContext;
	
	@RequestMapping(value="getChanceAward")
	@ResponseBody
	public Map<String, Object> getChanceAward(@RequestParam int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUserSignin entity = this.getBaseService().getById("" + muid);
		if (entity == null || entity.getTotalIntegral() <= entity.getUsedIntegral()) {
			data.put("message", "您没有可用的积分！");
			data.put("status", 0);
			return data;
		}
		int award = 0;
		int i = (int) (Math.random() * 10);
		int upMoney = appContext.getUpMoney();
		if (CHANCE[i]) {
			award = (int) (Math.random() * upMoney) + 1;
		}

		data.put("result", award);
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}
	
	@RequestMapping(value="saveChanceAward")
	@ResponseBody
	public Map<String, Object> saveChanceAward(@RequestParam int muid, @RequestParam int amount) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("status", 1);
		params.put("lastDate", "9999-01-01");
		UserExperience experience = userExperienceService.unique(params);
		if (experience == null) {
			experience = new UserExperience();
			experience.setmUserId(muid);
			experience.setStatus(1);
			experience.setExperienceAmount(amount);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    try {
				Date date = format.parse("9999-01-01");
				experience.setLastDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    userExperienceService.save(experience);
		} else {
			UserExperience e = new UserExperience();
			e.setExperienceId(experience.getExperienceId());
			e.setExperienceAmount(experience.getExperienceAmount() + amount);
			userExperienceService.update(e);
		}

		MobileUserSignin entity = this.getBaseService().getById("" + muid);
		entity.setmUserId(muid);
		entity.setUsedIntegral(entity.getUsedIntegral() + 1);
		this.getBaseService().update(entity);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", "已保存到您的体验金");
		data.put("status", 1);
		return data;
	}

	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUserSignin entity = this.getBaseService().getById("" + muid);
		if (entity == null) {
			entity = new MobileUserSignin();
			entity.setmUserId(muid);
			entity.setTotalIntegral(1);
			entity.setSigninDate(new Date());
			this.getBaseService().save(entity);
		} else {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String signinDate = format.format(entity.getSigninDate());
			String today = format.format(new Date());
			if (signinDate.equals(today)) {
				data.put("message", "今天您已签到！");
				data.put("status", 1);
				return data;
			}
			entity.setmUserId(muid);
			entity.setTotalIntegral(entity.getTotalIntegral() + 1);
			entity.setSigninDate(new Date());
			this.getBaseService().update(entity);
		}

		data.put("message", "签到成功");
		data.put("status", 1);
		return data;
	}

	/**
	 * 获取积分级别
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="getIntegral")
	@ResponseBody
	public Map<String, Object> getIntegral(@RequestParam String muid) {
		Map<String, Object> result = new HashMap<String, Object>();

		MobileUserSignin entity = this.getBaseService().getById(muid);
		int integral = 0;
		if (entity != null) {
			integral = entity.getTotalIntegral() - entity.getUsedIntegral();
		}
		result.put("integral", integral);

//		MobileUserExtra extra = mobileUserExtraService.getById("" + muid);
//		int level = 0;
//		if (extra != null && extra.getLevel() != null) {
//			level = extra.getLevel();
//		}
		MobileUser user = mobileUserService.getById(muid);
		int level = 0;
		if (user != null && user.getLevel() != null) {
			level = user.getLevel();
		}
		result.put("level", level);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
}
