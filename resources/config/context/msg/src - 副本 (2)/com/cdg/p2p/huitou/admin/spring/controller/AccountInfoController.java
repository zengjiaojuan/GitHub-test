package com.cddgg.p2p.huitou.admin.spring.controller;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.util.ArrayToJson;
import com.cddgg.base.util.StringUtil;
import com.cddgg.p2p.huitou.admin.spring.service.AccountInfoService;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;

/**   
 * Filename:    AccountInfoController.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年2月11日 下午1:42:29   
 * Description:  会员流水账操作
 *    
 */

/**
 * <p>
 * Title:AccountInfoController
 * </p>
 * <p>
 * Description: 会员流水账后台控制层
 * </p>
 *         <p>
 *         date 2014年2月11日
 *         </p>
 */
@Controller
@RequestMapping("/admin_account")
@CheckLogin(value=CheckLogin.ADMIN)
public class AccountInfoController {

    /** 注入会员流水账后台服务层 */
    @Resource
    private AccountInfoService accountInfoService;

    /**
     * <p>
     * Title: query
     * </p>
     * <p>
     * Description: 后台查询会员流水账明细
     * </p>
     * 
     * @param limit
     *            每页查询多少条
     * @param start
     *            开始查询的位置
     * @param page
     *            分页模型
     * @param ids
     *            会员基本信息主键
     * @return 流水账信息
     */
    @ResponseBody
    @RequestMapping("/query_by_user")
    public JSONObject queryPageByUser(String limit, String start,
            PageModel page, String ids) {

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

        List accountList = accountInfoService.queryPageByUser(ids, page);
        String titles = "time,type,expenditure,income,money,explan";
        ArrayToJson.arrayToJson(titles, accountList, jsonlist);

        resultjson.element("rows", jsonlist);
        resultjson.element("total", page.getTotalCount());

        return resultjson;
    }

}
