package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserRedpacket;

@Repository("userRedpacketDao")
public class UserRedpacketDao extends DefaultBaseDao<UserRedpacket,String> {
	public String getNamespace() {
		return UserRedpacket.class.getName();
	}
}
