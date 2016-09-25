package com.phb.puhuibao.entity;

import java.io.Serializable;

public class ProductBid implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5721585312494905698L;
	private Integer bidId;
	private String bidSN; // 编号
	private String productSN; // 编号
	private String productName;
	private Long currentAmount; // 已投资额度
	private Long totalAmount; // 总投资额度
	private Integer status; // 投资状态
	private String bidName;
	private String bidContract;//bid_pictures
	private Integer bidImportant ;//
	private Integer recommendStatus;//推荐标识符 0不推荐，1推荐
	private Integer type; // 产品类似
	private String contractPic;//contract_pic
	 
 
	public String getContractPic() {
		return contractPic;
	}
	public void setContractPic(String contractPic) {
		this.contractPic = contractPic;
	}
	public Integer getRecommendStatus() {
		return recommendStatus;
	}
	public void setRecommendStatus(Integer recommendStatus) {
		this.recommendStatus = recommendStatus;
	}
	public String getBidContract() {
		return bidContract;
	}
	public void setBidContract(String bidContract) {
		this.bidContract = bidContract;
	}
	public Integer getBidImportant() {
		return bidImportant;
	}
	public void setBidImportant(Integer bidImportant) {
		this.bidImportant = bidImportant;
	}
	public String getBidName() {
		return bidName;
	}
	public void setBidName(String bidName) {
		this.bidName = bidName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
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
