package com.cddgg.p2p.huitou.admin.spring.controller.loan;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.constant.Constant;
import com.cddgg.base.model.PageModel;
import com.cddgg.base.sms.SmsResult;
import com.cddgg.base.spring.service.BaseSmsService;
import com.cddgg.base.util.ArrayToJson;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.core.service.AttachmentService;
import com.cddgg.p2p.core.service.BaseLoansignService;
import com.cddgg.p2p.core.service.CommentService;
import com.cddgg.p2p.core.service.LoanrecordService;
import com.cddgg.p2p.core.service.RepaymentrecordService;
import com.cddgg.p2p.huitou.admin.spring.service.UserInfoServices;
import com.cddgg.p2p.huitou.admin.spring.service.loan.LoansignTypeService;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.LoansignType;
import com.cddgg.p2p.huitou.model.RechargeModel;
import com.cddgg.p2p.huitou.spring.service.EmailService;
import com.cddgg.p2p.huitou.spring.service.ProcessingService;
import com.cddgg.p2p.huitou.util.Arith;
import com.cddgg.p2p.huitou.util.DwzResponseUtil;
import com.cddgg.p2p.pay.entity.ReturnInfo;
import com.cddgg.p2p.pay.entity.TenderAuditInfo;
import com.cddgg.p2p.pay.payservice.RegisterService;
import com.cddgg.p2p.pay.util.ParameterIps;
import com.cddgg.p2p.pay.util.ParseXML;
import com.cddgg.p2p.pay.util.XmlParsingBean;

import freemarker.template.TemplateException;

/**
 * 通用标Controller
 * 
 * @author longyang
 * 
 */
@Controller
@RequestMapping("/baseLoanSign")
public class BaseLoanSignController {
	
	/** baseLoansignService 通用services */
	@Resource
	private BaseLoansignService baseLoansignService;

	/** loanSignQuery 借款标查询 */
	@Autowired
	private LoanSignQuery loanSignQuery;

	/** rechargeModel 导出实体 */
	@Resource
	private RechargeModel rechargeModel;

	/** loanrecordService 认购记录services */
	@Resource
	private LoanrecordService loanrecordService;

	/** repaymentrecordService 还款记录 */
	@Resource
	private RepaymentrecordService repaymentrecordService;

	/** attachmentService 借款标附件 */
	@Resource
	private AttachmentService attachmentService;

	/** commentService 评论Service */
	@Resource
	private CommentService commentService;

	@Resource
	private ProcessingService processingService;

	@Resource
	private UserInfoServices infoServices;
	
	@Resource
	private BaseSmsService baseSmsService; 
	
	@Resource
	private EmailService emailService;

	 @Resource
	 private LoansignTypeService loansignTypeService;
	/**
	 * <p>
	 * Title: queryBorrowersbaseList
	 * </p>
	 * <p>
	 * Description: 查询所有的借款人
	 * </p>
	 * 
	 * @param username
	 *            借款人用户名
	 * @param cardno
	 *            身份证号码
	 * @param page
	 *            分页page
	 * @param conditions
	 *            查询条件
	 * @param request
	 *            请求
	 * @return 列表展示页面
	 */
	@RequestMapping("borrowersbaseList")
	public String queryBorrowersbaseList(
			@RequestParam(value = "username", defaultValue = "", required = false) String username,
			@RequestParam(value = "cardno", defaultValue = "", required = false) String cardno,
			PageModel page, String conditions, HttpServletRequest request) {
		// 查询借款人条件
		Object count = baseLoansignService.queryBorrowersbasecount(username,
				cardno);
		page.setTotalCount(Integer.parseInt(count.toString()));
		// 分页查询所有借款人
		Object obj = baseLoansignService.queryBorrowersbaseList(page, username,
				cardno);
		request.setAttribute("list", obj);
		request.setAttribute("page", page);
		request.setAttribute("username", username.trim());
		request.setAttribute("cardno", cardno);
		return "WEB-INF/views/admin/loansign/borrowerlist";

	}
	/**
	 * <p>
	 * Title: queryBorrowersbaseList
	 * </p>
	 * <p>
	 * Description: 查询所有的借款人(针对普通标的情况)
	 * </p>
	 * 
	 * @param username
	 *            借款人用户名
	 * @param cardno
	 *            身份证号码
	 * @param page
	 *            分页page
	 * @param conditions
	 *            查询条件
	 * @param request
	 *            请求
	 * @return 列表展示页面
	 */
	@RequestMapping("borrowersbaseLists")
	public String queryBorrowersbaseLists(
			@RequestParam(value = "username", defaultValue = "", required = false) String username,
			@RequestParam(value = "cardno", defaultValue = "", required = false) String cardno,
			PageModel page, String conditions, HttpServletRequest request) {
		// 查询借款人条件
		Object count = baseLoansignService.queryBorrowersbasecounts(username,
				cardno);
		page.setTotalCount(Integer.parseInt(count.toString()));
		// 分页查询所有借款人
		Object obj = baseLoansignService.queryBorrowersbaseLists(page, username,
				cardno);
		request.setAttribute("list", obj);
		request.setAttribute("page", page);
		request.setAttribute("username", username.trim());
		request.setAttribute("cardno", cardno);
		return "WEB-INF/views/admin/loansign/borrowerlists";

	}

