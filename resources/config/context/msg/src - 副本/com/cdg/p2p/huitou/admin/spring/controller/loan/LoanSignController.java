package com.cddgg.p2p.huitou.admin.spring.controller.loan;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.spring.orm.hibernate.HibernateSupportTemplate;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.core.service.BaseLoansignService;
import com.cddgg.p2p.core.userinfo.UserInfoQuery;
import com.cddgg.p2p.huitou.admin.spring.service.loan.LoanSignService;
import com.cddgg.p2p.huitou.admin.spring.service.loan.LoansignTypeService;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.BorrowersApply;
import com.cddgg.p2p.huitou.entity.Costratio;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.LoansignType;
import com.cddgg.p2p.huitou.entity.Loansignbasics;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.model.RechargeModel;
import com.cddgg.p2p.huitou.util.Arith;

/**
 * <p>
 * Title:LoanSignController
 * </p>
 * <p>
 * Description: 普通标Controller
 * </p>
 *         <p>
 *         date 2014年1月26日
 *         </p>
 */
@Controller
@RequestMapping("/loanSign")
public class LoanSignController {
    
    /** 用于调用常用方法的dao */
    @Resource
    HibernateSupportTemplate dao;

    /** loanSignService:普通借款标的service */
    @Resource
    private LoanSignService loanSignService;

    /** baseLoansignService:借款标公用方法的service */
    @Resource
    private BaseLoansignService baseLoansignService;

    /** loanSignQuery 公用借款标的查询 */
    @Autowired
    private LoanSignQuery loanSignQuery;

    /** rechargeModel:导出数据的公用model */
    @Resource
    private RechargeModel rechargeModel;
    
    @Resource
    private LoansignTypeService loansignTypeService;

    @Resource
    private UserInfoQuery userInfoQuery;
    
   
    /**
    * <p>Title: index</p>
    * <p>Description: </p>
    * @return 后台普通标展示页面
    */
    @RequestMapping(value = { "index", "/" })
    public ModelAndView index() {

        ModelAndView returnModelAndView = new ModelAndView(
                "WEB-INF/views/admin/loansign/loansign");

        return returnModelAndView;
    }

    /**
     * <p>
     * Title: loanSignList
     * </p>
     * <p>
     * Description: 借款标列表
     * </p>
     * 
     * @param start
     *            开始
     * @param limit
     *            结束
     * @param loansignbasics
     *            借款标基础信息表
     * @param request
     *            请求的request
     * @return 结果 JSONObject
     */
    @ResponseBody
    @RequestMapping(value = { "loanSignList", "/" })
    public JSONObject loanSignList(
            @RequestParam(value = "start", defaultValue = "0", required = false) int start,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
            Loansignbasics loansignbasics,String loanType,HttpServletRequest request) {

        JSONObject resultjson = new JSONObject();
        // 得到总条数
        int count = loanSignService.getLoansignCount(loansignbasics,loanType);

        // 分页数据源
        List list = loanSignService.loanSignPage(start, limit, loansignbasics,loanType);
        JSONArray jsonlist = loanSignService.queryJSONByList(list);

        // 将数据源封装成json对象（命名必须row）
        resultjson.element("rows", jsonlist);
        // 总条数(命名必须total)
        resultjson.element("total", count);

        return resultjson;
    }

