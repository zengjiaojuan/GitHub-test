package com.phb.puhuibao.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idap.clinic.entity.UploadFile;
import com.idp.pub.context.AppContext;
import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.opensymphony.oscache.util.StringUtil;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.ProductBid;

@Controller
@RequestMapping(value = "/productBid")
public class ProductBidController extends BaseController<ProductBid, String> {
	private static final Log LOG = LogFactory.getLog(ProductBidController.class);

	@Resource(name = "appContext")
	private AppContext appContext;

	@Override
	@Resource(name = "productBidService")
	public void setBaseService(IBaseService<ProductBid, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "assetProductService")
	private IBaseService<AssetProduct, String> assetProductService;
	
	@Resource(name = "uploadFileService")
	private IBaseService<UploadFile, String> uploadFileService;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 翻页
	 * @param pageno
	 * @param type
	 * @param muid 用户id,为了加载用户的提醒信息
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String type,@RequestParam String muid) {
		Map<String, Object> params = new HashMap<String, Object>();
		if ("".equals(type)) {
		} else if ("1".equals(type)) { // 普惠宝所有
			params = new HashMap<String, Object>();
			params.put("getype", type);
		} else {
			params.put("type", type);
		}
		params.put("status", 1);
		List<AssetProduct> products = assetProductService.findList(params);
		
		params = new HashMap<String, Object>();
		params.put("lstatus", 2);
		List<ProductBid> bids = this.getBaseService().findList(params);
		List<String> productSNs = new ArrayList<String>();
		for (ProductBid bid : bids) {
			productSNs.add(bid.getProductSN());
		}

		Calendar cal = Calendar.getInstance();
	 
		/*
		 * 如果一个产品在产品定义表里有,但是在产品的标的表里没有,就会自动制标  现在注释掉,所有的标必须手动
		 */
		for (AssetProduct product : products) { // 自动制标
			product.setEstimateIncome(Functions.estimateIncome(product, jdbcTemplate));
//			if (!productSNs.contains(product.getProductSN())) {
//				String bidSN = product.getProductSN() + date;
//				params = new HashMap<String, Object>();
//				params.put("bidSN", bidSN);
//				ProductBid bid = this.getBaseService().unique(params);
//				int i = 1;
//				String sn = bidSN;
//				while (bid != null) {
//					sn = bidSN + "-" + i;
//					params = new HashMap<String, Object>();
//					params.put("bidSN", sn);
//					bid = this.getBaseService().unique(params);
//					i ++;
//				}
//				bidSN =  sn;
//				
//				bid = new ProductBid();
//				bid.setProductSN(product.getProductSN());
//				bid.setBidSN(bidSN);
//				bid.setStartDate(startDate);
//				cal.add(Calendar.DATE, appContext.getAutoBidPeriod());
//				bid.setEndDate(cal.getTime());// 没有作用
//				bid.setTotalAmount(product.getTotalAmount());
//				this.getBaseService().save(bid);
//			}
		}
		
		Pager<ProductBid> pager = new Pager<ProductBid>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(10);
		params = new HashMap<String, Object>();
		if ("".equals(type)) {
		} else if ("1".equals(type)) { // 普惠宝所有
			params = new HashMap<String, Object>();
			params.put("getype", type);
		} else {
			params.put("type", type);
		}
		params.put("lstatus", 9);
		params.put("orderBy", "a.status , bid_important desc");
		params.put("muid", muid);
		//params.put("order", "desc");
		Pager<ProductBid> p = this.getBaseService().findByPager(pager, params);
		List<ProductBid> result = p.getData();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ProductBid bid : result) {
			params = new HashMap<String, Object>();
			params.put("productSN", bid.getProductSN());
			AssetProduct product = assetProductService.unique(params);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("bidId", bid.getBidId());
			map.put("bidSN", bid.getBidSN());
			
			if(StringUtil.isEmpty(bid.getBidName())){
				map.put("productName",product.getProductName());
			}else{
				map.put("productName",bid.getBidName() );
			}
			
			map.put("guaranteeMethod", product.getGuaranteeMethod());
			map.put("investmentAmountMin", product.getInvestmentAmountMin());
			//map.put("investmentAmountMultiple", product.getInvestmentAmountMultiple());
			map.put("investmentAmountUpper", product.getInvestmentAmountUpper());
			map.put("estimateIncome", Functions.estimateIncome(product, jdbcTemplate));
			map.put("peroid", product.getPeriod());
			map.put("unit", product.getUnit());
			map.put("help", product.getHelp());
			map.put("productDesc", product.getProductDesc());
			map.put("annualizedRate", product.getAnnualizedRate());
			if (bid.getStatus() == 3) {
				map.put("currentAmount", bid.getTotalAmount());
			} else {
				map.put("currentAmount", bid.getCurrentAmount());
			}
			map.put("totalAmount", bid.getTotalAmount());
			if (bid.getStatus() == 0 ) {
				bid.setStatus(1);
				this.getBaseService().update(bid);
//			} else if (bid.getStatus() == 1 && new Date().getTime() > bid.getEndDate().getTime()) {
//				bid.setStatus(2);
//				this.getBaseService().update(bid);
			}
			map.put("status", bid.getStatus());
			list.add(map);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", list);
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
	/**
	 * 推荐
	 * @param muid 用户id 
	 * @return
	 */
	@RequestMapping(value="recommendation")
	@ResponseBody
	public Map<String, Object> recommendation( @RequestParam String muid) {
		Map<String,Object> retobj = null;
		Map<String, Object> data = new HashMap<String, Object>();
		List <Map<String, Object>> list = null;
		List <Map<String, Object>> list1 = null;
		String sql="";
		/*if(StringUtil.isEmpty(muid)){// 用户未登陆
			    //sql查出的是新手标
				sql = "SELECT b.bid_id, b.bid_sn,b.status, a.product_name, a.annualized_rate, a.period, a.unit,CASE WHEN b.status = 3 THEN  b.total_amount ELSE b.current_amount END as currentAmount,b.total_amount totalAmount FROM phb_product_bid b LEFT JOIN phb_asset_product a ON b.product_sn = a.product_sn WHERE b. STATUS = 1 AND a.product_sn = 'P888'";
				list = this.jdbcTemplate.queryForList(sql);
				if (list.isEmpty()) {// 没有可以投资的新手标
					data.put("result", retobj);
					data.put("status", 1);
				}else{
					retobj = list.get(0);
					data.put("result", retobj);
					data.put("status", 1);
				}			  
		} else{//用户登录
			//通过muid 查一下这个用户是否为新用户(没有购买过理财产品的)
			sql="SELECT u.bid_sn from  phb_muser_investment u WHERE u.m_user_id = " + muid;
			list1 = this.jdbcTemplate.queryForList(sql);//通过muid查询投资表得到结果list
			if(list1.isEmpty()){//新的用户(没有购买过理财产品的)
				//sql查出的是新手标
				sql = "SELECT b.bid_id, b.bid_sn,b.status, a.product_name, a.annualized_rate, a.period, a.unit,CASE WHEN b.status = 3 THEN  b.total_amount ELSE b.current_amount END as currentAmount,b.total_amount totalAmount FROM phb_product_bid b LEFT JOIN phb_asset_product a ON b.product_sn = a.product_sn WHERE b. STATUS = 1 AND a.product_sn = 'P888'";
					  
			}else{//老用户
				//sql查出所有的标
				sql = " SELECT b.bid_id, b.bid_sn,b.status, a.product_name, a.annualized_rate, a.period, a.unit  ,CASE WHEN b.status = 3 THEN  b.total_amount ELSE b.current_amount END as currentAmount,b.total_amount totalAmount  FROM phb_product_bid b LEFT JOIN phb_asset_product a ON b.product_sn = a.product_sn where b. STATUS = 1 order by a.important desc";
				
			}
			list = this.jdbcTemplate.queryForList(sql);
			if(list.isEmpty()){//没有可以推荐的
				data.put("result", retobj);
				data.put("status", 1);
				
			}else{
				retobj=list.get(0);
				data.put("result", retobj);
				data.put("status", 1);
			}
			*/
			
		
		
			sql = " SELECT b.bid_id, b.bid_sn,b.status, a.product_name, a.annualized_rate, a.period, a.unit  ,CASE WHEN b.status = 3 THEN  b.total_amount ELSE b.current_amount END as currentAmount,b.total_amount totalAmount  FROM phb_product_bid b LEFT JOIN phb_asset_product a ON b.product_sn = a.product_sn where b. STATUS = 1 order by a.important desc";
			
		
		list = this.jdbcTemplate.queryForList(sql);
		if(list.isEmpty()){//没有可以推荐的
			data.put("result", retobj);
			data.put("status", 0);
			
		}else{
			retobj=list.get(0);
			data.put("result", retobj);
			data.put("status", 1);
		}
		
//			sql = " SELECT b.bid_id, b.bid_sn, a.product_name, a.annualized_rate, a.period, a.unit  ,CASE WHEN b.status = 3 THEN  b.total_amount ELSE b.current_amount END as currentAmount,b.total_amount totalAmount  FROM phb_product_bid b LEFT JOIN phb_asset_product a ON b.product_sn = a.product_sn where b. STATUS = 1 order by a.important desc";
//			list = this.jdbcTemplate.queryForList(sql);
//			if (list.isEmpty()) {// 没有可以投资的了
//				data.put("result", retobj);
//				data.put("status", 1);
//			}else{
//				retobj = list.get(0);
//				data.put("result", retobj);
//				data.put("status", 1);
//			}
			

		return data;
 
	}
	
	
	
	
	/**
	 * 项目介绍
	 * @param bidsn 产品编号
	 * @return
	 */
	@RequestMapping(value="borrowDetails")
	//@RequestMapping(params = "method=borrowDetails", method = RequestMethod.GET)
	@ResponseBody
	public void borrowDetails( @RequestParam String bidsn, HttpServletRequest request, HttpServletResponse response) {
		
//		Map<String, Object> data = new HashMap<String, Object>();
		List <Map<String, Object>> list = null;
		String sql="";
		if(!StringUtil.isEmpty(bidsn)){    //当bidsn标的编号不为空时 通过sql查询债权信息			     
				sql = "SELECT b.bid_id,a.period,a.unit,a.annualized_rate, a.payment_method, a.product_desc, c.borrower_name,b.contract_pic, case left(c.borrower_id,2)  when '11' then '北京市' when '12' then '天津市' when '13' then '河北省' when '14' then '山西省' when '15' then '内蒙古自治区' when '21' then '辽宁省' when '22' then '吉林省' when '23' then '黑龙江省' when '31' then '上海市' when '32' then '江苏省' when '33' then '浙江省' when '34' then '安徽省' when '35' then '福建省' when '36' then '江西省' when '37' then '山东省' when '41' then '河南省' when '42' then '湖北省' when '43' then '湖南省' when '44' then '广东省' when '45' then '广西壮族自治区' when '46' then '海南省' when '50' then '重庆市' when '51' then '四川省' when '52' then '贵州省' when '53' then '云南省' when '54' then '西藏自治区' when '61' then '陕西省' when '62' then '甘肃省' when '63' then '青海省' when '64' then '宁夏回族自治区' when '65' then '新疆维吾尔自治区' when '71' then '台湾省' when '81' then '香港特别行政区' when '82' then '澳门特别行政区' else '未知'      end   as borrower_province , year(curdate())-if(length(c.borrower_id)=18,substring(c.borrower_id,7,4),if(length(c.borrower_id)=15,concat('19',substring(c.borrower_id,7,2)),null)) as borrower_age,  case if(length(c.borrower_id)=18, cast(substring(c.borrower_id,17,1) as UNSIGNED)%2, if(length(c.borrower_id)=15,cast(substring(c.borrower_id,15,1) as UNSIGNED)%2,3))  when 1 then '男'	 when 0 then '女' else '未知' end as borrower_gender, case c.borrower_job when 1 then '私营业主' when 2 then '工薪' when 3 then '企业高管' end as borrower_job, case c.money_useage when 1 then '扩大经营' when 2 then '资金周转' when 3 then '个人消费' end as money_useage, case c.warrant_status when 1 then '质押担保' end as warrant_status  FROM phb_product_bid b LEFT JOIN phb_asset_product a ON b.product_sn = a.product_sn LEFT JOIN crm_borrower c ON b.bid_contract = c.borrower_contract where b.bid_sn =  '"+ bidsn +"'";
				list = this.jdbcTemplate.queryForList(sql);
				//[{period=12, annualized_rate=0.100, payment_method=到期还本付息，月复利计息, 
				//product_desc=“年年红”是金朗理财平台推出的具有较高资金流动性的理财模式，固定十二个月出借。到期后本金利息自动收回，省时省心轻松理财。, 
				//borrower_name=null, borrower_province=未知, borrower_age=null, borrower_gender=未知,
				//borrower_job=null, money_useage=null, warrant_status=null}]
//				data.put("result", list);
//				data.put("status", 1);
				Object bid_id = list.get(0).get("bid_id");						//标id
				Object period = list.get(0).get("period");						//投资期限
				Object unit = list.get(0).get("unit");      					//还款期限单位
				Object payment_method = list.get(0).get("payment_method");      //还款方法
//				Object  annualized_rate = list.get(0).get("annualized_rate");    //年化收益率
				BigDecimal annualized_rate = ((BigDecimal) list.get(0).get("annualized_rate")).multiply(new BigDecimal(100)).setScale(0);
				//System.out.println(annualized_rate.toString());
				//String string = annualized_rate.toString();
//				System.out.println("11111111111111111111111111111111111111111111111111");
//				BigDecimal amountDB = new BigDecimal((char[]) annualized_rate);
//				int i = amountDB.intValue();

//				Object rate = Integer.parseInt((String) annualized_rate)*100);
				Object product_desc = list.get(0).get("product_desc");          //产品描述(借款描述)
				Object borrower_name = list.get(0).get("borrower_name");        //借债人姓名
				String borrowerName="";
				if (borrower_name!=null&&!borrower_name.equals("")) {
					borrowerName=(String)borrower_name;
				}else {
					borrowerName="未知";
				}
				Object borrower_province = list.get(0).get("borrower_province");//借债人户籍
				Double borrower_age = (Double) list.get(0).get("borrower_age");          //借债人年龄
				int borrowerAge=0;
				if (borrower_age!=null&&!borrower_age.equals("")) {
					borrowerAge = Integer.parseInt(borrower_age.intValue()+"");
				}else {
					borrowerAge=0;
				}
				Object borrower_gender = list.get(0).get("borrower_gender");    //借债人性别
				Object borrower_job = list.get(0).get("borrower_job");          //借款人身份（单位信息）
				String borrowerJob="";
				if (borrower_job!=null&&!borrower_job.equals("")) {
					borrowerJob=(String)borrower_job;
				}else {
					borrowerJob="未知";
				}
				Object money_useage = list.get(0).get("money_useage");          //借款用途
				Object warrant_status = list.get(0).get("warrant_status");      //担保状态
				String contract_pic = (String) list.get(0).get("contract_pic"); //合同图片
//				String[] contractPic1 = new String[2]; 
//				if (contract_pic!=null&&!contract_pic.equals("")) {
//					contractPic1 = contract_pic.split(",");
//				}else {
//					contractPic1[0] ="noImage_long.png";
//					contractPic1[1]="noImage_short.png";
//				}
											
//				for (Map<String, Object> map : list) {
//					for (Object All : map.values()) {
//						System.out.println(All);	
//					}
//				}
				
				// 应该在service做同步块
				File cache = null;
				try {
					cache = new File(request.getSession().getServletContext().getResource("/cache").getPath());  //获得路径
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (!cache.exists()) {
					cache.mkdir();
				}
				//String path = "cache/ prodIntro.html";  
				String path = "cache/ "+ bidsn + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".html";  
				String file = null;
				try {
					file = request.getSession().getServletContext().getResource("/").getPath() + path;
				} catch (MalformedURLException e) {
					LOG.error(e);
					e.printStackTrace();
				}
				File f = new File(file);
				File template = null;
				try {
					template = new File(request.getSession().getServletContext().getResource("/").getPath() + com.phb.puhuibao.common.Constants.PROD_INTRO);
				} catch (MalformedURLException e) {
					LOG.error(e);
					e.printStackTrace();
				}
				boolean flag = true;
				if (!f.exists() || f.lastModified() < template.lastModified()) {
					//DecimalFormat df = new DecimalFormat("#.00%");
					String content = getContent(request, com.phb.puhuibao.common.Constants.PROD_INTRO);//请求获得页面
					content = content.replaceFirst("\\$period\\$", ""+period);
					content = content.replaceFirst("\\$unit\\$", ""+unit);	
					content = content.replaceFirst("\\$annualized_rate\\$", ""+annualized_rate);	
					content = content.replaceFirst("\\$payment_method\\$", ""+payment_method);	
					content = content.replaceFirst("\\$product_desc\\$", ""+product_desc);	
					content = content.replaceFirst("\\$borrower_name\\$", ""+borrowerName);	
					content = content.replaceFirst("\\$borrower_province\\$", ""+borrower_province);	
					content = content.replaceFirst("\\$borrower_age\\$", ""+borrowerAge);	
					content = content.replaceFirst("\\$borrower_gender\\$", ""+borrower_gender);	
					content = content.replaceFirst("\\$borrower_job\\$", ""+borrowerJob);	
					content = content.replaceFirst("\\$money_useage\\$", ""+money_useage);	
					content = content.replaceFirst("\\$warrant_status\\$", ""+warrant_status);
					content = content.replaceFirst("\\$contractPic\\$", contract_pic);					
//					if (contractPic1.length<=0) {
//						for (int i = 0; i < contractPic1.length; i++) {
//							String contractPhto = contractPic1[i];
//							content = content.replaceFirst("\\$contractPic\\$", contractPhto);
//						}
//					}else {
//						for (int i = 0; i < contractPic1.length; i++) {
//							String contractPhto = contractPic1[i];
//							content = content.replaceFirst("\\$contractPic\\$", contractPhto);
//						}
//						
//					}
				

					OutputStream fis = null;
					OutputStreamWriter osw = null;
					try {
						fis = new FileOutputStream(f);
						osw = new OutputStreamWriter(fis,"utf-8");
						osw.write(content);
						osw.flush();
					} catch (IOException e) {
						flag = false;
						e.printStackTrace();
					} finally {
						try {
							if (osw != null) {
								osw.close();
							}
						} catch (Exception e) {
						}
					}
				}
				if (flag) {
					try {
						response.sendRedirect("../" + path);
						//response.sendRedirect(path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		}  
//		return data;
	}
	
	
	
	
	/**
	 * 当前投资量
	 * @param bidId
	 * @return
	 */
	@RequestMapping(value="getCurrentAmount")
	@ResponseBody
	public Map<String, Object> getCurrentAmount(@RequestParam String bidId) {
		ProductBid bid = this.getBaseService().getById(bidId);
		long currentAmount = bid.getCurrentAmount();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", currentAmount);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 产品详细
	 * @param id
	 * @param muid
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getBidDetailById")
	public void getBidDetailById(@RequestParam String id, @RequestParam String muid, HttpServletRequest request, HttpServletResponse response){
		ProductBid bid = this.getBaseService().getById(id);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productSN", bid.getProductSN());
		AssetProduct result = assetProductService.unique(params);
		if (result == null) {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;	
		}

		// 应该在service做同步块
		File cache = null;
		try {
			cache = new File(request.getSession().getServletContext().getResource("/cache").getPath());
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		if (!cache.exists()) {
			cache.mkdir();
		}

		String path = "cache/" + bid.getBidSN() + new SimpleDateFormat("yyyyMMdd").format(new Date()) + muid + ".html";
		String file = null;
		try {
			file = request.getSession().getServletContext().getResource("/").getPath() + path;
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		File f = new File(file);
		File template = null;
		try {
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + com.phb.puhuibao.common.Constants.BID_TEMPLATE);
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists() || f.lastModified() < template.lastModified()) {
			DecimalFormat df = new DecimalFormat("#.00%");
			String content = getContent(request, com.phb.puhuibao.common.Constants.BID_TEMPLATE);
			//content = content.replaceFirst("\\$version\\$", appContext.getVersion());
			content = content.replaceFirst("\\$bidSN\\$", bid.getBidSN());
			content = content.replaceFirst("\\$paymentMethod\\$", result.getPaymentMethod());
			content = content.replaceAll("\\$guaranteeMethod\\$", result.getGuaranteeMethod());
			double redemptionDueRate = result.getRedemptionDueRate();
			String redemptionDueRateStr = "0.00";
			if (redemptionDueRate > 0) {
				redemptionDueRateStr = df.format(redemptionDueRate);
			}
			content = content.replaceFirst("\\$redemptionDueRate\\$", redemptionDueRateStr);
			double redemptionEarlyRate = result.getRedemptionEarlyRate();
			String redemptionEarlyRateStr = "0.00";
			if (redemptionEarlyRate > 0) {
				redemptionEarlyRateStr = df.format(redemptionEarlyRate);
			}
			content = content.replaceFirst("\\$redemptionEarlyRate\\$", redemptionEarlyRateStr);
			content = content.replaceFirst("\\$investmentAmountMin\\$", "" + result.getInvestmentAmountMin());
			content = content.replaceFirst("\\$investmentAmountMultiple\\$", result.getInvestmentAmountMin() + "");
			content = content.replaceFirst("\\$investmentAmountUpper\\$", "" + result.getInvestmentAmountUpper());
			double annualizedRate = result.getAnnualizedRate();
			String annualizedRateStr = "0.00";
			if (annualizedRate > 0) {
				annualizedRateStr = df.format(annualizedRate);
			}
			content = content.replaceFirst("\\$annualizedRate\\$", annualizedRateStr);
	
			Format format = new SimpleDateFormat(com.phb.puhuibao.common.Constants.DATE_FORMAT_CHS);
//			String joinPeriod = "";
//			Date startDate = result.getStartDate();
//			if (startDate == null) {
//				startDate = new Date();
//			}
//			Date endDate = result.getEndDate();
//			if (endDate == null) {
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(startDate);
//				cal.add(Calendar.DATE, 1);
//				endDate = cal.getTime();
//			}
//			joinPeriod = format.format(startDate) + "至" + format.format(endDate);
//			String joinPeriod = format.format(bid.getStartDate()) + "至" + format.format(bid.getEndDate());
//			content = content.replaceFirst("\\$joinPeriod\\$", joinPeriod);
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1); // 到账第二天起息
			int w = cal.get(Calendar.DAY_OF_WEEK);
			if (w == 1) {
				cal.add(Calendar.DATE, 1);
			} else if (w == 7) {
				cal.add(Calendar.DATE, 2);
			}		
			while (true) {
				String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				String sql = "select 1 from phb_holiday where holiday_date='" + date + "'";
				List <Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
				if (list.isEmpty()) {
					break;
				}
				cal.add(Calendar.DATE, 1);
			}
	        if (result.getUnit().equals("年")) {
	            cal.add(Calendar.YEAR, result.getPeriod());
	        } else if (result.getUnit().indexOf("月") > 0) {
	            cal.add(Calendar.MONTH, result.getPeriod());
	        } else {
	            cal.add(Calendar.DATE, result.getPeriod());
	        }
			cal.add(Calendar.DATE, -1);
			content = content.replaceFirst("\\$endDate\\$", format.format(cal.getTime()));
			
			content = content.replaceFirst("\\$period\\$", result.getPeriod() + result.getUnit());
			double joinRate = result.getJoinRate();
			String joinRateStr = "0.00";
			if (joinRate > 0) {
				joinRateStr = df.format(joinRate);
			}
			content = content.replaceFirst("\\$joinRate\\$", joinRateStr);
			double managementRate = result.getManagementRate();
			String managementRateStr = "0.00";
			if (managementRate > 0) {
				managementRateStr = df.format(managementRate);
			}
			content = content.replaceFirst("\\$managementRate\\$", managementRateStr);
			if ("".equals(muid)) {
				content = content.replaceAll("\\$agreement\\$", "javascript:void(0);");
			} else {
				content = content.replaceAll("\\$agreement\\$", "../productBid.shtml?method=getAgreement&id=" + id +"&muid=" + muid);
			}
					
			OutputStream fis = null;
			OutputStreamWriter osw = null;
			try {
				fis = new FileOutputStream(f);
				osw = new OutputStreamWriter(fis,"utf-8");
				osw.write(content);
				osw.flush();
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
			} finally {
				try {
					if (osw != null) {
						osw.close();
						//Thread.sleep(5000);
					}
				} catch (Exception e) {
				}
			}
		}

		if (flag) {
			try {
				//request.getRequestDispatcher(path).forward(request, response);
				response.sendRedirect("../" + path);
			} catch (Exception e) {
			}
		} else {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 普惠宝协议
	 * @param id
	 * @param muid
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=getAgreement", method = RequestMethod.GET)
	@ResponseBody
	public void getAgreement(@RequestParam String id, @RequestParam String muid, HttpServletRequest request, HttpServletResponse response){
		ProductBid bid = this.getBaseService().getById(id);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productSN", bid.getProductSN());
		AssetProduct result = assetProductService.unique(params);
		if (result == null) {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;	
		}
		if (result.getType() == 2) {
			getMonthAgreement(id, muid, request, response);
			return;
		}

		// 应该在service做同步块
		File cache = null;
		try {
			cache = new File(request.getSession().getServletContext().getResource("/cache").getPath());
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		if (!cache.exists()) {
			cache.mkdir();
		}

		String path = "cache/" + bid.getBidSN() + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "agreement.html";
		String file = null;
		try {
			file = request.getSession().getServletContext().getResource("/").getPath() + path;
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		File f = new File(file);
		File template = null;
		try {
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + com.phb.puhuibao.common.Constants.AGREEMENT_TEMPLATE);
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists() ||  f.lastModified() < template.lastModified()) {
			Format format = new SimpleDateFormat(com.phb.puhuibao.common.Constants.DATE_FORMAT_CHS);
			DecimalFormat df = new DecimalFormat("#.00%");
			String content = getContent(request, com.phb.puhuibao.common.Constants.AGREEMENT_TEMPLATE);
			content = content.replaceFirst("\\$signDate\\$", format.format(new Date()));
			MobileUser user = mobileUserService.getById(muid);
			char[] chars = user.getmUserName().toCharArray();
			int len = chars.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					chars[i] = '*';
				}
				
			}
			content = content.replaceFirst("\\$userName\\$", new String(chars));
			chars = user.getIdNumber().toCharArray();
			len = chars.length - 5;
			for (int i = 0; i < len; i++) {
				if (i > 4) {
					chars[i] = '*';
				}
			}
			content = content.replaceFirst("\\$idNumbe\\$", new String(chars));
			content = content.replaceFirst("\\$totalAmount\\$", "" + bid.getTotalAmount());
			content = content.replaceAll("\\$period\\$", result.getPeriod() + result.getUnit());
			content = content.replaceFirst("\\$productName\\$", result.getProductName());
			double annualizedRate = result.getAnnualizedRate();
			String annualizedRateStr = "0.00";
			if (annualizedRate > 0) {
				annualizedRateStr = df.format(annualizedRate);
			}
			content = content.replaceFirst("\\$annualizedRate\\$", annualizedRateStr);
			content = content.replaceFirst("\\$guaranteeMethod\\$", result.getGuaranteeMethod());
			content = content.replaceFirst("\\$paymentMethod\\$", result.getPaymentMethod());

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1); // 到账第二天起息
			int w = cal.get(Calendar.DAY_OF_WEEK);
			if (w == 1) {
				cal.add(Calendar.DATE, 1);
			} else if (w == 7) {
				cal.add(Calendar.DATE, 2);
			}		
			while (true) {
				String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				String sql = "select 1 from phb_holiday where holiday_date='" + date + "'";
				List <Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
				if (list.isEmpty()) {
					break;
				}
				cal.add(Calendar.DATE, 1);
			}
	        if (result.getUnit().equals("年")) {
	            cal.add(Calendar.YEAR, result.getPeriod());
	        } else if (result.getUnit().indexOf("月") > 0) {
	            cal.add(Calendar.MONTH, result.getPeriod());
	        } else {
	            cal.add(Calendar.DATE, result.getPeriod());
	        }
			cal.add(Calendar.DATE, -1);
			content = content.replaceFirst("\\$endDate\\$", format.format(cal.getTime()));
			
			OutputStream fis = null;
			OutputStreamWriter osw = null;
			try {
				fis = new FileOutputStream(f);
				osw = new OutputStreamWriter(fis,"utf-8");
				osw.write(content);
				osw.flush();
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
			} finally {
				try {
					if (osw != null) {
						osw.close();
					}
				} catch (Exception e) {
				}
			}
		}

		if (flag) {
			try {
				response.sendRedirect(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 金朗理财服务协议
	 * @param id
	 * @param muid
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=getServiceAgreement", method = RequestMethod.GET)
	@ResponseBody
	public void getServiceAgreement(@RequestParam("bid") String id, @RequestParam String muid, HttpServletRequest request, HttpServletResponse response){
		ProductBid bid = this.getBaseService().getById(id);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productSN", bid.getProductSN());
		AssetProduct result = assetProductService.unique(params);
		if (result == null) {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;	
		}
 

		// 应该在service做同步块
		File cache = null;
		try {
			cache = new File(request.getSession().getServletContext().getResource("/cache").getPath());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!cache.exists()) {
			cache.mkdir();
		}

		String path = "cache/" + bid.getBidSN() + new SimpleDateFormat("yyyyMMdd").format(new Date()) + muid + "serviceagreement.html";
		String file = null;
		try {
			file = request.getSession().getServletContext().getResource("/").getPath() + path;
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		File f = new File(file);
		File template = null;
		try {
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + com.phb.puhuibao.common.Constants.SERVICE_AGREEMENT_TEMPLATE);
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists() || f.lastModified() < template.lastModified()) {
			Format format = new SimpleDateFormat(com.phb.puhuibao.common.Constants.DATE_FORMAT_CHS);
			DecimalFormat df = new DecimalFormat("#.00%");
			String content = getContent(request, com.phb.puhuibao.common.Constants.SERVICE_AGREEMENT_TEMPLATE);
			content = content.replaceFirst("\\$signDate\\$", format.format(new Date()));
			MobileUser user = mobileUserService.getById(muid);
			char[] chars = user.getmUserName().toCharArray();
			int len = chars.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					chars[i] = '*';
				}
				
			}
			content = content.replaceFirst("\\$userName\\$", new String(chars));
			chars = user.getIdNumber().toCharArray();
			len = chars.length - 5;
			for (int i = 0; i < len; i++) {
				if (i > 4) {
					chars[i] = '*';
				}
			}
			content = content.replaceFirst("\\$idNumbe\\$", new String(chars));

			
			
			content = content.replaceFirst("\\$totalAmount\\$", "" + bid.getTotalAmount());
			content = content.replaceAll("\\$period\\$", result.getPeriod() + result.getUnit());
			content = content.replaceFirst("\\$productName\\$", result.getProductName());
			double annualizedRate = result.getAnnualizedRate();
			String annualizedRateStr = "0.00";
			if (annualizedRate > 0) {
				annualizedRateStr = df.format(annualizedRate);
			}
			content = content.replaceFirst("\\$annualizedRate\\$", annualizedRateStr);
			content = content.replaceFirst("\\$guaranteeMethod\\$", result.getGuaranteeMethod());
			content = content.replaceFirst("\\$paymentMethod\\$", result.getPaymentMethod());

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1); // 到账第二天起息
			int w = cal.get(Calendar.DAY_OF_WEEK);
			if (w == 1) {
				cal.add(Calendar.DATE, 1);
			} else if (w == 7) {
				cal.add(Calendar.DATE, 2);
			}		
			while (true) {
				String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				String sql = "select 1 from phb_holiday where holiday_date='" + date + "'";
				List <Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
				if (list.isEmpty()) {
					break;
				}
				cal.add(Calendar.DATE, 1);
			}
	        if (result.getUnit().equals("年")) {
	            cal.add(Calendar.YEAR, result.getPeriod());
	        } else if (result.getUnit().indexOf("月") > 0) {
	            cal.add(Calendar.MONTH, result.getPeriod());
	        } else {
	            cal.add(Calendar.DATE, result.getPeriod());
	        }
			cal.add(Calendar.DATE, -1);
			content = content.replaceFirst("\\$endDate\\$", format.format(cal.getTime()));
			
			OutputStream fis = null;
			OutputStreamWriter osw = null;
			try {
				fis = new FileOutputStream(f);
				osw = new OutputStreamWriter(fis,"utf-8");
				osw.write(content);
				osw.flush();
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
			} finally {
				try {
					if (osw != null) {
						osw.close();
					}
				} catch (Exception e) {
				}
			}
		}

		if (flag) {
			try {
				response.sendRedirect(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	
	
	/**
	 * 安全保障
	 * @param id
	 * @param muid
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=getSecurety", method = RequestMethod.GET)
	@ResponseBody
	public void getSecurety(HttpServletRequest request, HttpServletResponse response){
		File cache = null;
		try {
			cache = new File(request.getSession().getServletContext().getResource("/cache").getPath());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!cache.exists()) {
			cache.mkdir();
		}

		String path = "cache/scuretassurance.html";
		String file = null;
		try {
			file = request.getSession().getServletContext().getResource("/").getPath() + path;
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		File f = new File(file);
		File template = null;
		try {
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + com.phb.puhuibao.common.Constants.SCURET_ASSURANCE);
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists()  || f.lastModified() < template.lastModified()) {
			String content = getContent(request, com.phb.puhuibao.common.Constants.SCURET_ASSURANCE);
 
			OutputStream fis = null;
			OutputStreamWriter osw = null;
			try {
				fis = new FileOutputStream(f);
				osw = new OutputStreamWriter(fis,"utf-8");
				osw.write(content);
				osw.flush();
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
			} finally {
				try {
					if (osw != null) {
						osw.close();
					}
				} catch (Exception e) {
				}
			}
		}

		if (flag) {
			try {
				response.sendRedirect(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 我的福利规则说明
	 * @param id
	 * @param muid
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=getMyWelfareRegualation", method = RequestMethod.GET)
	@ResponseBody
	public void getMyWelfareRegualation(HttpServletRequest request, HttpServletResponse response){
		File cache = null;
		try {
			cache = new File(request.getSession().getServletContext().getResource("/cache").getPath());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!cache.exists()) {
			cache.mkdir();
		}

		String path = "cache/myWelfareRegualation.html";
		String file = null;
		try {
			file = request.getSession().getServletContext().getResource("/").getPath() + path;
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		File f = new File(file);
		File template = null;
		try {
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + com.phb.puhuibao.common.Constants.MY_WELFARE_REGUALATION);
		} catch (MalformedURLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists()  || f.lastModified() < template.lastModified()) {
			String content = getContent(request, com.phb.puhuibao.common.Constants.MY_WELFARE_REGUALATION);
 
			OutputStream fis = null;
			OutputStreamWriter osw = null;
			try {
				fis = new FileOutputStream(f);
				osw = new OutputStreamWriter(fis,"utf-8");
				osw.write(content);
				osw.flush();
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
			} finally {
				try {
					if (osw != null) {
						osw.close();
					}
				} catch (Exception e) {
				}
			}
		}

		if (flag) {
			try {
				response.sendRedirect(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 月利通协议
	 * @param id
	 * @param muid
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=getMonthAgreement", method = RequestMethod.GET)
	@ResponseBody
	public void getMonthAgreement(@RequestParam String id, @RequestParam String muid, HttpServletRequest request, HttpServletResponse response){
		ProductBid bid = this.getBaseService().getById(id);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productSN", bid.getProductSN());
		AssetProduct result = assetProductService.unique(params);
		if (result == null) {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		// 应该在service做同步块
		File cache = null;
		try {
			cache = new File(request.getSession().getServletContext().getResource("/cache").getPath());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!cache.exists()) {
			cache.mkdir();
		}

		String path = "cache/" + bid.getBidSN() + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "monthagreement.html";
		String file = null;
		try {
			file = request.getSession().getServletContext().getResource("/").getPath() + path;
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File f = new File(file);
		File template = null;
		try {
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + com.phb.puhuibao.common.Constants.MONTH_AGREEMENT_TEMPLATE);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists() ||  f.lastModified() < template.lastModified()) {
			Format format = new SimpleDateFormat(com.phb.puhuibao.common.Constants.DATE_FORMAT_CHS);
			String content = getContent(request, com.phb.puhuibao.common.Constants.MONTH_AGREEMENT_TEMPLATE);
			content = content.replaceFirst("\\$signDate\\$", format.format(new Date()));
			MobileUser user = mobileUserService.getById(muid);
			char[] chars = user.getmUserName().toCharArray();
			int len = chars.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					chars[i] = '*';
				}
				
			}
			content = content.replaceFirst("\\$userName\\$", new String(chars));
			chars = user.getIdNumber().toCharArray();
			len = chars.length - 5;
			for (int i = 0; i < len; i++) {
				if (i > 4) {
					chars[i] = '*';
				}
			}
			content = content.replaceFirst("\\$idNumbe\\$", new String(chars));
			content = content.replaceFirst("\\$email\\$", user.getmUserEmail());

			OutputStream fis = null;
			OutputStreamWriter osw = null;
			try {
				fis = new FileOutputStream(f);
				osw = new OutputStreamWriter(fis,"utf-8");
				osw.write(content);
				osw.flush();
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
			} finally {
				try {
					if (osw != null) {
						osw.close();
						//Thread.sleep(5000);
					}
				} catch (Exception e) {
				}
			}
		}

		if (flag) {
			try {
				response.sendRedirect(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				request.getRequestDispatcher(com.phb.puhuibao.common.Constants.ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 可理财标
	 */
	@Override
	@RequestMapping(params = "isArray=true", method = RequestMethod.GET)
	@ResponseBody
	public List<ProductBid> findByList(@RequestParam String muid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lstatus", 2);
		List<ProductBid> bids = this.getBaseService().findList(params);
		return bids;
	}
}
