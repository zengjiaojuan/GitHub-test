package com.phb.puhuibao.common;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.entity.HexagramCredit;
import com.phb.puhuibao.entity.MobileUser;


public class hexagramMaintenance {
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;
	
	@Resource(name = "hexagramcreditService")
	private IBaseService<HexagramCredit, String> hexagramcreditService; 
	
	/**
	 * 维护 phb_hexagram_credit
	 * @param m_user_id 用户id 
	 * @param authentication 身份认证
	 * @return
	 */
	public  void updateCredit(int  mUserId, double authentication) {
		HexagramCredit hc = new HexagramCredit();
		hc.setmUserId(mUserId);
		hc.setIdAuthentication(authentication);
		hexagramcreditService.update(hc);
	}
	
	
 

	 

}
