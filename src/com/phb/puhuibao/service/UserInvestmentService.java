package com.phb.puhuibao.service;

import com.phb.puhuibao.entity.UserInvestment;

public interface UserInvestmentService {

	void processSave(UserInvestment entity, String redpacketId);
	void monthProcess(UserInvestment entity);
	void monthProcessLast(UserInvestment entity);
}
