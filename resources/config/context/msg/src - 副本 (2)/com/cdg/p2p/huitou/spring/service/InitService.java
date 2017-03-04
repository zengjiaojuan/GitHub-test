package com.cddgg.p2p.huitou.spring.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.cddgg.base.annotation.FieldConfig;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.PropertiesUtil;
import com.cddgg.commons.log.LOG;
import com.cddgg.commons.normal.ClassPathUtil;
import com.cddgg.p2p.huitou.admin.spring.service.ColumnManageService;
import com.cddgg.p2p.huitou.admin.spring.service.systemset.BannerService;
import com.cddgg.p2p.huitou.admin.spring.service.systemset.FooterService;
//import com.cddgg.p2p.huitou.admin.spring.service.systemset.KeywordManagementService;
import com.cddgg.p2p.huitou.admin.spring.service.systemset.LinkService;
import com.cddgg.p2p.huitou.constant.Path;

/**
 * 初始化service
 * 
 * @author ldd
 * 
 */
@Service
public class InitService implements ApplicationContextAware {

    /**
     * HibernateSupport
     */
    @Resource
    HibernateSupport commondao;

    /**
     * ColumnManageService
     */
    @Resource
    ColumnManageService columnServic;

    /**
     * 系统消息接口
     */
    @Resource
    private FooterService footerservice;

    /**
     * link
     */
    @Resource
    private LinkService linkService;

    /**
     * bannerService
     */
    @Resource
    private BannerService bannerService;

//    /**
//     */
//    @Resource
//    private KeywordManagementService keywordManagementService;

    
    /**
     * 初始化枚举
     * 
     * @param application
     *            ServletContext
     */
    private void initEnumStatus(ServletContext application) {

        List<String> list = ClassPathUtil
                .getClassPath("com.cddgg.p2p.huitou.constant.enums");
        FieldConfig config = null;
        try {

            for (Iterator<String> iterator = list.iterator(); iterator
                    .hasNext();) {

                Class<?> clazz = Class.forName(iterator.next());

                Method method = FieldConfig.class.getMethod("value");
                Field[] fields = clazz.getFields();
                String[] texts = new String[fields.length];

                for (int i = 0; i < fields.length; i++) {
                    config = fields[i].getAnnotation(FieldConfig.class);
                    if (config != null) {
                        texts[i] = (String) method.invoke(config);
                    }
                }

                application.setAttribute(clazz.getSimpleName().toLowerCase(),
                        texts);

            }
            LOG.info("--->初始化枚举状态成功！");

        } catch (Exception e) {
            LOG.error("初始化数据失败！", e);
        }

    }

    /**
     * 初始化路径
     * 
     * @param application
     *            ServletContext
     */
    private void initPath(ServletContext application) {

        Properties p = PropertiesUtil
                .getReadAbleProperties("/config/path/local_path.properties");

        Path.init(application.getRealPath("/"), p.getProperty("upload"));

        LOG.info("--->初始化虚拟路径成功！");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {

        ServletContext application = ((WebApplicationContext) applicationContext)
                .getServletContext();

        application.setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                applicationContext);
        columnServic.queryAllTopics(application);
        columnServic.queryAllArticles(application);
        application.setAttribute("application_link", linkService.query());
        application.setAttribute("application_banner", bannerService.query());
        application.setAttribute("footer", footerservice.queryFooter());
        initEnumStatus(application);
        querySafetyQuestion(application);
        queryAdminUser(application);
        
    }

    /**
     * 查询所有安全问题
     * 
     * @param application
     *            ServletContext
     */
    public void querySafetyQuestion(ServletContext application) {
        application.setAttribute("safetyQuestions",
                commondao.query("from Verifyproblem", false));
    }
    
    /**
     * 查询所有客服人员
     * @param application ServletContext
     */
    public void queryAdminUser(ServletContext application) {
        String sql ="SELECT id,realname FROM adminuser WHERE adminuser.role_id IN (SELECT id FROM role WHERE roleName='服务人员')";
        application.setAttribute("serviceStaffs", commondao.findBySql(sql));
    }
}
