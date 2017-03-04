package com.cddgg.p2p.huitou.spring.service.borrow;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.constant.enums.ENUM_CHECK_TYPE;
import com.cddgg.p2p.huitou.entity.BorrowersApply;
import com.cddgg.p2p.huitou.entity.Borrowersbase;
import com.cddgg.p2p.huitou.entity.Borrowerscompany;
import com.cddgg.p2p.huitou.entity.Borrowerscontact;
import com.cddgg.p2p.huitou.entity.Borrowersfiles;
import com.cddgg.p2p.huitou.entity.Borrowersfinanes;
import com.cddgg.p2p.huitou.entity.Borrowersothercontact;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.service.MemberCenterService;
import com.cddgg.p2p.huitou.spring.service.MyindexService;
import com.cddgg.p2p.huitou.spring.util.FileUtil;

@Resource
public class BorrowService {

    /**
     * 注入HibernateSupport
     */
    @Resource
    private HibernateSupport dao;

    /**
     * 注入MyindexService
     */
    @Resource
    private MyindexService myindexService;
    
    @Resource 
    private BorrowRecordService borrowRecordService;
    
    @Resource
    private MemberCenterService memberCenterService;

    /**
     * 查询会员个人信息
     * 
     * @param id
     *            会员编号
     * @return 返回会员信息
     */
    public Userbasicsinfo queryUserinfo(Long id) {
        return dao.get(Userbasicsinfo.class, id);
    }

    /**
     * 查询借款人
     * 
     * @param id
     *            借款人id
     * @return Borrowersbase
     */
    public Borrowersbase queryBorrowerbase(Long id) {
        return dao.get(Borrowersbase.class, id);
    }
 