    /**
     * <p>
     * Title: saveOrUpdateLoansign
     * </p>
     * <p>
     * Description: 保存or修改普通标
     * </p>
     * 
     * @param loanSign
     *            借款标信息
     * @param loansignbasics
     *            借款标基础信息
     * @return 返回的结果集
     */
    @ResponseBody
    @RequestMapping(value = { "saveOrUpdateLoansign", "/" })
    public JSONObject saveOrUpdateLoansign(Loansign loanSign,Loansignbasics loansignbasics,Integer type) {

        JSONObject json = new JSONObject();
        boolean bool = true;
        json.element("callbackType","closeCurrent");
        json.element("statusCode", "300");
            
        if(!StringUtil.isNumeric(loanSign.getCounterparts().toString())){
        	bool=false;
            json.element("message", "请输入能正确的普通会员最大认购份数！");
        }
        if(!StringUtil.isNumeric(loanSign.getVipCounterparts().toString())){
        	bool=false;
            json.element("message", "请输入能正确的特权会员最大认购份数！");
        }
        // 判断本期借款金额是否正确    
        	if (loanSign.getIssueLoan() % loanSign.getLoanUnit() != 0) {
                bool=false;
                json.element("message", "请输入能正确的借款金额！");
            }
            
//            // 结束时间不能小于当前时间
//            if (bool&&DateUtils.isAfter(Constant.DEFAULT_DATE_FORMAT,loanSign.getEndTime())) {
//                json.element("message", "结束时间应在当前时候之后！");
//                bool=false;
//            }
            if(!bool){
                return json;
            }
            String alert = getJudge(loanSign.getIssueLoan(),loanSign.getRate(),loanSign.getMonth(),type);
            if(!"".equals(alert)){
            	bool=false;
            	json.element("message", alert);
            }
        	String num = this.judgeLoan(loanSign.getIssueLoan(), loanSign.getBorrowersApply().getId(),type);
        	int nums = Integer.parseInt(num);
        	if(nums == 0){
        		bool=false;
        		json.element("message", "选择的借款标类型和该借款人申请的借款标类型不一致，请重新选择!");
        	}else if(nums == 1){
        		bool=false;
        		json.element("message", "发布金额大于了该借款人申请的金额!");
        	}else if(nums == 2){
        		bool=false;
        		json.element("message", "借款金额大于了投资本息的70%！");
        	}
        	
            // 2.检查有没有设置费用比例
            Costratio costratio = loanSignQuery.queryCostratio();
            loanSign.setRate(loanSign.getRate() / 100);
//            loansignbasics.setMgtMoneyScale(loansignbasics.getMgtMoneyScale() / 100);
            loansignbasics.setReward(loansignbasics.getReward() / 100);
            loanSign.setLoanType(1);// 普通标
            loanSign.setLoansignType(loansignTypeService.queryOne(type.toString()));
            if (loanSign.getId() != null) {
                // update
                bool = baseLoansignService.update(loanSign, loansignbasics,costratio);
            } else {
                // insert
                // 1.检查授信额度
            	BorrowersApply boor = userInfoQuery.getBorrowersApplys(loanSign.getBorrowersApply().getId());
            	loanSign.setBorrowersApply(boor);
            	loanSign.setUserbasicsinfo(boor.getBorrowersbase().getUserbasicsinfo());
                if (!baseLoansignService.checkCredit(loanSign)) {
                    json.element("message", "保存失败,借款金额超过借款人的可用授信额度！");
                    bool=false;
                }

                if (bool&&costratio == null) {
                    json.element("message", "请设置费用比例后在进行新增标操作！");
                   bool=false;
                }
                if(bool){
                    // 3.添加
                    bool = baseLoansignService.save(loanSign, loansignbasics,costratio);
                }else{
                    return json;
                }
            }
        if(bool){
            // dwz返回json对象
            json.element("statusCode", "200");
            json.element("message", "更新成功");
            json.element("navTabId", "main34");
            json.element("callbackType", "closeCurrent");
            return json;
        }else{
            json.element("message", "更新失败");
            return json;
        }
       
    }

    /**
    * <p>Title: onTimeRepay</p>
    * <p>Description: 按时还款</p>
    * @param repaymentRecordId  按时还款的记录编号
    * @param repayTime  按时选择还款时间
    * @return 数字
    * 1.成功 
    * 2.还款失败,只能针对未还款记录还款,请尝试刷新页面！ 
    * 3.还款时间应该在预计还款时间之前！
    * 4.还款失败,只能按期数顺序依次还款！
    * 5 异常
    */
    @ResponseBody
    @RequestMapping("/onTimeRepay")
    public int onTimeRepay(String repaymentRecordId, String repayTime){
        int state=0;
        // 1.判断是否可以还款
        Repaymentrecord repaymentrecord = dao.get(Repaymentrecord.class, Long.valueOf(repaymentRecordId));
        if (repaymentrecord.getRepayState() != 1) {// 1.未还款
            state=2;
        }
        try {
         // 按时还款的时间不能大于预计还款时间
            if (state>0&&DateUtils.isBefore(Constant.DEFAULT_DATE_FORMAT,
                    repaymentrecord.getPreRepayDate(),
                    Constant.DEFAULT_DATE_FORMAT, repayTime)) {
                state=3;
            }
        } catch (ParseException e) {
          return 5;
        }
        
        // 判断是否按期数还款
        if (loanSignQuery.checkRepayOrder(repaymentrecord)) {
            state=4;
        }
        // 还款
        boolean bool = baseLoansignService.onTimeRepay(repaymentrecord,repayTime);
        state=bool?1:5;
       return state;
    }
    /**
    * <p>Title: exceedTimeRepay</p>
    * <p>Description: 逾期还款</p>
    * @param repaymentRecordId 逾期还款的编号
    * @return  数字状态
    * 1.成功 2.还款失败,只能针对未还款记录还款,请尝试刷新页面. 3.还款失败,只能按期数顺序依次还款！4.异常
    */
    @ResponseBody
    @RequestMapping("/exceedTimeRepay")
    public int exceedTimeRepay(String repaymentRecordId) {

        JSONObject json = new JSONObject();
        // 1.判断是否可以还款
        Repaymentrecord repaymentrecord = dao.get(Repaymentrecord.class,
                Long.valueOf(repaymentRecordId));
        if (repaymentrecord.getRepayState() != 1) {// 1.未还款
            return 2;
        }
        // 判断是否按期数还款
        if (loanSignQuery.checkRepayOrder(repaymentrecord)) {
            return 3;
        }
        // 逾期还款
       boolean bool= baseLoansignService.exceedTimeRepay(repaymentrecord);
        return bool?1:4;
    }

