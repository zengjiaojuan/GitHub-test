package com.phb.puhuibao.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.TypeReference;
import com.idp.pub.context.AppContext;
import com.idp.pub.service.IBaseService;
import com.idp.pub.utils.DESUtils;
//import com.idp.pub.utils.JsonUtils;
import com.idp.pub.utils.RSAUtils;
import com.idp.pub.web.controller.BaseController;
import com.llpay.client.config.PartnerConfig;
import com.llpay.client.config.ServerURLConfig;
import com.llpay.client.conn.HttpRequestSimple;
import com.llpay.client.utils.LLPayUtil;
import com.llpay.client.vo.RetBean;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.MobileUser;
//import com.phb.puhuibao.entity.ThirdPayLog;
import com.phb.puhuibao.entity.UserCard;
import com.yeepay.TZTService;

@Controller
@RequestMapping(value = "/userCard")
public class UserCardController extends BaseController<UserCard, String> {
	@Override
	@Resource(name = "userCardService")
	public void setBaseService(IBaseService<UserCard, String> baseService) {
		super.setBaseService(baseService);
	}

//	@Resource(name = "thirdPayLogService")
//	private IBaseService<ThirdPayLog, String> thirdPayLogService;

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "userCardService")
	private IBaseService<UserCard, String> baseUserCardService;

	@Resource(name = "appContext")
	private AppContext appContext;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 银行卡清单
	 * @param muid
	 * @return
	 */
//	@RequestMapping(value="listForAndroid")
//	@ResponseBody
//	protected Map<String, Object> listForAndroid(@RequestParam String muid) {
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("mUserId", muid);
//		map.put("orderBy", "create_time");
//		map.put("order", "desc");
//		List<UserCard> result = this.getBaseService().findList(map);
//		for (UserCard uc : result) {
//			uc.setBankAccount(RSAUtils.encrypt(uc.getBankAccount()));
//			uc.setBankName(RSAUtils.encrypt(uc.getBankName()));
//		}
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("result", result);
//		data.put("message", "");
//		data.put("status", 1);
//		return data;
//	}
//	@RequestMapping(value="listForIOS")
//	@ResponseBody
//	protected Map<String, Object> listForIOS(@RequestParam String muid) {
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("mUserId", muid);
//		map.put("orderBy", "create_time");
//		map.put("order", "desc");
//		List<UserCard> result = this.getBaseService().findList(map);
//		for (UserCard uc : result) {
//			uc.setBankAccount(DESUtils.encrypt(uc.getBankAccount()));
//			uc.setBankName(DESUtils.encrypt(uc.getBankName()));
//		}
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("result", result);
//		data.put("message", "");
//		data.put("status", 1);
//		return data;
//	}

	/**
	 * 银行卡列表
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="list")
	@ResponseBody
	protected Map<String, Object> list(@RequestParam String muid) {
		MobileUser user = mobileUserService.getById(muid);
		Map<String, String> result		= TZTService.queryAuthbindList(user.getmUserTel(), "4");
//		String merchantaccount			= StringUtils.trimToEmpty(result.get("merchantaccount"));
//		String identityidFromYeepay		= StringUtils.trimToEmpty(result.get("identityid"));
//		String identitytypeFromYeeapy	= StringUtils.trimToEmpty(result.get("identitytype"));
		String cardlist					= StringUtils.trimToEmpty(result.get("cardlist"));
//		String sign						= StringUtils.trimToEmpty(result.get("sign"));
		String error_code				= StringUtils.trimToEmpty(result.get("error_code"));
		String error_msg				= StringUtils.trimToEmpty(result.get("error_msg"));
		String customError				= StringUtils.trimToEmpty(result.get("customError"));

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
		
		//Map<String, String> map = JSON.parseObject(JSON.toJSONString(JSON.parseArray(cardlist).get(0)), new TypeReference<Map<String, String>>() {});
		//Map<String, String> map = (Map<String, String>) JSON.parseArray(cardlist).get(0);
		//Map<String, Object> m = (Map<String, Object>) JsonUtils.toMap(JSON.toJSONString(JSON.parseArray(cardlist).get(0)));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Object object : JSON.parseArray(cardlist)) {
			Map<String, Object> card = new HashMap<String, Object>();
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) object;
			card.put("card_top", map.get("card_top"));
			card.put("card_last", map.get("card_last"));
			card.put("card_name", map.get("card_name"));
			list.add(card);
		}
		
		data.put("result", list);
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}
	
	/**
	 * 保存
	 * @param muid
	 * @param bankName
	 * @param bankAccount
	 * @return
	 */
	@RequestMapping(value="saveForAndroid")
	@ResponseBody
	public Map<String, Object> saveForAndroid(@RequestParam String muid, @RequestParam String bankAccount, @RequestParam String phone) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		MobileUser user = mobileUserService.getById(muid);
		String errorInfo = Functions.idCardValidate(user.getIdNumber());
		if (StringUtils.isNotEmpty(errorInfo)) {
			data.put("message", errorInfo);
			data.put("status", 0);
			return data;
		}

		bankAccount = RSAUtils.decrypt(bankAccount);
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("mUserId", muid);
//		params.put("bankAccount", bankAccount);
//		UserCard entity = this.getBaseService().unique(params);
//		if (entity != null) {
//			data.put("message", "该银行卡已绑定！");
//			data.put("status", 0);			
//			return data;
//		}

		return bindBankCard(muid, bankAccount, phone);
