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
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.ImpressionList;
import com.phb.puhuibao.service.impl.ImpressionListServiceImpl;

@Controller
@RequestMapping(value = "/impressionList")
public class ImpressionListController extends BaseController<ImpressionList, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(ImpressionListController.class);
	
	@Resource(name = "impressionListService")
	public void setBaseService(IBaseService<ImpressionList, String> baseService) {
		super.setBaseService(baseService);
	}
 
 

	@Resource(name = "impressionListService")
	private ImpressionListServiceImpl impressionListService;
	
	
	/**
                     获取系统自定义印象
	 * @param  
	 */

 
	
	@RequestMapping(value="getImpressionList")
	@ResponseBody
	public Map<String, Object> getImpressionList() {
		Map<String,Object> params = new HashMap<String,Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<ImpressionList> impression = this.getBaseService().findList(params);
			data.put("result", impression);
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
