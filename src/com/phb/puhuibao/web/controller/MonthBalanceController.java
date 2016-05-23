package com.phb.puhuibao.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.MonthBalance;

@Controller
@RequestMapping(value = "/monthBalance")
public class MonthBalanceController extends BaseController<MonthBalance, String> {
	@Resource(name = "monthBalanceService")
	public void setBaseService(IBaseService<MonthBalance, String> baseService) {
		super.setBaseService(baseService);
	}
	
}
