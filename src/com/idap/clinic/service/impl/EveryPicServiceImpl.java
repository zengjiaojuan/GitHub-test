package com.idap.clinic.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idap.clinic.entity.EveryPic;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.generatekey.service.IGenerateKeyMangerService;
import com.idp.pub.service.impl.DefaultBaseService;

@Transactional
@Service("everyPicService")
public class EveryPicServiceImpl extends DefaultBaseService<EveryPic, String> {
	@Resource(name = "everyPicDao")
	public void setBaseDao(IBaseDao<EveryPic, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "everyPicDao")
	public void setPagerDao(IPagerDao<EveryPic> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "generateKeyServcie")
	private IGenerateKeyMangerService generateKeyService;
	
	public EveryPic save(EveryPic entity) {
		 String everyPicId =  generateKeyService.getNextGeneratedKey(null).getNextKey();//produce an id
		 entity.setEveryPicId(everyPicId);
	 	 return  this.getBaseDao().save(entity);
	    }
		 
     public EveryPic update(EveryPic entity) {
			return  this.getBaseDao().update(entity);
	    }		 
		 
    public Integer delete(Map<String, Object> params) {
        return this.getBaseDao().delete(params); 
    }
}
