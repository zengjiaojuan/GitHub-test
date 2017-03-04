package com.cddgg.p2p.huitou.spring.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;


import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.model.Prepayment;
import com.cddgg.p2p.huitou.util.Arith;

/**
 * 还款业务处理
 * @author RanQiBing 2014-05-14
 *
 */
@Service
public class RepayMentServices {
	
	 @Resource
	 private HibernateSupport dao;
	/**
	 * 提前还款
	 * @param userId 用户编号
	 * @return 返回还款记录
	 * @throws ParseException 
	 */
	public List<Prepayment> advanceRepayment(Long userId) throws ParseException{
		String sql = "SELECT r.id, s.loanNumber, l.issueLoan, r.periods, l.`month`, ( SELECT SUM(repaymentrecord.money) FROM repaymentrecord WHERE repaymentrecord.repayState = 1 AND repaymentrecord.loanSign_id = l.id ), ( SELECT SUM(re.preRepayMoney) FROM repaymentrecord re WHERE re.repayState = 1 AND re.loanSign_id = l.id ) * 0.5, r.preRepayDate, l.id, r.preRepayMoney,l.refundWay,s.creditTime FROM repaymentrecord r, loansign l, loansignbasics s WHERE r.loanSign_id = l.id AND l.id = s.id and l.loanType=1 AND l.loanstate = 3 AND l.userbasicinfo_id = ? AND r.repayState = 1 GROUP BY l.id";
		List<Object[]> list = dao.findBySql(sql,userId);
		List<Prepayment> listPre = new ArrayList<>();
		if(null!=list && list.size() > 0){
			for(int i=0;i<list.size();i++){
				Prepayment pre = new Prepayment();
				int timeNum = DateUtils.differenceDate("yyyy-MM-dd",DateUtils.format("yyyy-MM-dd"),list.get(i)[7].toString());
				if(timeNum<0){
					continue;
				}else{
					pre.setId(Long.parseLong(list.get(i)[0].toString()));
					pre.setLoanNumber(list.get(i)[1].toString());
					pre.setMoney(Double.parseDouble(list.get(i)[2].toString()));
					pre.setLoanid(Long.parseLong(list.get(i)[8].toString()));
					if(Integer.parseInt(list.get(i)[10].toString())==1 || Integer.parseInt(list.get(i)[10].toString())==2){
						pre.setPeriods(list.get(i)[3].toString()+"/"+list.get(i)[4].toString());
						pre.setIntester(Double.parseDouble(list.get(i)[9].toString()));
						pre.setPenalty(Double.parseDouble(list.get(i)[6].toString()) - pre.getIntester()*0.5);
						pre.setSurplusMoney(Double.parseDouble(list.get(i)[5].toString()));
						pre.setCountMoney(pre.getIntester()+pre.getPenalty()+pre.getSurplusMoney());
					}else{
						int month = StringUtil.getMonth(list.get(i)[11].toString()+" 00:00:00",DateUtils.format("yyyy-MM-dd HH:mm:ss"))+1;
						double interest = Arith.div(Double.parseDouble(list.get(i)[9].toString()), Integer.parseInt(list.get(i)[4].toString()), 2).doubleValue();
						pre.setPeriods("1/1");
						pre.setIntester(interest*month);
						pre.setPenalty(interest*(Integer.parseInt(list.get(i)[4].toString())-month)*0.5);
						pre.setSurplusMoney(Double.parseDouble(list.get(i)[5].toString()));
						pre.setCountMoney(pre.getIntester()+pre.getPenalty()+pre.getSurplusMoney());
					}
				listPre.add(pre);
				}
			}
		}
		return listPre;
	}
	
	/**
	 * 按时还款
	 * @param userId 用户编号
	 * @return 返回还款记录
	 * @throws ParseException 
	 */
	public List<Repaymentrecord> scheduleRepayment(Long userId) throws ParseException{
		String sql = "SELECT * FROM repaymentrecord, loansign WHERE repaymentrecord.loanSign_id = loansign.id AND repaymentrecord.preRepayDate BETWEEN date_add( repaymentrecord.preRepayDate, INTERVAL '-7' DAY ) AND repaymentrecord.preRepayDate AND date_add( repaymentrecord.preRepayDate, INTERVAL '-7' DAY ) <= ? AND loansign.loanstate = 3 AND loansign.userbasicinfo_id = ? AND repaymentrecord.repayState = 1 GROUP BY loansign.id";
//		String hql = "from Repaymentrecord r where r.preRepayDate=? and r.repayState=1 and r.loansign.userbasicsinfo.id=?";
		List<Repaymentrecord> list = dao.findBySql(sql, Repaymentrecord.class, DateUtils.format("yyyy-MM-dd"),userId);
		return list;
	}
	
	/**
	 * 逾期还款
	 * @param userId 用户编号
	 * @return 返回还款记录
	 */
	public List<Repaymentrecord> overdueRepayment(Long userId){
		String sql = "SELECT * from repaymentrecord,loansign WHERE repaymentrecord.loanSign_id=loansign.id AND loansign.loanstate=3 AND loansign.userbasicinfo_id=? AND repaymentrecord.preRepayDate<? AND repaymentrecord.repayState=1 GROUP BY loansign.id";
		List<Repaymentrecord> list = dao.findBySql(sql, Repaymentrecord.class, userId,DateUtils.format("yyyy-MM-dd"));
		return list;
	}
	/**
	 * 获取用户借款总额
	 * @param userId 用户编号
	 * @return 用户借款总额
	 */
	public Double getMoney(Long userId){
		String sql = "SELECT SUM(repaymentrecord.money) as k from repaymentrecord,loansign where repaymentrecord.loanSign_id=loansign.id AND repaymentrecord.repayState=1 and loansign.userbasicinfo_id=?";
		return dao.queryNumberSql(sql, userId);
	}
	/**
	 * 获取用户借款标信息
	 * @param userId 用户编号
	 * @return 条数
	 */
	public int getNum(Long userId){
		String sql = "select count(*) from loansign where loanstate=3 and userbasicinfo_id=?";
		return dao.queryNumberSql(sql, userId).intValue();
	}
}
