package com.phb.puhuibao.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.helper.StringUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSON;
import com.idp.pub.context.AppContext;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.common.InitListener;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.ExperienceInvestment;
import com.phb.puhuibao.entity.ExperienceProduct;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.entity.UserMessage;
import com.phb.puhuibao.entity.UserRedpacket;
import com.phb.puhuibao.entity.UserSession;
import com.phb.puhuibao.service.MobileUserService;

@Transactional
@Service("mobileUserService")
public class MobileUserServiceImpl extends DefaultBaseService<MobileUser, String> implements MobileUserService {
	@Override
	@Resource(name = "mobileUserDao")
	public void setBaseDao(IBaseDao<MobileUser, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	@Resource(name = "mobileUserDao")
	public void setPagerDao(IPagerDao<MobileUser> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@Resource(name = "userInvestmentDao")
	private IBaseDao<UserInvestment, String> userInvestmentDao;
	@Resource(name = "itemInvestmentDao")
	private IBaseDao<ItemInvestment, String> itemInvestmentDao;
	@Resource(name = "assetProductDao")
	private IBaseDao<AssetProduct, String> assetProductDao;
	@Resource(name = "experienceProductDao")
	private IBaseDao<ExperienceProduct, String> experienceProductDao;
	@Resource(name = "userLoanDao")
	private IBaseDao<UserLoan, String> userLoanDao;
	@Resource(name = "loanItemDao")
	private IBaseDao<LoanItem, String> loanItemDao;
	@Resource(name = "experienceInvestmentDao")
	private IBaseDao<ExperienceInvestment, String> experienceInvestmentDao;
	@Resource(name = "userRedpacketDao")
	private IBaseDao<UserRedpacket, String> userRedpacketDao;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "appContext")
	private AppContext appContext;
	@Resource(name = "userMessageDao")
	private IBaseDao<UserMessage, String> userMessageDao;
	
	@Resource(name = "userSessionDao")
	private IBaseDao<UserSession, String> userSessionDao;

	@Override
	public MobileUser save(MobileUser entity) {
		entity.setCreateTime(new Date());
		entity = this.getBaseDao().save(entity);
		
		UserMessage message =  new UserMessage();
		
		
		// 新手红包
		UserRedpacket redpacket = new UserRedpacket();
		redpacket.setmUserId(entity.getmUserId());
		redpacket.setDeductionRate(appContext.getDeductionRate());
		redpacket.setRedpacketAmount(appContext.getRedpacketAmount());
		redpacket.setStatus(1);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, appContext.getRedpacketPeriod());
		redpacket.setLastDate(cal.getTime());
		userRedpacketDao.save(redpacket);
		
		// 新手红包通知
		 message =  new UserMessage();
		 message.setmUserId(entity.getmUserId());
		 message.setTitle("获得新手红包");
		 message.setContent("您于" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "获得新手红包"+appContext.getRedpacketAmount()+"元");
		 userMessageDao.save(message);
		
		 //推荐人
		if (entity.getParentId() != null && entity.getParentId() > 0) {
			redpacket.setmUserId(entity.getParentId());
			redpacket.setRedpacketAmount(appContext.getInviteRedpacketAmount());			
			userRedpacketDao.save(redpacket);
			
			 message =  new UserMessage();
			 message.setmUserId(entity.getParentId());
			 message.setTitle("获得推荐红包");
			 message.setContent("因为推荐"+ entity.getmUserTel()+"您于" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "获得推荐红包"+appContext.getInviteRedpacketAmount()+"元");
			 userMessageDao.save(message);
			
			
			
		}

		 message =  new UserMessage();
		message.setmUserId(entity.getmUserId());
		message.setTitle("系统消息");
		message.setContent("启奏陛下，您于" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "开启了金朗理财之旅！");
		userMessageDao.save(message);

		return entity;
	}
	
 
	@Override
	public Map<String, Object> processFortune(String muid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		List<UserInvestment> investments = userInvestmentDao.find(params);
		double todayIncome = 0;
		double totalIncome = 0; // 预收益
		double investmentAmount = 0;
		long currentTime = new Date().getTime();
		for (UserInvestment investment : investments) {
			if (investment.getStatus() >= 2) {
				//totalIncome += investment.getLastIncome();
			} else {
				String productSN = investment.getProductSN();
				params = new HashMap<String, Object>();
				params.put("productSN", productSN);
				AssetProduct product = assetProductDao.unique(params);
				 double rate = investment.getAnnualizedRate();  // 年利率从用户投资表取
				double amount = investment.getInvestmentAmount();
				double everyIncome = Functions.calEveryIncome(amount, rate);
				if (currentTime >= investment.getIncomeDate().getTime()) {
					todayIncome += everyIncome;
				}
				investmentAmount += amount;
				int factor;
		        if (product.getUnit().equals("年")) {
					//totalIncome += amount * rate * product.getPeriod();
		        	factor = 1;
		        } else if (product.getUnit().indexOf("月") > 0) {
//					if (product.getType() == 2 && investment.getLastDate() != null) { // 朗月赢
//						int period = product.getPeriod();
//						Date incomeDate = investment.getIncomeDate();
//						Calendar monthCal = Calendar.getInstance();
//						monthCal.setTime(incomeDate);
//						while (investment.getLastDate().getTime() >= monthCal.getTimeInMillis()) {
//							period --;
//							monthCal.add(Calendar.MONTH, 1);
//						}
//						totalIncome += amount * rate * period / 12;
//					} else {
						//totalIncome += amount * rate * product.getPeriod() / 12;
						factor = 12;
//					}
		        } else {
					//totalIncome += amount * rate * product.getPeriod() / 365;
					factor = 365;
		        }
				totalIncome += Functions.calTotalIncome(amount, rate, product.getPeriod(), factor);
			}
		}
		
		params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		List<ExperienceInvestment> experienceInvestments = experienceInvestmentDao.find(params);
		for (ExperienceInvestment investment : experienceInvestments) {
			if (investment.getStatus() >= 2) {
				//totalIncome += investment.getLastIncome();
			} else {
				String productSN = investment.getProductSN();
				params = new HashMap<String, Object>();
				params.put("productSN", productSN);
				ExperienceProduct product = experienceProductDao.unique(params);
				double rate = investment.getAnnualizedRate();
				double amount = investment.getInvestmentAmount();
				double everyIncome = Functions.calEveryIncome(amount, rate);
				if (currentTime >= investment.getIncomeDate().getTime()) {
					todayIncome += everyIncome;
				}
				//totalIncome += amount * rate * product.getPeriod() / 365;
				totalIncome += Functions.calTotalIncome(amount, rate, product.getPeriod(), 365);
			}
		}
		
		params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		List<ItemInvestment> itemInvestments = itemInvestmentDao.find(params);
		for (ItemInvestment investment : itemInvestments) {
			if (investment.getStatus() >= 2) {
				//totalIncome += investment.getLastIncome();
			} else {
				String itemSN = investment.getItemSN();
				params = new HashMap<String, Object>();
				params.put("itemSN", itemSN);
				LoanItem item = loanItemDao.unique(params);
				double rate = item.getAnnualizedRate();
				double amount = investment.getInvestmentAmount();
				double everyIncome = Functions.calEveryIncome(amount, rate);
				if (currentTime >= investment.getIncomeDate().getTime()) {
					todayIncome += everyIncome;
				}
				investmentAmount += amount;
				//totalIncome += amount * rate * item.getPeriod() / 12;
				totalIncome += Functions.calTotalIncome(amount, rate, item.getPeriod(), 12);
			}
		}

		BigDecimal todayIncomeBD = new BigDecimal(todayIncome).setScale(2, RoundingMode.HALF_UP);
		BigDecimal totalIncomeBD = new BigDecimal(totalIncome).setScale(2, RoundingMode.HALF_UP);
		
		MobileUser user =  this.getById(muid);
		BigDecimal balanceBD = new BigDecimal(user.getmUserMoney() - user.getFrozenMoney()).setScale(2, RoundingMode.HALF_UP);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("balance", balanceBD.toString());
		result.put("todayIncome", todayIncomeBD.toString());
		result.put("totalIncome", totalIncomeBD.toString());
		result.put("investmentAmount", investmentAmount);
		return result;
	}

