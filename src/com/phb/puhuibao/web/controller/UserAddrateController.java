package com.phb.puhuibao.web.controller;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

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
	 * 合同列表
	 * @param pageno
	 * @param clinicId
	 * @return
	 */
	@RequestMapping(value="queryDoctorsFromClinic")
	@ResponseBody
	public Map<String, Object> queryDoctorsManagement(@RequestParam int pageno,@RequestParam(value="clinic_id") String clinicId){
		Pager<UserAddrate> pdoctorResult=new Pager<UserAddrate>();
		pdoctorResult.setReload(true);
		pdoctorResult.setCurrent(pageno);
		Map<String,Object> hmap=new HashMap<String,Object>();
		hmap.put("orderBy", "doctor_date");
		hmap.put("clinicId", clinicId);
		Pager<UserAddrate> p=this.getBaseService().findByPager(pdoctorResult, hmap);
		 
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}
	
	/**
	 * 医生详细
	 * @param id
	 * @return
	 */
	@RequestMapping(value="queryDoctorById")
	@ResponseBody
	public Map<String, Object> queryContractManagement(@RequestParam(value="id") String id){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", this.getBaseService().getById(id));
		data.put("message", "");
		data.put("status", 1);
		return data;		
	}

}
