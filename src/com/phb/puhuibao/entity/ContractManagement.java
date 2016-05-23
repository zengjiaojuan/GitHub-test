package com.phb.puhuibao.entity;


/**
 * @author wei
 *
 */
public class ContractManagement {
	

	private String contractId;//contract_id` char(20) NOT NULL COMMENT '同合编号',
	private Integer investType; //invest_type` varchar(30) DEFAULT '' COMMENT '投资类型',
	private Integer productType; //product_type` tinyint(1) DEFAULT NULL COMMENT '金额产品',
	private Double contractAmount; //contract_amount` decimal(10,2) DEFAULT '0.00' COMMENT '金额',
	private String  contractExpiredate   ; //contract_expiredate` date DEFAULT NULL COMMENT '合同结束日期',
	private String contractStartdate   ; //contract_expiredate` date DEFAULT NULL COMMENT '合同计息日期',
	private String  contractDate;//contract_date` date DEFAULT NULL COMMENT '划扣日期',
	private Integer obligatoryRight; //obligatory_right` tinyint(1) DEFAULT NULL COMMENT '债权接收方式',
	
	private String bankName; //bank_name` varchar(50) DEFAULT '' COMMENT '开户银行名',
	private String bankId; //bank_id` varchar(20) DEFAULT '' COMMENT '开户银行id',
	private String bankAccountid; //bank_accountid` varchar(30) DEFAULT '' COMMENT '银行账号',
	private String returnBankname; //return_bankname` varchar(50) DEFAULT '' COMMENT '回款银行名',
	private String returnBankid; //return_bankid` varchar(20) DEFAULT '' COMMENT '回款银行id',
	private String returnBankaccount; //return_bankaccount` varchar(30) DEFAULT '' COMMENT '回款银行账号',
	
 
	private String userName;//user_name` varchar(25) NOT NULL DEFAULT '' COMMENT '投资人姓名',
	private String userTel; //user_tel` varchar(50) DEFAULT '' COMMENT '投资人电话',
	private String userId; //user_id` varchar(20) DEFAULT '' COMMENT '投资人身份证',
	private String userMail; //user_mail` varchar(50) DEFAULT '' COMMENT '电子邮件',
	private String userAddress; //user_address` varchar(50) DEFAULT '' COMMENT '用户地址',
	private String useridExdate;// `userid_exdate` date DEFAULT NULL COMMENT '身份证到期日',
	private String userPost; //user_post` varchar(32) DEFAULT '' COMMENT '用户邮编',
	private String useridDep;//`userid_dep`
	private String emergencyContact;//`emergency_contact`
	private String emergencyContactid;//`emergency_contactid`
	private String emergencyContacttel;//`emergency_contacttel`
	private Integer userGender; //user_gender` tinyint(1) DEFAULT NULL COMMENT '投资人性别',
	
	private String userManager; //user_manager` varchar(20) DEFAULT '' COMMENT '客户的理财经理',
	private String userTeammanager; //user_teammanager` char(32) DEFAULT '' COMMENT '团队经理',
	private Integer managerDep; //manager_dep` tinyint(1) DEFAULT NULL COMMENT '团队部门',
	
	private Integer isDeleted;
	private Integer contractIsarchived;//contract_isarchived` tinyint(1) DEFAULT '0' COMMENT '是否归档',
	
	

	

	
	
	public String getReturnBankname() {
		return returnBankname;
	}
	public void setReturnBankname(String returnBankname) {
		this.returnBankname = returnBankname;
	}
	public String getReturnBankid() {
		return returnBankid;
	}
	public void setReturnBankid(String returnBankid) {
		this.returnBankid = returnBankid;
	}
	public String getReturnBankaccount() {
		return returnBankaccount;
	}
	public void setReturnBankaccount(String returnBankaccount) {
		this.returnBankaccount = returnBankaccount;
	}
	public String getContractStartdate() {
		return contractStartdate;
	}
	public void setContractStartdate(String contractStartdate) {
		this.contractStartdate = contractStartdate;
	}
	public String getUseridExdate() {
		return useridExdate;
	}
	public void setUseridExdate(String useridExdate) {
		this.useridExdate = useridExdate;
	}
	public String getUseridDep() {
		return useridDep;
	}
	public void setUseridDep(String useridDep) {
		this.useridDep = useridDep;
	}
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	public String getEmergencyContactid() {
		return emergencyContactid;
	}
	public void setEmergencyContactid(String emergencyContactid) {
		this.emergencyContactid = emergencyContactid;
	}
	public String getEmergencyContacttel() {
		return emergencyContacttel;
	}
	public void setEmergencyContacttel(String emergencyContacttel) {
		this.emergencyContacttel = emergencyContacttel;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	 
	public Integer getContractIsarchived() {
		return contractIsarchived;
	}
	public void setContractIsarchived(Integer contractIsarchived) {
		this.contractIsarchived = contractIsarchived;
	}
	 
	public Integer getInvestType() {
		return investType;
	}
	public void setInvestType(Integer investType) {
		this.investType = investType;
	}
 
	public String getContractExpiredate() {
		return contractExpiredate;
	}
	public void setContractExpiredate(String contractExpiredate) {
		this.contractExpiredate = contractExpiredate;
	}
	public String getContractDate() {
		return contractDate;
	}
	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getUserGender() {
		return userGender;
	}
	public void setUserGender(Integer userGender) {
		this.userGender = userGender;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	public Double getContractAmount() {
		return contractAmount;
	}
	public void setContractAmount(Double contractAmount) {
		this.contractAmount = contractAmount;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankAccountid() {
		return bankAccountid;
	}
	public void setBankAccountid(String bankAccountid) {
		this.bankAccountid = bankAccountid;
	}
	public Integer getObligatoryRight() {
		return obligatoryRight;
	}
	public void setObligatoryRight(Integer obligatoryRight) {
		this.obligatoryRight = obligatoryRight;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getUserPost() {
		return userPost;
	}
	public void setUserPost(String userPost) {
		this.userPost = userPost;
	}
	public String getUserManager() {
		return userManager;
	}
	public void setUserManager(String userManager) {
		this.userManager = userManager;
	}
	public String getUserTeammanager() {
		return userTeammanager;
	}
	public void setUserTeammanager(String userTeammanager) {
		this.userTeammanager = userTeammanager;
	}
	public Integer getManagerDep() {
		return managerDep;
	}
	public void setManagerDep(Integer managerDep) {
		this.managerDep = managerDep;
	}
	
	
 
	
	 
	
	
}
