package com.phb.puhuibao.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.ThirdPayAccount;

@Controller
@RequestMapping(value = "/thirdPayAccount")
public class ThirdPayAccountController extends BaseController<ThirdPayAccount, String> {
	@Resource(name = "thirdPayAccountService")
	public void setBaseService(IBaseService<ThirdPayAccount, String> baseService) {
		super.setBaseService(baseService);
	}

}
