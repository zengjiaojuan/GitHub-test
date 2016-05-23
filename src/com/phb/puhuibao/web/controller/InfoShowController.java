package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.InfoShow;
import com.phb.puhuibao.service.impl.InfoShowServiceImpl;

@Controller
@RequestMapping(value = "/infoShow")
public class InfoShowController extends BaseController<InfoShow, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(InfoShowController.class);
	
	@Resource(name = "infoShowService")
	public void setBaseService(IBaseService<InfoShow, String> baseService) {
		super.setBaseService(baseService);
	}
 
 

	@Resource(name = "infoShowService")
	private InfoShowServiceImpl infoShowService;
	
	
	 
	
	/**
	    获取展示信息
	* @param  muid 用户id
	*/
	@RequestMapping(value="getInfomation")
	@ResponseBody
	public Map<String, Object> getInfomation(@RequestParam int type) {
	Map<String,Object> params = new HashMap<String,Object>();
	Map<String, Object> data = new HashMap<String, Object>();
	params.put("infomationType", type);
	params.put("orderBy", "infohow_id");
	try {
		List<InfoShow> friendlist = this.getBaseService().findList(params);
		data.put("result", friendlist);
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
