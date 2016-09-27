package com.phb.puhuibao.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idap.web.common.controller.Commons;
import com.idp.pub.context.AppContext;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.ApkUpload;
@Controller
@RequestMapping(value = "/apkUpload")
public class ApkUploadController  extends BaseController<ApkUpload, String> {
	private static final Log LOG = LogFactory.getLog(ProductBidController.class);
	private final static String ERROR_TEMPLATE = "/templates/puhuibao/error.html";
	@Resource(name = "appContext")
	private AppContext appContext;
	@Resource(name = "commons")
	private Commons commons;
	@Override
	@Resource(name = "apkUploadService")
	public void setBaseService(IBaseService<ApkUpload, String> baseService) {
		super.setBaseService(baseService);
	}

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value="findList")
	@ResponseBody
	protected Map<String, Object> findList(@RequestParam String type,@RequestParam String pageno) {
		Map<String,Object> params = new HashMap<String,Object>();
		if ("".equals(type)) {
		} else if ("1".equals(type)) {
			params = new HashMap<String,Object>();
			params.put("getype", type);
		} else {
			params.put("type", type);
		}
		List<ApkUpload> result = this.getBaseService().findList(params);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	@Deprecated
	@RequestMapping(value="getApkById")
	public void getApkDetailById(@RequestParam String id,HttpServletRequest request, HttpServletResponse response){
		ApkUpload apkupload = this.getBaseService().getById(id);
		if (apkupload == null) {
			try {
				request.getRequestDispatcher(ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;	
		}
		
			}
			
	
	@Deprecated
	@RequestMapping(value="uploadApk")
	public void uploadApk(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		DiskFileItemFactory  dfif = new  DiskFileItemFactory();
		ServletFileUpload  sfu = new ServletFileUpload(dfif);
		ApkUpload apk=new ApkUpload();
		apk.setType(1);
			List<FileItem> fis=null;
			try {
				fis = sfu.parseRequest(request);
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < fis.size(); i++) {
				FileItem  fi = fis.get(i);				
				if(fi.isFormField()){
                apk.setapk_describe(fi.getString());
					System.out.println(fi.getFieldName());
					System.out.println(fi.getName());
					System.out.println(fi.getString());
					
				}else{
					
					System.out.println(fi.getFieldName());
					System.out.println(fi.getName());
					System.out.println(fi.getString());					
					InputStream in=null;
					
					try {
						in = fi.getInputStream();
						String fileName = fi.getName();
						System.out.println(fileName);
						String path = request.getSession().getServletContext().getRealPath("\\AndroidApk\\");
						System.out.println(path);
						apk.setApk(path);
						Date  today = new Date();
						File  newFile = new File(path+"\\"+today.getTime()+fileName);
						OutputStream out=null;
						out = new  FileOutputStream(newFile);
						byte  bs[] = new byte[in.available()];
						in.read(bs);
						out.write(bs);
						out.flush();
						out.close();
						in.close();
						data.put("apk", apk);
						data.put("message", "Success");
						data.put("status", 1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						data.put("apk", null);
						data.put("message", "apk上传失败！");
						data.put("status", 0);
					}
				}
			}		
	}			
}

