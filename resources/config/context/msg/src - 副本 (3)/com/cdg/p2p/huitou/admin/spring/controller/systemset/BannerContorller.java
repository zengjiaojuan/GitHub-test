package com.cddgg.p2p.huitou.admin.spring.controller.systemset;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.p2p.huitou.admin.spring.service.systemset.BannerService;
import com.cddgg.p2p.huitou.entity.Banner;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;

/**
 * banner控制层
 * 
 * @author ransheng
 * 
 */
@Controller
@RequestMapping(value = { "banner" })
@SuppressWarnings(value = { "rawtypes" })
@CheckLogin(value = CheckLogin.ADMIN)
public class BannerContorller {

    /**
     * banner图片接口
     */
    @Resource
    private BannerService bannerservice;

    /**
     * application
     */
    @Resource
    private ServletContext application;

    /**
     * banner图片分页展示信息
     * 
     * @param request
     *            请求对象
     * @param page
     *            分页对象
     * @return jsp路径
     */
    @RequestMapping(value = { "bannerpage", "/" })
    public ModelAndView bannerPage(
            @ModelAttribute(value = "PageModel") PageModel page,
            HttpServletRequest request) {
        // 取到banner图片信息条数
        Object count = bannerservice.getCount();
        page.setTotalCount(Integer.parseInt(count.toString()));
        // 分页查询banner图片信息
        List list = bannerservice.bannerPage(page);
        request.setAttribute("page", page);
        request.setAttribute("list", list);
        if (application.getAttribute("application_banner") == null) {
            application.setAttribute("application_banner",
                    bannerservice.query());
        }
        return new ModelAndView("/WEB-INF/views/admin/banner/bannerlist");
    }

    /**
     * 打开新增（编辑）banner图片页面
     * 
     * @param id
     *            图片编号
     * @param request
     *            请求对象
     * @return jsp路径
     */
    @RequestMapping(value = { "banneropen", "/" })
    public ModelAndView openbanner(
            @RequestParam(value = "id", defaultValue = "", required = false) String id,
            HttpServletRequest request) {
        if (!"".equals(id.trim())) {
            // 查询单条banner图片信息
            Banner banner = bannerservice.getOnly(id);
            request.setAttribute("banner", banner);
        }
        return new ModelAndView("WEB-INF/views/admin/banner/updatebanner");
    }

    /**
     * 新增（编辑）banner图片
     * 
     * @param banner
     *            banner图片对象
     * @param request
     *            request
     * @param response
     *            response
     */
    @RequestMapping(value = { "updatebanner", "/" })
    public void openBanner(@ModelAttribute(value = "Banner") Banner banner,
            HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        JSONObject json = new JSONObject();
        try {
            out = response.getWriter();
            // 新增（编辑）banner图片信息
            boolean b = bannerservice.saveORupdateBanner(banner, request);
            if (b) {
                bannerservice.resetAppBanner(application);
                json.element("statusCode", "200");
                json.element("message", "更新成功");
                json.element("navTabId", "main37");
                json.element("callbackType", "closeCurrent");
            } else {
                json.element("statusCode", "300");
                json.element("message", "请上传JPG、PNG、GIF图片类型");
            }
            out.print(json);
        } catch (Throwable e) {
            json.element("message", "更新失败");
            json.element("statusCode", "300");
            json.element("callbackType", "closeCurrent");
            e.getMessage();
            out.print(json);
        }

    }


    /**
     * 
     * 根据id删除banner图片
     * 
     * @param ids
     *            多个图片编号
     * @return dwzjson数据
     */
    @RequestMapping(value = { "deletebanner", "/" })
    @ResponseBody
    public JSONObject deletebanner(
            @RequestParam(value = "ids", defaultValue = "", required = false) String ids,HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            // 根据id删除banner图片
            bannerservice.deletebanner(ids,request);
            bannerservice.resetAppBanner(application);
            json.element("statusCode", "200");
            json.element("message", "删除成功");
            json.element("navTabId", "main37");
            return json;
        } catch (Throwable e) {
            json.element("message", "删除失败");
            json.element("statusCode", "300");
            e.getMessage();
            return json;
        }
    }

    /**
     * 图片顺序上移
     * 
     * @param id
     *            图片顺序号
     * @param request
     *            请求对象
     * @return dwzjson数据
     */
    @RequestMapping(value = { "bannerup", "/" })
    @ResponseBody
    public JSONObject bannerup(
            @RequestParam(value = "id", defaultValue = "", required = true) String id,
            HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            List list = bannerservice.queryByNumberUp(id);
            // 如果不是第一条，选择上移
            if (list != null && list.size() > 1) {
                if (list.get(1) != null
                        && !"".equals(list.get(1).toString().trim())) {
                    Integer obj_id = Integer.parseInt(((Object[])list.get(1))[0]+"");
                    Integer i = Integer.parseInt(((Object[])list.get(1))[1]+"");
                    Integer number = ((Banner) list.get(0)).getNumber();
                    
                    // 查询
                    Banner banner = (Banner) list.get(0);
                    banner.setNumber(i);

                    // 查询
                    Banner ber = bannerservice.getBannerByNume(obj_id);
                    ber.setNumber(number);

                    // 修改
                    bannerservice.update(banner);
                    // 修改
                    bannerservice.update(ber);
                    
                }
            }
            bannerservice.resetAppBanner(application);
            json.element("statusCode", "200");
            json.element("message", "上移成功");
            json.element("navTabId", "main37");
            return json;
        } catch (Throwable e) {
            json.element("statusCode", "300");
            json.element("message", "上移失败");
            json.element("navTabId", "main37");
            e.getMessage();
            return json;
        }

    }

    /**
     * 图片顺序下移
     * 
     * @param id
     *            图片顺序号
     * @param request
     *            请求对象
     * @return dwz json数据
     */
    @RequestMapping(value = { "bannerdown", "/" })
    @ResponseBody
    public JSONObject bannerdown(
            @RequestParam(value = "id", defaultValue = "", required = true) String id,
            HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            List list = bannerservice.queryByNumberDown(id);
            // 如果不是第一条，选择上移
            if (list != null && list.size() > 1) {
                if (list.get(1) != null
                        && !"".equals(list.get(1).toString().trim())) {
                    Integer obj_id = Integer.parseInt(((Object[])list.get(1))[0]+"");
                    Integer i = Integer.parseInt(((Object[])list.get(1))[1]+"");
                    Integer number = ((Banner) list.get(0)).getNumber();
                    
                    // 查询
                    Banner banner = (Banner) list.get(0);
                    banner.setNumber(i);

                    // 查询
                    Banner ber = bannerservice.getBannerByNume(obj_id);
                    ber.setNumber(number);

                    // 修改
                    bannerservice.update(banner);
                    // 修改
                    bannerservice.update(ber);
                }
            }
            bannerservice.resetAppBanner(application);
            json.element("statusCode", "200");
            json.element("message", "下移成功");
            json.element("navTabId", "main37");
            return json;
        } catch (Throwable e) {
            e.getMessage();
            json.element("statusCode", "300");
            json.element("message", "下移失败");
            json.element("navTabId", "main37");
            return json;
        }
    }

}