    /**
    * <p>Title: queryByUserInfo</p>
    * <p>Description:根据会员基本信息查询借款基本信息 </p>
    * @param id 会员编号
    * @return 借款人基本信息
    */
    public Borrowersbase queryByUserInfo(Long id){
        
        String hql="from Borrowersbase where id=? and auditResult=1 and auditStatus=4";
        
        List<Borrowersbase> borrowList=dao.find(hql,id);
        
        if(null!=borrowList&&!borrowList.isEmpty()){
            
            return borrowList.get(0);
        }
       return  null;
        
    }
    /**
     * 添加对象
     * 
     * @param obj
     *            Object
     * @return boolean
     */
    public boolean addObject(Object obj) {
        try {
            dao.save(obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改对象
     * 
     * @param obj
     *            Object
     * @return boolean
     */
    public boolean updateObject(Object obj) {
        try {
            dao.update(obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除对象
     * 
     * @param obj
     *            Object
     * @return boolean
     */
    public boolean deleteObject(long fileId) {
        try {
            dao.delete(dao.get(Borrowersfiles.class, fileId));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 初始化借款人
     * @param borrower
     */
    public void initBorrower(Borrowersbase borrower){
        
        Borrowerscontact borrowerscontact = new Borrowerscontact();
        Borrowerscompany borrowerscompany = new Borrowerscompany();
        Borrowersfinanes borrowersfinanes = new Borrowersfinanes();
        Borrowersothercontact borrowersothercontact = new Borrowersothercontact();

        borrowerscontact.setBorrowersbase(borrower);
        borrowerscompany.setBorrowersbase(borrower);
        borrowersfinanes.setBorrowersbase(borrower);
        borrowersothercontact.setBorrowersbase(borrower);
        
        
        borrower.setBorrowerscontact(borrowerscontact);
        borrower.setBorrowerscompany(borrowerscompany);
        borrower.setBorrowersothercontact(borrowersothercontact);
        borrower.setBorrowersfinanes(borrowersfinanes);
        
        
        dao.save(borrower);
        
    }
    
    /**
     * 添加借款人基础信息
     * 
     * @param borrower
     *            Borrowersbase
     * @param request
     *            HttpServletRequest
     * @return boolean
     */
    public void addOrUptBorrower(Borrowersbase borrower,
            HttpServletRequest request) {
        Userbasicsinfo userbasic = queryUserinfo(((Userbasicsinfo) request
                .getSession().getAttribute(Constant.SESSION_USER)).getId());

        borrower.setAddTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        borrower.setAuditResult(0);
        borrower.setAuditStatus(1);
        borrower.setCredit(0);
        borrower.setSuminte(0);
        borrower.setUserbasicsinfo(userbasic);
        
        
        
        dao.update(borrower);
    }

    /**
     * 申请审核
     * 
     * @param baseId
     *            String
     * @return String
     */
    public boolean updateBorrower(String baseId, HttpServletRequest request,
            HttpServletResponse response) {
        Borrowersbase borrower = dao.get(Borrowersbase.class,
                Long.parseLong(baseId));
        borrower.setAuditResult(0);
        borrower.setAuditStatus(2);
        borrower.setCommittime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        return updateObject(borrower);
    }

    /**
     * 修改借款人联系方式
     * 
     * @param contact
     *            Borrowerscontact
     * @param request
     *            HttpServletRequest
     * @return boolean
     */
    public void addOrUptContact(Borrowerscontact contact,
            HttpServletRequest request) {
        contact.setAddTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        dao.update(contact);
    }

    /**
     * 修改借款人联系方式
     * 
     * @param company
     *            Borrowerscompany
     * @param request
     *            HttpServletRequest
     * @return boolean
     */
    public void updateCompany(Borrowerscompany company,
            HttpServletRequest request) {
        company.setAddTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        dao.update(company);
    }

    /**
     * 修改借款人财务状况
     * 
     * @param finanes
     *            Borrowersfinanes
     * @param request
     *            HttpServletRequest
     * @return boolean
     */
    public void updateFinanes(Borrowersfinanes finanes,
            HttpServletRequest request) {
        finanes.setAddTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        dao.update(finanes);
    }

    /**
     * 修改借款人联保情况
     * 
     * @param Othercontact
     *            Borrowersothercontact
     * @param request
     *            HttpServletRequest
     * @return boolean
     */
    public void updateOthercontact(Borrowersothercontact Othercontact,
            HttpServletRequest request) {
        Othercontact.setAddTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        dao.update(Othercontact);
    }

    /**
     * 判断用户是否已添加借款人基本信息
     * 
     * @param ubaseid
     * @return
     */
    public List<Borrowersbase> queryBorrowerByUbaseid(long ubaseid) {
        return dao.find("FROM Borrowersbase WHERE userbasicsinfo.id = "
                + ubaseid);
    }

   /**
    * 
    * @param request HttpServletRequest
    * @param response HttpServletResponse
    * @param file Borrowersfiles
    * @param fileType 文件类型
    * @return String
    * @throws IOException 异常
    */
    public String uploadFile(HttpServletRequest request,HttpServletResponse response, Borrowersfiles file,
            String fileType) throws IOException {
        Map<String, Object> imgMap = myindexService.upload(request);
//        String fileSuffix = imgMap.get("saveName").toString()
//                .substring(imgMap.get("saveName").toString().indexOf(".") + 1);// 文件后缀
//        if (!fileType.equals("商业图片")) {
//            if (fileSuffix.equals("txt")) {
//                fileType = "文本";
//            } else if (fileSuffix.equals("docx") || fileSuffix.equals("doc")) {
//                fileType = "Word";
//            } else if (fileSuffix.equals("pdf")) {
//                fileType = "PDF";
//            } else if (fileSuffix.equals("zip") || fileSuffix.equals("7z")
//                    || fileSuffix.equals("7z") || fileSuffix.equals("rar")) {
//                fileType = "压缩文件本";
//            } else if (fileSuffix.equals("xlsx") || fileSuffix.equals("xls")) {
//                fileType = "Excel";
//            } else {
//                fileType = "未知文件";
//            }
//        }
        file.setAddTime(imgMap.get("uploadTime").toString());
        file.setFileName(imgMap.get("saveName").toString());
        file.setFilePath(imgMap.get("imgurl").toString());
        file.setFileType(fileType);
        file.setFileRemark("");
        dao.save(file);
        request.setAttribute("imgurl", imgMap.get("imgurl").toString());
        if(fileType.equals("商业图片")){
            return forwardBusinessImg(request, response);
        }else if(fileType.equals("申请贷款")){
            forwardDataUpload(request, response);
            return "/WEB-INF/views/member/borrow/apply-borrower";
        }else{
            return forwardDataUpload(request, response);
        }
    }

    /**
     * 查询借款人上传的文件
     * 
     * @param baseId
     *            借款人id
     * @param request
     *            HttpServletRequest
     * @return List<Borrowersfiles>
     */
    public List<Borrowersfiles> queryBorrowersfiles(long baseId,
            HttpServletRequest request) {
        return (List<Borrowersfiles>) dao
                .query("FROM Borrowersfiles WHERE borrowersbase.id = ? AND fileType != ?",
                        false, baseId, "商业图片");
    }

    /**
     * 修改文件
     * 
     * @param Othercontact
     *            Borrowersothercontact
     * @param request
     *            HttpServletRequest
     * @return boolean
     */
    public boolean updateFile(Borrowersfiles file, HttpServletRequest request) {
        return updateObject(file);
    }

    /**
     * 删除文件
     * 
     * @param file
     * @param request
     * @return
     */
    public boolean deleteFile(String fileId) {
        return deleteObject(Long.parseLong(fileId));
    }

    /**
     * 根据id查询上传的文件
     * 
     * @param id
     *            (文件编号)
     * @return Uploadfile
     */
    public Borrowersfiles queryBorrowerFileById(long id) {
        return dao.get(Borrowersfiles.class, id);
    }

    /**
     * 
     * 下载文件
     * 
     * @param id
     *            文件id
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return boolean
     */
    public void downFile(Long id, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Borrowersfiles f = queryBorrowerFileById(id);
            String realPath = request.getSession().getServletContext()
                    .getRealPath("");
            FileUtil.downFile(realPath + "/upload" + f.getFilePath(),
                    f.getFileName(), response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有商业
     * 
     * @return
     */
    public List<Borrowersfiles> queryBusinessImg(long baseId,
            HttpServletRequest request) {
        return (List<Borrowersfiles>) dao
                .query("FROM Borrowersfiles WHERE borrowersbase.id = ? AND fileType = ?",
                        false, baseId, "商业图片");
    }

    /**
     * 跳转到商业图片页面上
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return String
     */
    public String forwardBusinessImg(HttpServletRequest request,
            HttpServletResponse response) {
        List<Borrowersbase> borrowerlist = queryBorrowerByUbaseid(((Userbasicsinfo) request
                .getSession().getAttribute(Constant.SESSION_USER)).getId());
        request.setAttribute("borrower",
                borrowerlist.size() > 0 ? borrowerlist.get(0) : null);
        if(borrowerlist.size() > 0){
            request.setAttribute(
                    "files",
                    queryBorrowersfiles(borrowerlist.get(0).getId(), request));
        }
        if(borrowerlist.size() > 0 ){
            request.setAttribute(
                    "businessImgs",
                    queryBusinessImg(borrowerlist.get(0).getId(), request));
        }
        return "/WEB-INF/views/member/borrow/step6";
    }

    /**
     * 跳转到资料上传页面
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return String
     */
    public String forwardDataUpload(HttpServletRequest request,
            HttpServletResponse response) {
        List<Borrowersbase> borrowerlist = queryBorrowerByUbaseid(((Userbasicsinfo) request
                .getSession().getAttribute(Constant.SESSION_USER)).getId());
        request.setAttribute("borrower",
                borrowerlist.size() > 0 ? borrowerlist.get(0) : null);
        if(borrowerlist.size() > 0){
            request.setAttribute(
                    "files",
                    queryBorrowersfiles(borrowerlist.get(0).getId(), request));
        }
        return "/WEB-INF/views/member/borrow/step7";
    }
    
    
    /**
    * <p>Title: updateinteger</p>
    * <p>Description: 修改会员的积分总额</p>
    * @param suminteger 总额
    * @param userId 用户编号
    */
    public void  updateinteger(int suminteger,Long userId){
        StringBuffer sb=new StringBuffer("update borrowersbase set suminte=");
        sb.append(suminteger).append(" where userbasicinfo_id=").append(userId);
        dao.executeSql(sb.toString());
    }
    
    /**
    * <p>Title: queryBorrowrecord</p>
    * <p>Description:用户借入记录中的查询 </p>
    * @param id
    * @param request
    */
    public void queryBorrowrecord(Userbasicsinfo  user, HttpServletRequest request) {
        
        
        String sql="select sum(t.issueLoan),count(t.id) from loansign t where t.userbasicinfo_id="+user.getId()+" and t.loanstate=2";
        Object[] res1 = (Object[])dao.findObjectBySql(sql);
        res1[0] = borrowRecordService.formateNumber(res1[0]);
        request.setAttribute("row_1",res1);
        
        //偿还中借入金额
        sql="select sum(t.issueLoan),count(t.id) from loansign t where t.userbasicinfo_id="+user.getId()+" and ((t.loanstate=3)or(t.loanstate=4 and (SELECT count(1) from repaymentrecord where loanSign_id=t.id and repayState=3)>0))";
        Object[] res2 = (Object[])dao.findObjectBySql(sql);
        res2[0]= res2[0]!=null?borrowRecordService.formateNumber(res2[0]):0.00;
        request.setAttribute("row_2",res2);
        
        //还清的借入
        sql="select sum(t.issueLoan),count(t.id) from loansign t where t.userbasicinfo_id="+user.getId()+" and t.loanstate=4 and (SELECT count(1) from repaymentrecord where loanSign_id=t.id and repayState=3)=0";
        Object[] res3 = (Object[])dao.findObjectBySql(sql);
        res3[0]= res3[0]!=null?borrowRecordService.formateNumber(res3[0]):0.00;
        request.setAttribute("row_3",res3);
        
//        String now = DateUtils.format("yyyy-MM");
//        //当前垫付
//        sql="select sum(t.preRepayMoney) from repaymentrecord t,loansign l where t.loanSign_id=l.id and l.userbasicinfo_id="+user.getId()+" and t.preRepayDate like '"+now+"%' and t.repayState =3";
//        Object res4 = dao.findObjectBySql(sql);
//        request.setAttribute("row_4",borrowRecordService.formateNumber(res4));
//        
//        //累计垫付
//        sql="select sum(t.preRepayMoney) from repaymentrecord t,loansign l where t.loanSign_id=l.id and l.userbasicinfo_id="+user.getId()+"  and t.repayState =3";
//        Object res5 = dao.findObjectBySql(sql);
//        request.setAttribute("row_5",borrowRecordService.formateNumber(res5));
//        
        //当前逾期(逾期未还款)
        sql="SELECT sum(preRepayMoney+money) FROM repaymentrecord rr,loansign ls where rr.loansign_id=ls.id and rr.repayState=3 and ls.userbasicinfo_id="+user.getId();                
        Object res6 = dao.findObjectBySql(sql);
        request.setAttribute("row_6",borrowRecordService.formateNumber(res6));
        
        //累计逾期
        sql="SELECT sum(preRepayMoney+money) FROM repaymentrecord rr,loansign ls where rr.loansign_id=ls.id and rr.repayState=4 and ls.userbasicinfo_id="+user.getId();
        Object res7 = dao.findObjectBySql(sql);
        request.setAttribute("row_7",borrowRecordService.formateNumber(res7));
        
        //累计借出奖励：
        sql = "select sum(t1.issueLoan*t2.reward) from loansign t1 INNER JOIN loansignbasics t2 ON t1.id = t2.id where t1.userbasicinfo_id="+user.getId()+" and t1.loanstate in ('3','4')";
        Object res8 = dao.findObjectBySql(sql);
        request.setAttribute("row_8",borrowRecordService.formateNumber(res8));
        
        sql = "SELECT SUM(mgtMoney) from loansignbasics a INNER JOIN loansign b ON a.id = b.id WHERE  b.loanstate>2 and b.loanstate<5 and  b.userbasicinfo_id="+user.getId();
        Object res9 = dao.findObjectBySql(sql);
        request.setAttribute("row_9",borrowRecordService.formateNumber(res9));
        
        //累计借入金额
        sql="SELECT SUM(tenderMoney) FROM loanrecord where loanSign_id in (SELECT id from loansign where loanstate>2 and loanstate<5 and  userbasicinfo_id="+user.getId()+")";
        Object res10 = dao.findObjectBySql(sql);
        res10 = borrowRecordService.formateNumber(res10);
        request.setAttribute("row_10",res10);
        //累计利息成本
        Double res11= Double.parseDouble(memberCenterService.interest(user.getId()).toString());
        request.setAttribute("row_11",res11);
        //借入利息成本
        Double res12 = 0.0;
        if (res11 != 0 && res10.toString() != "0") {
            res12 = res11/ Double.parseDouble(res10.toString().replaceAll(",", "")) * 100;
        }
        request.setAttribute("row_12", borrowRecordService.toJsonString(borrowRecordService.round(res12,2)));
        
    }
    
    /**
     * 提交申请
     * @param apply
     */
    public void submitApply(BorrowersApply apply){
        
        apply.setTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        apply.setStatus(ENUM_CHECK_TYPE.CHECKING.ordinal());
        
        dao.save(apply);
        
    }
}
