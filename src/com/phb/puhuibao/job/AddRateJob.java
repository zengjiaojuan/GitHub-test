package com.phb.puhuibao.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.entity.UserAddrate;


// 今天晚上11点把过期加息劵置为无效
@Component  
public class AddRateJob {
 
	private static final Log log = LogFactory.getLog(AddRateJob.class);
	@javax.annotation.Resource(name = "userAddrateService")
	private IBaseService<UserAddrate, String> userAddrateService;
	
 

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	 
	//@Scheduled(cron="0 0 23 * * ?")
    public void process() {
		try {
			String sql = "select record_id from phb_muser_addrate t where   date(t.last_date) =  curdate() and t.rate_status =1";
			List<Map<String, Object>> l = this.jdbcTemplate.queryForList(sql);
			UserAddrate addrate = new UserAddrate();
			 int recordid;
			for (Map<String, Object> m : l) {
				recordid = Integer.parseInt( m.get("record_id").toString());
				addrate.setRecordId(recordid);
				addrate.setRateStatus(0);
				userAddrateService.update(addrate);
			} 
		} catch (Exception e) {
			log.error("加息劵job任务出错:"+e.getLocalizedMessage());
			 
		}
	}
}
