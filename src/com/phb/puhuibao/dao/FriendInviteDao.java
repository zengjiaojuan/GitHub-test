package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.FriendInvite;

@Repository("friendInviteDao")
public class FriendInviteDao extends DefaultBaseDao<FriendInvite,String> {
	@Override
	public String getNamespace() {
		return FriendInvite.class.getName();
	}
}
