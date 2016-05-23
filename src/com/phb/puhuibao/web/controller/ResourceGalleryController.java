package com.phb.puhuibao.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.idap.clinic.entity.UploadFile;
import com.idap.web.common.controller.Commons;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.ResourceGallery;

@Controller
@RequestMapping(value = "/resourceGallery")
public class ResourceGalleryController extends BaseController<ResourceGallery, String> {
	@javax.annotation.Resource(name = "resourceGalleryService")
	public void setBaseService(IBaseService<ResourceGallery, String> baseService) {
		super.setBaseService(baseService);
	}

	@Resource(name = "uploadFileService")
	private IBaseService<UploadFile, String> uploadFileService;

	@Resource(name = "commons")
	private Commons commons;
	
	/**
	 * 上传图片
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=uploadPicture", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadPicture(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		MultipartFile multipartFile = null;
		for (Map.Entry<String, MultipartFile> set : fileMap.entrySet()) {
			multipartFile = set.getValue();// 文件名
		}
		String orgname = multipartFile.getOriginalFilename();// 获取原始文件名
		String fileType = orgname.substring(orgname.lastIndexOf(".") + 1, orgname.length());
		String fileName = System.currentTimeMillis() + "." + fileType;
		String filePath = commons.getFileUploadPath() + "/" + fileName;
		String mkpath = commons.getFileUploadPath();
		File file = new File(filePath);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			File mkdir = new File(mkpath);
			if (!mkdir.exists()) {
				mkdir.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			multipartFile.transferTo(file);
			
			UploadFile uploadFile = new UploadFile();
			uploadFile.setFileName(fileName);
			uploadFile.setFileType(fileType);
			uploadFile.setFilePath(filePath);
			uploadFile.setOrgFileName(orgname);
			uploadFile = uploadFileService.save(uploadFile);
			data.put("result", uploadFile.getId());
			data.put("message", "");
			data.put("status", 1);
		} catch (Exception e) {
			data.put("message", "图片上传失败！");
			data.put("status", 0);
		}
		return data;
	}
	
//	/**
//	 * 上传图片
//	 * @param resourceId
//	 * @param fileName
//	 * @param fileType
//	 * @param filePath
//	 * @param orgFileName
//	 * @return
//	 */
//	@RequestMapping(value="uploadPicture")
//	@ResponseBody
//	public Map<String, Object> uploadPicture (
//			@RequestParam int resourceId, 
//			@RequestParam String fileName, 
//			@RequestParam String fileType, 
//			@RequestParam String filePath, 
//			@RequestParam String orgFileName) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		UploadFile uploadFile = new UploadFile();
//		uploadFile.setFileName(fileName);
//		uploadFile.setFileType(fileType);
//		uploadFile.setFilePath(filePath);
//		uploadFile.setOrgFileName(orgFileName);
//		try {
//			uploadFile = uploadFileService.save(uploadFile);
//		} catch (Exception e) {
//			data.put("message", "图片上传失败！" + e.getMessage());
//			data.put("status", 0);			
//			return data;
//		}
//		ResourceGallery entity = new ResourceGallery();
//		entity.setPictureId(uploadFile.getId());
//		entity.setResourceId(resourceId);
//		
//		try {
//		    this.getBaseService().save(entity);
//		} catch (Exception e) {
//			data.put("message", "图片上传失败！" + e.getMessage());
//			data.put("status", 0);			
//			return data;
//		}
//
//		data.put("result", entity);
//		data.put("message", "图片上传成功！");
//		data.put("status", 1);
//		return data;
//	}
	
	/**
	 * 图片册
	 * @param type
	 * @return
	 */
	@RequestMapping(value="findList")
	@ResponseBody
	public Map<String, Object> findList(@RequestParam int resourceId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		List<ResourceGallery> result = this.getBaseService().findList(params);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 删除一张图片
	 * @param pictureId
	 * @return
	 */
	@RequestMapping(value="delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam String pictureId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pictureId", pictureId);
		try {
			this.getBaseService().delete(params);
		} catch (Exception e) {
			data.put("message", "图片删除失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
		params = new HashMap<String,Object>();
		params.put("id", "'" + pictureId + "'");
		uploadFileService.delete(params);

		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 上传一张图片
	 * @param pictureId
	 * @return
	 */
	@RequestMapping(value="add")
	@ResponseBody
	public Map<String, Object> add(@RequestParam int resourceId, @RequestParam String pictureId) {
		ResourceGallery entity = new ResourceGallery();
		entity.setPictureId(pictureId);
		entity.setResourceId(resourceId);
		
		Map<String, Object> data = new HashMap<String, Object>();
		try {
		    this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "图片上传失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}

		data.put("result", entity);
		data.put("message", "图片上传成功！");
		data.put("status", 1);
		return data;
	}
}
