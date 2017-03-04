package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Loansignbasics
 */
@Entity
@Table(name = "loansignbasics")
public class Loansignbasics implements java.io.Serializable {

	// Fields
    /***/
	private Long id;
	/**借款标信息*/
	private Loansign loansign;
	/**借款标号*/
	private String loanNumber;
	/**标题*/
	private String loanTitle;
	/**反担保方式*/
	private String unassureWay;
	/**担保方名称*/
	private String assure;
	/**平台奖励*/
	private Double reward;
	/**借款方借款用途*/
	private String behoof;
	/**借款方还款来源*/
	private String loanOrigin;
	/**借款方商业概述*/
	private String overview;
	/**风险控制措施*/
	private String riskCtrlWay;
	/**借款人感言*/
	private String speech;
	/**风险评估：1低、2中、3高*/
	private Integer riskAssess;
	/**放款时间*/
	private String creditTime;
	/**流标时间*/
	private String flotTime;
	/**借款说明*/
	private String loanExplain;
	/**创建时间*/
	private String loanSignTime;
	/**借款标类型：1信用、2抵押*/
	private Integer loanCategory;
	/**借款管理费*/
	private Double mgtMoney;
	/**标号(自动生成)*/
	private String pBidNo;
	/**合同编号*/
	private String pContractNo;
//	/**借款管理费比例*/
//	private Double mgtMoneyScale;

	// Constructors

	/** default constructor */
	public Loansignbasics() {
	}

	/** full constructor */
	/**
	 * @param loansign  借款标
	 * @param loanNumber 借款标号
	 * @param loanTitle 标题
	 * @param unassureWay 反担保方式
	 * @param assure 担保方名称
	 * @param reward 平台奖励
	 * @param behoof 借款方借款用途
	 * @param loanOrigin 借款方还款来源
	 * @param overview 借款方商业概述
	 * @param riskCtrlWay 风险控制措施
	 * @param speech 借款人感言
	 * @param riskAssess 风险评估
	 * @param creditTime 放款时间
	 * @param loanExplain  借款说明
	 * @param loanSignTime  创建时间
	 * @param loanCategory 借款标类型
	 * @param mgtMoney 借款管理费
	 * @param mgtMoneyScale 借款管理费比例
	 */
	public Loansignbasics(Loansign loansign, String loanNumber,
			String loanTitle, String unassureWay, String assure, Double reward,
			String behoof, String loanOrigin, String overview,
			String riskCtrlWay, String speech, Integer riskAssess,
			String creditTime,String flotTime, String loanExplain, String loanSignTime,
			Integer loanCategory, Double mgtMoney,String pBidNo,String pContractNo) {
		this.loansign = loansign;
		this.loanNumber = loanNumber;
		this.loanTitle = loanTitle;
		this.unassureWay = unassureWay;
		this.assure = assure;
		this.reward = reward;
		this.behoof = behoof;
		this.loanOrigin = loanOrigin;
		this.overview = overview;
		this.riskCtrlWay = riskCtrlWay;
		this.speech = speech;
		this.riskAssess = riskAssess;
		this.creditTime = creditTime;
		this.flotTime = flotTime;
		this.loanExplain = loanExplain;
		this.loanSignTime = loanSignTime;
		this.loanCategory = loanCategory;
		this.mgtMoney = mgtMoney;
		this.pBidNo = pBidNo;
		this.pContractNo = pContractNo;
//		this.mgtMoneyScale = mgtMoneyScale;
	}

