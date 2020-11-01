package com.meilun.su;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User工具类
 * @author Administrator
 *
 */
public class User {
	private String name;
	private String ip;
	
	public User(String name,String ip){
		this.name=name;
		this.ip=ip;
		}
		public String getName() {
		
			return name;
		}
		public void setName(String name){
			this.name=name;
		}
		public String getIp() {
			
			return ip;
		}
		public void setIp(String ip){
			this.ip=ip;
		}
		//获取格式化的当前时间字符串形式
		public static String getTimeStr(){
		SimpleDateFormat fm=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		
		return fm.format(new Date());
		
		}
}