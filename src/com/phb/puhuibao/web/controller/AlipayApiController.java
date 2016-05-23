package com.phb.puhuibao.web.controller;

 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.common.alipayapi.AlipayConfig;
import com.phb.puhuibao.common.alipayapi.AlipayNotify;
import com.phb.puhuibao.common.alipayapi.AlipaySubmit;
import com.phb.puhuibao.entity.AlipayApi;
import com.phb.puhuibao.entity.ResourceOrder;

@Controller
@RequestMapping(value = "/alipayApi")
public class AlipayApiController extends BaseController<AlipayApi, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(AlipayApiController.class);
	
	@Resource(name = "alipayApiService")
	public void setBaseService(IBaseService<AlipayApi, String> baseService) {
		super.setBaseService(baseService);
	}
 
	@javax.annotation.Resource(name = "resourceService")
	private IBaseService<com.phb.puhuibao.entity.Resource, String> resourceService;
	
	@javax.annotation.Resource(name = "resourceOrderService")
	private IBaseService<ResourceOrder, String> resourceOrderService;

 
	
	
	/**
                    支付宝付款
	 */

	@RequestMapping(value="alipay")
	@ResponseBody
	public Map<String, Object> alipay(int muId,int resourceId){ 
		Map<String, Object> data = new HashMap<String, Object>();
//    	PrintWriter out = response.getWriter();
        Date date = new Date();  
        // 支付类型  
        // 必填，不能修改  
        String payment_type = "1";  
        // 服务器异步通知页面路径  
        // 需http://格式的完整路径，不能加?id=123这类自定义参数  
        String notify_url = "http://182.92.179.84:81/puhuibao/alipayApi/async.shtml";   
       
        // 商户订单号.  
        // 商户网站订单系统中唯一订单号，必填  
        //String out_trade_no = date.getTime() + "";  
        // 订单名称  
        // 必填  
        String subject = "付款";  
        // 防钓鱼时间戳  
        // 若要使用请调用类文件submit中的query_timestamp函数  
        String anti_phishing_key = "";  
        // 客户端的IP地址  
        // 非局域网的外网IP地址，如：221.0.0.1  
        String exter_invoke_ip = "";  
 
       
        double price=0;
        com.phb.puhuibao.entity.Resource resourc = resourceService.getById(resourceId+"");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String result = dateFormat.format(new Date());
        String orderid="";
        if(resourc.getStatus()==0){//刚创建的任务,此次调用服务是为了给服务付款
        	price = resourc.getPrice()*resourc.getNumber();
            orderid = "PHB-"+result+"-"+"R-"+resourc.getResourceId();
        }else if(resourc.getStatus()==1){//此次调用服务是为了给报名付款,由于订单不存在,所以需要先创建订单
        	if(resourc.getmUserId()==muId){//重复付款,这个逻辑是  肯定是任务的创建者给任务付款,所以第一次付款后,状态为1
				data.put("message", "你已经付款了！");
				data.put("status", 1);
				return data;
        	}
			Map<String,Object> par = new HashMap<String,Object>();
			par.put("resourceId", resourceId);
			par.put("mUserId", muId);
			ResourceOrder resourceOrder = resourceOrderService.unique(par);
			if(resourceOrder!=null ){// 存在上次付款失败的情况
				if(resourceOrder.getStatus()!=0){
					data.put("message", "你已经付款了！");
					data.put("status", 1);
					return data;
				}
			}else{//创建新订单
				ResourceOrder order = new ResourceOrder();
				order.setmUserId(muId);
				order.setResourceId(resourceId);
				order.setStatus(0);//创建的时候为0    确认付款改为1
				order.setPrice(resourc.getPrice()); 
				resourceOrderService.save(order);
				
			}
        	Map<String,Object> params = new HashMap<String,Object>();
    		params.put("resourceId", resourceId);
    		params.put("mUserId", muId);
    		params.put("status", 0);
    		ResourceOrder ro = resourceOrderService.unique(params);//为了获得订单号
    		if(ro==null){
    			data.put("message", "出错了！");
				data.put("status", 1);
				return data;
    		}
    		price = resourc.getPrice();
    		orderid = "PHB-"+result+"-"+"O-"+ro.getOrderId();
        }
        


        String total_fee = price+"";
        String body = "test";
 

		
		
		
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);//接口服务----移动支付
		sParaTemp.put("partner", AlipayConfig.partner);//支付宝PID
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);//统一编码
		sParaTemp.put("payment_type", payment_type);//支付类型
		sParaTemp.put("notify_url", notify_url);//异步通知页面
		sParaTemp.put("seller_email", AlipayConfig.SELLER_EMAIL);//卖家支付宝账号
		sParaTemp.put("out_trade_no", orderid);//商品订单编号
		sParaTemp.put("subject", subject);//商品名称
		sParaTemp.put("total_fee", total_fee);//价格
		sParaTemp.put("body", body);
 
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
         
		 //Map<String, String> sPara = AlipaySubmit.buildRequestPara(sParaTemp);
		 
		sParaTemp.put("sign", AlipayConfig.private_key);
		sParaTemp.put("sign_type", AlipayConfig.sign_type);
		 
		sParaTemp.put("orderDesc","付款"+ resourc.getName());
		
