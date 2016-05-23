package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.CustomerManagement;
 

@Transactional
@Service("customerManagementService")
public class CustomerManagementServiceImpl extends DefaultBaseService<CustomerManagement, String>  {
	@Resource(name = "customerManagementDao")
	public void setBaseDao(IBaseDao<CustomerManagement, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Resource(name = "customerManagementDao")
	public void setPagerDao(IPagerDao<CustomerManagement> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	 @Override
	public CustomerManagement save(CustomerManagement entity) {
	 	 return   this.getBaseDao().save(entity);
	    }
	
	
 
	

}
