package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.CreditMatching;
 

@Transactional
@Service("creditMatchingService")
public class CreditMatchingServiceImpl extends DefaultBaseService<CreditMatching, String>  {
	@Resource(name = "creditMatchingDao")
	public void setBaseDao(IBaseDao<CreditMatching, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Resource(name = "creditMatchingDao")
	public void setPagerDao(IPagerDao<CreditMatching> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
 
	
	
 
	

}
