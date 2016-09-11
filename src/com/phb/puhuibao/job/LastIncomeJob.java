package com.phb.puhuibao.job;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.common.alipayapi.DateUtil;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.service.UserInvestmentService;

public class LastIncomeJob {
//每天晚上00:10:00把昨天的收益添加到累计收益。
	private static final Log log = LogFactory.getLog(LastIncomeJob.class);
	@Resource(name = "userInvestmentService")
	private IBaseService<UserInvestment, String> baseUserInvestmentService;
	@Resource(name = "userInvestmentService")
	private UserInvestmentService userInvestmentService;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "userInvestmentDao")
	private IBaseDao<UserInvestment, String> userInvestmentDao;
	
	 public void process() {
			try {
				String sql = "select * from phb_muser_investment t where  (t.last_date)>curdate() and (t.income_date<curdate())";
				List<Map<String, Object>> l = this.jdbcTemplate.queryForList(sql);
				UserInvestment userInvest = new UserInvestment();				
				for (Map<String, Object> m : l) {
					userInvest.setInvestmentId(Integer.parseInt( m.get("investment_id").toString()));
					userInvest.setInvestmentAmount(Long.parseLong( m.get("investment_amount").toString()));
					userInvest.setAnnualizedRate(Double.parseDouble(m.get("annualized_rate").toString()));					
					userInvest.setIncomeDate(DateUtil.strToDate(m.get("income_date").toString()));
					userInvest.setLastDate(DateUtil.strToDate(m.get("last_date").toString()));
					userInvest.setLastIncome(this.getLastIncome(userInvest));									
					userInvestmentDao.update(userInvest);
				} 
			} catch (Exception e) {
				log.error("投资累计收益更新失败:"+e.getLocalizedMessage());
				 
			}			
		}
	 
	 public double getLastIncome(UserInvestment userInvest){
		 Date nowDate=new Date();		 
		 Date incomeDate=userInvest.getIncomeDate();
		 Date lastDate=userInvest.getLastDate();
		 incomeDate.setHours(nowDate.getHours());
		 incomeDate.setMinutes(nowDate.getMinutes());
		 incomeDate.setSeconds(nowDate.getSeconds());
		 lastDate.setHours(nowDate.getHours());
		 lastDate.setMinutes(nowDate.getMinutes());
		 lastDate.setSeconds(nowDate.getSeconds());
		 int days=(int) ((nowDate.getTime()-incomeDate.getTime())/(24*3600*1000));		 
		 int yearDays=(incomeDate.getYear()%4==0&&incomeDate.getYear()%100!=0?366:365);			
		 double lastIncome=userInvest.getInvestmentAmount()*userInvest.getAnnualizedRate()/yearDays*days;
		 return lastIncome;
		}
	
}
