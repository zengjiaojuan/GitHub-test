package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.CalculateFormula;


@Transactional
@Service("calculateFormulaService")
public class CalculateFormulaServiceImpl extends DefaultBaseService<CalculateFormula, String>
{
	@Resource(name = "calculateFormulaDao")
	public void setBaseDao(IBaseDao<CalculateFormula, String> baseDao) {
		super.setBaseDao(baseDao);
	}

 
 

	 @Override
     public CalculateFormula update(CalculateFormula entity) {
			return  this.getBaseDao().update(entity);
	    }

 

}

