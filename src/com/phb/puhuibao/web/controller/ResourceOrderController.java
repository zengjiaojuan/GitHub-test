package com.phb.puhuibao.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.ResourceNotice;
import com.phb.puhuibao.entity.ResourceOrder;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserMessage;
import com.phb.puhuibao.service.ResourceOrderService;

@Controller
@RequestMapping(value = "/resourceOrder")
public class ResourceOrderController extends BaseController<ResourceOrder, String> {
	@Override
	@javax.annotation.Resource(name = "resourceOrderService")
	public void setBaseService(IBaseService<ResourceOrder, String> baseService) {
		super.setBaseService(baseService);
	}

	@javax.annotation.Resource(name = "resourceService")
	private IBaseService<Resource, String> resourceService;

	@javax.annotation.Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@javax.annotation.Resource(name = "resourceOrderService")
	private ResourceOrderService resourceOrderService;
	
	@javax.annotation.Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;
	
	@javax.annotation.Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;

	@javax.annotation.Resource(name = "resourceNoticeDao")
	private IBaseDao<ResourceNotice, String> resourceNoticeDao;

	@javax.annotation.Resource(name = "userMessageService")
	private IBaseService<UserMessage, String> userMessageService;
	
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 资源的订单列表
	 * @param pageno
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="resourceOrders")
	@ResponseBody
	public Map<String, Object> resourceOrders(@RequestParam int pageno, @RequestParam String resourceId) {
		String sql = "select a.order_id,a.create_time,a.m_user_id,a.status,b.photo,b.nickname,b.level,b.sex,c.method,c.price,c.category,c.status resourceStatus,c.m_user_id resourceCreater from (select resource_id,order_id,m_user_id,create_time,status from phb_resource_order where status = 1 and resource_id=" + resourceId + " order by create_time desc limit " + pageno * 5 + ",5)a left join phb_mobile_user b on a.m_user_id=b.m_user_id left join phb_resource c on a.resource_id=c.resource_id";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		sql = "select count(1) from phb_resource_order where status = 1 and resource_id=" + resourceId;
		List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("count", count.get(0).get("count(1)"));
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 报名列表   3.0接口
	 * @param pageno
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="getResourceOrders")
	@ResponseBody
	public Map<String, Object> getResourceOrders(@RequestParam String resourceId) {
		String sql = "select a.order_id,a.create_time,a.m_user_id,a.status,b.photo,b.nickname,b.level,b.sex,c.method,c.price,c.category,c.status resourceStatus,c.m_user_id resourceCreater from (select resource_id,order_id,m_user_id,create_time,status from phb_resource_order where (status = 1 or status = 2) and resource_id=" + resourceId + " order by status desc)a left join phb_mobile_user b on a.m_user_id=b.m_user_id left join phb_resource c on a.resource_id=c.resource_id order by a.status desc";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		sql = "select count(1) from phb_resource_order where (status = 1 or status = 2) and resource_id=" + resourceId;
		List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("count", count.get(0).get("count(1)"));
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 确认订单列表   3.0接口
	 * @param pageno
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="getResourceOrdersForConfirm")
	@ResponseBody
	public Map<String, Object> getResourceOrdersForConfirm(@RequestParam String resourceId) {
		String sql = "select a.order_id,a.create_time,a.m_user_id,a.status,b.photo,b.nickname,b.level,b.sex,c.method,c.price,c.category,c.status resourceStatus,c.m_user_id resourceCreater from (select resource_id,order_id,m_user_id,create_time,status from phb_resource_order where status>2 and resource_id=" + resourceId + " order by status desc)a left join phb_mobile_user b on a.m_user_id=b.m_user_id left join phb_resource c on a.resource_id=c.resource_id";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 我的订单列表 我参与的任务
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="myOrders")
	@ResponseBody
	public Map<String, Object> myOrders(@RequestParam int pageno, @RequestParam String muid) {
		String sql = "select a.order_id,a.resource_id,a.create_time,a.status,b.photo,b.nickname,c.name,c.address,c.resource_desc,c.m_user_id resourceCreater,c.method,c.price,c.category,c.status resourceStatus from (select order_id,m_user_id,resource_id,price,create_time,status from phb_resource_order where m_user_id=" + muid + " order by create_time desc limit " + pageno * 5 + ",5)a left join phb_resource c on a.resource_id=c.resource_id left join phb_mobile_user b on c.m_user_id=b.m_user_id";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		sql = "select count(1) from phb_resource_order where m_user_id=" + muid;
		List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
		for (Map<String, Object> map : result) {
			long order_id = (long) map.get("order_id");
			ResourceOrder order = this.getBaseService().getById(order_id + "");
			map.put("action", getOrderAction(order));
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("count", count.get(0).get("count(1)"));
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	private String getOrderAction(ResourceOrder order) {
//		0、报名  条件status=1，未报名 结果orderstatus=1（category=0，未支付），其他orderstatus=1
//		1、删除  条件category=0, orderstatus=0 结果删除
//		   付款  条件category=0, orderstatus=0 结果orderstatus=1
//		2、完成  条件category=1，orderstatus=2 or 7 结果orderstatus=3【3天后自动确认】
//		3、确认  条件category=0, orderstatus=3 结果orderstatus=4，达到人数status=3
//		   未完成  条件category=0, orderstatus=3 结果orderstatus=7
//		   （挂起）仲裁  条件category=0, orderstatus=3 结果orderstatus=4，达到人数status=3，确实完成了扣报名者10%的价格给owner；未完成扣owner10%的价格并退款给报名者
//		4、评价  条件category=0, orderstatus=4 结果orderstatus=5
//		5、回复  条件category=1, orderstatus=5 结果orderstatus=6
//		6、取消  条件orderstatus=1 结果orderstatus=8，（category=0，退款）
		Resource resource = resourceService.getById(order.getResourceId() + "");
		String action = "";
		switch (order.getStatus()) {
			case 0:
				action = "1"; // 删除或付款
				break;
			case 1:
				action = "6"; // 取消
				break;
			case 2:
				if (resource.getCategory() == 1) {
					action = "2"; // 任务完成
				}
				break;
			case 3:
				if (resource.getCategory() == 0) {
					action = "3"; // 确认支付或未完成
				}
				break;
			case 4:
				if (resource.getCategory() == 0) {
					action = "4"; // 评价 
				}
				break;
			case 5:
				if (resource.getCategory() == 1) {
					action = "5"; // 回复 
				}
				break;
			case 7:
				if (resource.getCategory() == 1 && resource.getStatus() == 7) {
					action = "2"; // 任务完成
				}
				break;
		}
		return action;
	}
	
	/**
	 * 保存订单
	 * @param muid
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam int resourceId, @RequestParam String content) {
		Map<String, Object> data = new HashMap<String, Object>();
		Resource resource = resourceService.getById(resourceId + "");
		if (resource.getStatus() >=2) {
			data.put("message", "该任务报名结束！");
			data.put("status", 0);
			return data;
		}
		
		ResourceOrder order = new ResourceOrder();
		order.setmUserId(muid);
		order.setResourceId(resourceId);
		order.setContent(content);
		order.setPrice(resource.getPrice());
		order.setCreateTime(new Date());

		try {
			order = resourceOrderService.processSave(order);
			if (order == null) {
				data.put("message", "人数已满！");
				data.put("status", 0);
				return data;
			}
			
			
			String sql = "select count(1)  ordersnumber from phb_resource_order where status=1 and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			long ordersnumber =   (long)count.get(0).get("ordersnumber");
			
			if(ordersnumber>=resource.getNumber()){//通知任务创建者
				ResourceNotice entity = new ResourceNotice();
				entity.setNoticeResource(resourceId);
				entity.setNoticeUser(resource.getmUserId());
				entity.setNoticeTitle("报名已满");
				if(resource.getMethod()==0){//先到先得
					entity.setNoticeMessage("你发布的任务["+resource.getName()+"]已成功征集到足够人数.");
				}else if(resource.getMethod()==1){//双向选择
					entity.setNoticeMessage("你发布的任务["+resource.getName()+"]已成功征集到足够人数,请进行选择.");
				}
				resourceNoticeDao.save(entity);
			}

 

			
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("result", order.getOrderId());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 报名并支付             购买别人的的服务并报名
	 * @param muid
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="enrolandpay")
	@ResponseBody
	public Map<String, Object> enrolandpay(@RequestParam int muid, @RequestParam int resourceid) {
		Map<String, Object> data = new HashMap<String, Object>();
		Resource resource = resourceService.getById(resourceid + "");
		if(resource==null){
			data.put("message", "该任务不存在！");
			data.put("status", 0);
			return data;
		}
		if (resource.getStatus() ==3 || resource.getStatus() ==2) {
			data.put("message", "该任务报名结束或已完成！");
			data.put("status", 0);
			return data;
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("resourceId", resourceid);
		params.put("status", 1);
		List<ResourceOrder> orders = this.getBaseService().findList(params);
		ResourceOrder sorder = null;

		if(resource.getMethod()==0){// 先来先到的任务  只要人数满就不能报名
			if(orders.size()>=resource.getNumber()){//已经被任务创建人选择过或者先来先到的人数已经=任务的需要的人
				data.put("message", "人数已满！");
				data.put("status", 1);
				return data;
			}
		} 
		
		 //可以随意支付报名
			try {
				ResourceOrder order = new ResourceOrder();
				order.setmUserId(muid);
				order.setResourceId(resourceid);
				if(resource.getMethod()==0){//先来先得 购买就自动确认为1
					order.setStatus(1);
				}else{//
					order.setStatus(0);//暂时是0  付款后会改为1
				}
				Map<String,Object> par = new HashMap<String,Object>();
				par.put("resourceId", resourceid);
				par.put("mUserId", muid);
				 
				ResourceOrder resourceOrder = this.getBaseService().unique(par);
				if(resourceOrder!=null){
					data.put("message", "你已经付款了！");
					data.put("status", 1);
					return data;
					
				}
				sorder = this.getBaseService().save(order);
				this.pay(order.getOrderId()+"");//付款
				
				
 
				
				Map<String,Object> para = new HashMap<String,Object>();
				para.put("resourceId", resourceid);
				para.put("status", 1);
				List<ResourceOrder> orders1 = this.getBaseService().findList(para);
				
				if(orders1.size()>=resource.getNumber()){//通知任务创建者    先来先到的,直接改状态
					ResourceNotice entity = new ResourceNotice();
					entity.setNoticeResource(resourceid);
					entity.setNoticeUser(resource.getmUserId());
					entity.setNoticeTitle("报名已满");
					if(resource.getMethod()==0){//先到先得
						Resource resource1 = new Resource();
						resource1.setResourceId(resourceid);
						resource1.setStatus(2);//报名管理结束状态 2
						resourceService.update(resource1); //修改状态
						
						entity.setNoticeMessage("你发布的任务["+resource.getName()+"]已成功征集到足够人数.");
					}else if(resource.getMethod()==1){//双向选择
						entity.setNoticeMessage("你发布的任务["+resource.getName()+"]已成功征集到足够人数,请进行选择.");
					}
					resourceNoticeDao.save(entity);//保存通知
				}
 
				
			} catch (Exception e) {
				data.put("message", "网络异常！");
				data.put("status", 0);
				return data;
			}
		 
		
		data.put("result", sorder.getOrderId());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 订单完成
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="finished")
	@ResponseBody
	public Map<String, Object> finished(@RequestParam String orderId) {
		Map<String, Object> data = new HashMap<String, Object>();
		ResourceOrder order = this.getBaseService().getById(orderId);
		
		try {
			ResourceOrder entity = new ResourceOrder();
			entity.setOrderId(order.getOrderId());
			entity.setStatus(3);
			this.getBaseService().update(entity);
			// 不考虑事务
			Resource resource = resourceService.getById(order.getResourceId() + "");
			UserMessage message =  new UserMessage();
			message.setTitle("系统消息");
			if (resource.getCategory() == 0) {
				message.setmUserId(entity.getmUserId());
				message.setContent("已完成，请您确认。技能名：" + resource.getName());
			} else {
				message.setmUserId(resource.getmUserId());
				message.setContent("已完成，请您确认。任务名：" + resource.getName());
			}
			userMessageService.save(message);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 订单确认
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="confirm")
	@ResponseBody
	public Map<String, Object> confirm(@RequestParam String orderId) {
		Map<String, Object> data = new HashMap<String, Object>();
		ResourceOrder order = this.getBaseService().getById(orderId);
		
		try {
			resourceOrderService.processConfirm(order);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 订单取消
	 * @param orderId
	 * @param content 理由
	 * @return
	 */
	@RequestMapping(value="cancel")
	@ResponseBody
	public Map<String, Object> cancel(@RequestParam int orderId, @RequestParam String content) {
		Map<String, Object> data = new HashMap<String, Object>();
//		ResourceOrder order = this.getBaseService().getById(orderId);
//		if (order.getStatus() > 0) {
//			data.put("message", "订单不可取消！");
//			data.put("status", 0);
//			return data;
//		}
		
		ResourceOrder entity = new ResourceOrder();
		entity.setOrderId(orderId);
		entity.setStatus(8);
		entity.setContent(content); // 理由
		try {
			this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 双向订单筛选
	 * @param resourceId
	 * @param orderIds
	 * @return
	 */
	@Deprecated
	@RequestMapping(value="selected")
	@ResponseBody
	public Map<String, Object> selected(@RequestParam int resourceId, @RequestParam String orderIds) {
		Map<String, Object> data = new HashMap<String, Object>();
		Resource resource = resourceService.getById(resourceId + "");
		if (resource.getStatus() == 2) {
			data.put("message", "该任务报名已结束！");
			data.put("status", 0);
			return data;
		}

		String[] ids = orderIds.split(",");
		if (ids.length != resource.getNumber()) {
			data.put("message", "只能选择" + resource.getNumber() + "人");
			data.put("status", 0);
			return data;
		}
		List<String> list = new ArrayList<String>();
		for (String id : ids) {
			ResourceOrder order = this.getBaseService().getById(id);
			if (!order.getResourceId().equals(resourceId)) {
				data.put("message", "该订单无效，订单号：" + order.getOrderId());
				data.put("status", 0);
			}
			list.add(id);
		}
		
		try {
			resourceOrderService.processSelected(resource, list);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	@RequestMapping(value="selectOrders")
	@ResponseBody
	public Map<String, Object> selectOrders(@RequestParam int resourceId, @RequestParam String orderIds) {
		Resource resource = resourceService.getById(resourceId + "");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceId", resourceId);
		params.put("status", 2);
		List<ResourceOrder> orders = this.getBaseService().findList(params);

		String[] ids = orderIds.split(",");

		Map<String, Object> data = new HashMap<String, Object>();
		if (ids.length + orders.size() > resource.getNumber()) {
			data.put("message", "只能选择" + resource.getNumber() + "人");
			data.put("status", 0);
			return data;
		}

		try {
			resourceOrderService.processSelectOrders(resource, ids, ids.length + orders.size() == resource.getNumber());
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 双向订单筛选 3.0版本
	 * @param resourceId
	 * @param orderIds
	 * @return
	 */
	@RequestMapping(value="orderselected")
	@ResponseBody
	public Map<String, Object> orderselected(@RequestParam int resourceId, @RequestParam String orderIds) {
		Map<String, Object> data = new HashMap<String, Object>();		
		Resource resource = resourceService.getById(resourceId + "");
		if (resource.getStatus() !=1) {
			data.put("message", "该任务不能报名！");
			data.put("status", 0);
			return data;
		}

		String[] ids = orderIds.split(",");
		if (ids.length ==0) {//没有选人 
			data.put("message", "请选人");
			data.put("status", 0);
			return data;
		}
		if (ids.length > resource.getNumber()) {// 不能选多了
			data.put("message", "只能选择" + resource.getNumber() + "人");
			data.put("status", 0);
			return data;
		}
		List<String> list = new ArrayList<String>();
		for (String id : ids) {
			ResourceOrder order = this.getBaseService().getById(id);
			if (!order.getResourceId().equals(resourceId)) {
				data.put("message", "该订单无效，订单号：" + order.getOrderId());
				data.put("status", 0);
			}
			list.add(id);
		}
		
		try {
			resourceOrderService.processSelectedOrders(resource, list);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "成功！");
		data.put("status", 1);
		return data;
	}
	
	
	@RequestMapping(value="pay")
	@ResponseBody
	public Map<String, Object> pay(@RequestParam String orderId) {
		Map<String, Object> data = new HashMap<String, Object>();
		ResourceOrder order = this.getBaseService().getById(orderId);
		if (order == null) {
			data.put("message", "该单不存在！");
			data.put("status", 0);
			return data;
		}
		Resource resource = resourceService.getById(order.getResourceId() + "");
		if (resource.getCategory() == 1) { // 需
			data.put("message", "无须支付！");
			data.put("status", 0);
			return data;
		} else {
			Pager<ResourceOrder> pager = new Pager<ResourceOrder>();
			pager.setCurrent(0);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("resourceId", resource.getResourceId());
			params.put("status", 1);
			Pager<ResourceOrder> p = this.getBaseService().findByPager(pager, params);
			if (p.getTotal() >= resource.getNumber()) {
				data.put("message", "人数已满！");
				data.put("status", 0);
				return data;
			}
			MobileUser user = mobileUserService.getById(order.getmUserId() + "");
			if ((user.getmUserMoney() - user.getFrozenMoney()) < resource.getPrice()) {
				data.put("message", "用户余额不足！");
				data.put("status", 9);
				return data;
			}
		}
		
		try {
			resourceOrderService.pay(resource, order);
		} catch (Exception e) {
			data.put("message", "付款失败！" + e.getMessage());
			data.put("status", 0);
			return data;
		}

		data.put("result", "");
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 给多人付款,给任务主人付款.  只要是有确认付款,说明任务完成
	 * @param resourceId
	 * @param orderIds
	 * @return
	 */
	
	@RequestMapping(value="payorders")
	@ResponseBody
	public Map<String, Object> payorders(@RequestParam String orderIds,@RequestParam int resourceId) {//需
		
		
		
		Map<String, Object> data = new HashMap<String, Object>();
		String[] ordres=orderIds.split(",");
		

 
		Resource resource = resourceService.getById(resourceId + "");
		if(resource.getCategory()==0){//拍卖技能  当前给任务的拥有者(单人)付款  此时,任务未必结束,继续保持:2,只有所有人给他付款,任务才结束:3
			for(int i =0;i<ordres.length;i++){
				ResourceOrder order = this.getBaseService().getById(ordres[i]);
				resourceOrderService.processConfirm(order);
				//this.giveTheResourceOwnerMoney(ordres[i]);//给任务拥有者付款的方法
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("resourceId", resourceId);
			params.put("status", 1);
			List<ResourceOrder> orders = this.getBaseService().findList(params);//获得付款报名成功,但是还没有确认打款的的订单
			if(orders.size()>0){//保持任务状态为2
				
			}else{//所有该付款的人都确认给任务发布者打款

				Resource entity = new Resource();
				entity.setStatus(3); //任务才置为3 完成
				entity.setResourceId( resourceId);
				resourceService.update(entity);
			}
			
			
		}else if(resource.getCategory()==1){//需要 给接任务的(多人)付款 此时,任务是结束状态
			for(int i =0;i<ordres.length;i++){
				ResourceOrder order = this.getBaseService().getById(ordres[i]);
				resourceOrderService.processConfirm(order);
				//this.giveThemMoneyOneOrder(ordres[i]);//给办事儿的人付款的方法
			}
			Resource entity = new Resource();
			entity.setStatus(3);//给别人打款后,任务结束 王威
			entity.setResourceId( resourceId);
			resourceService.update(entity);
			
		}
		
		

		 

		
		data.put("result", "");
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="giveThemMoneyOneOrder")
	@ResponseBody
	public Map<String, Object> giveThemMoneyOneOrder(@RequestParam String orderId) {
		Map<String, Object> data = new HashMap<String, Object>();
		ResourceOrder order = this.getBaseService().getById(orderId);
		if (order == null) {
			data.put("message", "该单不存在！");
			data.put("status", 0);
			return data;
		}
		Resource resource = resourceService.getById(order.getResourceId() + "");
 
		try {
			this.giveThemMoney(resource, order);
		} catch (Exception e) {
			data.put("message", "付款失败！" + e.getMessage());
			data.put("status", 0);
			return data;
		}

		data.put("result", "");
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	//给办事儿人收货支付
	public void giveThemMoney (Resource resource, ResourceOrder order) { }
	
	// 收货后支付给任务提供者
	public void giveTheResourceOwnerMoney ( String orderid) { }
	
	/**
	 * 给多人付款或者给任务拥有者付款界面-- 获取这些人的信息  或者  获取任务(单人)拥有者付款
	 * @param resourceId
	 * @param orderIds
	 * @return
	 */
	
	@RequestMapping(value="getUsersByResourceId")
	@ResponseBody
	public Map<String, Object> getUsersByResourceId(@RequestParam String resourceid,@RequestParam int muid) { 
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> result =null;
		
		Resource resource = resourceService.getById(resourceid);
		if (resource.getStatus()==0){
			data.put("result", "任务已失效!");
			data.put("message", "");
			data.put("status", 1);
			return data;
			
		}else if(resource.getStatus()==3){//彻底完成
			data.put("result", "任务已经完成!");
			data.put("message", "");
			data.put("status", 1);
			return data;
		}else if(resource.getStatus()==4){
			data.put("result", "任务已经取消!");
			data.put("message", "");
			data.put("status", 1);
			return data;
		} 
		
		if(resource.getCategory()==0){// 拍卖技能  被付款的人是任务的拥有者  
			String sql="select  r.order_id,  u.m_user_id muid,u.sex,u.photo,u.nickname,u.level  from  phb_resource_order r left join  phb_resource c on r.resource_id = c.resource_id left join  phb_mobile_user u  on u.m_user_id=c.m_user_id where r.resource_id = "+ resourceid +"  and r.m_user_id="+muid+" and r.status = 1 ";
			result = this.jdbcTemplate.queryForList(sql);
		}else if(resource.getCategory()==1){// 需求
			String sql="select  r.order_id,  u.m_user_id muid,u.sex,u.photo,u.nickname,u.level from  phb_resource_order r left join  phb_mobile_user u on u.m_user_id=r.m_user_id where r.resource_id = "+ resourceid +" and status = 1";
			result = this.jdbcTemplate.queryForList(sql);
		}
		

		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam int orderId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			this.getBaseService().delete(params);
		} catch (Exception e) {
			data.put("message", "删除失败！" + e.getMessage());
			data.put("status", 0);
			return data;
		}

		data.put("message", "删除成功！");
		data.put("status", 1);
		return data;
	}	
}