//		if ((Integer) data.get("status") == 1) {
//			entity = new UserCard();
//			entity.setmUserId(muid);
//			entity.setBankName(RSAUtils.decrypt(bankName));
//			entity.setBankAccount(bankAccount);
//			try {
//			    entity = this.getBaseService().save(entity);
//			} catch (Exception e) {
//				e.printStackTrace();
//				data.put("message", "网络异常！");
//				data.put("status", 0);			
//				return data;
//			}
//		}
	}

	@RequestMapping(value="saveForIOS")
	@ResponseBody
	public Map<String, Object> saveForIOS(@RequestParam String muid, @RequestParam String bankAccount, @RequestParam String phone) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		MobileUser user = mobileUserService.getById(muid);
		String errorInfo = Functions.idCardValidate(user.getIdNumber());
		if (StringUtils.isNotEmpty(errorInfo)) {
			data.put("message", errorInfo);
			data.put("status", 0);
			return data;
		}

		bankAccount = DESUtils.decrypt(bankAccount);
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("mUserId", muid);
//		params.put("bankAccount", bankAccount);
//		UserCard entity = this.getBaseService().unique(params);
//		if (entity != null) {
//			data.put("message", "该银行卡已绑定！");
//			data.put("status", 0);			
//			return data;
//		}
//
		return bindBankCard(muid, bankAccount, phone);
