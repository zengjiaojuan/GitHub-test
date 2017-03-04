package com.cddgg.p2p.huitou.admin.spring.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.normal.Validate;
import com.cddgg.p2p.huitou.constant.enums.ENUM_COLUMN_ISFIXED;
import com.cddgg.p2p.huitou.constant.enums.ENUM_SHOW_STATE;
import com.cddgg.p2p.huitou.entity.Article;
import com.cddgg.p2p.huitou.entity.Deputysection;
import com.cddgg.p2p.huitou.entity.Topic;
import com.cddgg.p2p.huitou.entity.Uploadfile;

/**
 * 栏目管理
 * 
 * @author My_Ascii
 * 
 */
@Service
@SuppressWarnings(value = { "columnservice" })
public class ColumnManageService {

    /*** 注入HibernateSupport*/
    @Resource
    HibernateSupport commondao;

    /**
     * 查询所有一级栏目
     * 
     * @return List
     */
    public List queryAllTopics() {
        return commondao.find("select new Topic(id, name, url) from Topic");
    }

    /**
     * 查询所有二级栏目
     * 
     * @return List
     */
    public List queryAllDeputysections() {
        return commondao
                .find("select new Deputysection(id, name) from Deputysection");
    }

    /**
     * 批量删除
     * 
     * @param cls
     *            要删除的对象
     * @param ids
     *            要删除的对象id
     */
    public void deleteMany(Class cls, String ids) {
        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            Long did = Long.valueOf(id[i]);
            commondao.delete(commondao.get(cls, did));
        }
    }

    /**
     * 批量删除
     * 
     * @param ids
     *            选中的id
     * @return boolean
     */
    public boolean deleteMany(String ids) {
        Deputysection deputy = null;
        boolean isSuccess = true;
        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            Long did = Long.valueOf(id[i]);
            deputy = commondao.get(Deputysection.class, did);
            if (deputy.getIsfixed() == ENUM_COLUMN_ISFIXED.UNDELETE.ordinal()
                    || deputy.getIsfixed() == ENUM_COLUMN_ISFIXED.UNUPDATE
                            .ordinal()) {
                isSuccess = false;
                break;
            }
            commondao.delete(deputy);
        }
        return isSuccess;
    }

    /**
     * 查询所有状态为显示的文章
     * 
     * @return List
     */
    public List queryAllArticle(String did) {
        StringBuffer sb = new StringBuffer(
                "FROM Article WHERE isShow = 1 and deputysection.id = ? ORDER BY createTime DESC");
        return commondao.find(sb.toString(), Long.parseLong(did));
    }

    /**
     * 查询所有状态为显示的文章
     * 
     * @return List
     */
    public List queryAllArticle(PageModel page,String did) {
        page.setNumPerPage(5);
        StringBuffer sb = new StringBuffer(
                "FROM Article WHERE isShow = 1 and deputysection.id = ? ORDER BY createTime DESC");
        return commondao.pageListByHql(page, sb.toString(), false,Long.parseLong(did));
    }
    
    /**
     * 查询所有状态为显示并推荐的文章的文章
     * 
     * @return List
     */
    public List queryArticle(String deputyName) {
        StringBuffer sb = new StringBuffer(
                "FROM Article WHERE isShow = 1 and isRecommend = 1 and deputysection.name = ?  ORDER BY createTime DESC");
        return commondao.find(sb.toString(), deputyName);
    }

    /**
     * 查询指定一级栏目下面的栏目类型为‘列表’的二级栏目
     * 
     * @param id
     *            (一级栏目的id)
     * @return List
     */
    public List queryDeputyByTid(long id) {
        String sql = "SELECT id, name FROM deputysection WHERE sectiontype_id = 2 and topic_id = ?";
        List list = commondao.findBySql(sql, id);
        // return commondao.findBySql(sql);
        return list;
    }

    /**
     * 查询所有栏目类型为‘列表’的二级栏目
     * 
     * @return List
     */
    public List queryAllArticles() {
        String sql = "SELECT id, name FROM deputysection WHERE sectiontype_id = 2";
        return commondao.findBySql(sql);
    }

    /**
     * 删除文章
     * 
     * @param id
     *            文章id
     */
    public void deleteArticle(long id) {
        commondao.delete(commondao.get(Article.class, id));
    }
    // //////////////////////////////////////////////////////////**邮件反馈**//////////////////////////////////////////////////////////////////////
    /**
     * 查询所有反馈邮件
     * 
     * @param page
     *            PageModel
     * @return List
     */
    public List queryAllFeedback(PageModel page) {
        List list = new ArrayList();
        list = commondao.pageListByHql(page, "from Feedbackinfo", false);
        return list;
    }
    
    /**
     * 查询所有反馈邮件
     * 
     * @return List
     */
    public List queryAllFeedback() {
        List list = new ArrayList();
        StringBuffer sb = new StringBuffer(
                "SELECT a.`name`, a.email, a.Phone, a.context, (SELECT b.typeName FROM feedbacktype b WHERE a.feedbacktype_id = b.id), a.addTime FROM feedbackinfo a");
        list = commondao.findBySql(sb.toString());
        return list;
    }

    /**
     * 根据id查询上传的文件
     * 
     * @param id
     *            (文件编号)
     * @return Uploadfile
     */
    public Uploadfile queryUploadFileById(long id) {
        return commondao.get(Uploadfile.class, id);
    }
    
    /**
     * 返回数据时设置需要返回的json对象的属性
     * @param json 用来存放返回的json数据
     * @param statusCode 状态码
     * @param message 返回的提示信息
     * @param navTabId 页面上弹窗的id
     * @param callbackType 回调函数
     * @return 返回JSONObject对象
     */
     public JSONObject setJson(JSONObject json, String statusCode,
             String message, String navTabId, String callbackType) {
         json.element("statusCode", statusCode);
         json.element("message", message);
         json.element("navTabId", navTabId);
         if (!callbackType.equals("")) {
             json.element("callbackType", callbackType);
         }
         return json;
     }

    /**
     * 查询所有的一级栏目，并将其存放到application中
     * 
     * @param application
     *            ServletContext
     */
    public void queryAllTopics(ServletContext application) {

        List<Topic> listTopics = (List<Topic>) commondao
                .find("FROM Topic a order by a.id asc");

        List<Deputysection> listDeputys = commondao.find(
                "FROM Deputysection a where a.isShow = ? order by a.topic.id,a.id asc",
                ENUM_SHOW_STATE.TRUE.ordinal());
        
        for (Iterator<Topic> topics = listTopics.iterator(); topics.hasNext();) {
            Topic topic = (Topic) topics.next();

            for (Iterator<Deputysection> deputysections = listDeputys.iterator(); deputysections.hasNext();) {
                
                Deputysection deputysection = deputysections.next();
                
                if (deputysection.getTopic().getId().equals(topic.getId()) && deputysection.getIsShow() == ENUM_SHOW_STATE.TRUE.ordinal()) {
                    topic.setUrl(deputysection.getUrl());
                    commondao.update(topic);
                    break;
                }
            }

        }
        application.setAttribute("topics", listTopics);
        application.setAttribute("appDeputys", listDeputys);
    }

    /**
     * 根据id查询二级栏目详情
     * 
     * @param id
     *            二级栏目id (二级栏目编号)
     * @return 返回二级栏目
     */
    public Deputysection queryDeputyById(long id) {
        return commondao.get(Deputysection.class, id);
    }
    
    /**
     * 根据id查询文章详情
     * 
     * @param id
     *            (文章编号)
     * @return 返回文章
     */
    public Article queryArticleById(long id) {
        return commondao.get(Article.class, id);
    }
    
    /**
     * 查询所有被推荐的文章
     * 
     * @param application
     *            ServletContext
     */
    public void queryAllArticles(ServletContext application) {
        List<Article> list = commondao.find(
                "FROM Article a where a.isRecommend = 1 order by a.createTime DESC");
        application.setAttribute("appArticles", list);
    }
    
    /**
     * 重置 application
     * @param application ServletContext
     * @param request HttpServletRequest
     */
    public void resetApplaction(HttpServletRequest request){
        queryAllTopics(request.getSession()
                .getServletContext());
        queryAllArticles(request.getSession()
                .getServletContext());
    }
    
    /**
     * 添加对象
     * 
     * @param obj
     *            Object
     */
    public void addObj(Object obj) {
        commondao.save(obj);
    }
    
    /**
     * 添加对象
     * 
     * @param obj
     *            Object
     */
    public void updateObj(Object obj) {
        commondao.update(obj);
    }
    
    /**
     * 查询所有反馈邮件
     * 
     * @param page
     *            PageModel
     * @return List
     */
    public List queryAllFeedback(PageModel page, String replyType, String replyStatus) {
        List list = new ArrayList();
        List<Object> param = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from Feedbackinfo where 1 = 1");
        if(Validate.emptyStringValidate(replyType)){
            hql.append(" and feedbacktype.id = ?");
            param.add(Integer.valueOf(replyType));
        }
        if(Validate.emptyStringValidate(replyStatus) && !replyStatus.equals("-1")){
            hql.append(" and replyStatus = ?");
            param.add(Integer.valueOf(replyStatus));
        }
        Object[] params = null;
        if (param.size() > 0) {
            params = param.toArray();
        }
        list = commondao.pageListByHql(page, hql.toString(), false, params);
        return list;
    }
}
