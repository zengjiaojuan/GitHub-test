package com.phb.puhuibao.web.controller;

import java.text.SimpleDateFormat;
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
import com.phb.puhuibao.common.Functions;
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
				//---获得截止日期---
				Date nowday =  new Date() ; 
				Date startday = investment.getIncomeDate();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(startday);
				calendar.add(Calendar.DAY_OF_MONTH, appContext.getExperiencePeriod());
				Date expiredDate = calendar.getTime();
				investment.setExpireDate(expiredDate);
				int leftDays=Functions.calLeftDays(startday, expiredDate);
				investment.setLeftDays(leftDays);			
			    }
			 investment.setTotalIncome(investment.getLastIncome());// 总收益	
			 result.add(investment);	
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
		entity.setmUserId(muid);
		entity.setProductSN(productSN);
		entity.setInvestmentAmount(investmentAmount);
		entity.setStatus(1);
		Calendar cal = Calendar.getInstance();	//得到投资当天时间
		//------计算并set起息日期-------
		int waitDay=0;
		int w = cal.get(Calendar.DAY_OF_WEEK)+1;
		if (w == 1) {
			waitDay=1;
			cal.add(Calendar.DATE, 1);
		} else if (w == 7) {
			waitDay=2;
			cal.add(Calendar.DATE, 2);
		}	
		while (true) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			String sql = "select 1 from phb_holiday where holiday_date='" + date + "'";
			List <Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list.isEmpty()) {
				break;
			}
			waitDay+=1;
			cal.add(Calendar.DATE, 1);
		}
		if(waitDay==0){
			cal.add(Calendar.DATE, 1);
			entity.setIncomeDate(cal.getTime());	
		}
		else{
			entity.setIncomeDate(cal.getTime());
		}			
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
