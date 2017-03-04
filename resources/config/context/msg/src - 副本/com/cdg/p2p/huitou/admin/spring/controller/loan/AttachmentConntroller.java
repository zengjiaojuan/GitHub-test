package com.cddgg.p2p.huitou.admin.spring.controller.loan;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.p2p.core.service.AttachmentService;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Attachment;
import com.cddgg.p2p.huitou.util.DwzResponseUtil;

/**
 * 借款标附件conntroller
 * 
 * @author ransheng
 * 
 */
@Controller
@RequestMapping(value = { "attachment" })
public class AttachmentConntroller {

    /** attachmentService */
    @Resource
    AttachmentService attachmentService;

    /**
     * <p>
     * Title: openAddPage
     * </p>
     * <p>
     * Description: 打开新增或修改页面
     * </p>
     * 
     * @param id
     *            编号
     * @param type
     *            类型
     * @param request
     *            请求
     * @return 页面
     */
    @RequestMapping(value = { "openAddPage", "/" })
    public ModelAndView openAddPage(
            @RequestParam(value = "id", defaultValue = "", required = true) String id,
            @RequestParam(value = "type", defaultValue = "", required = true) String type,
            HttpServletRequest request) {

        request.setAttribute("type", type);
        request.setAttribute("id", id);
        return new ModelAndView("WEB-INF/views/admin/loansign/attachment");
    }

    /**
     * <p>
     * Title: uploadAttchment
     * </p>
     * <p>
     * Description: 借款标附件上传
     * </p>
     * 
     * @param id
     *            借款标号
     * @param type
     *            附件类型
     * @param request
     *            请求
     * @return 结果 1上传附件为空 2上传的不是图类型 3上传成功 4只能上传一张标图
     */
    @RequestMapping(value = { "uploadAttchment", "/" })
    public void uploadAttchment(
            @ModelAttribute(value = "Attachment") Attachment attachmentt,
            HttpServletRequest request,HttpServletResponse response) {
        String pageid="main"+attachmentt.getId();
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            // 新增
            Integer result = attachmentService.uploadAttchment(attachmentt.getLoansign(), attachmentt.getAttachmentType(), request);
            response.setContentType("text/html;charset=UTF-8");
            out=response.getWriter();
            if (result==3) {
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS, "上传成功", pageid, "closeCurrent");
            } else if(result==1){
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR, "请选择图片文件", pageid,  "closeCurrent");
            }else if(result==2){
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR, "请上传JPG、PNG、GIF图片类型", pageid,  "closeCurrent");
            }else if(result==4){
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR, "只能上传一张标图", pageid,"closeCurrent");
            }
            out.print(json);
        } catch (Throwable e) {
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR, "更新失败", pageid, "closeCurrent");
            out.print(json);
        }
    }

    /**
     * 删除借款标附件
     * 
     * @param id
     *            附件id
     * @param request
     *            请求
     * @return boolean
     */
    @ResponseBody
    @RequestMapping(value = { "delAttachment", "/" })
    public Boolean delAttachment(
            @RequestParam(value = "id", defaultValue = "", required = true) String id,
            HttpServletRequest request) {
        // 删除附件
        return  attachmentService.delAttachment(id, request);
    }
}
