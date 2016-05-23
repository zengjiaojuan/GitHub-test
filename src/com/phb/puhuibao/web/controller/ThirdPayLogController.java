package com.phb.puhuibao.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.ThirdPayLog;

@Controller
@RequestMapping(value = "/thirdPayLog")
public class ThirdPayLogController extends BaseController<ThirdPayLog, String> {
	@Resource(name = "thirdPayLogService")
	public void setBaseService(IBaseService<ThirdPayLog, String> baseService) {
		super.setBaseService(baseService);
	}

}