	/**
	 * <p>
	 * Title: loanrecordList
	 * </p>
	 * <p>
	 * Description: 认购记录列表
	 * </p>
	 * 
	 * @param start
	 *            开始
	 * @param limit
	 *            结束
	 * @param id
	 *            编号
	 * @param request
	 *            请求
	 * @return 结果集
	 */
	@ResponseBody
	@RequestMapping(value = { "loanrecordList", "/" })
	public JSONObject loanrecordList(
			@RequestParam(value = "start", defaultValue = "0", required = false) int start,
			@RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
			@RequestParam(value = "id", defaultValue = "", required = false) int id,
			HttpServletRequest request) {

		// 得到总条数
		Object count = loanrecordService.getLoanrecordCount(id);
		// 分页数据源
		List list = loanrecordService.queryLoanrecordList(start, limit, id, 1);
		JSONArray jsonlist = loanrecordService.getJSONArrayByList(list);

		JSONObject resultjson = new JSONObject();
		// 将数据源封装为json对象（命名必须rows）
		resultjson.element("rows", jsonlist);
		// 总条数(命名必须total)
		resultjson.element("total", count);
		return resultjson;
	}

	/**
	 * <p>
	 * Title: repaymentRecordList
	 * </p>
	 * <p>
	 * Description: 还款记录列表
	 * </p>
	 * 
	 * @param start
	 *            开始
	 * @param limit
	 *            结束
	 * @param id
	 *            借款标编号
	 * @param request
	 *            请求
	 * @return 集合对象
	 */
	@ResponseBody
	@RequestMapping(value = { "repaymentRecordList", "/" })
	public JSONObject repaymentRecordList(
			@RequestParam(value = "start", defaultValue = "0", required = false) int start,
			@RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
			@RequestParam(value = "id", defaultValue = "", required = false) int id,
			HttpServletRequest request) {

		// 得到总条数
		Object count = repaymentrecordService.getrepaymentRecordCount(id);
		// 分页数据源
		List list = repaymentrecordService.queryRepaymentrecordList(start,
				limit, id);
		JSONArray jsonlist = repaymentrecordService.getJSONArrayByList(list);

		JSONObject resultjson = new JSONObject();
		// 将数据源封装为json对象（命名必须rows）
		resultjson.element("rows", jsonlist);
		// 总条数(命名必须total)
		resultjson.element("total", count);
		return resultjson;
	}

	/**
	 * <p>
	 * Title: AttachmentList
	 * </p>
	 * <p>
	 * Description: 附件信息列表
	 * </p>
	 * 
	 * @param start
	 *            开始
	 * @param limit
	 *            结束
	 * @param id
	 *            借款标编号
	 * @param request
	 *            请求
	 * @return 列表对象
	 */
	@ResponseBody
	@RequestMapping(value = { "attachmentList", "/" })
	public JSONObject attachmentList(
			@RequestParam(value = "start", defaultValue = "0", required = false) int start,
			@RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
			@RequestParam(value = "id", defaultValue = "", required = false) int id,
			HttpServletRequest request) {

		JSONArray jsonlist = new JSONArray();

		// 得到总条数
		Object count = attachmentService.getAttachmentCount(id);
		// 分页数据源
		List list = attachmentService.queryAttachmentList(start, limit, id);
		String titles = "id,attachmentName,attachmentType,uploadTime,realname";
		ArrayToJson.arrayToJson(titles, list, jsonlist);

		JSONObject resultjson = new JSONObject();
		// 将数据源封装为json对象（命名必须rows）
		resultjson.element("rows", jsonlist);
		// 总条数(命名必须total)
		resultjson.element("total", count);
		return resultjson;
	}

