package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.AddRate;
import com.phb.puhuibao.service.AddRateService;
 

@Transactional
@Service("addRateService")
public class AddRateServiceImpl extends DefaultBaseService<AddRate, String> implements AddRateService {
	@Override
	@Resource(name = "addRateDao")
	public void setBaseDao(IBaseDao<AddRate, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Override
	@Resource(name = "addRateDao")
	public void setPagerDao(IPagerDao<AddRate> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
 
	
	
 
	

}