	@Override
	public Map<String, Object> processmyAsset(String muid) {
		// 理财投资
//		String sql = "select c.product_name,sum(a.investment_amount) investment_amount,a.status,a.m_user_id from phb_muser_investment a left join phb_product_bid b on a.bid_sn=b.bid_sn left join phb_asset_product c on b.product_sn=c.product_sn group by c.product_name having a.status<=1 and a.m_user_id=" + muid;
		String sql = "select c.product_name,sum(a.investment_amount) investment_amount from (select bid_sn,investment_amount from phb_muser_investment where status<=1 and m_user_id=" + muid + ") a,phb_product_bid b,phb_asset_product c where a.bid_sn=b.bid_sn and b.product_sn=c.product_sn group by c.product_name";
		List <Map<String, Object>> investments = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> investmentMap = new HashMap<String, Object>();
		long investmentAsset = 0;
		for (Map<String, Object> map : investments) {
			investmentMap.put((String) map.get("product_name"), map.get("investment_amount"));
			investmentAsset += ((BigDecimal) map.get("investment_amount")).longValue();
		}
		
		// 项目投资
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		params.put("lestatus", 1);
		List<ItemInvestment> itemInvestments = itemInvestmentDao.find(params);
		for (ItemInvestment investment : itemInvestments) {
			if (investmentMap.get(investment.getUseage()) == null) {
			    investmentMap.put(investment.getUseage(), investment.getInvestmentAmount());
			} else {
			    investmentMap.put(investment.getUseage(), investment.getInvestmentAmount() + (Long) investmentMap.get(investment.getUseage()));
			}
			investmentAsset += investment.getInvestmentAmount();
		}
		
		// 授信贷款
		long creditLoan = 0;
		params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		params.put("type", 0);
		params.put("gstatus", 0);
		params.put("lstatus", 3);
		List<UserLoan> loans = userLoanDao.find(params);
		for (UserLoan loan : loans) {
			creditLoan += loan.getAmount();
		}

		MobileUser user = this.getBaseDao().get(muid);
		Map<String, Object> result = new HashMap<String, Object>();		
		BigDecimal totalAssetBD = new BigDecimal(investmentAsset + user.getmUserMoney() - creditLoan).setScale(2, RoundingMode.HALF_UP);
		result.put("totalAsset", totalAssetBD.toString());
		BigDecimal investmentAssetBD = new BigDecimal(investmentAsset).setScale(2, RoundingMode.HALF_UP);
		result.put("investmentAsset", investmentAssetBD.toString());
		result.put("investmentList", investmentMap);
		BigDecimal balanceAmountBD = new BigDecimal(user.getmUserMoney() - user.getFrozenMoney()).setScale(2, RoundingMode.HALF_UP);
		result.put("balance", balanceAmountBD.toString());
		result.put("frozenMoney", user.getFrozenMoney().toString());
		result.put("creditLoan", creditLoan);
		return result;
	}

