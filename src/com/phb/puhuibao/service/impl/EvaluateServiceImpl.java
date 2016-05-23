package com.phb.puhuibao.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.Evaluate;

@Transactional
@Service("evaluateService")
public class EvaluateServiceImpl extends DefaultBaseService<Evaluate, String> {
	@javax.annotation.Resource(name = "evaluateDao")
	public void setBaseDao(IBaseDao<Evaluate, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@javax.annotation.Resource(name = "evaluateDao")
	public void setPagerDao(IPagerDao<Evaluate> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
