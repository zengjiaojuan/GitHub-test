package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.CommentReply;

@Controller
@RequestMapping(value = "/commentReply")
public class CommentReplyController extends BaseController<CommentReply, String> {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "commentReplyService")
	public void setBaseService(IBaseService<CommentReply, String> baseService) {
		super.setBaseService(baseService);
	}
	
	/**
	 * 保存回复
	 * @param muid
	 * @param commentId
	 * @param parentId
	 * @param content
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam String commentId, @RequestParam String parentId, @RequestParam String content) {
		CommentReply entity = new CommentReply();
		int comment = 0;
		if (!"".equals(commentId)) {
			comment = Integer.valueOf(commentId);
		}
		entity.setCommentId(comment);
		entity.setmUserId(muid);
		entity.setContent(content);
		int parent = 0;
		if (!"".equals(parentId)) {
			parent = Integer.valueOf(parentId);
		}
		entity.setParentId(parent);
		
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

}