	/**
	 * 后台开户
	 * @param entity
	 */
	@Override
	public void adminCreate(MobileUser entity) {
		this.save(entity);
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setNickname(entity.getNickname());
		user.setAge(entity.getAge());
		user.setSex(entity.getSex());
		user.setConstellation(entity.getConstellation());
		user.setEmergencyName(entity.getEmergencyName());
		user.setEmergencyPhone(entity.getEmergencyPhone());
		user.setEmergencyRelation(entity.getEmergencyRelation());
		this.update(user);
	}
	

	/**
	 * 手机号码归属地
	 
	 */
public  String calcMobileProvince(String mobileNumber) {
 
        String jsonString = null;
        com.alibaba.fastjson.JSONObject object = null;
        String urlString = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=" + mobileNumber;
        StringBuffer sb = new StringBuffer();
        BufferedReader buffer;
       
        try{
        	URL url = new URL(urlString);
            InputStream in = url.openStream();
            buffer = new BufferedReader(new InputStreamReader(in,"gb2312"));
            String line = null;
            while((line = buffer.readLine()) != null){
                sb.append(line);
            }
            in.close();
            buffer.close();
            jsonString = sb.toString();
            jsonString = jsonString.substring(jsonString.indexOf('{'), jsonString.length());
            object = JSON.parseObject(jsonString);
            return object.getString("province");
        }catch(Exception e){
        	return "";
        }
        
    }
   
   
 
private  String callUrlByGet(String callurl,String charset){   
    String result = "";   
    try {   
        URL url = new URL(callurl);   
        URLConnection connection = url.openConnection();   
        connection.connect();   
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));   
        String line;   
        while((line = reader.readLine())!= null){    
            result += line;   
            result += "\n";
        }
    } catch (Exception e) {   
        e.printStackTrace();   
        return "";
    }
    return result;
}
/**
 * 手机号码归属地
 
 */
