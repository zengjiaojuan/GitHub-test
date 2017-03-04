package com.cddgg.p2p.huitou.spring.service.sign;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.HibernateSupportTemplate;

/**
 * 
 * 针对标的一些公用的方法
 * @author ranqibing
 *
 */
@Service
public class SignPublic {
	@Resource
	private HibernateSupportTemplate hibernateDao; 
	/**
	 * 
	 * 根据标的类型和条件查新标的相关信息
	 * @param type 标的类型(普通标、天标、秒标、流转标)
	 * @param title 标的标题
	 * @param grade 标号
	 * @param name  借款人名称
	 * @param state 标的状态(未发布、进行中、回款中、已完成、流标)
	 * @param areLenders 是否放款
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return 查询的标的相关信息的集合
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryList(int type,String title,String grade,String name,String state,String areLenders,String beginTime,String endTime){
		String sql=SignSql.querySql(type, title, grade, name, state, areLenders, beginTime, endTime);
		List<Object[]> list=(List<Object[]>) hibernateDao.findBySql(sql);
		return list;
	}
	/**
	 * 根据标的编号或者借出人编号查询借出记录
	 * @param user_id 借出人编号
	 * @param sign_id 标的编号
	 * @return 标相关的借出记录信息
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryRecordList(Long user_id,Long sign_id){
		String sql=SignSql.queryRecordSql(user_id, sign_id);
		List<Object[]> list=(List<Object[]>) hibernateDao.findBySql(sql);
		return list;
	}
}
