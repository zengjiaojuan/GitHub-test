package com.cddgg.p2p.huitou.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @version: 1.0
 * @since: JDK 1.7.0_25 Create at: 2014年2月26日 下午5:14:05 Description: 借款标类型标
 * 
 */
@Entity
@Table(name = "loansign_type")
public class LoansignType implements java.io.Serializable {

    /**
     * id
     */
    private Long id;

    /** 类型名称 */
    private String typename;
    /** 最小借款额度 */
    private Double mincredit;

    /** 最大借款额度 */
    private Double maxcredit;

    /** 最小借款期限 */
    private Integer minmoney;

    /** 最大借款期限 */
    private Integer maxmoney;

    /** 最低借款利率 */
    private Double minrate;

    /** 最高借款利率 */
    private Double maxrate;

    /** 借款标期 */
    private Integer money;

    /**
     * <p>
     * Title: LoansignType
     * </p>
     * <p>
     * Description:无参数构造
     * </p>
     */
    public LoansignType() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * <p>
     * Title: LoansignType
     * </p>
     * <p>
     * Description: 有参构造
     * </p>
     * 
     * @param id
     *            序号
     * @param typename
     *            类型名
     * @param mincredit
     *            最小借款额度
     * @param maxcredit
     *            最大借款额度
     * @param minmoney
     *            最小借款期限
     * @param maxmoney
     *            最大借款期限
     * @param minrate
     *            最低借款利率
     * @param maxrate
     *            最高借款利率
     * @param money
     *            借款标期
     */
    public LoansignType(Long id, String typename, Double mincredit,
            Double maxcredit, Integer minmoney, Integer maxmoney,
            Double minrate, Double maxrate, int money) {
        super();
        this.id = id;
        this.typename = typename;
        this.mincredit = mincredit;
        this.maxcredit = maxcredit;
        this.minmoney = minmoney;
        this.maxmoney = maxmoney;
        this.minrate = minrate;
        this.maxrate = maxrate;
        this.money = money;
    }

    /**
     * <p>
     * Title: getId
     * </p>
     * <p>
     * Description: 序号
     * </p>
     * 
     * @return 序号
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
     * <p>
     * Title: setId
     * </p>
     * <p>
     * Description: 序号
     * </p>
     * 
     * @param id
     *            序号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p>
     * Title: getTypename
     * </p>
     * <p>
     * Description:最小借款额度
     * </p>
     * 
     * @return String
     */
    @Column(name = "typename", length = 20)
    public String getTypename() {
        return typename;
    }

    /**
     * <p>
     * Title: setTypename
     * </p>
     * <p>
     * Description: 最小借款额度
     * </p>
     * 
     * @param typename
     *            typename
     */
    public void setTypename(String typename) {
        this.typename = typename;
    }

    /**
     * <p>
     * Title: getMincredit
     * </p>
     * <p>
     * Description: 最小借款额度
     * </p>
     * 
     * @return Double
     */
    @Column(name = "mincredit", precision = 18, scale = 4)
    public Double getMincredit() {
        return mincredit;
    }

    /**
     * <p>
     * Title: setMincredit
     * </p>
     * <p>
     * Description: 最小借款额度
     * </p>
     * 
     * @param mincredit
     *            最小借款额度
     */
    public void setMincredit(Double mincredit) {
        this.mincredit = mincredit;
    }

    /**
     * <p>
     * Title: getMincredit
     * </p>
     * <p>
     * Description: 最大借款额度
     * </p>
     * 
     * @return Double
     */
    @Column(name = "maxcredit", precision = 18, scale = 4)
    public Double getMaxcredit() {
        return maxcredit;
    }

    /**
     * <p>
     * Title: setMaxcredit
     * </p>
     * <p>
     * Description: 最大借款额度
     * </p>
     * 
     * @param maxcredit
     *            最大借款额度
     */
    public void setMaxcredit(Double maxcredit) {
        this.maxcredit = maxcredit;
    }

    /**
     * <p>
     * Title: getMinmoney
     * </p>
     * <p>
     * Description: 最小借款期限
     * </p>
     * 
     * @return 最小借款期限
     */
    @Column(name = "minmoney")
    public Integer getMinmoney() {
        return minmoney;
    }

    /**
     * <p>
     * Title: setMinmoney
     * </p>
     * <p>
     * Description: 最小借款期限
     * </p>
     * 
     * @param minmoney
     *            最小借款期限
     */
    public void setMinmoney(Integer minmoney) {
        this.minmoney = minmoney;
    }

    /**
     * <p>
     * Title: getMinmoney
     * </p>
     * <p>
     * Description: 最大借款期限
     * </p>
     * 
     * @return 最大借款期限
     */
    @Column(name = "maxmoney")
    public Integer getMaxmoney() {
        return maxmoney;
    }

    /**
     * <p>
     * Title: setMaxmoney
     * </p>
     * <p>
     * Description: 最大借款期限
     * </p>
     * 
     * @param maxmoney
     *            最大借款期限
     */
    public void setMaxmoney(Integer maxmoney) {
        this.maxmoney = maxmoney;
    }

    /**
     * <p>
     * Title: getMinrate
     * </p>
     * <p>
     * Description: 最低借款利率
     * </p>
     * 
     * @return 最低借款利率
     */
    @Column(name = "minrate", precision = 18, scale = 4)
    public Double getMinrate() {
        return minrate;
    }

    /**
     * <p>
     * Title: setMinrate
     * </p>
     * <p>
     * Description: 最低借款利率
     * </p>
     * 
     * @param minrate
     *            最低借款利率
     */
    public void setMinrate(Double minrate) {
        this.minrate = minrate;
    }

    /**
     * <p>
     * Title: getMaxrate
     * </p>
     * <p>
     * Description: 最高借款利率
     * </p>
     * 
     * @return 最高借款利率
     */
    @Column(name = "maxrate", precision = 18, scale = 4)
    public Double getMaxrate() {
        return maxrate;
    }

    /**
     * <p>
     * Title: setMaxrate
     * </p>
     * <p>
     * Description: 最高借款利率
     * </p>
     * 
     * @param maxrate
     *            最高借款利率
     */
    public void setMaxrate(Double maxrate) {
        this.maxrate = maxrate;
    }

    /**
     * <p>
     * Title: getMoney
     * </p>
     * <p>
     * Description: 借款标期
     * </p>
     * 
     * @return 借款标期
     */
    @Column(name = "money")
    public Integer getMoney() {
        return money;
    }

    /**
     * <p>
     * Title: setMoney
     * </p>
     * <p>
     * Description: 借款标期
     * </p>
     * 
     * @param money
     *            借款标期
     */
    public void setMoney(Integer money) {
        this.money = money;
    }

}
