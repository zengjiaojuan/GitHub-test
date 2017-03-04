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
 * Title:Manualintegral
 * </p>
 * <p>
 * Description: 手动积分表
 * </p>
 *         <p>
 *         date 2014年2月14日
 *         </p>
 */
@Entity
@Table(name = "manualintegral")
public class Manualintegral implements java.io.Serializable {

    /** 主键 */
    private Long id;
    /** 用户基本信息 */
    private Userbasicsinfo userbasicsinfo;
    /** 合计 */
    private Integer amountPoints;
    /** 银行流水 */
    private Integer bankWaterPoints;
    /** 复选框的值 */
    private String ckVaule;
    /** 信用卡账单 */
    private Integer creditCardPoints;
    /** 房产证得分 */
    private Integer houseCardPoints;
    /** 销售合同及发票 */
    private Integer salesContractInvoicePoints;
    /** 社保 */
    private Integer socialPoints;

    // Constructors

    /** default constructor */
    public Manualintegral() {
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
     *            用户基本信息
     * @param amountPoints
     *            合计
     * @param bankWaterPoints
     *            银行流水
     * @param ckVaule
     *            复选框的值
     * @param creditCardPoints
     *            信用卡账单
     * @param houseCardPoints
     *            房产证得分
     * @param salesContractInvoicePoints
     *            销售合同及发票
     * @param socialPoints
     *            社保
     */
    public Manualintegral(Userbasicsinfo userbasicsinfo, Integer amountPoints,
            Integer bankWaterPoints, String ckVaule, Integer creditCardPoints,
            Integer houseCardPoints, Integer salesContractInvoicePoints,
            Integer socialPoints) {
        this.userbasicsinfo = userbasicsinfo;
        this.amountPoints = amountPoints;
        this.bankWaterPoints = bankWaterPoints;
        this.ckVaule = ckVaule;
        this.creditCardPoints = creditCardPoints;
        this.houseCardPoints = houseCardPoints;
        this.salesContractInvoicePoints = salesContractInvoicePoints;
        this.socialPoints = socialPoints;
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
     * Title: getAmountPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return amountPoints
     */
    @Column(name = "amountPoints")
    public Integer getAmountPoints() {
        return this.amountPoints;
    }

    /**
     * <p>
     * Title: setAmountPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param amountPoints
     *            amountPoints
     */
    public void setAmountPoints(Integer amountPoints) {
        this.amountPoints = amountPoints;
    }

    /**
     * <p>
     * Title: getBankWaterPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return bankWaterPoints
     */
    @Column(name = "bankWaterPoints")
    public Integer getBankWaterPoints() {
        return this.bankWaterPoints;
    }

    /**
     * <p>
     * Title: setBankWaterPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param bankWaterPoints
     *            bankWaterPoints
     */
    public void setBankWaterPoints(Integer bankWaterPoints) {
        this.bankWaterPoints = bankWaterPoints;
    }

    /**
     * <p>
     * Title: getCkVaule
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return ckVaule
     */
    @Column(name = "ckVaule")
    public String getCkVaule() {
        return this.ckVaule;
    }

    /**
     * <p>
     * Title: setCkVaule
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param ckVaule
     *            ckVaule
     */
    public void setCkVaule(String ckVaule) {
        this.ckVaule = ckVaule;
    }

    /**
     * <p>
     * Title: getCreditCardPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return creditCardPoints
     */
    @Column(name = "creditCardPoints")
    public Integer getCreditCardPoints() {
        return this.creditCardPoints;
    }

    /**
     * <p>
     * Title: setCreditCardPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param creditCardPoints
     *            creditCardPoints
     */
    public void setCreditCardPoints(Integer creditCardPoints) {
        this.creditCardPoints = creditCardPoints;
    }

    /**
     * <p>
     * Title: getHouseCardPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return houseCardPoints
     */
    @Column(name = "houseCardPoints")
    public Integer getHouseCardPoints() {
        return this.houseCardPoints;
    }

    /**
     * <p>
     * Title: setHouseCardPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param houseCardPoints
     *            houseCardPoints
     */
    public void setHouseCardPoints(Integer houseCardPoints) {
        this.houseCardPoints = houseCardPoints;
    }

    /**
     * <p>
     * Title: getSalesContractInvoicePoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return salesContractInvoicePoints
     */
    @Column(name = "salesContractInvoicePoints")
    public Integer getSalesContractInvoicePoints() {
        return this.salesContractInvoicePoints;
    }

    /**
     * <p>
     * Title: setSalesContractInvoicePoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param salesContractInvoicePoints
     *            salesContractInvoicePoints
     */
    public void setSalesContractInvoicePoints(Integer salesContractInvoicePoints) {
        this.salesContractInvoicePoints = salesContractInvoicePoints;
    }

    /**
     * <p>
     * Title: getSocialPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return socialPoints
     */
    @Column(name = "socialPoints")
    public Integer getSocialPoints() {
        return this.socialPoints;
    }

    /**
     * <p>
     * Title: setSocialPoints
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param socialPoints
     *            socialPoints
     */
    public void setSocialPoints(Integer socialPoints) {
        this.socialPoints = socialPoints;
    }

}