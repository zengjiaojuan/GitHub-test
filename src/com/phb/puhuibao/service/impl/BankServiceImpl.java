package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.Bank;

@Transactional
@Service("bankService")
public class BankServiceImpl extends DefaultBaseService<Bank, String> {
	@Resource(name = "bankDao")
	public void setBaseDao(IBaseDao<Bank, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "bankDao")
	public void setPagerDao(IPagerDao<Bank> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
