package com.cddgg.p2p.pay.util;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cddgg.p2p.pay.constant.Certificate;
import com.cddgg.p2p.pay.entity.RepaymentInfo;
import com.cddgg.p2p.pay.entity.RepaymentInvestorInfo;
import com.ips.security.utility.IpsCrypto;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
/**
 * 解析xml并转换成对象
 * @author RanQiBing 2014-01-08
 *
 */
public class XmlParsingBean {
	/**
	 * 将xml文件解析转换成一个对象
	 * @param xml	xml文件
	 * @param obj	对象名称
	 * @return	返回一个对象
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static Object simplexml1Object(String xml,Object obj) throws FileNotFoundException, UnsupportedEncodingException {
			//解析成xml文件
		   String xmlResolve = IpsCrypto.triDesDecrypt(xml, ParameterIps.getDes_algorithm(),ParameterIps.getDesedevector());
		   //去掉xml文件头
		   xmlResolve = xmlResolve.substring(Certificate.XMLTOP_INDEX);
		   XStream xStream = new XStream(new DomDriver());
		   //将 pReq替换成对象名称
		   xStream.alias("pReq", obj.getClass());
		   //将xml文件转换成一个对象
		   Object reobj = xStream.fromXML(xmlResolve,obj);
		   return reobj;
		}
	/**
	 * 将webserver返回回的xml文件解析转换成一个对象
	 * @param xml	xml文件
	 * @param obj	对象名称
	 * @return	返回一个对象
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static Object simplexml2Object(String xml,Object obj) throws FileNotFoundException, UnsupportedEncodingException {
			//解析成xml文件
		   //String xmlResolve = IpsCrypto.triDesDecrypt(xml, Certificate.DES_ALGORITHM05,Certificate.DESEDEVECTOR05);
		   //去掉xml文件头
		   xml = xml.substring(Certificate.XMLTOP_INDEXTWO);
		   XStream xStream = new XStream(new DomDriver());
		   //将 pReq替换成对象名称
		   xStream.alias("pReq", obj.getClass());
		   //将xml文件转换成一个对象
		   Object reobj = xStream.fromXML(xml,obj);
		   return reobj;
		}
	/**
     * 解析提现异步返回信息的处理
     * @param strXml 返回加密后的xml文件
     * @return
     */
    public static RepaymentInfo parseXml(String xml){  
  
        RepaymentInfo repaymentInfo = new RepaymentInfo();  
            try {  
             // 解析成xml文件
                String strXml = IpsCrypto.triDesDecrypt(xml,ParameterIps.getDes_algorithm(),ParameterIps.getDesedevector());
                strXml = strXml.substring(1);
                
                //读入文档流  
                Document document = DocumentHelper.parseText(strXml.toString()); // 将字符串转  
                //获取根节点  
                Element root = document.getRootElement();  
                //将根节点下的信息放入还款对象中
                repaymentInfo.setpBidNo(root.elementTextTrim("pBidNo"));
                repaymentInfo.setpContractNo(root.elementTextTrim("pContractNo"));
                repaymentInfo.setpRepaymentDate(root.elementTextTrim("pRepaymentDate"));
                repaymentInfo.setpMerBillNo(root.elementTextTrim("pMerBillNo"));
                repaymentInfo.setpIpsBillNo(root.elementTextTrim("pIpsBillNo"));
                repaymentInfo.setpMemo1(root.elementTextTrim("pMemo1"));
                repaymentInfo.setpMemo2(root.elementTextTrim("pMemo2"));
                repaymentInfo.setpMemo3(root.elementTextTrim("pMemo3"));
                //解析repaymentInvestorInfosList节点  
                List<RepaymentInvestorInfo> repaymentInvestorInfosList = new ArrayList<RepaymentInvestorInfo>();   
                Element pDetails = root.element("pDetails");
                //遍历pDetails节点下的投资人信息
                for(Iterator iterator = pDetails.elementIterator("pRow");iterator.hasNext();){  
                    Element eStudent = (Element) iterator.next();  
                    RepaymentInvestorInfo repaymentInvestorInfo = new RepaymentInvestorInfo(); 
                    repaymentInvestorInfo.setpTTrdFee(eStudent.elementTextTrim("pTAcctType"));
                    repaymentInvestorInfo.setpTIpsAcctNo(eStudent.elementTextTrim("pFIpsAcctNo"));
                    repaymentInvestorInfo.setpStatus(eStudent.elementTextTrim("pStatus"));
                    repaymentInvestorInfo.setpMessage(eStudent.elementTextTrim("pMessage"));
                    repaymentInvestorInfosList.add(repaymentInvestorInfo); 
                }  
                
                repaymentInfo.setRepaymentInvestorInfoList(repaymentInvestorInfosList);
            } catch (DocumentException e) {  
                e.printStackTrace();  
            }  
            return repaymentInfo;  
    }  
}
