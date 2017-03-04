package com.cddgg.p2p.huitou.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * Title:Adminuserfundinfo
 * </p>
 * <p>
 * Description: Adminuserfundinfo 平台资金表
 * </p>
 *         <p>
 *         date 2014年2月14日
 *         </p>
 */
@Entity
@Table(name = "adminuserfundinfo")
public class Adminuserfundinfo implements java.io.Serializable {

    /** 主键 */
    private Long id;
    /** 可用余额 */
    private Double cashBalance;

    /** default constructor */
    public Adminuserfundinfo() {
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param cashBalance
     *            可用余额
     */
    public Adminuserfundinfo(Double cashBalance) {
        this.cashBalance = cashBalance;
    }

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
     * Title: getCashBalance
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return cashBalance
     */
    @Column(name = "cashBalance", precision = 18, scale = 4)
    public Double getCashBalance() {
        return this.cashBalance;
    }

    /**
     * <p>
     * Title: setCashBalance
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param cashBalance
     *            cashBalance
     */
    public void setCashBalance(Double cashBalance) {
        this.cashBalance = cashBalance;
    }

}