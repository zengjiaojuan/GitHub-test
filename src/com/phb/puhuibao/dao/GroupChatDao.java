package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.GroupChat;

@Repository("groupChatDao")
public class GroupChatDao extends DefaultBaseDao<GroupChat,String> {
	@Override
	public String getNamespace() {
		return GroupChat.class.getName();
	}
}
