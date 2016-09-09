package com.phb.puhuibao.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.idp.pub.constants.Constants;
import com.idp.pub.context.AppContext;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.IBaseService;
import com.idp.pub.utils.JsonUtils;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;

public class Functions {
	private static final Log LOG = LogFactory.getLog(Functions.class);
	
	/**
	 * 计算每天的收益，不考虑闰年
	 * @param amount 总投入 
	 * @param rate 年化率
	 * @return
	 */
	public static double calEveryIncome(double amount, double rate) {
		int yearDays = 365;
//		int year = cal.get(Calendar.YEAR);
//		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
//			yearDays = 366;
//		}
//		double everyIncome = amount * rate / yearDays;
//		BigDecimal bd = new BigDecimal(everyIncome).setScale(2, RoundingMode.DOWN);
//		return bd.doubleValue();
		BigDecimal amountBD = new BigDecimal(String.valueOf(amount));
		BigDecimal rateBD = new BigDecimal(String.valueOf(rate));
		BigDecimal yearDaysBD = new BigDecimal(String.valueOf(yearDays));
		//BigDecimal everyIncomeBD = amountBD.multiply(rateBD).divide(yearDaysBD, 2, BigDecimal.ROUND_DOWN);
		BigDecimal everyIncomeBD = amountBD.multiply(rateBD).divide(yearDaysBD, 2, BigDecimal.ROUND_DOWN);
		return everyIncomeBD.doubleValue();
	}

	public static double calTotalIncome(double amount, double rate, Integer period, int factor) {
		//totalIncome += amount * rate * product.getPeriod() / factor;
		BigDecimal amountBD = new BigDecimal(String.valueOf(amount));
		BigDecimal rateBD = new BigDecimal(String.valueOf(rate));
		BigDecimal periodBD = new BigDecimal(String.valueOf(period));
		BigDecimal factorBD = new BigDecimal(String.valueOf(factor));
		BigDecimal totalIncomeBD = amountBD.multiply(rateBD).multiply(periodBD).divide(factorBD, 2, BigDecimal.ROUND_DOWN);
		return totalIncomeBD.doubleValue();
	}

	public static double calIncomeByUnit(double amount, double rate, int factor) {
		BigDecimal amountBD = new BigDecimal(String.valueOf(amount));
		BigDecimal rateBD = new BigDecimal(String.valueOf(rate));
		BigDecimal factorBD = new BigDecimal(String.valueOf(factor));
		BigDecimal incomeByUnitBD = amountBD.multiply(rateBD).divide(factorBD, 2, BigDecimal.ROUND_DOWN);
		return incomeByUnitBD.doubleValue();
	}

	/**
	 * 截取字符串
	 * @param s
	 * @param length
	 * @return
	 */
	public static String cutString(String s, int length) {
		try {
			if (s.getBytes("UTF-8").length <= length) {
				return s;
			}
		} catch (UnsupportedEncodingException e) {
		}
		StringBuffer sb = new StringBuffer();
		int currentLength = 0;
		for (char c : s.toCharArray()) {
		    try {
				currentLength += String.valueOf(c).getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
			}
		    if (currentLength <= length) {
		    	sb.append(c);
		    } else {
		    	break;
		    }
		}
		return sb.toString() + "...";
	}

