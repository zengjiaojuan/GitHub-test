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
import com.phb.puhuibao.entity.UserMessage;

@Controller
@RequestMapping(value = "/userMessage")
public class UserMessageController extends BaseController<UserMessage, String> {
	@Override
	@Resource(name = "userMessageService")
	public void setBaseService(IBaseService<UserMessage, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String muid) {
		Pager<UserMessage> pager = new Pager<UserMessage>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("orderBy", "create_time");
		params.put("order", "desc");
		Pager<UserMessage> p = this.getBaseService().findByPager(pager, params);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
	
	@RequestMapping(value="haveUnreadMsg")
	@ResponseBody
	public Map<String, Object> haveUnreadMsg( @RequestParam String muid) {
		String sql="SELECT count(1) c from  phb_muser_message where  m_user_id = "+muid+" and is_read=0 ";
		
		Map<String, Object> data = new HashMap<String, Object>();
		long msgcount=0;
		List<Map<String, Object>> l = this.jdbcTemplate.queryForList(sql);
		for(int i=0;i<l.size();i++){
			msgcount += (long)l.get(i).get("c");
		}
		data.put("count", msgcount);
		data.put("status", 1);
		return data;
	}
	
	

	@RequestMapping(value="read")
	@ResponseBody
	public Map<String, Object> read(@RequestParam int messageId) {
		UserMessage um= new UserMessage();
		um.setMessageId(messageId);
		um.setIsRead(1);//默认为0  点击阅读后为1
		UserMessage u =  this.getBaseService().update(um);
		Map<String, Object> data = new HashMap<String, Object>();
		if (u == null) {
			data.put("message", "阅读失败！");
			data.put("status", 0);
		} else {
			data.put("message", "阅读成功！");
			data.put("status", 1);
		}
		return data;
	}
	
 
	/**
	获取系统消息,好友申请,任务消息里是否有未读的消息的标记
	* @param  muid 用户id
	*/
	@RequestMapping(value="msgisread")
	@ResponseBody
	public Map<String, Object> msgisread(@RequestParam int muid) {
	
	String sql="SELECT count(1) c from  phb_muser_message where  m_user_id = "+muid+" and is_read=0 union SELECT count(1) c from  phb_friend_invite where  to_user = "+muid+" and is_accepted=0 union SELECT count(1) c from  phb_resource_notice where  notice_user = "+muid+" and is_read=0";
	
	Map<String, Object> data = new HashMap<String, Object>();
	long msgcount=0;
	try {
		List<Map<String, Object>> l = this.jdbcTemplate.queryForList(sql);
		for(int i=0;i<l.size();i++){
			msgcount += (long)l.get(i).get("c");
		}
		data.put("result", msgcount);
		data.put("message", "");
		data.put("status", 1);
		return data;
	
	} catch (Exception e) {
		data.put("message", "网络异常！");
		data.put("status", 0);
		return data;
	}
	
	}
	
	@RequestMapping(value="delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam String messageIds) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("messageIds", messageIds);
		int count = this.getBaseService().delete(params);
		Map<String, Object> data = new HashMap<String, Object>();
		if (count == 0) {
			data.put("message", "删除失败！");
			data.put("status", 0);
		} else {
			data.put("message", "删除成功！");
			data.put("status", 1);
		}
		return data;
	}

}
