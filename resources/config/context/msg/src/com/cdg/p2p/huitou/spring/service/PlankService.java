package com.cddgg.p2p.huitou.spring.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.entity.Accountinfo;
import com.cddgg.p2p.huitou.entity.Accounttype;
import com.cddgg.p2p.huitou.entity.BlotterRecord;
import com.cddgg.p2p.huitou.entity.Loanrecord;
import com.cddgg.p2p.huitou.entity.Loansignbasics;

/**
 * 购标
 * @author RanQiBing 
 * 2014-04-10
 */
@Service
public class PlankService {
	
	@Resource
	private HibernateSupport dao;
	/**
	 * 
	 * @param loanrecord
	 * @param accountinfo
	 * @param money
	 */
	public void update(Loanrecord loanrecord,Accountinfo accountinfo,Double money,Loansignbasics loansignbasics,String number){
		dao.save(loanrecord);
		dao.save(accountinfo);
		dao.save(loansignbasics);
		String sql = "UPDATE userfundinfo SET userfundinfo.cashBalance=? where id = ?";
		dao.executeSql(sql,money,loanrecord.getUserbasicsinfo().getId());
		String sqlrecord = "update blotterrecord set isSucceed=1 where number=?";
		dao.executeSql(sqlrecord,number);
	}
	/**
	 * 获取类型
	 * @param id 类型编号
	 * @return
	 */
	public Accounttype accounttype(Long id){
		return dao.get(Accounttype.class, id);
	}
	
	/**
	 * 查询当前时间以前的认购金额得到标的剩余金额
	 * @param id
	 * @return
	 */
	public Double getMoney(Long id){
		String sql = "SELECT (SELECT issueLoan from loansign where id = ? ) - (select IFNULL(SUM(tenderMoney),0.00) from blotterrecord where loanSign_id=? and (isSucceed = 1 OR (isSucceed = 0 AND endTime>?)))";
		Double money = dao.queryNumberSql(sql,id,id,DateUtils.format("YYYY-MM-dd HH:mm:ss"));
		if(null!=money){
			return money;
		}else{
			return 0.00;
		}
	}
	
	/**
	 * 添加临时数据
	 * @param blotterRecord
	 */
	public void save(BlotterRecord blotterRecord){
		dao.save(blotterRecord);
	}
	/**
	 * 修改临时数据状态
	 * @param number
	 */
	public void update(String number){
		String sqlrecord = "update blotterrecord set isSucceed=2 where number=?";
		dao.executeSql(sqlrecord,number);
	}
	
	/**
	    * <p>Title: check_pMerBillNo</p>
	    * <p>Description: 检查订单是否存在</p>
	    * @param str 订单号码
	    * @return true 表示不存在，false表示存在
	    */
	    public boolean check_pMerBillNo(String str){
	        
	        String hql="FROM Accountinfo WHERE ipsNumber=?";
	        
	        Object obj=dao.findObject(hql, str);
	        
	        if(null==obj){
	            return true;
	        }else{
	            return false;
	        }
	        
	    }
	
}
