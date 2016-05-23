package com.phb.puhuibao.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

import com.idp.pub.context.AppContext;
import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.ProductBid;
import com.phb.puhuibao.entity.MobileUser;

@Controller
@RequestMapping(value = "/productBid")
public class ProductBidController extends BaseController<ProductBid, String> {
	private static final Log LOG = LogFactory.getLog(ProductBidController.class);

	@Resource(name = "appContext")
	private AppContext appContext;

	@Resource(name = "productBidService")
	public void setBaseService(IBaseService<ProductBid, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "assetProductService")
	private IBaseService<AssetProduct, String> assetProductService;

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
		//params.put("gendDate", new Date());
		List<ProductBid> bids = this.getBaseService().findList(params);
		List<String> productSNs = new ArrayList<String>();
		for (ProductBid bid : bids) {
			productSNs.add(bid.getProductSN());
		}

		Calendar cal = Calendar.getInstance();
		Date startDate = cal.getTime();
		String date = new SimpleDateFormat("yyMMdd").format(startDate);
		for (AssetProduct product : products) { // 自动制标
			product.setEstimateIncome(Functions.estimateIncome(product, jdbcTemplate));
			if (!productSNs.contains(product.getProductSN())) {
				String bidSN = product.getProductSN() + date;
				params = new HashMap<String, Object>();
				params.put("bidSN", bidSN);
				ProductBid bid = this.getBaseService().unique(params);
				int i = 1;
				String sn = bidSN;
				while (bid != null) {
					sn = bidSN + "-" + i;
					params = new HashMap<String, Object>();
					params.put("bidSN", sn);
					bid = this.getBaseService().unique(params);
					i ++;
				}
				bidSN =  sn;
				
				bid = new ProductBid();
				bid.setProductSN(product.getProductSN());
				bid.setBidSN(bidSN);
				bid.setStartDate(startDate);
				cal.add(Calendar.DATE, appContext.getAutoBidPeriod());
				bid.setEndDate(cal.getTime());
//				bid.setTotalAmount((long) appContext.getAutoBidAmount());
				bid.setTotalAmount(product.getTotalAmount());
				this.getBaseService().save(bid);
			}
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
		params.put("orderBy", "status,bid_sn desc,start_date desc");
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
			map.put("productName", product.getProductName());
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
			map.put("startDate", bid.getStartDate());
			if (bid.getStatus() == 0 && new Date().getTime() > bid.getStartDate().getTime()) {
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
		if (!f.exists() || f.lastModified() < result.getUpdateTime().getTime() || f.lastModified() < template.lastModified()) {
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
			String joinPeriod = format.format(bid.getStartDate()) + "至" + format.format(bid.getEndDate());
			content = content.replaceFirst("\\$joinPeriod\\$", joinPeriod);
			
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
		if (!f.exists() || f.lastModified() < result.getUpdateTime().getTime() || f.lastModified() < template.lastModified()) {
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
		if (!f.exists() || f.lastModified() < result.getUpdateTime().getTime() || f.lastModified() < template.lastModified()) {
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
		if (!f.exists() || f.lastModified() < result.getUpdateTime().getTime() || f.lastModified() < template.lastModified()) {
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
	@RequestMapping(params = "isArray=true", method = RequestMethod.GET)
	@ResponseBody
	public List<ProductBid> findByList(@RequestParam String muid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lstatus", 2);
		List<ProductBid> bids = this.getBaseService().findList(params);
		return bids;
	}
}
