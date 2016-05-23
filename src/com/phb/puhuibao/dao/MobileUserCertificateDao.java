package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.MobileUserCertificate;

@Repository("mobileUserCertificateDao")
public class MobileUserCertificateDao extends DefaultBaseDao<MobileUserCertificate,String> {
	public String getNamespace() {
		return MobileUserCertificate.class.getName();
	}

}
