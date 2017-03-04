package com.cddgg.p2p.huitou.admin.spring.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.normal.Validate;
import com.cddgg.p2p.huitou.admin.spring.service.ColumnManageService;
import com.cddgg.p2p.huitou.admin.spring.service.column.CommonProblemsService;
import com.cddgg.p2p.huitou.entity.Article;
import com.cddgg.p2p.huitou.entity.Deputysection;
import com.cddgg.p2p.huitou.entity.Feedbacktype;

/**
 * 前台栏目管理
 * 
 * @author My_Ascii
 * 
 */
@Controller
@RequestMapping("to")
public class PageUrlController {

    /**
     * 注入ColumnManageService
     */
    @Resource
    ColumnManageService columnservice;
    /**
     * 注入CommonProblemsService
     */
    @Resource
    CommonProblemsService problemservice;
    /**
     * 注入HibernateSupport
     */
    @Resource
    HibernateSupport commondao;

    /**
     * @param page
     *            PageModel
     * @param request
     *            HttpServletRequest
     * @return 返回页面
     */
    @RequestMapping("*.htm")
    public String queryByUrl(PageModel page, HttpServletRequest request) {
        return forward(request, page, null);
    }

    /**
     * 
     * @param request
     *            HttpServletRequest
     * @param url
     *            栏目路径
     * @return String
     */
    public String forward(HttpServletRequest request, PageModel page, String url) {
        String requestURI = "";
        if (Validate.emptyStringValidate(url)) {
            requestURI = "/" + url;// 获取请求的路径
        } else {
            requestURI = request.getServletPath();// 获取请求的路径
        }
        String param = requestURI.substring(4, requestURI.indexOf(".htm"));// 截取参数部分
        String[] params = param.split("-");// 用“-”将参数分割【param1：类似；param2：一级栏目id；param3：二级栏目id；param4：文章id】
        Deputysection deputy = columnservice.queryDeputyById(Long
                .parseLong(params[2]));// 根据id查询二级栏目
        request.setAttribute("topicId", params[1]);// 一级栏目id
        request.setAttribute("deputyId", params[2]);// 二级栏目id
        request.setAttribute("deputy", deputy);
        if (params[0].equals("single")) {
            // 单页
            request.setAttribute("type", "single");
         // 邮件反馈
            if (params[2].equals("69")) {
                List<Feedbacktype> feedbacktypes = commondao
                        .find("from Feedbacktype f where f.isShow=1");
                request.setAttribute("feedbacktypes", feedbacktypes);
                return "WEB-INF/views/visitor/feedback";
            }
            if (params[2].equals("27")) {
                return "WEB-INF/views/visitor/communal/receipts";
            }
            if (params[2].equals("79")) {
                request.setAttribute("problems", problemservice.queryCommonProblems(null, null, null, null));
                return "WEB-INF/views/visitor/qAndA";
            }
            if (params[2].equals("70")) {
                return "WEB-INF/views/visitor/communal/borrow";
            }
        }
        if (params[0].equals("list")) {
            // 列表
            request.setAttribute("deputyName", deputy.getName());
            request.setAttribute("articles",
                    columnservice.queryAllArticle(page, params[2]));
            request.setAttribute("page", page);
            return "WEB-INF/views/visitor/communal/list_page";
        }
        if (params[0].equals("article")) {
            // 文章
            Article article = columnservice.queryArticleById(Long
                    .parseLong(params[3]));
            request.setAttribute("article", article);
            request.setAttribute("deputy", deputy);
            request.setAttribute("type", "article");
        }
        return "WEB-INF/views/visitor/communal/single_page";

    }
}
