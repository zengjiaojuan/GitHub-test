package com.cddgg.p2p.huitou.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 临时认购记录表
 * @author Administrator
 *
 */
@Entity
@Table(name = "blotterRecord")
public class BlotterRecord {
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
	 * 购买是否成功(0、未付款 1、认购成功 2、认购失败)
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
	 * 在未付款的情况下订单失效时间
	 */
	private String endTime;
	/**
	 * 购买时是否是特权会员（0不是,1.是）
	 */
	private Integer isPrivilege;
	/**
	 * 当前订单号
	 */
	private String number;
	
	
	public BlotterRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param id
	 * @param loansign
	 * @param userbasicsinfo
	 * @param isSucceed
	 * @param tenderMoney
	 * @param tenderTime
	 * @param endTime
	 * @param isPrivilege
	 */
	public BlotterRecord(Long id, Loansign loansign,
			Userbasicsinfo userbasicsinfo, Integer isSucceed,
			Double tenderMoney, String tenderTime, String endTime,
			Integer isPrivilege,String number) {
		super();
		this.id = id;
		this.loansign = loansign;
		this.userbasicsinfo = userbasicsinfo;
		this.isSucceed = isSucceed;
		this.tenderMoney = tenderMoney;
		this.tenderTime = tenderTime;
		this.endTime = endTime;
		this.isPrivilege = isPrivilege;
		this.number = number;
	}

	/**
	 * 编号
	 * @return
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

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
		return loansign;
	}

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
		return userbasicsinfo;
	}

	public void setUserbasicsinfo(Userbasicsinfo userbasicsinfo) {
		this.userbasicsinfo = userbasicsinfo;
	}
	/**
	 * 
	 * @return isSucceed
	 */
	@Column(name = "isSucceed")
	public Integer getIsSucceed() {
		return isSucceed;
	}

	public void setIsSucceed(Integer isSucceed) {
		this.isSucceed = isSucceed;
	}
	/**
	 * tenderMoney
	 * @return tenderMoney
	 */
	@Column(name = "tenderMoney", precision = 18, scale = 4)
	public Double getTenderMoney() {
		return tenderMoney;
	}

	public void setTenderMoney(Double tenderMoney) {
		this.tenderMoney = tenderMoney;
	}
	/**
	 * 
	 * @return tenderTime
	 */
	@Column(name = "tenderTime", length = 32)
	public String getTenderTime() {
		return tenderTime;
	}

	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}
	/**
	 * 
	 * @return tenderTime
	 */
	@Column(name = "endTime", length = 32)
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * 
	 * @return isPrivilege
	 */
	@Column(name = "isPrivilege")
	public Integer getIsPrivilege() {
		return isPrivilege;
	}

	public void setIsPrivilege(Integer isPrivilege) {
		this.isPrivilege = isPrivilege;
	}
	@Column(name = "number",length = 30)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	
}
