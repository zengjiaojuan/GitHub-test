package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.WithdrawExceptionLog;
import com.phb.puhuibao.service.WithdrawExceptionLogService;

@Transactional
@Service("withdrawExceptionLogService")
public class WithdrawExceptionLogServiceImpl extends DefaultBaseService<WithdrawExceptionLog, String> implements WithdrawExceptionLogService {
	@Override
	@Resource(name = "withdrawExceptionLogDao")
	public void setBaseDao(IBaseDao<WithdrawExceptionLog, String> baseDao) {
		super.setBaseDao(baseDao);
	}
	@Override
	@Resource(name = "withdrawExceptionLogDao")
	public void setPagerDao(IPagerDao<WithdrawExceptionLog> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
}
