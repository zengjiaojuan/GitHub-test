package com.cddgg.p2p.huitou.spring.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.CollectRecord;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.CollectRecordService;

/**
 * 收藏记录
 * @author Administrator
 *
 */
@Controller
@CheckLogin(value=CheckLogin.WEB)
@RequestMapping(value = { "collectlist", "/" })
public class CollectRecordController {
	
	/**
	 * 收藏记录接口
	 */
	@Resource
	private CollectRecordService collectRecordService;
	
    /**
     * 跳转到收藏列表
     * @return
     */
    @RequestMapping("/collect")
    public ModelAndView collectList(
    		@ModelAttribute("PageModel") PageModel page,
    		HttpServletRequest request){
    	//获取当前用户信息
    	Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
    	//查询用户的收藏记录条数
    	Object obj=collectRecordService.queryCollectCount(user.getId());
    	page.setTotalCount(Integer.parseInt(obj.toString()));
    	//查询用户的收藏记录
    	List collects=collectRecordService.queryUserCollect(user.getId(), page);
    	request.setAttribute("page", page);
    	request.setAttribute("collects", collects);
    	return new ModelAndView("/WEB-INF/views/member/collect_list");
    }
    
    /**
     * 删除记录
     * @param page分页信息
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/deleteCollect")
    public ModelAndView deleteCollect(
    		@ModelAttribute("PageModel") PageModel page,
    		@RequestParam(value = "id", defaultValue = "", required = false) Long id,
    		HttpServletRequest request){
    	try {
    		collectRecordService.deleteCollect(id);
    	} catch(Throwable e) {
    		e.getMessage();
    	}
    	return collectList(page, request);
    }
    
    /**
     * 添加收藏记录
     * @param id 借款标id
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/addCollect")
    public String addCollect(
    		@RequestParam(value = "id", defaultValue = "", required = false)Long id,
    		HttpServletRequest request){
    	boolean flag=false;
    	Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
    	Loansign loansign=new Loansign(id);
    	CollectRecord collectRecord=new CollectRecord(loansign,user,DateUtils.format(null));
    	flag=collectRecordService.addCollect(collectRecord);
    	if(flag){
    		return "1";
    	}
    	return "0";
    }
    
    /**
     * 判断是否收藏过
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/checkCollect")
    @ResponseBody
    public String checkCollect(Long id,HttpServletRequest request){
    	boolean flag=false;
    	Userbasicsinfo user=(Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
    	Loansign loansign=new Loansign(id);
    	CollectRecord collectRecord=new CollectRecord(loansign,user,DateUtils.format(null));
    	flag=collectRecordService.checkCollect(collectRecord);
    	if(flag){
    		return "0";
    	}
    	return "1";
    }
}
