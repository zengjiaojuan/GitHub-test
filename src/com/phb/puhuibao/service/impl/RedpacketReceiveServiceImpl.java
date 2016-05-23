package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.RedpacketReceive;
 

@Transactional
@Service("redpacketReceiveService")
public class RedpacketReceiveServiceImpl extends DefaultBaseService<RedpacketReceive, String>  {
	@Resource(name = "redpacketReceiveDao")
	public void setBaseDao(IBaseDao<RedpacketReceive, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Resource(name = "redpacketReceiveDao")
	public void setPagerDao(IPagerDao<RedpacketReceive> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
 
	
	
 
	

}
