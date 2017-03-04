package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Costratio 推广奖金
 */
@Entity
@Table(name = "costratio")
public class Costratio implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private Long id;

    /**
     * 推广奖金
     */
    private Double bouns;

    /**
     * 二个月以内管理费(借款者)
     */
    private Double withinTwoMonths;
    
    /**
     * 二个月以上管理费(借款者)
     */
    private Double other;
    /**
     * 借款者上线(VIP用户上限金额)
     */
    private Double highLines;
    /**
     * 天标管理费
     */
    private Double dayRate; 

    /**
     * 管理费比例(投资者)
     */
    private Double mfeeratio;
    /**
     * 提前还款利率
     */
    private Double prepaymentRate;
    /**
     * 逾期还款
     */
    private Double overdueRepayment; 
//    /**
//     * 管理费上限
//     */
//    private Double mfeetop;

    /**
     * 特权会员管理费比例(投资者)
     */
    private Double pmfeeratio;
    /**
     * 充值提现费(普通会员)
     */
    private Double recharge;
    /**
     * 充值提现费(vip会员)
     */
    private Double viprecharge;
    /**
     * 提现手续费(普通会员)
     */
    private Double withdraw;
    /**
     * 提现手续费(vip会员)
     */
    private Double vipwithdraw;

//    /**
//     * 特权会员管理费上限
//     */
//    private Double pmfeetop;

    /**
     * 特权会员提现费比例
     */
    private Double pwfeeratio;

    /**
     * 特权会员提现费上限
     */
    private Double pwfeetop;

    /**
     * 提现费比例
     */
    private Double wfeeratio;

    /**
     * 提现费上限
     */
    private Double wfeetop;

    /**
     * oneyear
     */
    private Double oneyear;

    /**
     * oneyear
     */
    private Double threeyear;

    /**
     * twoyear
     */
    private Double twoyear;

    /**
     * tianshu
     */
    private Integer tianshu;

    /**
     * money
     */
    private Integer money;

    // Constructors

    /** default constructor */
    public Costratio() {
    }

    /**
     * full constructor
     * @param bouns         bouns
     * @param mfeeratio     mfeeratio
     * @param mfeetop       mfeetop
     * @param pmfeeratio    pmfeeratio
     * @param pmfeetop      pmfeetop
     * @param pwfeeratio    pwfeeratio
     * @param pwfeetop      pwfeetop
     * @param wfeeratio     wfeeratio
     * @param wfeetop       wfeetop
     * @param oneyear       oneyear
     * @param threeyear     threeyear
     * @param twoyear       twoyear
     * @param tianshu       tianshu
     * @param money         money
     */
    public Costratio(Long id, Double bouns, Double withinTwoMonths,
			Double other, Double highLines, Double dayRate, Double mfeeratio,
			Double prepaymentRate, Double overdueRepayment, Double pmfeeratio,
			Double pwfeeratio, Double pwfeetop, Double wfeeratio,
			Double wfeetop, Double oneyear, Double threeyear, Double twoyear,
			Integer tianshu, Integer money,Double recharge,Double viprecharge,Double withdraw,Double vipwithdraw) {
		super();
		this.id = id;
		this.bouns = bouns;
		this.withinTwoMonths = withinTwoMonths;
		this.other = other;
		this.highLines = highLines;
		this.dayRate = dayRate;
		this.mfeeratio = mfeeratio;
		this.prepaymentRate = prepaymentRate;
		this.overdueRepayment = overdueRepayment;
		this.pmfeeratio = pmfeeratio;
		this.pwfeeratio = pwfeeratio;
		this.pwfeetop = pwfeetop;
		this.wfeeratio = wfeeratio;
		this.wfeetop = wfeetop;
		this.oneyear = oneyear;
		this.threeyear = threeyear;
		this.twoyear = twoyear;
		this.tianshu = tianshu;
		this.money = money;
		this.recharge = recharge;
		this.viprecharge = viprecharge;
		this.withdraw = withdraw;
		this.vipwithdraw = vipwithdraw;
	}

    /**
     * Property accessors
     * @return id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

	/**
     * id
     * @param id    id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * bouns
     * @return  bouns
     */
    @Column(name = "bouns", precision = 18, scale = 4)
    public Double getBouns() {
        return this.bouns;
    }

    /**
     * bouns
     * @param bouns bouns
     */
    public void setBouns(Double bouns) {
        this.bouns = bouns;
    }

    /**
     * mfeeratio
     * @return  mfeeratio
     */
    @Column(name = "mfeeratio", precision = 18, scale = 4)
    public Double getMfeeratio() {
        return this.mfeeratio;
    }

    /**
     * mfeeratio
     * @param mfeeratio mfeeratio
     */
    public void setMfeeratio(Double mfeeratio) {
        this.mfeeratio = mfeeratio;
    }

    /**
     * mfeetop
     * @return  mfeetop
     */
