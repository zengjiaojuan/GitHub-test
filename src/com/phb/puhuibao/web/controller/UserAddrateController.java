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

import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.UserAddrate;
 

@Controller
@RequestMapping(value = "/userAddrate")
public class UserAddrateController extends BaseController<UserAddrate, String> {
	@Override
	@Resource(name = "userAddrateService")
	public void setBaseService(IBaseService<UserAddrate, String> baseService) {
		super.setBaseService(baseService);
	}
	
	/**
	 * 我的加息劵列表
	 * @param pageno
	 * @param clinicId
	 * @return
	 */
	@RequestMapping(value="myUserAddrateList")
	@ResponseBody
	public Map<String, Object> myUserAddrateList(@RequestParam int pageno, @RequestParam String muid){
		final Log log = LogFactory.getLog(NotificationController.class);
		Pager<UserAddrate> pdoctorResult=new Pager<UserAddrate>();
		pdoctorResult.setReload(true);
		pdoctorResult.setCurrent(pageno);
		pdoctorResult.setLimit(30);
		Map<String,Object> hmap=new HashMap<String,Object>();
		hmap.put("orderBy", "m.last_date desc");
		hmap.put("rateStatus", "0,1");
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			Pager<UserAddrate> p=this.getBaseService().findByPager(pdoctorResult, hmap);
			data.put("result", p.getData());
			data.put("count", p.getTotal());
			data.put("message", "");
			data.put("status", 1);
			return data;	
		} catch (Exception e) {
			log.error("我的加息劵列表出错:"  +e);
			data.put("message", "查询出错!");
			data.put("status", 0);
			return data;
			 
		}
 
	}
	
	 

}
