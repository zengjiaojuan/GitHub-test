package com.phb.puhuibao.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUserLoan;

@Transactional
@Service("mobileUserLoanService")
public class MobileUserLoanServiceImpl extends DefaultBaseService<MobileUserLoan, String> {
	@Resource(name = "mobileUserLoanDao")
	public void setBaseDao(IBaseDao<MobileUserLoan, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "mobileUserLoanDao")
	public void setPagerDao(IPagerDao<MobileUserLoan> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	public MobileUserLoan save(MobileUserLoan entity) {
		MobileUserLoan user = this.getBaseDao().get("" + entity.getmUserId());
		if (user == null) {
			entity.setCreateTime(new Date());
			this.getBaseDao().save(entity);
		} else {
			this.getBaseDao().update(entity);
		}
		return entity;
	}
}
