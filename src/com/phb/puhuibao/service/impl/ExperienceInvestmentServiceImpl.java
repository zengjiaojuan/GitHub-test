package com.phb.puhuibao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.IBaseService;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ExperienceInvestment;
import com.phb.puhuibao.entity.ExperienceProduct;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserExperience;
import com.phb.puhuibao.service.ExperienceInvestmentService;

@Transactional
@Service("experienceInvestmentService")
public class ExperienceInvestmentServiceImpl extends DefaultBaseService<ExperienceInvestment, String> implements ExperienceInvestmentService {
	@Override
	@Resource(name = "experienceInvestmentDao")
	public void setBaseDao(IBaseDao<ExperienceInvestment, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	
	@Resource(name = "experienceProductDao")
	private IBaseDao<ExperienceProduct, String> experienceProductDao;
	
	@Resource(name = "userExperienceDao")
	private IBaseDao<UserExperience, String> userExperienceDao;
	
	@Resource(name = "userExperienceService")
	private IBaseService<UserExperience, String> userExperienceService;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;
	@Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao; 
	@Override
	public void processSave(ExperienceInvestment entity) {
	 
	    //修改用户奖金总额
	    Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", entity.getmUserId());
		params.put("status", 3);                // 获得用户所有奖金总和
		UserExperience usersumexperience = userExperienceService.unique(params);
		UserExperience updateusersum = new UserExperience();
		updateusersum.setExperienceId(usersumexperience.getExperienceId());
		updateusersum.setExperienceAmount(usersumexperience.getExperienceAmount() - entity.getInvestmentAmount());  //原值-投资额
		userExperienceDao.update(updateusersum);

		entity.setCreateTime(new Date());
		this.getBaseDao().save(entity);
		 
	}		
	@Override
	public ExperienceInvestment update(ExperienceInvestment entity) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() + entity.getLastIncome());
		//user.setFrozenMoney(u.getFrozenMoney());
		mobileUserDao.update(user);
		
		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setAmount(entity.getLastIncome());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("理财体验收益");
		log.setChangeDesc("理财体验投资id: " + entity.getInvestmentId());
		log.setAccountType(3);
		userAccountLogDao.save(log);
		
		entity.setmUserId(null);
		return this.getBaseDao().update(entity);
	}

 
}
