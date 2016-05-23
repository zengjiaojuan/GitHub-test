package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.InfoShow;

@Repository("infoShowDao")
public class InfoShowDao extends DefaultBaseDao<InfoShow,String> {
	@Override
	public String getNamespace() {
		return InfoShow.class.getName();
	}
}
