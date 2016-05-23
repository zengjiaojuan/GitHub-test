package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserAsset;

@Transactional
@Service("userAssetService")
public class UserAssetServiceImpl extends DefaultBaseService<UserAsset, String> {
	@Resource(name = "userAssetDao")
	public void setBaseDao(IBaseDao<UserAsset, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "userAssetDao")
	public void setPagerDao(IPagerDao<UserAsset> pagerDao) {
		super.setPagerDao(pagerDao);
	}
}
