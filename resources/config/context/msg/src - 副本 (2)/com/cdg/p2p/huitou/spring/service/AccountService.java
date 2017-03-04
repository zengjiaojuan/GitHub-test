package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.StringUtil;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Accountinfo;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;

/**   
 * Filename:    AccountService.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年2月20日 下午4:17:33   
 * Description:  
 *   
 */

/**
* <p>Title:AccountService</p>
* <p>Description: 前台会员资金明细服务层</p>
* <p>date 2014年2月20日</p>
*/
@Service
public class AccountService {
    
    /** 数据库操作层*/
    @Resource
    private HibernateSupport dao;
    
    
    /**
    * <p>Title: queryPage</p>
    * <p>Description: 前台会员资金明细查询</p>
    * @param startime 开始时间
    * @param endtime 结束时间
    * @param page 分页参数
    * @param userBasic 会员基本信息
    * @return 结果集List
    */
    public List<Accountinfo> queryPage(String startime,String endtime,PageModel page,Userbasicsinfo userBasic){
        
        String hql="from Accountinfo where userbasicsinfo.id="+userBasic.getId();
        if(StringUtil.isNotBlank(startime)){
            
            if(StringUtil.isNotBlank(endtime)){
                hql+="and time BETWEEN '"+startime+" 00:00:00' AND '"+endtime+" 23:59:59'";
            }else{
                hql+="and time > '"+startime+" 00:00:00'"; 
            }
        }else{
            if(StringUtil.isNotBlank(endtime)){
                hql+="and time < '"+endtime+" 23:59:59'";
            }
        }
        
       return  (List<Accountinfo>) dao.pageListByHql(page, hql, true);
        
    }
}
