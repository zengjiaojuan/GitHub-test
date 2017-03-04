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
 * Title:Automatic
 * </p>
 * <p>
 * Description: 自动投标参数表
 * </p>
 *         <p>
 *         date 2014年2月14日
 *         </p>
 */
@Entity
@Table(name = "automatic")
public class Automatic implements java.io.Serializable {

    /** 主键 */
    private Long id;
    /** 用户 */
    private Userbasicsinfo userbasicsinfo;
    /** 每个用户对应设置的序号 */
    private Integer autouid;
    /** 年化利率起 */
    private Double yearratebegin;
    /** 年化利率止 */
    private Double yearrateend;
    /** 借款期限 */
    private Integer toborrow;
    /** 帐户保留金额 */
    private Double acount;
    /** 每次投标金额 */
    private Double tenderprice;
    /** 状态：0：取消1：启用 2：停用 */
    private Integer state;
    /** 录入时间 */
    private String entrytime;
    /** 修改时间 */
    private String updatetime;

    // Constructors

    /** default constructor */
    public Automatic() {
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
     * @param autouid
     *            每个用户对应设置的序号
     * @param yearratebegin
     *            年化利率起
     * @param yearrateend
     *            年化利率止
     * @param toborrow
     *            借款期限
     * @param acount
     *            帐户保留金额
     * @param tenderprice
     *            每次投标金额
     * @param state
     *            状态
     * @param entrytime
     *            录入时间
     * @param updatetime
     *            修改时间
     */
    public Automatic(Userbasicsinfo userbasicsinfo, Integer autouid,
            Double yearratebegin, Double yearrateend, Integer toborrow,
            Double acount, Double tenderprice, Integer state, String entrytime,
            String updatetime) {
        this.userbasicsinfo = userbasicsinfo;
        this.autouid = autouid;
        this.yearratebegin = yearratebegin;
        this.yearrateend = yearrateend;
        this.toborrow = toborrow;
        this.acount = acount;
        this.tenderprice = tenderprice;
        this.state = state;
        this.entrytime = entrytime;
        this.updatetime = updatetime;
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
    @JoinColumn(name = "userbasicinfo_id")
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
     * Title: getAutouid
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return autouid
     */
    @Column(name = "autouid")
    public Integer getAutouid() {
        return this.autouid;
    }

    /**
     * <p>
     * Title: setAutouid
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param autouid
     *            autouid
     */
    public void setAutouid(Integer autouid) {
        this.autouid = autouid;
    }

    /**
     * <p>
     * Title: getYearratebegin
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return yearratebegin
     */
    @Column(name = "yearratebegin", precision = 18, scale = 4)
    public Double getYearratebegin() {
        return yearratebegin;
    }

    /**
     * <p>
     * Title: setYearratebegin
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param yearratebegin
     *            yearratebegin
     */
    public void setYearratebegin(Double yearratebegin) {
        this.yearratebegin = yearratebegin;
    }

    /**
     * <p>
     * Title: getYearrateend
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return yearrateend
     */
    @Column(name = "yearrateend", precision = 18, scale = 4)
    public Double getYearrateend() {
        return yearrateend;
    }

    /**
     * <p>
     * Title: setYearrateend
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param yearrateend
     *            yearrateend
     */
    public void setYearrateend(Double yearrateend) {
        this.yearrateend = yearrateend;
    }

    /**
     * <p>
     * Title: getToborrow
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return toborrow
     */
    @Column(name = "toborrow")
    public Integer getToborrow() {
        return toborrow;
    }

    /**
     * <p>
     * Title: setToborrow
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param toborrow
     *            toborrow
     */
    public void setToborrow(Integer toborrow) {
        this.toborrow = toborrow;
    }

    /**
     * <p>
     * Title: getAcount
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return acount
     */
    @Column(name = "acount", precision = 18, scale = 4)
    public Double getAcount() {
        return this.acount;
    }

    /**
     * <p>
     * Title: setAcount
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param acount
     *            acount
     */
    public void setAcount(Double acount) {
        this.acount = acount;
    }

    /**
     * <p>
     * Title: getTenderprice
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return tenderprice
     */
    @Column(name = "tenderprice", precision = 18, scale = 4)
    public Double getTenderprice() {
        return this.tenderprice;
    }

    /**
     * <p>
     * Title: setTenderprice
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param tenderprice
     *            tenderprice
     */
    public void setTenderprice(Double tenderprice) {
        this.tenderprice = tenderprice;
    }

    /**
     * <p>
     * Title: getState
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return state
     */
    @Column(name = "state")
    public Integer getState() {
        return this.state;
    }

    /**
     * <p>
     * Title: setState
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param state
     *            state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * <p>
     * Title: getEntrytime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return entrytime
     */
    @Column(name = "entrytime", length = 30)
    public String getEntrytime() {
        return this.entrytime;
    }

    /**
     * <p>
     * Title: setEntrytime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param entrytime
     *            entrytime
     */
    public void setEntrytime(String entrytime) {
        this.entrytime = entrytime;
    }

    /**
     * <p>
     * Title: getUpdatetime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return updatetime
     */
    @Column(name = "updatetime", length = 30)
    public String getUpdatetime() {
        return this.updatetime;
    }

    /**
     * <p>
     * Title: setUpdatetime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param updatetime
     *            updatetime
     */
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

}