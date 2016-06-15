package com.phb.puhuibao.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserExperience;
import com.phb.puhuibao.service.UserExperienceService;

@Transactional
@Service("userExperienceService")
public class UserExperienceServiceImpl extends DefaultBaseService<UserExperience, String> implements UserExperienceService {
	@Override
	@Resource(name = "userExperienceDao")
	public void setBaseDao(IBaseDao<UserExperience, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	
	//获得理财体验金
	@Override
	public int processChanceAward(int muid,int award) {
		    UserExperience experience = new UserExperience();
			experience.setmUserId(muid);
			experience.setStatus(0);                           //刮奖为0  保存为1 总和为3 到期为4
			experience.setExperienceAmount(award);
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	        Calendar c = Calendar.getInstance();
	        c.add(Calendar.DAY_OF_MONTH, 10);
	        String lastday =  sf.format(c.getTime());
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			int id=0;
			try {
				date = format.parse(lastday);
				experience.setLastDate(date);
				UserExperience experience1 = this.getBaseDao().save(experience);           // 保存体验金
				id = experience1.getExperienceId();
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return id;
			 

	}

}
