package com.phb.puhuibao.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.Advertisement;

@Controller
@RequestMapping(value = "/advertisement")
public class AdvertisementController extends BaseController<Advertisement, String> {
	final Log log = LogFactory.getLog( AdvertisementController.class);
	@Override
	@Resource(name = "advertisementService")
	public void setBaseService(IBaseService<Advertisement, String> baseService) {
		super.setBaseService(baseService);
	}

    /**
     * 轮播图片
     * @return
     */
	@RequestMapping(value="findList")
	@ResponseBody
	public Map<String, Object> findList() {
		Map<String,Object> params = new HashMap<String,Object>();
		Map<String, Object> data;
		try {
			params.put("status", 1);
			params.put("gEndDate", new Date());
			List<Advertisement> result = this.getBaseService().findList(params);
			data = new HashMap<String, Object>();
			data.put("result", result);
			data.put("message", "");
			data.put("status", 1);
		} catch (Exception e) {
			log.error("失败:"+e);
			data = new HashMap<String, Object>();
			data.put("status", 0);
		}
		return data;
	}
	
	@RequestMapping(value="findListForResource")
	@ResponseBody
	public Map<String, Object> findListForResource() {
		Map<String,Object> params = new HashMap<String,Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			params.put("status", 2);
			params.put("gEndDate", new Date());
			List<Advertisement> result = this.getBaseService().findList(params);
			data.put("result", result);
			data.put("message", "");
			data.put("status", 1);
		} catch (Exception e) {
			data = new HashMap<String, Object>();
			data.put("status", 0);
			log.error("失败:"+e);
		}
		return data;
	}
}
