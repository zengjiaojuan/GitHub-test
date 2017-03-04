package com.cddgg.p2p.huitou.admin.spring.controller.loan;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.util.ArrayToJson;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.normal.Validate;
import com.cddgg.p2p.huitou.admin.spring.service.loan.LoansignTypeService;
import com.cddgg.p2p.huitou.entity.LoansignType;

/**
* <p>Title:LoansignTypeConntroller</p>
* <p>Description: 标的类型控制层</p>
* <p>date 2014年2月27日</p>
*/
@Controller
@RequestMapping("/loansigntype")
public class LoansignTypeConntroller { 
    
    @Resource
    private LoansignTypeService loansignTypeService;
    
    /**
    * <p>Title: index</p>
    * <p>Description:  进入标的类型管理页面</p>
    * @return 进入标的类型管理页面
    */
    @RequestMapping(value = { "index", "/" })
    public ModelAndView index() {
        return new ModelAndView("WEB-INF/views/admin/loansign/loansigntype_list");
    }
    
    
    /**
    * <p>Title: queryPage</p>
    * <p>Description: 分页</p>
    * @param page  分页page
    * @param limit 每页多少
    * @param start  开始
    * @param loansignType 查询的条件 
    * @param request 请求
    * @return  返回JSONObject
     * @throws UnsupportedEncodingException 
    */
    @ResponseBody
    @RequestMapping("/querypage")
    @SuppressWarnings("rawtypes")
    public JSONObject queryPage(PageModel page, String limit, String start,LoansignType loansignType, HttpServletRequest request) throws UnsupportedEncodingException {

        JSONObject resultjson = new JSONObject();

        JSONArray jsonlist = new JSONArray();
        System.out.println(request.getMethod());
        if(request.getMethod().equals("POST")){
            if(Validate.emptyStringValidate(loansignType.getTypename())){
            	String Typename = new String(loansignType.getTypename().getBytes("iso-8859-1"),"UTF-8");
            	loansignType.setTypename(Typename);
            }
        }
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
        List datalist = loansignTypeService.queryPage(page,loansignType);
        String titles = "id,typename,mincredit,maxcredit,minmoney,maxmoney,minrate,maxrate,money";
       
        //将查询结果转换为json结果集
        ArrayToJson.arrayToJson(titles, datalist, jsonlist);
        
        resultjson.element("rows", jsonlist);
        resultjson.element("total", page.getTotalCount());
        return resultjson;
        
    }
    
    /**
    * <p>Title: seeDetails</p>
    * <p>Description: 查询详情页面</p>
    * @param id  标类型编号  可有可无
    * @param request  请求
    * @return  页面
    */
    @RequestMapping("/seeDetails")
    public ModelAndView seeDetails(@RequestParam(value = "id", defaultValue = "", required = false) String id,HttpServletRequest request) {
        
        LoansignType loansignType=new LoansignType();
        if (null != id && !id.trim().equals("")) {//编辑
            loansignType=loansignTypeService.queryOne(id);
            request.setAttribute("loansigntype", loansignType);
        }else{//查询
            request.setAttribute("loansigntype", loansignType);
        }
        return new ModelAndView("WEB-INF/views/admin/loansign/editloansigntype");
    }
    
    
    
    /**
    * <p>Title: edit</p>
    * <p>Description: 编辑或新增</p>
    * @param loansignType  要添加或修改的对象
    * @param request  请求
    * @return 是否成功
    */
    @ResponseBody
    @RequestMapping(value={"edit","/"})
    public  JSONObject edit( LoansignType loansignType,HttpServletRequest request){
        JSONObject json = new JSONObject();
        json.element("callbackType","closeCurrent");
        json.element("statusCode", "300");
        if(loansignType.getMincredit()>=loansignType.getMaxcredit()){//最小授信额度要小于最大授信额度
        	json.element("message", "最小授信额度应该小于最大授信额度");
            return json;
        }
        
        if(loansignType.getMinmoney()>=loansignType.getMaxmoney()){//最小借款期限要小于最大借款期限
        	json.element("message", "最小借款期限应该小于最大借款期限");
            return json;
        }
        
        if(loansignType.getMinrate()>=loansignType.getMaxrate()){//最低借款利率要小于最大利率
        	json.element("message", "最低借款利率应该小于最高借款利率");
            return json;
        }
        
        boolean bool =loansignTypeService.addoredit(loansignType);
        
        if(bool){
            // dwz返回json对象
            json.element("statusCode", "200");
            json.element("message", "更新成功");
            json.element("navTabId", "main43");
            return json;
        }else{
            json.element("message", "更新失败");
            return json;
        }
      }
    
    /**
     * 删除多个
     * @param ids 删除的编号
     * @param request 请求
     * @return 是否成功
    */
    @ResponseBody
    @RequestMapping(value={"delete","/"})
    public  boolean delete(String ids){
            return loansignTypeService.delete(ids);
      }
}
