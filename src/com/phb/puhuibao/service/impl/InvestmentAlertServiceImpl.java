package com.phb.puhuibao.service.impl;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.InvestmentAlert;
 

@Transactional
@Service("investmentAlertService")
public class InvestmentAlertServiceImpl extends DefaultBaseService<InvestmentAlert, String>  {
	@Resource(name = "investmentAlertDao")
	public void setBaseDao(IBaseDao<InvestmentAlert, String> baseDao) {
		super.setBaseDao(baseDao);
	}

 
	public void setPagerDao(IPagerDao<InvestmentAlert> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	
 

}
