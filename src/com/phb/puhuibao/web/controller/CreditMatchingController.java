package com.phb.puhuibao.web.controller;

 
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.BorrowerManagement;
import com.phb.puhuibao.entity.CreditMatching;
 

@Controller
@RequestMapping(value = "/creditMatching")
public class CreditMatchingController extends BaseController<CreditMatching, String> {
	
	public int num = 7;
	
	@Resource(name = "creditMatchingService")
	public void setBaseService(IBaseService<CreditMatching, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@javax.annotation.Resource(name = "borrowerManagementService")
	private IBaseService<BorrowerManagement, String> borrowerService;
	
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	
	// min 到 max 之间的n个不同的随机数
	public   int[] randomCommon(int min, int max, int n){  
	    if (n > (max - min + 1) || max < min) {  
	           return null;  
	       }  
	    int[] result = new int[n];  
	    int count = 0;  
	    while(count < n) {  
	        int num = (int) (Math.random() * (max - min)) + min;  
	        boolean flag = true;  
	        for (int j = 0; j < n; j++) {  
	            if(num == result[j]){  
	                flag = false;  
	                break;  
	            }  
	        }  
	        if(flag){  
	            result[count] = num;  
	            count++;  
	        }  
	    }  
	    return result;  
	}  
	
	
	public  int getnewradom(int sum,int count){
		Random rdm=new Random();
		int temp = rdm.nextInt()%sum;
		double persent= sum/(count-1);
		double minpersent= sum/((count-1)*2);
		while(temp > persent ||  temp < minpersent){
			//temp = rdm.nextInt(sum);
			temp = (int) Math.round(Math.random()*100);
		}
		 return temp;
	}
	// 总和为 sum 的count个随机数
	public List<Integer> GetResult(int sum, int count)
    {
		Random rdm=new Random();
        List<Integer> result = new ArrayList<Integer>(count);
        int last =sum;
        for(int i=0;i<count-1;i++){
        	int temp = getnewradom(sum,count);
        	last = last - temp;
        	result.add(temp);
        }
        
        result.add(last);
		if(last<=0){
			return null;
		}   
      
        return result;
    }
	
	private  Date getLastDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }
	
	private  List<CreditMatching> getRatio(List<BorrowerManagement> doinglist, double sum,String batchName){
		
		List<CreditMatching> newlist = new ArrayList<CreditMatching>();
		
		 double smallmountsum=0.0;
		    double smallmountper=0.0;
		    int smallnum = 0;
		    DecimalFormat ddf = new DecimalFormat("#.00");  
         
         
		    for(int i=0;i<num;i++){
		    	BorrowerManagement bb = doinglist.get(i);
		    	if((bb.getContractAmount()-bb.getFilledAmount())<=100000){//小额的先满足
		    		smallnum++;
		    		smallmountsum+=bb.getContractAmount()-bb.getFilledAmount();
		    		smallmountper+=(bb.getContractAmount()-bb.getFilledAmount())/sum;

		    		CreditMatching cm = new CreditMatching();
		    		cm.setBorrowId(bb.getRecordId());
		    		cm.setBatchName(batchName);
			    	cm.setBorrowerName(bb.getBorrowerName());
			    	cm.setBorrowPersentage( Double.valueOf(ddf.format( (bb.getContractAmount()-bb.getFilledAmount())/sum)));
			    	cm.setBorrowAmount(bb.getContractAmount()-bb.getFilledAmount());//现有债权
			    	cm.setBorrowLeft(0);//一次性满足
			    	newlist.add(cm);
		    		//bb.getContractAmount()-bb.getFilledAmount()-newpersents.get(i)/100.0*sum
		    	}

		    }
		    
		    List<Integer> newpersents =  GetResult(100-(int)Math.round(smallmountper*100),num-smallnum);//形成剩余的百分比的随机分配
		    int j=0;
		    for(int i=0;i<num;i++){
		      
		    	BorrowerManagement bb = doinglist.get(i);
		    	if((bb.getContractAmount()-bb.getFilledAmount())>100000){//小额的先满足
		    		 

		    		CreditMatching cm = new CreditMatching();
		    		cm.setBorrowId(bb.getRecordId());
		    		cm.setBatchName(batchName);
			    	cm.setBorrowerName(bb.getBorrowerName());
			    	cm.setBorrowPersentage(newpersents.get(j)/100.0);
			    	cm.setBorrowAmount(bb.getContractAmount()-bb.getFilledAmount());
			    	cm.setBorrowLeft(bb.getContractAmount()-bb.getFilledAmount()-newpersents.get(j++)/100.0*sum);
			    	newlist.add(cm);
		    		 
		    	}

		    }
		    
		    return newlist;
		
	}
	
