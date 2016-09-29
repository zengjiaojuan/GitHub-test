package com.phb.puhuibao.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.context.AppContext;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.ExperienceInvestment;
import com.phb.puhuibao.entity.ExperienceProduct;
import com.phb.puhuibao.entity.UserExperience;
import com.phb.puhuibao.service.ExperienceInvestmentService;




@Controller
@RequestMapping(value = "/experienceInvestment")
public class ExperienceInvestmentController extends BaseController<ExperienceInvestment, String> {
	protected  final Logger       logger = LoggerFactory.getLogger(ExperienceInvestmentController.class);
	@Override
	@Resource(name = "experienceInvestmentService")
	public void setBaseService(IBaseService<ExperienceInvestment, String> baseService) {
		super.setBaseService(baseService);
	}

	@Resource(name = "experienceProductService")
	private IBaseService<ExperienceProduct, String> experienceProductService;

	@Resource(name = "userExperienceService")
	private IBaseService<UserExperience, String> userExperienceService;
	
	@Resource(name = "experienceInvestmentService")
	private ExperienceInvestmentService experienceInvestmentService;
	
	@Resource(name = "appContext")
	private AppContext appContext;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "findList")
	@ResponseBody
	public Map<String, Object> findList(@RequestParam String muid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		params.put("orderBy", "create_time");
		params.put("order", "desc");
		List<ExperienceInvestment> queryResult = this.getBaseService().findList(params); //用户体验投资集合findList=query
		List<ExperienceInvestment> result = new ArrayList<ExperienceInvestment>();

		params = new HashMap<String, Object>();
		for (ExperienceInvestment investment : queryResult) {
			investment.setUnit("天");
			if (investment.getStatus() >= 2) {
				investment.setLeftDays(0);
			} else {
				Calendar cal = Calendar.getInstance();
				long currentTime = new Date().getTime();
				Date incomeDate = investment.getIncomeDate();// 起息日
				Long incomeTime = incomeDate.getTime();
				int days=0;
				if (incomeDate.before(new Date())) {   // 当前日大于起息日
					double amount = investment.getInvestmentAmount();
					//double everyIncome = Functions.calEveryIncome(amount, investment.getAnnualizedRate());
					
					 days =(int) (currentTime - incomeTime) / (24 * 3600 * 1000)+1;												
				    BigDecimal lastIncome=new BigDecimal(investment.getInvestmentAmount()).multiply(new BigDecimal(investment.getAnnualizedRate())).multiply(new BigDecimal(days)).divide(new BigDecimal(365), 2, BigDecimal.ROUND_DOWN);
					investment.setLastIncome(lastIncome.doubleValue()); // 累计收益
				}
				cal = Calendar.getInstance();
				cal.setTime(incomeDate);
				if (investment.getUnit().equals("年")) {
					cal.add(Calendar.YEAR, investment.getPeriod());
				} else if (investment.getUnit().indexOf("月") > 0) {
					cal.add(Calendar.MONTH, investment.getPeriod());
				} else {
					cal.add(Calendar.DATE, investment.getPeriod());
				}
				int leftDays=0;
				if(incomeDate.after(new Date())){
					leftDays = investment.getPeriod();
				}else{
					leftDays =investment.getPeriod()-days+1;
				}					
				investment.setLeftDays(leftDays);    //剩余利息日
				/* 
				  Date startday = investment.getIncomeDate();// 起息日
				  Date nowday =  new Date() ;                // 当前时间
				  long diff = nowday.getTime() - startday.getTime();//这样得到的差值是微秒级别(当前时间-起息时间)
				 
				int currentTime_income = (int) (diff / (1000 * 60 * 60 * 24)) ;  // 天数  看到的时候包含当天 //剩余天数
				double amount = investment.getInvestmentAmount();                // 投资金额
				//double everyIncome = Functions.calEveryIncome(amount, investment.getAnnualizedRate());//计算每天的收益，不考虑闰年(投资金额 ,年化利率)
				//double annualizedRate = investment.getAnnualizedRate();
				double everyIncome= amount * (investment.getAnnualizedRate()) / 365;
				//BigDecimal bd1 = new BigDecimal(amount * (investment.getAnnualizedRate()));
				if(currentTime_income<=0){// 如果今天在起息日之前
					investment.setLeftDays(investment.getPeriod());// 到期天数(初始周期5天)
//					BigDecimal   b   =   new   BigDecimal(0.00); 
//					investment.setLastIncome(b.setScale(2,   BigDecimal.ROUND_DOWN).doubleValue());//预期收益 初始为0.00
					investment.setLastIncome(0.0);//预期收益 初始为0.0
				}else{
					if (currentTime_income > investment.getPeriod()) { //剩余天数>到期天数
						investment.setLeftDays(0);
						BigDecimal bg = new BigDecimal(everyIncome * investment.getPeriod());
						double LastIncome = bg.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
						investment.setLastIncome(LastIncome);	
//						investment.setLastIncome(everyIncome * investment.getPeriod());	
					} else {
						investment.setLeftDays(appContext.getExperiencePeriod() -  currentTime_income);
						BigDecimal bg = new BigDecimal(everyIncome * currentTime_income);
						double LastIncome = bg.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
						investment.setLastIncome(LastIncome);
//						investment.setLastIncome(everyIncome * currentTime_income);

				  Date startday = investment.getIncomeDate();// 起息日
				  Date nowday =  new Date() ;
				  long diff = nowday.getTime() - startday.getTime();//这样得到的差值是微秒级别
				 
				  int currentTime_income = (int) (diff / (1000 * 60 * 60 * 24)) + 1;  // 天数  看到的时候包含当天
				  double amount = investment.getInvestmentAmount();
				  double everyIncome = Functions.calEveryIncome(amount, investment.getAnnualizedRate());

				if(currentTime_income<=0){// 如果今天在起息日之前
					investment.setLeftDays(investment.getPeriod());
					investment.setLastIncome(0.0);
				}else{
					if (currentTime_income > investment.getPeriod()) {
						investment.setLeftDays(0);
						investment.setLastIncome(everyIncome * investment.getPeriod());
					} else {
						investment.setLeftDays(appContext.getExperiencePeriod() -  currentTime_income);
						investment.setLastIncome(everyIncome * currentTime_income);
						
					}
					
				}*/
 
				//获得截止日期
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(incomeDate);  // 起息开始时间
				calendar.add(Calendar.DAY_OF_MONTH, appContext.getExperiencePeriod()-1);
				Date expiredDate = calendar.getTime();
				investment.setExpireDate(expiredDate); //结束时间
				
				//总的收益变为BigDecimal 类型 并且结果 取小数点后俩位
				BigDecimal total=new BigDecimal(investment.getInvestmentAmount()).multiply(new BigDecimal(investment.getAnnualizedRate())).multiply(new BigDecimal(investment.getPeriod())).divide(new BigDecimal(365), 2, BigDecimal.ROUND_DOWN);
			   // BigDecimal total = new BigDecimal(everyIncome * investment.getPeriod()).setScale(2, BigDecimal.ROUND_DOWN);
				double totals = total.doubleValue(); //将total变为double类型
				investment.setTotalIncome(totals);// 总收益
			   // investment.setTotalIncome(everyIncome * investment.getPeriod());// 总收益
				result.add(investment);
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	@RequestMapping(value = "save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam String productSN,
			@RequestParam int investmentAmount) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		params.put("status", 3);   // 获得用户所有奖金总和
 
		UserExperience experience = userExperienceService.unique(params);
		if (experience.getExperienceAmount() < investmentAmount) {
			data.put("message", "您的体验金不足！");
			data.put("status", 0);
			return data;
		}
 
		ExperienceInvestment entity = new ExperienceInvestment();
		entity.setmUserId(muid); //用户id
		entity.setProductSN(productSN); //产品编号
		entity.setInvestmentAmount(investmentAmount); //理财投资金额
		entity.setStatus(1);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1); // 到账第二天起息
//		int w = cal.get(Calendar.DAY_OF_WEEK);
//		if (w == 1) {
//			cal.add(Calendar.DATE, 1);
//		} else if (w == 7) {
//			cal.add(Calendar.DATE, 2);
//		}
//		while (true) {
//			String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
//			String sql = "select 1 from phb_holiday where holiday_date='" + date + "'";
//			List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
//			if (list.isEmpty()) {
//				break;
//			}
//			cal.add(Calendar.DATE, 1);
//		}

		entity.setIncomeDate(cal.getTime()); // short date 计息时间
		try {
			    experienceInvestmentService.processSave(entity);
				data.put("message", "投资成功！");
				data.put("status", 1);
				return data;
		} catch (Exception e) {
			data.put("message", "投资失败！" + e.getMessage());
			data.put("status", 0);
			return data;
		}

	}
}
