package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserCard;

@Repository("userCardDao")
public class UserCardDao extends DefaultBaseDao<UserCard,String> {
	public String getNamespace() {
		return UserCard.class.getName();
	}
}
