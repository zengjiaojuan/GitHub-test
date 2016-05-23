package com.phb.puhuibao.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.CommentReply;

@Transactional
@Service("commentReplyService")
public class CommentReplyServiceImpl extends DefaultBaseService<CommentReply, String> {
	@javax.annotation.Resource(name = "commentReplyDao")
	public void setBaseDao(IBaseDao<CommentReply, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@javax.annotation.Resource(name = "commentReplyDao")
	public void setPagerDao(IPagerDao<CommentReply> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
