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
import com.phb.puhuibao.entity.ContractManagement;
 

@Controller
@RequestMapping(value = "/contractManagement")
public class ContractManagementController extends BaseController<ContractManagement, String> {
	@Resource(name = "contractManagementService")
	public void setBaseService(IBaseService<ContractManagement, String> baseService) {
		super.setBaseService(baseService);
	}
	
	 
	
 
}
