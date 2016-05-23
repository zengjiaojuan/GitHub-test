package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserMessage;

@Repository("userMessageDao")
public class UserMessageDao extends DefaultBaseDao<UserMessage,String> {
	public String getNamespace() {
		return UserMessage.class.getName();
	}

}
