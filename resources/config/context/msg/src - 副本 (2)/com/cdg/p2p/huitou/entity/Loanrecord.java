package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Loanrecord
 */
@Entity
@Table(name = "loanrecord")
public class Loanrecord implements java.io.Serializable {

	// Fields
    /**
     * 
     */
	private Long id;
	/**
	 *购买借款标信息
	 */
	private Loansign loansign;
	/**
	 * 会员基本信息
	 */
	private Userbasicsinfo userbasicsinfo;
	/**
	 * 购买是否成功
	 */
	private Integer isSucceed;
	/**
	 * 购买金额
	 */
	private Double tenderMoney;
	/**
	 * 购买时间
	 */
	private String tenderTime;
	/**
	 * 购买时是否是特权会员（0不是,1.是）
	 */
	private Integer isPrivilege;
	
	
	private String pMerBillNo; 
	
	private String pipsBillNo; 
	// Constructors

	/** default constructor */
	public Loanrecord() {
	}

	/** full constructor */
	/**
	 * 
	 * @param loansign 借款标信息
	 * @param userbasicsinfo 会员基本信息
	 * @param isSucceed 是否购买成功
	 * @param tenderMoney 购买金额
	 * @param tenderTime 购买时间
	 * @param isPrivilege 是否是特权会员
	 */
	public Loanrecord(Loansign loansign, Userbasicsinfo userbasicsinfo,
			Integer isSucceed, Double tenderMoney, String tenderTime,
			Integer isPrivilege,String pMerBillNo,String pipsBillNo) {
		this.loansign = loansign;
		this.userbasicsinfo = userbasicsinfo;
		this.isSucceed = isSucceed;
		this.tenderMoney = tenderMoney;
		this.tenderTime = tenderTime;
		this.isPrivilege = isPrivilege;
		this.pMerBillNo = pMerBillNo;
		this.pipsBillNo = pipsBillNo;
	}

	// Property accessors
	/**
	 * 
	 * @return id
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}
	/**
	 * 
	 * @param id id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * loansign
	 * @return loansign
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loanSign_id")
	public Loansign getLoansign() {
		return this.loansign;
	}
	/**
	 * 
	 * @param loansign loansign
	 */
	public void setLoansign(Loansign loansign) {
		this.loansign = loansign;
	}
	/**
	 * 
	 * @return userbasicsinfo
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userbasicinfo_id")
	public Userbasicsinfo getUserbasicsinfo() {
		return this.userbasicsinfo;
	}
	/**
	 * 
	 * @param userbasicsinfo userbasicsinfo
	 */
	public void setUserbasicsinfo(Userbasicsinfo userbasicsinfo) {
		this.userbasicsinfo = userbasicsinfo;
	}
	/**
	 * 
	 * @return isSucceed
	 */
	@Column(name = "isSucceed")
	public Integer getIsSucceed() {
		return this.isSucceed;
	}
	/**
	 * 
	 * @param isSucceed isSucceed
	 */
	public void setIsSucceed(Integer isSucceed) {
		this.isSucceed = isSucceed;
	}
	/**
	 * tenderMoney
	 * @return tenderMoney
	 */
	@Column(name = "tenderMoney", precision = 18, scale = 4)
	public Double getTenderMoney() {
		return this.tenderMoney;
	}
	/**
	 * 
	 * @param tenderMoney tenderMoney
	 */
	public void setTenderMoney(Double tenderMoney) {
		this.tenderMoney = tenderMoney;
	}
	/**
	 * 
	 * @return tenderTime
	 */
	@Column(name = "tenderTime", length = 32)
	public String getTenderTime() {
		return this.tenderTime;
	}
	/**
	 * 
	 * @param tenderTime tenderTime
	 */
	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}
	/**
	 * 
	 * @return isPrivilege
	 */
	@Column(name = "isPrivilege")
	public Integer getIsPrivilege() {
		return this.isPrivilege;
	}
	/**
	 * 
	 * @param isPrivilege isPrivilege
	 */
	public void setIsPrivilege(Integer isPrivilege) {
		this.isPrivilege = isPrivilege;
	}
	/**
	 * 
	 * @return pMerBillNo
	 */
	@Column(name = "pMerBillNo",length=30)
	public String getpMerBillNo() {
		return pMerBillNo;
	}
	/**
	 * @param pMerBillNo 投标订单号
	 */
	public void setpMerBillNo(String pMerBillNo) {
		this.pMerBillNo = pMerBillNo;
	}
	
	@Column(name = "pIpsBillNo",length=30)
	public String getPipsBillNo() {
		return pipsBillNo;
	}

	public void setPipsBillNo(String pipsBillNo) {
		this.pipsBillNo = pipsBillNo;
	}
	
	

}