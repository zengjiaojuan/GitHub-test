package com.phb.puhuibao.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easemob.server.comm.Constants;
import com.easemob.server.comm.HTTPMethod;
import com.easemob.server.comm.Roles;
import com.easemob.server.jersey.apidemo.EasemobChatGroups;
import com.easemob.server.jersey.utils.JerseyUtils;
import com.easemob.server.jersey.vo.ClientSecretCredential;
import com.easemob.server.jersey.vo.Credential;
import com.easemob.server.jersey.vo.EndPoints;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.GroupChat;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserEasemob;
import com.phb.puhuibao.entity.MuserFollow;

@Controller
@RequestMapping(value = "/mobileUserEasemob")
public class MobileUserEasemobController extends BaseController<MobileUserEasemob, String> {
	//private static final Logger LOGGER = LoggerFactory.getLogger(MobileUserEasemobController.class);
	private static final Log LOGGER = LogFactory.getLog(MobileUserEasemobController.class);
	private static final String APPKEY = Constants.APPKEY;
	private static final JsonNodeFactory factory = new JsonNodeFactory(false);
    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID, Constants.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);

	@Resource(name = "mobileUserEasemobService")
	public void setBaseService(IBaseService<MobileUserEasemob, String> baseService) {
		super.setBaseService(baseService);
	}
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;
	@Resource(name = "muserFollowService")
	private IBaseService<MuserFollow, String> muserFollowService;
	@Resource(name = "groupChatService")
	private IBaseService<GroupChat, String> groupChatService;

	@RequestMapping(value="getEasemobAccount")
	@ResponseBody
	public Map<String, Object> getEasemobAccount(@RequestParam String muid, @RequestParam String tomuid) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			MobileUserEasemob user = this.getBaseService().getById(muid);
			if (user == null) {
		        /**
		         * 注册IM用户[单个]
		         */
		        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
		        datanode.put("username","puhuibao_" + muid);
		        datanode.put("password", Constants.DEFAULT_PASSWORD);
		        ObjectNode createNewIMUserSingleNode = createNewIMUserSingle(datanode);
		        if (createNewIMUserSingleNode != null && createNewIMUserSingleNode.get("statusCode").asInt() == 200) {
		            LOGGER.info("注册IM用户[单个]: " + createNewIMUserSingleNode.toString());
		        	user = new MobileUserEasemob();
		        	user.setmUserId(Integer.valueOf(muid));
		        	user.setUserName("puhuibao_" + muid);
		        	user.setPassword(Constants.DEFAULT_PASSWORD);
		        	this.getBaseService().save(user);
		        } else {
					data.put("message", "环信注册失败！");
					data.put("status", 0);
					return data;
		        }
			}

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("userName", "puhuibao_" + muid);
			result.put("password", "puhuibao");
			MobileUser entity = mobileUserService.getById(muid + "");
			String nickname = entity.getNickname();
			result.put("nickname", nickname);

			if (!"".equals(tomuid)) {
				MobileUserEasemob touser = this.getBaseService().getById(tomuid + "");
				if (touser == null) {
			        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
			        datanode.put("username","puhuibao_" + tomuid);
			        datanode.put("password", Constants.DEFAULT_PASSWORD);
			        ObjectNode createNewIMUserSingleNode = createNewIMUserSingle(datanode);
			        if (createNewIMUserSingleNode != null && createNewIMUserSingleNode.get("statusCode").asInt() == 200) {
			            //LOGGER.info("注册IM用户[单个]: " + createNewIMUserSingleNode.toString());
			        	touser = new MobileUserEasemob();
			        	touser.setmUserId(Integer.valueOf(tomuid));
			        	touser.setUserName("puhuibao_" + tomuid);
			        	touser.setPassword(Constants.DEFAULT_PASSWORD);
			        	this.getBaseService().save(touser);
			        } else {
						data.put("message", "环信注册失败！");
						data.put("status", 0);
						return data;
			        }
				}

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("muserId", tomuid);
				params.put("followUser", muid);
				MuserFollow follow = muserFollowService.unique(params);
				if (follow != null) {
					nickname = follow.getNickname();
					result.replace("nickname", nickname);
				}
				
				entity = mobileUserService.getById(tomuid + "");
				nickname = entity.getNickname();
				params = new HashMap<String, Object>();
				params.put("muserId", muid);
				params.put("followUser", tomuid);
				follow = muserFollowService.unique(params);
				if (follow != null) {
					nickname = follow.getNickname();
				}
				result.put("toUserName", "puhuibao_" + tomuid);
				result.put("toNickname", nickname);
				result.put("toPhoto", entity.getPhoto());
			}
			
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
	
