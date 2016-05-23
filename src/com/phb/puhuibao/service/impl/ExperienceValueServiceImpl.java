package com.phb.puhuibao.service.impl;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ExperienceValue;
 

@Transactional
@Service("experienceValueService")
public class ExperienceValueServiceImpl extends DefaultBaseService<ExperienceValue, String>  {
	@Resource(name = "experienceValueDao")
	public void setBaseDao(IBaseDao<ExperienceValue, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	public void setPagerDao(IPagerDao<ExperienceValue> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	public double getTotalValueByUserId(String  muid) {
		String sql = "select sum(experience_value) experienceValue from phb_muser_experiencevalue where m_user_id=" + muid;
		Map<String, Object> experience = this.jdbcTemplate.queryForMap(sql);
		double sumvalue=0;
		if(experience.get("experienceValue")==null){
			
		}else{
			sumvalue = Double.parseDouble(experience.get("experienceValue").toString());
		}
 
		return sumvalue;
	}
	

}
