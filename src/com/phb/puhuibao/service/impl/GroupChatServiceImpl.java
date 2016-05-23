package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.GroupChat;
 

@Transactional
@Service("groupChatService")
public class GroupChatServiceImpl extends DefaultBaseService<GroupChat, String>  {
	@Resource(name = "groupChatDao")
	public void setBaseDao(IBaseDao<GroupChat, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	public void setPagerDao(IPagerDao<GroupChat> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	

}
