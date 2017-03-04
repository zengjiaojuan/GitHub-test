package com.cddgg.p2p.huitou.spring.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.pomo.web.page.model.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.core.service.CityInfoService;
import com.cddgg.p2p.huitou.admin.spring.service.ColumnManageService;
import com.cddgg.p2p.huitou.admin.spring.service.creditor.ProductService;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.constant.enums.ENUM_PUBLISH_STATE;
import com.cddgg.p2p.huitou.constant.enums.ENUM_SHOW_STATE;
import com.cddgg.p2p.huitou.entity.Product;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.ProductVerification;
import com.cddgg.p2p.huitou.spring.service.LoanInfoService;
import com.cddgg.p2p.huitou.spring.service.VisitorService;

/**
 * 普通请求
 * 
 * @author dgg
 * 
 */
@RequestMapping({ "visitor", "/" })
@Controller
public class VisitorController {

    /**
     * HibernateSupport
     */
    @Resource
    HibernateSupport dao;

    /**
     * VisitorService
     */
    @Resource
    VisitorService visitorservice;
    
    @Resource
	private LoanInfoService loanInfoService;
    
    
    /**
     * CityInfoService
     */
    @Resource
    CityInfoService cityInfoService;

    /**
     * ColumnManageService
     */
    @Resource
    ColumnManageService columnservice;

    /**
     * ProductService
     */
    @Resource
    ProductService productService;

    /**
     * 根据省份id查询城市
     * 
     * @param request
     *            请求
     * @param provinceId
     *            省份编号
     * @return url
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/querycity")
    public String queryCity(HttpServletRequest request, Long provinceId) {
        List list = cityInfoService.queryCityByProvince(provinceId);
        request.setAttribute("list", list);
        return "WEB-INF/views/visitor/city";
    }

    /**
     * 跳转到登陆界面
     * 
     * @return 登陆界面
     */
    @RequestMapping("to-login")
    public String toLogin() {
        return "WEB-INF/views/visitor/login";
    }

    /**
     * 跳转到注册界面
     * 
     * @return 注册界面
     */
    @RequestMapping("to-regist")
    public String toRegist() {
        return "WEB-INF/views/visitor/regist";
    }

    /**
     * 跳转到我的认购记录
     * 
     * @return 页面
     */
    @RequestMapping("my-product-list")
    public String toMyProductList() {
        return "WEB-INF/views/member/product-list";
    }

    // /**
    // * 页面跳转
    // *
    // * @param pageUrl
    // * (页面路径)
    // * @param request 请求
    // * @return 返回路径
    // */
    // @RequestMapping("jumpToPage")
    // @Deprecated
    // public String jumpToPage(String pageUrl, HttpServletRequest request) {
    // LOG.error("该方法存在安全问题，请勿使用！URL:"+request.getRequestURI());
    // return "WEB-INF/views/" + pageUrl;
    // }

    /**
     * 产品列表
     * 
     * @return 列表
     */
    @RequestMapping("/product-list")
    public Object toProductList() {

        return "WEB-INF/views/visitor/product-list";
    }

    /**
     * 产品列表
     * 
     * @param page
     *            分页
     * @return 列表
     */
    @RequestMapping("/page-product-list")
    @ResponseBody
    public Object pageProductList(Page page) {

        page.setData(dao
                .pageListByHql(
                        page,
                        "SELECT a.id,a.name,a.productType.dayDuring,a.ratePercentYear*100,a.productType.ratePayType,a.timePublish,a.investedMoeny,(a.investMax-a.investedMoeny),a.productType.dayType FROM Product a WHERE a.shows=? AND a.status=?",
                        false, ENUM_SHOW_STATE.TRUE.ordinal(),
                        ENUM_PUBLISH_STATE.PUBLISH.ordinal()));

        return page;

    }

    /**
     * 跳转到单页
     * 
     * @param topicId
     *            一级栏目id
     * @param request
     *            HttpServletRequest
     * @return 返回单页页面
     */
    @RequestMapping("showDeputPage")
    public String showDeputPage(String topicId, HttpServletRequest request) {
        request.setAttribute("topicId", topicId);
        return "WEB-INF/views/visitor/communal/danye";
    }

    /**
     * 跳转到产品详细信息
     * 
     * @param product
     *            产品id
     * @param request
     *            HttpServletRequest
     * @return 返回产品信息页面路径
     */
    @RequestMapping("product-info")
    @ProductVerification
    public String productInfo(Product product, HttpServletRequest request) {

        product = dao.get(Product.class, product.getId());

        request.setAttribute("data", product);
        request.setAttribute(
                "list",
                dao.fillQuery(
                        dao.getSession().getNamedQuery("product_info_history"),
                        product.getId()).list());

        visitorservice.putProductInfoRightCity(request);

        return "WEB-INF/views/visitor/product-info";
    }

    /**
     * 显示首页
     * 
     * @param request
     *            HttpServletRequest
     * @return 返回首页路径
     */
    @RequestMapping({ "/index", "/" })
    public String indexShow(HttpServletRequest request) {
        return initIndex(request);
    }

