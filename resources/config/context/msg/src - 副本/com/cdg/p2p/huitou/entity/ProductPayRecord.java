package com.cddgg.p2p.huitou.entity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.pay.entity.BidInfo;
import com.cddgg.p2p.huitou.constant.enums.ENUM_DURING_TYPE;
import com.cddgg.p2p.huitou.util.Arith;

/**
 * ProductPayRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "product_pay_record")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer",
        "creditorPayRecords" })
public class ProductPayRecord implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private Long id;

    /**
     * 用户基本信息
     */
    private Userbasicsinfo userbasicsinfo;

    /**
     * 债权人基本信息
     */
    private Userbasicsinfo userbasicsinfoCreditor;
    
    /**
     * 后台管理员
     */
    private Adminuser adminuser;

    /**
     * 提成金额
     */
    private Double award;

    /**
     * 产品
     */
    private Product product;

    /**
     * 管理费
     */
    private Double moneyManager;

    /**
     * 源产品期限
     */
    private Long productTimeDuring;

    /**
     * 源产品年利率
     */
    private Double productRatePercentYear;

    /**
     * 源产品名称
     */
    private String productName;

    /**
     * 购买金额
     */
    private Double money;

    /**
     * 购买方式(0:线上购买，1:线下购买)
     */
    private Integer payType;

    /**
     * 成功认购时的日期，精确到分钟
     */
    private String timeStart;

    /**
     * 开始认购时间
     */
    private String timePayStart;

    /**
     * 利息收益
     */
    private Double rateSum;

    /**
     * 预计收益总额[本金+利息]
     */
    private Double allSum;

    /**
     * 利息支付时间
     */
    private String timeRatePayFirst;

    /**
     * 本金支付支付时间
     */
    private String timePrincipalPay;

    /**
     * 状态(0:回购中,1:已完成,2:待分配)
     */
    private Integer status;
    /**
     *状态(0:未放款,1:已放款,2:已还款)
    */
    private Integer shows=0;
    /**
     * 已匹配的日期编号
     */
    private String alreadyDate;
    
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 产品认购编号
     */
    private String pBidNo;
    /**
     * 合同号
     */
    private String pContractNo;
    /**
     * 投标订单号
     */
    private String pMerBillNo;
    /**
     * 债权认购记录
     */
    private Set<CreditorPayRecord> creditorPayRecords = new HashSet<CreditorPayRecord>(
            0);


    /** default constructor */
    public ProductPayRecord() {
    }

    /**
     * full constructor
     * 
     * @param id
     *            id
     * @param userbasicsinfo
     *            userbasicsinfo
     * @param adminuser
     *            adminuser
     * @param award
     *            award
     * @param product
     *            product
     * @param moneyManager
     *            moneyManager
     * @param productTimeDuring
     *            productTimeDuring
     * @param productRatePercentYear
     *            productRatePercentYear
     * @param productName
     *            productName
     * @param money
     *            money
     * @param payType
     *            payType
     * @param timeStart
     *            timeStart
     * @param timePayStart
     *            timePayStart
     * @param rateSum
     *            rateSum
     * @param allSum
     *            allSum
     * @param timeRatePayFirst
     *            timeRatePayFirst
     * @param timePrincipalPay
     *            timePrincipalPay
     * @param status
     *            status
     * @param creditorPayRecords
     *            creditorPayRecords
     * @param endTime
     *            endTime
     * @param pBidNo
     *            pBidNo
     * @param pContractNo
     *            pContractNo
     * @param pMerBillNo
     *            pMerBillNo
     * @param pMerchantBillNo pMerchantBillNo
     */
    public ProductPayRecord(Long id, Userbasicsinfo userbasicsinfo,
            Adminuser adminuser, Double award, Product product,
            Double moneyManager, Long productTimeDuring,
            Double productRatePercentYear, String productName, Double money,
            Integer payType, String timeStart, String timePayStart,
            Double rateSum, Double allSum, String timeRatePayFirst,
            String timePrincipalPay, Integer status, String endTime,
            String pBidNo, String pContractNo, String pMerBillNo,
            String pMerchantBillNo,
            Set<CreditorPayRecord> creditorPayRecords) {
        super();
        this.id = id;
        this.userbasicsinfo = userbasicsinfo;
        this.adminuser = adminuser;
        this.award = award;
        this.product = product;
        this.moneyManager = moneyManager;
        this.productTimeDuring = productTimeDuring;
        this.productRatePercentYear = productRatePercentYear;
        this.productName = productName;
        this.money = money;
        this.payType = payType;
        this.timeStart = timeStart;
        this.timePayStart = timePayStart;
        this.rateSum = rateSum;
        this.allSum = allSum;
        this.timeRatePayFirst = timeRatePayFirst;
        this.timePrincipalPay = timePrincipalPay;
        this.status = status;
        this.creditorPayRecords = creditorPayRecords;
        // this.productRepayMoneyRecords = productRepayMoneyRecords;
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
     * adminuser
     * 
     * @return adminuser
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    public Adminuser getAdminuser() {
        return this.adminuser;
    }

    /**
     * adminuser
     * 
     * @param adminuser
     *            adminuser
     */
    public void setAdminuser(Adminuser adminuser) {
        this.adminuser = adminuser;
    }

    /**
     * userbasicsinfo
     * 
     * @return userbasicsinfo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userbasic_id")
    public Userbasicsinfo getUserbasicsinfo() {
        return this.userbasicsinfo;
    }

    /**
     * userbasicsinfo
     * 
     * @param userbasicsinfo
     *            userbasicsinfo
     */
    public void setUserbasicsinfo(Userbasicsinfo userbasicsinfo) {
        this.userbasicsinfo = userbasicsinfo;
    }

    /**
     * userbasicsinfoCreditor
     * @return  userbasicsinfoCreditor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userbasic_creditor_id")
    public Userbasicsinfo getUserbasicsinfoCreditor() {
        return userbasicsinfoCreditor;
    }

    /**
     * userbasicsinfoCreditor
     * @param userbasicsinfoCreditor    userbasicsinfoCreditor
     */
    public void setUserbasicsinfoCreditor(Userbasicsinfo userbasicsinfoCreditor) {
        this.userbasicsinfoCreditor = userbasicsinfoCreditor;
    }

    /**
     * 计算客服人员提成
     */
    private void execAward() {

        this.adminuser = userbasicsinfo.getUserrelationinfo().getAdminuser();

        if (this.adminuser != null) {

            this.award = Arith.mul(this.money, this.product.getAward())
                    .doubleValue();
            ;

        }
    }

    /**
     * 计算收益
     */
    private void execMoney() {

        this.rateSum = Arith.mul(this.money, this.product.getRate())
                .doubleValue();
        this.allSum = Arith.add(this.money, this.rateSum).doubleValue();
    }

    /**
     * 计算时间
     * 
     * @throws ParseException
     *             时间格式化异常
     */
    private void execTime() throws ParseException{
        
        switch(ENUM_DURING_TYPE.values()[this.product.getProductType().getDayType()]){
            case DAY:
                this.timePrincipalPay = DateUtils.add(DateUtils.DEFAULT_DATE_FORMAT, Calendar.DATE, this.product.getProductType().getDayDuring().intValue());
                this.timeRatePayFirst = this.timePrincipalPay;
                break;
            case MONTH:
                this.timePrincipalPay = DateUtils.add(DateUtils.DEFAULT_DATE_FORMAT, Calendar.MONTH, this.product.getProductType().getDayDuring().intValue());
                this.timeRatePayFirst = DateUtils.add(DateUtils.DEFAULT_DATE_FORMAT, Calendar.MONTH, 1);
                this.timeRatePayFirst = this.timeRatePayFirst.substring(0, this.timeRatePayFirst.length()-2)+"08";
                break;
            case YEAR:
                this.timePrincipalPay = DateUtils.add(DateUtils.DEFAULT_DATE_FORMAT, Calendar.YEAR, this.product.getProductType().getDayDuring().intValue());
                this.timeRatePayFirst = this.timePrincipalPay;
                break;
        }

        this.endTime = this.timePrincipalPay;
    }

    /**
     * 计算
     * 
     * @throws ParseException
     *             时间格式化异常
     */
    public void execute() throws ParseException {

        execAward();

        execMoney();

        execTime();
    }

    /**
     * product
     * 
     * @return product
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    public Product getProduct() {
        return this.product;
    }

    /**
     * product
     * 
     * @param product
     *            product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * moneyManager
     * 
     * @return moneyManager
     */
    @Column(name = "money_manager", precision = 20, scale = 4)
    public Double getMoneyManager() {
        return this.moneyManager;
    }

    /**
     * moneyManager
     * 
     * @param moneyManager
     *            moneyManager
     */
    public void setMoneyManager(Double moneyManager) {
        this.moneyManager = moneyManager;
    }

    /**
     * productTimeDuring
     * 
     * @return productTimeDuring
     */
    @Column(name = "product_time_during")
    public Long getProductTimeDuring() {
        return this.productTimeDuring;
    }

    /**
     * productTimeDuring
     * 
     * @param productTimeDuring
     *            productTimeDuring
     */
    public void setProductTimeDuring(Long productTimeDuring) {
        this.productTimeDuring = productTimeDuring;
    }

    /**
     * productRatePercentYear
     * 
     * @return productRatePercentYear
     */
    @Column(name = "product_rate_percent_year", precision = 20, scale = 4)
    public Double getProductRatePercentYear() {
        return this.productRatePercentYear;
    }

    /**
     * productRatePercentYear
     * 
     * @param productRatePercentYear
     *            productRatePercentYear
     */
    public void setProductRatePercentYear(Double productRatePercentYear) {
        this.productRatePercentYear = productRatePercentYear;
    }

    /**
     * money
     * 
     * @return money
     */
    @Column(name = "money", precision = 20, scale = 4)
    public Double getMoney() {
        return this.money;
    }

    /**
     * money
     * 
     * @param money
     *            money
     */
    public void setMoney(Double money) {
        this.money = money;
    }

    /**
     * payType
     * 
     * @return payType
     */
    @Column(name = "pay_type")
    public Integer getPayType() {
        return this.payType;
    }

    /**
     * payType
     * 
     * @param payType
     *            payType
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * timeStart
     * 
     * @return timeStart
     */
    @Column(name = "time_start", length = 20)
    public String getTimeStart() {
        return this.timeStart;
    }

    /**
     * timeStart
     * 
     * @param timeStart
     *            timeStart
     */
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * timePayStart
     * 
     * @return timePayStart
     */
    @Column(name = "time_pay_start", length = 20)
    public String getTimePayStart() {
        return this.timePayStart;
    }

    /**
     * timePayStart
     * 
     * @param timePayStart
     *            timePayStart
     */
    public void setTimePayStart(String timePayStart) {
        this.timePayStart = timePayStart;
    }

    /**
     * rateSum
     * 
     * @return rateSum
     */
    @Column(name = "rate_sum", precision = 20, scale = 4)
    public Double getRateSum() {
        return this.rateSum;
    }

    /**
     * rateSum
     * 
     * @param rateSum
     *            rateSum
     */
    public void setRateSum(Double rateSum) {
        this.rateSum = rateSum;
    }

    /**
     * allSum
     * 
     * @return allSum
     */
    @Column(name = "all_sum", precision = 20, scale = 4)
    public Double getAllSum() {
        return this.allSum;
    }

    /**
     * allSum
     * 
     * @param allSum
     *            allSum
     */
    public void setAllSum(Double allSum) {
        this.allSum = allSum;
    }

    /**
     * timeRatePayFirst
     * 
     * @return timeRatePayFirst
     */
    @Column(name = "time_rate_pay_first", length = 20)
    public String getTimeRatePayFirst() {
        return this.timeRatePayFirst;
    }

    /**
     * timeRatePayFirst
     * 
     * @param timeRatePayFirst
     *            timeRatePayFirst
     */
    public void setTimeRatePayFirst(String timeRatePayFirst) {
        this.timeRatePayFirst = timeRatePayFirst;
    }

    /**
     * timePrincipalPay
     * 
     * @return timePrincipalPay
     */
    @Column(name = "time_principal_pay", length = 20)
    public String getTimePrincipalPay() {
        return this.timePrincipalPay;
    }

    /**
     * timePrincipalPay
     * 
     * @param timePrincipalPay
     *            timePrincipalPay
     */
    public void setTimePrincipalPay(String timePrincipalPay) {
        this.timePrincipalPay = timePrincipalPay;
    }

    /**
     * status
     * 
     * @return status
     */
    @Column(name = "status")
    public Integer getStatus() {
        return this.status;
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
     * @return  shows
     */
    @Column(name = "shows")
    public Integer getShows() {
        return shows;
    }

    /**
     * shows
     * @param shows shows
     */
    public void setShows(Integer shows) {
        this.shows = shows;
    }

    /**
     * award
     * 
     * @return award
     */
    @Column(name = "award", precision = 20, scale = 4)
    public Double getAward() {
        return this.award;
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
     * creditorPayRecords
     * 
     * @return creditorPayRecords
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "productPayRecord")
    public Set<CreditorPayRecord> getCreditorPayRecords() {
        return this.creditorPayRecords;
    }

    /**
     * creditorPayRecords
     * 
     * @param creditorPayRecords
     *            creditorPayRecords
     */
    public void setCreditorPayRecords(Set<CreditorPayRecord> creditorPayRecords) {
        this.creditorPayRecords = creditorPayRecords;
    }
    
    

    /**
    * <p>Title: getAlreadyDate</p>
    * <p>Description: </p>
    * @return alreadyDate
    */
    @Column(name = "already_date", length = 255)
    public String getAlreadyDate() {
        return alreadyDate;
    }

    /**
    * <p>Title: setAlreadyDate</p>
    * <p>Description: </p>
    * @param alreadyDate alreadyDate
    */
    public void setAlreadyDate(String alreadyDate) {
        this.alreadyDate = alreadyDate;
    }


    /**
     * endTime
     * 
     * @return endTime
     */
    @Column(name = "endTime")
    public String getEndTime() {
        return endTime;
    }
    /**
     * @param endTime
     *            endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    /**
     * @return pBidNo 标号
     */
    @Column(name = "pBidNo",length = 30)
    public String getpBidNo() {
        return pBidNo;
    }
    /**
     * 
     * @param pBidNo 标号
     */
    public void setpBidNo(String pBidNo) {
        this.pBidNo = pBidNo;
    }
    /**
     * @return pContractNo 合同号
     */
    @Column(name = "pContractNo",length = 30)
    public String getpContractNo() {
        return pContractNo;
    }
    /**
     * @param pContractNo 合同号
     */
    public void setpContractNo(String pContractNo) {
        this.pContractNo = pContractNo;
    }
    /**
     * @return pMerBillNo 放款编号
     */
    @Column(name = "pMerBillNo",length = 30)
    public String getpMerBillNo() {
        return pMerBillNo;
    }
    /**
     * @param pMerBillNo 投标编号
     */
    public void setpMerBillNo(String pMerBillNo) {
        this.pMerBillNo = pMerBillNo;
    }
    
    /**
     * productName
     * 
     * @return productName
     */
    @Column(name = "product_name")
    public String getProductName() {
        return productName;
    }

    /**
     * productName
     * 
     * @param productName
     *            productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 设置环讯信息
     * @param info  info
     */
    public void setBidInfo(BidInfo info){
        this.pBidNo = info.getpBidNo();
        this.pContractNo = info.getpContractNo();
        this.pMerBillNo = info.getpMerBillNo();
    }

    /**
    * <p>Title: getAlreadyDates</p>
    * <p>Description: </p>
    * @return set
    */
    @Transient
    public Set<Long> getAlreadyDates(){
        
        Set<Long> set = new LinkedHashSet<>();
        for(String str : getAlreadyDate().split(",")){
            set.add(Long.parseLong(str));
        }
        return set;
    }
}