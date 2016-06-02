package com.phb.puhuibao.web.controller;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.AddRate;
 

@Controller
@RequestMapping(value = "/addRate")
public class AddRateController extends BaseController<AddRate, String> {
	final Log log = LogFactory.getLog(AddRateController.class);
	
	@Override
	@Resource(name = "addRateService")
	public void setBaseService(IBaseService<AddRate, String> baseService) {
		super.setBaseService(baseService);
	}
	


}
