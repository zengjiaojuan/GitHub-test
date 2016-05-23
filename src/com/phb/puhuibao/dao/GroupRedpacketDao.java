package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.GroupRedpacket;

@Repository("groupRedpacketDao")
public class GroupRedpacketDao extends DefaultBaseDao<GroupRedpacket, String> {
	public String getNamespace() {
		return GroupRedpacket.class.getName();
	}

}
