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
 * <p>
 * Title:Autointegral
 * </p>
 * <p>
 * Description: Autointegral自动积分表
 * </p>
 *         <p>
 *         date 2014年2月14日
 *         </p>
 */
@Entity
@Table(name = "autointegral")
public class Autointegral implements java.io.Serializable {

    /** 主键 */
    private Long id;
    /** 用户 */
    private Userbasicsinfo userbasicsinfo;
    /** 借款标 */
    private Loansign loansign;
    /** 还款计划 */
    private Repaymentrecord repaymentrecord;
    /** 预计积分 */
    private Integer predictintegral;
    /** 实际积分 */
    private Integer realityintegral;
    /** 是否逾期 */
    private Integer isover;

    // Constructors

    /** default constructor */
    public Autointegral() {
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param predictintegral
     *            预计积分
     * @param realityintegral
     *            实际积分
     */
    public Autointegral(Integer predictintegral, Integer realityintegral) {
        this.predictintegral = predictintegral;
        this.realityintegral = realityintegral;
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param userbasicsinfo
     *            用户
     * @param loansign
     *            借款标
     * @param repaymentrecord
     *            还款计划
     * @param predictintegral
     *            预计积分
     * @param realityintegral
     *            实际积分
     * @param isover
     *            是否逾期
     */
    public Autointegral(Userbasicsinfo userbasicsinfo, Loansign loansign,
            Repaymentrecord repaymentrecord, Integer predictintegral,
            Integer realityintegral, Integer isover) {
        this.userbasicsinfo = userbasicsinfo;
        this.loansign = loansign;
        this.repaymentrecord = repaymentrecord;
        this.predictintegral = predictintegral;
        this.realityintegral = realityintegral;
        this.isover = isover;
    }

    // Property accessors
    /**
     * <p>
     * Title: getId
     * </p>
     * <p>
     * Description:
     * </p>
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
     * <p>
     * Title: setId
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param id
     *            id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p>
     * Title: getUserbasicsinfo
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return userbasicsinfo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public Userbasicsinfo getUserbasicsinfo() {
        return this.userbasicsinfo;
    }

    /**
     * <p>
     * Title: setUserbasicsinfo
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param userbasicsinfo
     *            userbasicsinfo
     */
    public void setUserbasicsinfo(Userbasicsinfo userbasicsinfo) {
        this.userbasicsinfo = userbasicsinfo;
    }

    /**
     * <p>
     * Title: getLoansign
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return loansign
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loansign_id")
    public Loansign getLoansign() {
        return this.loansign;
    }

    /**
     * <p>
     * Title: setLoansign
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param loansign
     *            loansign
     */
    public void setLoansign(Loansign loansign) {
        this.loansign = loansign;
    }

    /**
     * <p>
     * Title: getRepaymentrecord
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return repaymentrecord
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repayment_id")
    public Repaymentrecord getRepaymentrecord() {
        return this.repaymentrecord;
    }

    /**
     * <p>
     * Title: setRepaymentrecord
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param repaymentrecord
     *            repaymentrecord
     */
    public void setRepaymentrecord(Repaymentrecord repaymentrecord) {
        this.repaymentrecord = repaymentrecord;
    }

    /**
     * <p>
     * Title: getPredictintegral
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return predictintegral
     */
    @Column(name = "predictintegral", nullable = false)
    public Integer getPredictintegral() {
        return this.predictintegral;
    }

    /**
     * <p>
     * Title: setPredictintegral
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param predictintegral
     *            predictintegral
     */
    public void setPredictintegral(Integer predictintegral) {
        this.predictintegral = predictintegral;
    }

    /**
     * <p>
     * Title: getRealityintegral
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return realityintegral
     */
    @Column(name = "realityintegral", nullable = false)
    public Integer getRealityintegral() {
        return this.realityintegral;
    }

    /**
     * <p>
     * Title: setRealityintegral
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param realityintegral
     *            realityintegral
     */
    public void setRealityintegral(Integer realityintegral) {
        this.realityintegral = realityintegral;
    }

    /**
     * <p>
     * Title: getIsover
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return isover
     */
    @Column(name = "isover")
    public Integer getIsover() {
        return this.isover;
    }

    /**
     * <p>
     * Title: setIsover
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param isover
     *            isover
     */
    public void setIsover(Integer isover) {
        this.isover = isover;
    }

}