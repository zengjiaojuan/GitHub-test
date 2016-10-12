package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.WithdrawExceptionLog;

@Repository("withdrawExceptionLogDao")
public class WithdrawExceptionLogDao extends DefaultBaseDao<WithdrawExceptionLog,String> {
	@Override
	public String getNamespace() {
		return WithdrawExceptionLog.class.getName();
	}
}
