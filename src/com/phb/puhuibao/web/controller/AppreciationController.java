package com.phb.puhuibao.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
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
import com.phb.puhuibao.entity.Appreciation;
import com.phb.puhuibao.entity.ConsumeDiscount;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserExtra;
import com.phb.puhuibao.entity.Order;

@Controller
@RequestMapping(value = "/appreciation")
public class AppreciationController extends BaseController<Appreciation, String> {
	@Resource(name = "appreciationService")
	public void setBaseService(IBaseService<Appreciation, String> baseService) {
		super.setBaseService(baseService);
	}

	@Resource(name = "mobileUserExtraService")
	private IBaseService<MobileUserExtra, String> mobileUserExtraService;

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "consumeDiscountService")
	private IBaseService<ConsumeDiscount, String> consumeDiscountService;

	@Resource(name = "orderService")
	private IBaseService<Order, String> orderService;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 增值服务列表
	 * @param pageno
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno) {
		Pager<Appreciation> pager = new Pager<Appreciation>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", 1);
		params.put("orderBy", "create_time");
		params.put("order", "asc");
		Pager<Appreciation> p = this.getBaseService().findByPager(pager, params);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 增值服务列表
	 * @param muid
	 * @param catId
	 * @return
	 */
	@RequestMapping(value="findList")
	@ResponseBody
	public Map<String, Object> findList(@RequestParam String muid, @RequestParam int catId) {
//		MobileUserExtra extra = mobileUserExtraService.getById(muid);
//		int level = 0;
//		if (extra != null && extra.getLevel() != null) {
//			level = extra.getLevel();
//		}
		MobileUser user = mobileUserService.getById(muid);
		int level = 0;
		if (user != null && user.getLevel() != null) {
			level = user.getLevel();
		}
//		String sql = "select a.price,a.start_date,a.end_date,a.appreciation_id,a.appreciation_name,a.appreciation_desc,case when b.level=" + level +" then a.icon else a.invalid_icon end as icon,case when c.order_id is not null then 2 when b.level=" + level +" then 1 else 0 end as status from phb_appreciation a left join phb_mobile_consume_discount b on a.appreciation_id=b.appreciation_id left join phb_order c on a.appreciation_id=c.appreciation_id and c.m_user_id=" + muid + " where a.cat_id=" + catId + " and a.status=1 and a.start_date<now() and a.end_date>now()";
		String sql = "select b.appreciation_id,b.appreciation_name,b.appreciation_desc,case when b.level=" + level +" then b.icon else b.invalid_icon end as icon,case when b.order_id is not null then 2 when b.level<=" + level +" then 1 else 0 end as status,b.price,c.discount from(select a.appreciation_id,a.appreciation_name,a.appreciation_desc,a.icon,a.invalid_icon,a.price,(select level from phb_mobile_consume_discount where appreciation_id=a.appreciation_id and level<=" + level +" order by level desc limit 1)level,(select order_id from phb_order where appreciation_id=a.appreciation_id and m_user_id=" + muid + " limit 1)order_id from phb_appreciation a where a.cat_id=" + catId + " and a.status=1 and a.start_date<now() and a.end_date>now()) b left join phb_mobile_consume_discount c on b.appreciation_id=c.appreciation_id and b.level=c.level";
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("list", this.jdbcTemplate.queryForList(sql));
		result.put("level", level);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	@RequestMapping(value="getAppreciationById")
	@ResponseBody
	public Map<String, Object> getAppreciationById (@RequestParam String muid, @RequestParam String appreciationId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("appreciationId", appreciationId);
		params.put("payStatus", 1);
		Order order = orderService.unique(params);

		Appreciation result = this.getBaseService().getById(appreciationId);
		if (order == null) {
			result.setStatus(0); // 借用状态字段
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 商户判断
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="isMerchant")
	@ResponseBody
	public Map<String, Object> isMerchant(@RequestParam String muid) {
		Appreciation entity = this.getBaseService().getById(muid);
		Map<String, Object> data = new HashMap<String, Object>();
		if (entity == null) {
			data.put("result", false);
			data.put("message", "读取失败！");
			data.put("status", 0);
		} else {
			data.put("result", true);
			data.put("message", "成功");
			data.put("status", 1);
		}
		return data;
	}

	@RequestMapping(value="getOrderInfo")
	@ResponseBody
	public Map<String, Object> getOrderInfo(@RequestParam String muid, @RequestParam String appreciationId) {
//		MobileUserExtra extra = mobileUserExtraService.getById(muid);
//		int level = 0;
//		if (extra != null && extra.getLevel() != null) {
//			level = extra.getLevel();
//		}
		MobileUser user = mobileUserService.getById(muid);
		int level = 0;
		if (user != null && user.getLevel() != null) {
			level = user.getLevel();
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appreciationId", appreciationId);
		params.put("level", level);
		ConsumeDiscount consumeDiscount = consumeDiscountService.unique(params);
		double discount = 0;
		if (consumeDiscount != null) {
			consumeDiscount.getDiscount();
		}
		
		Appreciation entity = this.getBaseService().getById(muid);
		double price = entity.getPrice();
		
		String appreciationName = entity.getAppreciationName();
		String appreciationDesc = entity.getAppreciationDesc();
		double memberPrice = price * discount;
		BigDecimal bg = new BigDecimal(memberPrice).setScale(2, RoundingMode.DOWN);
		memberPrice = bg.doubleValue();
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("appreciationName", appreciationName);
		result.put("appreciationDesc", appreciationDesc);
		result.put("price", price);
		result.put("level", level);
		result.put("memberPrice", memberPrice);
		
		data.put("result", result);
		data.put("message", "成功");
		data.put("status", 1);
		return data;
	}
}
