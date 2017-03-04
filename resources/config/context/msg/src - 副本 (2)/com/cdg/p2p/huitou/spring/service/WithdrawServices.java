package com.cddgg.p2p.huitou.spring.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pomo.web.page.model.Page;
import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.huitou.entity.Withdraw;
import com.cddgg.p2p.pay.constant.PayURL;
import com.cddgg.p2p.pay.entity.WithdrawalInfo;
import com.cddgg.p2p.pay.payservice.RegisterService;
import com.cddgg.p2p.pay.util.ParseXML;

import freemarker.template.TemplateException;

/**
 * 提现业务处理
 * @author RanQiBing 2014-02-13
 *
 */
@Service
public class WithdrawServices {
    
    @Resource
    private HibernateSupport dao;
    /**
     * 查询用户所有的提现信息
     * @return 返回一个提现信息的集合
     */
    public List<Withdraw> queryList(Page page,Long id){
        String hql = "select w.id,w.withdrawAmount,w.strNum,w.pIpsBillNo,w.time from Withdraw w where w.userbasicsinfo.id=?";
        List<Withdraw> list = dao.pageListByHql(page, hql, false, id);
        return list;
    }
    /**
     * 添加用户提现信息
     * @param withdraw 用户提现信息
     */
    public void save(Withdraw withdraw){
        dao.save(withdraw);
    }
    /**
     * 对信息进行加密处理
     * @param widthdrawal 提现信息
     * @return 返回加密后的提现信息
     * @throws TemplateException 
     * @throws IOException 
     */
    public Map<String,String> encryption(WithdrawalInfo widthdrawal) throws IOException, TemplateException{
      //将充值信息转换成xml文件
        String registerCall = ParseXML.withdrawalXml(widthdrawal);
        System.out.println(registerCall);
      //加密后的信息
        Map<String,String> map = RegisterService.registerCall(registerCall);
        map.put("url",PayURL.WITHDRAWALTESTURL);
        
        return map;
    }
    /**
     * 根据ips提现编号查询提现信息
     * @param ipsNo
     * @return 返回提现对象
     */
    public Withdraw withdrawIps(String ipsNo){
        String hql = "from Withdraw w where w.pIpsBillNo=?";
        List<Withdraw> with = dao.find(hql, ipsNo);
        if(with.size()>0){
            return with.get(0);
        }
        return null;
    }
    
    /**
     * 用户的提现信息
     * @param id
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<Withdraw> withdrawList(Long id, String beginTime, String endTime,PageModel page) {

        StringBuffer hql = new StringBuffer(
                "SELECT * FROM withdraw w where w.user_id=" + id);

        if (null != beginTime && !"".equals(beginTime)) {
            hql.append(" and date_format(w.time,'%Y-%m-%d')>='").append(beginTime+"'");
        }

        if (null != endTime && !"".equals(endTime)) {
            hql.append(" and date_format(w.time,'%Y-%m-%d')<='").append(endTime+"'");
        }
        hql.append(" LIMIT "+page.firstResult()+","+page.getNumPerPage());
        List<Withdraw> list = dao.findBySql(hql.toString(), Withdraw.class);

        return list;
    }
    
    /**
     * 根据id和时间查询提现记录的总条数
     * @param id
     * @param beginTime
     * @param endTime
     * @return
     */
    public Object count(Long id,String beginTime, String endTime){
    	StringBuffer hql=new StringBuffer("SELECT COUNT(w.id) FROM withdraw w WHERE w.user_id=?");
    	if (null != beginTime && !"".equals(beginTime)) {
            hql.append(" and date_format(w.time,'%Y-%m-%d')>='").append(beginTime+"'");
        }

        if (null != endTime && !"".equals(endTime)) {
            hql.append(" and date_format(w.time,'%Y-%m-%d')<='").append(endTime+"'");
        }
    	Object obj=dao.findObjectBySql(hql.toString(), id);
    	return obj;
    }
}
