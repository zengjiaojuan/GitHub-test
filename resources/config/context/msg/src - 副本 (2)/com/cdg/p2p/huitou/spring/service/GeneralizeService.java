package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.entity.Costratio;
import com.cddgg.p2p.huitou.entity.Generalizemoney;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.util.Arith;

/**   
 * Filename:    GeneralizeService.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年2月20日 上午8:38:17   
 * Description:  前台会员推广信息查询
 *   
 */

/**
* <p>Title:GeneralizeService</p>
* <p>Description: 会员推广服务层</p>
* <p>date 2014年2月20日</p>
*/
@Service
public class GeneralizeService {
    
    
    /** 数据库操作层*/
    @Resource
    private HibernateSupport dao;
    
    
    /**
    * <p>Title: queryGenlizePage</p>
    * <p>Description: 查询会员的推广信息</p>
    * @param uid 会员编号
    * @param page 分页信息
    * @return 查询到的结果集
    */
    public List queryGenlizePage(String uid,PageModel page){
        
        
        String dataSql="FROM Generalize where genuid="+uid;
        
        return dao.pageListByHql(page, dataSql, true);
        
    }
    
    /**
    * <p>Title: querygenMoenyPage</p>
    * <p>Description: 查询会员奖金获得记录</p>
    * @param uid 会员编号
    * @param page 分页信息
    * @return
    */
    public List<Generalizemoney> querygenMoenyPage(String uid,PageModel page){
        
        String dataSql="FROM Generalizemoney where genuid="+uid;
        
        return (List<Generalizemoney>) dao.pageListByHql(page, dataSql, true);
    }
    
    /**
     * <p>Title: updateBouns</p>
     * <p>Description: 计算推广人员的推广奖金</p>
     * @param userbasicsinfo 投资人
     */
     public void updateBouns(Userbasicsinfo userbasicsinfo){
         //费用比例
         Costratio costratio=null;
         
         //根据会员编号，查询推广人
         String sql="SELECT genuid FROM generalize WHERE uid=? AND rootid IS NULL";
         Object obj=dao.findObjectBySql(sql, userbasicsinfo.getId());
         
         //推广人不为空
         if(null!=obj){
             //查询当前会员第一笔投资
             String money_sql="SELECT IFNULL(expenditure,0.0) FROM accountinfo WHERE userbasic_id=? AND accounttype_id=3 AND expenditure>0 ORDER BY id LIMIT 1";
             Object money_obj=dao.findObjectBySql(money_sql, userbasicsinfo.getId());
             //投资金额不为空
             if(null!=money_obj){
                 //如果比例为空
                 if(null==costratio){
                     List<Costratio> list=dao.find("from Costratio");
                     if(list!=null&&!list.isEmpty()){
                         costratio=list.get(0);
                     }
                 }
                 
                 if(null!=costratio){
                     //计算推广奖金
                     double money=Arith.round(Arith.mul(Double.parseDouble(money_obj.toString()), costratio.getOneyear()), 2).doubleValue();
                     Generalizemoney generalizemoney=new Generalizemoney();
                     generalizemoney.setAddtime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
                     generalizemoney.setBonuses(money+"");
                     generalizemoney.setUmoney("0");
                     generalizemoney.setUname(userbasicsinfo.getUserName());
                     generalizemoney.setGenuid(Long.parseLong(obj.toString()));
                     //保存推广奖金
                     dao.save(generalizemoney);
                     //设置推广奖金已计算（如果rootid为空则表示奖金还没有添加到推广奖金表，如果不为空，则已经计算过，无须重复计算）
                     String update_sql="UPDATE generalize SET generalize.rootid =1 WHERE genuid=? AND uid=?";
                     dao.executeSql(update_sql, obj,userbasicsinfo.getId());
                 }
             }
         }
     }
    
}
