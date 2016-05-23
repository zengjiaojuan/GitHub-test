package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserAsset;

@Repository("userAssetDao")
public class UserAssetDao extends DefaultBaseDao<UserAsset,String> {
	public String getNamespace() {
		return UserAsset.class.getName();
	}
}
