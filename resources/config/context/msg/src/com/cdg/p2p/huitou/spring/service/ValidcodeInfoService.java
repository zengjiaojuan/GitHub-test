package com.cddgg.p2p.huitou.spring.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.huitou.entity.Validcodeinfo;

/**
* <p>Title:ValidcodeInfoService</p>
* <p>Description: ValidcodeInfo 服务层</p>
* <p>date 2014年2月18日</p>
*/
@Service
public class ValidcodeInfoService {

    /**
     * 数据库操作通用接口
     */
    @Resource
    private HibernateSupport commonDao;


    /**
    * <p>Title: getValidcodeinfoByUid</p>
    * <p>Description: 得到用户的短信限制信息</p>
    * @param id  用户编号
    * @return 限制信息对象
    */
    public Validcodeinfo getValidcodeinfoByUid(Long id){
        StringBuffer sb=new StringBuffer("select * from validcodeinfo where user_id=").append(id);
        List<Validcodeinfo> validList= commonDao.findBySql(sb.toString(), Validcodeinfo.class);
        return validList.size()>0?validList.get(0):null;
    }
    
    /**
    * <p>Title: update</p>
    * <p>Description: 修改对象信息</p>
    * @param validcodeinfo  对象
    */
    public void update(Validcodeinfo validcodeinfo){
        commonDao.update(validcodeinfo);
    }
    
}
