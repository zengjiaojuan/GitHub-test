package com.phb.puhuibao.web.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.idap.web.common.controller.Commons;
import com.idp.pub.constants.Constants;
import com.idp.pub.context.AppContext;
import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.utils.DESUtils;
import com.idp.pub.web.controller.BaseController;
import com.llpay.client.config.PartnerConfig;
import com.llpay.client.config.ServerURLConfig;
import com.llpay.client.conn.HttpRequestSimple;
import com.llpay.client.utils.LLPayUtil;
import com.llpay.client.vo.CashBean;
import com.llpay.client.vo.PayDataBean;
import com.llpay.client.vo.RetBean;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.ThirdPayAccount;
import com.phb.puhuibao.entity.ThirdPayLog;
import com.phb.puhuibao.entity.UserAccount;
import com.phb.puhuibao.entity.UserCard;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.service.UserAccountService;
import com.yeepay.TZTService;

@Controller
@RequestMapping(value = "/userAccount")
public class UserAccountController extends BaseController<UserAccount, String> {
	private static final Log LOG = LogFactory.getLog(UserAccountController.class);

	@Override
	@javax.annotation.Resource(name = "userAccountService")
	public void setBaseService(IBaseService<UserAccount, String> baseService) {
		super.setBaseService(baseService);
	}
	@javax.annotation.Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@javax.annotation.Resource(name = "appContext")
	private AppContext appContext;

	@javax.annotation.Resource(name = "thirdPayLogService")
	private IBaseService<ThirdPayLog, String> thirdPayLogService;

	@javax.annotation.Resource(name = "thirdPayAccountService")
	private IBaseService<ThirdPayAccount, String> thirdPayAccountService;

	@javax.annotation.Resource(name = "userAccountService")
	private UserAccountService userAccountService;

	@javax.annotation.Resource(name = "userAccountService")
	private IBaseService<UserAccount, String> baseUserAccountService;

	@javax.annotation.Resource(name = "userLoanService")
	private IBaseService<UserLoan, String> baseUserLoanService;

	@javax.annotation.Resource(name = "userInvestmentService")
	private IBaseService<UserInvestment, String> baseUserInvestmentService;

	@javax.annotation.Resource(name = "itemInvestmentService")
	private IBaseService<ItemInvestment, String> baseItemInvestmentService;

	@javax.annotation.Resource(name = "userCardService")
	private IBaseService<UserCard, String> baseUserCardService;
 
	@javax.annotation.Resource(name = "userCardService")
	private IBaseService<UserCard, String> userCardService;
	
	@Resource(name = "commons")
	private Commons commons;

