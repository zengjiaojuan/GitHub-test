package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.Holiday;

@Transactional
@Service("holidayService")
public class HolidayServiceImpl extends DefaultBaseService<Holiday, String> {
	@Resource(name = "holidayDao")
	public void setBaseDao(IBaseDao<Holiday, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "holidayDao")
	public void setPagerDao(IPagerDao<Holiday> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
