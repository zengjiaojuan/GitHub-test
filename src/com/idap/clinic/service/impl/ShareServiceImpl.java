package com.idap.clinic.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idap.clinic.entity.Share;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;

@Transactional
@Service("shareService")
public class ShareServiceImpl extends DefaultBaseService<Share, String> {
	@Resource(name = "shareDao")
	public void setBaseDao(IBaseDao<Share, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "shareDao")
	public void setPagerDao(IPagerDao<Share> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	
	public Share save(Share entity) {
	 	return  this.getBaseDao().save(entity);
    }
		 
    public Share update(Share entity) {
		return  this.getBaseDao().update(entity);
    }
		 
    public Integer delete(Map<String, Object> params) {
        return this.getBaseDao().delete(params); 
    }

}