    /**
     * 初始化首页数据
     * @param request HttpServletRequest
     * @return String
     */
    public String initIndex(HttpServletRequest request){
        request.setAttribute("artList01", columnservice.queryArticle("公告"));
        request.setAttribute("artList02", columnservice.queryArticle("新闻"));
        request.setAttribute("artList03", columnservice.queryArticle("风采"));
        request.setAttribute("artList04", columnservice.queryArticle("常见问题"));
        request.setAttribute("artList05", columnservice.queryArticle("理财新闻"));
        request.setAttribute("artList06", columnservice.queryArticle("理财学堂"));
        
        // 加载首页产品列表
        List<Object[]> loansignList=productService.initTop5latest();
        long lid=0l;
		for(int i=0;i<loansignList.size();i++){
			lid=Long.parseLong(loansignList.get(i)[2].toString());
			loansignList.get(i)[2]=loanInfoService.getCreditRating(lid);
		}
		
        request.setAttribute("loanlist5", loansignList);
       
        return "WEB-INF/views/visitor/index";
    }
    
    /**
     * 注册
     * 
     * @param user
     *            用户
     * @param request
     *            HttpServletRequest
     * @return 返回首页路径
     * @throws Exception
     *             抛出异常
     */
    @RequestMapping("/regist")
    public String regist(Userbasicsinfo user, HttpServletRequest request)
            throws Exception {
        user.setCreateTime(DateUtils.format("yyyy:MM:dd HH:mm:ss"));// 设置注册时间
        user.setLockTime(DateUtils.format("yyyy:MM:dd HH:mm:ss"));// 设置被锁时间为当前时间
        try {
            user.getUserrelationinfo().setUserbasicsinfo(user);
            visitorservice.regist(user);
            request.setAttribute("userName", user.getUserName());
            request.setAttribute("result", 1);
            request.setAttribute("msg", "注册成功！");
            return "WEB-INF/views/member/myIndex";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", 0);
            request.setAttribute("msg", "注册失败！");
            return "WEB-INF/views/visitor/index";
        }

    }

    /**
     * 登陆
     * 
     * @param userName
     *            用户名
     * @param password
     *            用户密码
     * @param request
     *            HttpServletRequest
     * @return String
     */
    @RequestMapping("/login")
    public String login(String userName, String password,
            HttpServletRequest request) {
        try {
            Userbasicsinfo user = visitorservice.login(userName, password);
            if (user != null) {
                request.setAttribute("userName", userName);
                request.setAttribute("result", 1);
                request.setAttribute("msg", "登陆成功！");
                request.getSession()
                        .setAttribute(Constant.ATTRIBUTE_USER, user);
                return "WEB-INF/views/member/myIndex";
            } else {
                request.setAttribute("result", 0);
                request.setAttribute("msg", "登陆失败！");
                return "WEB-INF/views/visitor/index";
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", 0);
            request.setAttribute("msg", "登陆失败！");
            return "WEB-INF/views/visitor/index";
        }
    }

    /**
     * 验证用户名是否重复
     * 
     * @param fieldId
     *            文本框id
     * @param fieldValue
     *            文本框值
     * @return List<Object>
     */
    @RequestMapping("/checkOnlyUsername")
    @ResponseBody
    public List<Object> checkOnlyUsername(String fieldId, String fieldValue) {

        List list = new ArrayList();
        boolean flag = visitorservice.checkUserName(fieldValue);
        list.add(fieldId);
        list.add(flag);
        return list;
    }

    /**
     * 验证用户名是否重复
     * 
     * @param fieldId
     *            文本框id
     * @param fieldValue
     *            文本框值
     * @return List<Object>
     */
    @RequestMapping("/checkOnlyEmail")
    @ResponseBody
    public List<Object> checkOnlyEmail(String fieldId, String fieldValue) {

        List list = new ArrayList();
        boolean flag = visitorservice.checkUserEmail(fieldValue);
        list.add(fieldId);
        list.add(flag);
        return list;
    }

    /**
     * 验证验证码是否正确
     * 
     * @param mes_code
     *            输入的验证码
     * @param request
     *            HttpServletRequest
     * @return boolean
     */
    @RequestMapping("/checkValideCode")
    @ResponseBody
    public boolean checkValideCode(String mes_code, HttpServletRequest request) {

        List list = new ArrayList();
        String valideCode = request.getSession().getAttribute("user_login")
                .toString();
        if (valideCode.equalsIgnoreCase(mes_code)) {
            return true;
        } else {
            return false;
        }
    }

    //
    //
    // @RequestMapping("/login")
    // public String login(Userinfo userInfo,String login_check_code,String
    // url,HttpServletRequest request,HttpServletResponse response) throws
    // Exception{
    //
    // String result = "visitor/login";
    //
    // if(!checkCode(request,Constant.ATTRIBUTE_LOGIN_CHECK_CODE,login_check_code))
    // return result;
    //
    // if(service_user.userLogin(userInfo,request,response)){
    //
    // if(url==null || url.trim().length()>0) url = Constant.URL_SUCCESS_LOGIN;
    //
    // response.sendRedirect(url);
    //
    // result = null;
    // }
    //
    // return result;
    // }

    /**
     * 验证验证码
     * 
     * @param request
     *            HttpServletRequest
     * @param name
     *            名字
     * @param value
     *            值
     * @return boolean
     */
    private boolean checkCode(HttpServletRequest request, String name,
            String value) {

        Object obj = request.getSession().getAttribute(name);
        if (obj == null || value == null
                || !value.equalsIgnoreCase((String) obj)) {// 校验验证码
            request.setAttribute(Constant.ATTRIBUTE_MSG, "验证码错误！");
            return false;
        }
        request.getSession().removeAttribute(name);
        return true;

    }
}
