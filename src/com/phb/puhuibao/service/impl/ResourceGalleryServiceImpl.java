package com.phb.puhuibao.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ResourceGallery;

@Transactional
@Service("resourceGalleryService")
public class ResourceGalleryServiceImpl extends DefaultBaseService<ResourceGallery, String> {
	@javax.annotation.Resource(name = "resourceGalleryDao")
	public void setBaseDao(IBaseDao<ResourceGallery, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@javax.annotation.Resource(name = "resourceGalleryDao")
	public void setPagerDao(IPagerDao<ResourceGallery> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
