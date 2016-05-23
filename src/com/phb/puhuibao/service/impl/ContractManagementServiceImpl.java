package com.phb.puhuibao.service.impl;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ContractManagement;
 

@Transactional
@Service("contractManagementService")
public class ContractManagementServiceImpl extends DefaultBaseService<ContractManagement, String>  {
	@Resource(name = "contractManagementDao")
	public void setBaseDao(IBaseDao<ContractManagement, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Resource(name = "contractManagementDao")
	public void setPagerDao(IPagerDao<ContractManagement> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	 @Override
	public ContractManagement save(ContractManagement entity) {
		 
		  
	 	 return   this.getBaseDao().save(entity);
	    }
	
	
 
	

}
