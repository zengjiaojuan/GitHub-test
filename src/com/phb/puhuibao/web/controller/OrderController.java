package com.phb.puhuibao.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.Appreciation;
import com.phb.puhuibao.entity.ConsumeDiscount;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserExtra;
import com.phb.puhuibao.entity.Order;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.service.OrderService;

@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseController<Order, String> {
	@Resource(name = "orderService")
	public void setBaseService(IBaseService<Order, String> baseService) {
		super.setBaseService(baseService);
	}

	@Resource(name = "orderService")
	private OrderService orderService;
	
	@Resource(name = "appreciationService")
	private IBaseService<Appreciation, String> appreciationService;

	@Resource(name = "mobileUserExtraService")
	private IBaseService<MobileUserExtra, String> mobileUserExtraService;

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "consumeDiscountService")
	private IBaseService<ConsumeDiscount, String> consumeDiscountService;

	@Resource(name = "userInvestmentService")
	private IBaseService<UserInvestment, String> userInvestmentService;

	/**
	 * 保存订单，返回信息：
	 		订单号
			商户名称
			增值服务名称
			单价
			折扣
			数量
			总价
	 * @param muid
	 * @param appreciationId
	 * @param number
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam int appreciationId, @RequestParam int number) {
		Appreciation appreciation = appreciationService.getById("" + appreciationId);
		if (appreciation.getCatId() == 1) {
			Map<String, Object> data = new HashMap<String, Object>();
			Order entity = new Order();
			entity.setAppreciationId(appreciationId);
			entity.setMemberPrice(0.0);
			entity.setmUserId(muid);
			entity.setNumber(number);
			entity.setPrice(0.0);
			try {
			    this.getBaseService().save(entity);
			    entity.setPayStatus(1);
			    this.getBaseService().update(entity);
			} catch (Exception e) {
				data.put("message", "网络异常！");
				data.put("status", 0);			
				return data;
			}
			data.put("message", "保存成功！");
			data.put("status", 1);
			return data;			
		}
		double price = appreciation.getPrice();

		MobileUserExtra extra = mobileUserExtraService.getById("" + muid);
//		int level = 0;
//		if (extra != null && extra.getLevel() != null) {
//			level = extra.getLevel();
//		}
		MobileUser user = mobileUserService.getById(muid + "");
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
		double memberPrice = price * discount;
		BigDecimal bg = new BigDecimal(memberPrice).setScale(2, RoundingMode.DOWN);
		memberPrice = bg.doubleValue();
		double amount = memberPrice * number;

		params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		params.put("leStatus", 1);
		List<UserInvestment> investments = userInvestmentService.findList(params);
		long totalAsset = 0;
		for (UserInvestment investment : investments) {
			totalAsset += investment.getInvestmentAmount();
		}
		//MobileUser user = mobileUserService.getById("" + muid);
		totalAsset += user.getmUserMoney();
		Double frozenMoney = user.getFrozenMoney();

		Map<String, Object> data = new HashMap<String, Object>();
		if (amount > totalAsset - frozenMoney) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("totalAsset", totalAsset);
			result.put("frozenMoney", frozenMoney);
			data.put("result",result);
			data.put("message", "您的资产不足以支付本次消费！");
			data.put("status", 0);			
			return data;			
		}
		
		
		Order entity = new Order();
		entity.setAppreciationId(appreciationId);
		entity.setMemberPrice(memberPrice);
		entity.setmUserId(muid);
		entity.setNumber(number);
		entity.setPrice(price);

		try {
		    this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);			
			return data;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", entity.getOrderId());
		result.put("merchantName", extra == null ? "" : extra.getMerchantName());
		result.put("appreciationName", appreciation.getAppreciationName());
		result.put("price", price);
		result.put("discount", discount);
		result.put("number", number);
		result.put("amount", amount);
		
		data.put("result",result);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="update")
	@ResponseBody
	public Map<String, Object> update(@RequestParam int orderId) {
		Order order = this.getBaseService().getById("" + orderId);
		int appreciationId = order.getAppreciationId();
		Appreciation appreciation = appreciationService.getById("" + appreciationId);
		double price = appreciation.getPrice();

//		MobileUserExtra extra = mobileUserExtraService.getById("" + order.getmUserId());
//		int level = 0;
//		if (extra != null && extra.getLevel() != null) {
//			level = extra.getLevel();
//		}
		MobileUser user = mobileUserService.getById(order.getmUserId() + "");
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
		double memberPrice = price * discount;
		BigDecimal bg = new BigDecimal(memberPrice).setScale(2, RoundingMode.DOWN);
		memberPrice = bg.doubleValue();
		double brokerage = memberPrice * order.getNumber() * appreciation.getBrokerageRate();
		bg = new BigDecimal(brokerage).setScale(2, RoundingMode.DOWN);
		brokerage = bg.doubleValue();

		order = new Order();
		order.setOrderId(orderId);
		order.setBrokerage(brokerage);
		order.setPayStatus(1);

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			orderService.processUpdate(order);
		} catch (Exception e) {
			data.put("message", "失败！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
		data.put("message", "成功！");
		data.put("status", 1);
		return data;
	}
}
