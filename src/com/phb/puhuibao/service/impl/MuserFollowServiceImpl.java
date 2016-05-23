package com.phb.puhuibao.service.impl;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MuserFollow;
 

@Transactional
@Service("muserFollowService")
public class MuserFollowServiceImpl extends DefaultBaseService<MuserFollow, String>  {
	@Resource(name = "muserFollowDao")
	public void setBaseDao(IBaseDao<MuserFollow, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	public void setPagerDao(IPagerDao<MuserFollow> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	

}
