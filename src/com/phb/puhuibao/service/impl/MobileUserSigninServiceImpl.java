package com.phb.puhuibao.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUserSignin;
import com.phb.puhuibao.entity.UserExperience;
import com.phb.puhuibao.service.MobileUserSigninService;

@Transactional
@Service("mobileUserSigninService")
public class MobileUserSigninServiceImpl extends DefaultBaseService<MobileUserSignin, String> implements MobileUserSigninService{
	@Override
	@Resource(name = "mobileUserSigninDao")
	public void setBaseDao(IBaseDao<MobileUserSignin, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "userExperienceDao")
	private IBaseDao<UserExperience, String> userExperienceService;
	
	
	// 保存理财体验金
	@Override
	public void processUpdate(UserExperience experience) {
		 Map<String, Object> params = new HashMap<String, Object>();

			try {
				//将刚才的刮奖置为有效
				UserExperience ex = new UserExperience();
				ex.setExperienceId(experience.getExperienceId());
				ex.setStatus(1);
			    userExperienceService.update(ex);
			    
			    //修改用户奖金总额
			   
				params.put("mUserId", experience.getmUserId());
				params.put("status", 3);                // 获得用户所有奖金总和
				UserExperience usersumexperience = userExperienceService.unique(params);
				if(usersumexperience==null){// 第一次刮奖
					UserExperience updateusersum = new UserExperience();
				  
					updateusersum.setmUserId(experience.getmUserId());
					updateusersum.setStatus(3);                           //刮奖为0  保存为1 总和为3 到期为4
					updateusersum.setExperienceAmount(experience.getExperienceAmount());
					 
			        String lastday =  "9999-01-01";
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					date = format.parse(lastday);
					updateusersum.setLastDate(date);
					userExperienceService.save(updateusersum);      
				}else{
					UserExperience updateusersum = new UserExperience();
					updateusersum.setExperienceId(usersumexperience.getExperienceId());
					updateusersum.setExperienceAmount(usersumexperience.getExperienceAmount()+experience.getExperienceAmount());  //原值+增额
					userExperienceService.update(updateusersum);
					
				}
				//签到次数减一
				params = new HashMap<String, Object>();
				params.put("mUserId", experience.getmUserId());
				MobileUserSignin entity = this.getBaseDao().unique(params);
				MobileUserSignin sigin = new MobileUserSignin();
				sigin.setmUserId(entity.getmUserId());
				sigin.setUsedIntegral(entity.getUsedIntegral() + 1);
				this.getBaseDao().update(sigin);
				
				} catch (ParseException e) {
				 
				e.printStackTrace();
			}
	}

}
