package com.cddgg.p2p.huitou.admin.spring.controller.column;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.p2p.huitou.admin.spring.service.ColumnManageService;
import com.cddgg.p2p.huitou.admin.spring.service.column.TopicService;
import com.cddgg.p2p.huitou.entity.Topic;

/**
 * 一级栏目管理
 * @author My_Ascii
 *
 */
@Controller
@RequestMapping(value = { "topic" })
public class TopicController {

    /*** 注入TopicService*/
    @Resource
    TopicService topicService;
    
    /*** 引用ColumnManageService*/
    @Resource
    ColumnManageService columnservice;
    
    /*** topicsList 用来存放topic的集合的名称*/
    private String topicsList = "topicsList";
    
    /*** page 分页*/
    private String page = "page";
    
    /**
     * 查询所有一级栏目
     * 
     * @param pageModel 分页
     * @param request 请求
     * @return 返回页面
     */
    @RequestMapping(value = { "queryAllTopics", "" })
    public ModelAndView queryAllTopics(PageModel pageModel,
            HttpServletRequest request) {
        request.setAttribute(topicsList, topicService.queryAllTopics(pageModel));
        request.setAttribute(page, pageModel);
        return new ModelAndView("WEB-INF/views/admin/column/topiclist");
    }

    /**
     * 跳转到添加/修改一级栏目的页面
     * @param topicId 一级栏目id
     * @param operation add/upt
     * @param request HttpServletRequest
     * @return ModelAndView
     */
    @RequestMapping("/forwardAddOrUpt")
    public ModelAndView queryTopicById(long topicId, String operation, HttpServletRequest request) {
        if(operation.equals("upt")){
            request.setAttribute("topic", topicService.queryTopicById(topicId));
        }
        request.setAttribute("operation", operation);
        return new ModelAndView("WEB-INF/views/admin/column/add_upt_topic");
    }
    
    /**
     * 新增/修改一级栏目
     * @param topic 一级栏目
     * @param operation add/upt
     * @param request HttpServletRequest
     * @return JSONObject
     */
    @RequestMapping("/add_upt_topic")
    @ResponseBody
    public JSONObject update(@ModelAttribute("Topic") Topic topic, String operation,
            HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            if (topic.getIsShow() == null) {
                topic.setIsShow(0);
            }
            if (topic.getIsfooter() == null) {
                topic.setIsfooter(0);
            }
            if(operation.equals("add")){
                topicService.addTopic(topic);
            } else if(operation.equals("upt")){
                topicService.updateTopic(topic);
            }
            //重置application
            columnservice.resetApplaction(request);
            return columnservice.setJson(json, "200", "更新成功", "main7", "closeCurrent");
        } catch (Exception e) {
            e.printStackTrace();
            return columnservice.setJson(json, "300", "更新失败", "main7", "closeCurrent");
        }
    }
    
    /**
     * 删除一级栏目
     * @param ids 被选中一级栏目的id
     * @param request HttpServletRequest
     * @return JSONObject
     */
    @RequestMapping("/deleteTopics")
    @ResponseBody
    public JSONObject deleteTopics(String ids, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            columnservice.deleteMany(Topic.class, ids);
          //重置application
            columnservice.resetApplaction(request);
            return columnservice.setJson(json, "200", "更新成功", "main7", "");
        } catch (Exception e) {
            return columnservice.setJson(json, "300", "更新失败", "main7", "");
        }
    }
}
