package com.cddgg.p2p.huitou.admin.spring.service.systemset;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.huitou.entity.Banner;
import com.cddgg.p2p.huitou.spring.util.FileUtil;

/**
 * banner图片信息
 * 
 * @author ransheng
 * 
 */

@SuppressWarnings(value = { "rawtypes", "unchecked" })
@Service
public class BannerService {

    /**
     * 通用dao
     */
    @Resource
    private HibernateSupport commonDao;

    /**
     * 查询banner图片条数
     * 
     * @return 返回banner图片条数
     */
    public Object getCount() {
        String sql = "select count(1) from banner";
        Object count = commonDao.findObjectBySql(sql);
        return count;
    }

    /**
     * 分页显示banner图片信息
     * 
     * @param page
     *            分页对象
     * @return 返回前10条banner图片信息
     */
    public List bannerPage(PageModel page) {
        List list = new ArrayList();
        String sql = "select id,number,picturename,url,type from banner ORDER BY number asc LIMIT "
                + page.firstResult() + "," + page.getNumPerPage();
        list = commonDao.findBySql(sql);
        return list;
    }

    /**
     * 根据id查询banner图片信息
     * 
     * @param id
     *            图片编号
     * @return 返回单条图片信息
     */
    public Banner getOnly(String id) {
        // 返回单条banner图片信息
        return commonDao.get(Banner.class, Long.valueOf(id));
    }

    /**
     * 新增（编辑）banner图片信息
     * 
     * @param banner
     *            banner图片系对象
     * 
     * @param request
     *            请求对象
     * @return true成功、false失败
     */
    public Boolean saveORupdateBanner(Banner banner, HttpServletRequest request) {
        // 文件夹名称
        String folder = "banner";
        // 上传图片
        String imgurl = FileUtil.upload(request, "fileurl", folder);
        // 如果有图片上传
        if (imgurl != null && !"1".equals(imgurl.trim())) {
            // 删除图片
            FileUtil.deleteFile(banner.getImgurl(), folder, request);
            banner.setImgurl(imgurl);
        }
        // 如果上传的不是图类型
        if (imgurl != null && imgurl.equals("2")) {
            return false;
        } else {
            // 如果选择的是修改
            if (banner.getId() != null
                    && !"".equals(banner.getId().toString().trim())) {
                commonDao.saveOrUpdate(banner);
            } else {
                List<Banner> b = commonDao
                        .find("from Banner b order by b.number desc");
                if (b != null && b.size() > 0) {
                    banner.setNumber(b.get(0).getNumber() + 1);
                } else {
                    banner.setNumber(1);
                }
                commonDao.saveOrUpdate(banner);
            }

            return true;
        }
    }

    /**
     * 根据id删除banner图片信息
     * 
     * @param ids
     *            多张图片id组合，以逗号隔开
     */
    public void deletebanner(String ids,HttpServletRequest request) {
        // 将多个图片编号id转换为数组
        String[] id = ids.split(",");
        Banner banner = null;
        // 文件夹名称
        String folder = "banner";
        // 循环id
        for (int i = 0; i < id.length; i++) {
            long bannerid = Long.valueOf(id[i]);
            banner = commonDao.get(Banner.class, bannerid);
            //删除服务器中的文件
            FileUtil.deleteFile(banner.getImgurl(), folder, request);
            //删除数据库信息
            commonDao.delete(bannerid, Banner.class);
        }
    }

    /**
     * 根据排序编号查询（上移）
     * 
     * @param id
     *            id
     * @return 返回null表示该条记录已经是最前面一条 ,否则返回该条记录的上一条排序编号
     */
    public List queryByNumberUp(String id) {

        // 定义查询banner图片信息的集合
        List list = new ArrayList();
        // 根据id查询banner图片信息
        Banner banner = commonDao.get(Banner.class, Long.valueOf(id));
        list.add(banner);
        if (banner != null) {
            // 根据banner排序编号查询图片细信息
            String sql = "select MAX(id),MAX(number) from banner where number<"
                    + banner.getNumber();
            Object obj = commonDao.findObjectBySql(sql);
            list.add(obj);
        }
        return list;
    }

    /**
     * 根据排序编号查询（下移）
     * 
     * @param id
     *            编号
     * @return 返回null表示该条记录已经是最前面一条 ,否则返回该条记录的上一条排序编号
     */
    public List queryByNumberDown(String id) {
        // 定义查询banner图片信息的集合
        List list = new ArrayList();
        Banner banner = commonDao.get(Banner.class, Long.valueOf(id));
        list.add(banner);
        if (banner != null) {
            // 根据banner排序编号查询图片细信息
            String sql = "select min(id),min(number) from banner where number>"
                    + banner.getNumber();
            Object obj = commonDao.findObjectBySql(sql);
            list.add(obj);
        }
        return list;
    }

    /**
     * 根据排序号查询单条数据
     * 
     * @param number
     *            排序号
     * @return 当个图片信息
     */
    public Banner getBannerByNume(Integer number) {
        // 根据图片排序编号查询图片信息
        String hql = "from Banner where id=" + number;
        List<Banner> list = commonDao.find(hql);
        Banner banner = null;
        if (list != null && list.size() > 0) {
            // 得到当个图片系想你
            banner = list.get(0);
        }
        return banner;
    }

    /**
     * 修改图片信息
     * 
     * @param banner
     *            图片信息
     */
    public void update(Banner banner) {
        // 修改图片信息
        commonDao.update(banner);
    }

    /**
     * 图片信息
     * 
     * @return 图片
     */
    public List<Banner> query() {
        String hql = "from Banner order by number asc";
        return commonDao.find(hql);
    }
    
    /**
     * 重置application中的banner图片
     * @param application ServletContext
     */
    public void resetAppBanner(ServletContext application){
        application.setAttribute("application_banner", query());
    }
}
