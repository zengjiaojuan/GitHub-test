package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Notification;

@Repository("notificationDao")
public class NotificationDao extends DefaultBaseDao<Notification,String> {
	public String getNamespace() {
		return Notification.class.getName();
	}

}
