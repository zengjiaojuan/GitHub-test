package com.cddgg.p2p.huitou.admin.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;

/**   
 * Filename:    GeneralizeMoneyServices.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年2月11日 上午11:23:52   
 * Description:  后台会员推广金额服务层
 *   
 */
@Service
public class GeneralizeMoneyServices {
    
    /** 注入数据库底层操作层*/
    @Resource
    private HibernateSupport dao;
    
    /**
    * <p>Title: queryByUser</p>
    * <p>Description: 根据会员查询推广奖金信息</p>
    * @param page 分页信息
    * @param ids 会员编号
    * @return 返回查询结果 数组
    */
    public List queryByUser(PageModel page,String ids){
        
        StringBuffer sqlbuffer=new StringBuffer("SELECT generalize.id,generalize.adddate,");
        sqlbuffer.append("(SELECT userbasicsinfo.userName FROM userbasicsinfo WHERE userbasicsinfo.id = generalize.uid),");
        sqlbuffer.append(" generalizemoney.umoney,generalizemoney.bonuses FROM generalizemoney");
        sqlbuffer.append(" INNER JOIN userbasicsinfo ON generalizemoney.genuid=userbasicsinfo.id ");
        sqlbuffer.append(" INNER JOIN generalize ON generalize.uid=userbasicsinfo.id  WHERE userbasicsinfo.id="+ids);
    
        StringBuffer sqlcount=new StringBuffer("SELECT COUNT(generalizemoney.id) FROM generalizemoney ");
        sqlcount.append(" INNER JOIN userbasicsinfo ON generalizemoney.genuid=userbasicsinfo.id");
        sqlcount.append(" INNER JOIN generalize ON generalize.uid=userbasicsinfo.id where userbasicsinfo.id="+ids);
        
       return  dao.pageListBySql(page, sqlcount.toString(), sqlbuffer.toString(),null);
    }

}
