package com.phb.puhuibao.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.ExperienceInvestment;
import com.phb.puhuibao.entity.ExperienceProduct;
import com.phb.puhuibao.entity.UserExperience;




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

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "findList")
	@ResponseBody
	public Map<String, Object> findList(@RequestParam String muid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		params.put("orderBy", "create_time");
		params.put("order", "desc");
		List<ExperienceInvestment> queryResult = this.getBaseService().findList(params);
		List<ExperienceInvestment> result = new ArrayList<ExperienceInvestment>();

		params = new HashMap<String, Object>();
		for (ExperienceInvestment investment : queryResult) {
			investment.setUnit("天");
			if (investment.getStatus() >= 2) {
				investment.setLeftDays(0);
			} else {
				SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
				Date dt;
				try {
					dt = df.parse(investment.getIncomeDate().toString()); // 起息日
					Long time = dt.getTime();// 这就是距离1970年1月1日0点0分0秒的毫秒数
					Long time2 = System.currentTimeMillis();// 与上面的相同,获取系统当前时间毫秒数

					int currentTime_income = Integer.parseInt(Long.toString(((time2 - time) / (1000 * 60 * 60 * 24))));  //
					double amount = investment.getInvestmentAmount();
					double everyIncome = Functions.calEveryIncome(amount, investment.getAnnualizedRate());

					if(currentTime_income<=0){// 如果今天在起息日之前
						investment.setLeftDays(investment.getPeriod());
						investment.setLastIncome(everyIncome * investment.getPeriod());
					}else{
						if (currentTime_income > investment.getPeriod()) {
							investment.setLeftDays(0);
							investment.setLastIncome(everyIncome * investment.getPeriod());
						} else {
							investment.setLeftDays(currentTime_income);
							investment.setLastIncome(everyIncome * currentTime_income);

						}
						
					}
					
					investment.setTotalIncome(everyIncome * investment.getPeriod());// 总收益

				} catch (ParseException e) {
					logger.error("日期格式化错误");
					logger.error(e.getMessage());
				}
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
		params.put("status", 1);
		params.put("gelastDate", new Date());
		// List<UserExperience> result = userExperienceService.findList(params);
		// if (result.isEmpty()) {
		// data.put("message", "您的体验金已用完！");
		// data.put("status", 0);
		// return data;
		// }
		UserExperience experience = userExperienceService.unique(params);
		if (experience.getExperienceAmount() < investmentAmount) {
			data.put("message", "您的体验金不足！");
			data.put("status", 0);
			return data;
		}

		// int amount = 0;
		// for (UserExperience experience : result) {
		// amount += experience.getExperienceAmount();
		// }

		// if (amount != investmentAmount) {
		// data.put("message", "体验金须一次性全投入！");
		// data.put("status", 0);
		// return data;
		// }

		ExperienceInvestment entity = new ExperienceInvestment();
		entity.setmUserId(muid);
		entity.setProductSN(productSN);
		entity.setInvestmentAmount(investmentAmount);
		entity.setStatus(1);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1); // 到账第二天起息
		int w = cal.get(Calendar.DAY_OF_WEEK);
		if (w == 1) {
			cal.add(Calendar.DATE, 1);
		} else if (w == 7) {
			cal.add(Calendar.DATE, 2);
		}
		while (true) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			String sql = "select 1 from phb_holiday where holiday_date='" + date + "'";
			List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
			if (list.isEmpty()) {
				break;
			}
			cal.add(Calendar.DATE, 1);
		}

		entity.setIncomeDate(cal.getTime()); // short date
		try {
			entity = this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "投资失败！" + e.getMessage());
			data.put("status", 0);
			return data;
		}
		data.put("message", "投资成功！");
		data.put("status", 1);
		return data;
	}
}
