package com.phb.puhuibao.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.Comment;

@Transactional
@Service("commentService")
public class CommentServiceImpl extends DefaultBaseService<Comment, String> {
	@javax.annotation.Resource(name = "commentDao")
	public void setBaseDao(IBaseDao<Comment, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@javax.annotation.Resource(name = "commentDao")
	public void setPagerDao(IPagerDao<Comment> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
