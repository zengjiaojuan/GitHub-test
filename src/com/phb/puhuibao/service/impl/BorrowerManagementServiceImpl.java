package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.BorrowerManagement;
 

@Transactional
@Service("borrowerManagementService")
public class BorrowerManagementServiceImpl extends DefaultBaseService<BorrowerManagement, String>  {
	@Resource(name = "borrowerManagementDao")
	public void setBaseDao(IBaseDao<BorrowerManagement, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Resource(name = "borrowerManagementDao")
	public void setPagerDao(IPagerDao<BorrowerManagement> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
 
	
	
 
	

}
