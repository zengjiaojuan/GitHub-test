package com.idap.web.clinic.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idap.clinic.entity.EveryPic;
import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;

@Controller
@RequestMapping(value = "/everyPic")
public class EveryPicController extends BaseController<EveryPic, String> {
	@Resource(name = "everyPicService")
	public void setBaseService(IBaseService<EveryPic, String> baseService) {
		super.setBaseService(baseService);
	}
		
	@RequestMapping(value="queryEveryPic")
	@ResponseBody
	public Map<String, Object> queryEveryPic(@RequestParam int pageno){
		Pager<EveryPic> pEveryPic = new Pager<EveryPic>();
		pEveryPic.setReload(true);
		pEveryPic.setCurrent(pageno);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("orderBy", "every_weight");
		map.put("order", "asc");
		Pager<EveryPic> p=this.getBaseService().findByPager(pEveryPic, map);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
}
