package com.phb.puhuibao.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 今天晚上10点把过期无效刮奖删除  减少垃圾数据
@Component  
public class deleteUselessLuckyMoneyJob {
	private static final Log log = LogFactory.getLog(deleteUselessLuckyMoneyJob.class);
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	 
	@Scheduled(cron="0 0 22 * * ?")
    public void process() {
		try {
			String sql = "delete from phb_muser_experience where status =0";
			 this.jdbcTemplate.execute(sql);
			 
		} catch (Exception e) {
			e.getStackTrace();
			log.error("后台删除刮奖job任务出错:"+e.getLocalizedMessage());
			 
		}
	}
}
