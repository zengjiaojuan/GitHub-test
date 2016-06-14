package com.phb.puhuibao.service;

import com.phb.puhuibao.entity.UserAccount;

public interface UserAccountService {

	void confirm(UserAccount entity);

	UserAccount processSave(UserAccount entity);

	int processDelete(int accountId);

	void refund(String orderId);

	void adminCreate(UserAccount entity);

 

	void chargeCallBack(String cardno, String identitynumber, String username, String usertel, String amount,
			String oid_paybill,String reqStr);

 

}