	/**
	 * 翻页
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String muid) {
		Pager<UserAccount> pager = new Pager<UserAccount>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mUserId", muid);
		map.put("orderBy", "create_time");
		map.put("order", "desc");
		Pager<UserAccount> p = this.getBaseService().findByPager(pager, map);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

 // 提现申请 接口
	@RequestMapping(value="saveForIOS")
	@ResponseBody
	public Map<String, Object> saveForIOS(@RequestParam String muid,
			@RequestParam String payPassword,
			@RequestParam double amount
			  ) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		UserCard card = baseUserCardService.unique(params);
		if (card == null) {
			data.put("message", "请先充值！");
			data.put("status", 0);
			return data;
		}
 
		MobileUser user = mobileUserService.getById("" + muid);
		if (StringUtils.isEmpty(user.getPayPassword())) {
			data.put("message", "请设置支付密码！");
			data.put("status", 8);
			return data;
		}
		if (!DESUtils.decrypt(payPassword).equals(user.getPayPassword())) {
			data.put("message", "支付密码不正确！");
			data.put("status", 0);
			return data;
		}
		
		// double 转 string, 再转 bigdecimal, 这样进行计算才不会导致精度损失
		BigDecimal usermobey = new BigDecimal(user.getmUserMoney().toString()); 
		BigDecimal frozenmoney = new BigDecimal(user.getFrozenMoney().toString()); 
		double avaible =   usermobey.subtract(frozenmoney).doubleValue(); 
	 
		if (avaible < amount) {
			data.put("message", "用户余额不足！");
			data.put("status", 0);
			return data;
		}
		if (amount < appContext.getWithdrawAmount() &&  amount < user.getmUserMoney() - user.getFrozenMoney()) {
			data.put("message", "一次提现不得少于" + appContext.getWithdrawAmount() + "或全部提取！");
			data.put("status", 0);
			return data;
		}
		
		UserAccount entity = new UserAccount();
		entity.setmUserId(user.getmUserId());
		entity.setAmount(amount);
		entity.setProcessType(1);
 
		try {
		    entity = userAccountService.processSave(entity);

		} catch (Exception e) {
			LOG.error("提现申请失败:"+e.getLocalizedMessage());
			e.getStackTrace();
			data.put("message", "提现申请失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
		data.put("message", "提现申请成功！");
		data.put("status", 1);
		return data;
	}
 
	
	@RequestMapping(value="cancel")
	@ResponseBody
	public Map<String, Object> cancel(@RequestParam int accountId) {
		Map<String, Object> data = new HashMap<String, Object>();
		int count = userAccountService.processDelete(accountId);
		if (count == 0) {
			data.put("message", "取消失败！");
			data.put("status", 0);
		} else {
			data.put("message", "取消成功！");
			data.put("status", 1);
		}
		return data;
	}
	
	@Override
	@RequestMapping(params = "method=update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(UserAccount entity) {
		Map<String, Object> results = Constants.MAP();
		if (entity.getProcessType() == 1 && entity.getIsPaid() == 1) {
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("mUserId", entity.getmUserId());
//			UserCard card = baseUserCardService.unique(params);
//
			//Map<String, Object> data = withdraw(card.getBankPhone(), entity.getAmount(), card.getBankAccount().substring(0, 6), card.getBankAccount().substring(card.getBankAccount().length() - 4));
			Map<String, Object> data = withdraw(entity.getmUserId(), entity.getAmount());
			if ((int) data.get("status") == 1) {
				String orderId = (String) data.get("result");
				UserAccount userAccount = new UserAccount();
				userAccount.setAccountId(entity.getAccountId());
				userAccount.setOrderId(orderId);
				try {
					baseUserAccountService.update(userAccount);
				} catch (Exception e) {
					results.put(Constants.SUCCESS, Constants.FALSE);
					results.put(Constants.MESSAGE, e.getMessage());
					return results;
				}
				entity.setIsPaid(3); // 银行处理中
			} else {
				results.put(Constants.SUCCESS, Constants.FALSE);
				results.put(Constants.MESSAGE, data.get("message"));
				return results;
			}
		}
		try {
			userAccountService.confirm(entity);
			results.put(Constants.SUCCESS, Constants.TRUE);
		} catch (Exception e) {
			results.put(Constants.SUCCESS, Constants.FALSE);
			results.put(Constants.MESSAGE, e.getMessage());
		}
		return results;
	}
	
 
 
	
	/**
	 * 提现
	 * @param phone
	 * @param amount
	 * @param cardTop
	 * @param cardLast
	 * @param request
	 * @return
	 */
//	@RequestMapping(value="withdraw")
//	@ResponseBody
	public Map<String, Object> withdraw(@RequestParam String phone, @RequestParam double amount, @RequestParam String cardTop, @RequestParam String cardLast) {
		Map<String, String> params 	= new HashMap<String, String>();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		params.put("requestid", 		uuid);
		params.put("identityid", 		phone);
		params.put("identitytype", 		"4");
		params.put("card_top", 			cardTop);
		params.put("card_last", 		cardLast);
		params.put("amount", 			String.valueOf((int) (amount * 100)));
		params.put("currency", 			"156");
		params.put("drawtype", 			"NATRALDAY_URGENT");
		params.put("userip", 			appContext.getUserIP());
//		params.put("imei", 				"");
//		params.put("ua", 				"");

		Map<String, String> result			= TZTService.withdraw(params);
//		String merchantaccount				= StringUtils.trimToEmpty(result.get("merchantaccount")); 
//	    String requestidFromYeepay  	 	= StringUtils.trimToEmpty(result.get("requestid")); 
//	    String ybdrawflowid 	   			= StringUtils.trimToEmpty(result.get("ybdrawflowid")); 
//	    String amountFromYeepay 			= StringUtils.trimToEmpty(result.get("amount")); 
//	    String card_topFromYeepay 	   		= StringUtils.trimToEmpty(result.get("card_top")); 
//	    String card_lastFromYeepay 			= StringUtils.trimToEmpty(result.get("card_last")); 
//	    String signFromYeepay 		   		= StringUtils.trimToEmpty(result.get("sign")); 
	    String status 			   			= StringUtils.trimToEmpty(result.get("status")); 
	    String error_code	   				= StringUtils.trimToEmpty(result.get("error_code")); 
	    String error_msg	   				= StringUtils.trimToEmpty(result.get("error_msg")); 
	    String customError	   				= StringUtils.trimToEmpty(result.get("customError")); 

	    Map<String, Object> data = new HashMap<String, Object>();
		if(!"".equals(error_code)) {
			data.put("message", error_code + ": " + error_msg);
			data.put("status", 0);
			return data;
		} else if(!"".equals(customError)) {
			data.put("message", customError);
			data.put("status", 0);
			return data;
		}
		
		if ("SUCCESS".equals(status)) {
			data.put("result", uuid);
			data.put("message", "提现请求成功。");
			data.put("status", 1);
			return data;
		} else {
			data.put("message", "请求提交失败。");
			data.put("status", 0);
			return data;
		}
	}
	