//		if ((Integer) data.get("status") == 1) {
//			entity = new UserCard();
//			entity.setmUserId(muid);
//			entity.setBankName(DESUtils.decrypt(bankName));
//			entity.setBankAccount(bankAccount);
//			try {
//			    entity = this.getBaseService().save(entity);
//			} catch (Exception e) {
//				e.printStackTrace();
//				data.put("message", "网络异常！");
//				data.put("status", 0);			
//				return data;
//			}
//		}
	}
	
	public Map<String, Object> authenticate(int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser user = mobileUserService.getById("" + muid);
		if (StringUtils.isEmpty(user.getmUserName()) || StringUtils.isEmpty(user.getIdNumber())) {
			data.put("message", "请先实名认证！");
			data.put("status", 0);
			return data;
		}
//		if (user.getIsAudit() != 1) {
//			data.put("message", "请先身份证拍照认证！");
//			data.put("status", 0);
//			return data;
//		}
//		if (StringUtils.isEmpty(user.getmUserEmail())) {
//			data.put("message", "请先绑定邮箱！");
//			data.put("status", 0);
//			return data;
//		}
		return null;
	}
		
	@RequestMapping(value="delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam int cardId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("cardId", cardId);
		int count = this.getBaseService().delete(params);
		if (count == 0) {
			data.put("message", "删除失败！");
			data.put("status", 0);
		} else {
			data.put("message", "删除成功！");
			data.put("status", 1);
		}
		return data;
	}

	/**
	 * 绑定测试用
	 * @param muid
	 * @param cardno
	 * @return
	 */
	@RequestMapping(value="bindBankCard")
	@ResponseBody
	public Map<String, Object> bindBankCard(@RequestParam String muid, @RequestParam String cardno, @RequestParam String phone) {
		MobileUser user = mobileUserService.getById(muid);
		if ("".equals(phone)) {
			phone = user.getmUserTel();
		}
				
		Map<String, String> params 	= new HashMap<String, String>();
		params.put("identityid", 		user.getmUserTel());
		params.put("cardno", 			cardno); // 银行卡号
		//String jsonStr = JSON.toJSONString(params);
		String uuid = UUID.randomUUID().toString().replace("-", "");
		params.put("requestid", 		uuid);
		params.put("identitytype", 		"4"); // 用户手机号
		params.put("username", 			user.getmUserName()); // 持卡人姓名 
		params.put("phone", 			phone); // 银行预留手机号
		params.put("idcardtype", 		"01");
		params.put("idcardno", 			user.getIdNumber()); // 证件号
		params.put("userip", 			appContext.getUserIP());
//		params.put("registerphone", 	"");
//		params.put("registerdate",	 	"");
//		params.put("registerip", 		"");
//		params.put("registeridcardno", 	"");
//		params.put("registercontact", 	"");
//		params.put("os", 				"");
//		params.put("imei", 				"");
//		params.put("ua", 				"");
//		params.put("registeridcardtype", "");

		Map<String, String> result			= TZTService.bindBankcard(params);
//		String merchantaccount				= StringUtils.trimToEmpty(result.get("merchantaccount"));
	    String requestidFromYeepay 	   		= StringUtils.trimToEmpty(result.get("requestid"));
//	    String codesender 			   		= StringUtils.trimToEmpty(result.get("codesender"));
//	    String signFromYeepay 		   		= StringUtils.trimToEmpty(result.get("sign"));
	    String error_code	   				= StringUtils.trimToEmpty(result.get("error_code"));
	    String error_msg	   				= StringUtils.trimToEmpty(result.get("error_msg"));
	    String customError	   				= StringUtils.trimToEmpty(result.get("customError"));

//		ThirdPayLog log = new ThirdPayLog();
//		log.setLogId(uuid);
//		log.setAction("bindBankcard");
//		log.setParams(jsonStr + JSON.toJSONString(result));
//		int status = 0;
//		if("".equals(error_code) && "".equals(customError)) {
//			status = 1;
//		}
//		log.setStatus(status);
//		thirdPayLogService.save(log);

		Map<String, Object> data = new HashMap<String, Object>();
		if ("600301".equals(error_code)) {
			data.put("message", "您姓名或身份证号与银行卡不匹配。如果实名认证有误，请联系客服修改。");
			data.put("status", 0);
			return data;		
		}
		if (!"".equals(error_code)) {
			data.put("message", error_code + ": " + error_msg);
			data.put("status", 0);
			return data;		
		} else if (!"".equals(customError)) {
			data.put("message", customError);
			data.put("status", 0);
			return data;		
		}

		UserCard card = baseUserCardService.getById(cardno);
		if (card == null) {
			card = new UserCard();
			card.setBankAccount(cardno);
			card.setmUserId(user.getmUserId());
			card.setBankPhone(phone);
			baseUserCardService.save(card);
		} else {
			card = new UserCard();
			card.setBankAccount(cardno);
			card.setmUserId(user.getmUserId());
			card.setBankPhone(phone);
			baseUserCardService.update(card);
		}

		data.put("result", requestidFromYeepay);
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}
	
	/**
	 * 绑定确认
	 * @param requestid
	 * @param captcha
	 * @return
	 */
	@RequestMapping(value="bindBankCardConfirm")
	@ResponseBody
	public Map<String, Object> bindBankCardConfirm(@RequestParam String requestid, @RequestParam String captcha) {
		Map<String, String> params 	= new HashMap<String, String>();
		params.put("requestid", 		requestid);
		params.put("validatecode", 		captcha);
		Map<String, String> result			= TZTService.confirmBindBankcard(params);
//		String merchantaccount				= StringUtils.trimToEmpty(result.get("merchantaccount")); 
//	    String requestidFromYeepay 	   		= StringUtils.trimToEmpty(result.get("requestid")); 
//	    String bankcode 			   		= StringUtils.trimToEmpty(result.get("bankcode")); 
//	    String card_top 			   		= StringUtils.trimToEmpty(result.get("card_top")); 
//	    String card_last 			   		= StringUtils.trimToEmpty(result.get("card_last")); 
//	    String signFromYeepay 		   		= StringUtils.trimToEmpty(result.get("sign")); 
	    String error_code	   				= StringUtils.trimToEmpty(result.get("error_code")); 
	    String error_msg	   				= StringUtils.trimToEmpty(result.get("error_msg")); 
	    String customError	   				= StringUtils.trimToEmpty(result.get("customError")); 

		Map<String, Object> data = new HashMap<String, Object>();
		if (!"".equals(error_code)) {
			data.put("message", error_code + ": " + error_msg);
			data.put("status", 0);
			return data;		
		} else if (!"".equals(customError)) {
			data.put("message", customError);
			data.put("status", 0);
			return data;		
		}
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}

	/**
	 * 银行卡列表
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="getBankCards")
	@ResponseBody
	public Map<String, Object> getBankCards(@RequestParam String muid) {
        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", PartnerConfig.OID_PARTNER);
        reqObj.put("user_id", muid);
        reqObj.put("offset", "0");
        reqObj.put("sign_type", PartnerConfig.SIGN_TYPE);
        reqObj.put("pay_type", "D");
        String sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(ServerURLConfig.QUERY_USER_BANKCARD_URL, reqJSON);
        
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		UserCard card = this.getBaseService().unique(params);
        String cardno = "";
        String prcptcd = "";
        if (card != null) {
        	cardno = card.getBankAccount();
        	prcptcd = card.getPrcptcd();
        }
        
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			org.json.JSONObject object = new org.json.JSONObject(resJSON);
			String ret_code = object.getString("ret_code");
			data.put("message", object.getString("ret_msg"));
			if ("0000".endsWith(ret_code)) {
				JSONArray o = new JSONArray(object.getString("agreement_list"));
				String bank_name = o.getJSONObject(0).getString("bank_name"); // 单卡
				String bank_code = o.getJSONObject(0).getString("bank_code"); // 单卡
				List<Map<String, String>> result = new ArrayList<Map<String, String>>();
				Map<String, String> map = new HashMap<String, String>();
				map.put("bank_name", bank_name);
				map.put("card_no", cardno);
				map.put("bank_code", bank_code);
				map.put("icon_id", bank_code);

//中国银行   01040000
//农业银行   01030000
//工商银行   01020000
//建设银行   01050000
//招商银行   03080000
//中信银行   03020000
//华夏银行   03040000
//民生银行   03050000
//广发银行   03060000
//平安银行   03070000
//兴业银行   03090000
//交通银行   03010000
//邮政储蓄银行   01000000
//光大银行     03030000
				map.put("prcptcd", prcptcd);
				result.add(map);
				data.put("result", result);
				data.put("status", 1);
			} else if ("8901".endsWith(ret_code)) { // 没有绑定卡
				data.put("status", 1);
			} else if ("3007".endsWith(ret_code)) { // [user_id]查询不存在
				data.put("status", 1);
			} else {
				data.put("status", 0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			data.put("message", e.getMessage());
			data.put("status", 0);
		}
		return data;
	}

	/**
	 * 判断银行卡bin判断是否有效
	 * @param card_no
	 * @return
	 */
	@RequestMapping(value="validBankCard")
	@ResponseBody
	public Map<String, Object> validBankCard(@RequestParam String cardno) {
        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", PartnerConfig.OID_PARTNER);
        reqObj.put("card_no", cardno);
        reqObj.put("sign_type", PartnerConfig.SIGN_TYPE);
        String sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(ServerURLConfig.QUERY_BANKCARD_URL, reqJSON);
        
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			org.json.JSONObject object = new org.json.JSONObject(resJSON);
			String ret_code = object.getString("ret_code");
			if ("0000".endsWith(ret_code)) {
				String bank_name = object.getString("bank_name");
				String sql = "select count(1) from phb_bank where position(bank_name in '" + bank_name + "')>0";
				List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
				long count = (long) result.get(0).get("count(1)");
				if (count == 1) {
					data.put("message", "");
					data.put("status", 1);
				} else {
					data.put("message", "该卡本公司不支持电子支付！");
					data.put("status", 0);
				}
			} else {
				data.put("message", object.getString("ret_msg"));
				data.put("status", 0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			data.put("message", e.getMessage());
			data.put("status", 0);
		}
		return data;
	}
	
	@RequestMapping(value="unbind")
	@ResponseBody
	public Map<String, Object> unbind(@RequestParam String muid) {
        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", PartnerConfig.OID_PARTNER);
        reqObj.put("user_id", muid);
        reqObj.put("offset", "0");
        reqObj.put("sign_type", PartnerConfig.SIGN_TYPE);
        reqObj.put("pay_type", "D");
        String sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(ServerURLConfig.QUERY_USER_BANKCARD_URL, reqJSON);
        String no_agree = null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			org.json.JSONObject object = new org.json.JSONObject(resJSON);
			String ret_code = object.getString("ret_code");
			if ("0000".endsWith(ret_code)) {
				JSONArray o = new JSONArray(object.getString("agreement_list"));
				no_agree = o.getJSONObject(0).getString("no_agree"); // 单卡
			} else {
				data.put("message", object.getString("ret_msg"));
				data.put("status", 1);
				return data;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			data.put("message", e.getMessage());
			data.put("status", 0);
			return data;
		}
		
//		JSONObject reqObj = new JSONObject();
//        reqObj.put("oid_partner", PartnerConfig.OID_PARTNER);
//        reqObj.put("user_id", muid);
//        reqObj.put("sign_type", PartnerConfig.SIGN_TYPE);
//        reqObj.put("pay_type", "D");
        reqObj.put("no_agree", no_agree);
        sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY);
        reqObj.put("sign", sign);
        reqJSON = reqObj.toString();
        resJSON = HttpRequestSimple.getInstance().postSendHttp(ServerURLConfig.UNBIND_BANKCARD_URL, reqJSON);
        RetBean retBean = JSON.parseObject(resJSON, RetBean.class);
		data.put("message", retBean.getRet_msg());
		if ("0000".endsWith(retBean.getRet_code())) {
			data.put("status", 1);
		} else {
			data.put("status", 0);
		}
		return data;
	}

	/**
	 * 支行列表
	 * @param muid
	 * @param cityCode
	 */
	@RequestMapping(value="getBranchBanks")
	@ResponseBody
	public Map<String, Object> getBranchBanks(@RequestParam String muid, @RequestParam String cityCode, @RequestParam String branchBankName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		UserCard card = baseUserCardService.unique(params);

        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", PartnerConfig.OID_PARTNER);
        reqObj.put("card_no", card.getBankAccount());
        if ("".equals(branchBankName)) {
            reqObj.put("brabank_name", "银行");
        } else {
            reqObj.put("brabank_name", branchBankName);
        }
        reqObj.put("city_code", cityCode);
        reqObj.put("sign_type", PartnerConfig.SIGN_TYPE);
        String sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(ServerURLConfig.QUERY_CNAPS_URL, reqJSON);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			org.json.JSONObject object = new org.json.JSONObject(resJSON);
			String ret_code = object.getString("ret_code");
			data.put("message", object.getString("ret_msg"));
			if ("0000".endsWith(ret_code)) {
				String result = object.getString("card_list");
				data.put("result", result);
			}
			data.put("status", 1);
		} catch (JSONException e) {
			e.printStackTrace();
			data.put("message", e.getMessage());
			data.put("status", 0);
		}
		return data;
	}
	
	/**
	 * 省列表
	 * @return
	 */
	@RequestMapping(value="getProvinces")
	@ResponseBody
	public Map<String, Object> getProvinces() {
		String sql = "select city as province,code from phb_provice_city where parent_code=''";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}
	
	/**
	 * 市列表
	 * @param parentCode
	 * @return
	 */
	@RequestMapping(value="getCities")
	@ResponseBody
	public Map<String, Object> getCities(@RequestParam String parentCode) {
		String sql = "select city,code from phb_provice_city where parent_code='" + parentCode + "'";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}
}