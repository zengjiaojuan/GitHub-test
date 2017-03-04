package com.cddgg.p2p.huitou.model;

/**
 * 提前还款页面显示内容显示
 * @author RanQiBing 20140703
 *
 */
public class Prepayment {
	
	private Long id;		//当期还款编号
	
	private Long loanid;	//当前标号
	 
	private String loanNumber; //标号
	
	private Double money;	//借款金额
		
	private String periods;	//期数
	
	private Double intester;	//本期利息
	
	private Double penalty;		//违约金
	
	private Double countMoney;	//剩余未还款总额
	
	private Double surplusMoney;	//剩余未还本金

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLoanid() {
		return loanid;
	}

	public void setLoanid(Long loanid) {
		this.loanid = loanid;
	}

	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public Double getIntester() {
		return intester;
	}

	public void setIntester(Double intester) {
		this.intester = intester;
	}

	public Double getPenalty() {
		return penalty;
	}

	public void setPenalty(Double penalty) {
		this.penalty = penalty;
	}

	public Double getCountMoney() {
		return countMoney;
	}

	public void setCountMoney(Double countMoney) {
		this.countMoney = countMoney;
	}

	public Double getSurplusMoney() {
		return surplusMoney;
	}

	public void setSurplusMoney(Double surplusMoney) {
		this.surplusMoney = surplusMoney;
	}
	
}