	/**
	 * 交易明细
	 * @param startdate
	 * @param enddate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="checkAccount")
	@ResponseBody
	public ResponseEntity<byte[]> checkAccount(@RequestParam String startdate, @RequestParam String enddate, HttpServletRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "account.txt");
		String sysPath = request.getSession().getServletContext().getResource("/cache").getPath();

		Map<String, String> result	  	= TZTService.getPathOfPayClearData(startdate, enddate, sysPath);
		String filePath					= StringUtils.trimToEmpty(result.get("filePath"));
		String error_code				= StringUtils.trimToEmpty(result.get("error_code"));
		String error					= StringUtils.trimToEmpty(result.get("error"));
		String customError				= StringUtils.trimToEmpty(result.get("customError"));
		
		if(!"".equals(error_code)) {
			LOG.error(error_code + ": " + error);
			return new ResponseEntity<byte[]>((error_code + ": " + error).getBytes("utf-8"), headers, HttpStatus.OK);
			//throw new Exception(error_code + ": " + error);
		} else if(!"".equals(customError)) {
			LOG.error(customError);
			return new ResponseEntity<byte[]>(customError.getBytes("utf-8"), headers, HttpStatus.OK);
			//throw new Exception(customError);
		}
		
		ByteArrayOutputStream baos = null;
		BufferedReader reader = null; 
		try {
			baos = new ByteArrayOutputStream();
			reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			int i = 0;
			double totalFee = 0;
			double totalAmount = 0;
			baos.write("交易时间,客户订单号,交易流水号,金额,手续费,类型".getBytes("utf-8"));
			baos.write(System.getProperty("line.separator").getBytes("utf-8"));
			while((line = reader.readLine()) != null) {
				String[] ls = line.split(",");
				if (ls.length == 14 && "phb".equals(ls[12])) {
					line = ls[3] + "," + ls[4] + "," + ls[5] + "," + ls[7] + "," + ls[9] + ",充值";
					baos.write(line.getBytes("utf-8"));
					baos.write(System.getProperty("line.separator").getBytes("utf-8"));
					i++;
					double amount = Double.valueOf(ls[7]);
					totalAmount += amount;
					double fee = Double.valueOf(ls[9]);
					totalFee += fee;
					ThirdPayAccount entity = thirdPayAccountService.getById(ls[5]);
					if (entity == null) {
						entity = new ThirdPayAccount();
						entity.setOrderId(ls[4]);
						entity.setYbOrderId(ls[5]);
						entity.setPayDate(ls[3]);
						entity.setAmount(amount);
						entity.setFee(fee);
						entity.setType("充值");
						thirdPayAccountService.save(entity);
					}
				}
			}
			baos.write(("总交易笔数：" + i).getBytes("utf-8"));
			baos.write(System.getProperty("line.separator").getBytes("utf-8"));
			BigDecimal amountBD = new BigDecimal(totalAmount).setScale(2, RoundingMode.DOWN);
			baos.write(("总金额：" + amountBD.doubleValue()).getBytes("utf-8"));
			baos.write(System.getProperty("line.separator").getBytes("utf-8"));
			BigDecimal feeBD = new BigDecimal(totalFee).setScale(2, RoundingMode.DOWN);
			baos.write(("总手续费：" + feeBD.doubleValue()).getBytes("utf-8"));
			baos.write(System.getProperty("line.separator").getBytes("utf-8"));
        } finally {
            try {
                if (reader != null) {
                	reader.close();
                }
            } catch (IOException ioe) {
                // ignore
            }
		}
		        
		return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value="checkAccountRefund")
	@ResponseBody
	public ResponseEntity<byte[]> checkAccountRefund(@RequestParam String startdate, @RequestParam String enddate, HttpServletRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "accountRefund.txt");
		String sysPath = request.getSession().getServletContext().getResource("/cache").getPath();

		Map<String, String> result	  	= TZTService.getPathOfRefundClearData(startdate, enddate, sysPath);
		String filePath					= StringUtils.trimToEmpty(result.get("filePath"));
		String error_code				= StringUtils.trimToEmpty(result.get("error_code"));
		String error					= StringUtils.trimToEmpty(result.get("error"));
		String customError				= StringUtils.trimToEmpty(result.get("customError"));
		
		if(!"".equals(error_code)) {
			LOG.error(error_code + ": " + error);
			return new ResponseEntity<byte[]>((error_code + ": " + error).getBytes("utf-8"), headers, HttpStatus.OK);
			//throw new Exception(error_code + ": " + error);
		} else if(!"".equals(customError)) {
			LOG.error(customError);
			return new ResponseEntity<byte[]>(customError.getBytes("utf-8"), headers, HttpStatus.OK);
			//throw new Exception(customError);
		}
		
		ByteArrayOutputStream baos = null;
		BufferedReader reader = null; 
		try {
			baos = new ByteArrayOutputStream();
			reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			int i = 0;
			double totalFee = 0;
			double totalAmount = 0;
			baos.write("交易时间,客户订单号,交易流水号,金额,手续费,类型".getBytes("utf-8"));
			baos.write(System.getProperty("line.separator").getBytes("utf-8"));
			while((line = reader.readLine()) != null) {
				String[] ls = line.split(",");
				if (ls.length == 15 && "phb".equals(ls[13])) {
					line = ls[3] + "," + ls[4] + "," + ls[5] + "," + ls[8] + "," + ls[10] + ",退款";
					baos.write(line.getBytes("utf-8"));
					baos.write(System.getProperty("line.separator").getBytes("utf-8"));
					i++;
					double amount = Double.valueOf(ls[8]);
					totalAmount += amount;
					double fee = Double.valueOf(ls[10]);
					totalFee += fee;
					ThirdPayAccount entity = thirdPayAccountService.getById(ls[5]);
					if (entity == null) {
						entity = new ThirdPayAccount();
						entity.setOrderId(ls[4]);
						entity.setYbOrderId(ls[5]);
						entity.setPayDate(ls[3]);
						entity.setAmount(amount);
						entity.setFee(fee);
						entity.setType("退款");
						thirdPayAccountService.save(entity);
					}
				}
			}
			baos.write(("总交易笔数：" + i).getBytes("utf-8"));
			baos.write(System.getProperty("line.separator").getBytes("utf-8"));
			BigDecimal amountBD = new BigDecimal(totalAmount).setScale(2, RoundingMode.DOWN);
			baos.write(("总金额：" + amountBD.doubleValue()).getBytes("utf-8"));
			baos.write(System.getProperty("line.separator").getBytes("utf-8"));
			BigDecimal feeBD = new BigDecimal(totalFee).setScale(2, RoundingMode.DOWN);
			baos.write(("总手续费：" + feeBD.doubleValue()).getBytes("utf-8"));
			baos.write(System.getProperty("line.separator").getBytes("utf-8"));
        } finally {
            try {
                if (reader != null) {
                	reader.close();
                }
            } catch (IOException ioe) {
                // ignore
            }
		}
		        
		return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.OK);
	}
//	
	/**
	 * 连连充值交易详细
	 * @param orderId
	 * @return
	 */
	@RequestMapping(params = "method=queryTransaction", method = RequestMethod.GET)
	@ResponseBody
	public ThirdPayLog queryPay(@RequestParam String orderId) {
		Map<String, Object> params 	= new HashMap<String, Object>();
		params.put("orderId", 		orderId);
		ThirdPayLog thirdPayLog =  thirdPayLogService.unique(params);
		return thirdPayLog;
	}
 

//	/**
//	 * 退款
//	 * @param entity
//	 * @return
//	 */
//	@RequestMapping(params = "method=refund", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String, String> refundPay(@RequestParam String orderId) {
//		Map<String, String> result	= TZTService.queryByOrder(orderId, "");
////		String orderidFromYeepay   	= StringUtils.trimToEmpty(result.get("orderid"));
//		String yborderidFromYeepay  = StringUtils.trimToEmpty(result.get("yborderid"));
//		String amount           	= StringUtils.trimToEmpty(result.get("amount"));
//		String error_code       	= StringUtils.trimToEmpty(result.get("error_code"));
//		String error            	= StringUtils.trimToEmpty(result.get("error"));
//		String customError        	= StringUtils.trimToEmpty(result.get("customError"));
//
//		Map<String, String> data = new HashMap<String, String>();
//		if(!"".equals(error_code)) {
//			data.put(Constants.SUCCESS, Constants.FALSE);
//			data.put(Constants.MESSAGE, error_code + ": " + error);
//			return data;
//		} else if(!"".equals(customError)) {
//			data.put(Constants.SUCCESS, Constants.FALSE);
//			data.put(Constants.MESSAGE, customError);
//			return data;
//		}
//
//		Map<String, String> params 	= new HashMap<String, String>();
//		params.put("orderid", 		orderId);
//		params.put("origyborderid",	yborderidFromYeepay);
////		params.put("amount", 		(int) (entity.getAmount() * 100) + "");
//		params.put("amount", 		amount);
//		params.put("currency", 		"156");
//		params.put("cause", 		"");
//		
//		result = TZTService.refund(params);
////		String merchantaccountFromYeepay	= StringUtils.trimToEmpty(result.get("merchantaccount")); 
////		String orderidFromYeepay			= StringUtils.trimToEmpty(result.get("orderid"));
////		String yborderid					= StringUtils.trimToEmpty(result.get("yborderid"));
////		String origyborderidFromYeepay		= StringUtils.trimToEmpty(result.get("origyborderid"));
////		String amountFromYeepay				= StringUtils.trimToEmpty(result.get("amount"));
////		String fee							= StringUtils.trimToEmpty(result.get("fee"));
////		String currencyFromYeepay			= StringUtils.trimToEmpty(result.get("currency"));
////		String timestamp					= StringUtils.trimToEmpty(result.get("timestamp"));
////		String remain						= StringUtils.trimToEmpty(result.get("remain"));
////		String signFromYeepay				= StringUtils.trimToEmpty(result.get("sign"));
//		error_code					= StringUtils.trimToEmpty(result.get("error_code"));
//		error						= StringUtils.trimToEmpty(result.get("error"));
//		customError					= StringUtils.trimToEmpty(result.get("customError"));
//
//		if(!"".equals(error_code)) {
//			data.put(Constants.SUCCESS, Constants.FALSE);
//			data.put(Constants.MESSAGE, error_code + ": " + error);
//			if ("100309".equals(error_code)) {
//				Map<String, Object> p = new HashMap<String, Object>();
//				p.put("orderId", orderId);
//				UserAccount entity = this.getBaseService().unique(p);
//				if (entity.getIsPaid() == 2) {
//				    return data;
//				}
//			} else {
//			    return data;
//			}
//		} else if(!"".equals(customError)) {
//			data.put(Constants.SUCCESS, Constants.FALSE);
//			data.put(Constants.MESSAGE, customError);
//			return data;
//		}
//
//		try {
//		    userAccountService.refund(orderId);
//			if ("".equals(error_code)) {
//				data.put(Constants.SUCCESS, Constants.TRUE);
//			}
//		} catch (Exception e) {
//			data.put(Constants.SUCCESS, Constants.FALSE);
//			data.put(Constants.MESSAGE, "请与系统管理员联系！" + e.getMessage());
//		}
//		return data;
//	}
	
