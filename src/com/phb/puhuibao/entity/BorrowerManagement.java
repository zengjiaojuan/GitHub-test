package com.phb.puhuibao.entity;


/**
 * @author wei
 *
 */
public class BorrowerManagement {
	
	private int      recordId;//
	private String   borrowerId; // borrower_id             借款人身份证',
	private String   borrowerName;// borrower_name            借款人姓名',
	private String   borrowerContract;// borrower_contract        借款人合同号',
	private int      borrowerJob;// borrower_job            '借款人身份,1:私营业主2:企业高管3:工薪阶层',
	private int      warrantStatus;// warrant_status            '担保状态 1:质押贷款',
	private int      moneyUseage;// money_useage            '资金用途 1:扩大经营2:资金周转3:个人消费',
	private double   contractAmount;// contract_amount            COMMENT '金额',
	private String   contractStartdate;// contract_startdate           ''起息日期  
	private String   contractExpiredate;// contract_expiredate         '合同结束日期',
	
	private double   filledPct;//filled_pct            COMMENT '金额',
	private double   filledAmount;//`filled_amount`            COMMENT '金额',
	
	
	
	
	
	public double getFilledAmount() {
		return filledAmount;
	}
	public void setFilledAmount(double filledAmount) {
		this.filledAmount = filledAmount;
	}
	public double getFilledPct() {
		return filledPct;
	}
	public void setFilledPct(double filledPct) {
		this.filledPct = filledPct;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(String borrowerId) {
		this.borrowerId = borrowerId;
	}
	public String getBorrowerName() {
		return borrowerName;
	}
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	public String getBorrowerContract() {
		return borrowerContract;
	}
	public void setBorrowerContract(String borrowerContract) {
		this.borrowerContract = borrowerContract;
	}
	public int getBorrowerJob() {
		return borrowerJob;
	}
	public void setBorrowerJob(int borrowerJob) {
		this.borrowerJob = borrowerJob;
	}
	public int getWarrantStatus() {
		return warrantStatus;
	}
	public void setWarrantStatus(int warrantStatus) {
		this.warrantStatus = warrantStatus;
	}
	public int getMoneyUseage() {
		return moneyUseage;
	}
	public void setMoneyUseage(int moneyUseage) {
		this.moneyUseage = moneyUseage;
	}
	public double getContractAmount() {
		return contractAmount;
	}
	public void setContractAmount(double contractAmount) {
		this.contractAmount = contractAmount;
	}
	public String getContractStartdate() {
		return contractStartdate;
	}
	public void setContractStartdate(String contractStartdate) {
		this.contractStartdate = contractStartdate;
	}
	public String getContractExpiredate() {
		return contractExpiredate;
	}
	public void setContractExpiredate(String contractExpiredate) {
		this.contractExpiredate = contractExpiredate;
	}
	
  
	 
	
	
}
