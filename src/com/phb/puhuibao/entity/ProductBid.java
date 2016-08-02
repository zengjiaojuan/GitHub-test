package com.phb.puhuibao.entity;

import java.io.Serializable;
import java.util.Date;

public class ProductBid implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5721585312494905698L;
	private Integer bidId;
	private String bidSN; // 编号
	private String productSN; // 编号
	private String productName;
	private Date startDate; // 加入期
	private Date endDate; // 到期日
	private Long currentAmount; // 已投资额度
	private Long totalAmount; // 总投资额度
	private Integer status; // 投资状态
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	private Integer type; // 产品类似
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getBidId() {
		return bidId;
	}
	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}
	public String getBidSN() {
		return bidSN;
	}
	public void setBidSN(String bidSN) {
		this.bidSN = bidSN;
	}
	public String getProductSN() {
		return productSN;
	}
	public void setProductSN(String productSN) {
		this.productSN = productSN;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Long currentAmount) {
		this.currentAmount = currentAmount;
	}
	public Long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}
}