	// Property accessors
	/**
	 * id
	 * @return id
	 */
	@Id
	 @GenericGenerator(name = "fund_id", strategy = "foreign", parameters = { @Parameter(name = "property", value = "loansign") })
    @GeneratedValue(generator = "fund_id")
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}
	/**
	 * @param id id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return loansign
	 */
	@ManyToOne()
	@JoinColumn(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
	public Loansign getLoansign() {
		return this.loansign;
	}
	/**
	 * @param loansign loansign
	 */
	public void setLoansign(Loansign loansign) {
		this.loansign = loansign;
	}
	/**
	 * @return loanNumber
	 */
	@Column(name = "loanNumber")
	public String getLoanNumber() {
		return this.loanNumber;
	}
	/**
	 * @param loanNumber loanNumber
	 */
	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}
	/**
	 * @return loanTitle
	 */
	@Column(name = "loanTitle")
	public String getLoanTitle() {
		return this.loanTitle;
	}
	/**
	 * @param loanTitle loanTitle
	 */
	public void setLoanTitle(String loanTitle) {
		this.loanTitle = loanTitle;
	}
	/**
	 * @return unassureWay
	 */
	@Column(name = "unassureWay")
	public String getUnassureWay() {
		return this.unassureWay;
	}  
	/**
	 * @param unassureWay unassureWay
	 */
	public void setUnassureWay(String unassureWay) {
		this.unassureWay = unassureWay;
	}
	/**
	 * @return assure
	 */
	@Column(name = "assure")
	public String getAssure() {
		return this.assure;
	}
	/**
	 * @param assure assure
	 */
	public void setAssure(String assure) {
		this.assure = assure;
	}
	/**
	 * @return reward
	 */
	@Column(name = "reward", precision = 18, scale = 4)
	public Double getReward() {
		return this.reward;
	}
	/**
	 * @param reward reward
	 */
	public void setReward(Double reward) {
		this.reward = reward;
	}
	/**
	 * @return behoof
	 */
	@Column(name = "behoof", length = 500)
	public String getBehoof() {
		return this.behoof;
	}
	/**
	 * @param behoof behoof
	 */
	public void setBehoof(String behoof) {
		this.behoof = behoof;
	}
	/**
	 * @return loanOrigin
	 */
	@Column(name = "loanOrigin", length = 500)
	public String getLoanOrigin() {
		return this.loanOrigin;
	}
	/**
	 * @param loanOrigin loanOrigin
	 */
	public void setLoanOrigin(String loanOrigin) {
		this.loanOrigin = loanOrigin;
	}
	/**
	 * @return overview
	 */
	@Column(name = "overview", length = 500)
	public String getOverview() {
		return this.overview;
	}
	/**
	 * @param overview overview
	 */
	public void setOverview(String overview) {
		this.overview = overview;
	}
	/**
	 * @return riskCtrlWay
	 */
	@Column(name = "riskCtrlWay", length = 500)
	public String getRiskCtrlWay() {
		return this.riskCtrlWay;
	}
	/**
	 * @param riskCtrlWay riskCtrlWay
	 */
	public void setRiskCtrlWay(String riskCtrlWay) {
		this.riskCtrlWay = riskCtrlWay;
	}
	/**
	 * @return speech
	 */
	@Column(name = "speech", length = 500)
	public String getSpeech() {
		return this.speech;
	}
	/**
	 * @param speech speech
	 */
	public void setSpeech(String speech) {
		this.speech = speech;
	}
	/**
	 * @return riskAssess
	 */
	@Column(name = "riskAssess")
	public Integer getRiskAssess() {
		return this.riskAssess;
	}
	/**
	 * @param riskAssess riskAssess
	 */
	public void setRiskAssess(Integer riskAssess) {
		this.riskAssess = riskAssess;
	}
	/**
	 * @return creditTime
	 */
	@Column(name = "creditTime", length = 50)
	public String getCreditTime() {
		return this.creditTime;
	}
	/**
	 * @param creditTime creditTime
	 */
	public void setCreditTime(String creditTime) {
		this.creditTime = creditTime;
	}
	@Column(name = "flotTime", length = 50)
	public String getFlotTime() {
		return flotTime;
	}

	public void setFlotTime(String flotTime) {
		this.flotTime = flotTime;
	}

	/**
	 * @return loanExplain
	 */
	@Column(name = "loanExplain", length = 50)
	public String getLoanExplain() {
		return this.loanExplain;
	}
	/**
	 * @param loanExplain loanExplain
	 */
	public void setLoanExplain(String loanExplain) {
		this.loanExplain = loanExplain;
	}
	/**
	 * @return loanSignTime
	 */
	@Column(name = "loanSignTime", length = 50)
	public String getLoanSignTime() {
		return this.loanSignTime;
	}
	/**
	 * @param loanSignTime loanSignTime
	 */
	public void setLoanSignTime(String loanSignTime) {
		this.loanSignTime = loanSignTime;
	}
	/**
	 * @return loanCategory
	 */
	@Column(name = "loanCategory")
	public Integer getLoanCategory() {
		return this.loanCategory;
	}
	/**
	 * @param loanCategory loanCategory
	 */
	public void setLoanCategory(Integer loanCategory) {
		this.loanCategory = loanCategory;
	}
	/**
	 * @return mgtMoney
	 */
	@Column(name = "mgtMoney", precision = 18, scale = 4)
	public Double getMgtMoney() {
		return this.mgtMoney;
	}
	/**
	 * @param mgtMoney mgtMoney
	 */
	public void setMgtMoney(Double mgtMoney) {
		this.mgtMoney = mgtMoney;
	}
	/**
	 * 借款标标号(自动生成)
	 * @return pBidNo
	 */
	@Column(name = "pBidNo",length = 30)
	public String getpBidNo() {
		return pBidNo;
	}
	/**
	 * 借款标标号(自动生成)
	 * @param pBidNo 借款标标号(自动生成)
	 */
	public void setpBidNo(String pBidNo) {
		this.pBidNo = pBidNo;
	}
	/**
	 * 借款标合同号(自动生成)
	 * @return pContractNo
	 */
	@Column(name = "pContractNo",length = 30)
	public String getpContractNo() {
		return pContractNo;
	}
	/**
	 * 借款标合同号(自动生成)
	 * @param pContractNo 借款标合同号(自动生成)
	 */
	public void setpContractNo(String pContractNo) {
		this.pContractNo = pContractNo;
	}
	
//	/**
//	 * @return mgtMoneyScale
//	 */
//	@Column(name = "mgtMoneyScale", precision = 18, scale = 4)
//	public Double getMgtMoneyScale() {
//		return this.mgtMoneyScale;
//	}
//	/**
//	 * @param mgtMoneyScale mgtMoneyScale
//	 */
//	public void setMgtMoneyScale(Double mgtMoneyScale) {
//		this.mgtMoneyScale = mgtMoneyScale;
//	}

}