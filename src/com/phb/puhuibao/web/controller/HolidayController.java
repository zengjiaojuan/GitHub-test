package com.phb.puhuibao.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.Holiday;

@Controller
@RequestMapping(value = "/holiday")
public class HolidayController extends BaseController<Holiday, String> {
	@Resource(name = "holidayService")
	public void setBaseService(IBaseService<Holiday, String> baseService) {
		super.setBaseService(baseService);
	}

}
