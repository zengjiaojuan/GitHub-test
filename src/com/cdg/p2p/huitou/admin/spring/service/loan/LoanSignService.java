package com.cddgg.p2p.huitou.admin.spring.service.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.HibernateSupportTemplate;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.core.service.BaseLoansignService;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Loanrecord;
import com.cddgg.p2p.huitou.entity.Loansignbasics;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.util.Arith;

/**
 * <p>
 * Title:LoanSignService
 * </p>
 * <p>
 * Description: 普通标服务层
 * </p>
 *         <p>
 *         date 2014年2月14日
 *         </p>
 */
@Service
public class LoanSignService {
	/** dao */
	@Resource
	private HibernateSupportTemplate dao;

	/** loanSignQuery */
	@Resource
	private LoanSignQuery loanSignQuery;

	/** baseLoansignService */
	@Resource
	private BaseLoansignService baseLoansignService;

	/** loansignQueryConditions */
	private String loansignQueryConditions = "";

	/**
	 * 普通标条数
	 * 
	 * @param loansignbasics
	 *            借款标查询对象
	 * @return 条数
	 */
	public int getLoansignCount(Loansignbasics loansignbasics,String loanType) {
		StringBuffer sb = new StringBuffer(
				"SELECT COUNT(1) FROM loansign INNER JOIN loansignbasics ON loansign.id = loansignbasics.id ");
		sb.append(" INNER JOIN userbasicsinfo ON loansign.userbasicinfo_id = userbasicsinfo.id WHERE loansign.loanType = 1");
		sb.append(baseLoansignService.getQueryConditions(loansignbasics,loanType));
		return loanSignQuery.queryCount(sb.toString());
	}

	/**
	 * 普通标列表
	 * 
	 * @param start
	 *            start
	 * @param limit
	 *            limit
	 * @param loansignbasics
	 *            借款标基础信息
	 * @return list
	 */
	public List loanSignPage(int start, int limit, Loansignbasics loansignbasics,String loanType) {
		List list = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT loansign.id, loansignbasics.loanNumber, loansignbasics.loanTitle, ");
		sb.append(" userbasicsinfo. NAME, loansign.loanUnit, loansign.issueLoan, loansign.`month`, ");
		sb.append("loansign_type.typename,");
		sb.append(" loansignbasics.mgtMoney, loansign.publishTime, loansign.rate * 100, loansignbasics.reward * 100 ,");
		sb.append(" round ( IFNULL(( SELECT SUM(tenderMoney) / loansign.loanUnit FROM loanrecord WHERE isSucceed = 1 AND loanrecord.loanSign_id = loansign.id ), 0 )), ");
		sb.append(" round ((SELECT ( loansign.issueLoan - IFNULL(SUM(tenderMoney), 0)) / loansign.loanUnit FROM loanrecord WHERE isSucceed = 1 AND loanrecord.loanSign_id = loansign.id)),");
		sb.append(" CASE WHEN loansign.refundWay = 1 THEN '按月等额本息' WHEN loansign.refundWay = 2 THEN '按月付息到期还本' ELSE '到期一次性还本息' END,");
		sb.append(" CASE WHEN loansign.loanstate = 1 THEN '未发布' WHEN loansign.loanstate = 2 THEN '进行中' WHEN loansign.loanstate = 3 THEN '回款中' WHEN loansign.loanstate = 4 THEN '已完成'  ELSE '流标' END, ");
		sb.append(" CASE WHEN loansign.loanstate = 3 OR loansign.loanstate = 4 THEN '已放款' ELSE '未放款' END, ");
		sb.append(" loansignbasics.creditTime, CASE WHEN loansign.isShow = 1 THEN '显示' ELSE '不显示' END, CASE WHEN loansign.isRecommand = 1 THEN '推荐' ELSE '不推荐' END,loansignbasics.flotTime");
		sb.append(" FROM loansign INNER JOIN loansignbasics ON loansign.id = loansignbasics.id ");
		sb.append(" INNER JOIN userbasicsinfo ON loansign.userbasicinfo_id = userbasicsinfo.id INNER JOIN loansign_type ON loansign.loansignType_id=loansign_type.id WHERE loansign.loanType = 1");
		sb.append(baseLoansignService.getQueryConditions(loansignbasics,loanType));
		sb.append("   ORDER BY loansign.id DESC,loansign.loanstate asc");

		loansignQueryConditions = sb.toString();
		sb.append(" LIMIT ").append(start).append(" , ").append(limit);
		list = dao.findBySql(sb.toString());
		return list;
	}

