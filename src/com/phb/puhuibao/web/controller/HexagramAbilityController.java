package com.phb.puhuibao.web.controller;

import java.text.DecimalFormat;
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
import com.phb.puhuibao.entity.HexagramAbility;
import com.phb.puhuibao.common.Constants;

@Controller
@RequestMapping(value = "/hexagramAbility")
public class HexagramAbilityController extends BaseController<HexagramAbility, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(HexagramAbilityController.class);
	
	@Resource(name = "hexagramAbilityService")
	public void setBaseService(IBaseService<HexagramAbility, String> baseService) {
		super.setBaseService(baseService);
	}
 
	
	/**
	    获取
	* @param  muid 用户id
	*/
	@RequestMapping(value="getHexagramAbility")
	@ResponseBody
	public Map<String, Object> getHexagramAbility(@RequestParam String muid) {
	Map<String, Object> data = new HashMap<String, Object>();
	try {
		
//		HexagramAbility ha = new HexagramAbility();
//		ha.setmUserId(79);
//		ha.setAbilityMumber(3.0);
//		ha.setFriendsNumber(30.0);
//		ha.setSpecialabilityNumber(4.0);
//		ha.setUserLevel(2.0);
//		ha.setMessioncompliteNumber(3.0);
//		
//		
//		HexagramAbility hh = this.getBaseService().save(ha);
//		
//		HexagramAbility ha1 = new HexagramAbility();
//		ha1.setmUserId(79);
//		ha1.setAbilityMumber(4.0);
//		ha1.setFriendsNumber(50.0);
//		ha1.setSpecialabilityNumber(6.0);
//		ha1.setUserLevel(3.0);
//		ha1.setMessioncompliteNumber(7.0);
//		
//		HexagramAbility hhh = this.getBaseService().update(ha1);
		
		HexagramAbility hexagramAbility = this.getBaseService().getById(muid);
		if(hexagramAbility!=null){
//			DecimalFormat df = new DecimalFormat("#.00");
//			if(hexagramAbility.getAbilityMumber()>=Constants.hexagramAbility_abilitynumber){
//				hexagramAbility.setAbilityMumber(1.0);
//			}else{
//				hexagramAbility.setAbilityMumber(Double.parseDouble(df.format(hexagramAbility.getAbilityMumber()/Constants.hexagramAbility_abilitynumber)));
//			}
//			
//			if(hexagramAbility.getFriendsNumber()>=Constants.hexagramAbility_friendsnumber){
//				hexagramAbility.setFriendsNumber(1.0);
//			}else{
//				hexagramAbility.setFriendsNumber(Double.parseDouble(df.format(hexagramAbility.getFriendsNumber()/Constants.hexagramAbility_friendsnumber)));
//			}	
//			
//			if(hexagramAbility.getMessioncompliteNumber()>=Constants.hexagramAbility_messioncomplitenumber){
//				hexagramAbility.setMessioncompliteNumber(1.0);
//			}else{
//				hexagramAbility.setMessioncompliteNumber(Double.parseDouble(df.format(hexagramAbility.getMessioncompliteNumber()/Constants.hexagramAbility_friendsnumber)));
//			}			
//	
//			if(hexagramAbility.getSpecialabilityNumber()>=Constants.hexagramAbility_specialabilitynumber){
//				hexagramAbility.setSpecialabilityNumber(1.0);
//			}else{
//				hexagramAbility.setSpecialabilityNumber(Double.parseDouble(df.format(hexagramAbility.getSpecialabilityNumber()/Constants.hexagramAbility_specialabilitynumber)));
//			}
			
			data.put("result", hexagramAbility);
		}else{
			data.put("result", "");
		}
		data.put("result", hexagramAbility);
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
