package com.phb.puhuibao.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUserExtra;

@Transactional
@Service("mobileUserExtraService")
public class MobileUserExtraServiceImpl extends DefaultBaseService<MobileUserExtra, String> {
	@Resource(name = "mobileUserExtraDao")
	public void setBaseDao(IBaseDao<MobileUserExtra, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "mobileUserExtraDao")
	public void setPagerDao(IPagerDao<MobileUserExtra> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	public MobileUserExtra save(MobileUserExtra entity) {
		entity.setCreateTime(new Date());
		this.getBaseDao().save(entity);
		return entity;
	}

}
