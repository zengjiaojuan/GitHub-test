package com.cddgg.p2p.huitou.admin.spring.controller.loan;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.p2p.core.service.CommentService;
import com.cddgg.p2p.huitou.entity.Comment;

/**
 * 评论人回复
 * 
 * @author ransheng
 * 
 */
@Controller
@RequestMapping(value = { "comment" })
public class CommentConntroller {

    /** commentService */
    @Resource
    private CommentService commentService;

    /**
    * <p>Title: updateComment</p>
    * <p>Description:  回复评论</p>
    * @param comment 评论信息
    * @param request 请求
    * @return 是否回复成功
    */
    @ResponseBody
    @RequestMapping(value = { "updateComment", "/" })
    public boolean updateComment(
            @ModelAttribute(value = "Comment") Comment comment,
            HttpServletRequest request) {
        commentService.updateComment(comment, request);
        return true;

    }
}
