package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ThirdPayLog;

@Transactional
@Service("thirdPayLogService")
public class ThirdPayLogServiceImpl extends DefaultBaseService<ThirdPayLog, String> {
	@Resource(name = "thirdPayLogDao")
	public void setBaseDao(IBaseDao<ThirdPayLog, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "thirdPayLogDao")
	public void setPagerDao(IPagerDao<ThirdPayLog> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
