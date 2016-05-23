package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Comment;

@Repository("commentDao")
public class CommentDao extends DefaultBaseDao<Comment, String> {
	public String getNamespace() {
		return Comment.class.getName();
	}

}
