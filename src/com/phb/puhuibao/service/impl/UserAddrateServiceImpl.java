package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserAddrate;
 

@Transactional
@Service("userAddrateService")
public class UserAddrateServiceImpl extends DefaultBaseService<UserAddrate, String>  {
	@Override
	@Resource(name = "userAddrateDao")
	public void setBaseDao(IBaseDao<UserAddrate, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Override
	@Resource(name = "userAddrateDao")
	public void setPagerDao(IPagerDao<UserAddrate> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
 
	
	
 
	

}
