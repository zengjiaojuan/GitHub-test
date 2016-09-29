package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ApkUpload;
import com.phb.puhuibao.service.ApkUploadService;
 

@Transactional
@Service("apkUploadService")
public class ApkUploadServiceImpl extends DefaultBaseService<ApkUpload, String> implements ApkUploadService {
	@Override
	@Resource(name = "apkUploadDao")
	public void setBaseDao(IBaseDao<ApkUpload, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Override
	@Resource(name = "apkUploadDao")
	public void setPagerDao(IPagerDao<ApkUpload> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
 
	
	
 
	

}
