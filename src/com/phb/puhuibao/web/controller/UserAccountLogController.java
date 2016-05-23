package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.UserAccountLog;

@Controller
@RequestMapping(value = "/userAccountLog")
public class UserAccountLogController extends BaseController<UserAccountLog, String> {
	@Resource(name = "userAccountLogService")
	public void setBaseService(IBaseService<UserAccountLog, String> baseService) {
		super.setBaseService(baseService);
	}

	/**
	 * 翻页
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String muid) {
		Pager<UserAccountLog> pager = new Pager<UserAccountLog>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(10);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("mUserId", muid);
		map.put("orderBy", "create_time");
		map.put("order", "desc");
		Pager<UserAccountLog> p=this.getBaseService().findByPager(pager, map);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
}
