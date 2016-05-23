package com.phb.puhuibao.web.controller;

 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.MuserImpression;
import com.phb.puhuibao.service.impl.MuserImpressionServiceImpl;

@Controller
@RequestMapping(value = "/muserImpression")
public class MuserImpressionController extends BaseController<MuserImpression, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(MuserImpressionController.class);
	
	@Resource(name = "muserImpressionService")
	public void setBaseService(IBaseService<MuserImpression, String> baseService) {
		super.setBaseService(baseService);
	}
 
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "muserImpressionService")
	private MuserImpressionServiceImpl muserImpressionService;
	
	
	/**
                            新增印象
	 * @param fromuserId 当前用户 id
	 * @param touserId   对此用户添加印象
	 * @param systemImprestions 系统提供的印象id，多id用逗号分隔
	 * @param userImprestions  用户自己输入的印象，多印象用逗号分隔
	 */

	@RequestMapping(value="saveImpression")
	@ResponseBody
	public Map<String, Object> saveImpression(@RequestParam int fromuserId,@RequestParam int touserId, String systemImprestions,String userImprestions) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		if((systemImprestions==null || "".equals(systemImprestions)) &&  (userImprestions==null || "".equals(userImprestions))){//如果用户什么都没做就点保存
			data.put("message", "保存成功！");
			data.put("status", 1);
			return data;
		}else{
			try {
				if(!(systemImprestions==null || "".equals(systemImprestions))){ //系统定义印象不为空
					   String[] im	= systemImprestions.split(",");
					   for( int i=0;i<im.length;i++){
						   MuserImpression entity = new MuserImpression();
						   entity.setFromuserId(fromuserId);
						   entity.setTouserId(touserId);
						   entity.setImpressionType("0");
						   entity.setImpressionContent(im[i]);
						   this.getBaseService().save(entity);
					   }
					} 
				if(!(userImprestions==null || "".equals(userImprestions))){ //用户定义印象不为空
							   String[] im	= userImprestions.split(",");
							   for( int i=0;i<im.length;i++){
								   MuserImpression entity = new MuserImpression();
								   entity.setFromuserId(fromuserId);
								   entity.setTouserId(touserId);
								   entity.setImpressionType("1");
								   entity.setImpressionContent(im[i]);
								   this.getBaseService().save(entity);
							   }
							}
						
					 
				 
			} catch (Exception e) {
				 
				data.put("message", "网络异常！");
				data.put("status", 0);
				return data;
			}
			
			
			
			

		}
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	
	/**
	    获取当前用户的所有印象
	* @param  
	*/
	@RequestMapping(value="getSelfImpressions")
	@ResponseBody
	public Map<String, Object> getSelfImpressions(@RequestParam int muserId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("touserId", muserId);
		List<MuserImpression> impression = this.getBaseService().findList(params);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", impression);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
	
	
	/**
	    获取自己给朋友的印象
	* @param  currentUserId 当前用户的muid
	* @param  friendId   朋友的muid
	*/
	@RequestMapping(value="getFriendImpressions")
	@ResponseBody
	public Map<String, Object> getFriendImpressions(@RequestParam int currentUserId, @RequestParam int friendId) {
		Map<String, Object> data = new HashMap<String, Object>();
		String sql="select a.impression_id,  if(b.impression_content is NULL, a.impression_content,b.impression_content) as impression_content from  (SELECT impression_id , group_concat( DISTINCT impression_content ) impression_content FROM phb_muser_impression where fromuser_id="+currentUserId+" and touser_id="+friendId+"  GROUP BY impression_content order by impression_id desc) a  LEFT JOIN phb_impression_list b ON a.impression_content = b.impression_id";
		List<Map<String, Object>> imlist	=null;  
		try {
			imlist = this.jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			data.put("message", "错误！" + e.getMessage());
			data.put("status", 0);			
			return data;
		}
 
 
		data.put("result", imlist);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	

	/**
	     删除印象  只有自己删除自己加的印象
	* @param impressionId 印象id
	*/
	
	@RequestMapping(value = "deleteImpressionById")
	@ResponseBody
	public Map<String, Object> deleteImpressionById(@RequestParam int impressionId) {
		 
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("impressionId", impressionId);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			this.getBaseService().delete(param);
		} catch (Exception e) {
	
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}
		data.put("result", impressionId);
		data.put("message", "删除成功！");
		data.put("status", 1);
		return data;
	}
 
 
}