//		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");  
//		System.out.println("sHtmlText:"+sHtmlText);
//		StringUti.writeToWeb(sHtmlText, "html", response);
//		return null;
		
		
		data.put("result", sParaTemp);
		data.put("message", "");
		data.put("status", 1);
		return data;
		
		
    }
	 
	/**
	   支付宝执行的
	* @param  
	*/
	@RequestMapping(value="async")
	@ResponseBody
	public String async(HttpServletRequest request,
			HttpServletResponse response) {
		 Map<String,String> params = new HashMap<String,String>();  
	        Map requestParams = request.getParameterMap();  
	        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {  
	            String name = (String) iter.next();  
	            String[] values = (String[]) requestParams.get(name);  
	            String valueStr = "";  
	            for (int i = 0; i < values.length; i++) {  
	                valueStr = (i == values.length - 1) ? valueStr + values[i]: valueStr + values[i] + ",";  
	            }  
	            params.put(name, valueStr);  
	        }  
	        String tradeNo = request.getParameter("out_trade_no");  
	        String tradeStatus = request.getParameter("trade_status");  
	        //String notifyId = request.getParameter("notify_id");  
	        if(AlipayNotify.verify(params)){//验证成功  
	            if(tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {  //修改任务或者订单状态
	            	 String [] strarray = tradeNo.split("-");
	            	 if(strarray[2].equals("R")){//任务
	            		 com.phb.puhuibao.entity.Resource resourc = resourceService.getById(strarray[3]);
	            			if (resourc.getStatus() ==3 || resourc.getStatus() ==2|| resourc.getStatus() ==1) {
	            				return "fail";
	            			}
 
	            		 com.phb.puhuibao.entity.Resource entity = new  com.phb.puhuibao.entity.Resource();
	            		 if(resourc.getStatus()==0){
	            			 entity.setResourceId(resourc.getResourceId());
	            			 entity.setStatus(1);
	            			 resourceService.update(entity);
	            		 }
	            	 }else if(strarray[2].equals("O")){//订单
	    				ResourceOrder resourceOrder = resourceOrderService.getById(strarray[3]);//第三个数是订单id
						if(resourceOrder.getStatus()!=0){
							return "fail";
						}
	        			ResourceOrder entity =  new ResourceOrder();
	        			entity.setOrderId(resourceOrder.getOrderId());
	        			entity.setStatus(1);
	        			resourceOrderService.update(entity);
	            	 }
	            }  
	            return "success";  
	        }else{//验证失败  
	            return "fail";  
	        }  
		
	}
	
	
	
	 
 
 
}
