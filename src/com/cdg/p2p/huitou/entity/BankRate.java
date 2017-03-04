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
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * BankRate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "bank_rate", uniqueConstraints = @UniqueConstraint(columnNames = "during"))
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","products"})
public class BankRate implements java.io.Serializable {

    // Fields

    /**
     * 主键
     */
    private Long id;
    /**
     * 期限
     */
    private String during;
    /**
     * 利率
     */
    private Double rate;
    /**
     * 修改时间
     */
    private String timeUpdate;
    /**
     * 关联产品
     */
    private Set<Product> products = new HashSet<Product>(0);

    // Constructors

    /** default constructor */
    public BankRate() {
    }

    /** minimal constructor */
    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param during
     *            期限
     * @param rate
     *            利率
     * @param timeUpdate
     *            修改时间
     */
    public BankRate(String during, Double rate, String timeUpdate) {
        this.during = during;
        this.rate = rate;
        this.timeUpdate = timeUpdate;
    }

    /** full constructor */
    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @param during  期限
    * @param rate 利率
    * @param timeUpdate 修改时间
    * @param products 产品
    */
    public BankRate(String during, Double rate, String timeUpdate,
            Set<Product> products) {
        this.during = during;
        this.rate = rate;
        this.timeUpdate = timeUpdate;
        this.products = products;
    }

    // Property accessors
    /**
    * <p>Title: getId</p>
    * <p>Description: </p>
    * @return Long
    */
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
    * <p>Title: setId</p>
    * <p>Description: </p>
    * @param id 主键
    */
    public void setId(Long id) {
        this.id = id;
    }

    /**
    * <p>Title: getDuring</p>
    * <p>Description: </p>
    * @return String 
    */
    @Column(name = "during", nullable = false, length = 30)
    public String getDuring() {
        return this.during;
    }

    /**
    * <p>Title: setDuring</p>
    * <p>Description: </p>
    * @param during 期限
    */
    public void setDuring(String during) {
        this.during = during;
    }

    /**
    * <p>Title: getRate</p>
    * <p>Description: </p>
    * @return Double
    */
    @Column(name = "rate", nullable = false, precision = 20, scale = 4)
    public Double getRate() {
        return this.rate;
    }

    /**
    * <p>Title: setRate</p>
    * <p>Description: 设置利率</p>
    * @param rate 利率
    */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
    * <p>Title: getTimeUpdate</p>
    * <p>Description: </p>
    * @return String
    */
    @Column(name = "time_update", nullable = false, length = 20)
    public String getTimeUpdate() {
        return this.timeUpdate;
    }

    /**
    * <p>Title: setTimeUpdate</p>
    * <p>Description: </p>
    * @param timeUpdate  修改时间
    */
    public void setTimeUpdate(String timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    /**
    * <p>Title: getProducts</p>
    * <p>Description:  产品</p>
    * @return  产品
    */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bankRate")
    public Set<Product> getProducts() {
        return this.products;
    }

    /**
    * <p>Title: setProducts</p>
    * <p>Description: </p>
    * @param products  产品
    */
    public void setProducts(Set<Product> products) {
        this.products = products;
    }

}