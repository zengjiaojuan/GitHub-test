package com.phb.puhuibao.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.idp.pub.constants.Constants;
import com.phb.puhuibao.entity.UserAccount;
import com.yeepay.TZTService;

@Component
public class BirthdayJob {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Scheduled(cron="0 48 11 * * *")  
    public void process() {
//		String sql = "SELECT order_id FROM phb_third_pay_account where order_id not in(SELECT order_id FROM phb_muser_account where order_id!='')";
//		List<Map<String, Object>> l = this.jdbcTemplate.queryForList(sql);
//		for (Map<String, Object> m : l) {
//			String orderId = (String) m.get("order_id");
//			Map<String, String> result	= TZTService.queryByOrder(orderId, "");
//			String yborderidFromYeepay  = StringUtils.trimToEmpty(result.get("yborderid"));
//			String amount           	= StringUtils.trimToEmpty(result.get("amount"));
//			String error_code       	= StringUtils.trimToEmpty(result.get("error_code"));
//			String error            	= StringUtils.trimToEmpty(result.get("error"));
//			String customError        	= StringUtils.trimToEmpty(result.get("customError"));
//
//			Map<String, String> data = new HashMap<String, String>();
//			if(!"".equals(error_code)) {
//				data.put(Constants.MESSAGE, error_code + ": " + error);
//			} else if(!"".equals(customError)) {
//				data.put(Constants.MESSAGE, customError);
//			}
//
//			Map<String, String> params 	= new HashMap<String, String>();
//			params.put("orderid", 		orderId);
//			params.put("origyborderid",	yborderidFromYeepay);
//			params.put("amount", 		amount);
//			params.put("currency", 		"156");
//			params.put("cause", 		"");
//			
//			result = TZTService.refund(params);
//			error_code					= StringUtils.trimToEmpty(result.get("error_code"));
//			error						= StringUtils.trimToEmpty(result.get("error"));
//			customError					= StringUtils.trimToEmpty(result.get("customError"));
//
//			data = new HashMap<String, String>();
//			if(!"".equals(error_code)) {
//				data.put(Constants.MESSAGE, error_code + ": " + error);
//			} else if(!"".equals(customError)) {
//				data.put(Constants.MESSAGE, customError);
//			}
//		}
	}

}
