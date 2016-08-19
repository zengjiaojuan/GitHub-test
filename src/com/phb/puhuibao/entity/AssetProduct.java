package com.phb.puhuibao.entity;

public class AssetProduct {
	private Integer productId; // id
	private String productName; // 产品名称
	private String productSN; // 编号
	private String paymentMethod; // 还款方式
	private String guaranteeMethod; // 保障方式
	private Double redemptionDueRate; // 到期赎回率
	private Double redemptionEarlyRate; // 提前赎回率
	private Integer investmentAmountMin; // 最小投资额
	private Double investmentAmountMultiple; // 投资额递增倍数
	private Integer investmentAmountUpper; // 单笔额度上限
	private Double annualizedRate; // 年化利率
	private Double rewardRate; //奖励利率
 
	private Integer period; // 周期
	private String unit; // 单位
	private Double joinRate; // 加入费率
	private Double managementRate; // 管理费率
	private String redemptionDueMethod; // 到期赎回方式
	private String redemptionEarlyMethod; // 提前赎回方式
	private Long currentAmount; // 已投资额度
	private Long totalAmount; // 总投资额度
	private String productDesc; // 产品介绍
	private String help;
	private Integer type; // 产品类似
	private Double estimateIncome; // 万元预估收益
	private Integer status; // 0无效，1有效
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Double getEstimateIncome() {
		return estimateIncome;
	}
	public void setEstimateIncome(Double estimateIncome) {
		this.estimateIncome = estimateIncome;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getHelp() {
		return help;
	}
	public void setHelp(String help) {
		this.help = help;
	}
 
 
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductSN() {
		return productSN;
	}
	public void setProductSN(String productSN) {
		this.productSN = productSN;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getGuaranteeMethod() {
		return guaranteeMethod;
	}
	public void setGuaranteeMethod(String guaranteeMethod) {
		this.guaranteeMethod = guaranteeMethod;
	}
	public Double getRedemptionDueRate() {
		return redemptionDueRate;
	}
	public void setRedemptionDueRate(Double redemptionDueRate) {
		this.redemptionDueRate = redemptionDueRate;
	}
	public Double getRedemptionEarlyRate() {
		return redemptionEarlyRate;
	}
	public void setRedemptionEarlyRate(Double redemptionEarlyRate) {
		this.redemptionEarlyRate = redemptionEarlyRate;
	}
	public Integer getInvestmentAmountMin() {
		return investmentAmountMin;
	}
	public void setInvestmentAmountMin(Integer investmentAmountMin) {
		this.investmentAmountMin = investmentAmountMin;
	}
	public Double getInvestmentAmountMultiple() {
		return investmentAmountMultiple;
	}
	public void setInvestmentAmountMultiple(Double investmentAmountMultiple) {
		this.investmentAmountMultiple = investmentAmountMultiple;
	}
	public Integer getInvestmentAmountUpper() {
		return investmentAmountUpper;
	}
	public void setInvestmentAmountUpper(Integer investmentAmountUpper) {
		this.investmentAmountUpper = investmentAmountUpper;
	}
	public Double getAnnualizedRate() {
		return annualizedRate;
	}
	public void setAnnualizedRate(Double annualizedRate) {
		this.annualizedRate = annualizedRate;
	}
	public Double getRewardRate() {
		return rewardRate;
	}
	public void setRewardRate(Double rewardRate) {
		this.rewardRate = rewardRate;
	}
	 
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getJoinRate() {
		return joinRate;
	}
	public void setJoinRate(Double joinRate) {
		this.joinRate = joinRate;
	}
	public Double getManagementRate() {
		return managementRate;
	}
	public void setManagementRate(Double managementRate) {
		this.managementRate = managementRate;
	}
	public String getRedemptionDueMethod() {
		return redemptionDueMethod;
	}
	public void setRedemptionDueMethod(String redemptionDueMethod) {
		this.redemptionDueMethod = redemptionDueMethod;
	}
	public String getRedemptionEarlyMethod() {
		return redemptionEarlyMethod;
	}
	public void setRedemptionEarlyMethod(String redemptionEarlyMethod) {
		this.redemptionEarlyMethod = redemptionEarlyMethod;
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
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
}
