package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.context.AppContext;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.MobileUserSignin;
import com.phb.puhuibao.entity.UserExperience;
import com.phb.puhuibao.service.UserExperienceService;

 

@Controller
@RequestMapping(value = "/userExperience")
public class UserExperienceController extends BaseController<UserExperience, String> {
	private static final Log LOG = LogFactory.getLog(UserExperienceController.class);
	@Override
	@Resource(name = "userExperienceService")
	public void setBaseService(IBaseService<UserExperience, String> baseService) {
		super.setBaseService(baseService);
	}
	
	private final static boolean[] CHANCE = {true, false, false, false, false, false, false, false, false, false};
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name = "mobileUserSigninService")
	private IBaseService<MobileUserSignin, String> mobileUserSigninService;
	
	@Resource(name = "userExperienceService")
	private IBaseService<UserExperience, String> userExperienceService;
	
	
	@javax.annotation.Resource(name = "userExperienceService")
	private UserExperienceService UserExperienceServiceimpl;
	
	@Resource(name = "appContext")
	private AppContext appContext;
	
	/**
	 * 体验金
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="myExperienceAmount")
	@ResponseBody
	public Map<String, Object> myExperienceAmount(@RequestParam String muid) {
		 Map<String, Object> params = new HashMap<String, Object>();
		 Map<String, Object> data = new HashMap<String, Object>();
			params.put("mUserId", muid);
			params.put("status", 3);                // 获得用户所有奖金总和
			UserExperience usersumexperience = userExperienceService.unique(params); 
			if(usersumexperience==null){
				data.put("result", 0);
			}else{
				data.put("result", usersumexperience.getExperienceAmount());
			}
			data.put("message", "成功");
			data.put("status", 1);
			return data;
		 
	}
	
	
	
	@RequestMapping(value="getChanceAward")
	@ResponseBody
	public Map<String, Object> getChanceAward(@RequestParam int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUserSignin entity = mobileUserSigninService.getById("" + muid);
		if(entity==null){
			data.put("message", "用户不存在！");
			data.put("status", 0);
			return data;
		}
		if (entity.getTotalIntegral() <= entity.getUsedIntegral()) {
			data.put("message", "您没有可用的积分！");
			data.put("status", 1);
			return data;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("status", 1);
		UserExperience experience = userExperienceService.unique(params);
		int award = 0;
		if (experience == null ) { // 第一次刮奖,必中
			int[] firstoption = {123,666,888};
			int first =  (int)(Math.random()*(3));
			award = firstoption[first];
		}  else{
			int i = (int) (Math.random() * 10);
			int upMoney = appContext.getUpMoney();
			if (CHANCE[i]) {                                        // 10%的概率获得大数目奖金
				award = (int) (Math.random() * upMoney) + 1;
			}else{                                                 // 90%概率获得小数目奖金
				award = (int) (Math.random() * 100) + 1;
			}
		}

		try {
			int experienceid = UserExperienceServiceimpl.processChanceAward(muid, award);
		    if(experienceid==0){
				data.put("message", "刮奖失败");
				data.put("status", 0);
				return data;
		    }else{
		    	data.put("result", award);
				data.put("experienceid", experienceid);
				data.put("message", "恭喜中奖！");
				data.put("status", 1);
				return data;
		    	
		    }
			
			
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getStackTrace());
			data.put("message", "失败"+e.getMessage());
			data.put("status", 0);
			return data;
		}
	}
	
 
}
