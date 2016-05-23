package com.phb.puhuibao.web.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.HexagramCharm;
import com.phb.puhuibao.common.Constants;


@Controller
@RequestMapping(value = "/hexagramCharm")
public class HexagramCharmController extends BaseController<HexagramCharm, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(HexagramCharmController.class);
	
	@Resource(name = "hexagramCharmService")
	public void setBaseService(IBaseService<HexagramCharm, String> baseService) {
		super.setBaseService(baseService);
	}
 
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	    获取
	* @param  muid 用户id
	*/
	@RequestMapping(value="getHexagramCharm")
	@ResponseBody
	public Map<String, Object> getHexagramCharm(@RequestParam String muid) {
	Map<String, Object> data = new HashMap<String, Object>();
	try {
		
		HexagramCharm hexagramCharm = this.getBaseService().getById(muid);
//		DecimalFormat df = new DecimalFormat("#.00");
		
		if(hexagramCharm!=null){
//			
//			if(hexagramCharm.getFansNumber()>=Constants.hexagramCharm_maxfannumber){
//				hexagramCharm.setFansNumber(1.0);
//			}else{
//				hexagramCharm.setFansNumber(Double.parseDouble(df.format(hexagramCharm.getFansNumber()/Constants.hexagramCharm_maxfannumber)));
//			}
//			if(hexagramCharm.getFriendsNumber()>=Constants.hexagramCharm_maxfriendnumber ){
//				hexagramCharm.setFriendsNumber(1.0);
//			}else{
//				hexagramCharm.setFriendsNumber(Double.parseDouble(df.format(hexagramCharm.getFriendsNumber()/Constants.hexagramCharm_maxfriendnumber ) ));
//			}
//			if(hexagramCharm.getPhotoNumber()>Constants.hexagramCharm_maxphotonumber){
//				hexagramCharm.setPhotoNumber(1.0);
//			}else{
//				hexagramCharm.setPhotoNumber(Double.parseDouble(df.format(hexagramCharm.getPhotoNumber()/Constants.hexagramCharm_maxphotonumber)));
//			}			
// 
//			hexagramCharm.setUserLevel((Double.parseDouble(df.format(hexagramCharm.getUserLevel()/Constants.hexagramCredit_userlevel))));
//			
			data.put("result", hexagramCharm);
		}else{
			data.put("result", "");
		}
		
		
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
