package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.entity.MobileUser;

@Controller
@RequestMapping(value = "/accountQuery")
public class AccountQueryController {
	
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param  
	 * @return
	 */
	@RequestMapping(value="Query")
	@ResponseBody
	public Map<String, Object> Query( @RequestParam String muserTel) {
		Map<String,Object> retobj = null;
		Map<String, Object> data = new HashMap<String, Object>();
		//List <Map<String, Object>> list = null;
		List <Map<String, Object>> list1 = null;
		
		Map<String, Object> params 	= new HashMap<String, Object>();
		params.put("mUserTel", muserTel);
		MobileUser mobileUser = mobileUserService.unique(params);
		int mUserId = mobileUser.getmUserId();
		/*
		String sql="SELECT m_user_id FROM phb_mobile_user WHERE m_user_tel="+muserTel+"";
		list = this.jdbcTemplate.queryForList(sql);
		Object mUserId = list.get(0).get("m_user_id");*/
		
		String sql1="SELECT C.m_user_name,C.m_user_tel,C.id_number,C.frozen_money,C.m_user_money,C.show_money,D.real_money,D.redpacket_money,D.investment_money,D.withdraw_deposit_money,D.recharge,D.accumulated_income FROM (SELECT a.m_user_id,a.m_user_name,a.m_user_tel,a.id_number,a.frozen_money,a.m_user_money,(a.m_user_money-a.frozen_money) show_money  from phb_mobile_user a WHERE a.m_user_id="+mUserId+") C LEFT JOIN  (select m_user_id, (select SUM(amount) from phb_muser_account_log where account_type in (0,1,2,3,4,5,6,7) and m_user_id="+mUserId+") real_money,(SELECT SUM(amount) from phb_muser_account_log  WHERE account_type=5 and m_user_id="+mUserId+") redpacket_money,(SELECT SUM(amount) from phb_muser_account_log  WHERE account_type=4 and m_user_id="+mUserId+") investment_money,(SELECT SUM(amount) from phb_muser_account_log  WHERE account_type=1 and m_user_id="+mUserId+") withdraw_deposit_money,(SELECT SUM(amount) from phb_muser_account_log  WHERE account_type=0 and m_user_id="+mUserId+") recharge,(select sum(amount) from phb_muser_account_log where  account_type in (2,3,5,6) and  m_user_id=5430) accumulated_income from  phb_muser_account_log where m_user_id="+mUserId+" LIMIT 1 ) D ON C.m_user_id=D.m_user_id;";
		list1 = this.jdbcTemplate.queryForList(sql1);
		if(list1.isEmpty()){
			data.put("result", retobj);
			data.put("status", 0);
		}else{
			retobj=list1.get(0);
			data.put("result", retobj);
			data.put("status", 1);
		}
		return data;
	}

	
}
