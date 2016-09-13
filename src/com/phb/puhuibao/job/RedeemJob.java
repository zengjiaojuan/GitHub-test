package com.phb.puhuibao.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.ExperienceInvestment;
import com.phb.puhuibao.entity.ExperienceProduct;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.ProductBid;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserMessage;
import com.phb.puhuibao.service.UserInvestmentService;
import com.phb.puhuibao.web.controller.SendSMSController;

@Component
public class RedeemJob {
	@Resource(name = "userInvestmentService")
	private IBaseService<UserInvestment, String> baseUserInvestmentService;
	@Resource(name = "userInvestmentService")
	private UserInvestmentService userInvestmentService;
	@Resource(name = "itemInvestmentService")
	private IBaseService<ItemInvestment, String> itemInvestmentService;
	@Resource(name = "loanItemService")
	private IBaseService<LoanItem, String> loanItemService;
	@Resource(name = "assetProductService")
	private IBaseService<AssetProduct, String> assetProductService;
	@Resource(name = "productBidService")
	private IBaseService<ProductBid, String> productBidService;
	@Resource(name = "experienceInvestmentService")
	private IBaseService<ExperienceInvestment, String> experienceInvestmentService;
	@Resource(name = "experienceProductService")
	private IBaseService<ExperienceProduct, String> experienceProductService;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "sendSMSController")
	private SendSMSController sendSMSController;
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> baseMobileUserService;
	
	@Resource(name = "userMessageDao")
	private IBaseDao<UserMessage, String> userMessageDao;
    private  double monthBeforeLastIncome=0.00;
		
    public void redeem() {    	
    	/*理财产品返息and结算
		 *       朗月赢按月给息，到期给本息
		                      其余产品都是到期给本息*/
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 1);
		
		UserMessage message = new UserMessage();
		String productSN=null;
		List<UserInvestment> investments = baseUserInvestmentService.findList(params);
		for (UserInvestment investment : investments) {
			productSN = investment.getProductSN();
			params = new HashMap<String, Object>();
			params.put("productSN", productSN);
			AssetProduct product = assetProductService.unique(params);
			Date incomeDate = investment.getIncomeDate(); 
			Calendar monthCal = Calendar.getInstance();
             // 朗月赢    type:2
			   if ((product.getType() == 2)) { 
				  if (investment.getLastDate() == null) { 
					investment.setLastDate(incomeDate); 
				   }
				
				monthCal.setTime(investment.getLastDate());// 获取上次计算利息的日期
			    monthCal.add(Calendar.MONTH, 1);
			    
                Calendar cal = Calendar.getInstance();
				long currentTime = cal.getTimeInMillis();
		        if (currentTime >= monthCal.getTimeInMillis()&&currentTime<investment.getExpireDate().getTime()) {//又满了一个月 :  每天把上次计息日期加一个月,如果加完后小于当前时间
		       
					double lastIncome = investment.getLastIncome(); 
					monthBeforeLastIncome=investment.getLastIncome();
		        	investment.setLastIncome(lastIncome);
			        investment.setLastDate(monthCal.getTime());
					userInvestmentService.monthProcess(investment);
					MobileUser user = baseMobileUserService.getById(investment.getmUserId() + "");
					
					 message =  new UserMessage();
					 message.setmUserId(user.getmUserId());
					 message.setTitle("收到月息");
					 message.setContent(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "获得月息"+(investment.getLastIncome()-monthBeforeLastIncome)+"元");
					 userMessageDao.save(message);
					
					
		        } else {
		        	
		        	continue;
		        } 
		        if(currentTime>=investment.getExpireDate().getTime()){
		        	investment.setStatus(2);		        	
		        	baseUserInvestmentService.update(investment);
		        	userInvestmentService.monthProcessLast(investment);
		        	MobileUser user= baseMobileUserService.getById(investment.getmUserId() + "");
		        	 message =  new UserMessage();
					 message.setmUserId(user.getmUserId());
					 message.setTitle("收回本金");
					 message.setContent(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "收到"+investment.getBidSN()+"本金"+investment.getInvestmentAmount()+"元");
					 userMessageDao.save(message);
		        }
		} else{
				   
				   Calendar cal2 = Calendar.getInstance();		
		 			 cal2.setTime(incomeDate);
		 			 if (product.getUnit().equals("年")) {
		 	            cal2.add(Calendar.YEAR, product.getPeriod());
		 	        } else if (product.getUnit().indexOf("月") > 0) {
		 	            cal2.add(Calendar.MONTH, product.getPeriod());
		 	        } else {
		 	            cal2.add(Calendar.DATE, product.getPeriod());
		 	        }
		 			 Calendar cal = Calendar.getInstance();
		 			 cal.getTime().setHours(cal2.getTime().getHours());
		 			 cal.getTime().setMinutes(cal2.getTime().getMinutes());
		 			 cal.getTime().setSeconds(cal2.getTime().getSeconds());
					long currentTime = cal.getTimeInMillis(); 					
		 	        if (currentTime >= cal2.getTimeInMillis()) {
		 	        	 double lastIncome = investment.getLastIncome();
		 	        	investment.setStatus(2);
		 	        	investment.setLastDate(cal.getTime());
			        	baseUserInvestmentService.update(investment);
			        	userInvestmentService.monthProcessLast(investment);
			        	MobileUser user = baseMobileUserService.getById(investment.getmUserId() + "");
			             message =  new UserMessage();
						 message.setmUserId(user.getmUserId());
						 message.setTitle("收到本金利息");
						 message.setContent(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "收到"+investment.getBidSN()+"本金利息"+new BigDecimal(investment.getInvestmentAmount() + investment.getLastIncome()).setScale(2, RoundingMode.HALF_UP)+"元");
						 userMessageDao.save(message);
		 	        } 
			   } 
		}
		
		//-----------------理财体验产品返息and结算-------------------
				                   
		params = new HashMap<String, Object>();
		params.put("status", 1);
		List<ItemInvestment> itemInvestments = itemInvestmentService.findList(params);		
		params = new HashMap<String, Object>();
		params.put("status", 2); // 回款中
		List<ProductBid> bids = productBidService.findList(params);
		for (ProductBid bid : bids) {
			params = new HashMap<String, Object>();
			params.put("bidSN", bid.getBidSN());
			params.put("status", 1); // 收益中
			investments = baseUserInvestmentService.findList(params);
			if (investments.size() == 0) {
				bid.setStatus(3);
				productBidService.update(bid);
			}
		}

		params = new HashMap<String, Object>();
		params.put("status", 1); // 一直是抢购状态
		List<ExperienceInvestment> experienceInvestments = experienceInvestmentService.findList(params);
		for (ExperienceInvestment exinvestment : experienceInvestments) {
			 productSN = exinvestment.getProductSN();
			params = new HashMap<String, Object>();
			params.put("productSN", productSN);
			ExperienceProduct product = experienceProductService.unique(params);
<<<<<<< HEAD
			Date incomeDate = exinvestment.getIncomeDate();
			Calendar cal = Calendar.getInstance();
			 cal.getTime().setHours(incomeDate.getHours());
 			 cal.getTime().setMinutes(incomeDate.getMinutes());
 			 cal.getTime().setSeconds(incomeDate.getSeconds());
			long currentTime = cal.getTimeInMillis();
=======
			Date incomeDate = investment.getIncomeDate();//起息日
			cal = Calendar.getInstance();
>>>>>>> e91e3e4319d9e61a0e704edfdab0b2ae9d937ba6
			cal.setTime(incomeDate);
	        cal.add(Calendar.DATE, product.getPeriod());
	        if (currentTime >= cal.getTimeInMillis()) { // 还没有到期
	        	//到结算日了
		        double rate = exinvestment.getAnnualizedRate();
				double amount = exinvestment.getInvestmentAmount();
		        double lastIncome = Functions.calTotalIncome(amount, rate, product.getPeriod(), 365);
		        
		        ExperienceInvestment i = new ExperienceInvestment();
				i.setInvestmentId(exinvestment.getInvestmentId());
				i.setLastIncome(lastIncome);
				i.setStatus(2); // 投资状态status：募集中0、收益中1、到期赎回2、提前赎回3
				i.setmUserId(exinvestment.getmUserId());
				experienceInvestmentService.update(i);
				MobileUser user = baseMobileUserService.getById(exinvestment.getmUserId() + "");
				i = experienceInvestmentService.getById(i.getInvestmentId() + "");
				//sendSMSController.sendExperienceIncome(i, user.getmUserTel());
				
				 message.setmUserId(user.getmUserId());
				 message.setTitle("收到体验金利息");
				 message.setContent(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "收到"+i.getProductSN()+"本金利息"+i.getLastIncome()+"元");
				 userMessageDao.save(message);
				
	        }

<<<<<<< HEAD
=======
	        //到结算日了
	        double rate = investment.getAnnualizedRate();//年化利率
			double amount = investment.getInvestmentAmount();//投资金额
	        double lastIncome = Functions.calTotalIncome(amount, rate, product.getPeriod(), 365);//累计收益
>>>>>>> e91e3e4319d9e61a0e704edfdab0b2ae9d937ba6
	        
			
		}
	}
}
