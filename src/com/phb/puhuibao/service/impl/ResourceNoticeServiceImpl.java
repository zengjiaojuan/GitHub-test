package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ResourceNotice;
 

@Transactional
@Service("resourceNoticeService")
public class ResourceNoticeServiceImpl extends DefaultBaseService<ResourceNotice, String>  {
	@Resource(name = "resourceNoticeDao")
	public void setBaseDao(IBaseDao<ResourceNotice, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	public void setPagerDao(IPagerDao<ResourceNotice> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	

}
