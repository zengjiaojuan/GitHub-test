package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Version;

@Repository("versionDao")
public class VersionDao extends DefaultBaseDao<Version,String> {
	public String getNamespace() {
		return Version.class.getName();
	}

}
