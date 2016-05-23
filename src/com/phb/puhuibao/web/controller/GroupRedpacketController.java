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

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.GroupChat;
import com.phb.puhuibao.entity.GroupRedpacket;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MuserFollow;
import com.phb.puhuibao.entity.RedpacketReceive;

@Controller
@RequestMapping(value = "/groupRedpacket")
public class GroupRedpacketController extends BaseController<GroupRedpacket, String> {
	 
    
	@Resource(name = "groupRedpacketService")
	public void setBaseService(IBaseService<GroupRedpacket, String> baseService) {
		super.setBaseService(baseService);
	}
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;
	
	@Resource(name = "groupChatService")
	private IBaseService<GroupChat, String> groupChatService;
	
	
	
	 
	
	@Resource(name = "redpacketReceiveService")
	private IBaseService<RedpacketReceive, String> redpacketReceiveService;
	
	@Resource(name = "muserFollowService")
	private IBaseService<MuserFollow, String> muserFollowService;
	
	
	
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	
 

/**
 * 获取红包信息
 * 
 */
@RequestMapping(value="getGroupRedpocket")
@ResponseBody
public Map<String, Object> getGroupRedpocket(@RequestParam int muid,@RequestParam String redpocketId) {
	Map<String, Object> data = new HashMap<String, Object>();
	try {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("redpacketId", redpocketId);
		GroupRedpacket gr  = this.getBaseService().unique(params);
 
		Map<String, String> result = getUserName(muid,gr.getRedpacketUser());  
		Map<String, Object> para=new HashMap<String, Object>();
		params.put("receiveRedpacket", gr.getRedpacketId());
		
		List<RedpacketReceive> receivers =  redpacketReceiveService.findList(para);
		for( int i=0;i<receivers.size();i++){
			Map<String,String> tempuser = getUserName(muid,receivers.get(i).getReceiveUser()) ;
			receivers.get(i).setReceiverName(tempuser.get("nickname") );
			receivers.get(i).setReceiverPhoto (tempuser.get("photo") );
		}
 
		 data.put("redpocket", gr);
		 data.put("owner", result);
		 data.put("receivers", receivers);
		 data.put("status", 1);
		return data;
	} catch (Exception e) {
		data.put("message", e.getLocalizedMessage());
		data.put("status", 0);
		return data;
	}	
}

/**
 * 新建红包
 * 
 */

 

@RequestMapping(value="saveGroupRedpocket")
@ResponseBody
public Map<String, Object> saveGroupRedpocket( @RequestParam int muid,
				                               String desc,
				                               @RequestParam int number,
				                               @RequestParam double amount, 
				                               int type 
		                              ) {
	Map<String, Object> data = new HashMap<String, Object>();
 
	try {
 
		GroupRedpacket entity = new GroupRedpacket();
		entity.setRedpacketAmount(amount);
		entity.setRedpacketDesc(desc);
		entity.setRedpacketNumber(number);
		entity.setRedpacketType(type);
		entity.setRedpacketUser(muid);
 
		entity = this.getBaseService().save(entity);
 
		data.put("result", entity);
		data.put("message", "");
		data.put("status", 1);
		return data;
	} catch (Exception e) {
		data.put("message", "网络异常！");
		data.put("status", 0);
		return data;
	}	
}



public Map<String, String> getUserName(@RequestParam int muid, @RequestParam int fmuid) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("muserId", muid);
	params.put("followUser", fmuid);
	MuserFollow follow = muserFollowService.unique(params);
	Map<String, String> result = new HashMap<String, String>();
  
	if (follow == null) {
		MobileUser friend = mobileUserService.getById(fmuid+"");
		if(friend != null){
			result.put("nickname", friend.getmUserName());
			result.put("photo", friend.getPhoto());
		}
		 
	} else {
		result.put("nickname", follow.getNickname());
		result.put("photo", follow.getPhoto());
	}
	return result;
}

 
	
 
	 

	 
	
 
}
