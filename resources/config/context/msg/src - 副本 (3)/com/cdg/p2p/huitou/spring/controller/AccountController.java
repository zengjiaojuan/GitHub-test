package com.cddgg.p2p.huitou.spring.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.base.model.PageModel;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.AccountService;

/**   
 * Filename:    AccountController.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年2月20日 下午4:16:37   
 * Description:  
 *   
 */

/**
* <p>Title:AccountController</p>
* <p>Description: 前台会员资金明细控制层</p>
* <p>date 2014年2月20日</p>
*/
@Controller
@RequestMapping("/member_account")
@CheckLogin(value=CheckLogin.WEB)
public class AccountController {

    /** 资金明细服务层*/
    @Resource
    private AccountService accountService;
    
    /**
    * <p>Title: queryPage</p>
    * <p>Description: </p>
    * @param starttime 开始时间
    * @param endtime 结束时间
    * @param request HttpServletRequest
    * @param page 分页信息
    * @return 数据展示页面
    */
    @RequestMapping("/query_page")
    public String queryPage(String starttime,String endtime,HttpServletRequest request,PageModel page){
        
        //获取当前登录人
        Userbasicsinfo userBasic= (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
        
        request.setAttribute("accountlist", accountService.queryPage(starttime, endtime, page, userBasic));
        
        request.setAttribute("page", page);
        
        return "/WEB-INF/views/member/money_record";
        
    }
    
}
