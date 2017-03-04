package com.cddgg.p2p.huitou.spring.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cddgg.base.model.PageModel;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.util.Arith;
import com.cddgg.p2p.huitou.util.DateUtil;
import com.cddgg.p2p.huitou.util.SQLUtils;

/**   
 * Filename:    ReturnedMoneyService.java   
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年3月28日 上午9:49:32   
 * Description:  
 *   
 */

@Service
public class ReturnedMoneyService {
    /** 引入log4j日志打印类*/
    private static final Logger logger = Logger.getLogger(ReturnedMoneyService.class);

    /** dao*/
    @Resource
    private HibernateSupport dao;
    
    /**
    * <p>Title: getReceivablePlan</p>
    * <p>Description: 回款计划明细查询</p>
    * @param ids 当前登录会员编号
    * @param minMoeny 最小金额
    * @param maxMoney 最大金额
    * @param minTime 最小时间
    * @param maxTime 最大时间
    * @param pageModel 分页模型
    * @return 查询结果
    */
    public List getReceivablePlan(String ids,String minMoeny,String maxMoney,String minTime,String maxTime,PageModel pageModel){
        
        List dataList=null;

        dataList=dao.pageListBySql(pageModel, SQLUtils.MONEY_RECORD_COUNT, SQLUtils.MONEY_RECORD, null, ids,minTime,maxTime,minMoeny,maxMoney);
        
        List<String[]> returnList=new ArrayList<String[]>();
        
        String data[]=null;
        
        if(null!=dataList&&!dataList.isEmpty()){
           
            double sumMoney=0.00;
             
            double rateMoney=0.00;
            for(Object obj:dataList){ 
                Object[] objs=(Object[]) obj;
                /**data[0] 借款标号,data[1] 类型,data[2] 回收日期,data[3] 回收金额,data[4] 本金+利息-佣金,
                 * data[5] 查询期数/总期数,data[6] 合同
                 * 
                 * 借款标id,
                 * 本金(分特权和非特权2部分),
                 * 产生的利息,
                 * 利率，
                 * 几个月还完，
                 * 借款标号，
                 * 是否是特权会员，
                 * 普通会员管理费比例，
                 * 普通会员管理费上限，
                 * 特权会员管理费比例，
                 * 特权会员管理费上限，
                 * 借款标借款总金额，
                 * 标的类型，
                 * 放款时间
                 * 
                 * 
                 */
                
                objs[1]=Arith.round(Double.parseDouble(objs[1].toString()), 2);//本金
                
                objs[2]=Arith.round(Double.parseDouble(objs[2].toString()), 2);//利息
                
                data=new String[7];
                data[0]=objs[5].toString();
                if("1".equals(objs[12].toString())){
                    data[1]="普通标"; 
                    //计算实际利息，并保留2位小数
                    objs[2]= Arith.round(Arith.mul(Arith.div(Double.parseDouble(objs[2].toString()), 12, 2), Double.parseDouble(objs[4].toString())), 2);
                }else if("2".equals(objs[12].toString())){
                    data[1]="天标";
                    objs[2]=Arith.round(Arith.mul(Double.parseDouble(objs[2].toString()),Double.parseDouble(objs[14].toString())), 2);
                             
                }else if("3".equals(objs[12].toString())){
                    data[1]="秒标";
                }else if("4".equals(objs[12].toString())){
                    data[1]="流转标";
                    
                    double days=0.0;
                    try {
                        days = DateUtil.differenceDate("yyyy-MM-dd HH:mm:ss",objs[15].toString(), objs[13].toString()+" 00:00:00");
                    } catch (ParseException e) {
                        logger.warn("获取流转标使用天数失败：getReceivablePlan(String ids=" + ids + ", String minMoeny=" + minMoeny + ", String maxMoney=" + maxMoney + ", String minTime=" + minTime + ", String maxTime=" + maxTime + ", PageModel pageModel=" + pageModel + ")exception ignored", e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
                    }
                    
                    objs[2]=Arith.round(Arith.mul(Arith.div(Double.parseDouble(objs[2].toString()), 365.00, 2), days), 2);
                    
                }
                
                sumMoney=Double.parseDouble(objs[1].toString())+Double.parseDouble(objs[2].toString());
                
                if("1".equals(objs[6].toString())){//特权会员
                    rateMoney=Double.parseDouble(objs[2].toString())*Double.parseDouble(objs[9].toString());
                    rateMoney=Arith.round(rateMoney, 2);
                    if(rateMoney>Double.parseDouble(objs[10].toString())){
                        sumMoney=sumMoney-Double.parseDouble(objs[10].toString());
                        data[4]=objs[1].toString()+"+"+objs[2].toString()+"-"+Arith.round(Double.parseDouble(objs[10].toString()), 2);
                    }else{
                        sumMoney=sumMoney-rateMoney;
                        data[4]=objs[1].toString()+"+"+objs[2].toString()+"-"+rateMoney;
                    }
                    
                }else{//非特权会员
                    rateMoney=Double.parseDouble(objs[2].toString())*Double.parseDouble(objs[7].toString());
                    rateMoney=Arith.round(rateMoney, 2);
                    if(rateMoney>Double.parseDouble(objs[8].toString())){
                        sumMoney=sumMoney-Double.parseDouble(objs[8].toString());
                        data[4]=objs[1].toString()+"+"+objs[2].toString()+"-"+Arith.round(Double.parseDouble(objs[8].toString()), 2);
                    }else{
                        sumMoney=sumMoney-rateMoney;
                        data[4]=objs[1].toString()+"+"+objs[2].toString()+"-"+rateMoney;
                    }
                }
                data[2]=objs[13].toString();
                data[3]=Arith.round(sumMoney, 2)+"";
                
                if(null!=objs[4]){
                    data[5]=objs[4].toString()+"/"+objs[4].toString();
                }else{
                    data[5]="1/1"; 
                }
                
                data[6]=objs[0].toString();
                
                returnList.add(data);
            }
            
        }
        
        return returnList;
    }

    
    /**
    * <p>Title: amountMoney</p>
    * <p>Description: 统计金额</p>
    * @param request HttpServletRequest
    */
    public void amountMoney(HttpServletRequest request){
        
        Userbasicsinfo userbasicsinfo=(Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
        
        String oneMonth=null==dao.findObjectBySql(SQLUtils.NEXT_MONTH_MONEY, userbasicsinfo.getId())?"0.00":dao.findObjectBySql(SQLUtils.NEXT_MONTH_MONEY, userbasicsinfo.getId()).toString();
        String threeMonth=null==dao.findObjectBySql(SQLUtils.NEXT_Three_MONTH_MONEY, userbasicsinfo.getId())?"0.00":dao.findObjectBySql(SQLUtils.NEXT_Three_MONTH_MONEY, userbasicsinfo.getId()).toString();
        String sixMonth=null==dao.findObjectBySql(SQLUtils.NEXT_SIX_MONTH_MONEY, userbasicsinfo.getId())?"0.00":dao.findObjectBySql(SQLUtils.NEXT_SIX_MONTH_MONEY, userbasicsinfo.getId()).toString();
        String allMonth=null==dao.findObjectBySql(SQLUtils.ALL_MONEY, userbasicsinfo.getId())?"0.00":dao.findObjectBySql(SQLUtils.ALL_MONEY, userbasicsinfo.getId()).toString();
        request.setAttribute("oneMonth",Arith.round(Double.parseDouble(oneMonth), 2));
        request.setAttribute("threeMonth",Arith.round(Double.parseDouble(threeMonth), 2) );
        request.setAttribute("sixMonth", Arith.round(Double.parseDouble(sixMonth), 2));
        request.setAttribute("allMonth", Arith.round(Double.parseDouble(allMonth), 2));
        request.setAttribute("investment_count", dao.findObjectBySql(SQLUtils.INVESTMENT_COUNT, userbasicsinfo.getId()));
        
    }
    
}