	public static void upload(HttpServletRequest request, HttpServletResponse response, String path) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		MultipartFile multipartFile = null;
		Map<String, Object> results = Constants.MAP();
		for (Map.Entry<String, MultipartFile> set : fileMap.entrySet()) {
			multipartFile = set.getValue();// 文件名
		}
		String orgName = multipartFile.getOriginalFilename(); // 获取原始文件名
		String filePath = null;
		try {
			filePath = request.getSession().getServletContext().getResource("/").getPath() + path;
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		File file = new File(filePath + "/" + orgName);
		try {
			File mkdir = new File(filePath);
			if (!mkdir.exists()) {
				mkdir.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			multipartFile.transferTo(file);
			results.put(Constants.SUCCESS, Constants.TRUE);
			results.put("image", path + "/" + orgName);
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
		} catch (IllegalStateException e) {
			results.put(Constants.SUCCESS, Constants.FALSE);
			results.put(Constants.MESSAGE, e.getMessage());
		} catch (IOException e) {
			results.put(Constants.SUCCESS, Constants.FALSE);
			results.put(Constants.MESSAGE, e.getMessage());
		}
        try {
			response.getWriter().write(JsonUtils.toJson(results));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Double estimateIncome(AssetProduct product, JdbcTemplate jdbcTemplate) {
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
			List <Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list.isEmpty()) {
				break;
			}
			cal.add(Calendar.DATE, 1);
		}
		
		Long incomeTime = cal.getTimeInMillis();
        if (product.getUnit().equals("年")) {
            cal.add(Calendar.YEAR, product.getPeriod());
        } else if (product.getUnit().indexOf("月") > 0) {
            cal.add(Calendar.MONTH, product.getPeriod());
        } else {
            cal.add(Calendar.DATE, product.getPeriod());
        }
		if (product.getType() == 3) { // 金宝宝
			cal.add(Calendar.MONTH, -3);
		}

        int amount = com.phb.puhuibao.common.Constants.ESTIMATE_INCOME_BASE;
        double rate = product.getAnnualizedRate();
		double lastIncome = 0;
		if (product.getType() == 4) { // 年年红
			Calendar monthCal = Calendar.getInstance();
			monthCal.setTimeInMillis(incomeTime);;
			while (cal.getTimeInMillis() > monthCal.getTimeInMillis()) {
				monthCal.add(Calendar.MONTH, 1);
				int days = (int) ((monthCal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
				double everyIncome = Functions.calEveryIncome(amount, rate);
				lastIncome += everyIncome * days;
				amount += lastIncome;
				incomeTime = monthCal.getTimeInMillis();
			}
		} else {
	        int days = (int) ((cal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
			double everyIncome = Functions.calEveryIncome(amount, rate);
			lastIncome = everyIncome * days;
		}
		
		return lastIncome;
	}
// 下线投资的时候,下下线投资的时候,计算上级提成
	public static void ProcessCommission(MobileUser u,
			double realInvestmentAmount,
			AppContext appContext, JdbcTemplate jdbcTemplate,
			IBaseDao<MobileUser, String> mobileUserDao,
			IBaseDao<UserAccountLog, String> userAccountLogDao,
			int period, int factor) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + u.getParentId() + " for update";
		jdbcTemplate.execute(sql);
		MobileUser parent = mobileUserDao.get("" + u.getParentId());
		if (parent != null) {
			//double commission = realInvestmentAmount * appContext.getParentCommissionRate() * period / factor;
			//BigDecimal commissionBD = new BigDecimal(commission).setScale(2, RoundingMode.DOWN);
			double commission = calTotalIncome(realInvestmentAmount, appContext.getParentCommissionRate(), period, factor);
			MobileUser user = new MobileUser();
			user.setmUserId(parent.getmUserId());
			user.setmUserMoney(parent.getmUserMoney() + commission);
			mobileUserDao.update(user);

			UserAccountLog log = new UserAccountLog();
			log.setmUserId(parent.getmUserId());
			log.setAmount(commission);
			log.setBalanceAmount(user.getmUserMoney() - parent.getFrozenMoney());
			log.setChangeType("下线投资提成");
			log.setChangeDesc("您的下线：" + u.getmUserName() + u.getmUserTel() + "投资了: " + realInvestmentAmount + "元");
			log.setAccountType(2);
			log.setFromUser(u.getmUserId());
			userAccountLogDao.save(log);
			
			sql = "select 1 from phb_mobile_user where m_user_id=" + parent.getParentId() + " for update";
			jdbcTemplate.execute(sql);
			MobileUser grandparent = mobileUserDao.get("" + parent.getParentId());
			if (grandparent != null) {
				//commission = realInvestmentAmount * appContext.getGrandparentCommissionRate() * period / factor;
				//commissionBD = new BigDecimal(commission).setScale(2, RoundingMode.DOWN);
				commission = calTotalIncome(realInvestmentAmount, appContext.getGrandparentCommissionRate(), period, factor);
				user = new MobileUser();
				user.setmUserId(grandparent.getmUserId());
				user.setmUserMoney(grandparent.getmUserMoney() + commission);
				mobileUserDao.update(user);

				log = new UserAccountLog();
				log.setmUserId(grandparent.getmUserId());
				log.setAmount(commission);
				log.setBalanceAmount(user.getmUserMoney() - grandparent.getFrozenMoney());
				log.setChangeType("下下线投资提成");
				log.setChangeDesc("您的下下线：" + u.getmUserName() + u.getmUserTel() + "投资了: " + realInvestmentAmount + "元");
				log.setAccountType(2);
				log.setFromUser(u.getmUserId());
				userAccountLogDao.save(log);
			}
		}
	}
	
	public static String idCardValidate(String IDStr) {
        String errorInfo = ""; // 记录错误信息
        String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        String Ai = "";
        if (IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为18位。";
            return errorInfo;
        }
        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((cal.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (cal.getTimeInMillis() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // ================ 地区码时候有效 ================
        Map<String, String> map = GetAreaCode();
        if (map.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;
        if (IDStr.length() == 18 && !Ai.equals(IDStr)) {
            errorInfo = "身份证无效，不是合法的身份证号码";
            return errorInfo;
        }
       return "";
    }
	
	 private static Map<String, String> GetAreaCode() {
         Map<String, String> map = new HashMap<String, String>();
         map.put("11", "北京");
         map.put("12", "天津");
         map.put("13", "河北");
         map.put("14", "山西");
         map.put("15", "内蒙古");
         map.put("21", "辽宁");
         map.put("22", "吉林");
         map.put("23", "黑龙江");
         map.put("31", "上海");
         map.put("32", "江苏");
         map.put("33", "浙江");
         map.put("34", "安徽");
         map.put("35", "福建");
         map.put("36", "江西");
         map.put("37", "山东");
         map.put("41", "河南");
         map.put("42", "湖北");
         map.put("43", "湖南");
         map.put("44", "广东");
         map.put("45", "广西");
         map.put("46", "海南");
         map.put("50", "重庆");
         map.put("51", "四川");
         map.put("52", "贵州");
         map.put("53", "云南");
         map.put("54", "西藏");
         map.put("61", "陕西");
         map.put("62", "甘肃");
         map.put("63", "青海");
         map.put("64", "宁夏");
         map.put("65", "新疆");
         map.put("71", "台湾");
         map.put("81", "香港");
         map.put("82", "澳门");
         map.put("91", "国外");
         return map;
     }

     /**
      * 功能：判断字符串是否为数字
      * 
      * @param str
      * @return
      */
     private static boolean isNumeric(String str) {
         Pattern pattern = Pattern.compile("[0-9]*");
         Matcher isNum = pattern.matcher(str);
         if (isNum.matches()) {
             return true;
         } else {
             return false;
         }
     }

     /**
      * 功能：判断字符串是否为日期格式
      * 
      * @param str
      * @return
      */
     public static boolean isDate(String strDate) {
         Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
         Matcher m = pattern.matcher(strDate);
         if (m.matches()) {
             return true;
         } else {
             return false;
         }
     }

     public static boolean isMobile(String mobile) {
    	 Pattern pattern = Pattern.compile("^1\\d{10}$");
         Matcher m = pattern.matcher(mobile);
         if (m.matches()) {
             return true;
         } else {
             return false;
         }
     }
     
	public static void ProcessUserLevel(MobileUser u,
			AppContext appContext,
			IBaseDao<ItemInvestment, String> itemInvestmentDao,
			IBaseDao<UserInvestment, String> userInvestmentDao,
			IBaseDao<UserLoan, String> userLoanDao,
			IBaseDao<LoanItem, String> loanItemDao,
			IBaseDao<MobileUser, String> mobileUserDao) {
//		long amount = 0;
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("mUserId", u.getmUserId());
//		List<UserInvestment> investments = userInvestmentDao.find(params);
//		for (UserInvestment investment : investments) {
//			amount += investment.getInvestmentAmount();
//		}
//		List<ItemInvestment> itemInvestments = itemInvestmentDao.find(params);
//		for (ItemInvestment investment : itemInvestments) {
//			amount += investment.getInvestmentAmount();
//		}
//		params = new HashMap<String, Object>();
//		params.put("mUserId",u.getmUserId());
//		params.put("type", 0);
//		params.put("gstatus", 0); // status=0待审查 status=1审查通过 status=2已还款）
//		List<UserLoan> loans = userLoanDao.find(params);
//		for (UserLoan loan : loans) {
//			amount += loan.getAmount();
//		}
//		Map<Integer, Integer> levelMap = appContext.getLevelMap();
//		int level = 0;
//		for (Map.Entry<Integer, Integer> entry : levelMap.entrySet()) {
//			if (amount >= entry.getValue() && level < entry.getKey()) {
//				level = entry.getKey();
//			}
//		}
//		MobileUser user = new MobileUser();
//		user.setmUserId(u.getmUserId());
//		user.setLevel(level);
//		mobileUserDao.update(user);
	}

	public static double getAmount(MobileUser user,
			IBaseService<UserInvestment, String> baseUserInvestmentService,
			IBaseService<ItemInvestment, String> baseItemInvestmentService,
			IBaseService<UserLoan, String> baseUserLoanService) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", user.getmUserId());
		params.put("status", 1);
		List<UserInvestment> investments = baseUserInvestmentService.findList(params);
		double investmentAmount = 0;
		for (UserInvestment investment : investments) {
			investmentAmount += investment.getInvestmentAmount();
		}
		List<ItemInvestment> itemInvestments = baseItemInvestmentService.findList(params);
		for (ItemInvestment investment : itemInvestments) {
			investmentAmount += investment.getInvestmentAmount();
		}

		long creditLoan = 0;
		double interest = 0;
		long currentTime = new Date().getTime();
		params = new HashMap<String, Object>();
		params.put("mUserId", user.getmUserId());
		params.put("type", 0);
		params.put("gstatus", 0);
		params.put("lstatus", 3); // 授信贷款：status=0待审查 status=1审查通过（同时填写放款日期） status=2已放款 status=3已还款 status=4已平仓
		List<UserLoan> loans = baseUserLoanService.findList(params);
		for (UserLoan loan : loans) {
			creditLoan += loan.getAmount();
			if (loan.getLastDate() != null && currentTime > loan.getLastDate().getTime()) {
				int days = (int) ((currentTime - loan.getLastDate().getTime()) / (24 * 3600 * 1000));
				interest += loan.getAmount() * loan.getRate() * days / 30;
			}
		}
		
		return investmentAmount - creditLoan - interest;
	}
	
	/**
	 * 发送https请求
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return 返回微信服务器响应的信息
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		try {
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(outputStr.getBytes("UTF-8"));
			outputStream.close();
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			LOG.error("连接超时：{}", ce);
		} catch (Exception e) {
			LOG.error("https请求异常：{}", e);
		}
		return null;
	}
	
	public static Map<String, String> parseXml(String xml) {
		 Map<String, String> map = new HashMap<String, String>();
		 Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		 Element root = document.getRootElement();
		 @SuppressWarnings("unchecked")
		List<Element> elementList = root.elements();
		 for (Element e : elementList)
		 map.put(e.getName(), e.getText());
		 return map;
	}
	
    
	public static String toXml(SortedMap<String, String> parameters){
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		for (Entry<String, String> entity : parameters.entrySet()) {
			String k = entity.getKey();
			String v = entity.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
			}else {
				sb.append("<"+k+">"+v+"</"+k+">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

 

 
}
