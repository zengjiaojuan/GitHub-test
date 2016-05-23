package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Invite;

@Repository("inviteDao")
public class InviteDao extends DefaultBaseDao<Invite,String> {
	public String getNamespace() {
		return Invite.class.getName();
	}

}
