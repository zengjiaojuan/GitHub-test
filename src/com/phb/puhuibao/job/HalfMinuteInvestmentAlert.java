package com.phb.puhuibao.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.entity.InvestmentAlert;

import push.android.AndroidCustomizedcast;
import push.ios.IOSCustomizedcast;

@Configuration
@EnableScheduling
public class HalfMinuteInvestmentAlert {
	@Resource(name = "investmentAlertService")
	private IBaseService<InvestmentAlert, String> investmentAlertService;
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
 

	//@Scheduled(cron="0/30 * * * * ?") // 每30秒
    public void process() throws Exception {
		

		String sql = "select alert_id,  alert_title,  alert_content, push_time, alert_deviceid, alert_devicetype FROM phb_investment_alert where alert_status=1";
		List<Map<String, Object>> l = this.jdbcTemplate.queryForList(sql);
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		for (Map<String, Object> m : l) { 
			Date pushtimedb=  (Date)m.get("push_time");
			String pushtime = sdf.format(pushtimedb);
			pushtime=pushtime.substring(0, 16);
			String nowtominute=sdf.format(new Date()).substring(0, 16);
			
			if(pushtime.equals(nowtominute)){
				
                String title = (String)m.get("alert_title");
                String content = (String)m.get("alert_content");
                int devicetype = (int)m.get("alert_devicetype");
				String appkey = "55c07c87e0f55a9e25000364";
				String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
				if(devicetype == 1){//android
					String appMasterSecret = "pqqu5smr4g096sbioawemd7czht6mmvt";
					

					AndroidCustomizedcast customizedcast = new AndroidCustomizedcast();
					customizedcast.setAppMasterSecret(appMasterSecret);
					customizedcast.setPredefinedKeyValue("appkey", appkey);
					customizedcast.setPredefinedKeyValue("timestamp", timestamp);
					// TODO Set your alias here, and use comma to split them if there are multiple alias.
					// And if you have many alias, you can also upload a file containing these alias, then 
					// use file_id to send customized notification.
					customizedcast.setPredefinedKeyValue("alias",  m.get("alert_deviceid"));
					// TODO Set your alias_type here
					customizedcast.setPredefinedKeyValue("alias_type", "kUMessageAliasTypeLogin");//umeng 的设置
					customizedcast.setPredefinedKeyValue("ticker", "Android customizedcast ticker");
					customizedcast.setPredefinedKeyValue("title",  title);
					customizedcast.setPredefinedKeyValue("text",   content);
					customizedcast.setPredefinedKeyValue("after_open", "go_app");
					customizedcast.setPredefinedKeyValue("display_type", "notification");
					// TODO Set 'production_mode' to 'false' if it's a test device. 
					// For how to register a test device, please see the developer doc.
					customizedcast.setPredefinedKeyValue("production_mode", "true");
					customizedcast.send();
				
					
					Map<String, Object> fordelete = new HashMap<String, Object>();
					fordelete.put("alertId", (long)m.get("alert_id")+"");
					investmentAlertService.delete(fordelete); 
					
					
				}else{//ios
					String appMasterSecret = "pqqu5smr4g096sbioawemd7czht6mmvt";
					IOSCustomizedcast customizedcast = new IOSCustomizedcast();
					customizedcast.setAppMasterSecret(appMasterSecret);
					customizedcast.setPredefinedKeyValue("appkey", appkey);
					customizedcast.setPredefinedKeyValue("timestamp", timestamp);
					// TODO Set your alias here, and use comma to split them if there are multiple alias.
					// And if you have many alias, you can also upload a file containing these alias, then 
					// use file_id to send customized notification.
					customizedcast.setPredefinedKeyValue("alias", m.get("alert_deviceid"));
					// TODO Set your alias_type here
					customizedcast.setPredefinedKeyValue("alias_type", "kUMessageAliasTypeLogin");
					customizedcast.setPredefinedKeyValue("alert", "【"+title+"】  "+content);
					customizedcast.setPredefinedKeyValue("badge", 0);
					customizedcast.setPredefinedKeyValue("sound", "chime");
					// TODO set 'production_mode' to 'true' if your app is under production mode
					customizedcast.setPredefinedKeyValue("production_mode", "true");
					customizedcast.send();
					
					Map<String, Object> fordelete = new HashMap<String, Object>();
					fordelete.put("alertId", (long)m.get("alert_id")+"");
					investmentAlertService.delete(fordelete); 
				
				}

				
			}
		}
	
		
		
	}
}
