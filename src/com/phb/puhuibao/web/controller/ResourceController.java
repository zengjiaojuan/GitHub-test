package com.phb.puhuibao.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.context.AppContext;
import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.Evaluate;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserHexagram;
import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.ResourceGallery;
import com.phb.puhuibao.entity.ResourceOrder;
import com.phb.puhuibao.entity.ResourceType;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.service.ResourceService;

@Controller
@RequestMapping(value = "/resource")
public class ResourceController extends BaseController<Resource, String> {
	@javax.annotation.Resource(name = "resourceService")
	public void setBaseService(IBaseService<Resource, String> baseService) {
		super.setBaseService(baseService);
	}

	@javax.annotation.Resource(name = "resourceService")
	private ResourceService resourceService;
	
	@javax.annotation.Resource(name = "resourceService")
	private IBaseService<Resource, String> resourceService1;

	@javax.annotation.Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@javax.annotation.Resource(name = "mobileUserHexagramService")
	private IBaseService<MobileUserHexagram, String> mobileUserHexagramService;

	@javax.annotation.Resource(name = "resourceGalleryService")
	private IBaseService<ResourceGallery, String> resourceGalleryService;

	@javax.annotation.Resource(name = "resourceOrderService")
	private IBaseService<ResourceOrder, String> resourceOrderService;

	@javax.annotation.Resource(name = "resourceTypeService")
	private IBaseService<ResourceType, String> resourceTypeService;

	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@javax.annotation.Resource(name = "evaluateService")
	private IBaseService<Evaluate, String> evaluateService;
	
	@javax.annotation.Resource(name = "userInvestmentService")
	private IBaseService<UserInvestment, String> userInvestmentService;
	
	@javax.annotation.Resource(name = "itemInvestmentService")
	private IBaseService<ItemInvestment, String> itemInvestmentService;

	@javax.annotation.Resource(name = "appContext")
	private AppContext appContext;

	/**
	 * 资源列表
	 * @param pageno
	 * @param type
	 * @param category
	 * @param sex
	 * @param level
	 * @param liveness
	 * @param distance
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
//	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String typeId, @RequestParam String category, @RequestParam String sex, @RequestParam String level, @RequestParam String liveness, @RequestParam String distance, @RequestParam double longitude, @RequestParam double latitude) {
//		Pager<Resource> pager = new Pager<Resource>();
//		pager.setReload(true);
//		pager.setCurrent(pageno);
//		Map<String, Object> params = new HashMap<String, Object>();
//		if (typeId.length() > 0 && StringUtils.isNumeric(typeId)) {
//			params.put("typeId", typeId);
//		}
//		if (category.length() > 0) {
//			params.put("category", category);
//		}
//		if (sex.length() > 0 && StringUtils.isNumeric(sex)) {
//			params.put("sex", sex);
//		}
//		if (level.length() > 0 && StringUtils.isNumeric(level)) {
//			params.put("level", level);
//		}
//		if (liveness.length() > 0 && StringUtils.isNumeric(liveness)) {
//			params.put("liveness", liveness);
//		}
//		if (distance.length() > 0 && StringUtils.isNumeric(distance)) {
//			params.put("distance", distance);
//		}
//		params.put("longitude", longitude);
//		params.put("latitude", latitude);
//		params.put("status", 1);
//		params.put("gnumber", 0);
//		params.put("orderBy", "distance");
//		Pager<Resource> p = this.getBaseService().findByPager(pager, params);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for (Resource resource : p.getData()) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("resourceId", resource.getResourceId());
//			map.put("mUserId", resource.getmUserId());
//			map.put("age", resource.getAge());
//			map.put("constellation", resource.getConstellation());
//			map.put("photo", resource.getPhoto());
//			map.put("nickname", resource.getNickname());
//			map.put("sex", resource.getSex());
//			map.put("liveness", resource.getLiveness());
//			map.put("level", resource.getLevel());
//			map.put("name", resource.getName());
//			map.put("price", resource.getPrice());
//			map.put("number", resource.getNumber());
//			map.put("address", resource.getAddress());
//			map.put("typeId", resource.getTypeId());
//			map.put("category", resource.getCategory());
//			map.put("publishTime", resource.getPublishTime());
//			map.put("longitude", resource.getLongitude());
//			map.put("latitude", resource.getLatitude());
//			map.put("resourceDesc", resource.getResourceDesc());
//			map.put("method", resource.getMethod());
//			MobileUserHexagram hexagram = mobileUserHexagramService.getById(resource.getmUserId() + "");
//			map.put("hexagram", hexagram);
//			list.add(map);
//		}
//
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("result", list);
//		data.put("count", p.getTotal());
//		data.put("message", "");
//		data.put("status", 1);
//		return data;
//	}
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String typeId, @RequestParam String sex, @RequestParam String orderBy, @RequestParam double longitude, @RequestParam double latitude) {
		Pager<Resource> pager = new Pager<Resource>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String, Object> params = new HashMap<String, Object>();
		if (typeId.length() > 0 && StringUtils.isNumeric(typeId)) {
			params.put("typeId", typeId);
		}
		if (sex.length() > 0 && StringUtils.isNumeric(sex)) {
			params.put("sex", sex);
		}
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		params.put("status", 1);
		if (orderBy.length() > 0) {
			params.put("orderBy", orderBy);
		}
		if ("distance".equals(orderBy)) {
			params.put("order", "asc");
		} else {
			params.put("order", "desc");
		}
		Pager<Resource> p = this.getBaseService().findByPager(pager, params);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Resource resource : p.getData()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resourceId", resource.getResourceId());
			map.put("mUserId", resource.getmUserId());
			map.put("age", resource.getAge());
			map.put("constellation", resource.getConstellation());
			map.put("photo", resource.getPhoto());
			map.put("nickname", resource.getNickname());
			map.put("sex", resource.getSex());
			map.put("liveness", resource.getLiveness());
			map.put("level", resource.getLevel());
			map.put("name", resource.getName());
			map.put("price", resource.getPrice());
			map.put("number", resource.getNumber());
			map.put("address", resource.getAddress());
			map.put("typeId", resource.getTypeId());
			map.put("category", resource.getCategory());
			map.put("publishTime", resource.getPublishTime());
			map.put("longitude", resource.getLongitude());
			map.put("latitude", resource.getLatitude());
			map.put("resourceDesc", resource.getResourceDesc());
			map.put("method", resource.getMethod());
			MobileUserHexagram hexagram = mobileUserHexagramService.getById(resource.getmUserId() + "");
			map.put("hexagram", hexagram);
			
			String resourcelevel = "";
			Double amount =  resource.getPrice()*resource.getNumber();//总金额=单价*个数
			if(amount>=10000){
				resourcelevel="S";
			}else if(amount>=5000 && amount<10000){
				resourcelevel="A";
			}else if(amount>=2000 && amount<5000){
				resourcelevel="B";
			}else if(amount>=500 && amount<2000){
				resourcelevel="C";
			}else if(amount>=100 && amount<500){
				resourcelevel="D";
			}else{
				resourcelevel="E";
			} 
			map.put("resourcelevel", resourcelevel);
			String sql = "select count(1) from phb_resource_order where status=1 and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			map.put("numberOfOrders", count.get(0).get("count(1)"));
			map.put("number", resource.getNumber());
			list.add(map);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", list);
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="queryPage")
	@ResponseBody
	public Map<String, Object> queryPage(@RequestParam int pageno, @RequestParam String category, @RequestParam String typeId, @RequestParam String sex, @RequestParam String orderBy, @RequestParam double longitude, @RequestParam double latitude) {
		Pager<Resource> pager = new Pager<Resource>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String, Object> params = new HashMap<String, Object>();
		if (!"".equals(category) && StringUtils.isNumeric(category)) {
			params.put("category", category);
		}
		if (typeId.length() > 0 && StringUtils.isNumeric(typeId)) {
			params.put("typeId", typeId);
		}
		if (sex.length() > 0 && StringUtils.isNumeric(sex)) {
			params.put("sex", sex);
		}
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		params.put("status", 1);
		if (orderBy.length() > 0) {
			params.put("orderBy", orderBy);
		}
		if ("distance".equals(orderBy)) {
			params.put("order", "asc");
		} else {
			params.put("order", "desc");
		}
		Pager<Resource> p = this.getBaseService().findByPager(pager, params);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Resource resource : p.getData()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resourceId", resource.getResourceId());
			map.put("mUserId", resource.getmUserId());
			map.put("age", resource.getAge());
			map.put("constellation", resource.getConstellation());
			map.put("photo", resource.getPhoto());
			map.put("nickname", resource.getNickname());
			map.put("sex", resource.getSex());
			map.put("liveness", resource.getLiveness());
			map.put("level", resource.getLevel());
			map.put("name", resource.getName());
			map.put("price", resource.getPrice());
			map.put("number", resource.getNumber());
			map.put("address", resource.getAddress());
			map.put("typeId", resource.getTypeId());
			map.put("category", resource.getCategory());
			map.put("publishTime", resource.getPublishTime());
			map.put("longitude", resource.getLongitude());
			map.put("latitude", resource.getLatitude());
			map.put("resourceDesc", resource.getResourceDesc());
			map.put("method", resource.getMethod());
//			MobileUserHexagram hexagram = mobileUserHexagramService.getById(resource.getmUserId() + "");
//			map.put("hexagram", hexagram);
			map.put("viewedcount", resource.getViewedcount());
			map.put("distance", resource.getDistance());
			map.put("VIP", processVIP(resource.getmUserId()));
			
			String resourcelevel = "";
			Double amount =  resource.getPrice()*resource.getNumber();//总金额=单价*个数
			if(amount>=10000){
				resourcelevel="S";
			}else if(amount>=5000 && amount<10000){
				resourcelevel="A";
			}else if(amount>=2000 && amount<5000){
				resourcelevel="B";
			}else if(amount>=500 && amount<2000){
				resourcelevel="C";
			}else if(amount>=100 && amount<500){
				resourcelevel="D";
			}else{
				resourcelevel="E";
			} 
			map.put("resourcelevel", resourcelevel);
			String sql = "select count(1) from phb_resource_order where status=1 and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			map.put("numberOfOrders", count.get(0).get("count(1)"));
			map.put("number", resource.getNumber());
			list.add(map);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", list);
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 我的资源  我发布的任务
	 * @param pageno
	 * @param muid
	 * @return
	1、删除  条件category=1, status=0 结果删除
	   付款  条件category=1, status=0 结果status=1
	2、选择  条件status=1，method=1双向，报名人数orderstatus=1够 结果status=2
	3、订单完成  条件method=0单向 or status=2，category=0供，orderstatus=1报名数>0 结果orderstatus=2
	4、订单确认  条件orderstatus=2，category=1需 结果orderstatus=3，达到人数则status=3,与前面不会重叠
	5、订单评价  条件orderstatus=3，category=1需 结果orderstatus=4，可能与4重叠
	6、评价回复  条件orderstatus=4，category=0供 结果orderstatus=5【在个人名片回复】
	 */
	@RequestMapping(value="myResources")
	@ResponseBody
	public Map<String, Object> myResources(@RequestParam int pageno, @RequestParam String muid) {
		Pager<Resource> pager = new Pager<Resource>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		Pager<Resource> p = this.getBaseService().findByPager(pager, params);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Resource resource : p.getData()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resourceId", resource.getResourceId());
			map.put("name", resource.getName());
			map.put("publishTime", resource.getPublishTime());
			map.put("status", resource.getStatus());
			ResourceType type = resourceTypeService.getById(resource.getTypeId() + "");
			map.put("typeName", type.getName());
			map.put("price", resource.getPrice());
			map.put("action", getResourceActions(resource));
			list.add(map);
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", list);
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="queryByKeyword")
	@ResponseBody
	public Map<String, Object> queryByKeyword(@RequestParam int pageno, @RequestParam String keyword) {
		Pager<Resource> pshare = new Pager<Resource>();
		pshare.setReload(true);
		pshare.setCurrent(pageno);
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", keyword);
		param.put("status", 1);
		param.put("orderBy", "a.update_time");
		param.put("order", "desc");
		Pager<Resource> p=this.getBaseService().findByPager(pshare, param);
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Resource resource : p.getData()) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resourceId", resource.getResourceId());
			map.put("mUserId", resource.getmUserId());
			map.put("age", resource.getAge());
			map.put("constellation", resource.getConstellation());
			map.put("photo", resource.getPhoto());
			map.put("nickname", resource.getNickname());
			map.put("sex", resource.getSex());
			map.put("liveness", resource.getLiveness());
			map.put("level", resource.getLevel());
			map.put("name", resource.getName());
			map.put("price", resource.getPrice());
			map.put("number", resource.getNumber());
			map.put("address", resource.getAddress());
			map.put("typeId", resource.getTypeId());
			map.put("category", resource.getCategory());
			map.put("publishTime", resource.getPublishTime());
			map.put("longitude", resource.getLongitude());
			map.put("latitude", resource.getLatitude());
			map.put("resourceDesc", resource.getResourceDesc());
			map.put("method", resource.getMethod());
//			MobileUserHexagram hexagram = mobileUserHexagramService.getById(resource.getmUserId() + "");
//			map.put("hexagram", hexagram);
			
