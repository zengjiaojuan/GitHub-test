package com.phb.puhuibao.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.phb.puhuibao.entity.InvestmentAlert;
import com.phb.puhuibao.service.impl.InvestmentAlertServiceImpl;

@Controller
@RequestMapping(value = "/investmentAlert")
public class InvestmentAlertController extends BaseController<InvestmentAlert, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(InvestmentAlertController.class);
	
	@Resource(name = "investmentAlertService")
	public void setBaseService(IBaseService<InvestmentAlert, String> baseService) {
		super.setBaseService(baseService);
	}
 
 

	@Resource(name = "investmentAlertService")
	private InvestmentAlertServiceImpl investmentAlertService;
	
	
	/**
      2015/9/6 17:17:08
	 * @param alertDeviceid 用户在umeng的id
	 * @param productSN 项目的编码
	 * @param itemtime 项目开标时间
	 * @param devicetype android:1 ios:0 
	 */

	@RequestMapping(value="savealert")
	@ResponseBody
	public Map<String, Object> savealert(@RequestParam String productSN,@RequestParam String alertDeviceid,@RequestParam String itemtime,@RequestParam String devicetype) {
		InvestmentAlert entity = new InvestmentAlert();
		entity.setAlertDeviceid(alertDeviceid);
		entity.setAlertTitle("开标提醒");
		entity.setAlertStatus(1);
		entity.setAlertProduct(productSN);
		entity.setAlertDevicetype(Integer.parseInt(devicetype));
		entity.setAlertContent("您关注的["+productSN+"]还有三分钟开标啦!");
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		try {
			String yy=sdf.format(new Date(Long.parseLong(itemtime) - 3 * 60 * 1000));
			entity.setPushTime((Date) sdf.parse(yy));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			this.getBaseService().save(entity);
		} catch (Exception e) {
			 
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}
		data.put("result", entity);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="deletealert")
	@ResponseBody
	public Map<String, Object> deletealert(@RequestParam String productSN,@RequestParam String alertDeviceid) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("alertProduct", productSN);
		params.put("alertDeviceid", alertDeviceid);
		int count = this.getBaseService().delete(params);
		Map<String, Object> data = new HashMap<String, Object>();
		if (count == 0) {
			data.put("message", "删除失败！");
			data.put("status", 0);
		} else {
			data.put("message", "删除成功！");			
			data.put("status", 1);
		}
		return data;
	}
	
 
 
}
