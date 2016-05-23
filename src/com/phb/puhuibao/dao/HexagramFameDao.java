package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.HexagramFame;
 

@Repository("hexagramFameDao")
public class HexagramFameDao extends DefaultBaseDao<HexagramFame,String>{
	@Override
	public String getNamespace() {
		return HexagramFame.class.getName();
	}
}

