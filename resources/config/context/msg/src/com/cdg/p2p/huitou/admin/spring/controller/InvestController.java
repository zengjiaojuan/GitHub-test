package com.cddgg.p2p.huitou.admin.spring.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.text.StrMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.normal.Validate;

@Controller
@RequestMapping(value = { "invest" })
public class InvestController {
    /*** 注入HibernateSupport*/
    @Resource
    HibernateSupport commondao;
    
    
    @RequestMapping(value = { "openInvestReords", "" })
    public ModelAndView openInvestReords(PageModel page, String investor,
            String investMoney, String time1, String time2,String serviceStaff,
            HttpServletRequest request) throws UnsupportedEncodingException {
        //对get提交的参数乱码进行处理
        if(request.getMethod().equals("GET")){
            if(Validate.emptyStringValidate(investor)){
                investor = new String(investor.getBytes("iso-8859-1"),"UTF-8");
            }
        }
        request.setAttribute("investReords", queryInvestRecords(page, investor, investMoney, time1, time2,serviceStaff));
        request.setAttribute("page", page);
        request.setAttribute("investor", investor);
        request.setAttribute("investMoney", investMoney);
        request.setAttribute("time1", time1);
        request.setAttribute("time2", time2);
        request.setAttribute("serviceStaff", serviceStaff);
        //统计p2p网贷平台投资总额
        String sumInvestMoney = "SELECT SUM(a.tenderMoney) "
        +"FROM loanrecord a INNER JOIN userbasicsinfo b ON a.userbasicinfo_id = b.id "
        +"INNER JOIN loansignbasics c ON a.loanSign_id = c.id "
        +"INNER JOIN loansign d ON a.loanSign_id = d.id ";
        List list = commondao.findBySql(sumInvestMoney);
        request.setAttribute("sumInvestMoney", list == null ? 0.00 : list.get(0));
        //统计所属客服下的投资总额
        String sumInvestMoney2 = "SELECT SUM(a.tenderMoney) "
        +"FROM loanrecord a INNER JOIN userbasicsinfo b ON a.userbasicinfo_id = b.id "
        +"INNER JOIN loansignbasics c ON a.loanSign_id = c.id "
        +"INNER JOIN loansign d ON a.loanSign_id = d.id "
        +"INNER JOIN userrelationinfo e ON a.userbasicinfo_id = e.id "
        +"INNER JOIN adminuser f ON e.adminuser_id = f.id "
        +"WHERE e.adminuser_id=4 ";
        List list2 = commondao.findBySql(sumInvestMoney2);
        request.setAttribute("sumInvestMoney2", list2 == null ? 0.00 : list2.get(0));
        return new ModelAndView("WEB-INF/views/admin/invest_records");
    }
    
    public List queryInvestRecords(PageModel page, String investor,
            String investMoney, String time1, String time2,String serviceStaff) {
        StringBuilder sqlInfo = new StringBuilder(
                "SELECT a.id,b.`name`,a.tenderMoney,a.tenderTime,c.loanTitle,d.issueLoan,"
                +"(SELECT e.name FROM userbasicsinfo e WHERE e.id = d.userbasicinfo_id) borrowerName, "
                +"(SELECT realname FROM adminuser WHERE adminuser.role_id IN (SELECT id FROM role WHERE roleName='服务人员') AND adminuser.id = e.adminuser_id ) serviceStaff "
                +"FROM loanrecord a INNER JOIN userbasicsinfo b ON a.userbasicinfo_id = b.id "
                +"INNER JOIN loansignbasics c ON a.loanSign_id = c.id "
                +"INNER JOIN loansign d ON a.loanSign_id = d.id "
                +"INNER JOIN userrelationinfo e ON a.userbasicinfo_id = e.id "
                + "INNER JOIN adminuser f ON e.adminuser_id = f.id where 1=1 ");
        StringBuilder sqlCount=new StringBuilder("SELECT COUNT(*) "
                +"FROM loanrecord a INNER JOIN userbasicsinfo b ON a.userbasicinfo_id = b.id "
                +"INNER JOIN loansignbasics c ON a.loanSign_id = c.id "
                +"INNER JOIN loansign d ON a.loanSign_id = d.id "
                +"INNER JOIN userrelationinfo e ON a.userbasicinfo_id = e.id "
                +"INNER JOIN adminuser f ON e.adminuser_id = f.id where 1=1 ");
        List<Object> param = new ArrayList<Object>();
        if (Validate.emptyStringValidate(investor)) {
            sqlInfo.append(" and b.`name` like  ? ");
            sqlCount.append(" and b.`name` like  ? ");
            param.add("%" + investor.trim() + "%");
        }
        if (Validate.emptyStringValidate(investMoney)) {
            sqlInfo.append(" and a.tenderMoney > ? ");
            sqlCount.append(" and a.tenderMoney > ?");
            param.add(investMoney.trim());
        }
        if (Validate.emptyStringValidate(time1) && Validate.emptyStringValidate(time2)) {
            sqlInfo.append(" and a.tenderTime > ? and a.tenderTime < ?");
            sqlCount.append(" and a.tenderTime > ? and a.tenderTime < ?");
            param.add(time1);
            param.add(time2);
        }
        if(Validate.emptyStringValidate(serviceStaff) && !serviceStaff.equals("-1")){
            sqlInfo.append(" and e.adminuser_id= ?");
            sqlCount.append(" and e.adminuser_id= ?");
            param.add(serviceStaff);
        }
        Object[] params = null;
        if (param.size() > 0) {
            params = param.toArray();
        }
        List list= commondao.pageListBySql(page, sqlCount.toString(), sqlInfo.toString(), null, params);
        return list;
    }
}
