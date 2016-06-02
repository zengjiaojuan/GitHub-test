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

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserLoan;
import com.phb.puhuibao.entity.UserLoan;

@Controller
@RequestMapping(value = "/loanItem")
public class LoanItemController extends BaseController<LoanItem, String> {
	@Override
	@Resource(name = "loanItemService")
	public void setBaseService(IBaseService<LoanItem, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@Resource(name = "userLoanService")
	private IBaseService<UserLoan, String> userLoanService;

	@Resource(name = "mobileUserLoanService")
	private IBaseService<MobileUserLoan, String> mobileUserLoanService;

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 项目区列表
	 * @param pageno
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno,@RequestParam String muid) {
		Pager<LoanItem> pager = new Pager<LoanItem>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(10);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderBy", "a.status,start_date desc");
		params.put("mUserId", muid);
		//params.put("order", "desc");
		Pager<LoanItem> p = this.getBaseService().findByPager(pager, params);
		List<LoanItem> result = p.getData();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (LoanItem item : result) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("itemId", item.getItemId());
			map.put("itemSN", item.getItemSN());
			map.put("itemName", item.getUseage());
			map.put("guaranteeMethod", item.getGuaranteeMethod());
			map.put("investmentAmountMin", item.getInvestmentAmountMin());
			map.put("estimateIncome", com.phb.puhuibao.common.Constants.ESTIMATE_INCOME_BASE * item.getAnnualizedRate() * item.getPeriod() / 12);
			map.put("peroid", item.getPeriod());
			map.put("itemDesc", item.getItemDesc());
			map.put("annualizedRate", item.getAnnualizedRate());
			map.put("currentAmount", item.getCurrentAmount());
			map.put("totalAmount", item.getTotalAmount());
			map.put("startDate", item.getStartDate());
			if (item.getStatus() == 0 && new Date().getTime() > item.getStartDate().getTime()) {
				item.setStatus(1);
				this.getBaseService().update(item);
//			} else if (item.getStatus() == 1 && new Date().getTime() > item.getEndDate().getTime()) {
//				item.setStatus(2);
//				this.getBaseService().update(item);
			}
			map.put("status", item.getStatus());
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
	 * 项目协议
	 * @param itemId
	 * @param muid
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=getItemAgreement", method = RequestMethod.GET)
	@ResponseBody
	public void getItemAgreement(@RequestParam String itemId, @RequestParam String muid, HttpServletRequest request, HttpServletResponse response){
		LoanItem item = this.getBaseService().getById(itemId);
		if (item == null) {
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

		String path = "cache/" + item.getItemSN() + new SimpleDateFormat("yyyyMMdd").format(new Date()) + muid + "itemagreement.html";
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
			template = new File(request.getSession().getServletContext().getResource("/").getPath() + com.phb.puhuibao.common.Constants.SERVICE_AGREEMENT_TEMPLATE);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean flag = true;
		if (!f.exists() ||  f.lastModified() < template.lastModified()) {
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

			
			
			content = content.replaceFirst("\\$totalAmount\\$", "" + item.getTotalAmount());
			content = content.replaceAll("\\$period\\$", ""+item.getPeriod() );
			content = content.replaceFirst("\\$productName\\$", item.getUseage());
			double annualizedRate = item.getAnnualizedRate();
			String annualizedRateStr = "0.00";
			if (annualizedRate > 0) {
				annualizedRateStr = df.format(annualizedRate);
			}
			content = content.replaceFirst("\\$annualizedRate\\$", annualizedRateStr);
			content = content.replaceFirst("\\$guaranteeMethod\\$", item.getGuaranteeMethod());
			content = content.replaceFirst("\\$paymentMethod\\$", item.getPaymentMethod());

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
			 cal.add(Calendar.MONTH, item.getPeriod());
			
//	        if (item.getUnit().equals("年")) {
//	            cal.add(Calendar.YEAR, item.getPeriod());
//	        } else if (item.getUnit().indexOf("月") > 0) {
//	            cal.add(Calendar.MONTH, item.getPeriod());
//	        } else {
//	            cal.add(Calendar.DATE, item.getPeriod());
//	        }
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
	 * 当前投资量
	 * @param bidId
	 * @return
	 */
	@RequestMapping(value="getCurrentAmount")
	@ResponseBody
	public Map<String, Object> getCurrentAmount(@RequestParam String itemId) {
		LoanItem item = this.getBaseService().getById(itemId);
		long currentAmount = item.getCurrentAmount();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", currentAmount);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 项目详细
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value="getItemDetailById")
	@ResponseBody
	public Map<String, Object> getItemDetailById(@RequestParam String itemId) {
		LoanItem item = this.getBaseService().getById(itemId);
		UserLoan loan = userLoanService.getById(item.getLoanId() + "");
		MobileUserLoan userLoan = mobileUserLoanService.getById(loan.getmUserId() + "");
		if (userLoan == null) { // 没经过资质审核
			userLoan = new MobileUserLoan();
			userLoan.setType(0);
		}
		MobileUser user = mobileUserService.getById(loan.getmUserId() + "");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userName", user.getmUserName());
		result.put("organization", userLoan.getOrganization());
		result.put("orgAddress", userLoan.getOrgAddress());
		result.put("orgProperty", userLoan.getOrgProperty());
		result.put("salary", userLoan.getSalary());
		result.put("itemDesc", item.getItemDesc());
		result.put("type", userLoan.getType());
		result.put("certificate1", userLoan.getJobCertification());
  
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 可投资标
	 */
	@Override
	@RequestMapping(params = "isArray=true", method = RequestMethod.GET)
	@ResponseBody
	public List<LoanItem> findByList(@RequestParam String muid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lstatus", 2);
		List<LoanItem> items = this.getBaseService().findList(params);
		return items;
	}
}
