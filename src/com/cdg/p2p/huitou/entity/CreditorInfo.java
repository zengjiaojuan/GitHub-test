package com.cddgg.p2p.huitou.entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * CreditorInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "creditor_info")
@JsonIgnoreProperties(value = { "creditor" })
public class CreditorInfo implements java.io.Serializable {

    // Fields

    /**
     * 债券编号
     */
    private Long id;

    /**
     * 债权基本信息
     */
    private Creditor creditor;

    /**
     * 借款人姓名
     */
    private String name;

    /**
     * 借款人性别
     */
    private String sex;

    /**
     * 借款人身份证
     */
    private String idCard;

    /**
     * 借款人手机
     */
    private String phone;

    /**
     * 借款金额
     */
    private String money;

    /**
     * 借款开始日期
     */
    private String timeStart;

    /**
     * 借款到期日期
     */
    private String timeEnd;

    /**
     * 共同借款人
     */
    private String peopleCommonName;

    /**
     * 共同借款人身份证
     */
    private String peopleCommonIdCard;

    /**
     * 还款方式
     */
    private String repaymentType;

    /**
     * 还款来源
     */
    private String epaymentOrigin;
    
    /**
     * 借款人家庭住址
     */
    private String address;
    
    /**
     * 借款人单位
     */
    private String workCompany;
    
    /**
     * 借款人借款利息
     */
    private String interest;
    
    /**
     * 借款用途
     */
    private String borrowUse;
    
    /**
     * 借款人朋友1
     */
    private String friendOne;
    
    /**
     * 借款人朋友2
     */
    private String friendTwo;
    
    /**
     * 借款人朋友3
     */
    private String friendThree;
    
    /**
     * 借款人朋友4
     */
    private String friendFour;
    
    /**
     * 借款人朋友5
     */
    private String friendFive;
    
    /**
     * 风控人员1
     */
    private String riskControlOne;
    
    /**
     * 风控人员2
     */
    private String riskControlTwo;
    
    /**
     * 放款人姓名
     */
    private String releaseMoneyName;
    
    /**
     * 放款人手机
     */
    private String releaseMoneyPhone;

    // Constructors

    /** default constructor */
    public CreditorInfo() {
    }

    /**
     * minimal constructor
     * @param creditor  creditor
     */
    public CreditorInfo(Creditor creditor) {
        this.creditor = creditor;
    }

    /**
     * full constructor
     * @param creditor  creditor
     * @param name  name
     * @param sex   sex
     * @param idCard    idCard
     * @param phone phone
     * @param money money
     * @param timeStart timeStart
     * @param timeEnd   timeEnd
     * @param peopleCommonName  peopleCommonName
     * @param peopleCommonIdCard    peopleCommonIdCard
     * @param repaymentType repaymentType
     * @param epaymentOrigin    epaymentOrigin
     * @param address   address
     * @param workCompany   workCompany
     * @param interest  interest
     * @param borrowUse borrowUse
     * @param friendOne friendOne
     * @param friendTwo friendTwo
     * @param friendThree   friendThree
     * @param friendFour    friendFour
     * @param friendFive    friendFive
     * @param riskControlOne    riskControlOne
     * @param riskControlTwo    riskControlTwo
     * @param releaseMoneyName  releaseMoneyName
     * @param releaseMoneyPhone releaseMoneyPhone
     */
    public CreditorInfo(Creditor creditor, String name, String sex,
            String idCard, String phone, String money, String timeStart,
            String timeEnd, String peopleCommonName, String peopleCommonIdCard,
            String repaymentType, String epaymentOrigin, String address,
            String workCompany, String interest, String borrowUse,
            String friendOne, String friendTwo, String friendThree,
            String friendFour, String friendFive, String riskControlOne,
            String riskControlTwo, String releaseMoneyName,
            String releaseMoneyPhone) {
        this.creditor = creditor;
        this.name = name;
        this.sex = sex;
        this.idCard = idCard;
        this.phone = phone;
        this.money = money;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.peopleCommonName = peopleCommonName;
        this.peopleCommonIdCard = peopleCommonIdCard;
        this.repaymentType = repaymentType;
        this.epaymentOrigin = epaymentOrigin;
        this.address = address;
        this.workCompany = workCompany;
        this.interest = interest;
        this.borrowUse = borrowUse;
        this.friendOne = friendOne;
        this.friendTwo = friendTwo;
        this.friendThree = friendThree;
        this.friendFour = friendFour;
        this.friendFive = friendFive;
        this.riskControlOne = riskControlOne;
        this.riskControlTwo = riskControlTwo;
        this.releaseMoneyName = releaseMoneyName;
        this.releaseMoneyPhone = releaseMoneyPhone;
    }

