package com.cddgg.p2p.huitou.spring.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cddgg.p2p.huitou.model.LoanContract;
import com.cddgg.p2p.huitou.spring.annotation.Logrecord;
import com.cddgg.p2p.huitou.spring.service.ContractService;
import com.cddgg.p2p.huitou.spring.util.FileUtil;

@RequestMapping("test")
@Controller
public class TestController {

	@Resource
	private ContractService service_contract;
	
	@RequestMapping("add")
	public String Test(HttpServletRequest request) throws Exception{
		
		System.out.println(FileUtil.upload(request, "C:/upload/pomo/87855/ad",FileUtil.NORMAL_TYPES,"testFile",null));
		
		
//
//		LoanContract templateAttribute = new LoanContract();
//		templateAttribute.setIdCardA("5001041992121808500");
//		templateAttribute.setIdCardB("5001041992121808501");
//		
//		templateAttribute.setPartyA("小然");
//		templateAttribute.setPartyB("小飞");
//		
//		templateAttribute.setLoanMoney(706402000.58);
//		templateAttribute.setBonaType("一次性还本金和利息");
//		templateAttribute.setDateTime("2013年12月9日 17时24分");
//		templateAttribute.setMonthBack("2014年5月10日 11时09分");
//		
//		templateAttribute.setPdf_password("1100");
//		
//		templateAttribute.setRate(1.2);
//		
//		templateAttribute.setContractId("DGG-545684-KJL");
//		templateAttribute.setSignedAddress("成都");
//		
//		File of = new File("C:/借款合同-new.pdf");
//		FileOutputStream fo = new FileOutputStream(of);
//
//		service_contract.born(templateAttribute, fo);
//		
//		fo.close();
		
		return "index";
	}
	
	@RequestMapping("pay")
	public String pay(HttpServletRequest request,@RequestParam(value = "creditorLinkIds") Object creditorLinkIds) throws Exception{
	    
	    System.out.println(creditorLinkIds);
	    
	    Map map = request.getParameterMap();
	    
	    return "index";
	    
	}
	
}