	/**
	 * 普通标列表转为JSONArray
	 * 
	 * @param list
	 *            集合
	 * @return JSONArray 对象
	 */
	public JSONArray queryJSONByList(List list) {
		JSONObject json = null;
		JSONArray jsonlist = new JSONArray();

		// 给每条数据添加标题
		for (Object obj : list) {
			json = new JSONObject();
			Object[] str = (Object[]) obj;
			json.element("id", str[0]);
			json.element("loanNumber", str[1]);
			json.element("loanTitle", str[2]);
			json.element("name", str[3]);
			json.element("loanUnit", str[4]);
			json.element("issueLoan",
					Arith.round(new BigDecimal(str[5].toString()), 2) + "元");
			json.element("month", str[6] + "个月");
			json.element("loancategory", str[7]);
//			json.element("mgtMoneyScale",
//					Arith.round(new BigDecimal(str[8].toString()), 2) + "%");
			json.element("mgtMoney", str[8]);
			json.element("publishTime", str[9]);
			json.element("rate",
					Arith.round(new BigDecimal(str[10].toString()), 2) + "%");
			json.element("reward",
					Arith.round(new BigDecimal(str[11].toString()), 2) + "%");
			json.element("successfulLending", str[12]);
			json.element("remainingCopies",
					Double.valueOf(str[13].toString()) > 0 ? str[13] : "满标");
			json.element("refundWay", str[14]);
			json.element("loanstate", str[15]);
			json.element("iscredit", str[16]);
			json.element("creditTime", str[17]);
			json.element("isShow", str[18]);
			json.element("isRecommand", str[19]);
			json.element("flotTime", str[20]);
			jsonlist.add(json);
		}
		return jsonlist;
	}

	/***
	 * 要导出的借款列表数据
	 * 
	 * @return List
	 */
	public List outPutList() {
		return dao.findBySql(loansignQueryConditions.toString());
	}

	/**
	 * <p>
	 * Title: updateRepaymentRecord
	 * </p>
	 * <p>
	 * Description: 逾期已还款的时候调用改信息
	 * </p>
	 * 
	 * @param repaymentrecord
	 *            还款记录
	 * @param repayTime
	 *            还款时间
	 * @return 是否成功
	 */
	public boolean updateRepaymentRecord(Repaymentrecord repaymentrecord,
			String repayTime) {
		// try {
		// 写入实际还款时间和状态=4,并计算出实际的还款金额=本该还的利息和逾期的手续费
		repaymentrecord.setRepayState(4);
		repaymentrecord.setRepayTime(repayTime);
		repaymentrecord.setRealMoney(repaymentrecord.getPreRepayMoney()
				+ (repaymentrecord.getMoney() + repaymentrecord
						.getPreRepayMoney()) * Constant.OVERDUE_INTEREST);
		dao.update(repaymentrecord);
		return true;
		// } catch (Exception e) {
		// return false;
		// }
	}

	/**
	 * 根据用户编号查询用户在当前时间以前购买的所有标的本息和
	 * 
	 * @param id
	 *            用户编号
	 * @return 返回所有本息和
	 */
	public Double getLoanRecordMoney(Long id) {
		String sql = "SELECT l.issueLoan, loan.tenderMoney, SUM(DISTINCT r.money), SUM(DISTINCT r.preRepayMoney), loan.tenderMoney / l.issueLoan * SUM(DISTINCT r.preRepayMoney), loan.tenderMoney / l.issueLoan * SUM(DISTINCT r.money) + loan.tenderMoney / l.issueLoan * SUM(DISTINCT r.preRepayMoney) FROM loanrecord loan INNER JOIN loansign l ON loan.loanSign_id = l.id INNER JOIN repaymentrecord r ON l.id = r.loanSign_id WHERE loan.userbasicinfo_id IN(SELECT b.userbasicinfo_id from borrowersbase b , borrowers_apply a WHERE b.id = a.base_id AND a.id=?) AND l.loanstate = 3 AND loan.tenderTime < ? AND r.repayState = 1 GROUP BY r.loanSign_id";
		List<Object[]> loanList = (List<Object[]>) dao.findBySql(sql,id,
				DateUtils.format("yyyy-MM-dd"));
		//得到当前用户当前时间之前为完成标的本息和
		Double moneyAndInterest = 0.00;
		if(loanList.size() > 0&&null != loanList){
			for (int i = 0; i < loanList.size(); i++) {
				Object[] obj = loanList.get(0);
				moneyAndInterest+=Double.parseDouble(obj[5].toString());
			}
		}
		return moneyAndInterest;
	}
	
	/**
	 * 查询该用户是否还有未完成的净值标
	 * @param id 用户编号
	 * @return 返回标的个数
	 */
	public Integer getNetLabel(Long id){
		String sql = "select count(*) from loansign l where l.loansignType_id=4 and l.loanType=1 and l.userbasicinfo_id=? and l.loanstate!=4";
		int num = dao.queryNumberSql(sql,id).intValue();
		return num;
	}
}
