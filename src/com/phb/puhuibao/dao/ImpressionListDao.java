package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ImpressionList;

@Repository("impressionListDao")
public class ImpressionListDao extends DefaultBaseDao<ImpressionList,String> {
	@Override
	public String getNamespace() {
		return ImpressionList.class.getName();
	}
}
