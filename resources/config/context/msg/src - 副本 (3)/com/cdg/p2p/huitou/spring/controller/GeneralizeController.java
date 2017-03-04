package com.cddgg.p2p.huitou.spring.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.util.StringUtil;
import com.cddgg.p2p.huitou.admin.spring.service.UserInfoServices;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.service.GeneralizeService;
import com.cddgg.p2p.huitou.util.GenerateLinkUtils;

/**   
 * Filename:    GeneralizeController.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年2月13日 下午1:51:31   
 * Description:  会员推广控制层
 *    
 */

/**
 * <p>
 * Title:GeneralizeController
 * </p>
 * <p>
 * Description: 会员推广信息控制层
 * </p>
 *         <p>
 *         date 2014年2月13日
 *         </p>
 */
@Controller
@RequestMapping("/generalize")
public class GeneralizeController {
    /** 引入log4j日志打印类 */
    private static final Logger LOGGER = Logger
            .getLogger(GeneralizeController.class);

    /** 注入后台会员服务层 */
    @Resource
    private UserInfoServices userInfoServices;

    /** 会员推广信息服务层 */
    @Resource
    private GeneralizeService generaLizeService;

    /**
     * <p>
     * Title: getPromoteLinks
     * </p>
     * <p>
     * Description: 生成当前登录会员的推广链接
     * </p>
     * 
     * @param request
     *            HttpServletRequest
     * @return 会员推广链接展示页面
     */
    @RequestMapping("/get_promote_links")
    public String getPromoteLinks(HttpServletRequest request) {

        Userbasicsinfo userbasic = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);

        String str = "";
        if(null!=userbasic){
            str = StringUtil.generatePassword("TG-" + userbasic.getId());

            try {
                str = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("对加密字符串进行URL编码失败！", e);
            }

            request.setAttribute("strurl",
                    GenerateLinkUtils.getServiceHostnew(request)
                            + "generalize/regist?member=" + str);

        }else{
            request.setAttribute("strurl","请您重新登录后获取");
        }
        
        return "/WEB-INF/views/member/generalize";
    }

    /**
     * <p>
     * Title: showregist
     * </p>
     * <p>
     * Description: 解析会员推广链接
     * </p>
     * 
     * @param member
     *            加密后的会员编号
     * @param request
     *            HttpServletRequest
     * @return 注册页面
     * @throws Exception
     */
    @RequestMapping("/regist")
    public String showregist(String member, HttpServletRequest request) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("showregist(String, HttpServletRequest)方法开始"); //$NON-NLS-1$
        }

        String starStr = member;
        // 判断是否为推广连接
        if (StringUtil.isNotBlank(member) && member.trim().length() > 3) {

            // 解密推广链接
            if ((member.indexOf("+") == -1 || member.indexOf("/") == -1)
                    && member.indexOf("=") == -1) {
                try {
                    member = StringUtil.correctPassword(URLDecoder.decode(
                            member, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(
                            "showregist(String member=" + member + ", HttpServletRequest request=" + request + ")导致抛异常的字符串是:" + starStr, e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
            } else {
                member = StringUtil.correctPassword(member);
            }

            // 获取推广人编号
            member = member.substring(3);


            // 判断编号是否正确
            if (StringUtil.isNumberString(member)) {

                Userbasicsinfo genuser = userInfoServices
                        .queryBasicsInfoById(member);

                if (genuser != null) {
                    // 将推广人保存到session中
                    request.getSession().setAttribute("generuser", genuser);
                }

            }

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("showregist(String, HttpServletRequest)方法结束OUTPARAM=/WEB-INF/views/visitor/regist"); //$NON-NLS-1$
        }
        return "/WEB-INF/views/visitor/regist";

    }

    /**
     * <p>
     * Title: queryGenlizePage
     * </p>
     * <p>
     * Description: 查询当前登录人的推广信息
     * </p>
     * 
     * @param request
     *            HttpServletRequest
     * @param page
     *            分页信息
     * @return 数据展示页面
     */
    @RequestMapping("/generalize_list")
    public String queryGenlizePage(HttpServletRequest request, PageModel page) {

        // 获取当前登录人
        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);

        List generlist = null;

        // 判断当前是否有人登录
        if (null != user) {
            // 查询推广信息
            generlist = generaLizeService.queryGenlizePage(user.getId() + "",
                    page);
        }

        // 保存查询结果
        request.setAttribute("generlist", generlist);

        // 保存分页信息
        request.setAttribute("page", page);
        
        //
        request.setAttribute("gen", "gen");

        return "/WEB-INF/views/member/generalizelist";
    }
    
    /**
    * <p>Title: queryGmoneyPage</p>
    * <p>Description: 会员推广奖金查询</p>
    * @param request HttpServletRequest
    * @param page 分页参数
    * @return
    */
    @RequestMapping("/genmoney_list")
    public String queryGmoneyPage(HttpServletRequest request, PageModel page){
        

        // 获取当前登录人
        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);

        List generlist = null;

        // 判断当前是否有人登录
        if (null != user) {
            // 查询推广信息
            generlist = generaLizeService.querygenMoenyPage(user.getId() + "",
                    page);
        }

        // 保存查询结果
        request.setAttribute("gmoney", generlist);
        // 保存分页信息
        request.setAttribute("page", page);
        
        //会员推广奖金
        request.setAttribute("gen", "gmoney");
        
        return "/WEB-INF/views/member/generalizelist";
    }

}
