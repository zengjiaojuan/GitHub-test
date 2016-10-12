package com.phb.puhuibao.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.service.UserAccountLogService;

@Transactional
@Service("userAccountLogService")
public class UserAccountLogServiceImpl extends DefaultBaseService<UserAccountLog, String> implements UserAccountLogService{
	@Override
	@Resource(name = "userAccountLogDao")
	public void setBaseDao(IBaseDao<UserAccountLog, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	@Resource(name = "userAccountLogDao")
	public void setPagerDao(IPagerDao<UserAccountLog> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public UserAccountLog save(UserAccountLog entity) {
		return  this.getBaseDao().save(entity);
	}
	@Override
	public   BigDecimal getBalanceMoney(int m_user_id){
		BigDecimal balanceAmount=new BigDecimal("0");
		String sql="select sum(amount)from phb_muser_account_log where account_type in (0,1,2,3,4,5,6,7) and m_user_id="+m_user_id;
		List<Map<String, Object>> list=this.jdbcTemplate.queryForList(sql);
		list=this.jdbcTemplate.queryForList(sql);
		 if(list.size()>0){
			 balanceAmount=(BigDecimal) list.get(0).get("sum(amount)");
			 }
		return balanceAmount;
	}
}
