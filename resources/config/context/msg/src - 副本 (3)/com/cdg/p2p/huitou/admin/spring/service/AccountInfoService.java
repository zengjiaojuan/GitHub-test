package com.cddgg.p2p.huitou.admin.spring.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.StringUtil;

/**   
 * Filename:    AccountInfo.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年2月11日 上午11:49:57   
 * Description:  流水账操作服务层
 *   
 */

/**
* <p>Title:AccountInfo</p>
* <p>Description: 流水账操作服务层</p>
* <p>date 2014年2月11日</p>
*/
@Service
public class AccountInfoService {

    
    /** 注入数据库操作层*/
    @Resource
    private HibernateSupport dao;
    
    /**
    * <p>Title: queryPageByUser</p>
    * <p>Description: 后台查询会员的流水账明细</p>
    * @param ids 会员编号
    * @param page 分页信息
    * @return 返回查询结果
    */
    public List queryPageByUser(String ids,PageModel page){
        
        List accountList=new ArrayList();
        
        //判断会员主键是否为数字
        if(StringUtil.isNotBlank(ids)&&StringUtil.isNumberString(ids)){
            StringBuffer sqlBuffer=new StringBuffer("SELECT accountinfo.time,accounttype.`name`,expenditure,income,money,explan  FROM accountinfo");
            
            sqlBuffer.append(" INNER JOIN accounttype ON accounttype_id=accounttype.id WHERE accountinfo.userbasic_id="+ids);
            
            accountList= dao.pageListBySql(page, sqlBuffer.toString(), null);
        }
       
        
        return accountList;
    }
}
