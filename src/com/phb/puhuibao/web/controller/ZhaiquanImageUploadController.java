package com.phb.puhuibao.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.idap.web.common.controller.Commons;
import com.phb.puhuibao.common.Functions;

@Controller
@RequestMapping(value = "/zhaiquanImageUpload")
public class ZhaiquanImageUploadController {
	@Resource(name = "commons")
	private Commons commons;

	/**
	 * 上传图片
	 * @param request
	 * @param response
	 * @param path
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response) {
		String path = commons.getFileUploadPath();
		Functions.uploadZhaiquan(request, response, path);
	}
}
