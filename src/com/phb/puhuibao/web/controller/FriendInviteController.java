package com.phb.puhuibao.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.opensymphony.oscache.util.StringUtil;
import com.phb.puhuibao.entity.FriendInvite;
import com.phb.puhuibao.entity.MuserFollow;
import com.phb.puhuibao.service.impl.MuserFollowServiceImpl;

import org.springframework.jdbc.core.JdbcTemplate;

@Controller
@RequestMapping(value = "/friendInvite")
public class FriendInviteController extends BaseController<FriendInvite, String> {
	private static final Log log = LogFactory.getLog(FriendInviteController.class);
	
	@Resource(name = "friendInviteService")
	public void setBaseService(IBaseService<FriendInvite, String> baseService) {
		super.setBaseService(baseService);
	}
 
	@Resource(name = "muserFollowService")
	private MuserFollowServiceImpl muserFollowService;
 
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	
	
	/**
	    添加好友申请
	* @param muid 当前用户 id
	* @param fmuid 关注用户id
	        把当前用户的名字也插入是为了 sql方便
	*/
	
	@RequestMapping(value="friendAdd")
	@ResponseBody
	public Map<String, Object> friendAdd(@RequestParam int myid,@RequestParam int tomuid, @RequestParam String myname) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("fromUser", myid);
		params.put("toUser", tomuid);
		params.put("isAccepted", 0);
		FriendInvite fi = this.getBaseService().unique(params);
		if(fi !=null ){// 已经申请过了,对方还没有接受,限制添加新的申请
			data.put("result", "");
			data.put("message", "保存成功！");
			data.put("status", 1);
			return data;
		}
 
		FriendInvite entity = new FriendInvite();
		entity.setFromUser(myid);
		entity.setFromUsername(myname);
		entity.setToUser(tomuid);
		
		try {
		     this.getBaseService().save(entity);
		}  catch (Exception e) {
		data.put("message", "网络错误！");
		data.put("status", 0);
		return data;
		}
		data.put("result", "");
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	
	/**
                       当前用户的好友申请信息
                        把申请用户的名字也插入是为了 做查询功能方便，不用leftjoin 一张大表
	 */

	@RequestMapping(value="getInvites")
	@ResponseBody
	public Map<String, Object> getInvites(@RequestParam int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("toUser", muid);
		
		try {
			List<FriendInvite> friendInvites = this.getBaseService().findList(params);
			data.put("result", friendInvites);
			data.put("message", "");
			data.put("status", 1);
			return data;
		
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}
		
		
		 
	}
 
	/**
                    点击 "接受"
	 */

	@RequestMapping(value="accepted")
	@ResponseBody
	public Map<String, Object> accepted(@RequestParam int inviteId,@RequestParam int myid,@RequestParam int friendid, @RequestParam String friendname, @RequestParam String myname) {
		Map<String, Object> data = new HashMap<String, Object>();
		int savefriendship= this.savefriendship(myid, friendid, friendname, myname);
		
		if(savefriendship==1){
			FriendInvite entity = new FriendInvite();
			entity.setInviteId(inviteId);
			entity.setIsAccepted(1);
			this.getBaseService().update(entity);
			data.put("result", "");
			data.put("message", "保存成功！");
			data.put("status", 1);
		}else if(savefriendship==0){
			data.put("result", "");
			data.put("message", "网络错误！");
			data.put("status", 0);
		}else if(savefriendship==2){
			data.put("result", "");
			data.put("message", "已经是好友！");
			data.put("status", 2);
		}else if(savefriendship==3){
			data.put("result", "");
			data.put("message", "对方已经将你拉黑！");
			data.put("status", 3);
		}else if(savefriendship==4){
			data.put("result", "");
			data.put("message", "请取消对他拉黑！");
			data.put("status", 4);
		} 
		
		 
		return data;
	}
	
	
	public int savefriendship(@RequestParam int muid,@RequestParam int fmuid, @RequestParam String followUname, @RequestParam String myname) {
		MuserFollow entity1 = new MuserFollow();
		MuserFollow entity2 = new MuserFollow();
		entity1.setMuserId(muid);
		entity1.setFollowUser(fmuid);
		entity1.setFollowUname(followUname);
		entity1.setFollowTime(new Date());
		entity2.setMuserId(fmuid);
		entity2.setFollowUser(muid);
		entity2.setFollowUname(myname);
		entity2.setFollowTime(new Date());
		
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			int status1 = this.followingStatus(muid, fmuid);
			int status2 = this.followingStatus(fmuid, muid);
			if(status1==0 && status2==0 ){////互为陌生人
				muserFollowService.save(entity1);
				muserFollowService.save(entity2);
				return 1;
			}else if(status1==0 && status2==1 ){//对方对我关注了,我没有对对方关注
				muserFollowService.save(entity1);
				return 1;
			} else if(status1==1 && status2==0 ){//已经关注对方 但是对方没有关注我
				muserFollowService.save(entity2);
				return 1;
			}else if(status1==1 && status2==1 ){//
				data.put("message", "已经是好友！");
				 
				return 2;
				
			}else if(status2==2 ){//
				data.put("message", "对方已将你拉黑！");
				 
				return 3;
				
			}else if(status1==2  ){//
				data.put("message", "请先取消对他拉黑！");
				 
				return 4;
				
			}

		} catch (Exception e) {
		
			return 0;
		}
		return 1;
		 
	}
	
	
	
	public int followingStatus(@RequestParam int muid,@RequestParam int fmuid) {
	Map<String,Object> params = new HashMap<String,Object>();
 
	int sataus = 0;
	 
	params.put("muserId", muid);
	params.put("followUser", fmuid);
 
		MuserFollow status = muserFollowService.unique(params);
		if(status==null){//没有关注关系
			sataus= 0;
		}else{//关注关系
			if(status.getFollowIsblocked()!=null){// 0 或者1
				if("1".equals(status.getFollowIsblocked().toString()) ){//muid 对 fmuid 进行了拉黑
					sataus= 2;
				} 
			}else{//默认的空值,相当于只进行了关注
				sataus= 1;
			}
		}
		 
		return sataus;
 
	}
	
	
	/**
                    删除 申请
 
 
	 */
	@RequestMapping(value="deleteInvite")
	@ResponseBody
	public Map<String, Object> deleteInvite(@RequestParam int inviteId) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("inviteId", inviteId);
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
