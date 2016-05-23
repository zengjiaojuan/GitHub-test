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
import com.phb.puhuibao.entity.HexagramFortune;
import com.phb.puhuibao.common.Constants;

@Controller
@RequestMapping(value = "/hexagramFortune")
public class HexagramFortuneController extends BaseController<HexagramFortune, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(HexagramFortuneController.class);
	
	@Resource(name = "hexagramFortuneService")
	public void setBaseService(IBaseService<HexagramFortune, String> baseService) {
		super.setBaseService(baseService);
	}
 
	
	/**
	    获取
	* @param  muid 用户id
	*/
	@RequestMapping(value="getHexagramFortune")
	@ResponseBody
	public Map<String, Object> getHexagramFortune(@RequestParam String muid) {
	Map<String, Object> data = new HashMap<String, Object>();
	try {
		
  
//		DecimalFormat df = new DecimalFormat("#.00");
		HexagramFortune hexagramFortune = this.getBaseService().getById(muid);
		if(hexagramFortune!=null){
			
//			if(hexagramFortune.getInvestValue()>= Constants.hexagramFortune_investvalue){
//				hexagramFortune.setInvestValue(1.0);
//			}else{
//				hexagramFortune.setInvestValue(Double.parseDouble(df.format(hexagramFortune.getInvestValue()/Constants.hexagramFortune_investvalue)));
//			}
//			
//			if(hexagramFortune.getDepositValue()>= Constants.hexagramFortune_depositvalue){
//				hexagramFortune.setDepositValue(1.0);
//			}else{
//				hexagramFortune.setDepositValue(Double.parseDouble(df.format(hexagramFortune.getDepositValue()/Constants.hexagramFortune_depositvalue)));
//			}
//			
//			if(hexagramFortune.getMessionPublish()>= Constants.hexagramFortune_messionpublish){
//				hexagramFortune.setMessionPublish(1.0);
//			}else{
//				hexagramFortune.setMessionPublish(Double.parseDouble(df.format(hexagramFortune.getMessionPublish()/Constants.hexagramFortune_messionpublish)));
//			}
						
			data.put("result", hexagramFortune);
		}else{
			data.put("result", "");
		}
		data.put("result", hexagramFortune);
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
