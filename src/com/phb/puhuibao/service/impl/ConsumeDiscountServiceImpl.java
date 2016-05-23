package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ConsumeDiscount;

@Transactional
@Service("consumeDiscountService")
public class ConsumeDiscountServiceImpl extends DefaultBaseService<ConsumeDiscount, String> {
	@Resource(name = "consumeDiscountDao")
	public void setBaseDao(IBaseDao<ConsumeDiscount, String> baseDao) {
		super.setBaseDao(baseDao);
	}

}
