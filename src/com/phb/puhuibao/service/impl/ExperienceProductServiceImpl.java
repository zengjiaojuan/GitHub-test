package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ExperienceProduct;

@Transactional
@Service("experienceProductService")
public class ExperienceProductServiceImpl extends DefaultBaseService<ExperienceProduct, String> {
	@Resource(name = "experienceProductDao")
	public void setBaseDao(IBaseDao<ExperienceProduct, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "experienceProductDao")
	public void setPagerDao(IPagerDao<ExperienceProduct> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