	/**
	 * <p>
	 * Title: CommentList
	 * </p>
	 * <p>
	 * Description: 评论列表
	 * </p>
	 * 
	 * @param start
	 *            开始
	 * @param limit
	 *            结束
	 * @param id
	 *            借款标编号
	 * @param request
	 *            请求
	 * @return 列表对象
	 */
	@ResponseBody
	@RequestMapping(value = { "commentList", "/" })
	public JSONObject commentList(
			@RequestParam(value = "start", defaultValue = "0", required = false) int start,
			@RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
			@RequestParam(value = "id", defaultValue = "", required = false) int id,
			HttpServletRequest request) {

		JSONArray jsonlist = new JSONArray();
		// 得到总条数
		Object count = commentService.getCommentCount(id);
		// 分页数据源
		List list = commentService.queryCommentList(start, limit, id);
		String titles = "id,cmtcontent,name,cmtReply,cmtIsShow";
		ArrayToJson.arrayToJson(titles, list, jsonlist);
		JSONObject resultjson = new JSONObject();
		// 将数据源封装为json对象（命名必须rows）
		resultjson.element("rows", jsonlist);
		// 总条数(命名必须total)
		resultjson.element("total", count);
		return resultjson;
	}

	/**
	 * <p>
	 * Title: deleteone
	 * </p>
	 * <p>
	 * Description: 删除一个未发布的借款标
	 * </p>
	 * 
	 * @param id
	 *            借款标编号
	 * @return 受影响次数
	 */
	@ResponseBody
	@RequestMapping(value = { "/deleteAll" })
	public int deleteOne(String id) {
		return baseLoansignService.deleteone(id);
	}

	/**
	 * <p>
	 * Title: outPutLoanrecordExcel
	 * </p>
	 * <p>
	 * Description: 导出出借记录
	 * </p>
	 * 
	 * @param id
	 *            编号
	 * @param response
	 *            response
	 */
	@RequestMapping("outPutLoanrecordExcel")
	public void outPutLoanrecordExcel(int id, HttpServletResponse response) {

		// 标题
		String[] header = new String[] { "出借人", "年化利率", "投标金额", "支付状态", "投标时间" };
		// 行宽度
		Integer[] column = new Integer[] { 8, 10, 12, 10, 12 };
		// 获取数据源
		List list = loanrecordService.queryLoanrecordList(0, 0, id, 2);

		List<Map<String, String>> content = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (Object obj : list) {
			Object[] str = (Object[]) obj;
			map = new HashMap<String, String>();
			map.put("出借人", str[0] + "");
			map.put("年化利率", Arith.round(new BigDecimal(str[1].toString()), 2)
					+ "%");
			map.put("投标金额", Arith.round(new BigDecimal(str[2].toString()), 2)
					+ "元");
			map.put("支付状态", str[3] + "");
			map.put("投标时间", str[4] + "");
			content.add(map);
		}
		// 下载excel
		rechargeModel.downloadExcel("出借记录", column, header, content, response);
	}

	/**
	 * <p>
	 * Title: publish
	 * </p>
	 * <p>
	 * Description: 发布(普通标，天标，秒标)
	 * </p>
	 * 
	 * @param loanSignId
	 *            标号
	 * @return 数字状态1.成功2.状态不是未发布的3发布报错
	 * @throws Exception
	 *             异常
	 */
	@ResponseBody
	@RequestMapping("/publish")
	public int publish(String loanSignId) throws Exception {
//		JSONObject json = new JSONObject();
		// 1、检查是否可以发布
		Loansign loansign = loanSignQuery.getLoansignById(loanSignId);
		
		if(loansign.getLoanType()==1){
			loansign.setEndTime(DateUtils.add("yyyy-MM-dd", Calendar.DATE, loansign.getLoansignType().getMoney()-1));
		}else{
			loansign.setEndTime(DateUtils.add("yyyy-MM-dd", Calendar.DATE, 4));
		}
		if (loansign.getLoanstate() != 1) {
			return 2;
		}
		// 2.发布
		boolean bool = baseLoansignService.publish(loansign);
		return bool ? 1 : 3;
	}
	/**
	 * 即将到期的标
	 * 
	 * @param page
	 *            分页
	 * @param loanType
	 *            借款标类型
	 * @param request
	 *            请求
	 * @return 页面
	 */
	@RequestMapping("toLoanSignExpiring")
	public ModelAndView toLoanSignExpiring(PageModel page, int loanType,
			HttpServletRequest request) {
		request.setAttribute("loanSignExpir",
				baseLoansignService.findExpirLoanSign(page, loanType));
		request.setAttribute("loanType", loanType);
		request.setAttribute("page", page);
		return new ModelAndView("WEB-INF/views/admin/loansign/loansignexpiring");
	}

