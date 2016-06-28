package com.idp.security.web.interceptor;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.idp.pub.constants.Constants;
import com.idp.security.web.pub.IConstant;
import com.idp.security.web.utils.SecurityUtils;
import com.phb.puhuibao.common.InitListener;

/**
 * 访问身份验证拦截器
 * 
 * @author panfei
 * 
 */
public class SecurityInterceptor implements HandlerInterceptor {
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private static String TOKEN = "phb";
	
	private List actions;

	public List getActions() {
		return actions;
	}

	public void setActions(List actions) {
		this.actions = actions;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (actions.contains(request.getServletPath())) {
			response.sendRedirect(request.getContextPath() + "/appContext.shtml?method=processInterceptor");
			return false;
		}
		if("/alipayApi/async.shtml".equals(request.getServletPath())){//特殊处理
			return true;
		}
		if(InitListener.sessionhash.size()==0){
			String sql= "select  user_id,session_id from phb_muser_session";
			List <Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
			for (Map<String, Object> map : list) {
				InitListener.sessionhash.put((Integer) map.get("user_id"), (Integer)map.get("session_id"));
				 
			}
		}
		//保证只有一个用户登录
		String tokenstr = request.getParameter("token");
		String muidstr = request.getParameter("muid");
		int token = 0;
		int muid = 0;
		if(!StringUtils.isEmpty(muidstr)){
		 
			 
			if(!StringUtils.isEmpty(tokenstr)){
				token = Integer.parseInt(tokenstr);
			}
			
			muid = Integer.parseInt(muidstr);
			if(!InitListener.sessionhash.containsKey(muid)){ // 用户没有登陆或者没有注册
				response.sendRedirect(request.getContextPath() + "/appContext.shtml?method=wronglogin");
				return false;
			}
			if(InitListener.sessionhash.get(muid) != token){ // 用户重复登陆
				response.sendRedirect(request.getContextPath() + "/appContext.shtml?method=wronglogin");
				return false;
			}
		}
		
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		if (signature!= null && timestamp != null) {
			if (new Date().getTime() - Long.parseLong(timestamp) < 99999999999l) {
				if (signature.equals(getMD5Str(TOKEN + timestamp))) {
					return true;
				}
			} else {
				response.sendRedirect(request.getContextPath() + "/appContext.shtml?method=processInterceptor");
				return false;
			}
		}
		


		if (SecurityUtils.isLogInSystem(request)) {
			return true;
		} else {
			String acceptType = request.getHeader("Accept");
			if (acceptType != null && acceptType.indexOf("json") != -1) {
				response.setContentType("application/json;charset=utf-8");
				// response.setCharacterEncoding("");
				Map<String, Object> results = Constants.MAP();
				results.put(Constants.SUCCESS, Constants.FALSE);
				results.put(Constants.MESSAGE, IConstant.LOGERRO_MSG);
				results.put(Constants.ERROR_CODE, 1);
				ObjectMapper jsonmapper = new ObjectMapper();
				jsonmapper.writeValue(response.getOutputStream(), results);
				response.flushBuffer();
			} else {
				response.sendRedirect(request.getContextPath()
						+ IConstant.LOGIN_PAGE);
			}
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
	
    private String getMD5Str(String str) {  
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();  
        StringBuffer md5StrBuff = new StringBuffer();  
        for (int i = 0; i < byteArray.length; i++) {  
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
        return md5StrBuff.toString();  
    }
}
