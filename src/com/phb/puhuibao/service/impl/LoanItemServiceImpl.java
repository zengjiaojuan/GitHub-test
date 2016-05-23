package com.phb.puhuibao.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.LoanItem;

@Transactional
@Service("loanItemService")
public class LoanItemServiceImpl extends DefaultBaseService<LoanItem, String> {
	@Resource(name = "loanItemDao")
	public void setBaseDao(IBaseDao<LoanItem, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "loanItemDao")
	public void setPagerDao(IPagerDao<LoanItem> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	public LoanItem save(LoanItem entity) {
		if (entity.getCreateTime() == null) {
			entity.setCreateTime(new Date());
		}
		this.getBaseDao().save(entity);
		return entity;
	}

}
