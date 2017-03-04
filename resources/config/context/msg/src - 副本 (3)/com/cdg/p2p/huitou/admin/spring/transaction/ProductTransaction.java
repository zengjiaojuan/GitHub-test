package com.cddgg.p2p.huitou.admin.spring.transaction;

import java.text.ParseException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.exception.ResponseException;
import com.cddgg.base.spring.exception.ResponseExceptionFactory;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.view.AjaxResponseView;
import com.cddgg.p2p.pay.entity.BidInfo;
import com.cddgg.p2p.huitou.constant.ErrorNumber;
import com.cddgg.p2p.huitou.constant.enums.ENUM_MONEY_EXCHANGE_STATE;
import com.cddgg.p2p.huitou.constant.enums.ENUM_PRODUCT_PAY_STATE;
import com.cddgg.p2p.huitou.entity.Product;
import com.cddgg.p2p.huitou.entity.ProductPayRecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.exception.ProductPayException;
import com.cddgg.p2p.huitou.model.ProductPayInfo;

/**
 * 产品长事务
 * @author ldd
 *
 */
@Service
public class ProductTransaction {

    /**
     * ResponseExceptionFactory
     */
    @Resource
    ResponseExceptionFactory factory;
    
    /**
     * HibernateSupport
     */
    @Resource
    HibernateSupport dao;
    
    /**
     * ProductPayRecordTransaction
     */
    @Resource
    ProductPayRecordTransaction tranProductPayRecord;
    
    /**
     * CreditorPayRecordTransaction
     */
    @Resource
    CreditorPayRecordTransaction tranCreditorPayRecord;
    
    /**
     * CreditorLinkTransaction
     */
    @Resource
    CreditorLinkTransaction tranCreditorLink;
    
    /**
     * AjaxResponseView
     */
    @Resource
    private AjaxResponseView view;
    
    
    /**
     * 添加产品认购信息及其相关信息
     * @param product   产品
     * @param user  投资人
     * @param stateExChange 投资方式
     * @param info  产品认购信息
     * @param bid   环讯信息
     * @throws ResponseException    响应式异常
     * @throws ParseException   时间格式化异常
     */
    public void payProduct(Product product,
            Userbasicsinfo user, ENUM_MONEY_EXCHANGE_STATE stateExChange,
            ProductPayInfo info,BidInfo bid) throws ResponseException, ParseException{
        
        if (!product.isPayAble(info.getMoney())) {// 判断是否允许购买
            throw new ProductPayException("您所购买的产品不足 " + info.getMoney() + " 元，请减少您的购买金额，或选购其他产品！",view);
        }
        
        double sum = 0d;
        ENUM_PRODUCT_PAY_STATE statePay = ENUM_PRODUCT_PAY_STATE.EXECUTING;
        
        for (double val : info.getMoneys()) {
            sum += val;
        }

        //判断是否为待分配购买
        if (sum < info.getMoney() * product.getProductType().getDayDuring()) {
            statePay = ENUM_PRODUCT_PAY_STATE.WAITING;
        } else if (sum > info.getMoney() * product.getProductType().getDayDuring()) {
            throw factory.bornAjax(ErrorNumber.NO_4);
        }
        
        // 添加产品认购记录
        ProductPayRecord record = tranProductPayRecord.addProductRecord(
                product, statePay,info.getAlreadyDateIds(), stateExChange, info.getTimeStart(),info.getTimeEnd(), info.getMoney(), user,new Userbasicsinfo(info.getUserId2()),bid);

        // 添加债权认购记录
        tranCreditorPayRecord.addRecord(record.getId(), user, info.getCreditors(),
                info.getMoneys(), info.getTimeStarts(), info.getTimeEnds());

        // 修改creditorLink余额,并释放锁定
        tranCreditorLink.updateCreditorLink(info.getCreditorLinkIds(), info.getMoneys(),user);

        //增加产品投资金额
        product.pay(info.getMoney());
        dao.update(product);

        
    }
    
}
