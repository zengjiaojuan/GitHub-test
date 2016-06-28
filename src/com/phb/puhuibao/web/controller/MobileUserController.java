package com.phb.puhuibao.web.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.idap.clinic.entity.UploadFile;
import com.idap.web.common.controller.Commons;
import com.idp.pub.constants.Constants;
import com.idp.pub.context.AppContext;
import com.idp.pub.service.IBaseService;
import com.idp.pub.utils.DESUtils;
import com.idp.pub.web.controller.BaseController;
import com.opensymphony.oscache.util.StringUtil;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.common.InitListener;
import com.phb.puhuibao.entity.Invite;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserExtra;
import com.phb.puhuibao.entity.MobileUserSignin;
import com.phb.puhuibao.entity.UserCard;
import com.phb.puhuibao.entity.UserSession;
import com.phb.puhuibao.service.MobileUserService;

@Controller
@RequestMapping(value = "/userInformation")
public class MobileUserController extends BaseController<MobileUser, String> {
	final Log log = LogFactory.getLog(MobileUserController.class);
	@Override
	@Resource(name = "mobileUserService")
	public void setBaseService(IBaseService<MobileUser, String> baseService) {
		super.setBaseService(baseService);
	}
	@Resource(name = "uploadFileService")
	private IBaseService<UploadFile, String> uploadFileService;
	@Resource(name = "mobileUserExtraService")
	private IBaseService<MobileUserExtra, String> mobileUserExtraService;
	@Resource(name = "mobileUserSigninService")
	private IBaseService<MobileUserSignin, String> mobileUserSigninService;
	@Resource(name = "inviteService")
	private IBaseService<Invite, String> inviteService;
	@Resource(name = "mobileUserService")
	private MobileUserService mobileUserService;
 
	@Resource(name = "userSessionService")
	private IBaseService<UserSession, String> userSessionService;
  
	
	@Resource(name = "commons")
	private Commons commons;
	
	@RequestMapping(value="getUserProfile")
	@ResponseBody
	public Map<String, Object> getUserProfile(@RequestParam int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
	 

		Map<String,Object> params=new HashMap<String,Object>();
		Map<String,Object> result=new HashMap<String,Object>();
		params.put("mUserId", muid);
		MobileUser muser = this.getBaseService().unique(params);
		if (muser == null) {
			data.put("message", "该用户不存在！");
			data.put("status", 0);
		}  else {
			 
			if(StringUtil.isEmpty(muser.getmUserName()) ){
				result.put("name", muser.getmUserTel());
			}else{
				result.put("name", muser.getmUserName());
			}
			result.put("photo", muser.getPhoto());
			result.put("mobile", muser.getmUserTel());
			result.put("id", muser.getIdNumber());
 
			data.put("result", result);
			data.put("message", "成功");
			data.put("status", 1);
		}
		return data;
	}
	
	
	
 
	@RequestMapping(value="mobileLoginForIOS")
	@ResponseBody
	public Map<String, Object> mobileLoginForIOS(@RequestParam String mUserTel, @RequestParam String mUserPwd) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (!Functions.isMobile(mUserTel)) {
			data.put("message", "请输入正确的手机号！");
			data.put("status", 0);
			return data;
		}

