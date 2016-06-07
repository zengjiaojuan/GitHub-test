package com.phb.puhuibao.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.entity.UserRedpacket;


// 今天晚上11点把过期红包置为无效
@Component  
public class RedPacketJob {
 
	private static final Log log = LogFactory.getLog(RedPacketJob.class);
	@javax.annotation.Resource(name = "userRedpacketService")
	private IBaseService<UserRedpacket, String> userRedpacketService;
	
 

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	 
	@Scheduled(cron="0 0 23 * * ?")
    public void process() {
		try {
			String sql = "select  t.redpacket_id  from phb_muser_redpacket t where   date(t.last_date) =  curdate() and t.status =1";
			List<Map<String, Object>> l = this.jdbcTemplate.queryForList(sql);
			UserRedpacket red = new UserRedpacket();
			 int redid;
			for (Map<String, Object> m : l) {
				redid = Integer.parseInt( m.get("redpacket_id").toString());
				red.setRedpacketId(redid);
				red.setStatus(0);
				userRedpacketService.update(red);
			} 
		} catch (Exception e) {
			e.getStackTrace();
			log.error("后台红包job任务出错:"+e.getLocalizedMessage());
			 
		}
	}
}
