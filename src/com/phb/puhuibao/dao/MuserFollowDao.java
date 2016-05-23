package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.MuserFollow;

@Repository("muserFollowDao")
public class MuserFollowDao extends DefaultBaseDao<MuserFollow,String> {
	@Override
	public String getNamespace() {
		return MuserFollow.class.getName();
	}
}
