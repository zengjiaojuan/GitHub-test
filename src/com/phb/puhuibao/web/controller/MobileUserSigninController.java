package com.phb.puhuibao.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.context.AppContext;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserSignin;
import com.phb.puhuibao.entity.UserExperience;
import com.phb.puhuibao.service.MobileUserSigninService;

@Controller
@RequestMapping(value = "/mobileUserSignin")
public class MobileUserSigninController extends BaseController<MobileUserSignin, String> {
	private static final Log LOG = LogFactory.getLog(MobileUserSigninController.class);
	
	@Override
	@Resource(name = "mobileUserSigninService")
	public void setBaseService(IBaseService<MobileUserSignin, String> baseService) {
		super.setBaseService(baseService);
	}
 

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "userExperienceService")
	private IBaseService<UserExperience, String> userExperienceService;
 
	
	@javax.annotation.Resource(name = "mobileUserSigninService")
	private MobileUserSigninService mobileUserSigninService;
	

	@Resource(name = "appContext")
	private AppContext appContext;
	

	// 保存刮奖其实是把那个无效刮奖置为有效
	@RequestMapping(value="saveChanceAward")
	@ResponseBody
	public Map<String, Object> saveChanceAward(@RequestParam int experienceid,@RequestParam int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		
		MobileUserSignin entity = this.getBaseService().getById("" + muid);
		if(entity==null){
			data.put("message", "用户尚未签到！");
			data.put("status", 0);
			return data;
		}
		if (entity.getTotalIntegral() <= entity.getUsedIntegral()) {
			data.put("message", "您没有可用的积分！");
			data.put("status", 0);
			return data;
		}
		
 
		UserExperience experience = userExperienceService.getById(experienceid+"");
		if (experience == null) {//不存在这个刮奖
			data.put("message", "不存在这个奖!");
			data.put("status", 0);
			return data;
			
		}else if(experience.getStatus()!=0){
			data.put("message", "状态错误!");
			data.put("status", 0);
			return data;
		}else if(experience.getmUserId()!=muid){
			data.put("message", "不是你的彩金!");
			data.put("status", 0);
			return data;
		} else {
			try {
				mobileUserSigninService.processUpdate(experience);
				data.put("message", "已保存到您的体验金");
				data.put("status", 1);
				return data;
			} catch (Exception e) {
				LOG.error(e.getStackTrace());
				data.put("message", "失败!");
				data.put("status", 0);
				return data;
			}
		}
	}

	
	/* 保存签到
	 *
	*/
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUserSignin entity = this.getBaseService().getById("" + muid);
		try {
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
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error(e.getStackTrace());
			data.put("message", "签到失败");
			data.put("status", 0);
			return data;
		}
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
