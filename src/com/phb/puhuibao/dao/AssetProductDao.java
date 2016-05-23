package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.AssetProduct;

@Repository("assetProductDao")
public class AssetProductDao extends DefaultBaseDao<AssetProduct,String> {
	@Override
	public String getNamespace() {
		return AssetProduct.class.getName();
	}
}
