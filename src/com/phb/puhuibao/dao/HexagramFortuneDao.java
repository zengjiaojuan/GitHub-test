package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.HexagramFortune;
 

@Repository("hexagramFortuneDao")
public class HexagramFortuneDao extends DefaultBaseDao<HexagramFortune,String>{
	@Override
	public String getNamespace() {
		return HexagramFortune.class.getName();
	}
}