    /**
    * <p>Title: exceedTimeRepayed</p>
    * <p>Description: 逾期已还款</p>
    * @param repaymentRecordId 逾期还款的编号
    * @param repayTime 时间
    * @return 数字状态
    * 1.成功 2.还款失败 3.还款失败,只有平台垫付的标可以还款,请尝试刷新页面！
    * 4.还款失败,实际还款时间必须大于预计还款时间！
    */
    @ResponseBody
    @RequestMapping("/exceedTimeRepayed")
    public  int exceedTimeRepayed(String repaymentRecordId, String repayTime) {

        // 1.判断是否可以还款
        Repaymentrecord repaymentrecord = dao.get(Repaymentrecord.class,
                Long.valueOf(repaymentRecordId));
        if (repaymentrecord.getRepayState() != 3) {// 3.逾期未还款
            return 3;
        }

        boolean flag = true;
        try {
            flag = DateUtils.isAfter(Constant.DEFAULT_DATE_FORMAT,
                    repaymentrecord.getPreRepayDate(),
                    Constant.DEFAULT_DATE_FORMAT, repayTime);
        } catch (ParseException e) {
        }

        if (flag) {
            return 4;
        }

        boolean bool = loanSignService.updateRepaymentRecord(repaymentrecord,
                repayTime);
        int returnint = bool ? 1 : 2;

        return returnint;
    }

    /**
    * <p>Title: outPutLoanSignExcel</p>
    * <p>Description: 导出普通标借款列表</p>
    * @param request  request
    * @param response response
    */
    @RequestMapping("/outPutExcel")
    public void outPutLoanSignExcel(HttpServletRequest request, HttpServletResponse response) {

        // 标题
        String[] header = new String[] {"序号", "借款标号", "标题", "借款人", "最小出借单位","借款金额", "还款期限", "借款标类型", "借款管理费", "年化利率", "平台奖励","成功借出份数", "剩余份数", "还款方式", "借款标状态", "是否放款", "放款时间", "发布时间","是否显示", "推荐到首页"};
        // 行宽度
        Integer[] column = new Integer[] { 8, 10, 11, 12, 12, 10, 10, 12, 10,
                10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 };
        // 获取数据源
        List list = loanSignService.outPutList();

        List<Map<String, String>> content = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;

        for (Object obj : list) {
            Object[] str = (Object[]) obj;
            map = new HashMap<String, String>();
            map.put("序号", str[0] + "");
            map.put("借款标号", str[1] + "");
            map.put("标题", str[2] + "");
            map.put("借款人", str[3] + "");
            map.put("最小出借单位", str[4] + "");
            map.put("借款金额", Arith.round(new BigDecimal(str[5].toString()), 2)
                    + "元");
            map.put("还款期限", str[6] + "个月");
            map.put("借款标类型", str[7] + "");
            map.put("借款管理费", str[8] + "");
            map.put("年化利率", Arith.round(new BigDecimal(str[10].toString()), 2)
                    + "%");
            map.put("平台奖励", Arith.round(new BigDecimal(str[11].toString()), 2)
                    + "%");
            map.put("成功借出份数", Integer.parseInt(str[12].toString()) + "");
            map.put("剩余份数", Double.valueOf(str[13].toString()) > 0 ? str[14]
                    + "" : "满标");
            map.put("还款方式", str[14] + "");
            map.put("借款标状态", str[15] + "");
            map.put("是否放款", str[16] + "");
            map.put("发布时间", null != str[9] ? str[9].toString() : "");
            map.put("放款时间", null != str[17] ? str[17].toString() : "");
            map.put("是否显示", str[18] + "");
            map.put("推荐到首页", str[19] + "");
            content.add(map);
        }
        // 下载excel
        rechargeModel.downloadExcel("普通借款标", column, header, content, response);
    }

