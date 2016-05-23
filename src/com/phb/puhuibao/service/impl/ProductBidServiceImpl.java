package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ProductBid;

@Transactional
@Service("productBidService")
public class ProductBidServiceImpl extends DefaultBaseService<ProductBid, String> {
	@Resource(name = "productBidDao")
	public void setBaseDao(IBaseDao<ProductBid, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "productBidDao")
	public void setPagerDao(IPagerDao<ProductBid> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
