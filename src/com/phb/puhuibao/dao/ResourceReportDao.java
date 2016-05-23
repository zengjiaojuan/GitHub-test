package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ResourceReport;

@Repository("resourceReportDao")
public class ResourceReportDao extends DefaultBaseDao<ResourceReport,String> {
	public String getNamespace() {
		return ResourceReport.class.getName();
	}

}
