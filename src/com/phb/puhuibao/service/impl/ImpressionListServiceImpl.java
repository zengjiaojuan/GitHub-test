package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ImpressionList;
 

@Transactional
@Service("impressionListService")
public class ImpressionListServiceImpl extends DefaultBaseService<ImpressionList, String>  {
	@Resource(name = "impressionListDao")
	public void setBaseDao(IBaseDao<ImpressionList, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	public void setPagerDao(IPagerDao<ImpressionList> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	

}
