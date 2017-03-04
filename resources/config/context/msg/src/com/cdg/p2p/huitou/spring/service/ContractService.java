package com.cddgg.p2p.huitou.spring.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFEncryption;

import com.cddgg.base.constant.Constant;
import com.cddgg.base.util.FreeMarkerUtil;
import com.cddgg.base.util.ProjectPathUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.huitou.model.LoanContract;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

/**
 * 合同生成service
 * 
 * @author ldd
 * 
 */
@Service
public class ContractService {

    /**
     * ITextRenderer
     */
    private ITextRenderer renderer;

    /**
     * loanContract
     */
    private String loanContract;
    
    /**
     * 构造方法
     */
    public ContractService() {

        String path = ProjectPathUtils.getClassesPath().replace("%20", " ")
                + "config/";

        loanContract = path + "marker/contract/loan_contract.html";
        renderer = new ITextRenderer();
        
        try {
            renderer.getFontResolver().addFont(path + "font/simsun.ttc",
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            LOG.error("添加中文字体失败！", e);
        }
        

    }

    /**
     * 生成
     * @param loanContract  LoanContract
     * @param outputStream  输出流
     * @throws Exception    异常
     */
    @SuppressWarnings("serial")
    public void born(final LoanContract loanContract, OutputStream outputStream) throws Exception {

        try {

            // 设置PDF文件内容
            renderer.setDocumentFromString(FreeMarkerUtil.execute(
                    "config/marker/contract/loan_contract.ftl",
                    Constant.CHARSET_DEFAULT, new HashMap<String, Object>() {
                        {
                            put("item", loanContract);
                        }
                    }));
            // 设置PDF根路径
            renderer.getSharedContext().setBaseURL("file:" + loanContract);

            renderer.layout();

            // 设置密码
            renderer.setPDFEncryption(new PDFEncryption(loanContract
                    .getPdfPassword().getBytes(), loanContract
                    .getPdfPassword().getBytes()));

            renderer.createPDF(outputStream);
            outputStream.flush();
            renderer.finishPDF();
        } catch (DocumentException e) {
            LOG.error("生成PDF文档失败！", e);
        }

    }

}
