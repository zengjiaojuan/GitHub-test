package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ThirdPayAccount;

@Transactional
@Service("thirdPayAccountService")
public class ThirdPayAccountServiceImpl extends DefaultBaseService<ThirdPayAccount, String> {
	@Resource(name = "thirdPayAccountDao")
	public void setBaseDao(IBaseDao<ThirdPayAccount, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "thirdPayAccountDao")
	public void setPagerDao(IPagerDao<ThirdPayAccount> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
