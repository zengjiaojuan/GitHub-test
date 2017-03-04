package com.cddgg.p2p.huitou.admin.spring.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.util.ArrayToJson;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.normal.Md5Util;
import com.cddgg.p2p.core.loansignfund.AutointegralQuery;
import com.cddgg.p2p.huitou.admin.spring.service.BorrowersApplyService;
import com.cddgg.p2p.huitou.admin.spring.service.IntegralService;
import com.cddgg.p2p.huitou.admin.spring.service.UserInfoServices;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.BorrowersApply;
import com.cddgg.p2p.huitou.entity.Borrowersbase;
import com.cddgg.p2p.huitou.entity.Manualintegral;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Userrelationinfo;
import com.cddgg.p2p.huitou.model.RechargeModel;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.RegistrationService;
import com.cddgg.p2p.huitou.spring.service.VipInfoService;
import com.cddgg.p2p.huitou.spring.service.borrow.BorrowService;
import com.cddgg.p2p.huitou.util.DwzResponseUtil;

import freemarker.template.TemplateException;

/**
 * <p>
 * Title:UserInfoController
 * </p>
 * <p>
 * Description: 会员/借款人控制层
 * </p>
 */
@Controller
@RequestMapping("/userinfo")
@CheckLogin(value=CheckLogin.ADMIN)
public class UserInfoController {

    /** 引入log4j日志打印类 */
    private static final Logger LOGGER = Logger
            .getLogger(UserInfoController.class);

    /** 注入会员服务层 */
    @Resource
    private UserInfoServices userInfoServices;

    /** 注入excel文件生成工具 */
    @Resource
    private RechargeModel modelRecharge;
    
    @Resource
    private AutointegralQuery autointegralQuery;
    
    @Resource
    private BorrowService borrowService;
    
    /** vipinfoservice 特权会员*/
    @Resource
    private VipInfoService vipinfoservice;
    
    /** integralService 借款人积分*/
    @Resource
    private IntegralService integralService;
    
    /** borrowersApplyService 借款申请*/
    @Resource
    private BorrowersApplyService borrowersApplyService;
    
    /**
     * 用户注册接口
     */
    @Resource
    private RegistrationService registrationService;
    
    /** 操作执行成功后要刷新的页面 */
    private String pageId = "main30";
    
    /** 操作执行成功后要刷新的页面 借款人管理 */
    private String borrowpageId = "main39";

