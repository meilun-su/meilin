package com.nnxy.aliyun;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
@Controller
@RequestMapping("/aliyun")
public class AliyunController {
	// 地域ID，当前仅支持华东2（上海）
    private static String regionId = "cn-shanghai";
    // 定义加密方式。MAC算法可选以下多种算法 HmacMD5 HmacSHA1，需和signmethod一致	
    private static final String HMAC_ALGORITHM = "hmacsha1";
    // token 7天有效，失效后需要重新获取
    private String token = null;

    //连接到阿里云物联网平台公钥
    String productKey = "a1deiQK0vng"; 
    //连接到阿里云物联网平台设备名称，不固定，不同的设备该值不一样，需要修改对应的设备值
    String deviceName = "softwaresu";
    //连接到阿里云物联网平台设备唯一值，不固定，不同的设备该值不一样，需要修改对应的唯一值
    String deviceSecret = "3d0c17ca43552ddaf49726540f4ff9d0";
    /**
     * 初始化HTTP客户端
     * 
     * @param productKey 产品key
     * @param deviceName 设备名称
     * @param deviceSecret 设备唯一值
     */
    public void conenct(String productKey, String deviceName, String deviceSecret) {
        try {
            // 注册地址
            URL url = new URL("https://iot-as-http." + regionId + ".aliyuncs.com/auth");

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(authBody(productKey, deviceName, deviceSecret));
            // flush输出流的缓冲
            out.flush();
            // 获取URLConnection对象对应的输入流
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // 读取URL的响应
            String result = "";
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println("----- auth result -----");
            System.out.println(result);

            // 关闭输入输出流
            in.close();
            out.close();
            conn.disconnect();

            // 获取token
            JSONObject json = JSONObject.parseObject(result);
            if (json.getIntValue("code") == 0) {
                token = json.getJSONObject("info").getString("token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 发送消息
     * 
     * @param topic 发送消息的Topic
     * @param payload 消息内容
     */
    public String publish(String topic, byte[] payload) {
    	String result = "";
        try {
            // 注册地址
        	URL url = new URL("https://iot-as-http." + regionId + ".aliyuncs.com/topic" + topic);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/octet-stream");
            conn.setRequestProperty("password", token);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(payload);
            out.flush();
            // 获取URLConnection对象对应的输入流
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // 读取URL的响应
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println("----- publish result -----");
            System.out.println(result);

            // 关闭输入输出流
            in.close();
            out.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成认证请求内容
     * 
     * @param params 认证参数
     * @return 认证请求消息体
     */
    private String authBody(String productKey, String deviceName, String deviceSecret) {

        // 构建认证请求
        JSONObject body = new JSONObject();
        body.put("productKey", productKey);
        body.put("deviceName", deviceName);
        body.put("clientId", productKey + "." + deviceName);
        body.put("timestamp", String.valueOf(System.currentTimeMillis()));
        body.put("signmethod", HMAC_ALGORITHM);
        body.put("version", "default");
        body.put("sign", sign(body, deviceSecret));

        System.out.println("----- auth body -----");
        System.out.println(body.toJSONString());

        return body.toJSONString();
    }

    /**
     * 设备端签名
     * 
     * @param params 签名参数
     * @param deviceSecret 设备密钥
     * @return 签名十六进制字符串
     */
    private String sign(JSONObject params, String deviceSecret) {
        // 请求参数按字典顺序排序
        Set<String> keys = getSortedKeys(params);
        // sign，signmethod 和 version除外
        keys.remove("sign");
        keys.remove("signmethod");	
        keys.remove("version");
        // 组装签名明文
        StringBuffer content = new StringBuffer();
        for (String key : keys) {
            content.append(key);
            content.append(params.getString(key));
        }
        // 计算签名
        String sign = encrypt(content.toString(), deviceSecret);
        System.out.println("sign content=" + content);
        System.out.println("sign result=" + sign);

        return sign;
    }

    /**
     * 获取JSON对象排序后的key集合
     * 
     * @param json 需要排序的JSON对象
     * @return 排序后的key集合
     */
    private Set<String> getSortedKeys(JSONObject json) {
        SortedMap<String, String> map = new TreeMap<String, String>();
        for (String key : json.keySet()) {
            String vlaue = json.getString(key);
            map.put(key, vlaue);
        }
        return map.keySet();
    }

    /**
     * 使用 HMAC_ALGORITHM 加密
     * 
     * @param content 明文
     * @param secret 密钥
     * @return 密文
     */
    private String encrypt(String content, String secret) {
        try {
            byte[] text = content.getBytes(StandardCharsets.UTF_8);
            byte[] key = secret.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(key, HMAC_ALGORITHM);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return byte2hex(mac.doFinal(text));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 二进制转十六进制字符串
     * 
     * @param b 二进制数组
     * @return 十六进制字符串
     */
    private String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int n = 0; b != null && n < b.length; n++) {
            String stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append('0');
            }
            sb.append(stmp);
        }
        return sb.toString().toUpperCase();
    }
    
	/**
	 * 采集温度
	 * 实现思路：1：采集温度，2：采集光度，3：打开开关。4：关闭开关
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/loadWD",method=RequestMethod.GET)
	@ResponseBody
	public String loadWD(Model model) throws UnsupportedEncodingException{
		AliyunController client = new AliyunController();
        client.conenct(productKey, deviceName, deviceSecret);
        String updateTopic = "/" + productKey + "/" + deviceName + "/user/softwaretoware";
        JSONObject json = new JSONObject();
    	json.put("temperature", 25);
        String result = client.publish(updateTopic, json.toString().getBytes(StandardCharsets.UTF_8));
        return "data ="+result;
	}
	
	/**
	 * 采集光度
	 * 实现思路：1：采集温度，2：采集光度，3：打开开关。4：关闭开关
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/loadGD",method=RequestMethod.GET)
	@ResponseBody
	public String loadGD(Model model) throws UnsupportedEncodingException{
		AliyunController client = new AliyunController();
        client.conenct(productKey, deviceName, deviceSecret);
        String updateTopic = "/" + productKey + "/" + deviceName + "/user/softwaretoware";
        JSONObject json = new JSONObject();
    	json.put("light", 25);
        String result = client.publish(updateTopic, json.toString().getBytes(StandardCharsets.UTF_8));
        return "data ="+result;
	}
	
	/**
	 * 打开开关
	 * 实现思路：1：采集温度，2：采集光度，3：打开开关。4：关闭开关
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/open",method=RequestMethod.GET)
	@ResponseBody
	public String open(Model model) throws IOException{
		AliyunController client = new AliyunController();
        client.conenct(productKey, deviceName, deviceSecret);
        String updateTopic = "/" + productKey + "/" + deviceName + "/user/softwaretoware";
        JSONObject json = new JSONObject();
    	json.put("switch", 1);
        client.publish(updateTopic, json.toString().getBytes(StandardCharsets.UTF_8));
        return "order published";
	}
	
	/**
	 * 关闭开关
	 * 实现思路：1：采集温度，2：采集光度，3：打开开关。4：关闭开关
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/close",method=RequestMethod.GET)
	@ResponseBody
	public String close(Model model) throws IOException{
		AliyunController client = new AliyunController();
        client.conenct(productKey, deviceName, deviceSecret);
        String updateTopic = "/" + productKey + "/" + deviceName + "/user/softwaretoware";
        JSONObject json = new JSONObject();
    	json.put("switch", 1);
        client.publish(updateTopic, json.toString().getBytes(StandardCharsets.UTF_8));
        return "order published";
	}
}