	/**
	 * 通过环迅进行放款操作
	 * 
	 * @param id
	 *            标编号
	 * @return 返回放款情况
	 */
	@RequestMapping("/credit")
	@ResponseBody
	public Integer ipsLoans(String id, HttpServletRequest request) {

		// 获取标的情况
		Loansign loan = loanSignQuery.getLoansignById(id);
		// 判断该标是否能放款
		int state = com.cddgg.p2p.huitou.constant.Constant.STATUES_ZERO;
		// 1、检查是否可以放款
		if (!loan.getLoanstate().equals(com.cddgg.p2p.huitou.constant.Constant.STATUES_TWO)) {// 是否是进行中
			state = com.cddgg.p2p.huitou.constant.Constant.STATUES_TWO;
		}
		// 满标才能放款
		if (state == com.cddgg.p2p.huitou.constant.Constant.STATUES_ZERO && loanSignQuery.isFull(id) == false) {
			state = com.cddgg.p2p.huitou.constant.Constant.STATUES_THERE;
		}
		if(state==com.cddgg.p2p.huitou.constant.Constant.STATUES_ZERO){
			// 封装标的放款实体
			TenderAuditInfo tend = new TenderAuditInfo(loan.getLoansignbasics()
					.getpBidNo(), loan.getLoansignbasics().getpContractNo(), loan.getIssueLoan().toString(),
					DateUtils.format("yyyy-MM-dd HH:mm:ss"), loan.getId()
							.toString());
			// 将实体进行加密操作
			ReturnInfo info=null;
			try {
				Map<String, String> map = RegisterService.registerCall(ParseXML
						.tenderAuditXml(tend));
				info = RegisterService.auditTender(map);
				// 返回成功进行处理
				if (Constant.SUCCESS.equals(info.getpErrCode())) {
					if(ParameterIps.pianText(info)){
						TenderAuditInfo auditInfo = (TenderAuditInfo) XmlParsingBean.simplexml1Object(info.getP3DesXmlPara(),new TenderAuditInfo());
						// 对借款人账户的处理
						boolean bool = processingService.tenderAudit(loan.getUserbasicsinfo(),
								auditInfo, request,loan.getLoansignbasics().getMgtMoney());
						if(!bool){
							Log.error("环迅放款成功-->我方放款失败-->数据处理失败-->还款ips编号:"+auditInfo.getpIpsBillNo()+"还款标号:"+loan.getId());
							state = com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR;
						}else{
							// 生成还款计划
							baseLoansignService.repaymentRecords(loan);
							state = com.cddgg.p2p.huitou.constant.Constant.STATUES_ONE;
						}
					}else{
						Log.error("该次放款信息不是由环迅返回");
						state = com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR;
					}
				}else{
					Log.error("环迅放款失败");
					state = com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR;
				}
			} catch (IOException | TemplateException | ParseException e) {
				// TODO Auto-generated catch block
				LOG.error("放款失败,数据解析失败-->需要解析的数据为："+info.getP3DesXmlPara());
				state = com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR;
				e.printStackTrace();
			}
		}
		return state;
	}
	/**
	 * 通过环迅进行流标操作
	 * 
	 * @param id
	 *            标编号
	 * @return 返回放款情况
	 */
	@RequestMapping("/flow.htm")
	@ResponseBody
	public Integer ipsFlowStandard(String id, HttpServletRequest request) {

		// 获取标的情况
		Loansign loan = loanSignQuery.getLoansignById(id);
		// 判断该标是否能放款
		int state = com.cddgg.p2p.huitou.constant.Constant.STATUES_ZERO;
		// 1、检查是否可以进行流标操作
		if (!loan.getLoanstate().equals(com.cddgg.p2p.huitou.constant.Constant.STATUES_TWO)) {// 是否是进行中
			state = com.cddgg.p2p.huitou.constant.Constant.STATUES_TWO;
		}
//		// 满标才能放款
//		if (state == com.cddgg.p2p.huitou.constant.Constant.STATUES_ZERO && loanSignQuery.isFull(id) == false) {
//			state = com.cddgg.p2p.huitou.constant.Constant.STATUES_THERE;
//		}
		if(state==com.cddgg.p2p.huitou.constant.Constant.STATUES_ZERO){
			// 封装标的放款实体
			TenderAuditInfo tend = new TenderAuditInfo(loan.getLoansignbasics(),loan.getIssueLoan().toString(),
					DateUtils.format("yyyy-MM-dd HH:mm:ss"), loan.getId().toString());
			// 将实体进行加密操作
			ReturnInfo info=null;
			try {
				Map<String, String> map = RegisterService.registerCall(ParseXML
						.tenderAuditXml(tend));
				info = RegisterService.auditTender(map);
				// 返回成功进行处理
				if (Constant.SUCCESS.equals(info.getpErrCode())) {
					if(ParameterIps.pianText(info)){
						TenderAuditInfo auditInfo = (TenderAuditInfo) XmlParsingBean.simplexml1Object(info.getP3DesXmlPara(),new TenderAuditInfo());
						// 对借款人账户的处理
						boolean bool = processingService.flowStandard(loan,auditInfo.getpIpsBillNo());
						if(!bool){
							Log.error("环迅流标成功-->我方流标失败-->数据处理失败-->流标ips编号:"+auditInfo.getpIpsBillNo()+"流标标号:"+loan.getId());
							state = com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR;
						}else{
							state = com.cddgg.p2p.huitou.constant.Constant.STATUES_ONE;
						}
					}else{
						Log.error("该次流标信息不是由环迅返回");
						state = com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR;
					}
				}else{
					Log.error("环迅流标失败");
					state = com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR;
				}
			} catch (IOException | TemplateException e) {
				// TODO Auto-generated catch block
				LOG.error("流标失败,数据解析失败-->需要解析的数据为："+info.getP3DesXmlPara());
				state = com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR;
				e.printStackTrace();
			}
		}
		return state;
	}
	/**
	 * 初始化发送信息页面
	 * @return 返回发送信息页面
	 */
	@RequestMapping("openMessage")
	public String openMessage(Long loanId,HttpServletRequest request){
		request.setAttribute("loan",baseLoansignService.get(loanId));
		return "WEB-INF/views/admin/loansign/add_remind";
	}
	/**
	 * 发送短信或邮件
	 * @param fashion 发送方式0 表示发送短信 1表示发送邮件
	 * @param content 发送内容
	 * @return 发送是否成功
	 */
	@RequestMapping("sendSms.htm")
	@ResponseBody
	public JSONObject sendChatMessage(int fashion,String content,String phone,String email){
		JSONObject json = new JSONObject();
		//发送短信
		try {
			if(fashion == com.cddgg.p2p.huitou.constant.Constant.STATUES_ZERO){
				SmsResult sms = baseSmsService.sendSMS(content, phone);
				if(sms.isSuccess()){
					return DwzResponseUtil.setJson(json, "200", "短信发送成功",null, "closeCurrent");
				}else{
					return DwzResponseUtil.setJson(json, "300", "短信发送失败",null, "closeCurrent");
				}
			}else{
				emailService.sendEmail("p2p网贷平台标到期提醒", content, email);
				return DwzResponseUtil.setJson(json, "200", "邮件发送成功",null, "closeCurrent");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return DwzResponseUtil.setJson(json, "300", "发送失败",null, "closeCurrent");
		}
	} 
	
    /**
     * 获取标类型的数据
     * @return 返回一个json数组
     */
	@RequestMapping("loanType.htm")
	@ResponseBody
    public JSONArray getLoanType(){
        JSONArray json = new JSONArray();
        List<LoansignType> listType = loansignTypeService.queryLoanType();
        for(int i=0;i<listType.size();i++){
        	JSONObject jsonObject = new JSONObject();
        	jsonObject.accumulate("text",listType.get(i).getTypename());
        	jsonObject.accumulate("value",listType.get(i).getId());
        	json.add(jsonObject);
        }
        return json;
    }
	
}
