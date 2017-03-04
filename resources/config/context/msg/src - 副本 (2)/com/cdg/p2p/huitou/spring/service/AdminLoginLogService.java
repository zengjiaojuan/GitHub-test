package com.cddgg.p2p.huitou.spring.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.entity.Adminuser;
import com.cddgg.p2p.huitou.entity.Log;
import com.cddgg.p2p.huitou.util.GetIpAddress;

@Service
public class AdminLoginLogService {
	
	@Resource
	private HibernateSupport dao;
	
	//记录后台管理人员登录日志
	public void addlog_TRAN(Adminuser adminuser,HttpServletRequest request){
		
		Log loginlog=new Log();
		
		loginlog.setIp(GetIpAddress.getIp(request));
		loginlog.setLoginId(adminuser.getId()+"");
		loginlog.setUserName(adminuser.getRealname());
		loginlog.setLogTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
		
		//保存登录日志
		dao.save(loginlog);
	}
}
