package com.phb.puhuibao.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.idap.web.common.controller.Commons;
//import com.alibaba.fastjson.TypeReference;
import com.idp.pub.context.AppContext;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.llpay.client.config.PartnerConfig;
import com.llpay.client.config.ServerURLConfig;
import com.llpay.client.conn.HttpRequestSimple;
import com.llpay.client.utils.LLPayUtil;
import com.opensymphony.oscache.util.StringUtil;
import com.phb.puhuibao.entity.MobileUser;
//import com.phb.puhuibao.entity.ThirdPayLog;
import com.phb.puhuibao.entity.UserCard;
import com.yeepay.TZTService;

@Controller
@RequestMapping(value = "/userCard")
public class UserCardController extends BaseController<UserCard, String> {
	final Log log = LogFactory.getLog(UserCardController.class);
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
	
	@Resource(name = "commons")
	private Commons commons;
	
	

	 
 
 
	
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
		Map<String, Object> data;
		try {
			Map<String, String> result = TZTService.confirmBindBankcard(params);
			//		String merchantaccount				= StringUtils.trimToEmpty(result.get("merchantaccount")); 
			//	    String requestidFromYeepay 	   		= StringUtils.trimToEmpty(result.get("requestid")); 
			//	    String bankcode 			   		= StringUtils.trimToEmpty(result.get("bankcode")); 
			//	    String card_top 			   		= StringUtils.trimToEmpty(result.get("card_top")); 
			//	    String card_last 			   		= StringUtils.trimToEmpty(result.get("card_last")); 
			//	    String signFromYeepay 		   		= StringUtils.trimToEmpty(result.get("sign")); 
			String error_code = StringUtils.trimToEmpty(result.get("error_code"));
			String error_msg = StringUtils.trimToEmpty(result.get("error_msg"));
			String customError = StringUtils.trimToEmpty(result.get("customError"));
			data = new HashMap<String, Object>();
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
		} catch (Exception e) {

            data = new HashMap<String, Object>();
			data.put("status", 0);
			log.error("失败:"+e);
		}
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

        
		Map<String, Object> data = new HashMap<String, Object>();
		try {
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
	        //String prcptcd = "";
	        if (card != null) {
	        	cardno = card.getBankAccount();
	        	//prcptcd = card.getPrcptcd();
	        }
			org.json.JSONObject object = new org.json.JSONObject(resJSON);
			String ret_code = object.getString("ret_code");
			data.put("message", object.getString("ret_msg"));
			if ("0000".endsWith(ret_code)) {
				JSONArray o = new JSONArray(object.getString("agreement_list"));
				String bank_name = o.getJSONObject(0).getString("bank_name"); // 单卡
				String bank_code = o.getJSONObject(0).getString("bank_code"); // 银行编码
				//String cardno = o.getJSONObject(0).getString("card_no"); //  连连返回的卡号只有末尾4个数字  不能用
				
				
				if( StringUtil.isEmpty(cardno) ){// 出错了 
					data.put("message", "用户卡号表卡号为空.");
					data.put("status", 0);
					return data;
				}
				
				List<Map<String, String>> result = new ArrayList<Map<String, String>>();
				Map<String, String> map = new HashMap<String, String>();
				map.put("bank_name", bank_name);
				map.put("card_no", cardno);
				map.put("bank_code", bank_code);
				map.put("icon_id", "06523cadab1a4ab798fbd715"+bank_code);

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
			 
				
				
				MobileUser user = mobileUserService.getById(muid);
				map.put("userName", user.getmUserName());// 姓名
				map.put("userIdNo", user.getIdNumber()); // 身份证
				
				result.add(map);
				data.put("result", result);
				data.put("status", 1);
			} else if ("8901".endsWith(ret_code)) { // 没有绑定卡
				data.put("message", "没有绑卡");
				data.put("status", 1);
			} else if ("3007".endsWith(ret_code)) { // [user_id]查询不存在
				data.put("message", "LLPAY没有此用户绑卡信息");
				data.put("status", 1);
			} else {
				data.put("status", 1);
			}
		} catch (JSONException e) {
			log.error("失败"+e);
			data.put("message", e.getMessage());
			data.put("status", 0);
		}
		return data;
	}

	/**
	 * 判断银行卡bin判断是否有效  判断用户是否已经绑卡
	 * @param card_no
	 * @return
	 */
	@RequestMapping(value="validBankCard")
	@ResponseBody
	public Map<String, Object> validBankCard(@RequestParam String cardno,@RequestParam String idnamber) {
 
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idNumber", idnamber);
		List<MobileUser> userlist = mobileUserService.findList(params);
		if(userlist.size()>0){// 用户已经绑卡  同一个身份证绑一张卡
			data.put("message","此身份证已经绑定了银行卡");
			data.put("status", 0);
			return data;
		}
		
        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", PartnerConfig.OID_PARTNER);
        reqObj.put("card_no", cardno);
        reqObj.put("sign_type", PartnerConfig.SIGN_TYPE);
        String sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(ServerURLConfig.QUERY_BANKCARD_URL, reqJSON);
        
		
		try {
			org.json.JSONObject object = new org.json.JSONObject(resJSON);
			String ret_code = object.getString("ret_code");
			if ("0000".endsWith(ret_code)) {
				String bank_name = object.getString("bank_name");
				String card_type = object.getString("card_type");//card_type:2 储蓄卡 3:信用卡
				if(StringUtil.isEmpty(card_type) || "3".equals(card_type) ){
					data.put("message", "本公司不支持信用卡支付！");
					data.put("status", 0);
					return data;
				}
				 
				
				
				String sql = "select count(1) from phb_bank where position(bank_name in '" + bank_name + "')>0";
				List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
				long count = (long) result.get(0).get("count(1)");
				if (count == 1) {
					data.put("message", "");
					data.put("status", 1);
					
					 String ipandport = commons.getAddrServerIp(); 
					 String projectname = commons.getProjectName();//lcb
				     StringBuffer url = new StringBuffer(); // "http://ip:7001/lcb/userAccount/withdrawNotify.shtml"
				     url.append("http://").append(ipandport).append("/").append(projectname).append("/userAccount/notify.shtml");
				     data.put("chargeCallBack", url.toString());
					
					
				} else {
					data.put("message", "该卡本公司不支持电子支付！");
					data.put("status", 0);
				}
			} else {
				data.put("message", object.getString("ret_msg"));
				data.put("status", 0);
			}
		} catch (JSONException e) {
			log.error("失败:"+e);
			data.put("message", e.getMessage());
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
		Map<String, Object> data = new HashMap<String, Object>();
		try {
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
			
			org.json.JSONObject object = new org.json.JSONObject(resJSON);
			String ret_code = object.getString("ret_code");
			data.put("message", object.getString("ret_msg"));
			if ("0000".endsWith(ret_code)) {
				String result = object.getString("card_list");
				data.put("result", result);
			}
			data.put("status", 1);
		} catch (JSONException e) {
			log.error("失败:"+e);
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