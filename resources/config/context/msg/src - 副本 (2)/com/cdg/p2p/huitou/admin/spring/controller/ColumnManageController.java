package com.cddgg.p2p.huitou.admin.spring.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.normal.Validate;
import com.cddgg.p2p.huitou.admin.spring.service.ColumnManageService;
import com.cddgg.p2p.huitou.admin.spring.service.column.DeputysectionService;
import com.cddgg.p2p.huitou.entity.Article;
import com.cddgg.p2p.huitou.entity.Deputysection;
import com.cddgg.p2p.huitou.entity.Topic;
import com.cddgg.p2p.huitou.spring.util.FileUtil;

/**
 * 栏目管理
 * 
 * @author My_Ascii
 * 
 */
@Controller
@RequestMapping(value = { "admincolum" })
public class ColumnManageController {

    /**
     * 引用ColumnManageService
     */
    @Resource
    ColumnManageService columnservice;
    
    /**
     * 引用DeputysectionService
     */
    @Resource
    DeputysectionService deputyService;
    
    /**
     * topicsList 用来存放topic的集合的名称
     */
    private String topicsList = "topicsList";
    
    /**
     * page 分页
     */
    private String page = "page";
    
   /**
    * 当存放一级栏目的下拉列表值改变时查询该一级栏目下栏目类型为‘列表’的二级栏目
    * @param id 文章id
    * @param request 请求
    * @return 返回数据和页面
    */
    @RequestMapping("/topicSelectChange")
    @ResponseBody
    public List<Deputysection> topicSelectChange(String id, HttpServletRequest request) {
        List<Deputysection> list = columnservice.queryDeputyByTid(id.equals("") ? 0 : Long
                .parseLong(id));
        return list;
    }

    /**
     * 上传图片
     * @param request 请求
     * @return 返回数据
     * @throws IOException 文件相关操作抛出的异常
     */
    @RequestMapping("/uploadFile")
    @ResponseBody
    public Map<String, Object> uploadFile(HttpServletRequest request)
            throws IOException {
        // 上传的绝对路径
        String realPath = request.getSession().getServletContext()
                .getRealPath("");
        String filePath = realPath + "/upload/default/columnimg/";
        // 可上传类型
        String[] types = FileUtil.IMAGE_TYPES;
        // 执行上传操作
        File file = FileUtil.upload(request, filePath, types, "filedata",
                DateUtils.format("yyyyMMddHHmmss"));
        // 定义一个map来存放要返回的结果
        Map<String, Object> imgMap = new HashMap<String, Object>();
        imgMap.put("err", "");// 操作失败,返回失败消息
        imgMap.put("msg", "/upload/default/columnimg/" + file.getName());// 操作成功，返回文件路径
        return imgMap;
    }

}