		Map<String,Object> params=new HashMap<String,Object>();
		params.put("mUserTel", mUserTel);
		MobileUser u = this.getBaseService().unique(params);
		if (u == null) {
			data.put("message", "该手机号没注册！");
			data.put("status", 0);
		} else if (!DESUtils.decrypt(mUserPwd).equals(u.getmUserPwd())) {
			data.put("message", "密码错误！");
			data.put("status", 0);			
		} else {
			if (u.getmUserName() != null) {
				u.setmUserName(DESUtils.encrypt(u.getmUserName()));
			}
			if (u.getIdNumber() != null) {
				u.setIdNumber(DESUtils.encrypt(u.getIdNumber()));
			}
//			if (u.getPayPassword() != null) {
//				u.setPayPassword(DESUtils.encrypt(u.getPayPassword()));
//			}
			u.setmUserPwd("");
			u.setPayPassword("");
	 
			// 防止用户两个手机同时登陆两个手机:每次登陆产生一个随机数  每次访问的时候需要带上这个随机数   这个随机数必须在内存和数据库都存在   在用户注册的时候  就会保存这个 所以只需要修改就行
			int sessionid = (int) (Math.random()*(999999999-100000000)+100000000);  
			UserSession us = new UserSession();
			us.setSessionId(sessionid);
			us.setUserId(u.getmUserId());
			userSessionService.update(us);
			
			InitListener.sessionhash.put(u.getmUserId(),sessionid);// 把这个用户的随机数 更新到内存
 
 
			data.put("result", u);
			data.put("token", sessionid);
			data.put("message", "登录成功！");
			data.put("status", 1);
		}
		return data;
	}

	@RequestMapping(value="checkMobile")
	@ResponseBody
	public Map<String, Object> checkMobile(@RequestParam String mUserTel) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (!Functions.isMobile(mUserTel)) {
			data.put("message", "请输入正确的手机号！");
			data.put("status", 0);
			return data;
		}

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserTel", mUserTel);
		MobileUser entity = this.getBaseService().unique(params);
		if (entity != null) {
			data.put("result", true);
			data.put("message", "该手机号已注册，请登录！");
		} else {
			data.put("result", false);
			data.put("message", "");
		}
		data.put("status", 1);			
		return data;
	}
	
	@RequestMapping(value="thirdPartyLogin")
	@ResponseBody
	public Map<String, Object> thirdPartyLogin(@RequestParam String thirdParty) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("thirdParty", thirdParty);
		MobileUser u = this.getBaseService().unique(params);
		Map<String, Object> data = new HashMap<String, Object>();
		if (u == null) {
			data.put("message", "请通过手机验证码绑定手机号！");
			data.put("status", 0);			
		} else {
			data.put("result", u);
			data.put("message", "登录成功！");
			data.put("status", 1);
		}
		return data;
	}
	
	@RequestMapping(value="thirdPartyBundle")
	@ResponseBody
	public Map<String, Object> thirdPartyBundle(@RequestParam String thirdParty, @RequestParam String mUserTel) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (!Functions.isMobile(mUserTel)) {
			data.put("message", "请输入正确的手机号！");
			data.put("status", 0);
			return data;
		}

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserTel", mUserTel);
		MobileUser entity = this.getBaseService().unique(params);
		if (entity != null) {
			entity.setThirdParty(thirdParty);
			try {
				entity = this.getBaseService().update(entity);
			} catch (Exception e) {
				data.put("message", "绑定失败！" + e.getMessage());
				data.put("status", 0);			
				return data;
			}
		} else {
			entity = new MobileUser();
			entity.setThirdParty(thirdParty);
			entity.setmUserTel(mUserTel);
			try {
			    entity = this.getBaseService().save(entity);
			} catch (Exception e) {
				data.put("message", "绑定失败！" + e.getMessage());
				data.put("status", 0);			
				return data;
			}
		}
		data.put("result", entity);
		data.put("message", "绑定成功！");
		data.put("status", 1);
		return data;
	}

 
	
	// 用户注册 不分 ios 和 android
	@RequestMapping(value="saveMobileUserForIOS")
	@ResponseBody
	public Map<String, Object> saveMobileUserForIOS(@RequestParam String mUserTel, @RequestParam String mUserPwd, @RequestParam String inviteCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			if (!Functions.isMobile(mUserTel)) {
				data.put("message", "请输入正确的手机号！");
				data.put("status", 0);
				return data;
			}

			Map<String,Object> params=new HashMap<String,Object>();
			params.put("mUserTel", mUserTel);
			MobileUser u = this.getBaseService().unique(params);
			if (u != null) {
				data.put("message", "该手机号已注册，请登录！");
				data.put("status", 0);
				return data;
			}
			
			
			int parentId = 0;
			if (inviteCode.length() > 0) {
				params=new HashMap<String,Object>();
				params.put("code", inviteCode);
				Invite result = inviteService.unique(params);
				if (result == null) {
					data.put("message", "该邀请码无效！");
					data.put("status", 2);
					return data;
				}
				parentId = result.getmUserId();
			}

			MobileUser entity = new MobileUser();
			entity.setmUserTel(mUserTel);
			entity.setmUserPwd(DESUtils.decrypt(mUserPwd));
			entity.setThirdParty("");
			entity.setmUserName("");
			entity.setPhoto("");
			entity.setOccupation("");
			entity.setmUserEmail("");
			entity.setPayPassword("");
			entity.setParentId(parentId);
			entity.setIdNumber("");
			String base = "abcdefghijklmnopqrstuvwxyz123456789";     
		    Random random = new Random();     
		    StringBuffer sb = new StringBuffer();     
		    for (int i = 0; i < 6; i++) {     
		        int number = random.nextInt(base.length());     
		        sb.append(base.charAt(number));     
		    }     
		    entity.setNickname(sb.toString());//生成随机昵称
			 mobileUserService.userCreate(entity);
			 
			 
			 
			 entity.setmUserPwd("");
			data.put("result", entity);
			data.put("token", entity.getLiveness()); // 存的是有用户的 key
			data.put("message", "注册成功！");
			data.put("status", 1);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("注册失败:"+e);
			data.put("message", "注册保存失败！" );
			data.put("status", 0);			
			return data;
		}
		
	}
 
	
	
	@RequestMapping(value="saveUserPhoto")
	@ResponseBody
	public Map<String, Object> saveUserPhoto(
			@RequestParam int muid, 
			@RequestParam String fileId ) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser entity = new MobileUser();
	 
		entity.setmUserId(muid);
		entity.setPhoto(fileId);
		try {
			this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "出错！");
			data.put("status", 0);			
			return data;
		}
 
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
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
	
	
	
	@RequestMapping(value="saveUserInformationForIOS")
	@ResponseBody
	public Map<String, Object> saveUserInformationForIOS(
			@RequestParam String uid, 
			@RequestParam String sex, 
			@RequestParam String age, 
			@RequestParam String occupation, 
			@RequestParam String nickname, 
			@RequestParam String constellation, 
			@RequestParam String fileName, 
			@RequestParam String fileType, 
			@RequestParam String filePath, 
			@RequestParam String orgFileName) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser entity = new MobileUser();
		if (fileName.length() > 0) {
			UploadFile uploadFile = new UploadFile();
			uploadFile.setFileName(fileName);
			uploadFile.setFileType(fileType);
			uploadFile.setFilePath(filePath);
			uploadFile.setOrgFileName(orgFileName);
			try {
				uploadFile = uploadFileService.save(uploadFile);
			} catch (Exception e) {
				data.put("message", "网络异常！");
				data.put("status", 0);			
				return data;
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("mUserId", uid);
			MobileUser u = this.getBaseService().unique(params);
			if (u.getPhoto() != null && u.getPhoto().length() > 0) {
				params = new HashMap<String,Object>();
				params.put("id", "'" + u.getPhoto() + "'");
				uploadFileService.delete(params);
			}
			entity.setPhoto(uploadFile.getId());
		}
		int int_sex = 1;
		if ("2".equals(sex)) {
			int_sex = 2;
		}
		int int_age = 0;
		if (age.length() > 0 && StringUtils.isNumeric(age)) {
			int_age = Integer.parseInt(age);
			if (int_age > 127) {
				data.put("message", "用户年龄不合理！");
				data.put("status", 0);			
				return data;
			}
		}
		if (uid.length() > 0 && StringUtils.isNumeric(uid)) {
		} else {
			data.put("message", "用户id非法！");
			data.put("status", 0);			
			return data;
		}
		entity.setmUserId(Integer.parseInt(uid));
		entity.setSex(int_sex);
		entity.setAge(int_age);
		entity.setOccupation(occupation);
		entity.setNickname(nickname);
		entity.setConstellation(constellation);
		try {
		    entity = this.getBaseService().update(entity);
		    entity = this.getBaseService().getById(uid);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		
	    entity.setmUserName(DESUtils.encrypt(entity.getmUserName()));
	    entity.setIdNumber(DESUtils.encrypt(entity.getIdNumber()));
	    entity.setmUserPwd("");
	    entity.setPayPassword("");
		data.put("result", entity);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}

	/**
	 * 保存用户上传的所有照片  新增和修改 删除，都用这个 因为前端取的是文件id的串
	 * @param uid
	 * @param fileIds
	 * @return
	 */
	@RequestMapping(value="saveUserPictureWall")
	@ResponseBody
	public Map<String, Object> saveUserPictureWall(@RequestParam int muid, @RequestParam String fileIds) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser entity = new MobileUser();
		entity.setPicturewall(fileIds);
		entity.setmUserId(muid);
		try {
		    entity = this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		data.put("result", entity);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}	
	/**
	 * 读取照片墙的所有照片id  获得字符串，带逗号分割
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="getUserPictureWall")
	@ResponseBody
	public Map<String, Object> getUserPictureWall(@RequestParam String muid) {
		MobileUser user = this.getBaseService().getById(muid);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", user.getPicturewall());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 个人名片照片
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="getUserProfilePic")
	@ResponseBody
	public Map<String, Object> getUserProfilePic(@RequestParam String muid) {
		MobileUser user = this.getBaseService().getById(muid);
		String photo = user.getPhoto();
		String id = "";
		if (StringUtils.isNotEmpty(photo)) {
			UploadFile uploadFile = uploadFileService.getById(photo);
			if (uploadFile != null) {
				id = photo;
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", id);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
  
	
	
	/**
	 * 个人名片中的信息更新
	 * @param muid
	 * @param fmuid
	 * @return
	 */
	@RequestMapping(value="setUserSocialProfile")
	@ResponseBody
	public Map<String, Object> setUserSocialProfile(@RequestParam int muid,String nickname,String socialStatus,int sex){
		MobileUser user = new MobileUser();
		user.setmUserId(muid);
		user.setNickname(nickname);
		user.setSocialStatus(socialStatus);
		user.setSex(sex);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			user = this.getBaseService().update(user);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		data.put("result", user);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}	
	
	/**
	 * 保存用户上传的首页照片
	 * @param uid
	 * @param fileIds
	 * @return
	 */
	@RequestMapping(value="saveUserProfilePic")
	@ResponseBody
	public Map<String, Object> saveUserProfilePic(@RequestParam int muid, @RequestParam String pictureid) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser entity = new MobileUser();
		entity.setPhoto(pictureid);
		entity.setmUserId(muid);
		try {
		    entity = this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		data.put("result", entity);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value="deleteMobileUser")
	@ResponseBody
	public Map<String, Object> deleteMobileUser(@RequestParam String mUserTel) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserTel", mUserTel);
		MobileUser user = this.getBaseService().unique(params);
		if (user == null) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("message", "用户不存在！");
			data.put("status", 0);
			return data;
		}
		int id = user.getmUserId();
		String sql = "delete from phb_muser_account where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_account_log where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_card where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_redpacket where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_investment where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_experience where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_experience_investment where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
