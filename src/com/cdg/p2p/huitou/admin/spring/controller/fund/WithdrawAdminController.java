package com.cddgg.p2p.huitou.admin.spring.controller.fund;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cddgg.base.model.PageModel;
import com.cddgg.p2p.huitou.admin.spring.service.fund.WithdrawAdminService;
import com.cddgg.p2p.huitou.model.RechargeModel;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;

/**
 * 提现记录
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/withdraw")
@CheckLogin(value = CheckLogin.ADMIN)
@SuppressWarnings(value = { "rawtypes" })
public class WithdrawAdminController {

    /**
     * 提现接口
     */
    @Resource
    private WithdrawAdminService withdrawAdminService;

    /**
     * 导出excel
     */
    @Resource
    private RechargeModel rechargeModel;
    /**
     * 查询提现记录
     * 
     * @param page
     *            分页对象
     * @param beginDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @param userName
     *            用户名
     * @param request
     *            request
     * @return 提现记录jsp
     */
    @RequestMapping("/openRatio")
    public String queryPage(@ModelAttribute("PageModel") PageModel page,
            String beginDate, String endDate, String userName, HttpServletRequest request) {
        Integer count = withdrawAdminService.queryCount(beginDate, endDate,userName);
        page.setTotalCount(count);
        List list = withdrawAdminService.queryPage(page, beginDate, endDate, userName);
        request.setAttribute("list", list);
        request.setAttribute("page", page);
        return "/WEB-INF/views/admin/withdraw/withdrawlist";
    }
    
    @RequestMapping("/export_excel")
    public void exportExcel(String ids,HttpServletRequest request,
    		HttpServletResponse response){
    	String[] headers=new String[]{
    			"姓名","用户名","提现金额","提现费","订单号","ips订单号","提现时间","备注"};
    	//数据源
    	List list=withdrawAdminService.queryById(ids);
    	List<Map<String,String>> content=new ArrayList<Map<String,String>>();
    	Map<String,String> map=null;
    	for(Object obj:list){
    		Object[] str=(Object[])obj;
    		map=new HashMap<String,String>();
    		map.put("姓名", str[1]+"");
    		map.put("用户名", str[2] + "");
            map.put("提现金额", str[3] + "");
            map.put("提现费", str[4]==null?"":str[4]+"");
            map.put("订单号", str[5] + "");
            map.put("ips订单号", str[6] + "");
            map.put("提现时间", str[7] + "");
            map.put("备注", str[8]==null?"":str[8]+"");
            content.add(map);
    	}
    	// 导出充值记录
        rechargeModel.downloadExcel("充值记录", null, headers, content, response, request);
    }
}