//	public Map<String, Object> deleteEasemobAccount(@RequestParam String muid) {
//        ObjectNode deleteIMUserByUserNameNode = deleteIMUserByUserName(muid);
//        if (null != deleteIMUserByUserNameNode) {
//            LOGGER.info("删除IM用户[单个]: " + deleteIMUserByUserNameNode.toString());
//        }
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("result", "user");
//		data.put("message", "");
//		data.put("status", 1);
//		return data;
//	}

	/**
	 * 注册IM用户[单个]
	 * 
	 * 给指定AppKey创建一个新的用户
	 * 
	 * @param dataNode
	 * @return
	 */
	public static ObjectNode createNewIMUserSingle(ObjectNode dataNode) {
		ObjectNode objectNode = factory.objectNode();
		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);
			objectNode.put("message", "Bad format of Appkey");
			return objectNode;
		}
		objectNode.removeAll();
		// check properties that must be provided
		if (null != dataNode && !dataNode.has("username")) {
			LOGGER.error("Property that named username must be provided .");
			objectNode.put("message", "Property that named username must be provided .");
			return objectNode;
		}
		if (null != dataNode && !dataNode.has("password")) {
			LOGGER.error("Property that named password must be provided .");
			objectNode.put("message", "Property that named password must be provided .");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name",
					APPKEY.split("#")[0]).resolveTemplate("app_name",
					APPKEY.split("#")[1]);
			objectNode = JerseyUtils.sendRequest(webTarget, dataNode, credential, HTTPMethod.METHOD_POST, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectNode;
	}

	/**
	 * 删除IM用户[单个]
	 * 删除指定AppKey下IM单个用户
	 * @param userName
	 * @return
	 */
	public static ObjectNode deleteIMUserByUserName(String userName) {
		ObjectNode objectNode = factory.objectNode();
		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);
			objectNode.put("message", "Bad format of Appkey");
			return objectNode;
		}
		try {
			JerseyWebTarget webTarget = EndPoints.USERS_TARGET
					.resolveTemplate("org_name", APPKEY.split("#")[0])
					.resolveTemplate("app_name", APPKEY.split("#")[1])
					.path(userName);
			objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_DELETE, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectNode;
	}

