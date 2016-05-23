package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUserCertificate;

@Transactional
@Service("mobileUserCertificateService")
public class MobileUserCertificateServiceImpl extends DefaultBaseService<MobileUserCertificate, String> {
	@Resource(name = "mobileUserCertificateDao")
	public void setBaseDao(IBaseDao<MobileUserCertificate, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "mobileUserCertificateDao")
	public void setPagerDao(IPagerDao<MobileUserCertificate> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
