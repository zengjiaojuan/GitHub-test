package push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public abstract class UmengNotification {
	// This JSONObject is used for constructing the whole request string.
	protected final JSONObject rootJson = new JSONObject();
	
	// This object is used for sending the post request to Umeng
	protected HttpClient client = new DefaultHttpClient();
	
	// The host
	protected static final String host = "http://msg.umeng.com";
	
	// The upload path
	protected static final String uploadPath = "/upload";
	
	// The post path
	protected static final String postPath = "/api/send";
	
	// The app master secret
	protected String appMasterSecret;
	
	// The user agent
	protected final String USER_AGENT = "Mozilla/5.0";
	
	// Keys can be set in the root level
	protected static final HashSet<String> ROOT_KEYS = new HashSet<String>(Arrays.asList(new String[]{
			"appkey", "timestamp", "type", "device_tokens", "alias", "alias_type", "file_id", 
			"filter", "production_mode", "feedback", "description", "thirdparty_id"}));
	
	// Keys can be set in the policy level
	protected static final HashSet<String> POLICY_KEYS = new HashSet<String>(Arrays.asList(new String[]{
			"start_time", "expire_time", "max_send_num"
	}));
	
	// Set predefined keys in the rootJson, for extra keys(Android) or customized keys(IOS) please 
	// refer to corresponding methods in the subclass.
	public abstract boolean setPredefinedKeyValue(String key, Object value) throws Exception;
	public void setAppMasterSecret(String secret) {
		appMasterSecret = secret;
	}
	public boolean send() {
        String url = host + postPath;
        String postBody = rootJson.toString();
        String sign = null;
		try {
			sign = DigestUtils.md5Hex(("POST" + url + postBody + appMasterSecret).getBytes("utf8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        url = url + "?sign=" + sign;
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        StringEntity se = null;
		try {
			se = new StringEntity(postBody, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        post.setEntity(se);
        // Send the post request and get the response
        HttpResponse response;
		try {
			response = client.execute(post);
	        int status = response.getStatusLine().getStatusCode();
//	        System.out.println("Response Code : " + status);
//	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//	        StringBuffer result = new StringBuffer();
//	        String line = "";
//	        while ((line = rd.readLine()) != null) {
//	            result.append(line);
//	        }
//	        System.out.println(result.toString());
	        if (status == 200) {
	            return true;
//	            System.out.println("Notification sent successfully.");
	        } else {
	            return false;
//	            System.out.println("Failed to send the notification!");
	        }
		} catch (Exception e) {
			e.printStackTrace();
            return false;
		}
    }
	
	
}
