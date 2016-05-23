package com.phb.puhuibao.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.Appreciation;

@Transactional
@Service("appreciationService")
public class AppreciationServiceImpl extends DefaultBaseService<Appreciation, String> {
	@Resource(name = "appreciationDao")
	public void setBaseDao(IBaseDao<Appreciation, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "appreciationDao")
	public void setPagerDao(IPagerDao<Appreciation> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	
	public Appreciation save(Appreciation entity) {
		entity.setCreateTime(new Date());
		return this.getBaseDao().save(entity);
	}
}
