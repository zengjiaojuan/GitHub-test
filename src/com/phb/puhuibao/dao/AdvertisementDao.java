package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Advertisement;

@Repository("advertisementDao")
public class AdvertisementDao extends DefaultBaseDao<Advertisement,String> {
	public String getNamespace() {
		return Advertisement.class.getName();
	}

}
