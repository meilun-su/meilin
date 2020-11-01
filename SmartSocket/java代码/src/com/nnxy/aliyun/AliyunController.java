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
	// ����ID����ǰ��֧�ֻ���2���Ϻ���
    private static String regionId = "cn-shanghai";
    // ������ܷ�ʽ��MAC�㷨��ѡ���¶����㷨 HmacMD5 HmacSHA1�����signmethodһ��	
    private static final String HMAC_ALGORITHM = "hmacsha1";
    // token 7����Ч��ʧЧ����Ҫ���»�ȡ
    private String token = null;

    //���ӵ�������������ƽ̨��Կ
    String productKey = "a1deiQK0vng"; 
    //���ӵ�������������ƽ̨�豸���ƣ����̶�����ͬ���豸��ֵ��һ������Ҫ�޸Ķ�Ӧ���豸ֵ
    String deviceName = "softwaresu";
    //���ӵ�������������ƽ̨�豸Ψһֵ�����̶�����ͬ���豸��ֵ��һ������Ҫ�޸Ķ�Ӧ��Ψһֵ
    String deviceSecret = "3d0c17ca43552ddaf49726540f4ff9d0";
    /**
     * ��ʼ��HTTP�ͻ���
     * 
     * @param productKey ��Ʒkey
     * @param deviceName �豸����
     * @param deviceSecret �豸Ψһֵ
     */
    public void conenct(String productKey, String deviceName, String deviceSecret) {
        try {
            // ע���ַ
            URL url = new URL("https://iot-as-http." + regionId + ".aliyuncs.com/auth");

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // ��ȡURLConnection�����Ӧ�������
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(authBody(productKey, deviceName, deviceSecret));
            // flush������Ļ���
            out.flush();
            // ��ȡURLConnection�����Ӧ��������
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // ��ȡURL����Ӧ
            String result = "";
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println("----- auth result -----");
            System.out.println(result);

            // �ر����������
            in.close();
            out.close();
            conn.disconnect();

            // ��ȡtoken
            JSONObject json = JSONObject.parseObject(result);
            if (json.getIntValue("code") == 0) {
                token = json.getJSONObject("info").getString("token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ������Ϣ
     * 
     * @param topic ������Ϣ��Topic
     * @param payload ��Ϣ����
     */
    public String publish(String topic, byte[] payload) {
    	String result = "";
        try {
            // ע���ַ
        	URL url = new URL("https://iot-as-http." + regionId + ".aliyuncs.com/topic" + topic);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/octet-stream");
            conn.setRequestProperty("password", token);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // ��ȡURLConnection�����Ӧ�������
            BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(payload);
            out.flush();
            // ��ȡURLConnection�����Ӧ��������
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // ��ȡURL����Ӧ
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println("----- publish result -----");
            System.out.println(result);

            // �ر����������
            in.close();
            out.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * ������֤��������
     * 
     * @param params ��֤����
     * @return ��֤������Ϣ��
     */
    private String authBody(String productKey, String deviceName, String deviceSecret) {

        // ������֤����
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
     * �豸��ǩ��
     * 
     * @param params ǩ������
     * @param deviceSecret �豸��Կ
     * @return ǩ��ʮ�������ַ���
     */
    private String sign(JSONObject params, String deviceSecret) {
        // ����������ֵ�˳������
        Set<String> keys = getSortedKeys(params);
        // sign��signmethod �� version����
        keys.remove("sign");
        keys.remove("signmethod");	
        keys.remove("version");
        // ��װǩ������
        StringBuffer content = new StringBuffer();
        for (String key : keys) {
            content.append(key);
            content.append(params.getString(key));
        }
        // ����ǩ��
        String sign = encrypt(content.toString(), deviceSecret);
        System.out.println("sign content=" + content);
        System.out.println("sign result=" + sign);

        return sign;
    }

    /**
     * ��ȡJSON����������key����
     * 
     * @param json ��Ҫ�����JSON����
     * @return ������key����
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
     * ʹ�� HMAC_ALGORITHM ����
     * 
     * @param content ����
     * @param secret ��Կ
     * @return ����
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
     * ������תʮ�������ַ���
     * 
     * @param b ����������
     * @return ʮ�������ַ���
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
	 * �ɼ��¶�
	 * ʵ��˼·��1���ɼ��¶ȣ�2���ɼ���ȣ�3���򿪿��ء�4���رտ���
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
	 * �ɼ����
	 * ʵ��˼·��1���ɼ��¶ȣ�2���ɼ���ȣ�3���򿪿��ء�4���رտ���
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
	 * �򿪿���
	 * ʵ��˼·��1���ɼ��¶ȣ�2���ɼ���ȣ�3���򿪿��ء�4���رտ���
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
	 * �رտ���
	 * ʵ��˼·��1���ɼ��¶ȣ�2���ɼ���ȣ�3���򿪿��ء�4���رտ���
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
