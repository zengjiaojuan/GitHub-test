package com.cddgg.p2p.huitou.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.cddgg.p2p.huitou.constant.enums.ENUM_DURING_TYPE;
import com.cddgg.p2p.huitou.util.Arith;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedNativeQuery(name = "product_info_history", resultSetMapping = "product_info_history", query = "SELECT a.id,a.name, a.time_publish, a.invest_max, a.invest_max * a.rate money, ( SELECT COUNT(0) FROM product_pay_record b WHERE a.id = b.product_id ) cur_pay_num, ( SELECT COUNT(0) FROM product_pay_record b, product c, product_type d WHERE c.type = d.id AND b.product_id = c.id AND d.id = a.type ) all_pay_num, ( SELECT SUM(b.money) FROM product_pay_record b, product c, product_type d WHERE c.type = d.id AND b.product_id = c.id AND d.id = a.type ) all_pay_money FROM product a WHERE a.id = ? ORDER BY a.id DESC LIMIT 10")
@SqlResultSetMapping(name = "product_info_history", entities = {}, columns = {
        @ColumnResult(name = "id"), @ColumnResult(name = "name"),
        @ColumnResult(name = "time_publish"),
        @ColumnResult(name = "invest_max"), @ColumnResult(name = "money"),
        @ColumnResult(name = "cur_pay_num"),
        @ColumnResult(name = "all_pay_num"),
        @ColumnResult(name = "all_pay_money") })
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "productPayRecords" })
public class Product implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private Long id;

    /**
     * 银行利率
     */
    private BankRate bankRate;

    /**
     * 产品类型
     */
    private ProductType productType;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 线上投资起点
     */
    private Double investOnlineMin;

    /**
     * 线下投资起点
     */
    private Double investOfflineMin;

    /**
     * 最大投资金额
     */
    private Double investMax;

    /**
     * 已投资金额
     */
    private Double investedMoeny=0d;

    /**
     * 年化利率
     */
    private Double ratePercentYear;

    /**
     * 计算利率(在产品发布时动态计算所得)
     */
    private double rate;

    /**
     * 是否发布(0 未发布 1 已发布)
     */
    private Integer status;

    /**
     * 是否显示(0 不显示 1 显示)
     */
    private Integer shows;

    /**
     * 产品描述
     */
    private String remark;

    /**
     * 员工奖励
     */
    private Double employeeAward;

    /**
     * 计算后的员工奖励
     */
    private Double award;

    /**
     * 产品发布时间
     */
    private String timePublish;

    /**
     * 产品购买记录
     */
    private Set<ProductPayRecord> productPayRecords = new HashSet<ProductPayRecord>(
            0);

    /**
     * Hibernate数据版本编号
     */
    private Long version;

    // Constructors

    /** default constructor */
    public Product() {
    }

    /**
     * full constructor
     * 
     * @param id
     *            id
     * @param bankRate
     *            bankRate
     * @param productType
     *            productType
     * @param name
     *            name
     * @param investOnlineMin
     *            investOnlineMin
     * @param investOfflineMin
     *            investOfflineMin
     * @param investMax
     *            invest_max
     * @param investedMoeny
     *            invested_moeny
     * @param ratePercentYear
     *            ratePercentYear
     * @param rate
     *            rate
     * @param status
     *            status
     * @param shows
     *            shows
     * @param remark
     *            remark
     * @param employeeAward
     *            employeeAward
     * @param productPayRecords
     *            productPayRecords
     * @param version
     *            version
     */
    public Product(Long id, BankRate bankRate, ProductType productType,
            String name, Double investOnlineMin, Double investOfflineMin,
            Double investMax, Double investedMoeny, Double ratePercentYear,
            double rate, Integer status, Integer shows, String remark,
            Double employeeAward, Set<ProductPayRecord> productPayRecords,
            Long version) {
        super();
        this.id = id;
        this.bankRate = bankRate;
        this.productType = productType;
        this.name = name;
        this.investOnlineMin = investOnlineMin;
        this.investOfflineMin = investOfflineMin;
        this.investMax = investMax;
        this.investedMoeny = investedMoeny;
        this.ratePercentYear = ratePercentYear;
        this.rate = rate;
        this.status = status;
        this.shows = shows;
        this.remark = remark;
        this.employeeAward = employeeAward;
        this.productPayRecords = productPayRecords;
        this.version = version;
    }

    /**
     * Property accessors
     * 
     * @return id
     */
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * id
     * 
     * @param id
     *            id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * bankRate
     * 
     * @return bankRate
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_rate_id")
    public BankRate getBankRate() {
        return this.bankRate;
    }

    /**
     * bankRate
     * 
     * @param bankRate
     *            bankRate
     */
    public void setBankRate(BankRate bankRate) {
        this.bankRate = bankRate;
    }

    /**
     * productType
     * 
     * @return productType
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", nullable = false)
    public ProductType getProductType() {
        return this.productType;
    }

    /**
     * productType
     * 
     * @param productType
     *            productType
     */
    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    /**
     * name
     * 
     * @return name
     */
    @Column(name = "name", length = 50)
    public String getName() {
        return this.name;
    }

    /**
     * name
     * 
     * @param name
     *            name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * timePublish
     * 
     * @return timePublish
     */
    @Column(name = "time_publish", length = 20)
    public String getTimePublish() {
        return timePublish;
    }

    /**
     * timePublish
     * 
     * @param timePublish
     *            timePublish
     */
    public void setTimePublish(String timePublish) {
        this.timePublish = timePublish;
    }

    /**
     * investOnlineMin
     * 
     * @return investOnlineMin
     */
    @Column(name = "invest_online_min", precision = 20, scale = 4)
    public Double getInvestOnlineMin() {
        return this.investOnlineMin;
    }

    /**
     * investOnlineMin
     * 
     * @param investOnlineMin
     *            investOnlineMin
     */
    public void setInvestOnlineMin(Double investOnlineMin) {
        this.investOnlineMin = investOnlineMin;
    }

    /**
     * investOfflineMin
     * 
     * @return investOfflineMin
     */
    @Column(name = "invest_offline_min", precision = 20, scale = 4)
    public Double getInvestOfflineMin() {
        return this.investOfflineMin;
    }

    /**
     * investOfflineMin
     * 
     * @param investOfflineMin
     *            investOfflineMin
     */
    public void setInvestOfflineMin(Double investOfflineMin) {
        this.investOfflineMin = investOfflineMin;
    }

    /**
     * ratePercentYear
     * 
     * @return ratePercentYear
     */
    @Column(name = "rate_percent_year", precision = 20, scale = 4)
    public Double getRatePercentYear() {
        return this.ratePercentYear;
    }

    /**
     * ratePercentYear
     * 
     * @param ratePercentYear
     *            ratePercentYear
     */
    public void setRatePercentYear(Double ratePercentYear) {
        this.ratePercentYear = ratePercentYear;
    }

    /**
     * remark
     * 
     * @return remark
     */
    @Column(name = "remark", length = 65535)
    public String getRemark() {
        return this.remark;
    }

    /**
     * remark
     * 
     * @param remark
     *            remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * employeeAward
     * 
     * @return employeeAward
     */
    @Column(name = "employee_award", precision = 20, scale = 4)
    public Double getEmployeeAward() {
        return this.employeeAward;
    }

    /**
     * employeeAward
     * 
     * @param employeeAward
     *            employeeAward
     */
    public void setEmployeeAward(Double employeeAward) {
        this.employeeAward = employeeAward;
    }

    /**
     * productPayRecords
     * 
     * @return productPayRecords
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    public Set<ProductPayRecord> getProductPayRecords() {
        return this.productPayRecords;
    }

    /**
     * productPayRecords
     * 
     * @param productPayRecords
     *            productPayRecords
     */
    public void setProductPayRecords(Set<ProductPayRecord> productPayRecords) {
        this.productPayRecords = productPayRecords;
    }

    /**
     * rate
     * 
     * @return rate
     */
    @Column(name = "rate", precision = 20, scale = 4)
    public double getRate() {
        return rate;
    }

    /**
     * rate
     * 
     * @param rate
     *            rate
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * status
     * 
     * @return status
     */
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    /**
     * status
     * 
     * @param status
     *            status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * shows
     * 
     * @return shows
     */
    @Column(name = "shows")
    public Integer getShows() {
        return shows;
    }

    /**
     * shows
     * 
     * @param shows
     *            shows
     */
    public void setShows(Integer shows) {
        this.shows = shows;
    }

    /**
     * investMax
     * 
     * @return investMax
     */
    @Column(name = "invest_max", precision = 20, scale = 4)
    public Double getInvestMax() {
        return investMax;
    }

    /**
     * invest_max
     * 
     * @param investMax
     *            invest_max
     */
    public void setInvestMax(Double investMax) {
        this.investMax = investMax;
    }

    /**
     * investedMoeny
     * 
     * @return investedMoeny
     */
    @Column(name = "invested_moeny", precision = 20, scale = 4)
    public Double getInvestedMoeny() {
        return investedMoeny;
    }

    /**
     * investedMoeny
     * 
     * @param investedMoeny
     *            investedMoeny
     */
    public void setInvestedMoeny(Double investedMoeny) {
        this.investedMoeny = investedMoeny;
    }

    /**
     * version
     * 
     * @return version
     */
    @Version
    public Long getVersion() {
        return version;
    }

    /**
     * version
     * 
     * @param version
     *            version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * award
     * 
     * @return award
     */
    @Column(name = "award", precision = 20, scale = 4)
    public Double getAward() {
        return award;
    }

    /**
     * award
     * 
     * @param award
     *            award
     */
    public void setAward(Double award) {
        this.award = award;
    }

    /**
     * 计算员工奖励
     */
    public void execAward() {

        switch (ENUM_DURING_TYPE.values()[this.getProductType().getDayType()]) {
        case DAY:

            this.award = Arith
                    .mul(this.getProductType().getDayDuring(),
                            this.getEmployeeAward())
                    .divide(new BigDecimal(365)).doubleValue();
            break;
        case MONTH:
            this.award = Arith
                    .mul(this.getProductType().getDayDuring(),
                            this.getEmployeeAward()).divide(new BigDecimal(12))
                    .doubleValue();
            break;
        case YEAR:
            this.award = Arith.mul(this.getProductType().getDayDuring(),
                    this.getEmployeeAward()).doubleValue();
            break;
        }

    }

    /**
     * 计算利息
     */
    public void execRate() {

        switch (ENUM_DURING_TYPE.values()[this.getProductType().getDayType()]) {
        case DAY:
            this.rate = Arith.div(this.ratePercentYear, 365,20)
                    .multiply(new BigDecimal(this.productType.getDayDuring()))
                    .doubleValue();
            break;
        case MONTH:
            this.rate = Arith.div(this.ratePercentYear, 12,20)
                    .multiply(new BigDecimal(this.productType.getDayDuring()))
                    .doubleValue();
            break;
        case YEAR:
            this.rate = Arith.mul(this.ratePercentYear,
                    this.productType.getDayDuring()).doubleValue();
            break;
        }

    }

    /**
     * 是否可以购买
     * 
     * @param money
     *            购买金额
     * @return boolean
     */
    public boolean isPayAble(double money) {

        if (this.investedMoeny + money > this.investMax) {
            return false;
        }
        return true;
    }

    /**
     * 购买
     * 
     * @param money
     *            金额
     */
    public void pay(double money) {

        this.investedMoeny += money;
    }

    /**
     * 得到银行利息
     * 
     * @return 银行利息
     */
    @Transient
    public double getBankValue() {

        if (this.getBankRate().getId() == 1) {

            return Arith
                    .mul(this.getBankRate().getRate(), this.investMax)
                    .divide(new BigDecimal(365),20, RoundingMode.HALF_EVEN)
                    .multiply(
                            new BigDecimal(this.getProductType().getDayDuring()))
                    .doubleValue();

        } else if (this.getBankRate().getId() == 2) {

            return Arith.mul(this.getBankRate().getRate(), this.investMax)
                    .divide(new BigDecimal(12),20, RoundingMode.HALF_EVEN).multiply(new BigDecimal(3))
                    .doubleValue();

        } else if (this.getBankRate().getId() == 3) {
            
            return Arith.mul(this.getBankRate().getRate(), this.investMax)
                    .divide(new BigDecimal(12),20, RoundingMode.HALF_EVEN).multiply(new BigDecimal(6))
                    .doubleValue();

        } else if (this.getBankRate().getId() == 4) {

            return Arith.mul(this.getBankRate().getRate(), this.investMax)
                    .doubleValue();

        } else if (this.getBankRate().getId() == 5) {

            return Arith.mul(this.getBankRate().getRate(), this.investMax)
                    .multiply(new BigDecimal(2)).doubleValue();

        } else if (this.getBankRate().getId() == 6) {

            return Arith.mul(this.getBankRate().getRate(), this.investMax)
                    .multiply(new BigDecimal(3)).doubleValue();

        } else if (this.getBankRate().getId() == 7) {

            return Arith.mul(this.getBankRate().getRate(), this.investMax)
                    .multiply(new BigDecimal(5)).doubleValue();

        }

        return 0;
    }
    
}