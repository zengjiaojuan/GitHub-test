package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.HexagramFame;


@Transactional
@Service("hexagramFameService")
public class HexagramFameServiceImpl extends DefaultBaseService<HexagramFame, String>
{
	@Resource(name = "hexagramFameDao")
	public void setBaseDao(IBaseDao<HexagramFame, String> baseDao) {
		super.setBaseDao(baseDao);
	}

 
 

	 @Override
     public HexagramFame update(HexagramFame entity) {
			return  this.getBaseDao().update(entity);
	    }

 

}

