package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.HexagramCharm;
 

@Repository("hexagramCharmDao")
public class HexagramCharmDao extends DefaultBaseDao<HexagramCharm,String>{
	@Override
	public String getNamespace() {
		return HexagramCharm.class.getName();
	}
}

