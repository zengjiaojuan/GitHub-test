package com.phb.puhuibao.service;

import com.phb.puhuibao.entity.AddRate;
import com.phb.puhuibao.entity.UserInvestment;

public interface UserInvestmentService {

	void processSave(UserInvestment entity, String redpacketId,AddRate addRate, Double double1);
	void monthProcess(UserInvestment entity);
	void monthProcessLast(UserInvestment entity);
	void processSave(UserInvestment entity, String redpacketId, Double double1);
}
