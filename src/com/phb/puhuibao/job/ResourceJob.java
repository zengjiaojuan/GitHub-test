package com.phb.puhuibao.job;

import org.springframework.jdbc.core.JdbcTemplate;

import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.UserAccountLog;

public class ResourceJob {
 

	@javax.annotation.Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;
	
	@javax.annotation.Resource(name = "userAccountLogService")
	private IBaseService<UserAccountLog, String> userAccountLogService;

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	//@Scheduled(cron="0 0 1 * * *") // 每日1点
    public void process() { }
}
