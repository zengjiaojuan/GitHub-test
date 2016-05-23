package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserMessage;

@Transactional
@Service("userMessageService")
public class UserMessageServiceImpl extends DefaultBaseService<UserMessage, String> {
	@Resource(name = "userMessageDao")
	public void setBaseDao(IBaseDao<UserMessage, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "userMessageDao")
	public void setPagerDao(IPagerDao<UserMessage> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