	/**
	 * 添加申请
	 * @param entity
	 * @return
	 */
	@RequestMapping(params = "method=adminCreate", method = RequestMethod.PUT)
	@ResponseBody
	protected Map<String, Object> adminCreate(UserAccount entity) {
		Map<String, Object> results = Constants.MAP();
		entity.setIsPaid(1);
		try {
			userAccountService.adminCreate(entity);
			results.put(Constants.SUCCESS, Constants.TRUE);
		} catch (Exception e) {
			results.put(Constants.SUCCESS, Constants.FALSE);
			results.put(Constants.MESSAGE, e.getMessage());
		}
		return results;
	}
	
	// llpay 充值回调函数    2016-08-02 王威
	@RequestMapping(value="notify")
	@ResponseBody
	public void notify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        RetBean retBean = new RetBean();
        String reqStr = LLPayUtil.readReqStr(req);

//        {"bank_code":"01020000","dt_order":"20160802124349","info_order":"18515895297,6217220200004369074,朱清华,320724198811210053",
//          "money_order":"100.01","no_order":"1779e8c894934a51b1cd08c44dd8c426","oid_partner":"201510191000543502","oid_paybill":"2016080282821252",
//          "pay_type":"D","result_pay":"SUCCESS","settle_date":"20160802",
//          "sign":"DDzdltOEr8W3NqBheDMo1Gx6PBmBB8pwzBsYQB6GVqPLPd1B8LJTephSbep5oXwnfvcdrUwjOsFx3ebUloKtKhX7P7HEWpNncoPkmL4Es70WQ+04/g5ts243YnQZGeWAXAGw1iN3GfmUyARa07f5xT4/8Qh6s6aXexrry5gycdQ=",
//          "sign_type":"RSA"}
        try {
            if (LLPayUtil.isnull(reqStr)) { // 连连的返回字符串是空  
                retBean.setRet_code("9999");
                retBean.setRet_msg("交易失败");
                resp.getWriter().write(JSON.toJSONString(retBean));
                resp.getWriter().flush();
                return;
            }
            
            if (!LLPayUtil.checkSign(reqStr, PartnerConfig.YT_PUB_KEY)) {
                retBean.setRet_code("9999");
                retBean.setRet_msg("交易失败");
                resp.getWriter().write(JSON.toJSONString(retBean));
                resp.getWriter().flush();
                return;
            }
        } catch (Exception e) {
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            resp.getWriter().write(JSON.toJSONString(retBean));
            resp.getWriter().flush();
            return;
        }




        PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
        if (!"SUCCESS".equals(payDataBean.getResult_pay())) {
        	return;
        }
        
        String oid_paybill = payDataBean.getOid_paybill();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderId", oid_paybill);
		UserAccount entity = baseUserAccountService.unique(params);
		if (entity != null) {// 如果这个订单已经插进来了
			return;
		}

		String info_order = payDataBean.getInfo_order(); //例如: 13842555226, 6214990610044647,张三,421125167897637263
		String cardno = null;  //银行卡号
		String identitynumber = null;// 身份证 id
		String username = null;//用户名
		String usertel = null;//用户手机号
		if (info_order.indexOf(",") > 0) {
			String[] info_orderarr = info_order.split(",");
			if (info_orderarr.length != 4) {// 返回的值不够
				return;
			}
			usertel = info_orderarr[0];
			cardno = info_orderarr[1];
			username = info_orderarr[2];
			identitynumber = info_orderarr[3];
		}
		String amount = payDataBean.getMoney_order();
		
		
		

		try {
		        userAccountService.chargeCallBack(cardno,identitynumber,username,usertel,amount,oid_paybill,reqStr);
		        retBean.setRet_code("0000");
		        retBean.setRet_msg("交易成功");
		        resp.getWriter().write(JSON.toJSONString(retBean));
		        resp.getWriter().flush();                                    // 通知连连交易成功
		        
		} catch (Exception e) {//   存储
			LOG.error(e);
			e.printStackTrace();
			
			 retBean.setRet_code("9999");
             retBean.setRet_msg("交易失败");
             resp.getWriter().write(JSON.toJSONString(retBean));
             resp.getWriter().flush();
             return;                                 // 通知连连交易失败
		}
	}
	
	/**
	 * 提现  后台点击提现   2016-08-02 王威
	 * @param muid
	 * @param amount
	 * @return
	 */
	@RequestMapping(value="withdraw")
	@ResponseBody
	public Map<String, Object> withdraw(@RequestParam int muid, @RequestParam double amount) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		UserCard card = baseUserCardService.unique(params);

		CashBean reqBean = new CashBean();
        //reqBean.setUser_id(muid);
        reqBean.setOid_partner(PartnerConfig.OID_PARTNER);
        reqBean.setSign_type(PartnerConfig.SIGN_TYPE);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        reqBean.setNo_order(uuid);
        reqBean.setDt_order(LLPayUtil.getCurrentDateTimeStr());
        reqBean.setMoney_order(amount + "");
        reqBean.setFlag_card("0");
        reqBean.setCard_no(card.getBankAccount());
		MobileUser user = mobileUserService.getById("" + muid);
        reqBean.setAcct_name(user.getmUserName());
        reqBean.setInfo_order("提现");
        String ipandport = commons.getAddrServerIp();
        String projectname = commons.getProjectName();//lcb
        StringBuffer url = new StringBuffer(); // "http://101.200.82.182:7001/lcb/userAccount/withdrawNotify.shtml"
        url.append("http://").append(ipandport).append("/").append(projectname).append("/userAccount/withdrawNotify.shtml");
        reqBean.setNotify_url(url.toString());
        reqBean.setApi_version(PartnerConfig.VERSION);
        reqBean.setPrcptcd(card.getPrcptcd());
        String sign = LLPayUtil.addSign(JSON.parseObject(JSON.toJSONString(reqBean)), PartnerConfig.TRADER_PRI_KEY);
        reqBean.setSign(sign);
        String reqJson = JSON.toJSONString(reqBean);
        String resJson = HttpRequestSimple.getInstance().postSendHttp(ServerURLConfig.WITHDRAW_URL, reqJson);
        RetBean retBean = JSON.parseObject(resJson, RetBean.class);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", uuid);
		data.put("message", retBean.getRet_msg());
		if ("0000".endsWith(retBean.getRet_code())) {
			data.put("status", 1);
		} else {
			data.put("status", 0);
		}
		return data;
	}
	
	/**
	 * 提现回调  后台点击提现回调 withdrawNotify  2016-08-02 王威
	 * @param muid
	 * @param amount
	 * @return
	 */
	 
	@RequestMapping(value="withdrawNotify")
	@ResponseBody
	public void withdrawNotify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        RetBean retBean = new RetBean();
        String reqStr = LLPayUtil.readReqStr(req);
        if (LLPayUtil.isnull(reqStr)) {
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            resp.getWriter().write(JSON.toJSONString(retBean));
            resp.getWriter().flush();
            return;
        }
        try {
            if (!LLPayUtil.checkSign(reqStr, PartnerConfig.YT_PUB_KEY)) {
                retBean.setRet_code("9999");
                retBean.setRet_msg("交易失败");
                resp.getWriter().write(JSON.toJSONString(retBean));
                resp.getWriter().flush();
                return;
            }
        } catch (Exception e) {
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            resp.getWriter().write(JSON.toJSONString(retBean));
            resp.getWriter().flush();
            return;
        }
        retBean.setRet_code("0000");
        retBean.setRet_msg("交易成功");
        resp.getWriter().write(JSON.toJSONString(retBean));
        resp.getWriter().flush();


        
		JSONObject object = JSON.parseObject(reqStr);
        if (!"SUCCESS".equals(object.getString("result_pay"))) {
        	return;
        }

		String no_order = object.getString("no_order");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderId", no_order);
		UserAccount entity = baseUserAccountService.unique(params);
		if (entity.getIsPaid() == 1) {
			return;
		}
		
        ThirdPayLog log = new ThirdPayLog();
        String uuid = UUID.randomUUID().toString().replace("-", "");
		log.setLogId(uuid);
		log.setAction("withdrawNotify");
		log.setParams(reqStr);
		log.setError("");
		log.setStatus(1);
		log.setOrderId(no_order);
		thirdPayLogService.save(log);

		entity.setIsPaid(1);
		userAccountService.confirm(entity);
	}

	/**
	 * 连连退款回调   refundNotify
	 * @param oid_paybill
	 * @param money_refund
	 * @return
	 */
	@RequestMapping(value="refund")
	@ResponseBody
	public Map<String, String> refund(@RequestParam String oid_paybill, @RequestParam String amount) {
        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", PartnerConfig.OID_PARTNER);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        reqObj.put("no_refund", uuid);
        reqObj.put("dt_refund", LLPayUtil.getCurrentDateTimeStr());
        reqObj.put("money_refund", amount);
        reqObj.put("oid_paybill", oid_paybill);
        
		 String ipandport = commons.getAddrServerIp(); 
		 String projectname = commons.getProjectName();//lcb
	     StringBuffer url = new StringBuffer(); // "http://ip:7001/lcb/userAccount/withdrawNotify.shtml"
	     url.append("http://").append(ipandport).append("/").append(projectname).append("/userAccount/refundNotify.shtml");
        reqObj.put("notify_url", url.toString());
        reqObj.put("sign_type", PartnerConfig.SIGN_TYPE);
        String sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(ServerURLConfig.REFUND_URL, reqJSON);

        Map<String, String> data = new HashMap<String, String>();
		try {
			// 使用        RetBean retBean = JSON.parseObject(resJson, RetBean.class);即可。虽然支付、退款、提现有（同步）返回参数，但由于存在回调的原因，只需考虑返回的2个参数即可
			org.json.JSONObject object = new org.json.JSONObject(resJSON);
			String ret_code = object.getString("ret_code");
			data.put(Constants.MESSAGE, object.getString("ret_msg"));
			if ("0000".endsWith(ret_code)) {
		        ThirdPayLog log = new ThirdPayLog();
		        String uuid1 = UUID.randomUUID().toString().replace("-", "");
				log.setLogId(uuid1);
				//log.setLogId(object.getString("oid_refundno")); // phb_muser_account中的order_id保存的是oid_paybill，但oid_paybill无法传到回调
				log.setAction("refund");
				log.setParams(oid_paybill);
				log.setError("");
				log.setStatus(1);
				log.setOrderId(object.getString("no_order"));
				thirdPayLogService.save(log);
				data.put(Constants.SUCCESS, Constants.TRUE);
			} else {
				data.put(Constants.SUCCESS, Constants.FALSE);
			}
		} catch (JSONException e) {
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, e.getMessage());
		}
		return data;
	}
	
	/**
	 * 退款回调
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value="refundNotify")
	@ResponseBody
	public void refundNotify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        RetBean retBean = new RetBean();
        String reqStr = LLPayUtil.readReqStr(req);
        if (LLPayUtil.isnull(reqStr)) {
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            resp.getWriter().write(JSON.toJSONString(retBean));
            resp.getWriter().flush();
            return;
        }
        try {
            if (!LLPayUtil.checkSign(reqStr, PartnerConfig.YT_PUB_KEY)) {
                retBean.setRet_code("9999");
                retBean.setRet_msg("交易失败");
                resp.getWriter().write(JSON.toJSONString(retBean));
                resp.getWriter().flush();
                return;
            }
        } catch (Exception e) {
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            resp.getWriter().write(JSON.toJSONString(retBean));
            resp.getWriter().flush();
            return;
        }
        retBean.setRet_code("0000");
        retBean.setRet_msg("交易成功");
        resp.getWriter().write(JSON.toJSONString(retBean));
        resp.getWriter().flush();

        PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
        if (!"SUCCESS".equals(payDataBean.getResult_pay())) {
        	return;
        }
        
        String no_order = payDataBean.getNo_order();
        
        
        ThirdPayLog log = new ThirdPayLog();
        String uuid = UUID.randomUUID().toString().replace("-", "");
		log.setLogId(uuid);
		log.setAction("refundNotify");
		log.setParams(reqStr);
		log.setError("");
		log.setStatus(1);
		log.setOrderId(no_order);
		thirdPayLogService.save(log);

		JSONObject object = JSON.parseObject(reqStr);
		String oid_refundno = object.getString("oid_refundno");
		String sta_refund = object.getString("sta_refund");
		if ("2".equals(sta_refund)) { // 退款成功
			log = thirdPayLogService.getById(oid_refundno);
			if (log == null) {
				LOG.error(reqStr);
			} else {
				String oid_paybill = log.getParams();
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("orderId", oid_paybill);
				UserAccount entity = this.getBaseService().unique(p);
				if (entity.getIsPaid() == 2) {
				    return;
				}
			    userAccountService.refund(oid_paybill);
			}
		}
	}
	
    /** APP_ID 应用从官方网站申请到的合法appId */
    public static final String WX_APP_ID = "wx28c3e05616e93328";
    /** 商户号 */
    public static final String WX_PARTNER_ID = "1285549801";
    /** 接口链接 */
    public static final String WX_PREPAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /** 商户平台和开发平台约定的API密钥，在商户平台设置  */
    public static final String WX_KEY = "qwertyuiop1234567890asdfghjkl123";

   
   
	
	private void paySuccess(HttpServletResponse response) {
        SortedMap<String, String> resMap = new TreeMap<String, String>();
        resMap.put("return_code", "SUCCESS");
        resMap.put("return_msg", "OK");

		response.setCharacterEncoding("UTF-8");
        response.setHeader("ContentType", "text/xml");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        PrintWriter writer = null;
        try {
	        writer = response.getWriter();
	        writer.flush();
	        writer.print(Functions.toXml(resMap));
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
        	writer.close();
        }
	}
	
 
	
  
}