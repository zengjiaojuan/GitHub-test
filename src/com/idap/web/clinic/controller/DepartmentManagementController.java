package com.idap.web.clinic.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idap.clinic.entity.DepartmentManagement;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
 

@Controller
@RequestMapping(value = "/departmentManagement")
public class DepartmentManagementController extends BaseController<DepartmentManagement, String> {
	@Resource(name = "departmentManagementService")
	public void setBaseService(IBaseService<DepartmentManagement, String> baseService) {
		super.setBaseService(baseService);
	}

	@RequestMapping(value="findList")
	@ResponseBody
	protected Map<String, Object> findList() {
		Map<String,Object> map=new HashMap<String,Object>();
		List<DepartmentManagement> result = this.getBaseService().findList(map);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
 
}
