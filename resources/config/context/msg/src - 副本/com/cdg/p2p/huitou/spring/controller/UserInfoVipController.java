package com.cddgg.p2p.huitou.spring.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.base.model.PageModel;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Vipinfo;
import com.cddgg.p2p.huitou.entity.Viptype;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.UserBaseInfoService;
import com.cddgg.p2p.huitou.spring.service.UserInfoVipService;
import com.cddgg.p2p.huitou.spring.service.VipInfoService;
import com.cddgg.p2p.pay.entity.PaymentIpsInfo;
import com.cddgg.p2p.pay.entity.PaymentReturnInfo;

/**
 * 会员升级
 * @author RanQiBing
 * 2014-04-17
 *
 */
@Controller
@RequestMapping("/userinfovip")
@CheckLogin(value = CheckLogin.WEB)
public class UserInfoVipController {
	
	@Resource
	private UserInfoVipService userInfoVipService;
	
	@Resource
	private VipInfoService vipInfoService;
	
	private DecimalFormat df=new DecimalFormat("0.00");
	
	@RequestMapping("upgrade.htm")
	public String openVip(HttpServletRequest request){
		Userbasicsinfo userinfo = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		request.setAttribute("type",userInfoVipService.getType());
		Object obj = vipInfoService.isVip(userinfo.getId());
		int typeVip = 0;
		if(null != obj){
			typeVip = 1;
		}
		request.setAttribute("typeVip", typeVip);
		Vipinfo vip = userInfoVipService.getuserId(userinfo.getId());
		if(null!=vip){
			request.setAttribute("time",vip.getEndtime());
		}
		return "WEB-INF/views/member/vippay/vipsevers";
	}
	/**
	 * 提交会员升级订单
	 * @param money 金额
	 * @param type 支付方式
	 * @return 返回地址
	 */
	@RequestMapping("payment.htm")
	public String payment(String money,String type,Long id,HttpServletRequest request){
		Userbasicsinfo userinfo = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		PaymentIpsInfo payment = new PaymentIpsInfo();
		if(null==id){
			Viptype viptype = userInfoVipService.getType();
			payment.setAmount(df.format(Double.parseDouble(money)));
			payment.setGateway_Type(type);
			Vipinfo vipinfo = new Vipinfo(payment.getBillno(),Constant.STATUES_ONE,userinfo,viptype);
			userInfoVipService.save(vipinfo);
		}else{
			Vipinfo vip = userInfoVipService.get(id);
			payment.setBillno(vip.getNumber());
			payment.setAmount(df.format(vip.getViptype().getMoney()));
		}
		request.setAttribute("payment",payment);
		return "WEB-INF/views/paymentips";
	}
	/**
	 * 环迅支付返回信息的处理
	 * @param ipsInfo 支付对象
	 * @param request 
	 * @return 返回页面
	 */
	@RequestMapping("sign.htm")
	public String sign(PaymentReturnInfo returnInfo,HttpServletRequest request){
		if(returnInfo.getSucc().equals("Y")){
			if(returnInfo.getSignature().equals(returnInfo.getSignmd5())){
				//获取当前订单
				Vipinfo vip = userInfoVipService.getVipNumber(returnInfo.getBillno());
				vip.setBankbillno(returnInfo.getBankbillno());
				vip.setIpsbillno(returnInfo.getIpsbillno());
				vip.setStatus(Constant.STATUES_TWO);
				Vipinfo vips = userInfoVipService.getuserId(vip.getUserbasicsinfo().getId(),vip.getId());
				int num = 0;
				try {
					if(null!=vips){
						if(null!=vips.getEndtime()){
							num = DateUtils.differenceDate("yyyy-MM-dd", DateUtils.format("yyyy-MM-dd"), vips.getEndtime());
							if(num>0){
								vip.setBegintime(DateUtils.add("yyyy-MM-dd HH:mm:ss",vips.getEndtime(),Calendar.DATE, 1));
							}else{
								vip.setBegintime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
							}
						}else{
							vip.setBegintime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
						}
					}else{
						vip.setBegintime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
					}
					vip.setEndtime(DateUtils.add("yyyy-MM-dd HH:mm:ss",vip.getBegintime(),Calendar.MONTH,12));
				}catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				userInfoVipService.update(vip);
			}
		}else{
			LOG.error("环迅支付失败-》错误信息:"+returnInfo.getMsg());
		}
		return "WEB-INF/views/member/vippay/viprecord";
	}
	/**
	 * 打开记录页面
	 * @param request
	 * @return
	 */
	@RequestMapping("vipRecords.htm")
	public String vipRecords(HttpServletRequest request){
		
		return "WEB-INF/views/member/vippay/viprecord";
	}
	/**
	 * 分页查询用户升级会员信息
	 * @param no 当前页
	 * @param request
	 * @return 返回页面路径
	 */
	@RequestMapping("vipRecord.htm")
	public String vipRecord(int no,HttpServletRequest request){
		Userbasicsinfo userinfo = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		PageModel page = new PageModel();
		page.setPageNum(no);
		request.setAttribute("page",userInfoVipService.getVipInfo(userinfo.getId(), page));
		return "WEB-INF/views/member/vippay/recordtable";
	}
	/**
	 * 删除数据
	 * @param id 会员编号
	 * @return
	 */
	@RequestMapping("delete.htm")
	@ResponseBody
	public String deletes(Long id){
		try {
			userInfoVipService.delete(userInfoVipService.get(id));
			return "1";
		} catch (Exception e) {
			return "0";
			// TODO: handle exception
		}
	}
	
}
