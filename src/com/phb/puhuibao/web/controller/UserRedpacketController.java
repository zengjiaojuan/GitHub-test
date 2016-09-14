package com.phb.puhuibao.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.UserRedpacket;

@Controller
@RequestMapping(value = "/userRedpacket")
public class UserRedpacketController extends BaseController<UserRedpacket, String> {
	@Override
	@Resource(name = "userRedpacketService")
	public void setBaseService(IBaseService<UserRedpacket, String> baseService) {
		super.setBaseService(baseService);
	}
	
	
	
	/**
	 * 所有红包列表
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="allRedpackets")
	@ResponseBody
	public Map<String, Object> allRedpackets(@RequestParam int pageno, @RequestParam String muid) {
		Pager<UserRedpacket> pager = new Pager<UserRedpacket>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(30);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("orderBy", "last_date");
		params.put("order", "desc");
		Map<String, Object> data = new HashMap<String, Object>();
		Pager<UserRedpacket> p = this.getBaseService().findByPager(pager, params);
	
		
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 所有有效红包 不分页
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="allAvailableRedpackets")
	@ResponseBody
	public Map<String, Object> allAvailableRedpackets( @RequestParam String muid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("status", 1);
	 
		List<UserRedpacket> result = this.getBaseService().findList(params);
		Map<String, Object> data = new HashMap<String, Object>();
 
		data.put("result", result);
		data.put("count", result.size());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
	/**
	 * 有效红包列表
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="queryForValid")
	@ResponseBody
	public Map<String, Object> queryForValid(@RequestParam int pageno, @RequestParam String muid) {
		Pager<UserRedpacket> pager = new Pager<UserRedpacket>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("status", 1);
		params.put("glastDate", new Date());
		params.put("orderBy", "last_date");
		params.put("order", "desc");
		Pager<UserRedpacket> p = this.getBaseService().findByPager(pager, params);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	/**
	 * 无效红包列表
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="queryForInvalid")
	@ResponseBody
	public Map<String, Object> queryForInvalid(@RequestParam int pageno, @RequestParam String muid) {
		Pager<UserRedpacket> pager = new Pager<UserRedpacket>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("lelastDate", new Date());
		params.put("orderBy", "last_date");
		params.put("order", "desc");
		Pager<UserRedpacket> p = this.getBaseService().findByPager(pager, params);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 有效红包
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="validRedpackets")
	@ResponseBody
	public Map<String, Object> validRedpackets(@RequestParam String muid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("status", 1);
		params.put("glastDate", new Date());
		List<UserRedpacket> result = this.getBaseService().findList(params);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 有效红包列表
	 * @param type
	 * @return
	 */
	@Override
	@RequestMapping(params = "isArray=true", method = RequestMethod.GET)
	@ResponseBody
	public List<UserRedpacket> findByList(@RequestParam String muid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("status", 1);
		params.put("glastDate", new Date());
		List<UserRedpacket> result = this.getBaseService().findList(params);
		return result;
	}
	
	/**
	 * 测试用
	 * @param muid
	 * @param redpacketAmount
	 * @param deductionRate
	 * @param status
	 * @param lastDate
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam int redpacketAmount, @RequestParam double deductionRate, @RequestParam int status, @RequestParam Date lastDate) {
		UserRedpacket entity = new UserRedpacket();
		entity.setmUserId(muid);
		entity.setRedpacketAmount(redpacketAmount);
		entity.setDeductionRate(deductionRate);
		entity.setStatus(status);
		entity.setLastDate(lastDate);

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
