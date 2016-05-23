package com.idp.pub.context;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public final class AppContext implements ApplicationContextAware {
	/**
	 * 
	 */
	private String contextPath;
	

	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * 刮奖上限
	 */
	private int upMoney;
	
	public int getUpMoney() {
		return upMoney;
	}
	public void setUpMoney(int upMoney) {
		this.upMoney = upMoney;
	}

	/**
	 * 用户级别
	 */
	private Map<Integer, Integer> levelMap;
	public Map<Integer, Integer> getLevelMap() {
		return levelMap;
	}
	public void setLevelMap(Map<Integer, Integer> levelMap) {
		this.levelMap = levelMap;
	}

	/**
	 * 支付IP
	 */
	private String userIP;
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	/**
	 * 父级提成
	 */
	private double parentCommissionRate;
	/**
	 * 祖级提成
	 */
	private double grandparentCommissionRate;
	
	public double getParentCommissionRate() {
		return parentCommissionRate;
	}
	public void setParentCommissionRate(double parentCommissionRate) {
		this.parentCommissionRate = parentCommissionRate;
	}
	public double getGrandparentCommissionRate() {
		return grandparentCommissionRate;
	}
	public void setGrandparentCommissionRate(double grandparentCommissionRate) {
		this.grandparentCommissionRate = grandparentCommissionRate;
	}


	/**
	 * 自动发标总金额，单位元
	 */
	private int autoBidAmount;
	/**
	 * 自动发标认购期限，单位天
	 */
	private int autoBidPeriod;
	
	public int getAutoBidAmount() {
		return autoBidAmount;
	}
	public void setAutoBidAmount(int autoBidAmount) {
		this.autoBidAmount = autoBidAmount;
	}
	public int getAutoBidPeriod() {
		return autoBidPeriod;
	}
	public void setAutoBidPeriod(int autoBidPeriod) {
		this.autoBidPeriod = autoBidPeriod;
	}


	/**
	 * 红包
	 */
	private int redpacketAmount;

	/**
	 * 邀请人红包
	 */
	private int inviteRedpacketAmount;

	/**
	 * 红包有效期 天数
	 */
	private int redpacketPeriod;
	
	/**
	 * 理财替扣率
	 */
	private Double deductionRate;

	public int getRedpacketAmount() {
		return redpacketAmount;
	}
	public void setRedpacketAmount(int redpacketAmount) {
		this.redpacketAmount = redpacketAmount;
	}
	public int getInviteRedpacketAmount() {
		return inviteRedpacketAmount;
	}
	public void setInviteRedpacketAmount(int inviteRedpacketAmount) {
		this.inviteRedpacketAmount = inviteRedpacketAmount;
	}
	public int getRedpacketPeriod() {
		return redpacketPeriod;
	}
	public void setRedpacketPeriod(int redpacketPeriod) {
		this.redpacketPeriod = redpacketPeriod;
	}
	public Double getDeductionRate() {
		return deductionRate;
	}
	public void setDeductionRate(Double deductionRate) {
		this.deductionRate = deductionRate;
	}


	/**
	 * 提现立即
	 */
	private int payOnline;
	
	public int getPayOnline() {
		return payOnline;
	}
	public void setPayOnline(int payOnline) {
		this.payOnline = payOnline;
	}


	/**
	 * 投资默认状态: 收益中
	 */
	private int investmentStatus;
	/**
	 * 邮件来自
	 */
	private String mailFrom;

	/**
	 * abut
	 */
	private String about;
	/**
	 * 体验金
	 */
	private int experienceAmount;
	/**
	 * 体验金有效天数
	 */
	private int experiencePeriod;
	/**
	 * 默认一次提现不少于1000元
	 */
	private double withdrawAmount;

	public int getExperiencePeriod() {
		return experiencePeriod;
	}
	public void setExperiencePeriod(int experiencePeriod) {
		this.experiencePeriod = experiencePeriod;
	}
	public double getWithdrawAmount() {
		return withdrawAmount;
	}
	public void setWithdrawAmount(double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	public int getExperienceAmount() {
		return experienceAmount;
	}
	public void setExperienceAmount(int experienceAmount) {
		this.experienceAmount = experienceAmount;
	}
	public int getInvestmentStatus() {
		return investmentStatus;
	}
	public void setInvestmentStatus(int investmentStatus) {
		this.investmentStatus = investmentStatus;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}


	/**
	 * spring上下文 不包含controller
	 */
	private static ApplicationContext springApplicationContext;

	/**
	 * 是否调试模式
	 */
	private boolean debuging = false;

	/**
	 * 系统版本
	 */
	private String version = "1.0.0";

	/**
	 * 产品编号
	 */
	private String siteCode;

	private String fileBasePath;// 文件存放父路径

	/**
	 * 权限是否控制
	 */
	private boolean authecation = false;

	public boolean isDebuging() {
		return debuging;
	}

	public void setDebuging(boolean debuging) {
		this.debuging = debuging;
	}

	public boolean isAuthecation() {
		return authecation;
	}

	public void setAuthecation(boolean authecation) {
		this.authecation = authecation;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	/**
	 * 应用名称
	 */
	private String appName = "";

	/**
	 * 基础服务的Url
	 */
	private String baseUrl = "";

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * 数据库登录名
	 */
	private String userName;
	/**
	 * 数据库密码
	 */
	private String password;

	/**
	 * 数据库连接url
	 */
	private String dbUrl;

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("AppContext:[");
		str.append("appName:").append(appName).append(",\n");
		str.append("siteCode:").append(this.siteCode).append(",");
		str.append("version:").append(this.version).append(",");
		str.append("debuging:").append(this.debuging).append(",");
		str.append("authecation:").append(this.authecation).append(",");
		str.append("baseUrl:").append(this.baseUrl).append(",");
		str.append("fileBasePath:").append(this.fileBasePath).append(",");
		str.append("userName:").append(this.userName).append(",");
		str.append("password:").append(this.password).append(",");
		str.append("dbUrl:").append(this.dbUrl);
		str.append("]");
		return str.toString();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getFileBasePath() {
		return fileBasePath;
	}

	public void setFileBasePath(String fileBasePath) {
		this.fileBasePath = fileBasePath;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		springApplicationContext = applicationContext;
	}

	public static ApplicationContext getSpringApplicationContext() {
		return springApplicationContext;
	}
}