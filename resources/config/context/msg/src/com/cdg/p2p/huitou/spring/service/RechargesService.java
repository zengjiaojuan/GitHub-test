package com.cddgg.p2p.huitou.spring.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.pay.constant.PayURL;
import com.cddgg.p2p.pay.entity.RechargeInfo;
import com.cddgg.p2p.pay.payservice.RegisterService;
import com.cddgg.p2p.pay.util.ParseXML;
import com.cddgg.p2p.huitou.entity.Recharge;

import freemarker.template.TemplateException;

/**
 * 在线充值的业务处理
 * 
 * @author RanQiBing 2014-01-26
 * 
 */
@Service
public class RechargesService {
    /**
     * HibernateSupport
     */
    @Resource
    private HibernateSupport dao;

    /**
     * 记录充值信息
     * 
     * @param recharge
     *            充值信息
     */
    public void rechargeSave(Recharge recharge) {
        dao.save(recharge);
    }

    /**
     * 查询充值信息
     * 
     * @param id
     *            当前登录账户编号
     * @param beginTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return list 返回当前用户的充值信息
     */
    public List<Recharge> rechargeList(Long id, String beginTime, String endTime,PageModel page) {

        StringBuffer hql = new StringBuffer(
                "SELECT * FROM recharge r where r.user_id=" + id);

        if (null != beginTime && !"".equals(beginTime)) {
            hql.append(" and date_format(r.time,'%Y-%m-%d')>='").append(beginTime+"'");
        }

        if (null != endTime && !"".equals(endTime)) {
            hql.append(" and date_format(r.time,'%Y-%m-%d')<='").append(endTime+"'");
        }
        hql.append(" LIMIT ").append(page.firstResult()+","+page.getNumPerPage());
        List<Recharge> list = dao.findBySql(hql.toString(), Recharge.class);

        return list;
    }
    
    /**
     * 查询充值记录的条数
     * @param id
     * @return
     */
    public Object count(Long id){
    	Object obj=dao.findObjectBySql("SELECT COUNT(r.id) FROM recharge r where r.user_id=?", id);
    	return obj;
    }
    /**
     * 查询用户总的充值金额
     * @param id	当前登录账户编号
     * @return	返回总充值的金额
     */
    public Double totalAmount(Long id){
    	StringBuffer hql = new StringBuffer(
                "select sum(r.rechargeAmount) from Recharge r where r.userbasicsinfo.id=?");
    	return dao.queryNumberHql(hql.toString(), false, id);
    }
    
    /**
     * 根据充值流水号查询充值信息
     * 
     * @param orderNum
     *            充值流水号
     * @return Recharge
     */
    public Recharge getRecharge(String orderNum) {

        StringBuffer hql = new StringBuffer("from Recharge r where r.orderNum=")
                .append(orderNum);
        List<Recharge> list = dao.find(hql.toString());
        if (list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    /**
     * 对充值信息进行加密
     * 
     * @param recharge
     *            充值信息
     * @return 加密后的充值信息
     * @throws TemplateException
     * @throws IOException
     */
    public Map<String, String> encryption(RechargeInfo recharge)
            throws IOException, TemplateException {
        // 将充值信息转换成xml文件
        String registerCall = ParseXML.rechargeXml(recharge);
        // 加密后的信息
        Map<String, String> map = RegisterService.registerCall(registerCall);
        // 将访问地址放在map里
        map.put("url", PayURL.RECHARGETESTURL);

        return map;
    }

}