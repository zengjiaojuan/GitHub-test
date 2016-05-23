package com.idap.clinic.service.impl;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idap.clinic.entity.UserInformation;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.generatekey.service.IGenerateKeyMangerService;
import com.idp.pub.service.impl.DefaultBaseService;



/**
 * @###################################################
 * @创建日期：2015-3-6
 * @开发人员：wangwei
 * @功能描述：
 * @修改日志：
 * @###################################################
 */

@Transactional
@Service("userInformationService")
public class UserInformationServiceImpl extends DefaultBaseService<UserInformation, String>
		  {
	@Resource(name = "userInformationDao")
	public void setBaseDao(IBaseDao<UserInformation, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "userInformationDao")
	public void setPagerDao(IPagerDao<UserInformation> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "generateKeyServcie")
	private IGenerateKeyMangerService generateKeyService;
 
	public UserInformation save(UserInformation entity) {
		if (entity.getmUserId() == null) {
		    String mUserId = generateKeyService.getNextGeneratedKey(null).getNextKey();//produce an id
		    entity.setmUserId(mUserId);
		}
		entity.setmDate(new Date());
	 	return  this.getBaseDao().save(entity);
	}
	
 
		 
 	 
		 
 
    

}
