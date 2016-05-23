package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.AlipayApi;
 

@Transactional
@Service("alipayApiService")
public class AlipayApiServiceImpl extends DefaultBaseService<AlipayApi, String>  {
	@Resource(name = "alipayApiDao")
	public void setBaseDao(IBaseDao<AlipayApi, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	public void setPagerDao(IPagerDao<AlipayApi> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	

}
