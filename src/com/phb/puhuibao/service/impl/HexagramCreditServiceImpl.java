package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.generatekey.service.IGenerateKeyMangerService;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.HexagramCredit;


@Transactional
@Service("hexagramCreditService")
public class HexagramCreditServiceImpl extends DefaultBaseService<HexagramCredit, String>
{
	@Resource(name = "hexagramCreditDao")
	public void setBaseDao(IBaseDao<HexagramCredit, String> baseDao) {
		super.setBaseDao(baseDao);
	}

 
	@Resource(name = "generateKeyServcie")
	private IGenerateKeyMangerService generateKeyService;

 

	 @Override
     public HexagramCredit update(HexagramCredit entity) {
			return  this.getBaseDao().update(entity);
	    }

 

}

