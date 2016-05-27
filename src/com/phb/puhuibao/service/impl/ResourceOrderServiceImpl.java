package com.phb.puhuibao.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.entity.Pager;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.ResourceOrder;
import com.phb.puhuibao.entity.ResourceType;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserMessage;
import com.phb.puhuibao.service.ResourceOrderService;

@Transactional
@Service("resourceOrderService")
public class ResourceOrderServiceImpl extends DefaultBaseService<ResourceOrder, String> implements ResourceOrderService {
	@Override
	@javax.annotation.Resource(name = "resourceOrderDao")
	public void setBaseDao(IBaseDao<ResourceOrder, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	@javax.annotation.Resource(name = "resourceOrderDao")
	public void setPagerDao(IPagerDao<ResourceOrder> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@javax.annotation.Resource(name = "resourceDao")
	private IBaseDao<Resource, String> resourceDao;

	@javax.annotation.Resource(name = "resourceTypeDao")
	private IBaseDao<ResourceType, String> resourceTypeDao;

	@javax.annotation.Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;

	@javax.annotation.Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;
	
	@javax.annotation.Resource(name = "userMessageDao")
	private IBaseDao<UserMessage, String> userMessageDao;

	@Override
	public ResourceOrder processSave(ResourceOrder order) {
		Resource resource = resourceDao.get(order.getResourceId() + "");
 

		if (resource.getMethod() == 0) {
			Pager<ResourceOrder> pager = new Pager<ResourceOrder>();
			pager.setReload(true);
			pager.setCurrent(0);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("resourceId", resource.getResourceId());
			params.put("status", 1);
			Pager<ResourceOrder> p = this.getPagerDao().findByPager(pager, params);
			if (p.getTotal() >= resource.getNumber()) {
				//return "人数已满！";
				return null;
			}
			if (resource.getNumber() - p.getTotal() == 1) {
				Resource entity = new Resource();
				entity.setResourceId(resource.getResourceId());
				entity.setStatus(2);
				resourceDao.update(entity);
			}
		}
		if (resource.getCategory() == 0) { // 供
			order.setStatus(0); // 0=无效，1=有效，2=完成
		} else {
			order.setStatus(1);
		}
		return save(order);
	}
	
	@Override
	public void processConfirm(ResourceOrder order) { }

	@Override
	public void processSelected(Resource resource, List<String> ids) { }
	
	//3.0版本  这个函数可以处理任务和技能，现在的选中只需要改变一个状态，确认完成的时候才设计到金钱
	@Override
	public void processSelectedOrders(Resource resource, List<String> ids) {
		 
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("resourceId", resource.getResourceId());
			params.put("status", 1);  
			List<ResourceOrder> orders = this.getBaseDao().find(params);
			for (ResourceOrder order : orders) {
				if (ids.contains(order.getOrderId()+"")) {
					ResourceOrder entity = new ResourceOrder();
					entity.setOrderId(order.getOrderId());
					entity.setStatus(2);//选中状态   0：创建 1：有效 2：选中 3：完成 4：取消
					update(entity);
				}
			}

			//判断是否选满，没选满下次可以再次选择  3.0改版
			params = new HashMap<String, Object>();
			params.put("resourceId", resource.getResourceId());
			params.put("status", 2);  
			List<ResourceOrder> orderslist = this.getBaseDao().find(params);
			if(orderslist.size()==resource.getNumber()){//有效订单数==任务需求数
				Resource entity = new Resource();
				entity.setResourceId(resource.getResourceId());
				entity.setStatus(2);  // 如果报名截止的时候  也会状态改变为2
				resourceDao.update(entity);
			}
	}

	@Override
	public void pay(Resource resource, ResourceOrder order) { }

	// 任务状态：0=未付款（category=1，可以自己删除），1=报名中（category=1，已支付），2=报名结束，3=已完成，4=取消
	// 订单状态：0=未付款（category=0，可以自己删除），1=报名（category=0，已支付），2=报名选中，3=已完成，4=已确认（到账），5=已评论，6=已回复，7=未完成，8=取消
	@Override
	public void processSelectOrders(Resource resource, String[] ids, boolean isFull) { }
}
