package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.HexagramFortune;


@Transactional
@Service("hexagramFortuneService")
public class HexagramFortuneServiceImpl extends DefaultBaseService<HexagramFortune, String>
{
	@Resource(name = "hexagramFortuneDao")
	public void setBaseDao(IBaseDao<HexagramFortune, String> baseDao) {
		super.setBaseDao(baseDao);
	}

 
 

	 @Override
     public HexagramFortune update(HexagramFortune entity) {
			return  this.getBaseDao().update(entity);
	    }

 

}

