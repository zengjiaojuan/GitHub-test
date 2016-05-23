package com.phb.puhuibao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import push.android.AndroidBroadcast;
import push.ios.IOSBroadcast;

import com.idp.pub.constants.Constants;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.Notification;

@Controller
@RequestMapping(value = "/notification")
public class NotificationController extends BaseController<Notification, String> {
	//private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Log log = LogFactory.getLog(NotificationController.class);
	
	@Resource(name = "notificationService")
	public void setBaseService(IBaseService<Notification, String> baseService) {
		super.setBaseService(baseService);
	}
	
	@RequestMapping(value="sendAndroidBroadcast")
	@ResponseBody
	public Map<String, Object> sendAndroidBroadcast() {
//		JSONObject content = new JSONObject();
//		try {
//			content.put("everyPicId", "81eee43965c44a469dca942aa2ac5a01");
//			content.put("everyTitle", "喜讯");
//			content.put("everyContent", "新版本上线！");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		String appkey = "55c1ca6be0f55a397c002a3c";
		String appMasterSecret = "i2eslpxldno1mnglytewk46hhvpkym6w";
		int timestamp = (int) (System.currentTimeMillis() / 1000);
		AndroidBroadcast broadcast = new AndroidBroadcast();
		broadcast.setAppMasterSecret(appMasterSecret);
		try {
			broadcast.setPredefinedKeyValue("appkey", appkey);
			broadcast.setPredefinedKeyValue("timestamp", timestamp);
			broadcast.setPredefinedKeyValue("ticker", "Android broadcast ticker");
			broadcast.setPredefinedKeyValue("title", "title");
			broadcast.setPredefinedKeyValue("text", "content");
			broadcast.setPredefinedKeyValue("after_open", "go_activity");
			broadcast.setPredefinedKeyValue("activity", "com.huanlianruitong.huanlian.message.MessageActivity");
			broadcast.setPredefinedKeyValue("display_type", "notification");
			broadcast.setPredefinedKeyValue("production_mode", "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean b = broadcast.send();

		Map<String, Object> data = new HashMap<String, Object>();
		if (b) {
			data.put("message", "success!");
			data.put("status", 1);
		} else {
			data.put("message", "failed!");
			data.put("status", 0);
		}
		return data;
	}

	@RequestMapping(value="sendIOSBroadcast")
	@ResponseBody
	public Map<String, Object> sendIOSBroadcast() {
		String appkey = "55c07c87e0f55a9e25000364";
		String appMasterSecret = "pqqu5smr4g096sbioawemd7czht6mmvt";
		int timestamp = (int) (System.currentTimeMillis() / 1000);
		IOSBroadcast broadcast = new IOSBroadcast();
		broadcast.setAppMasterSecret(appMasterSecret);
		try {
			broadcast.setPredefinedKeyValue("appkey", appkey);
			broadcast.setPredefinedKeyValue("timestamp", timestamp);
			broadcast.setPredefinedKeyValue("alert", "title");
			broadcast.setPredefinedKeyValue("badge", 0);
			broadcast.setPredefinedKeyValue("sound", "chime");
			broadcast.setPredefinedKeyValue("production_mode", "true");
			broadcast.setCustomizedField("type", "2");
			broadcast.setCustomizedField("text", "content");
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean b = broadcast.send();
		Map<String, Object> data = new HashMap<String, Object>();
		if (b) {
			data.put("message", "success!");
			data.put("status", 1);
		} else {
			data.put("message", "failed!");
			data.put("status", 0);
		}
		return data;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(Notification entity) {
		Map<String, Object> results = Constants.MAP();
		try {
			this.getBaseService().update(entity);
			if (entity.getStatus() == 1) {
				send(entity.getTitle(), entity.getContent());
			}
			results.put(Constants.SUCCESS, Constants.TRUE);
		} catch (Exception e) {
			results.put(Constants.SUCCESS, Constants.FALSE);
			results.put(Constants.MESSAGE, e.getMessage());
		}
		return results;
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	protected Map<String, Object> create(Notification entity) {
		Map<String, Object> results = Constants.MAP();
		try {
			this.getBaseService().save(entity);
			if (entity.getStatus() == 1) {
				send(entity.getTitle(), entity.getContent());
			}
			results.put(Constants.SUCCESS, Constants.TRUE);
		} catch (Exception e) {
			results.put(Constants.SUCCESS, Constants.FALSE);
			results.put(Constants.MESSAGE, e.getMessage());
		}
		return results;
	}

	private void send(String title, String content) {
		String appkey = "55c1ca6be0f55a397c002a3c";
		String appMasterSecret = "i2eslpxldno1mnglytewk46hhvpkym6w";
		int timestamp = (int) (System.currentTimeMillis() / 1000);
		AndroidBroadcast broadcast = new AndroidBroadcast();
		broadcast.setAppMasterSecret(appMasterSecret);
		try {
			broadcast.setPredefinedKeyValue("appkey", appkey);
			broadcast.setPredefinedKeyValue("timestamp", timestamp);
			broadcast.setPredefinedKeyValue("ticker", "Android broadcast ticker");
			broadcast.setPredefinedKeyValue("title", title);
			broadcast.setPredefinedKeyValue("text", content);
			broadcast.setPredefinedKeyValue("after_open", "go_app");
//			broadcast.setPredefinedKeyValue("after_open", "go_activity");
//			broadcast.setPredefinedKeyValue("activity", "com.huanlianruitong.huanlian.message.MessageActivity");
			broadcast.setPredefinedKeyValue("display_type", "notification");
			broadcast.setPredefinedKeyValue("production_mode", "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean b = broadcast.send();
		if (!b) {
			log.error("AndroidBroadcast failed: " + title + content);
		}

		appkey = "55c07c87e0f55a9e25000364";
		appMasterSecret = "pqqu5smr4g096sbioawemd7czht6mmvt";
		timestamp = (int) (System.currentTimeMillis() / 1000);
		IOSBroadcast iosBroadcast = new IOSBroadcast();
		iosBroadcast.setAppMasterSecret(appMasterSecret);
		try {
			iosBroadcast.setPredefinedKeyValue("appkey", appkey);
			iosBroadcast.setPredefinedKeyValue("timestamp", timestamp);
			iosBroadcast.setPredefinedKeyValue("alert", title);
			iosBroadcast.setPredefinedKeyValue("badge", 0);
			iosBroadcast.setPredefinedKeyValue("sound", "chime");
			iosBroadcast.setPredefinedKeyValue("production_mode", "true");
			iosBroadcast.setCustomizedField("type", "2");
			iosBroadcast.setCustomizedField("content", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		b = iosBroadcast.send();
		if (!b) {
			log.error("IOSBroadcast failed: " + title + content);
		}
	}
}
