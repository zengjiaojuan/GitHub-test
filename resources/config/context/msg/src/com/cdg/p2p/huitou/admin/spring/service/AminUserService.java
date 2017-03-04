package com.cddgg.p2p.huitou.admin.spring.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;

/**
* <p>Title:AminUserService</p>
* <p>Description: 后台管理员操作层</p>
*  date 2014年1月24日
*/
@Service
@SuppressWarnings(value = { "rawtypes" })
public class AminUserService {

    /** 注入数据操作层*/
    @Resource
    HibernateSupport commondao;

    /**
     * <p>
     * Title: count</p>
     * <p>
     * Description: 统计条数</p>
     * 
     * @param name
     *            输入的用户
     * @return 查询出来的总条数
     */
    public Object count(String name) {
        Object i = commondao
                .findObjectBySql("select count(1) from adminuser where  username Like '%"
                        + name.trim() + "%'");
        return i;
    }

    /**
     * <p>
     * Title: queryPage</p>
     * <p>
     * Description: 获取用户信息</p>
     * 
     * @param page 分页信息
     *            
     * @param name 用户名称（模糊查询条件）
     *            
     * @return 查询的list结果集
     */
    public List queryPage(PageModel page, String name) {
        List list = new ArrayList();
        String sql = "SELECT id,age,email,phone FROM adminuser where  username Like '%"
                + name.trim()
                + "%'LIMIT "
                + page.firstResult()
                + ","
                + page.getNumPerPage();
        list = commondao.findBySql(sql);
        return list;
    }
}