//		sql = "delete from phb_appreciation where m_user_id=" + id;
//		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_mobile_user_extra where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_order where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_muser_loan where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_mobile_user_loan where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);
		sql = "delete from phb_mobile_user where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);		
 
		sql = "delete from phb_supply_resource_order where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);		
		sql = "delete from phb_resource where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);		
		sql = "delete from phb_evaluate where m_user_id=" + id;
		this.jdbcTemplate.execute(sql);		
		
//		int count = this.getBaseService().delete(params);
//		Map<String, Object> data = new HashMap<String, Object>();
//		if (count == 0) {
//			data.put("message", "删除失败！");
//			data.put("status", 0);
//		} else {
//			data.put("message", "删除成功！");
//			data.put("status", 1);
//		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", "ok");
		data.put("status", 1);
		return data;
	}

	/**
	 * 修改登录密码
	 * @param mUserTel
	 * @param old_mUserPwd
	 * @param mUserPwd
	 * @return
	 */
	@Deprecated
	@RequestMapping(value="modifyPassword")
	@ResponseBody
	public Map<String, Object> modifyPassword(@RequestParam String mUserTel, @RequestParam String old_mUserPwd, @RequestParam String mUserPwd) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("mUserTel", mUserTel);
		params.put("mUserPwd", old_mUserPwd);
		MobileUser u = this.getBaseService().unique(params);
		if (u == null) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("message", "该手机号及密码不匹配！");
			data.put("status", 0);
			return data;
		}
		MobileUser entity = new MobileUser();
		entity.setmUserId(u.getmUserId());
		entity.setmUserPwd(mUserPwd);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			entity = this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "密码修改失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
		data.put("message", "密码修改成功！");
		data.put("status", 1);
		return data;
	}
 
	@RequestMapping(value="modifyPasswordForIOS")
	@ResponseBody
	public Map<String, Object> modifyPasswordForIOS(@RequestParam String muid, @RequestParam String oldPassword, @RequestParam String password) {
		MobileUser u = this.getBaseService().getById(muid);
		if (!DESUtils.decrypt(oldPassword).equals(u.getmUserPwd())) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("message", "旧登录密码不正确！");
			data.put("status", 0);
			return data;
		}
		MobileUser entity = new MobileUser();
		entity.setmUserId(u.getmUserId());
		entity.setmUserPwd(DESUtils.decrypt(password));
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			entity = this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "登录密码修改失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
		data.put("message", "登录密码修改成功！");
		data.put("status", 1);
		return data;
	}

 // 旧密码是否正确
	@RequestMapping(value="checkOldPayPasswordIsRight")
	@ResponseBody
	public Map<String, Object> checkOldPayPasswordIsRight(@RequestParam String muid, @RequestParam String oldPayPassword) {
		MobileUser u = this.getBaseService().getById(muid);
		Map<String, Object> data = new HashMap<String, Object>();
		if (!DESUtils.decrypt(oldPayPassword).equals(u.getPayPassword())) {
			data.put("message", "旧支付密码不正确！");
			data.put("status", 0);
			return data;
		}else{
			data.put("message", "旧支付密码正确!");
			data.put("status", 1);			
			return data;
		}
		 
	}
	//更新新的支付密码
	@RequestMapping(value="updateNewPayPassword")
	@ResponseBody
	public Map<String, Object> updateNewPayPassword(@RequestParam String muid, @RequestParam String payPassword) {
		MobileUser u = this.getBaseService().getById(muid);
		MobileUser entity = new MobileUser();
		entity.setmUserId(u.getmUserId());
		entity.setPayPassword(DESUtils.decrypt(payPassword));
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			entity = this.getBaseService().update(entity);
			data.put("message", "支付密码修改成功！");
			data.put("status", 1);
			return data;
		} catch (Exception e) {
			log.error(e);
			data.put("message", "支付密码修改失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
		
	}
	
 
	
	@RequestMapping(value="checkLoginPasswordForIOS")
	@ResponseBody
	public Map<String, Object> checkLoginPasswordForIOS(@RequestParam String muid, @RequestParam String loginPassword) {
		MobileUser u = this.getBaseService().getById(muid);
		Map<String, Object> data = new HashMap<String, Object>();
		if (!DESUtils.decrypt(loginPassword).equals(u.getmUserPwd())) {
			data.put("message", "登录密码不正确！");
			data.put("status", 0);
			return data;
		}else{
			data.put("message", "登录密码正确！");
			data.put("status", 1);
			return data;
		}
	}

	
	/**
	 * 重新绑定手机号
	 * @param muid
	 * @param telphonenumber
	 * @return
	 */
	@RequestMapping(value="rebindPhone")
	@ResponseBody
	public Map<String, Object> rebindPhone(@RequestParam int muid, @RequestParam String telphonenumber) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (!Functions.isMobile(telphonenumber)) {
			data.put("message", "请输入正确的手机号！");
			data.put("status", 0);
			return data;
		}
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("mUserTel", telphonenumber);
		MobileUser u = this.getBaseService().unique(params);
		if (u != null) {
			data.put("message", "该手机号已注册，请直接登录或更换手机号重新绑定！");
			data.put("status", 0);
			return data;
		}
		MobileUser entity = new MobileUser();
		entity.setmUserTel(telphonenumber);
		entity.setmUserId(muid);
		try {
		    entity = this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "绑定失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
 
		data.put("message", "绑定成功！");
		data.put("status", 1);
		return data;
	}
	
 
	@RequestMapping(value="findPasswordForIOS")
	@ResponseBody
	public Map<String, Object> findPasswordForIOS(@RequestParam String mUserTel, @RequestParam String password) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserTel", mUserTel);
		MobileUser u = this.getBaseService().unique(params);
		if (u == null) {
			data.put("message", "改手机号未注册！");
			data.put("status", 0);
			return data;
		}
		MobileUser entity = new MobileUser();
		entity.setmUserId(u.getmUserId());
		entity.setmUserPwd(DESUtils.decrypt(password));
		try {
			entity = this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "登录密码修改失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
		data.put("message", "登录密码修改成功！");
		data.put("status", 1);
		return data;
	}

	@Resource(name = "userCardService")
	private IBaseService<UserCard, String> userCardService;
	 
	@RequestMapping(value="findPayPasswordForIOS")
	@ResponseBody
	public Map<String, Object> findPayPasswordForIOS(@RequestParam int muid, @RequestParam String payPassword) {
//		Map<String,Object> params=new HashMap<String,Object>();
//		params.put("bankAccount", DESUtils.decrypt(bankAccount));
//		UserCard card = userCardService.unique(params);
//		if (card == null) {
//			Map<String, Object> data = new HashMap<String, Object>();
//			data.put("message", "银行卡卡号不正确！");
//			data.put("status", 0);
//			return data;
//		}
//		String cardno = DESUtils.decrypt(bankAccount);
//		Map<String, String> result = TZTService.bankCardCheck(cardno);
//		String isvalid = StringUtils.trimToEmpty(result.get("isvalid"));
//		if ("0".equals(isvalid)) {
//			Map<String, Object> data = new HashMap<String, Object>();
//			data.put("message", "银行卡卡号不正确！");
//			data.put("status", 0);
//			return data;
//		}

		MobileUser entity = new MobileUser();
		entity.setmUserId(muid);
		entity.setPayPassword(DESUtils.decrypt(payPassword));
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			entity = this.getBaseService().update(entity);
			data.put("message", "支付密码修改成功！");
			data.put("status", 1);
			return data;
		} catch (Exception e) {
			data.put("message", "支付密码修改失败！" + e.getMessage());
			log.error("修改密码失败:"+e);
			data.put("status", 0);			
			return data;
		}

	}

 
	@RequestMapping(value="certificationForIOS")
	@ResponseBody
	public Map<String, Object> certificationForIOS(@RequestParam int muid, @RequestParam String mUserName, @RequestParam String idNumber) {
		Map<String, Object> data = new HashMap<String, Object>();
		idNumber = DESUtils.decrypt(idNumber);
		String errorInfo = Functions.idCardValidate(idNumber);
		if (!"".equals(errorInfo)) {
			data.put("message", errorInfo);
			data.put("status", 0);			
			return data;			
		}

		MobileUser entity = new MobileUser();
		entity.setmUserId(muid);
		entity.setmUserName(DESUtils.decrypt(mUserName));
		entity.setIdNumber(idNumber);
		try {
			entity = this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		data.put("message", "保存成功");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 紧急联系人
	 * @param muid
	 * @param emergencyName
	 * @param emergencyPhone
	 * @param emergencyRelation
	 * @return
	 */
	@RequestMapping(value="saveEmergency")
	@ResponseBody
	public Map<String, Object> saveEmergency(@RequestParam int muid, @RequestParam String emergencyName, @RequestParam String emergencyPhone, @RequestParam String emergencyRelation) {
		MobileUser entity = new MobileUser();
		entity.setmUserId(muid);
		entity.setEmergencyName(emergencyName);
		entity.setEmergencyPhone(emergencyPhone);
		entity.setEmergencyRelation(emergencyRelation);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			entity = this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		data.put("message", "保存成功");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 身份证拍照认证
	 * @param muid
	 * @param idPhotoFileName
	 * @param idPhotoFileType
	 * @param idPhotoFilePath
	 * @param idPhotoOrgFileName
	 * @param idPhotoHandFileName
	 * @param idPhotoHandFileType
	 * @param idPhotoHandFilePath
	 * @param idPhotoHandOrgFileName
	 * @return
	 */
	@RequestMapping(value="idCertification")
	@ResponseBody
	public Map<String, Object> idCertification(@RequestParam int muid, 
			@RequestParam String idPhotoFileName, 
			@RequestParam String idPhotoFileType, 
			@RequestParam String idPhotoFilePath, 
			@RequestParam String idPhotoOrgFileName,
			@RequestParam String idPhotoBackFileName, 
			@RequestParam String idPhotoBackFileType, 
			@RequestParam String idPhotoBackFilePath, 
			@RequestParam String idPhotoBackOrgFileName,
			@RequestParam String idPhotoHandFileName, 
			@RequestParam String idPhotoHandFileType, 
			@RequestParam String idPhotoHandFilePath, 
			@RequestParam String idPhotoHandOrgFileName) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser entity = new MobileUser();
		UploadFile uploadFile = new UploadFile();
		uploadFile.setFileName(idPhotoFileName);
		uploadFile.setFileType(idPhotoFileType);
		uploadFile.setFilePath(idPhotoFilePath);
		uploadFile.setOrgFileName(idPhotoOrgFileName);
		UploadFile uploadFileBack = new UploadFile();
		uploadFileBack.setFileName(idPhotoBackFileName);
		uploadFileBack.setFileType(idPhotoBackFileType);
		uploadFileBack.setFilePath(idPhotoBackFilePath);
		uploadFileBack.setOrgFileName(idPhotoBackOrgFileName);
		UploadFile uploadFileHand = new UploadFile();
		uploadFileHand.setFileName(idPhotoHandFileName);
		uploadFileHand.setFileType(idPhotoHandFileType);
		uploadFileHand.setFilePath(idPhotoHandFilePath);
		uploadFileHand.setOrgFileName(idPhotoHandOrgFileName);
		try {
			uploadFile = uploadFileService.save(uploadFile);
			uploadFileBack = uploadFileService.save(uploadFileBack);
			uploadFileHand = uploadFileService.save(uploadFileHand);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		MobileUser u = this.getBaseService().getById("" + muid);
		params = new HashMap<String,Object>();
		params.put("id", "'" + u.getIdPhoto() + "','" + u.getIdPhotoHand() + "'");
		uploadFileService.delete(params);
		
		entity.setmUserId(muid);
		entity.setIdPhoto(uploadFile.getId());
		entity.setIdPhotoBack(uploadFileBack.getId());
		entity.setIdPhotoHand(uploadFileHand.getId());
		entity.setIsAudit(10);
		try {
			this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("idPhoto", entity.getIdPhoto());
		result.put("idPhotoBack", entity.getIdPhotoBack());
		result.put("idPhotoHand", entity.getIdPhotoHand());
		result.put("isAudit", 10);
		data.put("result", result);
		data.put("message", "保存成功");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 读取用户变更信息
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="getChangedInfo")
	@ResponseBody
	public Map<String, Object> getChangedInfo(@RequestParam String muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser entity = this.getBaseService().getById(muid);
		if (entity == null) {
			data.put("message", "读取失败！");
			data.put("status", 0);
		} else {
			int realNameAuthenticate = 0;
			String errorInfo = Functions.idCardValidate(entity.getIdNumber());
			if (StringUtils.isEmpty(errorInfo) && StringUtils.isNotBlank(entity.getmUserName())) {
				realNameAuthenticate = 1;
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("isAudit", entity.getIsAudit());
			result.put("realNameAuthenticate", realNameAuthenticate);
			result.put("mUserEmail", entity.getmUserEmail());
			data.put("result", result);
			data.put("message", "");
			data.put("status", 1);
		}
		return data;
	}
	
	@Resource(name = "mailSender")
	private JavaMailSender mailSender;
	@Resource(name = "appContext")
	private AppContext appContext;
	/**
	 * 绑定邮箱
	 * @param muid
	 * @param mUserEmail
	 * @return
	 */
	@RequestMapping(value="bindEmail")
	@ResponseBody
	public Map<String, Object> bindEmail(@RequestParam int muid, @RequestParam String mUserEmail, HttpServletRequest request) {
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/userInformation/activateBindEmail.shtml?" + request.getQueryString();
		Map<String, Object> data = new HashMap<String, Object>();
        MimeMessage m = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(m, true, "UTF-8");
            // 设置收件人
            helper.setTo(mUserEmail);
            // 设置发件人
            helper.setFrom(appContext.getMailFrom());
            // 设置主题
            helper.setSubject("【金朗理财】绑定邮箱");
            // 设置 HTML 内容
            helper.setText("亲爱的金朗理财用户，您好！<br />您申请绑定此邮箱到您的账户，您现在只需点击以下链接，即可完成绑定。<br />请点击该链接<a href=\"" + url + "\">绑定邮箱!</a><br />若您无法直接点击链接，也可复制以下地址到浏览器地址栏中：<br />" + StringEscapeUtils.escapeHtml4(url) + "<br />--------------------------------<br />注：此邮件由有利网系统自动发送，请勿回复<br />北京普惠宝科技有限责任公司", true);
            mailSender.send(m);
        } catch (Exception e) {
			data.put("message", "发送激活邮件失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
        }
		data.put("message", "等待邮件激活！");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 邮箱激活
	 * @param muid
	 * @param mUserEmail
	 * @param response
	 */
	@RequestMapping(value="activateBindEmail")
	@ResponseBody
	public void activateBindEmail(@RequestParam int muid, @RequestParam String mUserEmail, HttpServletResponse response) {
		MobileUser entity = new MobileUser();
		entity.setmUserId(muid);
		entity.setmUserEmail(mUserEmail);

        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        try {
			entity = this.getBaseService().update(entity);
			response.getWriter().write("邮箱激活成功！");
		} catch (Exception e) {
			try {
				response.getWriter().write("邮箱激活失败，请联系客服！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

 
	
 
	
	/**
	 * 我的财富
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="myFortune")
	@ResponseBody
	public Map<String, Object> myFortune(@RequestParam String muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> result;
		try {
			result = mobileUserService.processFortune(muid);
		} catch (Exception e) {
			log.error(e.getStackTrace());
			data.put("message", "错误！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
		
//		MobileUser user = this.getBaseService().getById(muid);
//		result.put("balance", user.getmUserMoney() - user.getFrozenMoney());
		int signinStatus = 0;
		MobileUserSignin entity = mobileUserSigninService.getById(muid);
		if (entity != null) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String signinDate = format.format(entity.getSigninDate());
			String today = format.format(new Date());
			if (signinDate.equals(today)) {
				signinStatus = 1;
			}			
		}

		result.put("signinStatus", signinStatus);
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 我的资产
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="myAsset")
	@ResponseBody
	public Map<String, Object> myAsset(@RequestParam String muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> result;
		try {
			result = mobileUserService.processmyAsset(muid);
		} catch (Exception e) {
			data.put("message", "错误！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

 
	 

	/**
	 * 开户
	 * @param entity
	 * @return
	 */
	@RequestMapping(params = "method=adminCreate", method = RequestMethod.PUT)
	@ResponseBody
	protected Map<String, Object> adminCreate(MobileUser entity) {
		Map<String, Object> results = Constants.MAP();
		try {
			mobileUserService.adminCreate(entity);
			results.put(Constants.SUCCESS, Constants.TRUE);
		} catch (Exception e) {
			results.put(Constants.SUCCESS, Constants.FALSE);
			results.put(Constants.MESSAGE, e.getMessage());
		}
		return results;
	}
	
 
	@RequestMapping(value="verifyPayPasswordForIOS")
	@ResponseBody
	public Map<String, Object> verifyPayPasswordForIOS(@RequestParam String muid, @RequestParam String payPassword) {
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser user = this.getBaseService().getById(muid);
		if (user.getPayPassword().equals(DESUtils.decrypt(payPassword))) {
			data.put("message", "");
			data.put("status", 1);
		} else {
			data.put("message", "支付密码错误！");
			data.put("status", 0);
		}
		return data;
	}
	
	
	 
		@RequestMapping(value="checkPasswordExist")
		@ResponseBody
		public Map<String, Object> checkPasswordExist(@RequestParam String muid) {
			Map<String, Object> data = new HashMap<String, Object>();
			
			try {
				MobileUser user = this.getBaseService().getById(muid);
				if (StringUtils.isEmpty(user.getPayPassword()) ) {
					data.put("message", "支付密码不存在!");
					data.put("status", 2);
				} else {
					data.put("message", "支付密码存在!");
					data.put("status", 1);
				}
				 
			} catch (Exception e) {
				data.put("message", "错误!");
				data.put("status", 0);
			 
			}
			
			

			return data;
		}
	

}
