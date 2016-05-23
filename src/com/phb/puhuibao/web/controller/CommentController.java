package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.Comment;
import com.phb.puhuibao.entity.CommentReply;

@Controller
@RequestMapping(value = "/comment")
public class CommentController extends BaseController<Comment, String> {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "commentService")
	public void setBaseService(IBaseService<Comment, String> baseService) {
		super.setBaseService(baseService);
	}

	@javax.annotation.Resource(name = "resourceService")
	private IBaseService<Resource, String> resourceService;

	@Resource(name = "commentReplyService")
	private IBaseService<CommentReply, String> commentReplyService;

	/**
	 * 我发出的评论
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String resourceId) {
		Pager<Comment> pager = new Pager<Comment>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceId", resourceId);
		params.put("orderBy", "create_time desc");
		Pager<Comment> p = this.getBaseService().findByPager(pager, params);
		for (Comment comment : p.getData()) {
			params = new HashMap<String, Object>();
			params.put("commentId", comment.getCommentId());
			List<CommentReply> list = commentReplyService.findList(params);
			comment.setReplyList(list);
			recursion(list);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	private void recursion(List<CommentReply> list) {
		for (CommentReply reply : list) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("parentId", reply.getReplyId());
			List<CommentReply> replys = commentReplyService.findList(params);
			reply.setChildReplyList(replys);
			recursion(replys);
		}			
	}

	/**
	 * 保存评论
	 * @param muid
	 * @param resourceId
	 * @param content
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam int resourceId, @RequestParam String content) {
		Comment entity = new Comment();
		entity.setmUserId(muid);
		entity.setResourceId(resourceId);
		entity.setContent(content);
		
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="deleteComment")
	@ResponseBody
	public Map<String, Object> deleteComment(@RequestParam int commentId) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("commentId", commentId);
		int count = this.getBaseService().delete(params);
		Map<String, Object> data = new HashMap<String, Object>();
		if (count == 0) {
			data.put("message", "已经删除!");
			data.put("status", 0);
		} else {
			data.put("message", "删除成功！");			
			data.put("status", 1);
		}
		return data;
	}
}
