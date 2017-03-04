package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;

/**
 * 我的收支单
 * @author Administrator
 *
 */
@Resource
@SuppressWarnings(value = { "rawtypes" })
public class MyExpensesService {
	
	/**
	 *注入接口
	 */
	@Resource
    private HibernateSupport dao;
	
	/**
	 * 得到事件类型
	 * @return
	 */
	public List queryType(Long id){
		String sql="SELECT b.id,b.name"
				+ " FROM accountinfo a,accounttype b"
				+ " WHERE a.accounttype_id=b.id AND a.userbasic_id=? GROUP BY b.id";
		List list=dao.findBySql(sql,id);
		return list;
	}
	
	/**
	 * 根据条件查询条数
	 * @param beginTime
	 * @param endTime
	 * @param typeId 事件类型
	 * @return
	 */
	public Object getCount(String beginTime,String endTime,Long typeId,Long userId){
		StringBuffer sql=new StringBuffer(
				"SELECT count(*) FROM accountinfo a,accounttype b"
				+ " WHERE a.accounttype_id=b.id AND a.userbasic_id=?");
		sql.append(connectSql(beginTime, endTime, typeId));
		Object obj=dao.findObjectBySql(sql.toString(),userId);
		return obj;
	}
	
	/**
	 * 查询收支记录
	 * @param userId 用户id
	 * @param beginTime
	 * @param endTime
	 * @param typeId 事件类型
	 * @param page
	 * @return
	 */
	public List queryExpense(Long userId,String beginTime,String endTime,
			Long typeId,PageModel page){
		StringBuffer sql=new StringBuffer(
				"SELECT a.id,a.time,b.name,a.expenditure,a.income,a.explan"
				+ " FROM accountinfo a,accounttype b WHERE a.accounttype_id=b.id AND a.userbasic_id="+userId);
		sql.append(" AND a.explan NOT LIKE '管理费' ");
		sql.append(connectSql(beginTime, endTime, typeId))
		.append(" ORDER BY a.time DESC LIMIT "+page.firstResult()+","+page.getNumPerPage());
		List list=dao.findBySql(sql.toString());
		return list;
	}
	
	/**
	 * 拼接SQL语句
	 * @param beginTime
	 * @param endTime
	 * @param typeId
	 * @return
	 */
	public String connectSql(String beginTime,String endTime,Long typeId){
		StringBuffer buf=new StringBuffer("");
		if(typeId!=null && !"".equals(typeId)){
			buf.append(" AND b.id="+typeId);
		}
		if(beginTime!=null && !"".endsWith(beginTime)){
			buf.append(" AND DATE_FORMAT(a.time,'%Y-%m-%d')>='"+beginTime+"'");
		}
		if(endTime!=null && !"".equals(endTime)){
			buf.append(" AND DATE_FORMAT(a.time,'%Y-%m-%d')<='"+endTime+"'");
		}
		return buf.toString();
	}
}
