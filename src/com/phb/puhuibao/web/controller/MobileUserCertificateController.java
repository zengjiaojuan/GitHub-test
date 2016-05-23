package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idap.clinic.entity.UploadFile;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.MobileUserCertificate;

@Controller
@RequestMapping(value = "/mobileUserCertificate")
public class MobileUserCertificateController extends BaseController<MobileUserCertificate, String> {
	@Resource(name = "mobileUserCertificateService")
	public void setBaseService(IBaseService<MobileUserCertificate, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@Resource(name = "uploadFileService")
	private IBaseService<UploadFile, String> uploadFileService;

	@RequestMapping(value="loanCertification")
	@ResponseBody
	public Map<String, Object> loanCertification(@RequestParam int muid, 
			@RequestParam String fileName, 
			@RequestParam String fileType, 
			@RequestParam String filePath, 
			@RequestParam String orgFileName) {		
		UploadFile uploadFileJob = new UploadFile();
		uploadFileJob.setFileName(fileName);
		uploadFileJob.setFileType(fileType);
		uploadFileJob.setFilePath(filePath);
		uploadFileJob.setOrgFileName(orgFileName);

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			uploadFileService.save(uploadFileJob);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}

		MobileUserCertificate entity = new MobileUserCertificate();
		entity.setmUserId(muid);
		entity.setCertification(uploadFileJob.getId());
		try {
			this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		
		data.put("message", "保存成功");
		data.put("status", 1);
		return data;
	}
}