//    @Column(name = "mfeetop", precision = 18, scale = 4)
//    public Double getMfeetop() {
//        return this.mfeetop;
//    }
//
//    /**
//     * mfeetop
//     * @param mfeetop   mfeetop
//     */
//    public void setMfeetop(Double mfeetop) {
//        this.mfeetop = mfeetop;
//    }

    /**
     * pmfeeratio
     * @return  pmfeeratio
     */
    @Column(name = "pmfeeratio", precision = 18, scale = 4)
    public Double getPmfeeratio() {
        return this.pmfeeratio;
    }

    /**
     * pmfeeratio
     * @param pmfeeratio    pmfeeratio
     */
    public void setPmfeeratio(Double pmfeeratio) {
        this.pmfeeratio = pmfeeratio;
    }

    /**
     * pmfeetop
     * @return  pmfeetop
     */
//    @Column(name = "pmfeetop", precision = 18, scale = 4)
//    public Double getPmfeetop() {
//        return this.pmfeetop;
//    }
//
//    /**
//     * pmfeetop
//     * @param pmfeetop  pmfeetop
//     */
//    public void setPmfeetop(Double pmfeetop) {
//        this.pmfeetop = pmfeetop;
//    }

    /**
     * pwfeeratio
     * @return  pwfeeratio
     */
    @Column(name = "pwfeeratio", precision = 18, scale = 4)
    public Double getPwfeeratio() {
        return this.pwfeeratio;
    }

    /**
     * pwfeeratio
     * @param pwfeeratio    pwfeeratio
     */
    public void setPwfeeratio(Double pwfeeratio) {
        this.pwfeeratio = pwfeeratio;
    }

    /**
     * pwfeetop
     * @return  pwfeetop
     */
    @Column(name = "pwfeetop", precision = 18, scale = 4)
    public Double getPwfeetop() {
        return this.pwfeetop;
    }

    /**
     * pwfeetop
     * @param pwfeetop  pwfeetop
     */
    public void setPwfeetop(Double pwfeetop) {
        this.pwfeetop = pwfeetop;
    }

    /**
     * wfeeratio
     * @return  wfeeratio
     */
    @Column(name = "wfeeratio", precision = 18, scale = 4)
    public Double getWfeeratio() {
        return this.wfeeratio;
    }

    /**
     * wfeeratio
     * @param wfeeratio wfeeratio
     */
    public void setWfeeratio(Double wfeeratio) {
        this.wfeeratio = wfeeratio;
    }

    /**
     * wfeetop
     * @return  wfeetop
     */
    @Column(name = "wfeetop", precision = 18, scale = 4)
    public Double getWfeetop() {
        return this.wfeetop;
    }

    /**
     * wfeetop
     * @param wfeetop   wfeetop
     */
    public void setWfeetop(Double wfeetop) {
        this.wfeetop = wfeetop;
    }

    /**
     * oneyear
     * @return  oneyear
     */
    @Column(name = "oneyear", precision = 22, scale = 0)
    public Double getOneyear() {
        return this.oneyear;
    }

    /**
     * oneyear
     * @param oneyear   oneyear
     */
    public void setOneyear(Double oneyear) {
        this.oneyear = oneyear;
    }

    /**
     * threeyear
     * @return  threeyear
     */
    @Column(name = "threeyear", precision = 22, scale = 0)
    public Double getThreeyear() {
        return this.threeyear;
    }

    /**
     * threeyear
     * @param threeyear threeyear
     */
    public void setThreeyear(Double threeyear) {
        this.threeyear = threeyear;
    }

    /**
     * twoyear
     * @return  twoyear
     */
    @Column(name = "twoyear", precision = 22, scale = 0)
    public Double getTwoyear() {
        return this.twoyear;
    }

    /**
     * twoyear
     * @param twoyear   twoyear
     */
    public void setTwoyear(Double twoyear) {
        this.twoyear = twoyear;
    }

    /**
     * tianshu
     * @return  tianshu
     */
    @Column(name = "tianshu")
    public Integer getTianshu() {
        return this.tianshu;
    }

    /**
     * tianshu
     * @param tianshu   tianshu
     */
    public void setTianshu(Integer tianshu) {
        this.tianshu = tianshu;
    }

    /**
     * money
     * @return  money
     */
    @Column(name = "money")
    public Integer getMoney() {
        return this.money;
    }

    
    /**
     * money
     * @param money money
     */
    public void setMoney(Integer money) {
        this.money = money;
    }
    /**
     * 二个月以内管理费(借款人)
     * @return WithinTwoMonths
     */
    @Column(name = "withinTwoMonths", precision = 18, scale = 4)
	public Double getWithinTwoMonths() {
		return withinTwoMonths;
	}
	/**
	 * @param withinTwoMonths 二个月以内管理费(借款人)
	 */
	public void setWithinTwoMonths(Double withinTwoMonths) {
		this.withinTwoMonths = withinTwoMonths;
	}
	/**
	 * 二个月以上管理费(投资人)
	 * @return other
	 */
	@Column(name = "other", precision = 18, scale = 4)
	public Double getOther() {
		return other;
	}
	/**
	 * @param other 二个月以上管理费(投资人)
	 */
	public void setOther(Double other) {
		this.other = other;
	}
	/**
	 * 借款者上线(VIP用户上限金额)
	 * @return highLines
	 */
	@Column(name = "highLines", precision = 18, scale = 4)
	public Double getHighLines() {
		return highLines;
	}
	/**
	 * @param highLines 借款者上线(VIP用户上限金额)
	 */
	public void setHighLines(Double highLines) {
		this.highLines = highLines;
	}
	/**
	 * 天标管理费
	 * @return  dayRate
	 */
	@Column(name = "dayRate", precision = 18, scale = 4)
	public Double getDayRate() {
		return dayRate;
	}
	/**
	 * @param dayRate 天标管理费
	 */
	public void setDayRate(Double dayRate) {
		this.dayRate = dayRate;
	}
	/**
	 * 提前还款利率
	 * @return  prepaymentRate
	 */
	@Column(name = "prepaymentRate", precision = 18, scale = 4)
	public Double getPrepaymentRate() {
		return prepaymentRate;
	}
	/**
	 * @param prepaymentRate 提前还款利率
	 */
	public void setPrepaymentRate(Double prepaymentRate) {
		this.prepaymentRate = prepaymentRate;
	}
	/**
	 * 逾期还款 
	 * @return  overdueRepayment
	 */
	@Column(name = "overdueRepayment", precision = 18, scale = 4)
	public Double getOverdueRepayment() {
		return overdueRepayment;
	}
	/**
	 * @param overdueRepayment 逾期还款 
	 */
	public void setOverdueRepayment(Double overdueRepayment) {
		this.overdueRepayment = overdueRepayment;
	}
    @Column(name = "recharge")
	public Double getRecharge() {
		return recharge;
	}

	public void setRecharge(Double recharge) {
		this.recharge = recharge;
	}
	@Column(name = "viprecharge")
	public Double getViprecharge() {
		return viprecharge;
	}

	public void setViprecharge(Double viprecharge) {
		this.viprecharge = viprecharge;
	}
	@Column(name = "withdraw")
	public Double getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(Double withdraw) {
		this.withdraw = withdraw;
	}
	@Column(name = "vipwithdraw")
	public Double getVipwithdraw() {
		return vipwithdraw;
	}

	public void setVipwithdraw(Double vipwithdraw) {
		this.vipwithdraw = vipwithdraw;
	}

}