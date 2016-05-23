package com.phb.puhuibao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ExperienceProduct;
import com.phb.puhuibao.entity.ExperienceInvestment;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserExperience;

@Transactional
@Service("experienceInvestmentService")
public class ExperienceInvestmentServiceImpl extends DefaultBaseService<ExperienceInvestment, String> {
	@Resource(name = "experienceInvestmentDao")
	public void setBaseDao(IBaseDao<ExperienceInvestment, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	
	@Resource(name = "experienceProductDao")
	private IBaseDao<ExperienceProduct, String> experienceProductDao;
	
	@Resource(name = "userExperienceDao")
	private IBaseDao<UserExperience, String> userExperienceDao;

	public ExperienceInvestment save(ExperienceInvestment entity) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", entity.getmUserId());
		params.put("status", 1);
		params.put("gelastDate", new Date());
//		List<UserExperience> result = userExperienceDao.find(params);
//		for (UserExperience experience : result) {
//			experience.setStatus(0);
//			userExperienceDao.update(experience);
//		}
		UserExperience experience = userExperienceDao.unique(params);
		experience.setExperienceAmount(experience.getExperienceAmount() - entity.getInvestmentAmount());;
		userExperienceDao.update(experience);

		entity.setCreateTime(new Date());
		this.getBaseDao().save(entity);
		return entity;
	}
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;
	@Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;

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
		log.setAccountType(1);
		userAccountLogDao.save(log);
		
		entity.setmUserId(null);
		return this.getBaseDao().update(entity);
	}
}
