package com.cddgg.p2p.huitou.admin.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.StringUtil;
import com.cddgg.p2p.huitou.entity.BorrowersApply;

/**   
 * Filename:    BorrowersApplyService.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年4月14日 下午4:00:08   
 * Description:  借款人申请记录
 *     
 */

@Service
public class BorrowersApplyService {

    @Resource
    private HibernateSupport dao;
    
    /**
    * <p>Title: queryPage</p>
    * <p>Description: 查询借款申请列表</p>
    * @param pageModel 分页模型
    * @return 查询结果
    */
    public List<BorrowersApply> queryPage(PageModel pageModel){
        
        String hql="from BorrowersApply order by status";
        
        return (List<BorrowersApply>) dao.pageListByHql(pageModel, hql, true, null);
        
    }
    
    /**
    * <p>Title: updateApplyStatus</p>
    * <p>Description: 修改借款申请状态</p>
    * @param ids 主键
    * @param status 状态
    * @return 修改结果 true、false
    */
    public boolean updateApplyStatus(String ids,String status,String remark){
        
        int result=0;
        boolean flag=true;
        
        String sql="UPDATE borrowers_apply SET `status`="+status+",remark='"+remark+"' WHERE id="+ids;
        
        result=dao.executeSql(sql);
        
        if(result<=0){
            flag=false;
        }
        return flag;
        
    }
    
    /**
    * <p>Title: updatesApply</p>
    * <p>Description: 批量修改借款申请状态</p>
    * @param ids 要修改状态的申请编号
    * @param status 要修改成的状态
    * @return 修改结果 true false
    */
    public boolean updatesApply(String ids,String status){
        
        boolean flag=false;
        
        if(StringUtil.isNotBlank(ids)&&StringUtil.isNotBlank(status)&&StringUtil.isNumberString(status)){
         // 根据“，”拆分字符串
            String[] newids = ids.split(",");
            // 要修改状态的编号
            String delstr = "";
            for (String idstr : newids) {
                // 将不是空格和非数字的字符拼接
                if (StringUtil.isNotBlank(idstr)
                        && StringUtil.isNumberString(idstr)) {
                    delstr += idstr + ",";
                }
            }
            
            if(StringUtil.isNotBlank(delstr)){
                String sql="UPDATE borrowers_apply SET borrowers_apply.`status`="+status
                        +",borrowers_apply.remark='批量处理' WHERE borrowers_apply.id IN ("+
                        delstr.substring(0,delstr.length()-1)+")";
                //修改状态 
                if(dao.executeSql(sql)>0){
                    flag=true;
                }
                 
            }
        }
        
        return flag;
        
    }
    
}
