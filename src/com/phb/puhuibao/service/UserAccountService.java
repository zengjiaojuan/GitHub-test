package com.phb.puhuibao.service;

import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.ResourceOrder;
import com.phb.puhuibao.entity.UserAccount;

public interface UserAccountService {

	void confirm(UserAccount entity);

	UserAccount processSave(UserAccount entity);

	int processDelete(int accountId);

	void refund(String orderId);

	void adminCreate(UserAccount entity);

	void processSave(UserAccount entity, ResourceOrder o, int accountType);

	void processSave(UserAccount entity, Resource r, int accountType);

}