			String resourcelevel = "";
			Double amount =  resource.getPrice()*resource.getNumber();//总金额=单价*个数
			if(amount>=10000){
				resourcelevel="S";
			}else if(amount>=5000 && amount<10000){
				resourcelevel="A";
			}else if(amount>=2000 && amount<5000){
				resourcelevel="B";
			}else if(amount>=500 && amount<2000){
				resourcelevel="C";
			}else if(amount>=100 && amount<500){
				resourcelevel="D";
			}else{
				resourcelevel="E";
			} 
			map.put("resourcelevel", resourcelevel);
			String sql = "select count(1) from phb_resource_order where status=1 and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			map.put("numberOfOrders", count.get(0).get("count(1)"));
			list.add(map);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", list);
		data.put("count", list.size());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 保存
	 * @param muid
	 * @param name
	 * @param price
	 * @param number
	 * @param address
	 * @param publishTime
	 * @param typeId
	 * @param category
	 * @param status
	 * @param resourceDesc
	 * @param method
	 * @param longitude
	 * @param latitude
	 * @param pictureIds
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, 
			@RequestParam String name, 
			@RequestParam double price,
			@RequestParam int number,
			 String address,//地点可为空
			//@RequestParam(value ="publishTime") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date publishTime,
			 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date publishTime,//报名截止时间
			@RequestParam int typeId,
			@RequestParam int category,
			@RequestParam int status,
			@RequestParam String resourceDesc,
			@RequestParam int method,
			  double longitude, //经纬度为空,值为0
			  double latitude,//经纬度为空,值为0
			@RequestParam String pictureIds) {
		Resource entity = new Resource();
		entity.setmUserId(muid);
		entity.setName(name);
		entity.setPrice(price);
		entity.setNumber(number);
		entity.setAddress(address);
		Date publishtime=publishTime;
		if(publishTime==null){//如果时间为空,则需要添加默认时间,7天后
			Date d = new Date();
			 publishtime=  new Date(d.getTime() + (long)7 * 24 * 60 * 60 * 1000);
		}
		entity.setPublishTime(publishtime);
		entity.setTypeId(typeId);
		entity.setCategory(category);
		entity.setStatus(status);
		if (category == 1) {
			entity.setStatus(0);
		}
		entity.setResourceDesc(resourceDesc);
		entity.setMethod(method);
		entity.setCreateTime(new Date());
		if(longitude>0){
			entity.setLongitude(longitude);
			entity.setLatitude(latitude);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			entity = this.getBaseService().save(entity);
			String[] ids = pictureIds.split(",");
			for (String id : ids) {
				if ("".equals(id) || "null".equals(id)) {
					continue;
				}
				ResourceGallery gallery = resourceGalleryService.getById(id);
				if (gallery == null) {
					gallery = new ResourceGallery();
					gallery.setPictureId(id);
					gallery.setResourceId(entity.getResourceId());
					resourceGalleryService.save(gallery);
				} else {
					gallery.setResourceId(entity.getResourceId());
					resourceGalleryService.update(gallery);
				}
			}
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("result", entity.getResourceId());
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	
	/**
	 * 保存
	 * @param muid
	 * @param name
	 * @param price
	 * @param number
	 * @param address
	 * @param publishTime
	 * @param typeId
	 * @param category
	 * @param status
	 * @param resourceDesc
	 * @param method
	 * @param longitude
	 * @param latitude
	 * @param pictureIds
	 * @return
	 */
	@RequestMapping(value="resourceSave")
	@ResponseBody
	public Map<String, Object> resourceSave(@RequestParam int muid, 
			@RequestParam String name, 
			@RequestParam double price,
			@RequestParam int number,
			 String address,//地点可为空
			//@RequestParam(value ="publishTime") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date publishTime,
			 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date publishTime,//报名截止时间
			@RequestParam int typeId,
			@RequestParam int category,
			@RequestParam int status,
			@RequestParam String resourceDesc,
			@RequestParam int method,
			  double longitude, //经纬度为空,值为0
			  double latitude,//经纬度为空,值为0
			@RequestParam String pictureIds) {
		Resource entity = new Resource();
		entity.setmUserId(muid);
		entity.setName(name);
		entity.setPrice(price);
		entity.setNumber(number);
		entity.setAddress(address);
		Date publishtime=publishTime;
		if(publishTime==null){//如果时间为空,则需要添加默认时间,7天后
			Date d = new Date();
			 publishtime=  new Date(d.getTime() + (long)7 * 24 * 60 * 60 * 1000);
		}
		entity.setPublishTime(publishtime);
		
		Date validTime  = new Date(publishtime.getTime() + (long)10 * 24 * 60 * 60 * 1000);
		entity.setValidTime(validTime); // 任务有效时间为报名截止时间后第10天
		
		entity.setTypeId(typeId);
		entity.setCategory(category);
		entity.setStatus(status);
		if (category == 1) {
			entity.setStatus(0);
		}
		entity.setResourceDesc(resourceDesc);
		entity.setMethod(method);
		entity.setCreateTime(new Date());
		entity.setLongitude(longitude);
		entity.setLatitude(latitude);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			entity = this.getBaseService().save(entity);
			String[] ids = pictureIds.split(",");
			for (String id : ids) {
				if ("".equals(id) || "null".equals(id)) {
					continue;
				}
				ResourceGallery gallery = resourceGalleryService.getById(id);
				if (gallery == null) {
					gallery = new ResourceGallery();
					gallery.setPictureId(id);
					gallery.setResourceId(entity.getResourceId());
					resourceGalleryService.save(gallery);
				} else {
					gallery.setResourceId(entity.getResourceId());
					resourceGalleryService.update(gallery);
				}
			}
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("result", entity.getResourceId());
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	
	/**
	 * update
	 * @param resourceId
	 * @param name
	 * @param price
	 * @param number
	 * @param address
	 * @param publishTime
	 * @param typeId
	 * @param status
	 * @param resourceDesc
	 * @param method
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	@RequestMapping(value="update")
	@ResponseBody
	public Map<String, Object> update(@RequestParam int resourceId, 
			@RequestParam String name, 
			@RequestParam double price,
			@RequestParam int number,
			@RequestParam String address,
			@RequestParam Date publishTime,
			@RequestParam int typeId,
			@RequestParam int category,
			@RequestParam int status,
			@RequestParam String resourceDesc,
			@RequestParam int method,
			@RequestParam double longitude, 
			@RequestParam double latitude) {
		Map<String, Object> data = new HashMap<String, Object>();
		Resource entity = this.getBaseService().getById(resourceId + "");
		if (entity == null) {
			data.put("message", "该供需资源不存在！");
			data.put("status", 0);
			return data;
		}

		Resource resource = new Resource();
		resource.setResourceId(resourceId);
		if (!name.equals(entity.getName())) {
			resource.setName(name);
		}
		if (entity.getPrice() == null || price != entity.getPrice()) {
			resource.setPrice(price);
		}
		if (entity.getNumber() == null || number != entity.getNumber()) {
			resource.setNumber(number);
		}
		if (!address.equals(entity.getAddress())) {
			resource.setAddress(address);
		}
		if (!publishTime.equals(entity.getPublishTime())) {
			resource.setPublishTime(publishTime);
		}
		if (entity.getTypeId() == null || typeId != entity.getTypeId()) {
			resource.setTypeId(typeId);
		}
		if (entity.getCategory() == null || category != entity.getCategory()) {
			resource.setCategory(category);
		}
		if (entity.getStatus() == null || status != entity.getStatus()) {
			resource.setStatus(status);
		}
		if (resourceDesc.equals(entity.getResourceDesc())) {
			resource.setResourceDesc(resourceDesc);
		}
		if (entity.getMethod() == null || method != entity.getMethod()) {
			resource.setMethod(method);
		}
		if (entity.getLongitude() == null || longitude != entity.getLongitude()) {
			resource.setLongitude(longitude);
		}
		if (entity.getLatitude() == null || latitude != entity.getLatitude()) {
			resource.setLatitude(latitude);
		}

		try {
			this.getBaseService().update(resource);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam int resourceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceId", resourceId);

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			this.getBaseService().delete(params);
		} catch (Exception e) {
			data.put("message", "删除失败！" + e.getMessage());
			data.put("status", 0);
			return data;
		}

		data.put("message", "删除成功！");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 任务详细
	 * @param muid
	 * @param resourceId
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	@RequestMapping(value="getDetail")
	@ResponseBody
	public Map<String, Object> getDetail(@RequestParam int muid, @RequestParam String resourceId, @RequestParam double longitude, @RequestParam double latitude) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		Resource resource = this.getBaseService().unique(params);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resourceId", resource.getResourceId());
		map.put("name", resource.getName());
		map.put("price", resource.getPrice());
		map.put("publishTime", resource.getPublishTime());
		map.put("distance", resource.getDistance());
		map.put("address", resource.getAddress());
		map.put("nickname", resource.getNickname());
		map.put("resourceDesc", resource.getResourceDesc());
		map.put("category", resource.getCategory());
		map.put("mUserId", resource.getmUserId());
		map.put("number", resource.getNumber());

		MobileUser user = mobileUserService.getById(resource.getmUserId() + "");
		map.put("photo", user.getPhoto());
		map.put("level", user.getLevel());
		map.put("VIP", processVIP(resource.getmUserId()));

		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		List<ResourceGallery> gallery = resourceGalleryService.findList(params);
		map.put("gallery", gallery);
		ResourceType type = resourceTypeService.getById(resource.getTypeId() + "");
		map.put("defaultImage", type.getDefaultImage());
		map.put("orderId", "");
		//任务状态：0=未付款（category=1，可以自己删除），1=报名中（category=1，已支付），2=报名结束，3=已完成，4=取消
		//订单状态：0=未付款（category=0，可以自己删除），1=报名（category=0，已支付），2=报名选中，3=已完成，4=已确认（到账），5=已评价，6=已回复，7=未完成，8=取消
		if (resource.getmUserId() == muid) {
			map.put("status", resource.getStatus());
			map.put("action", getResourceActions(resource));
//			int action = 0;
//			if (resource.getStatus() == 0) {
//				action = 1; // 付款
//			} else if (resource.getStatus() == 1) {
//				action = 2; // 报名中
//			} else if (resource.getStatus() == 2) {
//				action = 3; // 报名结束
//			} else if (resource.getStatus() == 3) {
//				action = 4; // 已完成
//			}
//			// 任务action：1删除、付款 2选择报名 3订单完成 4订单评价、5回复
//			if (resource.getStatus() == 1 && resource.getMethod() == 1) {
//				String sql = "select count(1) from phb_resource_order where status=1 and resource_id=" + resource.getResourceId();
//				List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
//				if ((Long) count.get(0).get("count(1)") >= resource.getNumber()) {
//					action = 5; // 选择报名
//				}
//			}
//			if ((resource.getStatus() == 1 && resource.getMethod() == 0) || resource.getStatus() >= 2) {
//				if (resource.getCategory() == 1) {
//					String sql = "select count(1) from phb_resource_order where status=1 and resource_id=" + resource.getResourceId();
//					List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
//					if ((Long) count.get(0).get("count(1)") > 0) {
//						action = 6; // 订单完成，支付到账
//					}
//					//action = 7; // 评价
//				} else {
//					//action = 8; // 回复
//				}
//			}
//			map.put("action", action);
		} else  {
			if (resource.getStatus() == 1 ) {
				Resource entity = new Resource();
				entity.setResourceId(resource.getResourceId());
				entity.setViewedcount(resource.getViewedcount() + 1);
				this.getBaseService().update(entity);
			}
			params = new HashMap<String,Object>();
			params.put("resourceId", resourceId);
			params.put("mUserId", muid);
			ResourceOrder order = resourceOrderService.unique(params);
			if (order == null) {
				map.put("action", 0);
			} else {
				map.replace("orderId", order.getOrderId());
				map.put("status", order.getStatus());
				map.put("action", getOrderAction(order.getStatus(), resource));
//				switch (order.getStatus()) {
//				case 0:
//					map.put("action", 0); // 未付款
//					break;
//				case 1:
//					if (resource.getCategory() == 0 && (resource.getStatus() == 2 || resource.getMethod() == 0)) {
//						map.put("action", 3); // 任务完成
//					} else if (resource.getMethod() == 0 || (resource.getMethod() == 1 && resource.getStatus() == 2)) {
//						map.put("action", 2); // 报名已确认
//					} else {
//						map.put("action", 1); // 报名待确认
//					}
//					break;
//				case 2:
//					Map<String,Object> para = new HashMap<String,Object>();
//					para.put("resourceId", resourceId);
//					if (resource.getCategory() == 0) {
//						para.put("mUserId", muid);
//						Evaluate evaluate = evaluateService.unique(para);
//						if (evaluate == null) {
//							map.put("action", 4); // 评价
//						} else {
//							map.put("action", 5); // 已评价
//						}
//					} else { // 回复
//						map.put("action", 6);
////						para.put("toUserId", muid);
////						Evaluate evaluate = evaluateService.unique(para);
////						if (evaluate == null) {
////							map.put("action", 6); // 回复
////						} else {
////							map.put("action", 7); // 已回复
////						}
//					}
//					break;
//				default:
//					map.put("action", 8); // 报名取消
//					break;
//				}
			}
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", map);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	private String getOrderAction(Integer status, Resource resource) {
//		0、报名  条件status=1，未报名 结果orderstatus=1（category=0，未支付），其他orderstatus=1
//		1、删除  条件category=0, orderstatus=0 结果删除
//		   付款  条件category=0, orderstatus=0 结果orderstatus=1
//		2、完成  条件category=1，orderstatus=2 or 7 结果orderstatus=3【3天后自动确认】
//		3、确认  条件category=0, orderstatus=3 结果orderstatus=4，达到人数status=3
//		   未完成  条件category=0, orderstatus=3 结果orderstatus=7
//		   （挂起）仲裁  条件category=0, orderstatus=3 结果orderstatus=4，达到人数status=3，确实完成了扣报名者10%的价格给owner；未完成扣owner10%的价格并退款给报名者
//		4、评价  条件category=0, orderstatus=4 结果orderstatus=5
//		5、回复  条件category=1, orderstatus=5 结果orderstatus=6
//		6、取消  条件orderstatus=1 结果orderstatus=8，（category=0，退款）
		String action = "";
		switch (status) {
			case 0:
				action = "1"; // 删除或付款
				break;
			case 1:
				action = "6"; // 取消
				break;
			case 2:
				if (resource.getCategory() == 1) {
					action = "2"; // 任务完成
				}
				break;
			case 3:
				if (resource.getCategory() == 0) {
					action = "3"; // 确认支付或未完成
				}
				break;
			case 4:
				if (resource.getCategory() == 0) {
					action = "4"; // 评价 
				}
				break;
			case 5:
				if (resource.getCategory() == 1) {
					action = "5"; // 回复 
				}
				break;
			case 7:
				if (resource.getCategory() == 1 && resource.getStatus() == 7) {
					action = "2"; // 任务完成
				}
				break;
		}
		return action;
	}

	private String getResourceActions(Resource resource) {
//		1、删除  条件category=1, status=0 结果删除
//		   付款  条件category=1, status=0 结果status=1
//		2、选择  条件status=1，报名人数orderstatus=2满，结果status=2，（category=0，给报名者退款，orderstatus=8）
//		3、订单完成  条件status=1 or 2，orderstatus=2 or 7，category=0，结果orderstatus=3【3天后自动确认】
//		4、订单确认  条件orderstatus=3，category=1需 结果orderstatus=4，达到人数则status=3
//		   订单未完成  条件orderstatus=3，category=1需 结果orderstatus=7
//		   （挂起）仲裁  条件orderstatus=3，category=1需 结果orderstatus=4，达到人数则status=3，确实完成了扣owner10%的价格给报名者；未完成扣报名者10%的价格并退款给owner
//		5、订单评价  条件orderstatus=4，category=1需 结果orderstatus=5
//		6、评价回复  条件orderstatus=5，category=0供 结果orderstatus=6
//		7、取消  条件status=1，报名人数(orderstatus=1 or 2)为0，结果status=4，（category=1，退款）
		String actions = "";
		if (resource.getStatus() == 0 && resource.getCategory() == 1) {
			actions = "1"; // 删除或付款
		} else if (resource.getStatus() == 1) {
			actions = "2"; // 选择
			String sql = "select count(1) from phb_resource_order where status=3 and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			if ((Long) count.get(0).get("count(1)") > 0) {
				actions += ",4"; // 确认支付或未完成
			}
		}
		if ((resource.getStatus() == 1 || resource.getStatus() == 2) && resource.getCategory() == 0) {
			String sql = "select count(1) from phb_resource_order where (status=2 or status=7) and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			if ((Long) count.get(0).get("count(1)") > 0) {
				if ("".equals(actions)) {
					actions = "3"; // 完成
				} else {
					actions += ",3"; // 完成
				}
			}
			sql = "select count(1) from phb_resource_order where status=5 and resource_id=" + resource.getResourceId();
			count = this.jdbcTemplate.queryForList(sql);
			if ((Long) count.get(0).get("count(1)") > 0) {
				if ("".equals(actions)) {
					actions = "6"; // 回复
				} else {
					actions += ",6"; // 回复
				}
			}
		} else if ((resource.getStatus() == 1 || resource.getStatus() == 2) && resource.getCategory() == 1) {
			String sql = "select count(1) from phb_resource_order where status=3 and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			if ((Long) count.get(0).get("count(1)") > 0) {
				if ("".equals(actions)) {
					actions = "4"; // 确认支付
				} else {
					actions += ",4"; // 确认支付
				}
			}
			sql = "select count(1) from phb_resource_order where status=4 and resource_id=" + resource.getResourceId();
			count = this.jdbcTemplate.queryForList(sql);
			if ((Long) count.get(0).get("count(1)") > 0) {
				if ("".equals(actions)) {
					actions = "5"; // 评价
				} else {
					actions += ",5"; // 评价
				}
			}
		}
		if (resource.getStatus() == 1) {
			String sql = "select count(1) from phb_resource_order where (status=1 or status=2) and resource_id=" + resource.getResourceId();
			List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
			if ((Long) count.get(0).get("count(1)") == 0) {
				if ("".equals(actions)) {
					actions = "7"; // 取消
				} else {
					actions += ",7"; // 取消
				}
			}
		}
		return actions;
	}

	private String processVIP(int muid) {
		long amount = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		List<UserInvestment> investments = userInvestmentService.findList(params);
		for (UserInvestment investment : investments) {
			amount += investment.getInvestmentAmount();
		}
		List<ItemInvestment> itemInvestments = itemInvestmentService.findList(params);
		for (ItemInvestment investment : itemInvestments) {
			amount += investment.getInvestmentAmount();
		}
		Map<Integer, Integer> levelMap = appContext.getLevelMap();
		String vip  = "";
		for (Map.Entry<Integer, Integer> entry : levelMap.entrySet()) {
			if (amount >= entry.getValue()) {
				vip = "V" + entry.getKey();
			}
		}
		return vip;
	}

//余额支付
	@RequestMapping(value="pay")
	@ResponseBody
	public Map<String, Object> pay(@RequestParam String resourceId) {
		Resource resource = this.getBaseService().getById(resourceId);
		Map<String, Object> data = new HashMap<String, Object>();
		if (resource.getCategory() == 0) { // 供
			data.put("message", "无须支付！");
			data.put("status", 0);
			return data;
		} else {
			MobileUser user = mobileUserService.getById(resource.getmUserId() + "");
			if ((user.getmUserMoney() - user.getFrozenMoney()) < resource.getPrice() * resource.getNumber()) {
				data.put("message", "用户余额不足！");
				data.put("status", 9);
				return data;
			}
		}
		
		try {
			resourceService.pay(resource);
		} catch (Exception e) {
			data.put("message", "付款失败！" + e.getMessage());
			data.put("status", 0);
			return data;
		}

		data.put("result", "");
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	@RequestMapping(value="cancel")
	@ResponseBody
	public Map<String, Object> cancel(@RequestParam int muid,@RequestParam int resourceId) {
		Resource resource =  resourceService1.getById(resourceId+"");
		Map<String, Object> data = new HashMap<String, Object>();
		if (resource.getmUserId() != muid) {
			data.put("message", "无权限！");
			data.put("status", 0);
			return data;
		}
		if (resource.getStatus() != 1) {
			data.put("message", "任务进行中不可取消！");
			data.put("status", 0);
			return data;
		}

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		params.put("status", 1);
		List<ResourceOrder> orders1 = resourceOrderService.findList(params);
		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		params.put("status", 2);
		List<ResourceOrder> orders2 = resourceOrderService.findList(params);
		int ordersnumber = orders1.size()+orders2.size();
		if(ordersnumber>0){
			data.put("message", "任务进行中不可取消！");
			data.put("status", 0);
			return data;
		}
		
		
		Resource entity = new Resource();
		entity.setResourceId(resourceId);
		entity.setStatus(4); // 取消
		try {
			this.getBaseService().update(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "取消成功！");
		data.put("status", 1);
		return data;
	}
	
	/**
    当前用户的任务消息详情
*/

@RequestMapping(value="noticeDetail")
@ResponseBody
public Map<String, Object> noticeDetail(@RequestParam int muid, @RequestParam String resourceId, @RequestParam double longitude, @RequestParam double latitude) {
	Map<String, Object> data = new HashMap<String, Object>();
	Map<String, Object> params=new HashMap<String, Object>();
	params.put("resourceId", resourceId);
	params.put("longitude", longitude);
	params.put("latitude", latitude);
	Resource resource = this.getBaseService().unique(params);
	try {
		if(resource.getmUserId()==muid){//说明是任务创建者得到提示消息
			 
			data = this.getManagementResource(muid, resourceId, longitude, latitude);
		}else{//说明是报名的人得到提示消息
			 
			data = this.getEnrollResource(muid, resourceId, longitude, latitude);
		}
		return data;
	} catch (Exception e) {
		data.put("message", "网络异常！");
		data.put("status", 0);
		return data;
	}
}
	
	
	
	/**
	 * 领取任务分支---从地图进入任务列表,或者从"我的"出发进入的"我参与的"任务      报名界面  准备购买或者报名
	 * @param muid
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="getEnrollResource")
	@ResponseBody
	public Map<String, Object> getEnrollResource(@RequestParam int muid, @RequestParam String resourceId, @RequestParam double longitude, @RequestParam double latitude) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		Resource resource = this.getBaseService().unique(params);
		
		//计数
		int nowcount = resource.getViewedcount();
		if(resource.getStatus()==1 ){// 报名阶段才可以算计数
			int newcount = resource.getViewedcount();
			newcount++;

			Resource r  = new Resource();
			r.setResourceId(resource.getResourceId());
			r.setViewedcount(newcount);
			this.getBaseService().update(r);
		}
		
		
		
		
		
		if(resource.getmUserId()==muid){//在地图上刚好点击了自己创建的任务
			data = this.getManagementResource(muid, resourceId, longitude, latitude);
			return data;
		} 
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resourceId", resource.getResourceId());
		map.put("name", resource.getName());
		map.put("price", resource.getPrice());
		map.put("publishTime", resource.getPublishTime());
		map.put("distance", resource.getDistance());
		map.put("address", resource.getAddress());
		map.put("nickname", resource.getNickname());
		map.put("resourceDesc", resource.getResourceDesc());
		map.put("category", resource.getCategory());
		map.put("mUserId", resource.getmUserId());
		map.put("viewedcount", nowcount);
		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		List<ResourceGallery> gallery = resourceGalleryService.findList(params);
		map.put("gallery", gallery);
		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		params.put("mUserId", muid);
		//params.put("status", 1);
		
		ResourceOrder  order = resourceOrderService.unique(params);
		//resource: 0:失效或者临时  1:有效 2:报名完成 3:任务完成  4:取消         orders:   0:报名 1:有效 2:完成 3:取消
		//	// 0:失效的;1: 有效的(新建立即有效,过期失效)可以继续接受报名的;2:联系他;3:未支付;4:任务结束查看收款;5:待评价;6:提醒用户已经选满  7:进入任务  8:提醒用户等待退款流程 9:进入报名管理进行选人 10:彻底结束,没有后续 11: 等待别人报名中 12:去付款
		 
		
		//报名数
		if(order==null){
			map.put("numberOfOrders", 0);//报名数为0
		}else{
			int enrolled = 0;
			params = new HashMap<String,Object>();
			params.put("resourceId", resourceId);
			List<ResourceOrder> orders = resourceOrderService.findList(params);
			for(int i = 0;i<orders.size();i++){
		  	  if(orders.get(i).getStatus()==1){//经过付款确认后,状态为2  付款的时候,原先确认为1的,状态改为0
		  		enrolled ++;
		  	  }
		    }
			map.put("numberOfOrders", enrolled);//报名数
		}
		
		
		
		if(order==null){/////////////当前用户没有报名,可以报名
			if(resource.getStatus()!=1){ // 旁观者在查看一个已经报名管理结束的任务  或者已经失效的任务 或者取消的任务(  只是为了万一)
				map.put("staus", 6);
			}else{
				if(resource.getCategory()==1){//需要  只报名
					map.put("staus", 1);
				}else if(resource.getCategory()==0){//供  需要进入付款报名
					map.put("staus", 3);
			    }
				
			}

		}else{///////////////当前用户已经报名或者以被选中或者已经被排除
			if(resource.getStatus()==2){//报名完成  报名管理结束  订单状态只能是0 或者1 或者2 
				
				if      (order.getStatus() ==1){         //被选中 或者报名已经有效
					if(resource.getCategory()==1){       //需
						map.put("staus", 2);             // 报名有效 被选中  联系他 准备去给他干活
					}else if(resource.getCategory()==0){ //供
						map.put("staus", 7);             // 我购买了服务,订单被选中,准备线下事情做完后给他打款 
						
					}
				}else if(order.getStatus() ==0){          //不需要付款的报名被任务发布者排除在选择范围外     或者刚付款报名等待被选
					if(resource.getCategory()==1){        //需
						map.put("staus", 6);              //报名管理结束当前用户只是报名没被选中    醒用户已经选满  (无需退款 )
					}else if(resource.getCategory()==0){  //供
						map.put("staus", 8);              // 报名管理结束 我购买了服务没被选中  (准备退款)
					}
					 
				}else if(order.getStatus() ==2){          //已付款  任务彻底结束 查看是否对任务提供人评价
					
					if(resource.getCategory()==1){        //需   别人给我打款
						map.put("staus", 4);              //已经完成的状态,点击任务完成,显示金额为获得的金额
					}else if(resource.getCategory()==0){  //供  准备评价
						 
						Map<String,Object> para = new HashMap<String,Object>();
						para.put("resourceId", resourceId);
						para.put("mUserId", muid);//当前用户
						para.put("toUserId", resource.getmUserId());//任务提供人
						Evaluate evaluate = evaluateService.unique(para);
						
		    			if(evaluate !=null){                    //用户已经评价
		    				map.put("staus", 10);         //已经评价完,什么都结束了,并且不需要评价,彻底结束 
		    			}else{
		    				map.put("staus", 5);          //去评价的状态
		    			}
					}
				}
			}else if(resource.getStatus()==1){            // 任务发布者报名管理之前,当前用户报名成功   order只可能是1 ,如果付款和报名合并的话(报名付款的都是1,只报名的也是1)
				if      (order.getStatus() ==1){          // 供,并且 付款报名的  
					map.put("staus", 2);                  // 报名有效 联系他 让他来干活 或者我联系他,去哪儿干活
				}else if(order.getStatus() ==0){         // 能继续报名的任务,订单竟然是0,  付款失败
					map.put("staus", 12);
					map.put("orderId", order.getOrderId());  //去支付  需要订单id
					
				} 
			}else if(resource.getStatus()==0){            ///////任务失效  失效的任务别人看不见吧
 
			}else if(resource.getStatus()==3){            //////任务完成   任务是2   订单必须都是2  可能没有评价的
				
				if(resource.getCategory()==1){            //需   别人给我打款
					map.put("staus", 4);                  //已经完成的状态,点击任务完成,显示金额为获得的金额
				}else if(resource.getCategory()==0){      //供  准备评价
					Map<String,Object> para = new HashMap<String,Object>();
					para.put("resourceId", resourceId);
					para.put("mUserId", muid);
					para.put("toUserId", resource.getmUserId());//任务提供人
					Evaluate evaluate = evaluateService.unique(para);
	    			if(evaluate !=null){//用户已经评价
	    				map.put("staus", 10);//已经评价完,什么都结束了,并且不需要评价,彻底结束 
	    			}else{
	    				map.put("staus", 5);//去评价的状态
	    			}
				}
				
				
			}else if(resource.getStatus()==4){//取消的订单   需要去退款页面  未做
				
			}
			
		}
		

		ResourceType type = resourceTypeService.getById(resource.getTypeId() + "");
		map.put("defaultImage", type.getDefaultImage());
		
		
		data.put("result", map);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
	/**
	 * 发布任务分支---在地图上点击我发布的任务,或者从"我的" 点击自己发布的进行中的任务   如果已经满则跟他们联系(供),或者进入任务付款(需),没满则进入多选界面(双向)
	 * @param muid 当前用户id
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="getManagementResource")
	@ResponseBody
	public Map<String, Object> getManagementResource(@RequestParam int muid, @RequestParam String resourceId, @RequestParam double longitude, @RequestParam double latitude) {
		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("resourceId", resourceId);
//		params.put("longitude", longitude);
//		params.put("latitude", latitude);
		Resource resource = this.getBaseService().getById(resourceId);
		int nowcount = resource.getViewedcount();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resourceId", resource.getResourceId());
		map.put("name", resource.getName());
		map.put("price", resource.getPrice());
		map.put("publishTime", resource.getPublishTime());
		map.put("distance", resource.getDistance());
		map.put("address", resource.getAddress());
		map.put("nickname", resource.getNickname());
		map.put("resourceDesc", resource.getResourceDesc());
		map.put("category", resource.getCategory());
		map.put("mUserId", resource.getmUserId());
		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		List<ResourceGallery> gallery = resourceGalleryService.findList(params);
		map.put("gallery", gallery);
		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		 
		//增加浏览计数器
		
		if(resource.getStatus()==1 ){// 报名阶段才可以算计数
			int newcount = resource.getViewedcount();
			newcount++;

			Resource r  = new Resource();
			r.setResourceId(resource.getResourceId());
			r.setViewedcount(newcount);
			this.getBaseService().update(r);
		}
		map.put("viewedcount", nowcount);
 
		List<ResourceOrder> orders = resourceOrderService.findList(params);
		// 0:失效的;1: 有效的(新建立即有效,过期失效)可以继续接受报名的;2:联系他;3:未支付;4:已支付已完成;5:待评价;6:提醒用户已经选满  7:进入任务  8:提醒用户等待退款流程 9:进入报名管理进行选人  10:任务彻底完成 11: 等待别人报名中 12:去付款
		//resource: 0:失效或者临时  1:有效 2:报名完成 3:任务完成  4:取消         orders:   0:报名 1:有效 2:完成 3:取消
		int enrolled = 0;
    	String payedusers="";//当前报名人ids
	    for(int i = 0;i<orders.size();i++){
	    	  if(orders.get(i).getStatus()==1){//经过发布任务的人确认后,状态为1  ,先来先得的任务,报名自动就是1
	    		  payedusers+=orders.get(i).getmUserId()+",";
	    		  enrolled ++;
	    	}
	    }
		if(payedusers.length()>0){
			payedusers=payedusers.substring(0,payedusers.length()-1);
		}
		map.put("numberOfOrders", enrolled);//报名数
		

		 if(resource.getCategory()==0){         // 我发出的供
			 if(resource.getStatus()==0){       //失效
				 map.put("staus", 0);           //  任务失效或者过期
				 
			 }else  if(resource.getStatus()==1){//有效  当前的任务在等待别人持续报名状态
				 if(resource.getMethod()==0){//先来先得的任务,购买服务的人数还不够任务要求人数
	    				map.put("staus", 11);// 等待他们报名中
	    			}else{
	    				if(enrolled==0){//没人报名,或者没有有效报名(报名没 )
	    					map.put("staus", 11);// 一个人都没有 等待他们报名中
	    				}else{
	    					map.put("staus", 9);//双向选择的任务进入选择流程    进入报名管理    可能人数不够不能报名管理
	    				}
	    				 
	    			}
				 map.put("enrolledusers",payedusers);//返回前台当前已报名的人的id
				 
			 }else if(resource.getStatus()==2){//报名管理完成
				 
				 if(resource.getMethod()==0){//先来先得的任务,报名人数自动被选中,报名人数已达需要的人数
	    				map.put("staus", 2);// 联系他们,沟通办事儿细节
	    			}else{
	    				 map.put("staus", 2);//双向选择的任务报名管理结束,联系他们沟通办事儿细节
	    			}
				 map.put("enrolledusers",payedusers);//返回前台当前已报名的人的id
			 }else if(resource.getStatus()==3){//任务完成   别人都已经确认给我付款了
				 map.put("staus", 4);// 事儿办了 钱收了 任务结束 查看别人对我的评价列表   #这是这条线的结束#
				 
			 }else if(resource.getStatus()==4){//取消  未做
				 
			 }
			 
		 }else if(resource.getCategory()==1){//我发出的需
			 if(resource.getStatus()==0){//失效
				 map.put("staus", 0);//
				 
			 }else  if(resource.getStatus()==1){//有效
				 if(resource.getMethod()==0){//先来先得的任务,购买服务的人数还不够任务要求人数
	    				map.put("staus", 11);// 等待他们报名中
	    		 }else{
	    				 map.put("staus", 9);//双向选择的任务进入选择流程    进入报名管理    可能人数不够不能报名管理
	    		 }
				 map.put("enrolledusers",payedusers);//返回前台当前已报名的人的id
			 }else if(resource.getStatus()==2){//报名管理完成
				 if(resource.getMethod()==0){//先来先得的任务,报名人数自动被选中,报名人数已达需要的人数
	    				map.put("staus", 7);// 联系他们,沟通办事儿细节
	    			}else{
	    				 map.put("staus", 7);//双向选择的任务报名管理结束,联系他们沟通办事儿细节
	    			}
				 map.put("enrolledusers",payedusers);//返回前台当前已报名的人的id
			 }else if(resource.getStatus()==3){//我给他们打款后,任务完成    需要对别人(多人)评价    未完待续: 对一个人已经评价了,他就不应该出现在列表了
				 Map<String,Object> para = new HashMap<String,Object>();
					para.put("resourceId", resourceId);
					para.put("mUserId", muid);
					 
					List<Evaluate> el = evaluateService.findList(para);//我对所有这个任务的参与者发出的评价
					if(  orders.size()>el.size()){//订单数量大于评价数量,则说明还没有评价完
						map.put("staus", 5);//去评价的状态
					}else{
						map.put("staus", 10);//彻底结束  #这是这条线的彻底结束#
					}
					
	    				
			 }else if(resource.getStatus()==4){//取消  没做
				 
			 }
			 
		 }
		
 
		ResourceType type = resourceTypeService.getById(resource.getTypeId() + "");
		map.put("defaultImage", type.getDefaultImage());
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", map);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
	/** 3.0接口
	 * 我发布的任务或者技能---在地图上点击我发布的任务,或者从"我的" 点击自己发布的进行中的任务   如果已经满则跟他们联系(供),或者进入任务付款(需),没满则进入多选界面(双向)
	 * @param muid 当前用户id
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="getMyResource")
	@ResponseBody
	public Map<String, Object> getMyResource(@RequestParam int muid, @RequestParam String resourceId, @RequestParam double longitude, @RequestParam double latitude) {
		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("resourceId", resourceId);
//		params.put("longitude", longitude);
//		params.put("latitude", latitude);
		Resource resource = this.getBaseService().getById(resourceId);
		int nowcount = resource.getViewedcount();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resourceId", resource.getResourceId());
		map.put("name", resource.getName());
		map.put("price", resource.getPrice());
		map.put("publishTime", resource.getPublishTime());
		map.put("distance", resource.getDistance());
		map.put("address", resource.getAddress());
		map.put("nickname", resource.getNickname());
		map.put("resourceDesc", resource.getResourceDesc());
		map.put("category", resource.getCategory());
		map.put("mUserId", resource.getmUserId());
		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		List<ResourceGallery> gallery = resourceGalleryService.findList(params);
		map.put("gallery", gallery);
		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		 
		//增加浏览计数器
		
		if(resource.getStatus()==1 ){// 报名阶段才可以算计数
			int newcount = resource.getViewedcount();
			newcount++;

			Resource r  = new Resource();
			r.setResourceId(resource.getResourceId());
			r.setViewedcount(newcount);
			this.getBaseService().update(r);
		}
		map.put("viewedcount", nowcount);
 
		List<ResourceOrder> orders = resourceOrderService.findList(params);
		// 0:失效的;1: 有效的(新建立即有效,过期失效)可以继续接受报名的; 3:未支付;4:已支付已完成;5:待评价;6:提醒用户已经选满  7:进入任务  8:提醒用户等待退款流程 9:进入报名管理进行选人  10:任务彻底完成 11: 等待别人报名中 12:去付款  13：结束报名  14：等待别人给我付款
		//resource: 0:失效或者临时  1:有效 2:报名完成 3:任务完成  4:取消         orders:   0:报名 未付款 1:有效报名 2:选择完成 3:完成 4：取消
		int enrolled = 0;
		int comfirmed = 0;
    	String payedusers="";//当前报名人ids
	    for(int i = 0;i<orders.size();i++){
	    	  if(orders.get(i).getStatus()==2){//经过发布任务的人确认后,状态为2 
	    		  payedusers+=orders.get(i).getmUserId()+",";
	    		  comfirmed ++;
	    	}else if(orders.get(i).getStatus()==1){//报名但是mei'you的人
	    		  
	    		  enrolled ++;
	    	}
	    }
		if(payedusers.length()>0){
			payedusers=payedusers.substring(0,payedusers.length()-1);
		}
		map.put("numberOfOrders", comfirmed);//报名数
		

		 if(resource.getCategory()==0){         // 我发出的技能
			 if(resource.getStatus()==0){       //失效
				 map.put("staus", 0);           //  任务失效或者过期
				 
			 }else  if(resource.getStatus()==1){//有效  当前的任务在等待别人持续报名状态
				 
	    				if(enrolled==0 && comfirmed ==0){//没人报名,也没有确认报名的人
	    					map.put("staus", 11);// 一个人都没有 等待他们报名中
	    				}else if(enrolled > 0 && comfirmed ==0){// 有人报名但是没选过人
	    					map.put("staus", 9);// 进入报名管理   
	    				}else if(comfirmed >=0 ){//有确认的订单，进入完成功能
	    					map.put("staus", 13);// 用现有的人进行任务，终止别人报名（修改一下任务状态，并把款项退给报名的人）
	    				}
				 map.put("enrolledusers",payedusers);//返回前台当前已报名确认的人的id
			 }else if(resource.getStatus()==2){//报名管理完成
	    				 map.put("staus", 14);// 等待别人确认给我付款
				 map.put("enrolledusers",payedusers);//返回前台当前已报名的人的id
			 }else if(resource.getStatus()==3){//任务完成   别人都已经确认给我付款了
				 map.put("staus", 4);// 事儿办了 钱收了 任务结束 查看别人对我的评价列表   #这是这条线的结束#
			 }else if(resource.getStatus()==4){//取消  未做
			 }
			 
		 }else if(resource.getCategory()==1){//我发出的任务
			 if(resource.getStatus()==0){//失效
				 map.put("staus", 0);//
				 
			 }else  if(resource.getStatus()==1){//有效
	 				if(enrolled==0 && comfirmed ==0){//没人报名,也没有确认报名的人
						map.put("staus", 11);// 一个人都没有 等待他们报名中，点击没反应
					}else if(enrolled > 0 && comfirmed ==0){// 有人报名但是没选过人
						map.put("staus", 9);// 进入报名管理   ，同点击报名人列表
					}else if(comfirmed >=0 ){//有确认的订单，可终止报名
						map.put("staus", 13);// 用现有的人进行任务，终止别人报名（仅仅修改一下任务状态从1 到2）
					}
				 map.put("enrolledusers",payedusers);//返回前台当前已报名的人的id
			 }else if(resource.getStatus()==2){//报名管理完成
				  
	    		 map.put("staus", 7);//进入任务，准备确认付款
	    		 
				 map.put("enrolledusers",payedusers);//返回前台当前已报名的人的id
			 }else if(resource.getStatus()==3){//我给他们打款后,任务完成    需要对别人(多人)评价    未完待续: 对一个人已经评价了,他就不应该出现在列表了
				 Map<String,Object> para = new HashMap<String,Object>();
					para.put("resourceId", resourceId);
					para.put("mUserId", muid);
					List<Evaluate> el = evaluateService.findList(para);//我对所有这个任务的参与者发出的评价
					if(  orders.size()>el.size()){//订单数量大于评价数量,则说明还没有评价完
						map.put("staus", 5);//去评价的状态
					}else{
						map.put("staus", 10);//彻底结束  #这是这条线的彻底结束#
					}
			 }else if(resource.getStatus()==4){//取消  没做
				 
			 }
			 
		 }
		
 
		ResourceType type = resourceTypeService.getById(resource.getTypeId() + "");
		map.put("defaultImage", type.getDefaultImage());
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", map);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
	/** 3.0 版本
	 * 我参与的任务--从地图进入任务列表,或者从"我的"出发进入的"我参与的"任务      报名界面  准备购买或者报名
	 * @param muid
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="getmyEnrollResource")
	@ResponseBody
	public Map<String, Object> getmyEnrollResource(@RequestParam int muid, @RequestParam String resourceId, @RequestParam double longitude, @RequestParam double latitude) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		Resource resource = this.getBaseService().unique(params);
		
		//计数
		int nowcount = resource.getViewedcount();
		if(resource.getStatus()==1 ){// 报名阶段才可以算计数
			int newcount = resource.getViewedcount();
			newcount++;

			Resource r  = new Resource();
			r.setResourceId(resource.getResourceId());
			r.setViewedcount(newcount);
			this.getBaseService().update(r);
		}
 
		if(resource.getmUserId()==muid){//在地图上刚好点击了自己创建的任务
			data = this.getMyResource(muid, resourceId, longitude, latitude);
			return data;
		} 
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resourceId", resource.getResourceId());
		map.put("name", resource.getName());
		map.put("price", resource.getPrice());
		map.put("publishTime", resource.getPublishTime());
		map.put("distance", resource.getDistance());
		map.put("address", resource.getAddress());
		map.put("nickname", resource.getNickname());
		map.put("resourceDesc", resource.getResourceDesc());
		map.put("category", resource.getCategory());
		map.put("mUserId", resource.getmUserId());
		map.put("viewedcount", nowcount);
		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		List<ResourceGallery> gallery = resourceGalleryService.findList(params);
		map.put("gallery", gallery);
		params = new HashMap<String,Object>();
		params.put("resourceId", resourceId);
		params.put("mUserId", muid);
		//params.put("status", 1);
		
		ResourceOrder  order = resourceOrderService.unique(params);
		//resource: 0:失效或者临时  1:有效 2:报名完成 3:任务完成  4:取消         orders:   0:报名 1:有效 2:完成 3:取消
		//	// 0:失效的;1: 有效的(新建立即有效,过期失效)可以继续接受报名的;2:联系他;3:未支付;4:任务结束查看收款;5:待评价;
		  //       6:提醒用户已经选满  7:进入任务  8:提醒用户等待退款流程 9:进入报名管理进行选人 10:彻底结束,没有后续 11: 等待别人报名中 12:去付款
		  //       13：结束报名  14：等待别人给我付款
		 
		
		//报名数
		if(order==null){
			map.put("numberOfOrders", 0);//报名数为0
		}else{
			int enrolled = 0;
			int comfirmed = 0;
			params = new HashMap<String,Object>();
			params.put("resourceId", resourceId);
			List<ResourceOrder> orders = resourceOrderService.findList(params);
			for(int i = 0;i<orders.size();i++){
		  	  if(orders.get(i).getStatus()==1){//经过付款确认后,状态为2   
		  		enrolled ++;
		  	  } else if(orders.get(i).getStatus()==2){//经过付款 为1的, 
		  		comfirmed ++;
		  	  }
		    }
			map.put("numberOfOrders", comfirmed);//报名数
		}
		
		
		
		if(order==null){/////////////当前用户没有报名,可以报名
			if(resource.getStatus()!=1){ // 旁观者在查看一个已经报名管理结束的任务  或者已经失效的任务 或者取消的任务(  只是为了万一)
				map.put("staus", 6);//6:提醒用户已经选满
			}else{
				if(resource.getCategory()==1){// 我没有报名  需  只报名
					map.put("staus", 1);//报名
				}else if(resource.getCategory()==0){// 我没有报名 供  需要进入付款报名
					map.put("staus", 3);//支付报名
			    }
				
			}

		}else{///////////////当前用户已经报名或者以被选中或者已经被排除
			if(resource.getStatus()==2){//报名完成  报名管理结束  订单状态只能是0 或者1 或者2 
				
				if      (order.getStatus() ==1){         //被选中 或者报名已经有效
					if(resource.getCategory()==1){       //需
						map.put("staus", 2);             // 报名有效 被选中  联系他 准备去给他干活
					}else if(resource.getCategory()==0){ //供
						map.put("staus", 7);             // 我购买了服务,订单被选中,准备线下事情做完后给他打款 
						
					}
				}else if(order.getStatus() ==0){          //付款不成功的订单 由于报名截止，不能做任何事情
					 
						map.put("staus", 0);              //过期
					 
					 
				}else if(order.getStatus() ==2){          //已付款  经过了确认
					
					if(resource.getCategory()==1){        //需   别人给我打款
						map.put("staus", 14);              //等待别人给我打款
					}else if(resource.getCategory()==0){  //供  准备给别人打款
						 
						map.put("staus", 7);              //去付款
					}
				}
			}else if(resource.getStatus()==1){            // 任务发布者报名管理之前,当前用户报名成功   order只可能是1  或者2 或者0
				if      (order.getStatus() ==1){          // 供,并且 付款报名的  
					map.put("staus", 2);                  // 报名有效 联系他 让他来干活 或者我联系他,去哪儿干活
				}else if(order.getStatus() ==0){         // 能继续报名的任务,订单竟然是0,  付款失败
					map.put("staus", 12);//去付款
					map.put("orderId", order.getOrderId());  //去支付  需要订单id
					
				}else if(order.getStatus() ==2){          //已付款  经过了确认
					
					if(resource.getCategory()==1){        //需   别人给我打款
						map.put("staus", 14);              //等待别人给我打款
					}else if(resource.getCategory()==0){  //供  准备给别人打款
						 
						map.put("staus", 7);              //去付款
					}
				}
				
				
				
			}else if(resource.getStatus()==0){            ///////任务失效  失效的任务别人看不见吧
 
			}else if(resource.getStatus()==3){            //////任务完成   任务是3  订单必须都是3  或者0
				
				  if(order.getStatus() ==0){         // 完成的任务 状态为0  需要退款或者直接失效
					if(resource.getCategory()==1){            //需   别人给我打款
						map.put("staus", 0);                  //没选中  失效订单
					}else if(resource.getCategory()==0){ 
						
						map.put("staus", 8);                  //等待退款
					}
				}else if(order.getStatus() ==3){          //已付款  经过了确认
					if(resource.getCategory()==1){            //需   别人给我打款
						map.put("staus", 4);                  //已经完成的状态,点击任务完成,显示金额为获得的金额
					}else if(resource.getCategory()==0){      //供  准备评价
						Map<String,Object> para = new HashMap<String,Object>();
						para.put("resourceId", resourceId);
						para.put("mUserId", muid);
						para.put("toUserId", resource.getmUserId());//任务提供人
						Evaluate evaluate = evaluateService.unique(para);
		    			if(evaluate !=null){//用户已经评价
		    				map.put("staus", 10);//已经评价完,什么都结束了,并且不需要评价,彻底结束 
		    			}else{
		    				map.put("staus", 5);//去评价的状态
		    			}
					}
					
					 
				}
				
 
			}else if(resource.getStatus()==4){//取消的订单   需要去退款页面  未做
				
			}
			
		}
		

		ResourceType type = resourceTypeService.getById(resource.getTypeId() + "");
		map.put("defaultImage", type.getDefaultImage());
		
		
		data.put("result", map);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 获取当前任务下没有对他们评价的人的列表    供需都可能
	 * @param muid 当前用户id
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="getToBeCommentedList")
	@ResponseBody
	public Map<String, Object> getToBeCommentedList(@RequestParam int resourceId, @RequestParam int muid) {
		List<Map<String, Object>> toBeCommentedList = null;
		Map<String, Object> data = new HashMap<String, Object>();
		Resource resource = this.getBaseService().getById(resourceId+"");
	 
		if(resource.getCategory()==0){//供 拍卖技能  被评价的人是任务的拥有者 ,绝对单人
			int tomuid = resource.getmUserId();//被评价的人是任务拥有者
			Map<String,Object> para = new HashMap<String,Object>();
			para.put("resourceId", resourceId);
			para.put("mUserId", muid);
			para.put("toUserId", tomuid);
			Evaluate evaluate = evaluateService.unique(para);
			if(evaluate!=null){//服务购买人已经对服务提供人已经有评价了
				data.put("result", toBeCommentedList);
				data.put("message", "");
				data.put("status", 1);
				return data;
				
			}else{//返回服务提供人的信息
			 String sql = "select u.m_user_id,u.photo, u.sex,  u.level,  u.nickname, r.resource_id from   phb_mobile_user u left join phb_resource r on u.m_user_id = r.m_user_id where   r.resource_id = "+resourceId;
			 toBeCommentedList = this.jdbcTemplate.queryForList(sql);
			}
		}else if(resource.getCategory()==1){//需     被评价的人是任务执行者,多人  从订单中找这些人吧
			String sql="SELECT u.m_user_id,  u.photo,  u.sex,  u.level, u.nickname, o.resource_id FROM phb_resource_order o LEFT JOIN phb_mobile_user u ON u.m_user_id = o.m_user_id WHERE o.resource_id = "+resourceId+"  AND o.status=2 AND o.m_user_id NOT in (SELECT e.to_user_id  FROM phb_evaluate e  WHERE e.resource_id = "+resourceId+")";
			  toBeCommentedList = this.jdbcTemplate.queryForList(sql);
		}
 
		data.put("result", toBeCommentedList);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	
	/**
	 * 提前结束报名
	 *  
	 */
	@RequestMapping(value="endenroll")
	@ResponseBody
	public Map<String, Object> endenroll(@RequestParam int resourceId  ) {
		Map<String, Object> data = new HashMap<String, Object>();
		Resource entity = this.getBaseService().getById(resourceId + "");
		if (entity == null) {
			data.put("message", "该供需资源不存在！");
			data.put("status", 0);
			return data;
		}
		if (entity.getStatus() != 1) {
			data.put("message", "异常！");
			data.put("status", 0);
			return data;
		}

		Resource resource = new Resource();
		resource.setResourceId(resourceId);
		resource.setStatus(2);//报名截止的状态
		 

		try {
			this.getBaseService().update(resource);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
 
	/**
	 * 获取当前任务下选择后联系他们     我发布的任务,别人报名,选择后
	 * @param muid 当前用户id
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value="getToBeContactListAfterChoosing")
	@ResponseBody
	public Map<String, Object> getToBeContactListAfterChoosing(@RequestParam int resourceId) {
		String sql="SELECT u.m_user_id,  u.photo,   u.sex,  u.level,   u.nickname FROM phb_mobile_user u LEFT JOIN phb_resource_order o ON u.m_user_id = o.m_user_id  WHERE o.resource_id = "+resourceId+"  AND o.status=1";
		List<Map<String, Object>> toBeCommentedList = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", toBeCommentedList);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
}
