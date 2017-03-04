package com.cddgg.p2p.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cddgg.base.spring.orm.hibernate.HibernateSupportTemplate;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Adminuser;
import com.cddgg.p2p.huitou.entity.Attachment;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.spring.util.FileUtil;

/**
 * 借款标附件service
 * 
 * @author Administrator
 * 
 */
@Service
public class AttachmentService {
    /** dao */
    @Resource
    HibernateSupportTemplate dao;

    /** loanSignQuery */
    @Resource
    private LoanSignQuery loanSignQuery;

    /** filedata */
    private String filedata = "Filedata";
    /** loansign */
    private String loansign = "loanSign";

    /**
     * <p>
     * Title: getAttachmentCount
     * </p>
     * <p>
     * Description: 借款标为id的附件信息的条数
     * </p>
     * 
     * @param loansignId
     *            借款编号
     * @return 条数
     */
    public int getAttachmentCount(int loansignId) {
        StringBuffer sb = new StringBuffer(
                "SELECT COUNT(1) from attachment where loansign_id=");
        return loanSignQuery.queryCount(sb.append(loansignId).toString());
    }

    /**
     * 通过借款标标号查询到该借款标的附件信息
     * 
     * @param page
     * @param loanSignId
     *            借款标id
     *            @param start start
     *            @param limit limit
     * @return 附件信息
     */
    public List<Attachment> queryAttachmentList(int start, int limit,
            int loanSignId) {
        List list = new ArrayList();
        StringBuffer sb = new StringBuffer(
                "SELECT attachment.id,attachmentName, CASE WHEN attachmentType = 1 THEN '标图' ELSE '借款方相关资料' END, ");
        sb.append(
                " uploadTime, adminuser.realname FROM attachment INNER JOIN adminuser ON attachment.adminuser_id = adminuser.id WHERE loansign_id = ")
                .append(loanSignId);
        sb.append(" LIMIT ").append(start).append(" , ").append(limit);
        list = dao.findBySql(sb.toString());
        return list;
    }

    /**
     * 上传附件
     * 
     * @param id
     *            标号
     * @param type
     *            上传类型（1标图 2借款者资料）
     * @param request
     *            请求
     * @return 1上传附件为空 2上传的不是图类型
     */
    public Integer uploadAttchment(Loansign loansign, Integer type,HttpServletRequest request) {
   	 // 文件夹名称
       String folder = "attachment";
       // 上传为标图
       if (type == 1) {
           StringBuffer sb = new StringBuffer(
                   "SELECT COUNT(1) from attachment where attachmentType=1 and loansign_id=" + loansign.getId());
           int count = loanSignQuery.queryCount(sb.toString());
           if (count == 1) {
               return 4;
           }
       }
       
       // 上传图片
       String imgurl = FileUtil.upload(request, "fileurl", folder);
       
    // 上传附件为空
     if (imgurl == null || imgurl.equals("1")) {
         return 1;
     }
     if (imgurl != null && imgurl.equals("2")) {
         return 2;
     }
       
      
       
       // 取到当前登录管理员
       Adminuser adminUser = (Adminuser) request.getSession().getAttribute(Constant.ADMINLOGIN_SUCCESS);
       if(null==adminUser){
       	adminUser=new Adminuser(Long.valueOf("2"));
       }
       MultipartFile file = null;
       MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
       file = multipartRequest.getFile("fileurl");
       String name = file.getOriginalFilename();
       // 取到附件文件名
       name = name.substring(0, name.indexOf("."));
       Attachment attachmently = new Attachment();
       attachmently.setAdminuser(adminUser);// 上传人
       attachmently.setAttachmentName(imgurl);// 附件保存地址
       attachmently.setOriginalName(name);// 附件原始名称
       attachmently.setAttachmentType(type);// 1标图 2借款者资料
       attachmently.setUploadTime(DateUtils.format(Constant.DEFAULT_TIME_FORMAT));
       attachmently.setLoansign(loansign);// 标号
       // 保存附件
       dao.save(attachmently);
       return 3;
   }

    /**
     * 删除附件
     * 
     * @param id
     *            附件id
     * @param request 请求
     * @return 是否
     */
    public boolean delAttachment(String id, HttpServletRequest request) {
        try {
            Attachment attachment = dao.get(Attachment.class, Long.valueOf(id));
            // 删除附件
            FileUtil.deleteFile(attachment.getAttachmentName(), loansign,
                    request);
            // 从数据库删除附件
            dao.delete(attachment);
            return true;
        } catch (DataAccessException e) {
            return false;
        }

    }

}
