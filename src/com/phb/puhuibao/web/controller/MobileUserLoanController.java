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

import com.idap.clinic.entity.UploadFile;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.MobileUserLoan;

@Controller
@RequestMapping(value = "/mobileUserLoan")
public class MobileUserLoanController extends BaseController<MobileUserLoan, String> {
	private static final Log log = LogFactory.getLog(MobileUserLoanController.class);
	@Override
	@Resource(name = "mobileUserLoanService")
	public void setBaseService(IBaseService<MobileUserLoan, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@Resource(name = "uploadFileService")
	private IBaseService<UploadFile, String> uploadFileService;

	@RequestMapping(value="loanCertification")
	@ResponseBody
	public Map<String, Object> loanCertification( @RequestParam int muid,  @RequestParam String fileIds  ) {	
 
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUserLoan entity = new MobileUserLoan();
		entity.setmUserId(muid);
		entity.setJobCertification(fileIds);
	 
		try {
			this.getBaseService().save(entity);
			data.put("message", "保存成功！");
			data.put("status", 1);
			return data;
		} catch (Exception e) {
			log.error("失败:"+e);
			data.put("message", "保存失败！");
			data.put("status", 0);
			return data;
		}
 
	}
}
