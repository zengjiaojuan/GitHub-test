package com.phb.puhuibao.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.service.ResourceService;

@Transactional
@Service("resourceService")
public class ResourceServiceImpl extends DefaultBaseService<Resource, String> implements ResourceService {
	@javax.annotation.Resource(name = "resourceDao")
	public void setBaseDao(IBaseDao<Resource, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@javax.annotation.Resource(name = "resourceDao")
	public void setPagerDao(IPagerDao<Resource> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@javax.annotation.Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;

	@javax.annotation.Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;

	@Override
	public void pay(Resource resource) {
		double amount = resource.getPrice() * resource.getNumber();
		MobileUser u = mobileUserDao.get(resource.getmUserId() + "");
		MobileUser user = new MobileUser();
		user.setmUserId(u.getmUserId());
		user.setmUserMoney(u.getmUserMoney() - amount);
		mobileUserDao.update(user);
		
		Resource entity = new Resource();
		entity.setResourceId(resource.getResourceId());
		entity.setStatus(1);
		update(entity);

		UserAccountLog log = new UserAccountLog();
		log.setmUserId(resource.getmUserId());
		log.setAmount(-amount);
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("需求资源支付");
		log.setChangeDesc("资源id: " + resource.getResourceId());
		log.setAccountType(31);
		userAccountLogDao.save(log);
	}
}