//	@RequestMapping(value="getEasemobUsers")
//	@ResponseBody
//	public Map<String, Object> getEasemobUsers(@RequestParam String muid, @RequestParam String accounts) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		String[] accountArray = accounts.split(",");
//		for (String userName : accountArray) {
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("userName", userName);
//			MobileUserEasemob entity = this.getBaseService().unique(params);
//			if (entity == null) {
//				data.put("message", "环信账号：" + userName + "不存在！");
//				data.put("status", 0);
//				return data;
//			}
//			MobileUser user = mobileUserService.getById(entity.getmUserId() + "");
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("muid", user.getmUserId());
//			map.put("photo", user.getIdPhoto());
//			String remark = user.getNickname();
//			params = new HashMap<String, Object>();
//			params.put("muserId", muid);
//			params.put("followUser", entity.getmUserId());
//			MuserFollow follow = muserFollowService.unique(params);
//			if (follow != null) {
//				remark = follow.getFollowUname();
//			}
//			map.put("remark", remark);
//			list.add(map);
//		}
//		data.put("result", list);
//		data.put("message", "");
//		data.put("status", 1);
//		return data;
//	}

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * getEaseUsers
	 * @param muid
	 * @param ids 环信账号字符串
	 * @return
	 */
	@RequestMapping(value="getEaseUsers")
	@ResponseBody
	public Map<String, Object> getEaseUsers(@RequestParam String muid, @RequestParam String ids) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			String sql = "select username,d.photo,case when isnull(remark) then nickname else remark end remark from(select a.user_name username,b.photo,(select follow_uname from phb_muser_follow where follow_user=a.m_user_id and m_user_id=" + muid +")remark,b.nickname FROM phb_mobile_user_easemob a left join phb_mobile_user b on a.m_user_id=b.m_user_id where find_in_set (a.user_name,'" + ids + "'))d";
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
	 * getGroupEaseUsers
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value="getGroupEaseUsers")
	@ResponseBody
	public Map<String, Object> getGroupEaseUsers(@RequestParam String groupId) {
		Map<String, Object> data = new HashMap<String, Object>();
		GroupChat group = groupChatService.getById(groupId);
		if (group == null) {
			data.put("message", "该群组不存在！");
			data.put("status", 0);
			return data;
		}
		String muid = group.getGroupOwnerid() + "";
		String ids = group.getMember();
		try {
			String sql = "select m_user_id FROM phb_mobile_user_easemob where find_in_set (m_user_id,'" + ids + "')";
			List<Map<String, Object>> idList = this.jdbcTemplate.queryForList(sql);
			List<String> list = new ArrayList<String>();
			for (Map<String, Object> map : idList) {
				String id = map.get("m_user_id") + "";
				list.add(id);
			}
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				if (!list.contains(id)) {
			        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
			        datanode.put("username","puhuibao_" + id);
			        datanode.put("password", Constants.DEFAULT_PASSWORD);
			        ObjectNode createNewIMUserSingleNode = createNewIMUserSingle(datanode);
			        if (createNewIMUserSingleNode != null && createNewIMUserSingleNode.get("statusCode").asInt() == 200) {
			        	MobileUserEasemob user = new MobileUserEasemob();
			        	user.setmUserId(Integer.valueOf(id));
			        	user.setUserName("puhuibao_" + id);
			        	user.setPassword(Constants.DEFAULT_PASSWORD);
			        	this.getBaseService().save(user);
			        }
				}
			}

			sql = "select username,d.photo, case when isnull(remark) then nickname else remark end remark from(select a.user_name username,b.photo,(select follow_uname from phb_muser_follow where follow_user=a.m_user_id and m_user_id=" + muid +")remark,b.nickname FROM phb_mobile_user_easemob a left join phb_mobile_user b on a.m_user_id=b.m_user_id where find_in_set (a.m_user_id,'" + ids + "'))d";
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
	 * 创建群组
	 * @param muid
	 * @param groupName
	 * @param desc
	 * @param isPublic
	 * @param members
	 * @param maxusers
	 * @return
	 */
	@RequestMapping(value="createGroup")
	@ResponseBody
	public Map<String, Object> createGroup(@RequestParam int muid,
            @RequestParam String easeUserName,
            @RequestParam String groupName,
            @RequestParam String desc,
            @RequestParam boolean isPublic,
            @RequestParam String members,
            int maxUsers) {
		List<Map<String, Object>> memberList = getEaseUsersByIds(muid + "", members);
		ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
//	    "groupname":"testrestgrp12", //群组名称, 此属性为必须的
//	    "desc":"server create group", //群组描述, 此属性为必须的
//	    "public":true, //是否是公开群, 此属性为必须的
//	    "maxusers":300, //群组成员最大数(包括群主), 值为数值类型,默认值200,此属性为可选的
//	    "approval":true, //加入公开群是否需要批准, 默认值是false（加如公开群不需要群主批准）, 此属性为必选的，私有群必须为true
//	    "owner":"jma1", //群组的管理员, 此属性为必须的
//	    "members":["jma2","jma3"] //群组成员,此属性为可选的,但是如果加了此项,数组元素至少一个（注：群主jma1不需要写入到members里面）
		dataObjectNode.put("groupname", groupName);
		dataObjectNode.put("desc", desc);
		dataObjectNode.put("approval", false);
		dataObjectNode.put("public", isPublic);
		dataObjectNode.put("maxusers", maxUsers);
		dataObjectNode.put("owner", easeUserName);
		ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
		for (Map<String, Object> map : memberList) {
			arrayNode.add((String) map.get("username"));
		}
//		String[] memberArray = members.split(",");
//		for(String member : memberArray) {
//			arrayNode.add(member);
//		}
		dataObjectNode.put("members", arrayNode);
		ObjectNode creatChatGroupNode = EasemobChatGroups.creatChatGroups(dataObjectNode);
		Map<String, Object> data = new HashMap<String, Object>();

		if (creatChatGroupNode != null && creatChatGroupNode.get("statusCode").asInt() == 200) {
            LOGGER.info("创建群组: " + creatChatGroupNode.toString());
			GroupChat entity = new GroupChat();
            entity.setGroupId(creatChatGroupNode.get("data").get("groupid").asText());
            entity.setIsPublic(isPublic ? 1 : 0);
            entity.setMaxUsers(maxUsers);
            entity.setGroupName(groupName);
            entity.setGroupDescscription(desc);
            entity.setGroupOwner("puhuibao_"+muid);
            entity.setGroupOwnerid(muid);
            entity.setMember(members);
            groupChatService.save(entity);

    		data.put("result", memberList);
    		data.put("message", "");
    		data.put("status", 1);
    		return data;
        } else {
			data.put("message", "创建群组失败！");
			data.put("status", 0);
			return data;
        }
	}

	// 返回与当前用户相关的环信用户
	private List<Map<String, Object>> getEaseUsersByIds(String muid, String ids) {
		String sql = "select m_user_id FROM phb_mobile_user_easemob where find_in_set (m_user_id,'" + ids + "')";
		List<Map<String, Object>> idList = this.jdbcTemplate.queryForList(sql);
		List<String> list = new ArrayList<String>();
		for (Map<String, Object> map : idList) {
			String id = map.get("m_user_id") + "";
			list.add(id);
		}
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			if (!list.contains(id)) {
		        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
		        datanode.put("username","puhuibao_" + id);
		        datanode.put("password", Constants.DEFAULT_PASSWORD);
		        ObjectNode createNewIMUserSingleNode = createNewIMUserSingle(datanode);
		        if (createNewIMUserSingleNode != null && createNewIMUserSingleNode.get("statusCode").asInt() == 200) {
		        	MobileUserEasemob user = new MobileUserEasemob();
		        	user.setmUserId(Integer.valueOf(id));
		        	user.setUserName("puhuibao_" + id);
		        	user.setPassword(Constants.DEFAULT_PASSWORD);
		        	this.getBaseService().save(user);
		        }
			}
		}

		sql = "select username,d.photo, case when isnull(remark) then nickname else remark end remark from(select a.user_name username,b.photo,(select follow_uname from phb_muser_follow where follow_user=a.m_user_id and m_user_id=" + muid +")remark,b.nickname FROM phb_mobile_user_easemob a left join phb_mobile_user b on a.m_user_id=b.m_user_id where find_in_set (a.m_user_id,'" + ids + "'))d";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		return result;
	}
	
	/**
	 * 群组批量添加成员
	 * @param muid
	 * @param groupId
	 * @param members
	 * @return
	 */
	@RequestMapping(value="addMembersToGroupBatch")
	@ResponseBody
	public Map<String, Object> addMembersToGroupBatch(@RequestParam String muid, @RequestParam String groupId, @RequestParam String members) {
		List<Map<String, Object>> memberList = getEaseUsersByIds(muid + "", members);
		ArrayNode usernames = JsonNodeFactory.instance.arrayNode();
		for (Map<String, Object> map : memberList) {
			usernames.add((String) map.get("username"));
		}
		ObjectNode usernamesNode = JsonNodeFactory.instance.objectNode();
		usernamesNode.put("usernames", usernames);
		ObjectNode addUserToGroupBatchNode = EasemobChatGroups.addUsersToGroupBatch(groupId, usernamesNode);

		Map<String, Object> data = new HashMap<String, Object>();
		if (addUserToGroupBatchNode != null && addUserToGroupBatchNode.get("statusCode").asInt() == 200) {
			GroupChat group = groupChatService.getById(groupId);
			GroupChat entity = new GroupChat();
			entity.setGroupId(groupId);
			entity.setMember(group.getMember() + "," + members);
			groupChatService.update(entity);

			data.put("result", memberList);
			data.put("message", "");
			data.put("status", 1);
			return data;
		} else {
			data.put("message", "群组批量添加成员失败！");
			data.put("status", 0);
			return data;
		}
	}
	
	@RequestMapping(value="deleteMemberFromGroup")
	@ResponseBody
	public Map<String, Object> deleteMemberFromGroup(@RequestParam String groupId, @RequestParam String easeUserName) {
		ObjectNode deleteUserFromGroupNode = EasemobChatGroups.deleteUserFromGroup(groupId, easeUserName);
		
		Map<String, Object> data = new HashMap<String, Object>();
		if (deleteUserFromGroupNode != null && deleteUserFromGroupNode.get("statusCode").asInt() == 200) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userName", easeUserName);
			MobileUserEasemob user = this.getBaseService().unique(params);
			if (user == null) {
				data.put("message", "环信账号：" + easeUserName + "不存在！");
				data.put("status", 0);
				return data;
			}
			GroupChat group = groupChatService.getById(groupId);
			String[] members = group.getMember().split(",");
			String ids = "";
			boolean first = true;
			for (String member : members) {
				if (member.equals(user.getmUserId() + "")) {
					continue;
				}
				if (first) {
					ids = member;
					first = false;
				} else {
					ids += "," + member;
				}
			}
			GroupChat entity = new GroupChat();
			entity.setGroupId(groupId);
			entity.setMember(ids);
			groupChatService.update(entity);

			data.put("message", "");
			data.put("status", 1);
			return data;
		} else {
			data.put("message", "群组移除成员失败！");
			data.put("status", 0);
			return data;
		}
	}
}
