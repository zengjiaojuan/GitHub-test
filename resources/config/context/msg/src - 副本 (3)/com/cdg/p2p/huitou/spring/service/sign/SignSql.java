package com.cddgg.p2p.huitou.spring.service.sign;

import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.normal.Validate;

/**
 * 
 * 针对标的一些查询的sql语句
 * @author ranqibing
 *
 */
public class SignSql {
	/**
	 * 标的查询和按条件查询
	 * @param type 标的类型(普通标、天标、秒标、流转标)
	 * @param title 标的标题
	 * @param grade 标号
	 * @param name  借款人名称
	 * @param state 标的状态(未发布、进行中、回款中、已完成、流标)
	 * @param areLenders 是否放款
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
    public static String querySql(int type,String title,String grade,String name,String state,String areLenders,String beginTime,String endTime){
    	StringBuffer sql=new StringBuffer("SELECT b.loanNumber,b.loanTitle,c.`name`,a.loanUnit,b.issueLoan,a.`month`,a.publishTime,a.rate,b.reward,SUM(b.issueLoan/a.loanUnit),");
     	sql.append("a.loanstate FROM loansign a INNER JOIN loansignbasics b ON a.id = b.loansign_id INNER JOIN userfundinfo u ON a.userfundinfo_id = u.id ");
    	sql.append("INNER JOIN userbasicsinfo c ON u.id = c.userfundinfo_id  WHERE a.loanType=");
    	sql.append(type);
    	if(Validate.emptyStringValidate(title)){
    		sql.append(" and b.loanTitle=").append(title);
    	}
    	if(Validate.emptyStringValidate(grade)){
    		sql.append(" and b.loanNumber=").append(grade);
    	}
    	if(Validate.emptyStringValidate(name)){
    		sql.append(" c.`name`=").append(name);
    	}
    	if(Validate.emptyStringValidate(state)){
    		int num=Integer.parseInt(state);
    		sql.append(" a.loanstate=").append(num);
    	}
//    	if(Validate.emptyStringValidate(areLenders)){
//    		sql.append(" c.`name`=").append(name);
//    	}
    	if(Validate.emptyStringValidate(beginTime)){
    		sql.append(" and TO_DAYS(a.publishTime) - TO_DAYS('").append(beginTime).append("') >=0");
    	}
    	if(Validate.emptyStringValidate(endTime)){
    		sql.append(" and TO_DAYS(a.publishTime) - TO_DAYS('").append(endTime).append("') <=0");
    	}
     	return sql.toString();
    }
    /**
     * 查询标的购买记录
     * @param user_id 会员编号
     * @param sign_id 标编号
     * @return
     */
    public static String queryRecordSql(Long user_id,Long sign_id){
    	StringBuffer str=new StringBuffer("SELECT b.userName,b.`name`,l.rate,a.tenderMoney,a.isSucceed,a.tenderTime");
    	str.append(" FROM loansign l INNER JOIN loanrecord a ON l.id=a.loanSign_id INNER JOIN userfundinfo u ON a.userfundinfo_id = u.id");
    	str.append(" INNER JOIN userbasicsinfo b ON u.id = b.userfundinfo_id where 1=1");
    	if(null!=user_id){
    		str.append(" and a.userfundinfo_id=").append(user_id);
    	}
    	if(null!=sign_id){
    		str.append(" and a.loanSign_id=").append(sign_id);
    	}
    	return str.toString();
    }
}