    /**
     * <p>
     * Title: queryPage
     * </p>
     * <p>
     * Description: 分页查询会员信息
     * </p>
     * 
     * @param userinfo
     *            查询条件
     * @param page
     *            分页对象
     * @param request
     *            HttpServletRequest
     * @param limit
     *            每页查询条数
     * @param start
     *            从第几行开始查询
     * @param isbrow
     *            是否是借款人
     * @return 查询结果转换后的json对象
     */
    @ResponseBody
    @RequestMapping("/querypage")
    public JSONObject queryPage(Userbasicsinfo userinfo, PageModel page,
            HttpServletRequest request, String limit, String start,
            String isbrow) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryPage(Userbasicsinfo userinfo=" + userinfo + ", PageModel page=" + page + ", HttpServletRequest request=" + request + ", String limit=" + limit + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        }

         if (StringUtil.isNotBlank(isbrow) &&
                 StringUtil.isNumberString(isbrow)) {
             userinfo.setLockTime(isbrow);
         }
        JSONObject resultjson = new JSONObject();

        JSONArray jsonlist = new JSONArray();

        // 每页显示条数
        if (StringUtil.isNotBlank(limit) && StringUtil.isNumberString(limit)) {
            page.setNumPerPage(Integer.parseInt(limit) > 0 ? Integer
                    .parseInt(limit) : 10);
        } else {
            page.setNumPerPage(10);
        }

        // 计算当前页
        if (StringUtil.isNotBlank(start) && StringUtil.isNumberString(start)) {
            page.setPageNum(Integer.parseInt(start) / page.getNumPerPage() + 1);
        }

        @SuppressWarnings("rawtypes")
        List datalist = userInfoServices.queryUserPage(page, userinfo);
        // endtime,vipendtime,isborrower,备用
        String titles = "id,username,realname,suminte,credit,createTime,lasttime,lastaddress,logincount,vipendtime,isborr,isLock,adminname";
        // 将查询结果转换为json结果集
        ArrayToJson.arrayToJson(titles, datalist, jsonlist);

        resultjson.element("rows", jsonlist);
        resultjson.element("total", page.getTotalCount());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryPage(Userbasicsinfo, PageModel, HttpServletRequest, String)方法结束OUTPARAM=" + resultjson); //$NON-NLS-1$
        }
        return resultjson;

    }

    /**
     * <p>
     * Title: dataToExcel
     * </p>
     * <p>
     * Description: 将会员信息导出excel
     * </p>
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @param ids
     *            要导出会员的编号
     */
    @RequestMapping("/table-to-excel")
    public void dataToExcel(HttpServletRequest request,
            HttpServletResponse response, String ids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("dataToExcel(HttpServletRequest request=" + request + ", HttpServletResponse response=" + response + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        String headers = "序号,用户名,真实姓名,积分,授信额度,注册时间,上次登录时间,上次登录地址,登录次数,会员类型（普通/特权）,会员期限,是否是借款人,是否禁用,推广编号,获得推广奖金,谁推广我";
        // 标题
        String[] header = StringUtil.sliptTitle(headers, null);
        int headerLength = header.length;

        // 查询所有信息
        @SuppressWarnings("unchecked")
        List<Object> datalist = userInfoServices.queryAll(ids);

        List<Map<String, String>> content = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        for (Object obj : datalist) {

            Object[] result = (Object[]) obj;
            map = new HashMap<String, String>();

            // 将对应的值存放在map中
            for (int i = 0; i < headerLength; i++) {
                map.put(header[i],
                        result[i] == null ? " " : result[i].toString());
            }

            content.add(map);
        }

        // 导出会员信息
        modelRecharge.downloadExcel("会员信息", null, header, content, response);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("dataToExcel(HttpServletRequest, HttpServletResponse)方法结束"); //$NON-NLS-1$
        }
    }

    /**
     * <p>
     * Title: queryBorrowPage
     * </p>
     * <p>
     * Description: 查询借款人列表
     * </p>
     * 
     * @param userinfo
     *            搜索条件
     * @param page
     *            分页参数
     * @param request
     *            HttpServletRequest
     * @param limit
     *            每页显示条数
     * @param start
     *            从几条数据开始查询
     * @return 查询结果转换的json对象
     */
    @ResponseBody   
    @RequestMapping("/queryborrowpage")
    public JSONObject queryBorrowPage(Userbasicsinfo userinfo, PageModel page,
            HttpServletRequest request, String limit, String start) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryBorrowPage(Userbasicsinfo userinfo=" + userinfo + ", PageModel page=" + page + ", HttpServletRequest request=" + request + ", String limit=" + limit + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        }

        JSONObject resultjson = new JSONObject();

        JSONArray jsonlist = new JSONArray();

        // 每页显示条数
        if (StringUtil.isNotBlank(limit) && StringUtil.isNumberString(limit)) {
            page.setNumPerPage(Integer.parseInt(limit) > 0 ? Integer
                    .parseInt(limit) : page.getNumPerPage());
        }

        if (StringUtil.isNotBlank(start) && StringUtil.isNumberString(start)) {
            page.setPageNum(Integer.parseInt(start) / page.getNumPerPage() + 1);
        }

        @SuppressWarnings("rawtypes")
        List datalist = userInfoServices.queryBrrowPage(page, userinfo);

        // json对象的键
        String titles = "id,username,suminte,addtime,credit,createtime,lasttime,logincount,endtime,vipendtime,ispass";

        // 数组集合转成josnarray
        ArrayToJson.arrayToJson(titles, datalist, jsonlist);
        resultjson.element("rows", jsonlist);
        resultjson.element("total", page.getTotalCount());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryBorrowPage(Userbasicsinfo, PageModel, HttpServletRequest, String)方法结束OUTPARAM=" + resultjson); //$NON-NLS-1$
        }
        return resultjson;
    }

    /**
     * <p>
     * Title: queryBorrowBasinfo
     * </p>
     * <p>
     * Description: 查询借款人基本信息
     * </p>
     * 
     * @param ids
     *            借款人编号
     * @param request
     *            HttpServletRequest
     * @return 借款人基本信息页面
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/query_borrow_baseinfo")
    public String queryBorrowBasinfo(String ids, HttpServletRequest request) {

        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {
            List<Object> objlist = userInfoServices.queryBorrowBasinfo(ids);

            if (null != objlist && !objlist.isEmpty()) {
                request.setAttribute("borrowBaseinfo", objlist.get(0));
            }
        }

        return "/WEB-INF/views/admin/usermanager/borrow_base_info";

    }

    /**
     * <p>
     * Title: queryPersonal
     * </p>
     * <p>
     * Description: 查询借款人个人资料
     * </p>
     * 
     * @param ids
     *            借款人编号
     * @param request
     *            HttpServletRequest
     * @return 借款人个人资料页面
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/query_personal")
    public String queryPersonal(String ids, HttpServletRequest request) {

        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {

            List<Object> objlist = userInfoServices.queryPersonal(ids);

            if (null != objlist && !objlist.isEmpty()) {
                request.setAttribute("borrowPersonal", objlist.get(0));
            }
        }

        return "/WEB-INF/views/admin/usermanager/borrow_personal_info";
    }

    /**
     * <p>
     * Title: queryContact
     * </p>
     * <p>
     * Description: 查询借款人联系信息
     * </p>
     * 
     * @param ids
     *            借款人编号
     * @param request
     *            HttpServletRequest
     * @return 返回页面
     */
    @RequestMapping("/query_contact")
    public String queryContact(String ids, HttpServletRequest request) {

        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {
            request.setAttribute("queryContact",
                    userInfoServices.queryContact(ids));
        }

        return "/WEB-INF/views/admin/usermanager/borrow_contact_info";
    }

    /**
     * <p>
     * Title: queryCompany
     * </p>
     * <p>
     * Description: 借款人单位资料查询
     * </p>
     * 
     * @param ids
     *            借款人编号
     * @param request
     *            HttpServletRequest
     * @return 借款人单位资料页面
     */
    @RequestMapping("/query_company")
    public String queryCompany(String ids, HttpServletRequest request) {
        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {
            request.setAttribute("queryCompany",
                    userInfoServices.queryCompany(ids));
        }

        return "/WEB-INF/views/admin/usermanager/borrow_company_info";
    }

    /**
     * <p>
     * Title: queryFinanes
     * </p>
     * <p>
     * Description: 查询借款人财务状况
     * </p>
     * 
     * @param ids
     *            借款人主键
     * @param request
     *            HttpServletRequest
     * @return 现在借款人财务状况的页面
     */
    @RequestMapping("/query_finanes")
    public String queryFinanes(String ids, HttpServletRequest request) {
        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {
            request.setAttribute("queryFinanes",
                    userInfoServices.queryFinanes(ids));
        }

        return "/WEB-INF/views/admin/usermanager/borrow_finanes_info";
    }

    /**
     * <p>
     * Title: queryOtherContact
     * </p>
     * <p>
     * Description: 借款人联保情况
     * </p>
     * 
     * @param ids
     *            借款人主键编号
     * @param request
     *            HttpServletRequest
     * @return 借款人联保情况展示页面
     */
    @RequestMapping("/query_other_contact")
    public String queryOtherContact(String ids, HttpServletRequest request) {
        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {
            request.setAttribute("queryOtherContact",
                    userInfoServices.queryOtherContact(ids));
        }

        return "/WEB-INF/views/admin/usermanager/borrow_other_contact_info";
    }

    /**
     * <p>
     * Title: disUser
     * </p>
     * <p>
     * Description: 禁用会员
     * </p>
     * 
     * @param ids
     *            要禁用的会员
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping("/disuser")
    public JSONObject disUser(String ids) {

        JSONObject json = new JSONObject();

        if (StringUtil.isNotBlank(ids)) {
            userInfoServices.updateUserStatus(ids, "1");
        }
        return DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS,
                "禁用会员成功", pageId, null);
    }

    /**
     * <p>
     * Title: enUser
     * </p>
     * <p>
     * Description: 启用会员
     * </p>
     * 
     * @param ids
     *            要启用会员的编号
     * @return 启用结果
     */
    @ResponseBody
    @RequestMapping("/enuser")
    public JSONObject enUser(String ids) {
        JSONObject json = new JSONObject();

        if (StringUtil.isNotBlank(ids)) {
            userInfoServices.updateUserStatus(ids, "0");
        }

        return DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS,
                "启用会员成功", pageId, null);
    }

    /**
     * <p>
     * Title: updateTime
     * </p>
     * <p>
     * Description: 跳转到修改会员期限的页面
     * </p>
     * 
     * @param ids
     *            要修改会员期限的编号
     * @param request
     *            HttpServletRequest
     * @return 返回修改结果
     */
    @RequestMapping("/update_user_date")
    public String updateTime(String ids, HttpServletRequest request) {

        request.setAttribute("ids", ids);

        return "/WEB-INF/views/admin/usermanager/update_user_date_page";
    }

    /**
     * <p>
     * Title: query
     * </p>
     * <p>
     * Description: 修改特权会员期限
     * </p>
     * 
     * @param ids
     *            要修改会员编号
     * @param endtime
     *            特权会员结束日期
     * @return 修改结果
     */
    @ResponseBody
    @RequestMapping("/update_member_date")
    public JSONObject updateMemberDate(String ids, String endtime) {

        JSONObject json = new JSONObject();

        if (userInfoServices.updateMemberDate(endtime, ids)) {
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS,
                    "修改会员期限成功", pageId, "closeCurrent");
        } else {
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR,
                    "修改会员期限失败", pageId, "closeCurrent");
        }

        return json;

    }

    /**
     * <p>
     * Title: queryById
     * </p>
     * <p>
     * Description: 修改会员信息前根据编号查询
     * </p>
     * 
     * @param ids
     *            要修改会员基本信息编号
     * @param request
     *            HttpServletRequest
     * @return 修改页面
     */
    @RequestMapping("/query_relation")
    public String queryById(String ids, HttpServletRequest request) {

        request.setAttribute("relation",
                userInfoServices.queryRelationById(ids));
        request.setAttribute("baseinfo",
                userInfoServices.queryBasicsInfoById(ids));
        return "/WEB-INF/views/admin/usermanager/update_user_relation_page";
    }

    /**
     * <p>
     * Title: update
     * </p>
     * <p>
     * Description: 修改会员信息
     * </p>
     * 
     * @param relation
     *            会员联系信息
     * @param baseinfo
     *            会员基本信息
     * @param ids
     *            会员基本信息主键
     * @return 修改结果 json对象
     */
    @ResponseBody
    @RequestMapping("/update_user")
    public JSONObject update(Userrelationinfo relation,
            Userbasicsinfo baseinfo, String ids) {

        JSONObject json = new JSONObject();

        // 修改联系信息
        userInfoServices.updateRelation(ids, relation);
        // 修改基本信息
        userInfoServices.udapteBasic(ids, baseinfo);

        DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS, "修改成功",
                pageId, "closeCurrent");

        return json;

    }

    /**
     * <p>
     * Title: updateCredit
     * </p>
     * <p>
     * Description: 修改借款人授信额度
     * </p>
     * 
     * @param ids
     *            借款人基本信息主键
     * @param credit
     *            授信额度
     * @return 返回成功或者失败 ‘n’表示失败，‘y’表示成功
     */
    @ResponseBody
    @RequestMapping("/update_credit")
    public String updateCredit(String ids, String credit) {

        String result = "n";

        if (userInfoServices.updateCredit(ids, credit) > 0) {
            result = "y";
        }
        return result;

    }

    /**
     * <p>
     * Title: addUserBasic
     * </p>
     * <p>
     * Description: 后台添加会员新
     * </p>
     * 
     * @param relation
     *            会员关联信息
     * @param userName
     *            用户名
     * @param password
     *            密码
     * @param request
     *            HttpServletRequest
     * @return 返回状态
     */
    @ResponseBody
    @RequestMapping("/add_user")
    public JSONObject addUserBasic(Userrelationinfo relation, String userName,
            String password, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        String message = "会员添加成功";

        // 调用会员注册
        try {
            Userbasicsinfo userbasic = registrationService.registrationSave(
                    userName, relation.getEmail(), password, null,
                    request);

            if (userbasic != null) {

                password = Md5Util.execute(password);

                if (userInfoServices.addUser(userbasic, relation.getCardId(),
                        relation.getPhone()) <= 0) {
                    message = "会员账号添加成功，但手机号码和身份证号码保存失败";
                }
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS,
                        message, pageId, "closeCurrent");
            } else {
                message = "会员添加失败";
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR,
                        message, pageId, "closeCurrent");
            }
        } catch (DataAccessException e) {
            LOGGER.error("添加会员出现异常:" + e);
        } catch (IOException e) {
            LOGGER.error("添加会员出现异常:" + e);
        } catch (TemplateException e) {
            LOGGER.error("添加会员出现异常:" + e);
        }

        return json;
    }
    
    /**
    * <p>Title: checkUserName</p>
    * <p>Description: 验证用户名的唯一性</p>
    * @param userName 延验证的字符
    * @return 验证结果
    */
    @ResponseBody
    @RequestMapping("/check_name")
    public boolean checkUserName(String userName){
        
        boolean flag=true;
        
        if(StringUtil.isNotBlank(userName)){
            flag=registrationService.checkUserName(userName);
        }
        return flag;
    }

    
    /**
    * <p>Title: checkEmail</p>
    * <p>Description: 验证会员邮箱的唯一性</p>
    * @param email 要验证的字符
    * @return 验证结果
    */
    @ResponseBody
    @RequestMapping("/chcke_email")
    public boolean checkEmail(String email){
        
        boolean flag=true;
        
        if(StringUtil.isNotBlank(email)){
            flag=registrationService.checkEmail(email);
        }
        
        return flag;
        
    }
    
    /**
    * <p>Title: getUserMoney</p>
    * <p>Description: 后台会员资金统计</p>
    * @param ids 会员编号
    * @param request HttpServletRequest
    * @return 数据展示页面
    */
    @RequestMapping("/get_user_money")
    public String getUserMoney(String ids,HttpServletRequest request){
        
        if(StringUtil.isNotBlank(ids)&&StringUtil.isNumberString(ids)){
            userInfoServices.getUserMoney(ids, request);
        }
        
        return "/WEB-INF/views/admin/usermanager/moneystatistics";
    }
    
    /**
    * <p>Title: openCustomer</p>
    * <p>Description: 保存参数，跳转到分配客服页面</p>
    * @param ids 会员编号
    * @param username 会员用户名
    * @param request HttpServletRequest
    * @return 分配客服页面
    */
    @RequestMapping("/open_customer")
    public String openCustomer(String ids,String username,HttpServletRequest request){
        
        request.setAttribute("ids", ids);
        
        request.setAttribute("username", username);
        
        return "/WEB-INF/views/admin/usermanager/customer";
        
    }
    
    /**
    * <p>Title: updateCustomer</p>
    * <p>Description: 分配客服</p>
    * @param uid 会员编号
    * @param adminid 客服编号
    * @return 修改结果
    */
    @ResponseBody
    @RequestMapping("/update_random")
    public JSONObject updateCustomer(String uid,String adminid){
        
        JSONObject json=new JSONObject();
        
        //判断参数是否为空或非数字
        if(StringUtil.isNotBlank(uid)&&StringUtil.isNotBlank(adminid)&&StringUtil.isNumberString(uid+adminid)){
            userInfoServices.updateCustomer(uid, adminid);
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS, "客服分配成功", pageId, "closeCurrent");
        }else{
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR, "参数错误，分配客服失败", pageId, "closeCurrent");
        }
        
        return json;
    }
    
    /**
    * <p>Title: getUsercount</p>
    * <p>Description: 统计会员数量</p>
    * @return
    */
    @ResponseBody
    @RequestMapping("/user_count")
    public String getUsercount(){
        
        return userInfoServices.getUsercount();
    }
    
    /******会员管理修改会员等级*****/
    /**
     * <p>
     * Title: openisvip
     * </p>
     * <p>
     * Description:打开会员修改页面
     * </p>
     * 
     * @param id
     *            标号
     * @param request
     * @return 要返回的页面
     */
    @RequestMapping("/openisvip")
    public String openisvip(String id, String username,
            HttpServletRequest request) {

        request.setAttribute("vid", id);
        request.setAttribute("vusername", username);
        // 查询到用户当前的会员等级
        Object object = vipinfoservice.isVip(Long.valueOf(id));
        request.setAttribute("vipendTime", object);
        return "/WEB-INF/views/admin/usermanager/vipinfo";

    }
    
    /**
     * <p>
     * Title: updatevipinfo
     * </p>
     * <p>
     * Description:修改会员等级
     * </p>
     * 
     * @param uid
     *            用户编号
     * @param vipTime
     *            结束时间
     * @return 结果
     */
    @ResponseBody
    @RequestMapping("/update_vip")
    public JSONObject updatevipinfo(String uid, String vipTime) {

        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isNotBlank(uid) && StringUtil.isNotBlank(vipTime)) {
                Userbasicsinfo user = userInfoServices.queryBasicsInfoById(uid);
                Object object = vipinfoservice.isVip(Long.valueOf(uid));
                if (object != null
                        && DateUtils.isBefore(Constant.DEFAULT_DATE_FORMAT,
                                vipTime, Constant.DEFAULT_TIME_FORMAT,
                                object.toString())) {
                    DwzResponseUtil.setJson(json,
                            Constant.HTTP_STATUSCODE_ERROR, "会员期限选择错误", pageId,
                            "closeCurrent");
                    return json;
                }
                userInfoServices.updatevip(user, vipTime);
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS,
                        "会员等级", pageId, "closeCurrent");
            } else {
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR,
                        "参数错误，会员等级修改失败", pageId, "closeCurrent");
            }
            return json;
        } catch (ParseException e) {
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR,
                    "参数错误，会员等级修改失败", pageId, "closeCurrent");
            return json;
        }

    }
    /**
     * <p>Title: openUserRecord</p>
     * <p>Description: 借出记录</p>
     * @param id 编号
     * @param request  请求
     * @return  页面
     */
     @RequestMapping("/openuserrecord")
     public String openUserRecord(String id,HttpServletRequest request){
         request.setAttribute("uid", id);
         return "/WEB-INF/views/admin/usermanager/user_record_list";
     }
     
     /**
      * <p>Title: queryUserLoanlist</p>
      * <p>Description: 借出记录的查询</p>
      * @param limit 每页多少
      * @param start  开始
      * @param page 分页
      * @param uid 用户编号
      * @param state 查询的状态  2 进行中  3  回款中  4 已经完成的  
      * @return 数据
      */
      @ResponseBody
      @RequestMapping("/queryuserlist")
      public JSONObject queryUserLoanlist(String limit, String start, PageModel page,String uid,int state) {

          JSONObject resultjson = new JSONObject();

          JSONArray jsonlist = new JSONArray();

          // 每页显示条数
          if (StringUtil.isNotBlank(limit) && StringUtil.isNumberString(limit)) {
              page.setNumPerPage(Integer.parseInt(limit) > 0 ? Integer
                      .parseInt(limit) : 10);
          } else {
              page.setNumPerPage(10);
          }

          // 计算当前页
          if (StringUtil.isNotBlank(start) && StringUtil.isNumberString(start)) {
              page.setPageNum(Integer.parseInt(start) / page.getNumPerPage() + 1);
          }

          List datalist =userInfoServices.queryuserrelistjxByuId(page, uid,state);
          String titles = "lrid,lsid,loanNumber,name,tenderTime,tenderMoney,rate,month,useDay,loanType,issueLoan";
          // 将查询结果转换为json结果集
          ArrayToJson.arrayToJson(titles, datalist, jsonlist);

          resultjson.element("rows", jsonlist);
          resultjson.element("total", page.getTotalCount());

          return resultjson;
      }
      
      /**
       * 资料上传
       * 
       * @param ids
       *            编号
       * @param request
       *            request
       * @return 资料上传
       */
      @RequestMapping("/query_data_upload")
      public String queryDataUpload(String ids, HttpServletRequest request) {
          request.setAttribute("list", userInfoServices.queryDataUpload(ids));
          return "/WEB-INF/views/admin/usermanager/borrow_data_upload";
      }
      
      /**
       * 借款人商业图片
       * 
       * @param ids
       *            编号
       * @param request
       *            request
       * @return 借款人商业图片
       */
      @RequestMapping("/query_stock_photo")
      public String queryStockPhoto(String ids, HttpServletRequest request) {
          request.setAttribute("list", userInfoServices.queryStockPhoto(ids));
          return "/WEB-INF/views/admin/usermanager/borrow_stock_photo";
      }
      
      /**
       * <p>
       * Title: openBorrowRecord
       * </p>
       * <p>
       * Description:借入记录
       * </p>
       * 
       * @param id
       *            编号
       * @param request
       *            响应
       * @return 页面
       */
      @RequestMapping("/openbborrowrecord")
      public String openBborrowRecord(long bid, HttpServletRequest request) {
          Borrowersbase borrow = borrowService.queryByUserInfo(bid);
          request.setAttribute("id", borrow.getUserbasicsinfo().getId());
          borrowService.queryBorrowrecord(borrow.getUserbasicsinfo(), request);
          return "/WEB-INF/views/admin/usermanager/borrow_record_list";
      }
      
      /**
       * <p>
       * Title: getUserManualIntegration
       * </p>
       * <p>
       * Description:设置积分
       * </p>
       * 
       * @param id
       *            用户编号
       * @param request
       *            响应
       * @return 信息
       */
      @RequestMapping("/getusermanualintegr")
      public String getUserManualIntegration(Long id, HttpServletRequest request) {

          Borrowersbase borrowersbase = borrowService.queryBorrowerbase(id);

          Manualintegral manualintegral = autointegralQuery
                  .queryManuaByuser(borrowersbase.getUserbasicsinfo());

          request.setAttribute("uid", borrowersbase.getUserbasicsinfo().getId());
          if (null != manualintegral && null != manualintegral.getAmountPoints()) {
              request.setAttribute("amounts", manualintegral.getAmountPoints());
          } else {
              request.setAttribute("amounts", 0);
          }
          request.setAttribute("manualintegral", manualintegral);

          return "/WEB-INF/views/admin/usermanager/manualIntegration";
      }
      
      /**
       * <p>
       * Title: openAutoinit
       * </p>
       * <p>
       * Description:跳转到自动积分页面
       * </p>
       * 
       * @param bid
       *            借款人编号
       * @param request
       *            响应
       * @return 页面
       */
      @RequestMapping("/openautoinit")
      public ModelAndView openAutoinit(String bid, HttpServletRequest request) {

          request.setAttribute("bid", bid);

          Borrowersbase borrower = borrowService.queryBorrowerbase(Long
                  .valueOf(bid));
          // 自动积分和
          request.setAttribute("sumautoint", autointegralQuery
                  .queryAutoSUMIntegral(borrower.getUserbasicsinfo()));

          return new ModelAndView(
                  "WEB-INF/views/admin/usermanager/borrow_integrat");
      }
      
      /**
       * <p>
       * Title: queryBorrowLoanlistjx
       * </p>
       * <p>
       * Description: 查询竟标中的借入记录
       * </p>
       * 
       * @param limit
       * @param start
       * @param page
       * @param id
       * @param state
       * @return 数据
       */
      @ResponseBody
      @RequestMapping("/queryborrowlist")
      public JSONObject queryBorrowLoanlist(String limit, String start,
              PageModel page, String uid, int state) {

          JSONObject resultjson = new JSONObject();

          JSONArray jsonlist = new JSONArray();

          // 每页显示条数
          if (StringUtil.isNotBlank(limit) && StringUtil.isNumberString(limit)) {
              page.setNumPerPage(Integer.parseInt(limit) > 0 ? Integer
                      .parseInt(limit) : 10);
          } else {
              page.setNumPerPage(10);
          }

          // 计算当前页
          if (StringUtil.isNotBlank(start) && StringUtil.isNumberString(start)) {
              page.setPageNum(Integer.parseInt(start) / page.getNumPerPage() + 1);
          }

          List datalist = userInfoServices.querybljxByuserId(page, uid, state);
          String titles = "id,loantitle,issueLoan,publishTime,rate,creditTime";
          // 将查询结果转换为json结果集
          ArrayToJson.arrayToJson(titles, datalist, jsonlist);

          resultjson.element("rows", jsonlist);
          resultjson.element("total", page.getTotalCount());

          return resultjson;
      }
      
      /**
       * <p>
       * Title: queryautointe
       * </p>
       * <p>
       * Description: 用户自动积分查询
       * </p>
       * 
       * @param limit
       *            limit
       * @param start
       *            开始
       * @param page
       *            分页
       * @param bid
       *            借款人id
       * @return 数据
       */
      @ResponseBody
      @RequestMapping("/queryautointe")
      public JSONObject queryautointe(String limit, String start, PageModel page,
              String bid) {

          JSONObject resultjson = new JSONObject();

          JSONArray jsonlist = new JSONArray();

          // 每页显示条数
          if (StringUtil.isNotBlank(limit) && StringUtil.isNumberString(limit)) {
              page.setNumPerPage(Integer.parseInt(limit) > 0 ? Integer
                      .parseInt(limit) : 10);
          } else {
              page.setNumPerPage(10);
          }

          // 计算当前页
          if (StringUtil.isNotBlank(start) && StringUtil.isNumberString(start)) {
              page.setPageNum(Integer.parseInt(start) / page.getNumPerPage() + 1);
          }

          List datalist = integralService.queryByuserId(page, bid);
          String titles = "id,loansignid,periods,realityintegral,Isover,preRepayMoney,predictintegral,loanNumber";
          // 将查询结果转换为json结果集
          ArrayToJson.arrayToJson(titles, datalist, jsonlist);

          resultjson.element("rows", jsonlist);
          resultjson.element("total", page.getTotalCount());

          return resultjson;
      }
      
      /**
    * <p>Title: queryApply</p>
    * <p>Description: 借款申请查询</p>
    * @param limit 每页查询条数
    * @param start 开始的位置
    * @param page 分页模型
    * @return 查询结果
    */
    @ResponseBody
    @RequestMapping("/apply_page")
    public JSONObject queryApply(String limit, String start, PageModel page){
          JSONObject resultjson = new JSONObject();

          JSONArray jsonlist = new JSONArray();

          // 每页显示条数
          if (StringUtil.isNotBlank(limit) && StringUtil.isNumberString(limit)) {
              page.setNumPerPage(Integer.parseInt(limit) > 0 ? Integer
                      .parseInt(limit) : 10);
          } else {
              page.setNumPerPage(10);
          }

          // 计算当前页
          if (StringUtil.isNotBlank(start) && StringUtil.isNumberString(start)) {
              page.setPageNum(Integer.parseInt(start) / page.getNumPerPage() + 1);
          }
          
          List<BorrowersApply> applyList=borrowersApplyService.queryPage(page);
          
          JSONObject json=null;
         
          if(null!=applyList&&!applyList.isEmpty()){
            for (BorrowersApply apply : applyList) {
                json = new JSONObject();
                json.element("id", apply.getId());
                json.element("realname", apply.getBorrowersbase()
                        .getUserbasicsinfo().getName());
                json.element("phone", apply.getBorrowersbase().getPhone());
                json.element("money", apply.getMoney());
                json.element("time", apply.getTime());
                switch (apply.getType().intValue()) {
                case 1:
                    json.element("type", "助人贷");
                    break;
                case 2:
                    json.element("type", "助企贷");
                    break;
                case 3:
                    json.element("type", "企业群联保贷");
                    break;
                default:
                    json.element("type", "投资人周转贷");
                    break;
                }
                json.element("status", apply.getStatus());
                json.element("remark", apply.getRemark());
                
                jsonlist.add(json);
            }
          }
          resultjson.element("rows", jsonlist);
          resultjson.element("total", page.getTotalCount());

          return resultjson;
      }
    
    /**
     * <p>
     * Title: saveOrUpdatemanualIntegration
     * </p>
     * <p>
     * Description: 保存或修改手动积分信息
     * </p>
     * 
     * @param manualin
     *            手动积分信息
     * @return 结果
     */
    @ResponseBody
    @RequestMapping("/saveOrUpdateman")
    public JSONObject saveOrUpdatemanualIntegration(Manualintegral manualin) {
        JSONObject json = new JSONObject();

        // 首先通过后台传过来的进行计算该用户的总和
        int amountpoints = autointegralQuery.getALLBYOneSerch(manualin);
        boolean bool = integralService.saveOrUpdateManualinte(manualin,
                amountpoints);
        if (bool) {
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS,
                    "手动积分设置成功", borrowpageId, "closeCurrent");
        } else {
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR,
                    "手动积分设置失败", borrowpageId, "closeCurrent");
        }
        return json;
    }
    
    /**
    * <p>Title: updateApplyStatus</p>
    * <p>Description: 审核借款人申请</p>
    * @param ids 申请编号
    * @param status 状态
    * @param remark 备注
    * @return 修改结果
    */
    @ResponseBody
    @RequestMapping("/apply_update")
    public JSONObject updateApplyStatus(String ids,String status,String remark){
        JSONObject json = new JSONObject();
        
        if(StringUtil.isNotBlank(ids)&&StringUtil.isNotBlank(status)&&StringUtil.isNumberString(ids+status)){
            try {
                remark=new String(remark.getBytes("ISO-8859-1"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
           if(borrowersApplyService.updateApplyStatus(ids, status,remark)){
               DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS, "修改成功", "main54", null);
           }else{
               DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR, "修改失败", "main54", null);
           }
        }else{
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR, "参数错误", "main54", null);
        }
        
        return json;
    }
    
    /**
     * <p>
     * Title: passBorrow
     * </p>
     * <p>
     * Description: 审核通过和不通过
     * </p>
     * 
     * @param ids
     *            审核通过
     * @param state
     *            状态
     * @return 结果
     */
    @ResponseBody
    @RequestMapping("/pass")
    public JSONObject passBorrow(int state, String ids) {

        JSONObject json = new JSONObject();

        if (StringUtil.isNotBlank(ids)) {
            if(!userInfoServices.ispassture(ids)){
                
                return DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR,
                        "只有未审核的借款人才能审核", borrowpageId, null);
            }
            boolean bool = userInfoServices.updateborrowState(ids, state);
            if (!bool) {
                return DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR,
                        "审核失败", borrowpageId, null);
            }
        }
       
        return  DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS,
                "借款人审核成功", borrowpageId, null);
    }
    
    /**
    * <p>Title: updateBorrowApplyStatus</p>
    * <p>Description: 批量修改借款申请</p>
    * @param ids 要修改的id
    * @param status 要修改的状态
    * @return 修改结果
    */
    @ResponseBody
    @RequestMapping("/update_applys")
    public JSONObject updateBorrowApplyStatus(String ids,String status){
        
        JSONObject json=new JSONObject();
        
        if(borrowersApplyService.updatesApply(ids, status)){
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS, "修改成功", "main54", null);
        }else{
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR, "修改失败", "main54", null);
        }
        
        return json;
    }
    
    /**
     * 下载文件
     * @param fileId String
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return boolean
     */
    @RequestMapping("download")
    @ResponseBody
    public void downloadFile(String id, HttpServletRequest request,
            HttpServletResponse response) {
        borrowService
                .downFile(Long.parseLong(id), request, response);
    }
}
