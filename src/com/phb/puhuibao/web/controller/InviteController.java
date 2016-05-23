package com.phb.puhuibao.web.controller;

import java.util.Calendar;
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
import com.phb.puhuibao.entity.Invite;
//import com.phb.puhuibao.entity.MobileUser;
//import com.phb.puhuibao.entity.UserAccountLog;

@Controller
@RequestMapping(value = "/invite")
public class InviteController extends BaseController<Invite, String> {
	@Resource(name = "inviteService")
	public void setBaseService(IBaseService<Invite, String> baseService) {
		super.setBaseService(baseService);
	}

//	@Resource(name = "mobileUserService")
//	private IBaseService<MobileUser, String> mobileUserService;
//	@Resource(name = "userAccountLogService")
//	private IBaseService<UserAccountLog, String> userAccountLogService;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 邀请列表翻页
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String muid) {
//		Pager<Invite> pager = new Pager<Invite>();
//		pager.setReload(true);
//		pager.setCurrent(pageno);
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("mUserId", muid);
//		Pager<Invite> p = this.getBaseService().findByPager(pager, params);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for (Invite invite : p.getData()) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("mUserName", invite.getmUserName());
//			map.put("photo", invite.getPhoto());
//			map.put("commission", invite.getCommission());
//			list.add(map);
//		}
//
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("result", list);
//		data.put("count", p.getTotal());
//		data.put("message", "");
//		data.put("status", 1);
		String sql = "select m_user_name,photo,commission,m_user_tel from phb_mobile_user b left join (select from_user,sum(amount) commission from (select from_user,amount from phb_muser_account_log where m_user_id=" + muid + " and from_user>0)a group by from_user)c on b.m_user_id=c.from_user where b.parent_id in(select m_user_id from phb_mobile_user where parent_id=" + muid + ") or b.parent_id=" + muid;
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		sql = "select count(1) from phb_mobile_user b left join (select from_user,sum(amount) commission from (select from_user,amount from phb_muser_account_log where m_user_id=" + muid + " and from_user>0)a group by from_user)c on b.m_user_id=c.from_user where b.parent_id in(select m_user_id from phb_mobile_user where parent_id=" + muid + ") or b.parent_id=" + muid;
		List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("count", count.get(0).get("count(1)"));
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 提成汇总
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="sum")
	@ResponseBody
	public Map<String, Object> sum(@RequestParam String muid) {
		String sql = "select sum(amount) commission from phb_muser_account_log where m_user_id=" + muid + " and from_user>0";

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", this.jdbcTemplate.queryForList(sql).get(0).get("commission"));
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 我的邀请
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="findList")
	@ResponseBody
	public Map<String, Object> findList(@RequestParam String muid) {
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("parentId", muid);
//		List<MobileUser> users = mobileUserService.findList(params);
//		List<Map<String, Object>> result =  new ArrayList<Map<String, Object>>();
//
//		for (MobileUser user : users) {
//			params = new HashMap<String,Object>();
//			params.put("parentId", user.getmUserId());
//			List<MobileUser> us = mobileUserService.findList(params);
//			Map<String, Object> m = new HashMap<String, Object>();
//			for (MobileUser u : us) {
//				m.put("userName", u.getmUserName());
//				m.put("photo", u.getPhoto());
//			}
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("userName", user.getmUserName());
//			m.put("photo", user.getPhoto());
//			map.put("children", m);
//			result.add(map);
//		}
		String sql = "select m_user_name,photo,commission from phb_mobile_user b left join (select from_user,sum(amount) commission from (select from_user,amount from phb_muser_account_log where m_user_id=" + muid + " and from_user>0)a group by from_user)c on b.m_user_id=c.from_user where b.parent_id in(select m_user_id from phb_mobile_user where parent_id=" + muid + ") or b.parent_id=" + muid;

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", this.jdbcTemplate.queryForList(sql));
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="getInviteCode")
	@ResponseBody
	public Map<String, Object> getInviteCode(@RequestParam int muid) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		Invite entity = this.getBaseService().unique(params);
		if (entity == null) {
			String code = createRandom(4);
			params = new HashMap<String, Object>();
			params.put("code", code);
			Invite result = this.getBaseService().unique(params);
			while (result != null) {
				code = createRandom(4);
				params = new HashMap<String,Object>();
				params.put("code", code);
				result = this.getBaseService().unique(params);
			}
	
			entity = new Invite();
			entity.setCode(code);
			entity.setmUserId(muid);
			Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DATE, 10);
	        entity.setLastDate(cal.getTime());
			try {
			    this.getBaseService().save(entity);
			} catch (Exception e) {
				data.put("message", "网络异常！");
				data.put("status", 0);
				return data;
			}
		}

		data.put("result", entity.getCode());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	private String createRandom(int length) {
		String retStr = "";
		String strTable = "abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		for (int i = 0; i < length; i++) {
			int intR = (int) (Math.random() * len);
			retStr += strTable.charAt(intR);
		}
		return retStr;
	}

}
