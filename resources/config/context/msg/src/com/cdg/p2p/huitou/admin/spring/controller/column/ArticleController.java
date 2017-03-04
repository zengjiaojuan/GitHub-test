package com.cddgg.p2p.huitou.admin.spring.controller.column;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.normal.Validate;
import com.cddgg.p2p.huitou.admin.spring.service.ColumnManageService;
import com.cddgg.p2p.huitou.admin.spring.service.column.ArticleService;
import com.cddgg.p2p.huitou.entity.Article;

@Controller
@RequestMapping(value = { "article" })
public class ArticleController {
    
    /*** 引用ColumnManageService*/
    @Resource
    ColumnManageService columnservice;
    
    /*** 引用ArticleService*/
    @Resource
    ArticleService articleService;
    
    /*** page 分页*/
    private String page = "page";

    /**
     * 打开文章管理页面
     * 
     * @param pageModel 分页
     * @param title 文章标题
     * @param showStatus 是否显示
     * @param recommendStatus 是否推荐
     * @param deputyId 二级栏目名称
     * @param request 请求
     * @return 返回数据和页面
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = { "openArticles", "" })
    public ModelAndView openArticles(PageModel pageModel, String title,
            String showStatus, String recommendStatus, String deputyId,
            HttpServletRequest request) throws UnsupportedEncodingException {
        //对get提交的参数乱码进行处理
        if(request.getMethod().equals("GET")){
            if(Validate.emptyStringValidate(title)){
                title = new String(title.getBytes("iso-8859-1"),"UTF-8");
            }
            if(Validate.emptyStringValidate(deputyId)){
                deputyId = new String(deputyId.getBytes("iso-8859-1"),"UTF-8");
            }
        }
        request.setAttribute("articleList", articleService.queryAllArticles(pageModel,
                title, showStatus, recommendStatus, deputyId));
        request.setAttribute(page, pageModel);
        request.setAttribute("deputysections",
                columnservice.queryAllDeputysections());
        request.setAttribute("title", title);
        request.setAttribute("showStatus", showStatus == "" ? "-1" : showStatus);
        request.setAttribute("recommendStatus", recommendStatus == "" ? "-1"
                : recommendStatus);
        request.setAttribute("deputyId", deputyId);
        return new ModelAndView("WEB-INF/views/admin/column/deputylist2");
    }
    
    /**
     *  根据id查询文章详情
     * @param id 文章id
     * @param operation 当前所做的操作（add:添加; upt:修改）
     * @param request 请求
     * @return 返回数据和页面
     */
    @RequestMapping("/queryArticleById")
    public ModelAndView queryArticleById(long id, String operation,
            HttpServletRequest request) {
        request.setAttribute("topics", columnservice.queryAllTopics());
        request.setAttribute("operation", operation);
        if (operation.equals("upt")) {
            Article article = articleService.queryArticleById(id);
            request.setAttribute("article", article);
            request.setAttribute("topicId", article.getDeputysection()
                    .getTopic().getId());
        }
        return new ModelAndView("WEB-INF/views/admin/column/add_upt_article");
    }

    /**
     * 添加/修改文章
     * @param id 文章id
     * @param operation operation 当前所做的操作（add:添加; upt:修改）
     * @param article 文章
     * @param request 请求
     * @return 返回数据
     */
    @RequestMapping("/addOrUpdateArticle")
    @ResponseBody
    public JSONObject addOrUpdateArticle(long id, String operation,
            Article article, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            if (article.getIsShow() == null) {
                article.setIsShow(0);
            }
            if (article.getIsRecommend() == null) {
                article.setIsRecommend(0);
            }
            if (operation.equals("upt")) {
                articleService.updateArticle(article);
            } else if (operation.equals("add")) {
                article.setCreateTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
                articleService.addArticle(article);
            }
            //重置application
            columnservice.resetApplaction(request);
            return columnservice.setJson(json, "200", "更新成功", "main10", "closeCurrent");

        } catch (Exception e) {
            e.printStackTrace();
            return columnservice.setJson(json, "300", "更新失败", "main10", "closeCurrent");
        }
    }

    
    /**
     * 删除文章
     * @param ids 要删除的文章的id
     * @param request 请求
     * @return 返回数据
     */
    @RequestMapping("/deleteArticle")
    @ResponseBody
    public JSONObject deleteArticle(String ids, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            columnservice.deleteMany(Article.class, ids);
            columnservice.resetApplaction(request);
            return columnservice.setJson(json, "200", "更新成功", "main10", "");
        } catch (Exception e) {
            return columnservice.setJson(json, "300", "更新失败", "main10", "");
        }
    }
    
}
