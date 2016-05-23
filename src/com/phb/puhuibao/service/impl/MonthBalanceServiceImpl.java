package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MonthBalance;

@Transactional
@Service("monthBalanceService")
public class MonthBalanceServiceImpl extends DefaultBaseService<MonthBalance, String> {
	@Resource(name = "monthBalanceDao")
	public void setBaseDao(IBaseDao<MonthBalance, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "monthBalanceDao")
	public void setPagerDao(IPagerDao<MonthBalance> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
