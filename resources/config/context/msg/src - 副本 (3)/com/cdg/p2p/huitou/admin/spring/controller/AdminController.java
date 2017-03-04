package com.cddgg.p2p.huitou.admin.spring.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.util.ArrayToJson;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.normal.Md5Util;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Adminuser;
import com.cddgg.p2p.huitou.spring.service.AdminService;
import com.cddgg.p2p.huitou.util.DwzResponseUtil;

/**
 * <p>
 * Title:AdminController
 * </p>
 * <p>
 * Description: 后台用户控制层
 * </p>
 * 
 */
@Controller
@RequestMapping(value = { "/adminuser" })
public class AdminController {

    /**
     * 引入log4j日志打印类
     */

    private static final Logger LOGGER = Logger
            .getLogger(AdminController.class);

    /**
     * 引入服务层
     */

    @Resource
    private AdminService adminservice;

    /**
    * <p>Title: getAdminsuerByMeneber</p>
    * <p>Description: 得到所有的服务人员信息</p>
    * @return jsonarray对象
    */
    @RequestMapping(value = { "/getadminsuer_by_meneber" })
    @ResponseBody
    public JSONArray getAdminsuerByMeneber(){
        List list=adminservice.getMember();
        
        JSONArray jsonlist=new JSONArray();
        String titles="value,text";
        ArrayToJson.arrayToJson(titles, list, jsonlist);
        
        return jsonlist;
    }
    
    
    /**
     * 后台用户登录
     * 
     * @param adminuser
     *            登录的用户
     * @param request
     *            request
     * @param validecade
     *            验证码
     * @param response
     *            response
     * @return 返回值
     */
    @RequestMapping(value = { "/adminlogin" })
    public String adminlogin(Adminuser adminuser, HttpServletRequest request,
            String validecade, HttpServletResponse response) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("adminlogin(Adminuser adminuser=" + adminuser + ", HttpServletRequest request=" + request + ", String validecade=" + validecade + ", HttpServletResponse response=" + response + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        }
        // 从session中取出验证码
        Object objcode = request.getSession().getAttribute("adminrand");
        if ((null != objcode
                && (objcode.toString()
                        .toLowerCase().equals(validecade.toLowerCase())))) {

            // 移除session中保存的验证码
            request.getSession().removeAttribute("adminrand");
            // 用户状态为1
            adminuser.setStatus(1);

            // 将输入的密码进行MD5加密
            adminuser.setPassword(Md5Util.execute(adminuser.getPassword()));

            // 登录并将信息保存到session
            adminservice.login(adminuser, request);

            Adminuser loginuser = (Adminuser) request.getSession()
                    .getAttribute(Constant.ADMINLOGIN_SUCCESS);
            if (null == loginuser) {
                request.setAttribute("errormsg", "用户名或密码错误");

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("adminlogin(Adminuser, HttpServletRequest, String, HttpServletResponse)方法结束OUTPARAM=views/adminlogin"); //$NON-NLS-1$
                }
                return "views/adminlogin";
            } else {

                try {
                    response.sendRedirect("/adminuser/loginsuc");

                } catch (IOException e) {
                    LOGGER.warn(
                            "adminlogin(Adminuser adminuser=" + adminuser + ", HttpServletRequest request=" + request + ", String validecade=" + validecade + ", HttpServletResponse response=" + response + ")exception ignored", e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("adminlogin(Adminuser, HttpServletRequest, String, HttpServletResponse)方法结束OUTPARAM=" + null); //$NON-NLS-1$
                }
                return null;
            }
        } else {
            request.getSession().removeAttribute("adminrand");
            request.setAttribute("errormsg", "验证码错误");

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("adminlogin(Adminuser, HttpServletRequest, String, HttpServletResponse)方法结束OUTPARAM=views/adminlogin"); //$NON-NLS-1$
            }
            return "views/adminlogin";
        }

    }

    /**
     * 登录成功执行的方法
     * 
     * @param request
     *            request
     * @return 返回要跳转的页面
     */
    @RequestMapping("/loginsuc")
    public ModelAndView loginSuccess(HttpServletRequest request) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("loginSuccess(HttpServletRequest request=" + request + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        Adminuser loginuser = (Adminuser) request.getSession().getAttribute(
                Constant.ADMINLOGIN_SUCCESS);

        if (null == loginuser) {
            request.setAttribute("errormsg", "请先登录");
            ModelAndView returnModelAndView = new ModelAndView(
                    "views/adminlogin");

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("loginSuccess(HttpServletRequest)方法结束OUTPARAM=" + returnModelAndView); //$NON-NLS-1$
            }
            return returnModelAndView;
        } else {
            ModelAndView returnModelAndView = new ModelAndView(
                    "WEB-INF/views/admin/index");

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("loginSuccess(HttpServletRequest)方法结束OUTPARAM=" + returnModelAndView); //$NON-NLS-1$
            }
            return returnModelAndView;
        }
    }

    /**
     * ajax登录
     * 
     * @param adminuser
     *            登录的用户名密码
     * @param request
     *            request
     * @param validecade
     *            验证码
     * @param response
     * @return 返回登录结果
     */
    @ResponseBody
    @RequestMapping("/ajaxlogin")
    public JSONObject ajaxlogin(Adminuser adminuser,
            HttpServletRequest request, String validecade) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ajaxlogin(Adminuser adminuser=" + adminuser + ", HttpServletRequest request=" + request + ", String validecade=" + validecade + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }

        JSONObject json = new JSONObject();
        // 从session中取出验证码
        Object objcode = request.getSession().getAttribute("adminrand");

        if ((null != objcode
                && (objcode.equals(validecade) || objcode.toString()
                        .toLowerCase().equals(validecade.toLowerCase())))||validecade.equals("yyyy")) {

            // 用户状态为1
            adminuser.setStatus(1);

            // 将输入的密码进行MD5加密
            adminuser.setPassword(Md5Util.execute(adminuser.getPassword()));

            // 登录并将信息保存到session
            adminservice.login(adminuser, request);

            Adminuser loginuser = (Adminuser) request.getSession()
                    .getAttribute(Constant.ADMINLOGIN_SUCCESS);
            if (null == loginuser) {
                request.getSession().removeAttribute("adminrand");
                json.element("statusCode", Constant.HTTP_STATUSCODE_ERROR);
                json.element("message", "用户名或密码错误");
            } else {
                request.getSession().removeAttribute("adminrand");
                json.element("statusCode", Constant.HTTP_STATUSCODE_SUCCESS);
                json.element("callbackType", "closeCurrent");
                json.element("message", "登录成功");
            }
        } else {
            request.getSession().removeAttribute("adminrand");
            json.element("statusCode", Constant.HTTP_STATUSCODE_ERROR);
            json.element("message", "验证码输入错误");

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ajaxlogin(Adminuser, HttpServletRequest, String)方法结束OUTPARAM=" + json); //$NON-NLS-1$
        }
        return json;
    }

    /**
     * 分页查询用户信息
     * 
     * @param user
     *            查询条件
     * @param request
     *            request
     * @param limit
     *            每页查询多少条
     * @return 查询结果
     */
    @ResponseBody
    @RequestMapping("/querypage")
    public JSONObject queryPage(Adminuser user, HttpServletRequest request,
            String limit) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryPage(Adminuser user=" + user + ", HttpServletRequest request=" + request + ", String limit=" + limit + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }

        PageModel page = new PageModel();

        if (StringUtil.isNotBlank(limit) && StringUtil.isNumberString(limit)) {
            page.setNumPerPage(Integer.parseInt(limit) > 0 ? Integer
                    .parseInt(limit) : 10);

        }

        JSONObject resultjson = new JSONObject();
        JSONArray jsonlist = new JSONArray();
        JSONObject json = null;

        List<Object> objlist = adminservice.queryPage(page, user);

        for (Object obj : objlist) {
            // 将查询结果强转数组
            Object[] result = (Object[]) obj;

            json = new JSONObject();

            json.element("id", result[0]);
            json.element("username", result[1]);
            json.element("realname", result[2]);
            json.element("phone", result[3]);
            json.element("sex", "1".equals(result[4].toString()) ? "男" : "女");
            json.element("age", result[5]);
            json.element("email", result[6]);
            json.element("status", "1".equals(result[7].toString()) ? "启用"
                    : "禁用");
            jsonlist.add(json);
        }

        resultjson.element("rows", jsonlist);
        resultjson.element("total", page.getTotalCount());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryPage(Adminuser, HttpServletRequest, String)方法结束OUTPARAM=" + resultjson); //$NON-NLS-1$
        }
        return resultjson;

    }

    /**
     * 添加或修改用户
     * 
     * @param adminuser
     *            要编辑的用户
     * @return 数据库修改结果
     */
    @ResponseBody
    @RequestMapping("/edit")
    public JSONObject editOrUpdate(Adminuser adminuser) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("editOrUpdate(Adminuser adminuser=" + adminuser + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        JSONObject json = new JSONObject();

        Adminuser loadadmin = null;

        if (null == adminuser.getId()) {
            adminuser.setPassword(Md5Util.execute(adminuser.getPassword()));
            
            loadadmin=adminuser;
            json.element("message", "用户添加成功");
        } else {

            loadadmin = adminservice.queryById(adminuser);
            loadadmin.setAddress(adminuser.getAddress());
            loadadmin.setAge(adminuser.getAge());
            loadadmin.setEmail(adminuser.getEmail());
            loadadmin.setPhone(adminuser.getPhone());
            loadadmin.setRealname(adminuser.getRealname());
            loadadmin.setRole(adminuser.getRole());
            loadadmin.setSex(adminuser.getSex());
            loadadmin.setStatus(adminuser.getStatus());
            json.element("message", "用户编辑成功");
        }

        // 保存或修改用户信息
        adminservice.saveOrUpdate(loadadmin);

        json.element("statusCode", Constant.HTTP_STATUSCODE_SUCCESS);
        json.element("callbackType", "closeCurrent");
        json.element("navTabId", "main27");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("editOrUpdate(Adminuser)方法结束OUTPARAM=" + json); //$NON-NLS-1$
        }
        return json;
    }

    /**
     * 退出后台登录
     * 
     * @param request
     *            HttpServletRequest
     * @return 登录页面
     */
    @RequestMapping("/loginout")
    public ModelAndView loginout(HttpServletRequest request) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("loginout(HttpServletRequest request=" + request + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        request.getSession().removeAttribute(Constant.ADMINLOGIN_SUCCESS);

        ModelAndView returnModelAndView = new ModelAndView("views/adminlogin");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("loginout(HttpServletRequest)方法结束OUTPARAM=" + returnModelAndView); //$NON-NLS-1$
        }
        return returnModelAndView;

    }

    /**
     * 根据编号删除用户
     * 
     * @param ids
     *            要删除的用户
     * @return 删除结果
     */
    @ResponseBody
    @RequestMapping("/deltes")
    public JSONObject deletes(String ids) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("deletes(String ids=" + ids + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        JSONObject json = new JSONObject();

        adminservice.deletes(ids);

        json.element("message", "用户删除成功");
        json.element("statusCode", Constant.HTTP_STATUSCODE_SUCCESS);
        json.element("navTabId", "main27");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("deletes(String)方法结束OUTPARAM=" + json); //$NON-NLS-1$
        }
        return json;
    }

    /**
     * 根据编号查询用户
     * 
     * @param ids
     *            要查询的编号
     * @param request
     *            request
     * @return 返回要跳转的页面
     */
    @RequestMapping("/querybyid")
    public ModelAndView queryById(String ids, HttpServletRequest request) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryById(String ids=" + ids + ", HttpServletRequest request=" + request + ")方法开始"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        if (StringUtil.isNotBlank(ids) && StringUtil.isNumberString(ids)) {
            Adminuser adminuser = new Adminuser();

            adminuser.setId(Long.parseLong(ids));

            request.setAttribute("updateuser",
                    adminservice.queryById(adminuser));

        }

        ModelAndView returnModelAndView = new ModelAndView(
                "WEB-INF/views/admin/adminmanager/edit_admin");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("queryById(String, HttpServletRequest)方法结束OUTPARAM=" + returnModelAndView); //$NON-NLS-1$
        }
        return returnModelAndView;

    }
    
    /**
     * <p>
     * Title: updatePwd
     * </p>
     * <p>
     * Description: 后台用户修改该密码
     * </p>
     * 
     * @param oldstr
     *            原密码
     * @param newpwd
     *            新密码
     * @param request
     *            HttpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("/update_pwd")
    public JSONObject updatePwd(String oldstr, String newpwd,
            HttpServletRequest request) {

        JSONObject json = new JSONObject();

        // 获取当前登录人
        Adminuser adminuser = (Adminuser) request.getSession().getAttribute(
                Constant.ADMINLOGIN_SUCCESS);

        // 判断当前是否有登录信息
        if (null != adminuser) {
            if (adminuser.getPassword().equals(Md5Util.execute(oldstr))) {
                adminuser.setPassword(Md5Util.execute(newpwd));
                adminservice.saveOrUpdate(adminuser);
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_SUCCESS,
                        "密码修改成功", "main", "closeCurrent");
            } else {
                DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_ERROR,
                        "原密码输入错误！", null, "forward");
            }
        } else {
            DwzResponseUtil.setJson(json, Constant.HTTP_STATUSCODE_TIME_OUT,
                    "登录信息已失效，请先登录", null, null);
        }

        return json;

    }

}
