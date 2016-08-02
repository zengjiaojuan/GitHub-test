package com.phb.puhuibao.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jsoup.helper.StringUtil;
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
	@Override
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
	@RequestMapping(value="pageQuery")
	@ResponseBody
	public Map<String, Object> pageQuery(@RequestParam int pageno, @RequestParam String muid, String type) {
		// 提现  0,8
		Pager<UserAccountLog> pager = new Pager<UserAccountLog>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(30);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("mUserId", muid);
		map.put("orderBy", "create_time");
		map.put("order", "desc");
		//map.put("accountType", type);
		if(!StringUtil.isBlank(type)){
			map.put("accountTypeString", type);
		}
		
		//0: 提现 1:充值   2: 下线投资提成 3:理财体验收益 4:投资 5:红包抵资 6:投资收益 7:投资赎回 8:提现中 
		//9:提现取消 10:退款完成等待入账 11:授信贷款 12授信贷款月息 13:授信贷款还本 14:投资平仓 15:贷款平仓
		Pager<UserAccountLog> p=this.getBaseService().findByPager(pager, map);
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<UserAccountLog> result = p.getData();
		  for(UserAccountLog log:result){
			if(log.getAccountType() == 1 || log.getAccountType() == 2 || log.getAccountType() ==  3 || log.getAccountType() == 6 || log.getAccountType() == 9 || log.getAccountType() == 14){
				//处理提现冻结 精度丢失问题 
				BigDecimal amountDB = new BigDecimal(log.getAmount()).setScale(2, RoundingMode.HALF_UP);
				log.setFlagAmount(""+amountDB);
			}else if(log.getAccountType() == 0 || log.getAccountType() == 4 || log.getAccountType() == 10 || log.getAccountType() == 11 || log.getAccountType() == 12 || log.getAccountType() == 13 || log.getAccountType() == 15 || log.getAccountType() == 8){
				//处理提现冻结 精度丢失问题 
				BigDecimal amountDB = new BigDecimal(log.getAmount()).setScale(2, RoundingMode.HALF_UP);
				log.setFlagAmount(""+amountDB);
			}else if(log.getAccountType() ==  5 || log.getAccountType() == 7){
				//处理提现冻结 精度丢失问题 
				BigDecimal amountDB = new BigDecimal(log.getAmount()).setScale(2, RoundingMode.HALF_UP);
				log.setFlagAmount(""+amountDB);
			}
		}
		
		
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
}
