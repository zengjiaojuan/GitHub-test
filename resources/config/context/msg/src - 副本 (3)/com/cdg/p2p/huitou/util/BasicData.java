package com.cddgg.p2p.huitou.util;

/**
 * 
 * 投资记录基础数据查询实体
 * @author Administrator
 *
 */
public class BasicData {
    
      private long loansignId;      //标的id

	  private double rate;         //标的利率
	  
	  private double issueLoan;    //标的金额
	  
	  private Integer loanType;     //标的类型
	  
	  private String loanNumber;    //标号
	  
	  private String loanTitle;    //标号
	  
	  private double tenderMoney;   //购标金额
	  
	  private Integer isPrivilege;  //是否为会员
	  
	  private String tenderTime;    //购标时间
	  
	  private String preRepayDate;  //预计还款时间
	  
	  private Integer month;        //标的期数
	  
	  private double preRepayMoney;  //预计还款金额
	  
	  private String repayTime;      //还款时间
	  
	  private Integer periods;        //标的还款期
      
	  private double mfeeratio;      //管理费比例
 	  
	  private double mfeetop;		//管理费上限
 	  
	  private double pmfeeratio;		//特权用户管理费比例
 	  
	  private double pmfeetop;		//特权用户管理费上限

	  private String userName;		//借款者
	  
	  private Integer refundWay;    //还款方式
	  
	  private Integer reparyState;  //还款状态
	  
	  private Integer useDay;     //天标和秒标的借款天数
	  
	  private double money;    //本金
	  
	  private double realMoney; //实际还款利息
	  
	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getIssueLoan() {
		return issueLoan;
	}

	public void setIssueLoan(double issueLoan) {
		this.issueLoan = issueLoan;
	}

	public Integer getLoanType() {
		return loanType;
	}

	public void setLoanType(Integer loanType) {
		this.loanType = loanType;
	}

	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public double getTenderMoney() {
		return tenderMoney;
	}

	public void setTenderMoney(double tenderMoney) {
		this.tenderMoney = tenderMoney;
	}

	public Integer getIsPrivilege() {
		return isPrivilege;
	}

	public void setIsPrivilege(Integer isPrivilege) {
		this.isPrivilege = isPrivilege;
	}

	public String getTenderTime() {
		return tenderTime;
	}

	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}

	public String getPreRepayDate() {
		return preRepayDate;
	}

	public void setPreRepayDate(String preRepayDate) {
		this.preRepayDate = preRepayDate;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public double getPreRepayMoney() {
		return preRepayMoney;
	}

	public void setPreRepayMoney(double preRepayMoney) {
		this.preRepayMoney = preRepayMoney;
	}

	public String getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}

	public Integer getPeriods() {
		return periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public double getMfeeratio() {
		return mfeeratio;
	}

	public void setMfeeratio(double mfeeratio) {
		this.mfeeratio = mfeeratio;
	}

	public double getMfeetop() {
		return mfeetop;
	}

	public void setMfeetop(double mfeetop) {
		this.mfeetop = mfeetop;
	}

	public double getPmfeeratio() {
		return pmfeeratio;
	}

	public void setPmfeeratio(double pmfeeratio) {
		this.pmfeeratio = pmfeeratio;
	}

	public double getPmfeetop() {
		return pmfeetop;
	}

	public void setPmfeetop(double pmfeetop) {
		this.pmfeetop = pmfeetop;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getRefundWay() {
		return refundWay;
	}

	public void setRefundWay(Integer refundWay) {
		this.refundWay = refundWay;
	}

	public Integer getReparyState() {
		return reparyState;
	}

	public void setReparyState(Integer reparyState) {
		this.reparyState = reparyState;
	}

	public Integer getUseDay() {
		return useDay;
	}

	public void setUseDay(Integer useDay) {
		this.useDay = useDay;
	}

    public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

    public long getLoansignId() {
        return loansignId;
    }

    public void setLoansignId(long loansignId) {
        this.loansignId = loansignId;
    }
    
}