	/**
	 * 获得分配方案
 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getRatio")
	@ResponseBody
	public  Map<String, Object> getRatio(@RequestParam double sum ) throws Exception {
	 
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		Date date = new Date();
        String batchName="";

		 SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		 SimpleDateFormat df1=new SimpleDateFormat("yyyyMM");
		 
		 String nowdate= df.format(date); 
		 String nowmonth= df1.format(date); 
	     String lastmonth =   df1.format(getLastDate(date)) ;
	     
	     String last25 = lastmonth+"25";
		 String this10 = nowmonth+"10";
		 String this25 = nowmonth+"26";
 
		 Date todaydate =  df.parse(nowdate);
		 Date last25d =    df.parse(last25);
		 Date this10d =    df.parse(this10);
		 Date this25d =    df.parse(this25);
		 
         if(todaydate.after(this10d) && this25d.after(todaydate) || todaydate.equals(this25d)){//如果现在的时间是在这个月的10号和25号之间,包含15号
        	batchName = nowmonth+"-02";
         }else  if(todaydate.after(last25d) && this10d.after(todaydate) || todaydate.equals(this10d)){// 如果现在是本月10号（包含）和上月25号之间 
        	batchName = nowmonth+"-01";
         }else{
 			data.put("result", null);
 			data.put("message", "不能在今天产生债权分配方案，改天再试！");
 			data.put("status", 0);
 			return data;	
 
         }
		 
		
		
		params.put("batchName", batchName);
		 
		List<CreditMatching> cmlist = this.getBaseService().findList(params);
		if(cmlist.size()==0){//需要新生成
			
			params = new HashMap<String,Object>();
			params.put("filledFinished", 1);//0：没有任何分配  1：分配中2： 分配完成
			params.put("orderBy", "filled_pct");
			params.put("order", "desc");
			List <BorrowerManagement>doinglist = borrowerService.findList(params);//分配到一半
			 
			int doingnum=0;
			 for(BorrowerManagement bm:doinglist){
				 if(bm.getFilledPct()>0){
					 doingnum++;
				 }
			 }
			int   leftnum = num-doingnum;
			
			params = new HashMap<String,Object>();
			params.put("filledFinished", 0);//还没有分配
			 
			List <BorrowerManagement> todolist = borrowerService.findList(params);//还没有任何分配的人
			int todonum = todolist.size();
			
			
		    int [] randomnum = randomCommon(0,todonum-1,leftnum);
		    
		    for(int i =0;i<randomnum.length;i++){
		    	doinglist.add(todolist.get(randomnum[i]));
		    }
		    
		    int ok=0;
		    List<CreditMatching> newlist = getRatio(doinglist,sum,batchName);
//		    while (ok < num){
//		    	ok=0;
//		    	  newlist = getRatio(doinglist,sum);
//		    	  for(int i =0;i<newlist.size();i++){
//		    		  if(newlist.get(i).getBorrowLeft()>=0){
//		    			  ok++;
//		    		  }
//		    	  }
//		    	
//		    }
		    
		 
		   
		    
		   
			
 
			data.put("result", newlist);
			data.put("message", "成功产生新的分配方案！");
			data.put("status", 1);
			return data;	
			
		}else{//已经生成，提醒
			data.put("result", null);
			data.put("message", "本周期债权分配方案已经生成过了。");
			data.put("status", 2);
			return data;	
			
		}
		
		
 

		
	}
	
	 

}
