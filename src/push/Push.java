package push;

import org.json.JSONException;
import org.json.JSONObject;

import push.android.AndroidBroadcast;
import push.ios.IOSBroadcast;
import push.ios.IOSUnicast;

public class Push {
	private final static int timestamp = (int) (System.currentTimeMillis() / 1000);

	public static void main(String[] args) {
		sendIOSBroadcast();
	}
	public static void androidCast() {
		String appkey = "55334e8267e58e2836000903";
		String appMasterSecret = "ncowciualbnlrsh90z3mvjzmyhd9bhgr";
		JSONObject content = new JSONObject();
		try {
			content.put("everyPicId", "81eee43965c44a469dca942aa2ac5a01");
			content.put("everyTitle", "喜讯");
			content.put("everyContent", "新版本上线！");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String title = "广播消息";
		AndroidBroadcast broadcast = new AndroidBroadcast();
		broadcast.setAppMasterSecret(appMasterSecret);
		try {
			broadcast.setPredefinedKeyValue("appkey", appkey);
			broadcast.setPredefinedKeyValue("timestamp", timestamp);
			broadcast.setPredefinedKeyValue("ticker", "Android broadcast ticker");
			broadcast.setPredefinedKeyValue("device_tokens", "AqHEuFGgN_kuK0xEtO-6cMgYQ-ixgFhEMlohTVgfmg3W");
			broadcast.setPredefinedKeyValue("title", title);
			broadcast.setPredefinedKeyValue("text", content.toString());
			broadcast.setPredefinedKeyValue("after_open", "go_activity");
			broadcast.setPredefinedKeyValue("activity", "com.huanlianruitong.huanlian.message.MessageActivity");
			broadcast.setPredefinedKeyValue("display_type", "notification");
			broadcast.setPredefinedKeyValue("production_mode", "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		broadcast.send();
	}
	
	public static void sendIOSUnicast() {
		String appkey = "5534613c67e58e9a8400123d";
		String appMasterSecret = "oa71kkmh87nke4cwzixtt9dihct1zfhr";
		IOSUnicast unicast = new IOSUnicast();
		unicast.setAppMasterSecret(appMasterSecret);
		try {
			unicast.setPredefinedKeyValue("appkey", appkey);
			unicast.setPredefinedKeyValue("timestamp", timestamp);
			unicast.setPredefinedKeyValue("device_tokens", "ffa22c1faf3b80ae0809c49d828c5ff7b20d80f4f64cc6e1ab1d7227cb88be01");
			unicast.setPredefinedKeyValue("alert", "广播消息");
			unicast.setPredefinedKeyValue("badge", 0);
			unicast.setPredefinedKeyValue("sound", "chime");
			unicast.setPredefinedKeyValue("production_mode", "true");
			unicast.setCustomizedField("type", "2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		unicast.send();
	}

	public static void sendIOSBroadcast() {
		String appkey = "5534613c67e58e9a8400123d";
		String appMasterSecret = "oa71kkmh87nke4cwzixtt9dihct1zfhr";
		IOSBroadcast broadcast = new IOSBroadcast();
		broadcast.setAppMasterSecret(appMasterSecret);
		try {
			broadcast.setPredefinedKeyValue("appkey", appkey);
			broadcast.setPredefinedKeyValue("timestamp", timestamp);
			broadcast.setPredefinedKeyValue("alert", "广播消息");
			broadcast.setPredefinedKeyValue("badge", 0);
			broadcast.setPredefinedKeyValue("sound", "chime");
			broadcast.setPredefinedKeyValue("production_mode", "true");
			broadcast.setCustomizedField("type", "2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		broadcast.send();
	}
}
