package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.Notification;

@Transactional
@Service("notificationService")
public class NotificationServiceImpl extends DefaultBaseService<Notification, String> {
	@Resource(name = "notificationDao")
	public void setBaseDao(IBaseDao<Notification, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "notificationDao")
	public void setPagerDao(IPagerDao<Notification> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
