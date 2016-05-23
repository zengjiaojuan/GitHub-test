package com.phb.puhuibao.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phb.puhuibao.entity.ExperienceInvestment;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.UserInvestment;

@Controller
@RequestMapping(value = "/sendSMS")
public class SendSMSController {
	private final Log log = LogFactory.getLog(getClass());
	private String preURL;
	private String sign;
	
	public String getPreURL() {
		return preURL;
	}

	public void setPreURL(String preURL) {
		this.preURL = preURL;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@RequestMapping(value="sendValidCode")
	@ResponseBody
	public Map<String, Object> sendValidCode(@RequestParam String mobile) {
		Map<String, Object> data = new HashMap<String, Object>();
		String validCode = createRandom(true, 6);
		String content = "您的短信验证码为：" + validCode + "。切勿告知他人，请在页面中输入以完成验证。";

		if (send(content, mobile)) {
			data.put("result", validCode);
			data.put("message", "Success");
			data.put("status", 1);
		} else {
			data.put("result", "");
			data.put("message", "短信发送失败！可能没有额度了！");
			data.put("status", 0);				
		}
		return data;
	}

	private String createRandom(boolean numberFlag, int length) {
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);
		return retStr;
	}

	private boolean send(String content, String mobile) {
		StringBuffer sb = new StringBuffer();
		try {
			String send_content = URLEncoder.encode(sign + content, "UTF-8");
			URL url = new URL(preURL + "&mobile=" + mobile + "&content=" + send_content);	
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			BufferedReader br = new  BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));		     
			String line;
		    while ((line=br.readLine()) != null) {
		    	//追加字符串获得XML形式的字符串
		    	sb.append(line+"");
		    	//System.out.println("提取数据 :  "+line);
		    }
		    br.close();		    
		} catch (IOException e) {
		} finally {
		}
		return sb.indexOf("Success") > 0;
	}

	public void sendMonthIncome(UserInvestment i, String mobile) {
		String content = "收到月息" + i.getLastIncome() + "元，编号：" + i.getBidSN() + "，交易号：" + i.getInvestmentId();
		
		if (!send(content, mobile)) {
			log.error(content);
		}
	}

	public void sendMonthProcessLast(UserInvestment i, String mobile) {
		String content = "收到本金" + i.getInvestmentAmount() + "元，编号：" + i.getBidSN() + "，交易号：" + i.getInvestmentId();
		
		if (!send(content, mobile)) {
			log.error(content);
		}
	}

	public void sendIncome(UserInvestment i, String mobile) {
		BigDecimal amount = new BigDecimal(i.getInvestmentAmount() + i.getLastIncome()).setScale(2, RoundingMode.HALF_UP);
		String content = "收到本金利息" + amount.toString() + "元，编号：" + i.getBidSN() + "，交易号：" + i.getInvestmentId();
		
		if (!send(content, mobile)) {
			log.error(content);
		}
	}

	public void sendItemIncome(ItemInvestment i, String mobile) {
		BigDecimal amount = new BigDecimal(i.getInvestmentAmount() + i.getLastIncome()).setScale(2, RoundingMode.HALF_UP);
		String content = "收到本金利息" + amount.toString() + "元，编号：" + i.getItemSN() + "，交易号：" + i.getInvestmentId();
		
		if (!send(content, mobile)) {
			log.error(content);
		}
	}

	public void sendExperienceIncome(ExperienceInvestment i, String mobile) {
		String content = "收到体验利息" + i.getLastIncome() + "元，编号：" + i.getProductSN() + "，交易号：" + i.getInvestmentId();
		
		if (!send(content, mobile)) {
			log.error(content);
		}
	}
}
