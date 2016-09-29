package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.AddRate;
import com.phb.puhuibao.entity.ApkUpload;

@Repository("apkUploadDao")
public class ApkUploadDao extends DefaultBaseDao<ApkUpload,String> {
	@Override
	public String getNamespace() {
		return ApkUpload.class.getName();
	}
}