    /**
     * Property accessors
     * @return  id
     */
    @Id
    @GenericGenerator(name = "creditor_id", strategy = "foreign", parameters = { @Parameter(name = "property", value = "creditor") })
    @GeneratedValue(generator = "creditor_id")
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
     * id
     * @return  id
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "creditorInfo")
    @JoinColumn(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    public Creditor getCreditor() {
        return this.creditor;
    }

    /**
     * creditor
     * @param creditor  creditor
     */
    public void setCreditor(Creditor creditor) {
        this.creditor = creditor;
    }

    /**
     * name
     * @return  name
     */
    @Column(name = "name", length = 20)
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
     * sex
     * @return  sex
     */
    @Column(name = "sex")
    public String getSex() {
        return this.sex;
    }

    /**
     * sex
     * @param sex   sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * idCard
     * @return  idCard
     */
    @Column(name = "id_card", length = 20)
    public String getIdCard() {
        return this.idCard;
    }

    /**
     * idCard
     * @param idCard    idCard
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * phone
     * @return  phone
     */
    @Column(name = "phone", length = 15)
    public String getPhone() {
        return this.phone;
    }

    /**
     * phone
     * @param phone phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * money
     * @return  money
     */
    @Column(name = "money", precision = 20, scale = 4)
    public String getMoney() {
        return this.money;
    }

    /**
     * money
     * @param money money
     */
    public void setMoney(String money) {
        this.money = money;
    }

    /**
     * timeStart
     * @return  timeStart
     */
    @Column(name = "time_start", length = 20)
    public String getTimeStart() {
        return this.timeStart;
    }

    /**
     * timeStart
     * @param timeStart timeStart
     */
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * timeEnd
     * @return  timeEnd
     */
    @Column(name = "time_end", length = 20)
    public String getTimeEnd() {
        return this.timeEnd;
    }

    /**
     * timeEnd
     * @param timeEnd   timeEnd
     */
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * peopleCommonName
     * @return  peopleCommonName
     */
    @Column(name = "people_common_name", length = 50)
    public String getPeopleCommonName() {
        return this.peopleCommonName;
    }

    /**
     * peopleCommonName
     * @param peopleCommonName  peopleCommonName
     */
    public void setPeopleCommonName(String peopleCommonName) {
        this.peopleCommonName = peopleCommonName;
    }

    /**
     * peopleCommonIdCard
     * @return  peopleCommonIdCard
     */
    @Column(name = "people_common_id_card", length = 20)
    public String getPeopleCommonIdCard() {
        return this.peopleCommonIdCard;
    }

    /**
     * peopleCommonIdCard
     * @param peopleCommonIdCard    peopleCommonIdCard
     */
    public void setPeopleCommonIdCard(String peopleCommonIdCard) {
        this.peopleCommonIdCard = peopleCommonIdCard;
    }

    /**
     * repaymentType    
     * @return  repaymentType
     */
    @Column(name = "repayment_type", length = 50)
    public String getRepaymentType() {
        return this.repaymentType;
    }

    /**
     * repaymentType
     * @param repaymentType repaymentType
     */
    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    /**
     * epaymentOrigin
     * @return  epaymentOrigin
     */
    @Column(name = "epayment_origin", length = 50)
    public String getEpaymentOrigin() {
        return this.epaymentOrigin;
    }

    /**
     * epaymentOrigin
     * @param epaymentOrigin    epaymentOrigin
     */
    public void setEpaymentOrigin(String epaymentOrigin) {
        this.epaymentOrigin = epaymentOrigin;
    }

    /**
     * address
     * @return  address
     */
    @Column(name = "address", length = 100)
    public String getAddress() {
        return this.address;
    }

    /**
     * address
     * @param address   address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * workCompany
     * @return  workCompany
     */
    @Column(name = "work_company", length = 50)
    public String getWorkCompany() {
        return this.workCompany;
    }

    
    /**
     * workCompany
     * @param workCompany   workCompany
     */
    public void setWorkCompany(String workCompany) {
        this.workCompany = workCompany;
    }

    /**
     * interest
     * @return  interest
     */
    @Column(name = "interest", precision = 20, scale = 0)
    public String getInterest() {
        return this.interest;
    }

    /**
     * interest
     * @param interest  interest
     */
    public void setInterest(String interest) {
        this.interest = interest;
    }

    /**
     * borrowUse
     * @return  borrowUse
     */
    @Column(name = "borrow_use", length = 200)
    public String getBorrowUse() {
        return this.borrowUse;
    }

    /**
     * borrowUse
     * @param borrowUse borrowUse
     */
    public void setBorrowUse(String borrowUse) {
        this.borrowUse = borrowUse;
    }

    /**
     * friendOne
     * @return  friendOne
     */
    @Column(name = "friend_one", length = 20)
    public String getFriendOne() {
        return this.friendOne;
    }

    /**
     * friendOne
     * @param friendOne friendOne
     */
    public void setFriendOne(String friendOne) {
        this.friendOne = friendOne;
    }

    /**
     * friendTwo
     * @return  friendTwo
     */
    @Column(name = "friend_two", length = 20)
    public String getFriendTwo() {
        return this.friendTwo;
    }

    /**
     * friendTwo
     * @param friendTwo friendTwo
     */
    public void setFriendTwo(String friendTwo) {
        this.friendTwo = friendTwo;
    }

    /**
     * friend_three
     * @return  friend_three
     */
    @Column(name = "friend_three", length = 20)
    public String getFriendThree() {
        return this.friendThree;
    }

    /**
     * friendThree
     * @param friendThree   friendThree
     */
    public void setFriendThree(String friendThree) {
        this.friendThree = friendThree;
    }

    /**
     * friendFour
     * @return  friendFour
     */
    @Column(name = "friend_four", length = 20)
    public String getFriendFour() {
        return this.friendFour;
    }

    /**
     * friendFour
     * @param friendFour    friendFour
     */
    public void setFriendFour(String friendFour) {
        this.friendFour = friendFour;
    }

    /**
     * friend_five
     * @return  friend_five
     */
    @Column(name = "friend_five", length = 20)
    public String getFriendFive() {
        return this.friendFive;
    }

    /**
     * friendFive
     * @param friendFive    friendFive
     */
    public void setFriendFive(String friendFive) {
        this.friendFive = friendFive;
    }

    /**
     * riskControlOne
     * @return  riskControlOne
     */
    @Column(name = "risk_control_one", length = 20)
    public String getRiskControlOne() {
        return this.riskControlOne;
    }

    /**
     * riskControlOne
     * @param riskControlOne    riskControlOne
     */
    public void setRiskControlOne(String riskControlOne) {
        this.riskControlOne = riskControlOne;
    }

    /**
     * riskControlTwo
     * @return  riskControlTwo
     */
    @Column(name = "risk_control_two", length = 20)
    public String getRiskControlTwo() {
        return this.riskControlTwo;
    }

    /**
     * riskControlTwo
     * @param riskControlTwo    riskControlTwo
     */
    public void setRiskControlTwo(String riskControlTwo) {
        this.riskControlTwo = riskControlTwo;
    }

    /**
     * releaseMoneyName
     * @return  releaseMoneyName
     */
    @Column(name = "release_money_name", length = 20)
    public String getReleaseMoneyName() {
        return this.releaseMoneyName;
    }

    /**
     * releaseMoneyName
     * @param releaseMoneyName  releaseMoneyName
     */
    public void setReleaseMoneyName(String releaseMoneyName) {
        this.releaseMoneyName = releaseMoneyName;
    }

    /**
     * releaseMoneyPhone
     * @return  releaseMoneyPhone
     */
    @Column(name = "release_money_phone", length = 20)
    public String getReleaseMoneyPhone() {
        return this.releaseMoneyPhone;
    }

    /**
     * releaseMoneyPhone
     * @param releaseMoneyPhone releaseMoneyPhone
     */
    public void setReleaseMoneyPhone(String releaseMoneyPhone) {
        this.releaseMoneyPhone = releaseMoneyPhone;
    }

}