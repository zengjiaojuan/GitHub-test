package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Appreciation;

@Repository("appreciationDao")
public class AppreciationDao extends DefaultBaseDao<Appreciation,String> {
	public String getNamespace() {
		return Appreciation.class.getName();
	}
}
