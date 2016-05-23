package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.GroupRedpacket;
 

@Transactional
@Service("groupRedpacketService")
public class GroupRedpacketServiceImpl extends DefaultBaseService<GroupRedpacket, String>  {
	@Resource(name = "groupRedpacketDao")
	public void setBaseDao(IBaseDao<GroupRedpacket, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Resource(name = "groupRedpacketDao")
	public void setPagerDao(IPagerDao<GroupRedpacket> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
 
	
	
 
	

}
