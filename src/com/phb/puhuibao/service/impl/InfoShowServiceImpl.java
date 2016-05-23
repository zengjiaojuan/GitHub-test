package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.InfoShow;
 

@Transactional
@Service("infoShowService")
public class InfoShowServiceImpl extends DefaultBaseService<InfoShow, String>  {
	@Resource(name = "infoShowDao")
	public void setBaseDao(IBaseDao<InfoShow, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	public void setPagerDao(IPagerDao<InfoShow> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	

}
