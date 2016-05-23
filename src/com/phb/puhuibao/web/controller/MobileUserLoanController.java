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
import com.phb.puhuibao.entity.MobileUserLoan;

@Controller
@RequestMapping(value = "/mobileUserLoan")
public class MobileUserLoanController extends BaseController<MobileUserLoan, String> {
	@Resource(name = "mobileUserLoanService")
	public void setBaseService(IBaseService<MobileUserLoan, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@Resource(name = "uploadFileService")
	private IBaseService<UploadFile, String> uploadFileService;

	@RequestMapping(value="loanCertification")
	@ResponseBody
	public Map<String, Object> loanCertification(@RequestParam int muid, 
			@RequestParam String jobFileName, 
			@RequestParam String jobFileType, 
			@RequestParam String jobFilePath, 
			@RequestParam String jobOrgFileName,
			@RequestParam String incomeFileName, 
			@RequestParam String incomeFileType, 
			@RequestParam String incomeFilePath, 
			@RequestParam String incomeOrgFileName,
			@RequestParam String housingFileName, 
			@RequestParam String housingFileType, 
			@RequestParam String housingFilePath, 
			@RequestParam String housingOrgFileName) {		
		UploadFile uploadFileJob = new UploadFile();
		uploadFileJob.setFileName(jobFileName);
		uploadFileJob.setFileType(jobFileType);
		uploadFileJob.setFilePath(jobFilePath);
		uploadFileJob.setOrgFileName(jobOrgFileName);
		UploadFile uploadFileIncome = new UploadFile();
		uploadFileIncome.setFileName(incomeFileName);
		uploadFileIncome.setFileType(incomeFileType);
		uploadFileIncome.setFilePath(incomeFilePath);
		uploadFileIncome.setOrgFileName(incomeOrgFileName);
		UploadFile uploadFileHousing = new UploadFile();
		uploadFileHousing.setFileName(housingFileName);
		uploadFileHousing.setFileType(housingFileType);
		uploadFileHousing.setFilePath(housingFilePath);
		uploadFileHousing.setOrgFileName(housingOrgFileName);

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			uploadFileService.save(uploadFileJob);
			uploadFileService.save(uploadFileIncome);
			uploadFileService.save(uploadFileHousing);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		Map<String,Object> params = new HashMap<String, Object>();
		MobileUserLoan u = this.getBaseService().getById("" + muid);
		if (u != null) {
			params = new HashMap<String, Object>();
			params.put("id", "'" + u.getJobCertification() + "','" + u.getIncomeCertification() + "','" + u.getHousingCertification() + "'");
			uploadFileService.delete(params);
		}
		
		MobileUserLoan entity = new MobileUserLoan();
		entity.setmUserId(muid);
		entity.setJobCertification(uploadFileJob.getId());
		entity.setIncomeCertification(uploadFileIncome.getId());
		entity.setHousingCertification(uploadFileHousing.getId());
		try {
			this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		
		Map<String, String> result = new HashMap<String, String>();
		data.put("result", result);
		data.put("message", "保存成功");
		data.put("status", 1);
		return data;
	}
}
