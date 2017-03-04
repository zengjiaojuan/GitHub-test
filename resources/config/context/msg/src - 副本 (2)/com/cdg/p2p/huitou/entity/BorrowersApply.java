package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * BorrowersApply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "borrowers_apply")
public class BorrowersApply implements java.io.Serializable {

	// Fields

	private Long id;
	private Borrowersbase borrowersbase;
	
	/**
     * 申请时间
     */
    private String time;
	
	/**
	 * 申请金额
	 */
	private Double money;
	
	/**
	 * 类型[1:助人贷,2:助企贷,3:企业群联保贷,4:投资人周转贷;]
	 */
	private Integer type;
	
	/**
	 * 状态[0:审核中,1:已通过,2:未通过]
	 */
	private Integer status;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 是否发布有借款标[0:未发布,1:已发布]
	 */
	private Integer state=0;

	// Constructors

	/** default constructor */
	public BorrowersApply() {
	}

	/** minimal constructor */
	public BorrowersApply(Borrowersbase borrowersbase, Double money,
			Integer type, Integer status) {
		this.borrowersbase = borrowersbase;
		this.money = money;
		this.type = type;
		this.status = status;
	}

	/** full constructor */
	public BorrowersApply(Borrowersbase borrowersbase, Double money,
			Integer type, Integer status, String remark,Integer state) {
		this.borrowersbase = borrowersbase;
		this.money = money;
		this.type = type;
		this.status = status;
		this.remark = remark;
		this.state = state;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "base_id", nullable = false)
	public Borrowersbase getBorrowersbase() {
		return this.borrowersbase;
	}

	public void setBorrowersbase(Borrowersbase borrowersbase) {
		this.borrowersbase = borrowersbase;
	}

	@Column(name = "money", nullable = false, precision = 20, scale = 4)
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "type", nullable = false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "remark", length = 65535)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "time", length = 20)
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @Column(name = "state")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	

}