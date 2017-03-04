package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.core.service.BaseLoansignService;
import com.cddgg.p2p.core.userinfo.UserInfoQuery;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Attachment;
import com.cddgg.p2p.huitou.entity.Borrowersbase;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.util.Arith;

/**
 * 借款标详细信息
 * @author RanQiBing
 * 2014-04-11
 *
 */
@Service
public class LoanInfoService {
	
	@Resource
	private HibernateSupport dao;
	
	@Resource
	private UserInfoQuery userInfoQuery;
	
	@Resource
	private BaseLoansignService baseLoansignService;
	/**
	 * 查询并判断用户的信用等级
	 * @param userid 用户编号
	 * @return 返回用户信用等级
	 */
	public Integer getCreditRating(Long userid){
		Integer creditRating = 0;
		//查询用户的总积分
		String hql = "from Borrowersbase b where b.userbasicsinfo.id=?";
		Borrowersbase base = (Borrowersbase) dao.findObject(hql, userid);
		//得到用户的总积分
		Integer suminte = 0;
		if(null!=base.getSuminte()){
			suminte = base.getSuminte();
		}
		//判断用户的信用等级
		if(Constant.STATUES_ONE<=suminte && Constant.SRSRUES_TEN>=suminte){
			creditRating = Constant.STATUES_ONE;  //表示半颗星
		}else if(11<=suminte && suminte<=20){
			creditRating = Constant.STATUES_TWO;  //表示一颗星
		}else if(21<=suminte && suminte<=30){
			creditRating = Constant.STATUES_THERE;  //表示一颗半星
		}else if(31<=suminte && suminte<=40){
			creditRating = Constant.STATUES_FOUR;  //表示二颗星
		}else if(41<=suminte && suminte<=50){
			creditRating = Constant.STATUES_FIVE;  //表示二颗半星
		}else if(51<=suminte && suminte<=60){
			creditRating = Constant.STATUES_SIX;  //表示三颗星
		}else if(61<=suminte && suminte<=80){
			creditRating = Constant.STATUES_SEVEN;  //表示三颗半星
		}else if(81<=suminte && suminte<=110){
			creditRating = Constant.STATUES_EIGHT;  //表示四颗星
		}else if(111<=suminte && suminte<=180){
			creditRating = Constant.STATUES_NINE;  //表示四颗半星
		}else if(suminte>180){
			creditRating = Constant.SRSRUES_TEN;  //表示五颗星
		}else{
			creditRating = Constant.STATUES_ZERO;  //表示零颗星
		}
		
		return creditRating;
	}
	/**
	 * 查询一个标的所有认购记录
	 * @param id 标编号 
	 * @return 返回查询结果
	 */
	public PageModel getLoanRecord(Long id,PageModel page){
		String sql = "SELECT u.`name`,l.rate,r.tenderMoney,CASE WHEN r.isSucceed=1 THEN '成功' else '失败' END,date_format(r.tenderTime,'%Y-%m-%d') from loanrecord r,userbasicsinfo u,loansign l where r.userbasicinfo_id=u.id AND r.loanSign_id=l.id AND r.loanSign_id=?";
		String sqls = "select count(r.id) from loanrecord r,userbasicsinfo u,loansign l where r.userbasicinfo_id=u.id AND r.loanSign_id=l.id AND r.loanSign_id=?";
		page.setTotalCount(dao.queryNumberSql(sqls, id).intValue());
		sql=sql+" LIMIT "+(page.getPageNum()-1)*10+","+page.getNumPerPage()+"";
		List<Object[]> list = dao.findBySql(sql, id); 
		page.setList(list);
		return page;
	}
	/**
	 * 获取当前借款标的借款人的所有借款记录
	 * @param id 借款标编号
	 * @param pageNo 当前页数
	 * @return 返回分页内容
	 */
	public PageModel getLoanSignRecord(Long id,PageModel page){
		//获取当前标的信息
		Loansign loan = (Loansign) dao.findObject("from Loansign l where l.id=?",id);
		String sql = "SELECT b.loanNumber,b.loanTitle,l.issueLoan,l.rate,l.refundWay,l.`month`,l.useDay,l.id FROM loansign l,loansignbasics b WHERE l.id=b.id AND l.userbasicinfo_id=? AND l.id!=?";
		String sqlCount = "SELECT count(l.id) FROM loansign l,loansignbasics b WHERE l.id=b.id AND l.userbasicinfo_id=? AND l.id!=?";
		page.setTotalCount(dao.queryNumberSql(sqlCount,loan.getUserbasicsinfo().getId(),id).intValue());
		sql=sql+" LIMIT "+(page.getPageNum()-1)*10+","+page.getNumPerPage()+"";
		List<Object[]> list = dao.findBySql(sql,loan.getUserbasicsinfo().getId(),id); 
		page.setList(list);
		return page;
	}
	/**
	 * 查询该标所有的附件信息
	 * @param id 标编号
	 * @return 返回所有标信息
	 */
	public List<Attachment> getAttachment(long id){
		String hql = "from Attachment a where a.loansign.id=? and a.attachmentType=2";
		List<Attachment> list = dao.find(hql,id);
		return list;
	}
	/**
	 * 获取用户还能购买多少份
	 * @param loan 标信息
	 * @param userinfo 用户信息
	 * @return 返回最大购买份数
	 */
	public Integer getCount(Loansign loan,Userbasicsinfo userinfo){
		int maxcount = 0;
		boolean bool = userInfoQuery.isPrivilege(userinfo);
		//获取当前标的剩余份数
		double moneyLoan = baseLoansignService.sumLoanMoney(loan.getId());
		if(loan.getLoanType()==1){
			int vip = 0;
			if(bool){
				vip = loan.getVipCounterparts();
			}else{
				vip = loan.getCounterparts();
			}
			if(loan.getIssueLoan()-moneyLoan > vip*loan.getLoanUnit()){
				if(userinfo.getUserfundinfo().getMoney()>=vip*loan.getLoanUnit()){
					maxcount=vip;
				}else{
					maxcount = Arith.div(userinfo.getUserfundinfo().getMoney(), loan.getLoanUnit()).intValue();
				}
			}else{
				if(loan.getIssueLoan()-moneyLoan>userinfo.getUserfundinfo().getMoney()){
					maxcount = Arith.div(userinfo.getUserfundinfo().getMoney(), loan.getLoanUnit()).intValue();
				}else{
					maxcount = Arith.div(loan.getIssueLoan()-moneyLoan, loan.getLoanUnit()).intValue();
				}
			}
		}else{
			if(userinfo.getUserfundinfo().getMoney()>loan.getIssueLoan()-moneyLoan){
				maxcount = Arith.div(loan.getIssueLoan()-moneyLoan, loan.getLoanUnit()).intValue();
			}else{
				maxcount = Arith.div(userinfo.getUserfundinfo().getMoney(), loan.getLoanUnit()).intValue();
			}
		}
		return maxcount;
	}

}
