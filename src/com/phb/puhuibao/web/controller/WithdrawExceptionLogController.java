package com.phb.puhuibao.web.controller;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.WithdrawExceptionLog;
 

@Controller
@RequestMapping(value = "/withdrawExceptionLog")
public class WithdrawExceptionLogController extends BaseController<WithdrawExceptionLog, String> {
	final Log log = LogFactory.getLog(WithdrawExceptionLogController.class);
	
	@Override
	@Resource(name = "withdrawExceptionLogService")
	public void setBaseService(IBaseService<WithdrawExceptionLog, String> baseService) {
		super.setBaseService(baseService);
	}
	


}
