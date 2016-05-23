package com.phb.puhuibao.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ResourceReport;

@Transactional
@Service("resourceReportService")
public class ResourceReportImpl extends DefaultBaseService<ResourceReport, String> {
	@javax.annotation.Resource(name = "resourceReportDao")
	public void setBaseDao(IBaseDao<ResourceReport, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@javax.annotation.Resource(name = "resourceReportDao")
	public void setPagerDao(IPagerDao<ResourceReport> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