public  String getMobileLocation(String tel) {
	
	try{
		 String url = "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=" + tel;
	        String result = callUrlByGet(url,"GBK");
	        StringReader stringReader = new StringReader(result); 
	        InputSource inputSource = new InputSource(stringReader); 
	        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); 
	        Document document = documentBuilder.parse(inputSource);
	        String retmsg = document.getElementsByTagName("retmsg").item(0).getFirstChild().getNodeValue();
	        if(retmsg.equals("OK")){
	      
	            String province = document.getElementsByTagName("province").item(0).getFirstChild().getNodeValue().trim();
	            String city = document.getElementsByTagName("city").item(0).getFirstChild().getNodeValue().trim();
	            if (province.equals("-") || city.equals("-")) {
	                return (calcMobileProvince(tel) );
	            }else {
	                return (province +","+ city );
	            }
		        }else {
		            return "";
		        }
			} catch(Exception e){
		    	return "";
		    }
 
}
	

	@Override
	public MobileUser userCreate(MobileUser entity) {
		this.save(entity);
		
		// 防止用户两个手机同时登陆两个手机:每次登陆产生一个随机数  每次访问的时候需要带上这个随机数   这个随机数必须在内存和数据库都存在   在用户注册的时候  就会产生这个
		int sessionid = (int) (Math.random()*(999999999-100000000)+100000000);  
		UserSession us = new UserSession();
		us.setSessionId(sessionid);
		us.setUserId(entity.getmUserId());
		userSessionDao.save(us);
		
		InitListener.sessionhash.put(entity.getmUserId(), sessionid);// 用户注册的时候把这个用户的随机数 在内存和数据库葛存一分
		
		String provinceandcity = getMobileLocation(entity.getmUserTel());
		if( !StringUtil.isBlank(provinceandcity) && provinceandcity.indexOf(",")>0){ // 正确获取了用户的省市信息
			String[] array  = provinceandcity.split(",");
			entity.setUserProvince(array[0]);
			entity.setUserCity(array[1]);
			
		}else if(!StringUtil.isBlank(provinceandcity) && provinceandcity.indexOf(",") == -1){// 只获取了 省份信息
			entity.setUserProvince(provinceandcity);
		} else{// 省市信息都是空
			
		}

        entity.setLiveness(sessionid); // 临时存储
		return entity;
		
	}
}
