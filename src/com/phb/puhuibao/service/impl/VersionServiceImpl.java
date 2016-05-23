package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.Version;

@Transactional
@Service("versionService")
public class VersionServiceImpl extends DefaultBaseService<Version, String> {
	@Resource(name = "versionDao")
	public void setBaseDao(IBaseDao<Version, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "versionDao")
	public void setPagerDao(IPagerDao<Version> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
