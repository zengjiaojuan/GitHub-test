package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.HexagramCredit;
import com.phb.puhuibao.common.Constants;

@Controller
@RequestMapping(value = "/hexagramCredit")
public class HexagramCreditController extends BaseController<HexagramCredit, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(HexagramCreditController.class);
	
	@Resource(name = "hexagramCreditService")
	public void setBaseService(IBaseService<HexagramCredit, String> baseService) {
		super.setBaseService(baseService);
	}
 
 
 
	
	/**
	    获取
	* @param  muid 用户id
	*/
	@RequestMapping(value="getHexagramCredit")
	@ResponseBody
	public Map<String, Object> getHexagramCredit(@RequestParam String muid) {
	Map<String, Object> data = new HashMap<String, Object>();
	try {
		HexagramCredit hexagramCredit = this.getBaseService().getById(muid);
		if(hexagramCredit!=null){
//			hexagramCredit.setIdAuthentication(hexagramCredit.getIdAuthentication()/Constants.hexagramCredit_authentication);
//			hexagramCredit.setUserLevel(hexagramCredit.getUserLevel()/Constants.hexagramCredit_userlevel);//用户级别最高5
//			hexagramCredit.setResourceComment(hexagramCredit.getResourceComment()/Constants.hexagramCredit_usercomment);//评分最高5
			data.put("result", hexagramCredit);
		}else{
			data.put("result", "");
		}
		data.put("result", hexagramCredit);
		data.put("message", "");
		data.put("status", 1);
		return data;
	
	} catch (Exception e) {
		data.put("message", "网络异常！");
		data.put("status", 0);
		return data;
	}
	
	}
	
 
 
 
}
