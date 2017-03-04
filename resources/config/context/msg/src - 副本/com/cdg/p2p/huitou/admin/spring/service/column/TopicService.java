package com.cddgg.p2p.huitou.admin.spring.service.column;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.huitou.entity.Topic;

/**
 * 一级栏目
 * @author My_Ascii
 *
 */
@Service
public class TopicService {
    
    /*** 注入HibernateSupport*/
    @Resource
    HibernateSupport commondao;

    /**
     * 分页查询一级栏目
     * 
     * @return List
     */
    public List queryAllTopics(PageModel page) {
        List list = new ArrayList();
        String hql = "select new Topic(id, name, isShow, isfooter, pageTitle) from Topic";
        list = commondao.pageListByHql(page, hql, true);
        return list;
    }

    /**
     * 根据id查询一级栏目详情
     * 
     * @param topicId
     *            (一级栏目编号)
     * @return 返回一级栏目
     */
    public Topic queryTopicById(long topicId) {
        return commondao.get(Topic.class, topicId);
    }

    /**
     * 修改一级栏目
     * 
     * @param topic
     *            一级栏目
     */
    public void updateTopic(Topic topic) {
        commondao.update(topic);
    }
    
    /**
     * 新增一级栏目
     * 
     * @param topic
     *            一级栏目
     */
    public void addTopic(Topic topic) {
        commondao.save(topic);
    }
}
