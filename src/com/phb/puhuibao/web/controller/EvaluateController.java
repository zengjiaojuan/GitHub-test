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
import com.phb.puhuibao.entity.Evaluate;

@Controller
@RequestMapping(value = "/evaluate")
public class EvaluateController extends BaseController<Evaluate, String> {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "evaluateService")
	public void setBaseService(IBaseService<Evaluate, String> baseService) {
		super.setBaseService(baseService);
	}
	@javax.annotation.Resource(name = "resourceService")
	private IBaseService<Resource, String> resourceService;

	/**
	 * 我发出的评价
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String muid) {
		Pager<Evaluate> pager = new Pager<Evaluate>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		params.put("orderBy", "create_time");
		params.put("order", "desc");
		Pager<Evaluate> p = this.getBaseService().findByPager(pager, params);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
	/**
	 * 个人主页下最新评价 我收到的评价
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="getRecentEvaluate")
	@ResponseBody
	public Map<String, Object> getRecentEvaluate(@RequestParam String muid) {
 
		String sql = "select e.evaluate_id,e.to_user_id,e.m_user_id,e.content,e.create_time,u.nickname,u.photo ,u.level   from  phb_evaluate e  left join  phb_mobile_user u on e.m_user_id = u.m_user_id where  e.to_user_id  = "+muid+" order by  e.create_time desc";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		sql = "select  count(1)  from  phb_evaluate e  left join  phb_mobile_user u on e.to_user_id = u.m_user_id where  e.to_user_id  = "+muid+" order by  e.create_time desc";
		List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("count", count.get(0).get("count(1)"));
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 对资源的评价   
	 * @param pageno
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="queryForResource")
	@ResponseBody
	public Map<String, Object> queryForResource(@RequestParam int pageno, @RequestParam int resourceId) {
		Pager<Evaluate> pager = new Pager<Evaluate>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceId", resourceId);
		params.put("orderBy", "create_time");
		params.put("order", "desc");
		Pager<Evaluate> p = this.getBaseService().findByPager(pager, params);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 保存评价
	 * @param muid
	 * @param resourceId
	 * @param content
	 * @param score
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, //发出评价的人
			@RequestParam int toUserId, //接受评价的人
			@RequestParam int resourceId, 
			@RequestParam String content,
			@RequestParam int score) {
		
		Evaluate entity = new Evaluate();
		entity.setmUserId(muid);
		entity.setResourceId(resourceId);
		entity.setContent(content);
		entity.setScore(score);
		entity.setToUserId(toUserId);
		
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
	
	/**
	 * 平均评分
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="getResourceEvaluateScore")
	@ResponseBody
	public Map<String, Object> getResourceEvaluateScore(@RequestParam String resourceId) {
		String sql = "SELECT sum(score)/count(*) as score FROM phb_evaluate where resource_id=" + resourceId;
		this.jdbcTemplate.execute(sql);
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result.get(0).get("score"));
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * getEvaluateTypes
	 * @return
	 */
	@RequestMapping(value="getEvaluateTypes")
	@ResponseBody
	public Map<String, Object> getEvaluateTypes() {
		String sql = "select type_id,content,'' as status from phb_evaluate_type";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
}
