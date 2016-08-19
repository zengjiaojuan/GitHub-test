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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.context.AppContext;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.MobileUser;

@Controller
@RequestMapping(value = "/assetProduct")
public class AssetProductController extends BaseController<AssetProduct, String> {
	private final static String PRODUCT_TEMPLATE = "/templates/puhuibao/product.html";
	private final static String AGREEMENT_TEMPLATE = "/templates/puhuibao/agreement.html";
	private final static String MONTH_AGREEMENT_TEMPLATE = "/templates/puhuibao/monthAgreement.html";
	private final static String ERROR_TEMPLATE = "/templates/puhuibao/error.html";

	@Resource(name = "appContext")
	private AppContext appContext;

	@Override
	@Resource(name = "assetProductService")
	public void setBaseService(IBaseService<AssetProduct, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 产品列表
	 * @param type
	 * @return
	 */
	@RequestMapping(value="findList")
	@ResponseBody
	protected Map<String, Object> findList(@RequestParam String type) {
		Map<String,Object> params = new HashMap<String,Object>();
		if ("".equals(type)) {
		} else if ("1".equals(type)) { // 普惠宝所有
			params = new HashMap<String,Object>();
			params.put("getype", type);
		} else {
			params.put("type", type);
		}
		List<AssetProduct> result = this.getBaseService().findList(params);

		for (AssetProduct product : result) {
			product.setEstimateIncome(Functions.estimateIncome(product, jdbcTemplate));
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
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
	@Deprecated
	@RequestMapping(value="getProductDetailById")
	public void getProductDetailById(@RequestParam String id, @RequestParam String muid, HttpServletRequest request, HttpServletResponse response){
		AssetProduct result = this.getBaseService().getById(id);
		if (result == null) {
			try {
				request.getRequestDispatcher(ERROR_TEMPLATE).forward(request, response);
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

		String path = "cache/" + result.getProductSN() + new SimpleDateFormat("yyyyMMdd").format(new Date()) + muid + ".html";
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
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + PRODUCT_TEMPLATE);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists()  || f.lastModified() < template.lastModified()) {
			DecimalFormat df = new DecimalFormat("#.00%");
			String content = getContent(request, PRODUCT_TEMPLATE);
			//content = content.replaceFirst("\\$version\\$", appContext.getVersion());
			content = content.replaceFirst("\\$productSN\\$", result.getProductSN());
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
			content = content.replaceFirst("\\$investmentAmountMin\\$", ""+result.getInvestmentAmountMin());
			content = content.replaceFirst("\\$investmentAmountMultiple\\$", ""+result.getInvestmentAmountMultiple());
			content = content.replaceFirst("\\$investmentAmountUpper\\$", ""+result.getInvestmentAmountUpper());
			double annualizedRate = result.getAnnualizedRate();
			String annualizedRateStr = "0.00";
			if (annualizedRate > 0) {
				annualizedRateStr = df.format(annualizedRate);
			}
			content = content.replaceFirst("\\$annualizedRate\\$", annualizedRateStr);
	
			Format format = new SimpleDateFormat("yyyy年MM月dd日");
			String joinPeriod = "";
			 
		 
			
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
				content = content.replaceAll("\\$agreement\\$", "../assetProduct.shtml?method=getAgreement&id=" + id +"&muid=" + muid);
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
				request.getRequestDispatcher(ERROR_TEMPLATE).forward(request, response);
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
	@Deprecated
	@RequestMapping(params = "method=getAgreement", method = RequestMethod.GET)
	@ResponseBody
	public void getAgreement(@RequestParam String id, @RequestParam String muid, HttpServletRequest request, HttpServletResponse response){
		AssetProduct result = this.getBaseService().getById(id);
		if (result == null) {
			try {
				request.getRequestDispatcher(ERROR_TEMPLATE).forward(request, response);
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
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!cache.exists()) {
			cache.mkdir();
		}

		String path = "cache/" + result.getProductSN() + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "agreement.html";
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
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + AGREEMENT_TEMPLATE);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists() ||  f.lastModified() < template.lastModified()) {
			Format format = new SimpleDateFormat("yyyy年MM月dd日");
			DecimalFormat df = new DecimalFormat("#.00%");
			String content = getContent(request, AGREEMENT_TEMPLATE);
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
			content = content.replaceFirst("\\$totalAmount\\$", "" + result.getTotalAmount());
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
				request.getRequestDispatcher(ERROR_TEMPLATE).forward(request, response);
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
	@Deprecated
	@RequestMapping(params = "method=getMonthAgreement", method = RequestMethod.GET)
	@ResponseBody
	public void getMonthAgreement(@RequestParam String id, @RequestParam String muid, HttpServletRequest request, HttpServletResponse response){
		AssetProduct result = this.getBaseService().getById(id);
		if (result == null) {
			try {
				request.getRequestDispatcher(ERROR_TEMPLATE).forward(request, response);
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

		String path = "cache/" + result.getProductSN() + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "monthagreement.html";
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
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + MONTH_AGREEMENT_TEMPLATE);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists() ||  f.lastModified() < template.lastModified()) {
			Format format = new SimpleDateFormat("yyyy年MM月dd日");
			String content = getContent(request, MONTH_AGREEMENT_TEMPLATE);
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
				request.getRequestDispatcher(ERROR_TEMPLATE).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
