package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.HexagramAbility;


@Transactional
@Service("hexagramAbilityService")
public class HexagramAbilityServiceImpl extends DefaultBaseService<HexagramAbility, String>
{
	@Resource(name = "hexagramAbilityDao")
	public void setBaseDao(IBaseDao<HexagramAbility, String> baseDao) {
		super.setBaseDao(baseDao);
	}

 
 

	 @Override
     public HexagramAbility update(HexagramAbility entity) {
			return  this.getBaseDao().update(entity);
	    }

 

}

