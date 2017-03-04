package com.cddgg.p2p.huitou.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 产品类型
 */
@Entity
@Table(name = "product_type")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","products"})
public class ProductType implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 利息支付方式
     */
    private String ratePayType;
    /**
     * 本金支付方式
     */
    private String principalPayType;
    /**
     * 客户总收益计算方式
     */
    private String earningsExpression;
    
    /**
     * 期限类型
     */
    private Integer dayType;
    
    /**
     * 产品期限
     */
    private Long dayDuring;
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
     * 年化利率
     */
    private Double ratePercentYear;
    /**
     * 产品描述
     */
    private String remark;
    /**
     * 员工奖励
     */
    private Double employeeAward;

    /**
     * 产品集合
     */
    private Set<Product> products = new HashSet<Product>(0);

    // Constructors

    /** default constructor */
    public ProductType() {
    }

    /**
     * minimal constructor
     * @param name              name
     * @param ratePayType       ratePayType
     * @param principalPayType  principalPayType
     * @param earningsExpression    earningsExpression
     */
    public ProductType(String name, String ratePayType,
            String principalPayType, String earningsExpression) {
        this.name = name;
        this.ratePayType = ratePayType;
        this.principalPayType = principalPayType;
        this.earningsExpression = earningsExpression;
    }

    /**
     * full constructor
     * @param name              name
     * @param ratePayType       ratePayType
     * @param principalPayType  principalPayType
     * @param earningsExpression    earningsExpression
     * @param products  products
     */
    public ProductType(String name, String ratePayType,
            String principalPayType, String earningsExpression,
            Set<Product> products) {
        this.name = name;
        this.ratePayType = ratePayType;
        this.principalPayType = principalPayType;
        this.earningsExpression = earningsExpression;
        this.products = products;
    }

    /**
     * Property accessors
     * @return  id
     */
    @Id
    @GeneratedValue
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
     * name
     * @return  name
     */
    @Column(name = "name", nullable = false, length = 20)
    public String getName() {
        return this.name;
    }

    /**
     * name
     * @param name  name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ratePayType
     * @return  ratePayType
     */
    @Column(name = "rate_pay_type", nullable = false, length = 20)
    public String getRatePayType() {
        return this.ratePayType;
    }

    /**
     * ratePayType
     * @param ratePayType   ratePayType
     */
    public void setRatePayType(String ratePayType) {
        this.ratePayType = ratePayType;
    }

    /**
     * dayDuring
     * @return  dayDuring
     */
    @Column(name = "day_during")
    public Long getDayDuring() {
        return this.dayDuring;
    }

    /**
     * dayDuring
     * @param dayDuring dayDuring
     */
    public void setDayDuring(Long dayDuring) {
        this.dayDuring = dayDuring;
    }

    /**
     * investOnlineMin
     * @return  investOnlineMin investOnlineMin
     */
    @Column(name = "invest_online_min", precision = 20, scale = 4)
    public Double getInvestOnlineMin() {
        return this.investOnlineMin;
    }

    /**
     * investOnlineMin
     * @param investOnlineMin   investOnlineMin
     */
    public void setInvestOnlineMin(Double investOnlineMin) {
        this.investOnlineMin = investOnlineMin;
    }

    /**
     * investOfflineMin
     * @return  investOfflineMin
     */
    @Column(name = "invest_offline_min", precision = 20, scale = 4)
    public Double getInvestOfflineMin() {
        return this.investOfflineMin;
    }

    /**
     * investOfflineMin
     * @param investOfflineMin  investOfflineMin
     */
    public void setInvestOfflineMin(Double investOfflineMin) {
        this.investOfflineMin = investOfflineMin;
    }

    /**
     * ratePercentYear
     * @return  ratePercentYear
     */
    @Column(name = "rate_percent_year", precision = 20, scale = 4)
    public Double getRatePercentYear() {
        return this.ratePercentYear;
    }

    /**
     * ratePercentYear
     * @param ratePercentYear   ratePercentYear
     */
    public void setRatePercentYear(Double ratePercentYear) {
        this.ratePercentYear = ratePercentYear;
    }

    /**
     * remark
     * @return  remark
     */
    @Column(name = "remark", length = 65535)
    public String getRemark() {
        return this.remark;
    }

    /**
     * remark
     * @param remark    remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * employeeAward
     * @return  employeeAward
     */
    @Column(name = "employee_award", precision = 20, scale = 4)
    public Double getEmployeeAward() {
        return this.employeeAward;
    }

    /**
     * employeeAward
     * @param employeeAward employeeAward
     */
    public void setEmployeeAward(Double employeeAward) {
        this.employeeAward = employeeAward;
    }

    /**
     * principalPayType
     * @return  principalPayType
     */
    @Column(name = "principal_pay_type", nullable = false, length = 20)
    public String getPrincipalPayType() {
        return this.principalPayType;
    }

    /**
     * principalPayType
     * @param principalPayType  principalPayType
     */
    public void setPrincipalPayType(String principalPayType) {
        this.principalPayType = principalPayType;
    }

    /**
     * earningsExpression
     * @return  earningsExpression
     */
    @Column(name = "earnings_expression", nullable = false, length = 50)
    public String getEarningsExpression() {
        return this.earningsExpression;
    }

    /**
     * earningsExpression
     * @param earningsExpression    earningsExpression
     */
    public void setEarningsExpression(String earningsExpression) {
        this.earningsExpression = earningsExpression;
    }

    /**
     * products
     * @return  products
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "productType")
    public Set<Product> getProducts() {
        return this.products;
    }

    /**
     * products
     * @param products  products
     */
    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    /**
     * investMax
     * @return  investMax
     */
    @Column(name = "invest_max", precision = 20, scale = 4)
    public Double getInvestMax() {
        return investMax;
    }

    /**
     * investMax
     * @param investMax investMax
     */
    public void setInvestMax(Double investMax) {
        this.investMax = investMax;
    }

    /**
     * 日期类型
     * @return  dayType
     */
    @Column(name = "day_type", nullable = false)
    public Integer getDayType() {
        return dayType;
    }

    /**
     * dayType
     * @param dayType   dayType
     */
    public void setDayType(Integer dayType) {
        this.dayType = dayType;
    }

}