package com.cddgg.p2p.pay.entity;
/**
 *环讯返回信息对象
 * @author RanQiBing 2014-01-07
 *
 */
public class ReturnInfo {
	
    /**“平台”账号*/
    private String pMerCode;
    /**处理状态*/
    private String pErrCode;
	/**处理信息*/
    private String pErrMsg;		
    /**3DES 报文体加密*/
    private String p3DesXmlPara;
    /**MD5WithRSA摘要*/
    private String pSign;
    /**
     * @return pMerCode
     */
    public String getpMerCode() {
        return pMerCode;
    }
    /**
     * @param pMerCode pMerCode
     */
    public void setpMerCode(String pMerCode) {
        this.pMerCode = pMerCode;
    }
    /**
     * @return pErrCode
     */
    public String getpErrCode() {
        return pErrCode;
    }
    /**
     * @param pErrCode pErrCode
     */
    public void setpErrCode(String pErrCode) {
        this.pErrCode = pErrCode;
    }
    /**
     * @return pErrMsg
     */
    public String getpErrMsg() {
        return pErrMsg;
    }
    /**
     * @param pErrMsg pErrMsg
     */
    public void setpErrMsg(String pErrMsg) {
        this.pErrMsg = pErrMsg;
    }
    /**
     * @return p3DesXmlPara
     */
    public String getP3DesXmlPara() {
        return p3DesXmlPara;
    }
    /**
     * @param p3DesXmlPara p3DesXmlPara
     */
    public void setP3DesXmlPara(String p3DesXmlPara) {
        this.p3DesXmlPara = p3DesXmlPara;
    }
    /**
     * @return pSign
     */
	public String getpSign() {
		return pSign;
	}
	/**
	 * pSign
	 * @param pSign pSign
	 */
	public void setpSign(String pSign) {
		this.pSign = pSign;
	}

}
