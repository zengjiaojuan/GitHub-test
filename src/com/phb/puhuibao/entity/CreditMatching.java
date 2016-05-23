package com.phb.puhuibao.entity;


/**
 * @author wei
 *
 */
public class CreditMatching {
	
	private int      borrowId;//borrow_id
	private String  borrowerName;//borrower_name
	private double   borrowPersentage; // borrow_persentage             借款人身份证',
	private double      borrowAmount;// borrow_amount            借款人姓名',
	 
	private double      borrowLeft;// borrow_left        借款人合同号',
	private String   batchName;//batch_name
	public int getBorrowId() {
		return borrowId;
	}
	public void setBorrowId(int borrowId) {
		this.borrowId = borrowId;
	}
	public String getBorrowerName() {
		return borrowerName;
	}
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	public double getBorrowPersentage() {
		return borrowPersentage;
	}
	public void setBorrowPersentage(double borrowPersentage) {
		this.borrowPersentage = borrowPersentage;
	}
	public double getBorrowAmount() {
		return borrowAmount;
	}
	public void setBorrowAmount(double borrowAmount) {
		this.borrowAmount = borrowAmount;
	}
	public double getBorrowLeft() {
		return borrowLeft;
	}
	public void setBorrowLeft(double borrowLeft) {
		this.borrowLeft = borrowLeft;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	
	
 
	 
	
	
 
}
