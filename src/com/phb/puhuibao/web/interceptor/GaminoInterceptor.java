package com.phb.puhuibao.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.entity.CalculateFormula;
import com.phb.puhuibao.entity.ExperienceValue;
import com.phb.puhuibao.entity.UserCard;
import com.phb.puhuibao.web.controller.UserAccountController;

public class GaminoInterceptor implements HandlerInterceptor {
	private static final Log LOG = LogFactory.getLog(UserAccountController.class);

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@javax.annotation.Resource(name = "mobileUserService")
  
	private IBaseService<UserCard, String> userCardService;
 
	
	@javax.annotation.Resource(name = "calculateFormulaService")
	private IBaseService<CalculateFormula, String> calculateFormulaService;
	
	@javax.annotation.Resource(name = "experienceValueService")
	private IBaseService<ExperienceValue, String> experienceValueService;

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception)
			throws Exception { 

//		String muid = request.getParameter("muid");
	// 
//			LOG.error("muid is null!");
//			return;
	// 
		
	}

 
	
	
	
	
	

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
