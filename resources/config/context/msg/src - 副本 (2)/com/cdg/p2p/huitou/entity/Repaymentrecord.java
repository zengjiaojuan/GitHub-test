package com.cddgg.p2p.huitou.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Repaymentrecord
 */
@Entity
@Table(name = "repaymentrecord")
public class Repaymentrecord implements java.io.Serializable {

    // Fields

    /**
     * 主键id
     */
    private Long id;
    /**
     * 借款标信息
     */
    private Loansign loansign;
    /**
     * 期数
     */
    private Integer periods;
    /**
     * 预计还款日期
     */
    private String preRepayDate;
    /**
     * 本金
     */
    private Double money;
    /**
     * 预计还款利息
     */
    private Double preRepayMoney;
    /**
     * 还款状态：1未还款、2按时还款、3逾期未还款、4逾期已还款、5提前还款
     */
    private Integer repayState;
    /**
     * 实际还款时间
     */
    private String repayTime;
    /**
     * 实际还款利息
     */
    private Double realMoney;
    /**
     * 逾期利息
     */
    private Double overdueInterest;
    /**
     * 还款编号
     */
    private String pMerBillNo;
    /**
     * ips还款编号
     */
    private String pIpsBillNo;
    
    /**
     * 自动积分表信息
     */
    private Set<Autointegral> autointegrals = new HashSet<Autointegral>(0);

    // Constructors

    /** default constructor */
    public Repaymentrecord() {
    }

    /** full constructor */
    /**
     * 
     * @param loansign 借款标信息
     * @param periods 期数
     * @param preRepayDate 预计还款天数
     * @param money 本金
     * @param preRepayMoney 预计还款金额
     * @param repayState 还款状态
     * @param repayTime 预计还款时间
     * @param realMoney 时间还款金额
     * @param autointegrals 自动积分信息
     */
    public Repaymentrecord(Loansign loansign, Integer periods,
            String preRepayDate, Double money, Double preRepayMoney,
            Integer repayState, String repayTime, Double realMoney,
            Set<Autointegral> autointegrals,String pMerBillNo,String pIpsBillNo,Double overdueInterest) {
        this.loansign = loansign;
        this.periods = periods;
        this.preRepayDate = preRepayDate;
        this.money = money;
        this.preRepayMoney = preRepayMoney;
        this.repayState = repayState;
        this.repayTime = repayTime;
        this.realMoney = realMoney;
        this.autointegrals = autointegrals;
        this.pMerBillNo = pMerBillNo;
        this.pIpsBillNo = pIpsBillNo;
        this.overdueInterest = overdueInterest;
    }

    // Property accessors
    /**
     * 
     * @return Long
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * 
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return Loansign
     */
    @ManyToOne
    @JoinColumn(name = "loanSign_id")
    public Loansign getLoansign() {
        return this.loansign;
    }

    /**
     * 
     * @param loansign 借款标信息
     */
    public void setLoansign(Loansign loansign) {
        this.loansign = loansign;
    }

    /**
     *  
     * @return Integer
     */
    @Column(name = "periods")
    public Integer getPeriods() {
        return this.periods;
    }

    /**
     * 
     * @param periods 期数
     */
    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    /**
     * 
     * @return String
     */
    @Column(name = "preRepayDate")
    public String getPreRepayDate() {
        return this.preRepayDate;
    }

    /**
     * 
     * @param preRepayDate 预计还款天数
     */
    public void setPreRepayDate(String preRepayDate) {
        this.preRepayDate = preRepayDate;
    }

    /**
     * 
     * @return  Double
     */
    @Column(name = "money", precision = 18, scale = 4)
    public Double getMoney() {
        return this.money;
    }

    /**
     * 
     * @param money 本金
     */
    public void setMoney(Double money) {
        this.money = money;
    }

    /**
     * 
     * @return Double
     */
    @Column(name = "preRepayMoney", precision = 18, scale = 4)
    public Double getPreRepayMoney() {
        return this.preRepayMoney;
    }

    /**
     * 
     * @param preRepayMoney 预计还款金额
     */
    public void setPreRepayMoney(Double preRepayMoney) {
        this.preRepayMoney = preRepayMoney;
    }

    /**
     *  
     * @return Integer
     */
    @Column(name = "repayState")
    public Integer getRepayState() {
        return this.repayState;
    }

    /**
     * 
     * @param repayState 还款状态
     */
    public void setRepayState(Integer repayState) {
        this.repayState = repayState;
    }

    /**
     * 
     * @return String
     */
    @Column(name = "repayTime")
    public String getRepayTime() {
        return this.repayTime;
    }

    /**
     * 
     * @param repayTime 还款时间
     */
    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    /**
     * 
     * @return Double
     */
    @Column(name = "realMoney", precision = 18, scale = 4)
    public Double getRealMoney() {
        return this.realMoney;
    }

    /**
     * 
     * @param realMoney 实际还款金额
     */
    public void setRealMoney(Double realMoney) {
        this.realMoney = realMoney;
    }
    
    /**
     * 逾期利息
     * @return
     */
    @Column(name="overdueInterest",precision = 18, scale = 4)
    public Double getOverdueInterest() {
		return overdueInterest;
	}
    /**
     * 逾期利息
     * @param overdueInterest 逾期利息
     */
	public void setOverdueInterest(Double overdueInterest) {
		this.overdueInterest = overdueInterest;
	}

	/**
     * 还款编号
     * @return pMerBillNo
     */
    @Column(name = "pMerBillNo",length = 30)
    public String getpMerBillNo() {
		return pMerBillNo;
	}
    /**
     * @param pMerBillNo 还款编号
     */
	public void setpMerBillNo(String pMerBillNo) {
		this.pMerBillNo = pMerBillNo;
	}
	/**
	 * ips还款编号
	 * @return pIpsBillNo
	 */
	@Column(name = "pIpsBillNo",length = 30)
	public String getpIpsBillNo() {
		return pIpsBillNo;
	}
	/**
	 * @param pIpsBillNo ips还款编号
	 */
	public void setpIpsBillNo(String pIpsBillNo) {
		this.pIpsBillNo = pIpsBillNo;
	}

	/**
     * 
     * @return Set<Autointegral>
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "repaymentrecord")
    public Set<Autointegral> getAutointegrals() {
        return this.autointegrals;
    }

    /**
     * 
     * @param autointegrals 自动积分信息
     */
    public void setAutointegrals(Set<Autointegral> autointegrals) {
        this.autointegrals = autointegrals;
    }

}