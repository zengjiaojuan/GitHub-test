package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.HexagramCharm;


@Transactional
@Service("hexagramCharmService")
public class HexagramCharmServiceImpl extends DefaultBaseService<HexagramCharm, String>
{
	@Resource(name = "hexagramCharmDao")
	public void setBaseDao(IBaseDao<HexagramCharm, String> baseDao) {
		super.setBaseDao(baseDao);
	}

 
 

	 @Override
     public HexagramCharm update(HexagramCharm entity) {
			return  this.getBaseDao().update(entity);
	    }

 

}

