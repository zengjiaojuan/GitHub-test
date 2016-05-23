package com.phb.puhuibao.web.controller;

import java.util.ArrayList;
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

import com.easemob.server.comm.Constants;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserEasemob;
import com.phb.puhuibao.entity.MuserFollow;
import com.phb.puhuibao.service.impl.MuserFollowServiceImpl;

import org.springframework.jdbc.core.JdbcTemplate;

@Controller
@RequestMapping(value = "/muserFollow")
public class MuserFollowController extends BaseController<MuserFollow, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(MuserFollowController.class);
	
	@Resource(name = "muserFollowService")
	public void setBaseService(IBaseService<MuserFollow, String> baseService) {
		super.setBaseService(baseService);
	}
	@Resource(name = "mobileUserEasemobService")
	private IBaseService<MobileUserEasemob, String> mobileUserEasemobService;

 

	@Resource(name = "muserFollowService")
	private MuserFollowServiceImpl muserFollowService;
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	/**
                    关注
	 * @param muid 当前用户 id
	 * @param fmuid 关注用户id
                        把当前关注用户的名字也插入是为了 做查询功能方便，sql语句里不用写case when
	 */

	@RequestMapping(value="savefollow")
	@ResponseBody
	public Map<String, Object> savefollow(@RequestParam int muid,@RequestParam int fmuid, @RequestParam String followUname) {
		MuserFollow entity = new MuserFollow();
		entity.setMuserId(muid);
		entity.setFollowUser(fmuid);
		entity.setFollowUname(followUname);
		entity.setFollowTime(new Date());
		
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("muserId", muid);
		params.put("followUser", fmuid);
		MuserFollow en = this.getBaseService().unique(params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if( !(en == null) ){
				if(en.getFollowIsblocked()=="1"){//原先已经拉黑了 先删除吧
					this.getBaseService().delete(params);
				}
		     }
			this.getBaseService().save(entity);

		} catch (Exception e) {
			 
			data.put("message", "重复关注！");
			data.put("status", 0);
			return data;
		}
		data.put("result", entity);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	/**
	    关注
	* @param muid 当前用户 id
	* @param fmuid 关注用户id
	        把当前关注用户的名字也插入是为了 做查询功能方便，sql语句里不用写case when
	*/
	
	@RequestMapping(value="savefriendship")
	@ResponseBody
	public Map<String, Object> savefriendship(@RequestParam int muid,@RequestParam int fmuid, @RequestParam String followUname, @RequestParam String myname) {
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
			    this.getBaseService().save(entity1);
			    this.getBaseService().save(entity2);
				
			}else if(status1==0 && status2==1 ){//对方对我关注了,我没有对对方关注
				 this.getBaseService().save(entity1);
				
			} else if(status1==1 && status2==0 ){//已经关注对方 但是对方没有关注我
				this.getBaseService().save(entity2);
				
			}else if(status1==1 && status2==1 ){//
				data.put("message", "已经是好友！");
				data.put("status", 1);
				return data;
				
			}else if(status2==2 ){//
				data.put("message", "对方已将你拉黑！");
				data.put("status", 1);
				return data;
				
			}else if(status1==2  ){//
				data.put("message", "请先取消对他拉黑！");
				data.put("status", 1);
				return data;
				
			}

		} catch (Exception e) {
		
	 
		}
		data.put("result", "");
		data.put("message", "添加好友成功！");
		data.put("status", 1);
		return data;
	}

	/**
	    检查是否关注
	* @param  muid 用户id
	*/
	@RequestMapping(value="followingStatus" )
	@ResponseBody
	public int followingStatus(@RequestParam int muid,@RequestParam int fmuid) {
	Map<String,Object> params = new HashMap<String,Object>();
 
	int sataus = 0;
	 
	params.put("muserId", muid);
	params.put("followUser", fmuid);
 
		MuserFollow status = this.getBaseService().unique(params);
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
	    获取朋友列表
	* @param  muid 用户id
	*/
	@RequestMapping(value="getFriendsList" )
	@ResponseBody
	public Map<String, Object> getFriendsList(@RequestParam int muid) {
	Map<String,Object> params = new HashMap<String,Object>();
	Map<String, Object> data = new HashMap<String, Object>();
	params.put("findfriends", true);
	params.put("currentuserId", muid);
	try {
		List<MuserFollow> friendlist = this.getBaseService().findList(params);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (MuserFollow friend : friendlist) {
			String fmuid =  friend.getFollowUser() + "";
			MobileUserEasemob user = mobileUserEasemobService.getById(fmuid);
			String easeUserName;
			String easePassword;
			if (user == null) {
		        easeUserName = "puhuibao_" + fmuid;
		        easePassword = Constants.DEFAULT_PASSWORD;
		        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
		        datanode.put("username",easeUserName);
		        datanode.put("password", easePassword);
		        ObjectNode createNewIMUserSingleNode = MobileUserEasemobController.createNewIMUserSingle(datanode);
		        if (createNewIMUserSingleNode != null && createNewIMUserSingleNode.get("statusCode").asInt() != 400) {
		        	user = new MobileUserEasemob();
		        	user.setmUserId(Integer.valueOf(fmuid));
		        	user.setUserName(easeUserName);
		        	user.setPassword(easePassword);
		        	mobileUserEasemobService.save(user);
		        } else {
		    		data.put("message", "连接环信异常！");
		    		data.put("status", 0);
		    		return data;
		        }
			} else {
		        easeUserName = user.getUserName();
		        easePassword = user.getPassword();
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("easeUserName", easeUserName);
			map.put("easePassword", easePassword);
			map.put("muserId", friend.getMuserId());
			map.put("followUser", friend.getFollowUser());
			map.put("followUname", friend.getFollowUname());
			map.put("followReporttype", friend.getFollowReporttype());
			map.put("followReportcomment", friend.getFollowReportcomment());
			map.put("followIsblocked", friend.getFollowIsblocked());
			map.put("followTime", friend.getFollowTime());
			map.put("commentTime", friend.getCommentTime());
			map.put("photo", friend.getPhoto());
			map.put("nickname", friend.getNickname());
			map.put("sex", friend.getSex());
			map.put("level", friend.getLevel());
			list.add(map);
		}
		data.put("result", list);
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
	    获取关注列表
	* @param  muid 用户id
	*/
	@RequestMapping(value="getFollowingList")
	@ResponseBody
	public Map<String, Object> getFollowingList(@RequestParam int muid) {
	Map<String,Object> params = new HashMap<String,Object>();
	Map<String, Object> data = new HashMap<String, Object>();
	params.put("findfollowings", true);
	params.put("currentuserId", muid);
	try {
		List<MuserFollow> followingslist = this.getBaseService().findList(params);
		data.put("result", followingslist);
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
	获取粉丝列表
	* @param  muid 用户id
	*/
	@RequestMapping(value="getFansList")
	@ResponseBody
	public Map<String, Object> getFansList(@RequestParam int muid) {
	
	String sql="SELECT f.m_user_id muserid,f.follow_user followuser,u.photo ,u.sex,u.level,ifnull(following.follow_uname,u.nickname) nickname FROM phb_muser_follow f LEFT JOIN phb_mobile_user u ON f.m_user_id = u.m_user_id left join (SELECT follow_user,follow_uname FROM phb_muser_follow WHERE m_user_id="+muid+" )following on (following.follow_user = f.m_user_id ) WHERE f.follow_user = "+muid+" AND f.m_user_id NOT IN (SELECT follow_user FROM phb_muser_follow WHERE m_user_id="+muid+" AND follow_isblocked ='1')";
	
	Map<String, Object> data = new HashMap<String, Object>();
	 
	try {
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		data.put("result", result);
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
	获取黑名单列表
	* @param  muid 用户id
	*/
	@RequestMapping(value="getBlockedList")
	@ResponseBody
	public Map<String, Object> getBlockedList(@RequestParam int muid) {
	Map<String,Object> params = new HashMap<String,Object>();
	Map<String, Object> data = new HashMap<String, Object>();
	params.put("findblocked", true);
	params.put("currentuserId", muid);
	try {
		List<MuserFollow> friendlist = this.getBaseService().findList(params);
		data.put("result", friendlist);
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
                     修改关注的人的备注名
	 * @param muid 当前用户 id
	 * @param fmuid 关注用户id
	 * @param followUname 关注用户新的备注名字
 
	 */

	@RequestMapping(value="changefname")
	@ResponseBody
	public Map<String, Object> changefname(@RequestParam int muid,@RequestParam int fmuid,@RequestParam String followUname) {
		MuserFollow entity = new MuserFollow();
		entity.setMuserId(muid);
		entity.setFollowUser(fmuid);
		entity.setFollowUname(followUname);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			this.getBaseService().update(entity);
		} catch (Exception e) {
			 
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}
		data.put("result", entity);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	/**
               取消关注
	 * @param muid 当前用户 id
	 * @param fmuid 关注用户id
	 * @param followUname 关注用户新的备注名字
 
	 */
	@RequestMapping(value="cancelfollow")
	@ResponseBody
	public Map<String, Object> cancelfollow(@RequestParam int muid,@RequestParam int fmuid) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("dmuserId", muid);
		params.put("dfollowUser", fmuid);
		int count = this.getBaseService().delete(params);
		Map<String, Object> data = new HashMap<String, Object>();
		if (count == 0) {
			data.put("message", "他不是你关注的对象!");
			data.put("status", 0);
		} else {
			data.put("message", "取消关注成功！");			
			data.put("status", 1);
		}
		return data;
	}
	
	/**
	    拉黑
	* @param muid 当前用户 id
	* @param fmuid 关注用户id
	 
	
	*/

	@RequestMapping(value = "block")
	@ResponseBody
	public Map<String, Object> block(@RequestParam int muid,@RequestParam int fmuid) {
		MuserFollow entity = new MuserFollow();
		entity.setMuserId(muid);
		entity.setFollowUser(fmuid);
		entity.setFollowIsblocked("1");
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			this.getBaseService().update(entity);
		} catch (Exception e) {

			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}
		data.put("result", entity);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}

	
	/**
	     取消 拉黑
	* @param muid 当前用户 id
	* @param fmuid 好友id
	*/
	
	@RequestMapping(value = "unblock")
	@ResponseBody
	public Map<String, Object> unblock(@RequestParam int muid,@RequestParam int fmuid) {
		 
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dmuserId", muid);
		param.put("dfollowUser", fmuid);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			this.getBaseService().delete(param);
		} catch (Exception e) {
	
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}
		data.put("result", "取消成功!");
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	/**
	     举报
	* @param muid 当前用户 id
	* @param fmuid 关注用户id
	 
	
	*/

	@RequestMapping(value = "report")
	@ResponseBody
	public Map<String, Object> report(@RequestParam int muid,@RequestParam int fmuid,  String reporttype,String reportcontent) {
		MuserFollow entity = new MuserFollow();
		entity.setMuserId(muid);
		entity.setFollowUser(fmuid);
	
		Map<String, Object> data = new HashMap<String, Object>();
		if("".equals(reporttype) && "".equals(reportcontent)){
			data.put("message", "请至少选择或者输入一个举报理由！");
			data.put("status", 0);
			return data;
		}
		entity.setFollowReporttype(reporttype);
		entity.setFollowReportcomment(reportcontent);
		entity.setCommentTime(new Date());
		try {
			this.getBaseService().update(entity);
		} catch (Exception e) {
	
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}
		data.put("result", entity);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
    }
 
	/**
	 * getUserInfo聊天用户信息 -lyb
	 * @param muid
	 * @param fmuid
	 * @return
	 */
	@RequestMapping(value="getUserInfo")
	@ResponseBody
	public Map<String, Object> getUserInfo(@RequestParam String muid, @RequestParam String fmuid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("muserId", muid);
		params.put("followUser", fmuid);
		MuserFollow follow = this.getBaseService().unique(params);

		Map<String, Object> data = new HashMap<String, Object>();
		if (follow == null) {
			data.put("message", "没有数据!");
			data.put("status", 0);
		} else {
			Map<String, String> result = new HashMap<String, String>();
			result.put("nickname", follow.getNickname());
			result.put("photo", follow.getPhoto());
			data.put("result", result);
			data.put("message", "");
			data.put("status", 1);
		}
		return data;
	}

}
