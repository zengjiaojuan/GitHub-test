package com.phb.puhuibao.service;

import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.UserLoan;

public interface UserLoanService {
	public void processLoan(UserLoan entity);

	public void monthProcess(UserLoan entity);

	public void monthProcessLast(UserLoan entity);

	public void closeUser(MobileUser user);
}
