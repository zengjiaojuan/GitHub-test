package com.cddgg.base.entity;
/**
 * 存放关于投资人的每个借款标的每期还款信息
 * @author RanQiBing 
 * 2014-04-01
 *
 */
public class ExpensesInfo {
	
	/**
	 * 标编号
	 */
	private Long loanid;
	/**
	 * 用户编号
	 */
	private Long userId;
	/**
	 * 本金
	 */
	private double money;
	/**
	 * 利息
	 */
	private double interest;
	/**
	 * 违约金
	 */
	private double penalty;
	/**
	 * 管理费
	 */
	private double management;
	/**
	 * ips账号
	 */
	private String ipsNumber;
	/**
	 * 还款状态 2按时还款  4逾期已还款  5提前还款
	 */
	private Integer state;
	/**
	 * 标编号
	 * @return loanid
	 */
	public Long getLoanid() {
		return loanid;
	}
	/**
	 * 是否发布有净值标
	 */
	public Integer isLoanState;
	/**
	 * @param loanid  标编号
	 */
	public void setLoanid(Long loanid) {
		this.loanid = loanid;
	}
	/**
	 * 用户编号
	 * @return userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId 用户编号
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 本金
	 * @return money
	 */
	public double getMoney() {
		return money;
	}
	/**
	 * @param money 本金
	 */
	public void setMoney(double money) {
		this.money = money;
	}
	/**
	 * 利息
	 * @return  interest
	 */
	public double getInterest() {
		return interest;
	}
	/**
	 * @param interest 利息
	 */
	public void setInterest(double interest) {
		this.interest = interest;
	}
	/**
	 * 违约金
	 * @return penalty
	 */ 
	public double getPenalty() {
		return penalty;
	}
	/**
	 * @param penalty 违约金
	 */
	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}
	/**
	 * 管理费
	 * @return   management
	 */
	public double getManagement() {
		return management;
	}
	/**
	 * @param management 管理费
	 */
	public void setManagement(double management) {
		this.management = management;
	}
	/**
	 * ips账号
	 * @return ipsNumber
	 */
	public String getIpsNumber() {
		return ipsNumber;
	}
	/**
	 * @param ipsNumber ips账号
	 */
	public void setIpsNumber(String ipsNumber) {
		this.ipsNumber = ipsNumber;
	}
	/**
	 * 是否发布有净值标
	 * @return isLoanState
	 */
	public Integer getIsLoanState() {
		return isLoanState;
	}
	/**
	 * @param isLoanState 是否发布有净值标
	 */
	public void setIsLoanState(Integer isLoanState) {
		this.isLoanState = isLoanState;
	}
	/**
	 * 还款状态
	 * @return state
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * @param state 还款状态
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	public ExpensesInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param loanid 标编号
	 * @param userId 用户编号
	 * @param money 本金
	 * @param interest 利息
	 * @param penalty 违约金
	 * @param management 管理费
	 */
	public ExpensesInfo(Long loanid, Long userId, double money,
			double interest, double penalty, double management,String ipsNumber,Integer isLoanState,Integer state) {
		super();
		this.loanid = loanid;
		this.userId = userId;
		this.money = money;
		this.interest = interest;
		this.penalty = penalty;
		this.management = management;
		this.ipsNumber = ipsNumber;
		this.isLoanState = isLoanState;
		this.state = state;
	}
	
}
