package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.context.AppContext;

@Controller
@RequestMapping(value = "/appContext")
public class ApplicationContextController {
	@Resource(name = "appContext")
	private AppContext appContext;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * getAbout 关于我们
	 * @return
	 */
	@RequestMapping(value="getAbout")
	@ResponseBody
	public Map<String, Object> getAbout(){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", appContext.getAbout());
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}
	
	/**
	 * 初始化数据库
	 * @return
	 */
	@RequestMapping(value="initDB")
	@ResponseBody
	public Map<String, Object> initDB(){
		String sql = "delete from phb_muser_investment";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_product_bid";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_item_investment";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_loan_item";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_loan";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_account";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_account_log";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_mobile_consume_discount";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_order";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_feedback";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_message";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_invite";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_notification";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_third_pay_log";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_third_pay_account";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_month_balance";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_resource";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_mobile_user_signin";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_mobile_user_loan";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_mobile_user_certificate";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_card";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_redpacket";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_experience_investment";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_experience";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_mobile_user_extra";
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_mobile_user";
		this.jdbcTemplate.execute(sql);
		sql = "delete from uploadfile";
		this.jdbcTemplate.execute(sql);
		sql = "phb_muser_appreciation";
		this.jdbcTemplate.execute(sql);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", "ok");
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(params = "method=processInterceptor", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> processInterceptor(){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", "权限不足！");
		data.put("status", 0);
		return data;		
	}
	
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(params = "method=wronglogin", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> wronglogin(){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", "请重新登陆！");
		data.put("status", 20);
		return data;		
	}

	/**
	 * 获得版本
	 * @return
	 */
	@Deprecated
	@RequestMapping(value="getVersion")
	@ResponseBody
	public Map<String, Object> getVersion(){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", appContext.getVersion());
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}
}
