#include "main.h"
#include "mqtt.h"
#include "kaiguan.h"

//code uint8_t  PRODUCTKEY[]={"a1z6Igb2mrC"};                                 //产品ID
//#define  PRODUCTKEY_LEN		(sizeof(PRODUCTKEY)-1)                            //产品ID长度
//code uint8_t  DEVICENAME[]={"my_switch"};                                  //设备名  
//#define  DEVICENAME_LEN       (sizeof(DEVICENAME)-1)                            //设备名长度
//code uint8_t  DEVICESECRE[]={"5e3eccdb76b47ee913a51fab8b29c758"};            //设备秘钥   
//#define  DEVICESECRE_LEN      (sizeof(DEVICESECRE)-1)                           //设备秘钥长度

code uint8_t  TOPIC_Subscribe[]={"/a1deiQK0vng/hardwaresu/user/servertohardware"};			//需要订阅的主题  (数据下传)
#define SUBSCRIBE_LEN (sizeof(TOPIC_Subscribe)-1)
code uint8_t  TOPIC_Publish[]={"/a1deiQK0vng/hardwaresu/user/hardwaretoserver"};        //需要发布的主题   (数据上传)
#define PUBLISH_LEN (sizeof(TOPIC_Publish)-1)

code uint8_t CLIENTID[]={"sjj1|securemode=3,signmethod=hmacsha1|"};			//客户ID
#define CLIENTID_LEN (sizeof(CLIENTID)-1)

code uint8_t USERNAME[]={"hardwaresu&a1deiQK0vng"};			//固定格式   ${YourDeviceName}&${YourProductKey}
#define USERNAME_LEN (sizeof(USERNAME)-1)

code uint8_t PASSWARD[]={"497A5E4C33A2A423467A53871E4F6291407D2B90"};	//密码，需要mqtt签名工具计算
#define PASSWARD_LEN (sizeof(PASSWARD)-1)

#define FIXED_LEN		2		//连接报文中，固定报头长度=2
#define VARIABLE_LEN 	10		//连接报文中，可变报头长度=10

uint8_t Payload_len;
uint8_t ClientID_len,Username_len,Passward_len;
xdata uint8_t MQTT_Buff[200];

/*----------------------------------------------------------*/
/*函数名：连接服务器报文                                    */
/*参  数：无                                                */
/*返回值：无                                                */
/*----------------------------------------------------------*/
void MQTT_ConectPack(void)
{	
//	Fixed_len = 2;                                                        //连接报文中，固定报头长度=2
//	Variable_len = 10;                                                    //连接报文中，可变报头长度=10
	
	Payload_len = 2 + CLIENTID_LEN + 2 + USERNAME_LEN + 2 + PASSWARD_LEN; //总报文长度       
	
	MQTT_Buff[0]=0x10;                              //第1个字节 ：固定0x01	
	MQTT_Buff[1]=VARIABLE_LEN + Payload_len;        //第2个字节 ：可变报头+有效负荷的长度
	MQTT_Buff[2]=0x00;          				    //第3个字节 ：固定0x00	            
	MQTT_Buff[3]=0x04;                              //第4个字节 ：固定0x04
	MQTT_Buff[4]=0x4D;								//第5个字节 ：固定0x4D
	MQTT_Buff[5]=0x51;								//第6个字节 ：固定0x51
	MQTT_Buff[6]=0x54;								//第7个字节 ：固定0x54
	MQTT_Buff[7]=0x54;								//第8个字节 ：固定0x54
	MQTT_Buff[8]=0x04;								//第9个字节 ：固定0x04
	MQTT_Buff[9]=0xC2;								//第10个字节：使能用户名和密码校验，不使用遗嘱，不保留会话
	MQTT_Buff[10]=0x00; 						    //第11个字节：保活时间高字节 0x00
	MQTT_Buff[11]=0x3c;								//第12个字节：保活时间高字节 0x64   100s
	
	/*     CLIENT_ID      */
	MQTT_Buff[12] = 0;//CLIENTID_LEN/256;                			  			//客户端ID长度高字节
	MQTT_Buff[13] = CLIENTID_LEN%256;               			  			//客户端ID长度低字节
	memcpy(&MQTT_Buff[14],CLIENTID,CLIENTID_LEN);                 			//复制过来客户端ID字串	
	/*     用户名        */
	MQTT_Buff[14+CLIENTID_LEN] = 0;//USERNAME_LEN/256; 				  		    //用户名长度高字节
	MQTT_Buff[15+CLIENTID_LEN] = USERNAME_LEN%256; 				 		    //用户名长度低字节
	memcpy(&MQTT_Buff[16+CLIENTID_LEN],USERNAME,USERNAME_LEN);              //复制过来用户名字串	
	/*      密码        */
	MQTT_Buff[16+CLIENTID_LEN+USERNAME_LEN] = 0;//PASSWARD_LEN/256;			    //密码长度高字节
	MQTT_Buff[17+CLIENTID_LEN+USERNAME_LEN] = PASSWARD_LEN%256;			    //密码长度低字节
	memcpy(&MQTT_Buff[18+CLIENTID_LEN+USERNAME_LEN],PASSWARD,PASSWARD_LEN); //复制过来密码字串

	MQTT_sendBuff(MQTT_Buff, FIXED_LEN + VARIABLE_LEN + Payload_len);      //加入发送数据缓冲区
}
/*----------------------------------------------------------*/
/*函数名：SUBSCRIBE订阅topic报文                            */
/*参  数：QoS：订阅等级                                     */
/*参  数：topic_name：订阅topic报文名称                     */
/*返回值：无                                                */
/*----------------------------------------------------------*/
void MQTT_Subscribe(void)
{	
	//Fixed_len = 2;                              //SUBSCRIBE报文中，固定报头长度=2
	//Variable_len = 2;                           //SUBSCRIBE报文中，可变报头长度=2	
	Payload_len = 2 + SUBSCRIBE_LEN + 1;   //计算有效负荷长度 = 2字节(topic_name长度)+ topic_name字符串的长度 + 1字节服务等级
	
	MQTT_Buff[0]=0x82;                                    //第1个字节 ：固定0x82                      
	MQTT_Buff[1]=VARIABLE_LEN + Payload_len;              //第2个字节 ：可变报头+有效负荷的长度	
	MQTT_Buff[2]=0x00;                                    //第3个字节 ：报文标识符高字节，固定使用0x00
	MQTT_Buff[3]=0x01;		                              //第4个字节 ：报文标识符低字节，固定使用0x01
	MQTT_Buff[4]=SUBSCRIBE_LEN/256;                  //第5个字节 ：topic_name长度高字节
	MQTT_Buff[5]=SUBSCRIBE_LEN%256;		          //第6个字节 ：topic_name长度低字节
	memcpy(&MQTT_Buff[6],TOPIC_Subscribe,SUBSCRIBE_LEN);  //第7个字节开始 ：复制过来topic_name字串		
	MQTT_Buff[6+SUBSCRIBE_LEN]=0;                  //最后1个字节：订阅等级为0
	
	MQTT_sendBuff(MQTT_Buff, FIXED_LEN + VARIABLE_LEN + Payload_len);  //加入发送数据缓冲区
}

