package com.phb.puhuibao.job;

import org.springframework.jdbc.core.JdbcTemplate;

import com.idp.pub.service.IBaseService;
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

	//@Scheduled(cron="0 0 1 * * *") // 每日1点
    public void process() { }
}
