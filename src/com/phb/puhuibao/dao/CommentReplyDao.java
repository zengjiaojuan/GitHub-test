package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.CommentReply;

@Repository("commentReplyDao")
public class CommentReplyDao extends DefaultBaseDao<CommentReply, String> {
	public String getNamespace() {
		return CommentReply.class.getName();
	}

}