/*----------------------------------------------------------*/
/*函数名：等级0 发布消息报文                                */
/*参  数：topic_name：topic名称                             */
/*参  数：data：数据                                        */
/*参  数：data_len：数据长度                                */
/*返回值：无                                                */
/*----------------------------------------------------------*/
//xdata uint8_t Json_WenduTab[]={"{\"temperature\":25}"};
//#define Json_WenduTab_Len (sizeof(Json_WenduTab)-1)
//xdata uint8_t Json_LightTab[]={"{\"light\":125}"};
//#define Json_LightTab_Len (sizeof(Json_LightTab)-1)

xdata uint8_t Json_Tab[]={"{\"temperature\":25,\"light\":125}"};
#define Json_Tab_Len (sizeof(Json_Tab)-1)

void MQTT_PublishQs0(uint8_t wendu, uint8_t guang)
{	
	uint8_t Variable_len;
	//Fixed_len = 2;                             //固定报头长度：2字节
	Variable_len = 2 + PUBLISH_LEN;          //可变报头长度：2字节(topic长度)+ topic字符串的长度
	Payload_len = Json_Tab_Len;                    //有效负荷长度：就是data_len
	
	MQTT_Buff[0]=0x30;                         //第1个字节 ：固定0xC0                
	MQTT_Buff[1]=Variable_len + Payload_len;   //第2个字节 ：可变报头+有效负荷的长度	
	MQTT_Buff[2]=PUBLISH_LEN/256;            //第3个字节 ：topic长度高字节
	MQTT_Buff[3]=PUBLISH_LEN%256;		       //第4个字节 ：topic长度低字节
	memcpy(&MQTT_Buff[4],TOPIC_Publish,PUBLISH_LEN); //第5个字节开始 ：拷贝topic字符串	
	
	Json_Tab[15]=wendu/10%10+'0';
	Json_Tab[16]=wendu%10+'0';
	
	Json_Tab[26]=guang/100%10+'0';
	Json_Tab[27]=guang/10%10+'0';
	Json_Tab[28]=guang%10+'0';
	
	memcpy(&MQTT_Buff[4+PUBLISH_LEN],Json_Tab,Json_Tab_Len);   //拷贝data数据
	
//	Json_WenduTab[15]=wendu/10%10+'0';
//	Json_WenduTab[16]=wendu%10+'0';
//	
//	Json_LightTab[9]=guang/100%10+'0';
//	Json_LightTab[10]=guang/10%10+'0';
//	Json_LightTab[11]=guang%10+'0';
	
	
	
	//memcpy(&MQTT_Buff[4+PUBLISH_LEN+Json_WenduTab_Len],Json_LightTab,Json_LightTab_Len);
	
	MQTT_sendBuff(MQTT_Buff, FIXED_LEN + Variable_len + Payload_len);  //加入发送数据缓冲区
}
/*----------------------------------------------------------*/
/*函数名：处理服务器发来的等级0的推送                       */
/*参  数：redata：接收的数据                                */
/*返回值：无                                                */
/*----------------------------------------------------------*/
void MQTT_DealPushdata_Qs0(unsigned char *redata)
{
	//uint8_t  re_len;               	           //定义一个变量，存放接收的数据总长度
	//uint8_t  pack_num;                         //定义一个变量，当多个推送一起过来时，保存推送的个数
    //uint8_t  temp_len;                         //定义一个变量，暂存数据
    uint8_t  totle_len;                        //定义一个变量，存放已经统计的推送的总数据量
	uint8_t  topic_len;              	       //定义一个变量，存放推送中主题的长度
	//uint8_t  cmd_len;                          //定义一个变量，存放推送中包含的命令数据的长度
	//uint8_t  cmd_loca;                         //定义一个变量，存放推送中包含的命令的起始位置
	//uint8_t  i;                                //定义一个变量，用于for循环
	xdata uint8_t temp[64];	       //临时缓冲区
	//uint8_t *data1;                   //redata过来的时候，第一个字节是数据总量，data用于指向redata的第2个字节，真正的数据开始的地方
		
	if(redata[0]!=0x30)return;			//服务器发送数据到设备的固定报头
	totle_len =	redata[1];
	topic_len = redata[3];				//Topic长度小于256时可直接读取低位的值
	memcpy(temp,&redata[2+2+topic_len],totle_len-1-topic_len);
	if(strstr(temp,"{\"switch\":1}"))	//打开开关
	{
		//switch_mode=1;
		switch_open();
	}
	else if(strstr(temp,"{\"switch\":0}"))	//关闭开关
	{
		//switch_mode=0;
		switch_close();
	}
	
	
//	re_len = redata[0];                                   			//获取接收的数据总长度
//	data1 = &redata[1];                                              //data指向redata的第2个字节，真正的数据开始的 
//	pack_num = temp_len = totle_len = 0;                			//各个变量清零
//	do{
//		pack_num++;                                     			//开始循环统计推送的个数，每次循环推送的个数+1
//		temp_len = data1[1+totle_len]+2;                 			//计算本次统计的推送的数据长度
//		totle_len += temp_len;                          			//累计统计的总的推送的数据长度
//		re_len -= temp_len ;                            			//接收的数据总长度 减去 本次统计的推送的总长度      
//	}while(re_len!=0);                                  			//如果接收的数据总长度等于0了，说明统计完毕了
//	//if(pack_num!=1)u1_printf("本次接收了%d个推送数据\r\n",pack_num);//串口输出信息
//	temp_len = totle_len = 0;                		            	//各个变量清零
//	for(i=0;i<pack_num;i++){                                        //已经统计到了接收的推送个数，开始for循环，取出每个推送的数据 
//		topic_len = data1[2+totle_len]*256+data1[3+totle_len] + 2;    //计算本次推送数据中主题占用的数据量
//		cmd_len = data1[1+totle_len]-topic_len;                      //计算本次推送数据中命令数据占用的数据量
//		cmd_loca = totle_len + 2 +  topic_len;                      //计算本次推送数据中命令数据开始的位置
//		memcpy(temp,&data1[cmd_loca],cmd_len);                       //命令数据拷贝出来		                 
//		CMDBuf_Deal(temp, cmd_len);                                 //加入命令到缓冲区
//		temp_len = data1[1+totle_len]+2;                             //记录本次推送的长度
//		totle_len += temp_len;                                      //累计已经统计的推送的数据长度
//	}	
}
/*----------------------------------------------------------*/
/*函数名：PING报文，心跳包                                  */
/*参  数：无                                                */
/*返回值：无                                                */
/*----------------------------------------------------------*/
void MQTT_PingREQ(void)
{
	MQTT_Buff[0]=0xC0;              //第1个字节 ：固定0xC0                      
	MQTT_Buff[1]=0x00;              //第2个字节 ：固定0x00 

	MQTT_sendBuff(MQTT_Buff, 2);   //加入数据到缓冲区
}
/*----------------------------------------------------------*/
/*函数名：与云平台断开连接                                  */
/*参  数：无                                                */
/*返回值：无                                                */
/*----------------------------------------------------------*/
void MQTT_disconect(void)
{
	MQTT_Buff[0]=0xe0;              //第1个字节 ：固定0xC0                      
	MQTT_Buff[1]=0x00;              //第2个字节 ：固定0x00 

	MQTT_sendBuff(MQTT_Buff, 2);   //加入数据到缓冲区
}


//void memcpy(uint8_t *targetData,uint8_t *sourceData,uint8_t len)
//{
//	uint8_t i;
//	for(i=0;i<len;i++)
//	{
//		targetData[i] = sourceData[i];
//	}
//}