    /**
     * <p>
     * Title: seeDetails
     * </p>
     * <p>
     * Description: 查询普通标详情
     * </p>
     * 
     * @param id
     *            标编号
     * @param operNumber
     *            操作号 1 新增 2 编辑 3 查看详情
     * @param request
     *            HttpServletRequest
     * @return 执行完方法返回的页面
     */
    @RequestMapping("/seeDetails")
    public ModelAndView seeDetails(
            @RequestParam(value = "id", defaultValue = "", required = false) String id,
            int operNumber, HttpServletRequest request) {

        if (null != id && !id.trim().equals("") && operNumber != 1) {
            // 通过id查询到信息
            Loansign loansign = loanSignQuery.getLoansignById(id);
//            loansign.setRate(Arith.round(
//                    new BigDecimal(loansign.getRate()).multiply(new BigDecimal(
//                            100)), 2).doubleValue());

            Loansignbasics loansignbasics = loanSignQuery.getLoansignbasicsById(id);
//            loansignbasics.setReward(Arith.round(
//                    new BigDecimal(loansignbasics.getReward())
//                            .multiply(new BigDecimal(100)), 2).doubleValue());
//            loansignbasics.setMgtMoneyScale(Arith.round(
//                    new BigDecimal(loansignbasics.getMgtMoneyScale())
//                            .multiply(new BigDecimal(100)), 2).doubleValue());

            request.setAttribute("loansign", loansign);
            request.setAttribute("loansignbasics", loansignbasics);
        } else {
            request.setAttribute("loansign", new Loansign());
            request.setAttribute("loansignbasics", new Loansignbasics());
        }
        //查询标类型
        request.setAttribute("loanType",loansignTypeService.queryLoanType());
        request.setAttribute("operNumber", operNumber);
        ModelAndView returnModelAndView = new ModelAndView(
                "WEB-INF/views/admin/loansign/editloansign");

        return returnModelAndView;
    }
    /**
     * 在发布净值标的时候判断用户的借款金额
     * (净值标借款金额 = 用户前期投资未回收的所有本息和 *70%)
     * 用户发布的金额
     * @param id 用户编号
     * @return 返回用户可发布的净值标金额
     */
    public String judgeLoan(Double money,Long id,Integer typeId){
    	Userbasicsinfo user = new Userbasicsinfo();
    	user.setId(id);
    	BorrowersApply boor = userInfoQuery.getBorrowersApplys(id);
    	if(!boor.getType().equals(typeId)){//发标的类型和申请的类型不一样
    		return "0";
    	}
    	if(boor.getMoney()<money){//发标金额大于了借款金额
    		return "1";  
    	}
    	if(boor.getType()==4){
    		Double moneyAndInterest = loanSignService.getLoanRecordMoney(id);
        	Double moenys = Arith.mul(moneyAndInterest, 0.7).doubleValue();
        	if(money>moenys){
        		return "2";
        	}
    	}
    	return "3";
    }
    /**
     * 
     * @param id
     * @return
     */
    public String getJudge(Double money,Double rate,int month,int id){
    	LoansignType type = loansignTypeService.queryOne(id+"");
    	//判断额度
    	if(type.getMincredit()>money){
    		return "借款金额不能小于最小"+type.getTypename()+"的最小额度";
    	}
    	if(type.getMaxcredit()<money){
    		return "借款金额不能大于最大"+type.getTypename()+"的最大额度";
    	}
    	//利率
    	if(type.getMinrate()>Arith.div(rate,100,4).doubleValue()){
    		return "年华利率不能小于"+type.getTypename()+"的最小利率";
    	}
    	if(type.getMaxrate()<Arith.div(rate,100,4).doubleValue()){
    		return "年华利率不能大于"+type.getTypename()+"的最大利率";
    	}
    	//借款标期限
    	if(type.getMinmoney()>month){
    		return "借款期限不能小于"+type.getTypename()+"的最小借款期限";
    	}
    	if(type.getMaxmoney()<month){
    		return "借款期限不能大于"+type.getTypename()+"的最大借款期限";
    	}
    	return "";
    }
}
