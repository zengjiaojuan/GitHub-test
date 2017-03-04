package com.cddgg.p2p.huitou.admin.spring.service.loan;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.StringUtil;
import com.cddgg.p2p.huitou.entity.LoansignType;


/**
* <p>Title:LoansignTypeService</p>
* <p>Description: 标的类型服务层</p>
* <p>date 2014年2月27日</p>
*/
@Service
public class LoansignTypeService {

    /** 注入数据库服务层*/
    @Resource
    private HibernateSupport dao;

    
    /**
    * <p>Title: queryPage</p>
    * <p>Description: 分页查询 </p>
    * @param page 分页
    * @param loansignType 查询条件
    * @return   标的类型的集合
    */
    @SuppressWarnings("rawtypes")
    public List queryPage(PageModel page,LoansignType loansignType) {
        List datalist = new ArrayList();
        StringBuffer countsql=new StringBuffer("SELECT count(1) from loansign_type");
        StringBuffer listsql=new StringBuffer("SELECT id,typename,mincredit,maxcredit,minmoney,maxmoney,minrate*100,maxrate*100,money FROM loansign_type");
        
        
        //类型名称，最小借款额度，最大借款额度，最小借款期限，最大借款期限，最低借款利率，最高借款利率，借款标期
        if(!StringUtil.isBlank(loansignType.getTypename())){
            countsql.append("  and typename LIKE '%").append(loansignType.getTypename()).append("%'");
            listsql.append("  and typename LIKE '%").append(loansignType.getTypename()).append("%'");
        }
        if(null!=loansignType.getMincredit()&&!StringUtil.isBlank(loansignType.getMincredit().toString())){
            countsql.append("  and mincredit>=").append(loansignType.getMincredit());
            listsql.append("  and mincredit>=").append(loansignType.getMincredit());
        }
        if(null!=loansignType.getMaxcredit()&&!StringUtil.isBlank(loansignType.getMaxcredit().toString())){
           countsql.append("  and maxcredit<=").append(loansignType.getMaxcredit());
           listsql.append("  and maxcredit<=").append(loansignType.getMaxcredit());
        }
        if(null!=loansignType.getMinmoney()&&StringUtil.isNumberString(loansignType.getMinmoney().toString())){
            countsql.append("  and minmoney>= ").append(loansignType.getMinmoney());
            listsql.append("  and minmoney>= ").append(loansignType.getMinmoney());
        }
        if(null!=loansignType.getMaxmoney()&&StringUtil.isNumberString(loansignType.getMaxmoney().toString())){
            countsql.append("  and maxmoney<= ").append(loansignType.getMaxmoney());
            listsql.append("  and maxmoney<= ").append(loansignType.getMaxmoney());
        }
        
        if(null!=loansignType.getMinrate()&&!StringUtil.isBlank(loansignType.getMinrate().toString())){
            countsql.append("  and minrate*100>= ").append(loansignType.getMinrate());
            listsql.append("  and minrate*100>= ").append(loansignType.getMinrate());
        }
        if(null!=loansignType.getMaxrate()&&!StringUtil.isBlank(loansignType.getMaxrate().toString())){
            countsql.append("  and maxrate*100<= ").append(loansignType.getMaxrate());
            listsql.append("  and maxrate*100<= ").append(loansignType.getMaxrate());
        }
        
        if(null!=loansignType.getMoney()&&StringUtil.isNumberString(loansignType.getMoney().toString())){
            countsql.append("  and money= ").append(loansignType.getMoney());
            listsql.append("  and money= ").append(loansignType.getMoney());
        }
        
        datalist = dao.pageListBySql(page, countsql.toString().replaceFirst("and", "where"), listsql.toString().replaceFirst("and", "where"), null);
        return datalist;
    }
    
    /**
    * <p>Title: addoredit</p>
    * <p>Description: 添加或修改</p>
    * @param loansignType  标的类型
    * @return 是否成功
    */
    public boolean addoredit(LoansignType loansignType){
            loansignType.setMinrate(loansignType.getMinrate()/100);
            loansignType.setMaxrate(loansignType.getMaxrate()/100);
            if(null!=loansignType.getId()&&!"".equals(loansignType.getId())){
                dao.update(loansignType);
            }else{
                dao.save(loansignType);
            }
            return true;
    }
    
    /**
    * <p>Title: delete</p>
    * <p>Description: 删除多个标的类型</p>
    * @param ids  标的编号
    * @return 是否成功
    */
    public boolean delete(String ids){
        if(ids.length()>1){
            ids=ids.substring(0, ids.length()-1);
        }
        
        //先判断是否有关联
        StringBuffer sb=new StringBuffer("SELECT count(1) from loansign where loanType in (?)");
        Object obj=dao.findObjectBySql(sb.toString(), ids);
        if(Integer.parseInt(obj.toString())>0){
        	return false;
        }
        sb=new StringBuffer("delete from loansign_type where id in (").append(ids).append(")");
        int result=dao.executeSql(sb.toString());
        return result>0?true:false;
    }
    
    /**
    * <p>Title: queryOne</p>
    * <p>Description: 通过编号查询 </p>
    * @param id 编号
    * @return  标的类型
    */
    public LoansignType queryOne(String id){
        LoansignType loansignType=dao.get(LoansignType.class, Long.valueOf(id));
        return loansignType;
    }
    /**
     * 查询所有的借款标类型
     * @return 借款标集合
     */
    public List<LoansignType> queryLoanType(){
    	List<LoansignType> list = dao.find("from LoansignType");
    	return list;
    }
}
