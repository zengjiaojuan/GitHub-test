package com.phb.puhuibao.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.ResourceOrder;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.service.ResourceOrderService;

public class ResourceJob {
	@javax.annotation.Resource(name = "resourceService")
	private IBaseService<Resource, String> resourceService;

	@javax.annotation.Resource(name = "resourceOrderService")
	private IBaseService<ResourceOrder, String> baseResourceOrderService;

	@javax.annotation.Resource(name = "resourceOrderService")
	private ResourceOrderService resourceOrderService;

	@javax.annotation.Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;
	
	@javax.annotation.Resource(name = "userAccountLogService")
	private IBaseService<UserAccountLog, String> userAccountLogService;

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Scheduled(cron="0 0 1 * * *") // 每日1点
    public void process() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 1); // 报名中
		params.put("lpublishTime", new Date());
		List<Resource> list = resourceService.findList(params);
		for (Resource resource : list) {
			Resource entity = new Resource();
			entity.setResourceId(resource.getResourceId());
			entity.setStatus(2); // 报名结束
			String sql = "select count(1) from phb_resource_order where status=2 and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			long c = resource.getNumber() - (long) count.get(0).get("count(1)");
			if (c > 0) {
				if (resource.getCategory() == 1) {
					Functions.refund(mobileUserService, userAccountLogService, resource, c * resource.getPrice());
				} else {
					params = new HashMap<String, Object>();
					params.put("resourceId", resource.getResourceId());
					params.put("status", 1);
					List<ResourceOrder> orders = baseResourceOrderService.findList(params);
					for (ResourceOrder order : orders) {
						ResourceOrder o = new ResourceOrder();
						o.setOrderId(order.getOrderId());
						o.setStatus(8); // 订单取消
						baseResourceOrderService.update(o);
						Functions.refund(mobileUserService, userAccountLogService, order, resource.getPrice());
					}
				}
			}
			resourceService.update(entity);
		}
		
		params = new HashMap<String, Object>();
		params.put("status", 2); // 报名结束
		params.put("lvalidTime", new Date());
		list = resourceService.findList(params);
		for (Resource resource : list) {
			String sql = "select count(1) from phb_resource_order where status=3 and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			long c = resource.getNumber() - (long) count.get(0).get("count(1)");
			if (c > 0) {
				if (resource.getCategory() == 1) {
					Functions.refund(mobileUserService, userAccountLogService, resource, c * resource.getPrice());
				} else {
					params = new HashMap<String, Object>();
					params.put("resourceId", resource.getResourceId());
					params.put("status", 2);
					List<ResourceOrder> orders = baseResourceOrderService.findList(params);
					for (ResourceOrder order : orders) {
						ResourceOrder o = new ResourceOrder();
						o.setOrderId(order.getOrderId());
						o.setStatus(8); // 订单取消
						baseResourceOrderService.update(o);
						Functions.refund(mobileUserService, userAccountLogService, order, resource.getPrice());
					}
				}
			}
		}
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -3); // 3天后自动确认
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dataFormat.format(cal.getTime());
		String sql = "select order_id from phb_resource_order where status=3 and update_time>" + dateTime;
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		for (Map<String, Object> map : result) {
			long order_id = (long) map.get("order_id");
			ResourceOrder order = baseResourceOrderService.getById(order_id + "");
			resourceOrderService.processConfirm(order);
		}
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -30); // 取消退款，只考虑30天内的

	}
}
