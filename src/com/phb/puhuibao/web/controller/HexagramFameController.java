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
import com.phb.puhuibao.entity.HexagramFame;
import com.phb.puhuibao.common.Constants;

@Controller
@RequestMapping(value = "/hexagramFame")
public class HexagramFameController extends BaseController<HexagramFame, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(HexagramFameController.class);
	
	@Resource(name = "hexagramFameService")
	public void setBaseService(IBaseService<HexagramFame, String> baseService) {
		super.setBaseService(baseService);
	}
 
	
	/**
	    获取 名望六芒星   
	* @param  muid 用户id
	*/
	@RequestMapping(value="getHexagramFame")
	@ResponseBody
	public Map<String, Object> getHexagramFame(@RequestParam String muid) {
	Map<String, Object> data = new HashMap<String, Object>();
	try {
 
		HexagramFame hexagramFame = this.getBaseService().getById(muid);
//		DecimalFormat df = new DecimalFormat("#.00");
		if(hexagramFame!=null){
//			  if(hexagramFame.getMultipersonMession()>=Constants.hexagramFame_multipersonmession){
//				   hexagramFame.setMultipersonMession(1.0);
//				}else{
//				    hexagramFame.setMultipersonMession(Double.parseDouble(df.format(hexagramFame.getMultipersonMession()/Constants.hexagramFame_multipersonmession)));
//				}
//				hexagramFame.setEvaluateScore(hexagramFame.getEvaluateScore()/Constants.hexagramFame_evaluatescore);
//			     
//				if(hexagramFame.getShareTimes()>=Constants.hexagramFame_sharetimes){
//			    	 hexagramFame.setShareTimes(1.0);
//			     }else{
//			    	 hexagramFame.setShareTimes(Double.parseDouble(df.format(hexagramFame.getShareTimes()/Constants.hexagramFame_sharetimes)));
//			     }
//		
//				if( hexagramFame.getValidMessions()>= Constants.hexagramFame_validmessions){
//					hexagramFame.setValidMessions(1.0);
//				}else{
//					hexagramFame.setValidMessions(Double.parseDouble(df.format(hexagramFame.getValidMessions()/Constants.hexagramFame_validmessions)));
//				}
//			    hexagramFame.setUserLevel(hexagramFame.getUserLevel()/Constants.hexagramCredit_userlevel);
			   data.put("result", hexagramFame);
			}else{
				data.put("result", "");
			}
			data.put("result", hexagramFame);
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
