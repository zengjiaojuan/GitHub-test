package com.phb.puhuibao.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phb.puhuibao.entity.Feedback;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.generatekey.service.IGenerateKeyMangerService;
import com.idp.pub.service.impl.DefaultBaseService;

@Transactional
@Service("feedbackService")
public class FeedbackServiceImpl extends DefaultBaseService<Feedback, String> {
	@Resource(name = "feedbackDao")
	private IBaseDao<Feedback, String> baseDao;
	@Resource(name = "feedbackDao")
	private IPagerDao<Feedback> pagerDao;
	@Resource(name = "feedbackDao")
	public void setBaseDao(IBaseDao<Feedback, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Resource(name = "feedbackDao")
	public void setPagerDao(IPagerDao<Feedback> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@Resource(name = "generateKeyServcie")
	private IGenerateKeyMangerService generateKeyService;
	
	public Feedback save(Feedback entity) {
	 	return baseDao.save(entity);
    }
		 
    public Feedback update(Feedback entity) {
		return baseDao.update(entity);
    }
		 
    public Integer delete(Map<String, Object> params) {
        return baseDao.delete(params); 
    }
}
