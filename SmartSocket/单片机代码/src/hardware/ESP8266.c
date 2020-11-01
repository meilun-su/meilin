#include "main.h"
#include "ESP8266.h"
#include "key.h"
#include "JLX12864G.h"
#include "mqtt.h"
#include "DHT11.h"
#include "guang.h"
//code uint8_t WIFI_Name[]={"PCT-AL10%01%CloudClone"};
//code uint8_t WIFI_Key[]={"21203873"};

xdata uint8_t ESP8266_rxBuff[128];
xdata uint8_t ESP8266_txBuff[64];
uint8_t rx_cnt,tx_cnt;
bit rx_over;
bit key_flag;
uint16_t pingCnt;
uint8_t rx_mode,uartRxTimeout;
//void ESP8266_Init(void)
//{
//	ESP8266_exitTransparentMode();
//}
//退出透传模式
void ESP8266_exitTransparentMode(void)
{

	switch(key_cval)
	{
		case S2_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				rx_mode=AT_MODE;
				uart0_send_str("+++");
			}
		break;
		case S3_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				rx_mode=AT_MODE;
				uart0_send_str("AT+CIPMODE=0\r\n");
			}
		break;
		case S4_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				mode=MODE_CONNECT_WIFI;
				LCD_clearScreen();
				LCD_displayString8x16(0,0,"  connect wifi  ");
			}
		break;
		case S5_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				mode=MODE_CONNECT_SERVER;
				LCD_clearScreen();
				LCD_displayString8x16(0,0," connect server ");
			}
		break;
		default : key_flag=0;break;
	}
}
//连接WIFI
void ESP8266_connectWifi(void)
{
	
	switch(key_cval)
	{
		case S2_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				rx_mode=AT_MODE;
				uart0_send_str("AT+CIPMODE=3\r\n");
			}
		break;
		case S3_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				rx_mode=AT_MODE;
				uart0_send_str("AT+CWJAP_DEF=\"meilun_su\",\"xs045201.\"\r\n");
			}
		break;
		case S4_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				mode=MODE_EXITTRANSP;
				LCD_clearScreen();
				LCD_displayString8x16(0,0," IoT experiment ");
				//uart0_send_str("AT+CWJAP_DEF=\"PCT-AL10%01%CloudClone\",\"21203873\"\r\n");
			}
		break;
		case S5_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				mode=MODE_CONNECT_SERVER;
				LCD_clearScreen();
				LCD_displayString8x16(0,0," connect server ");
			}
		break;
		default : key_flag=0;break;
	}
}
//连接服务器
void ESP8266_connectServer(void)
{
	switch(key_cval)
	{
		case S2_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				rx_mode=AT_MODE;
				uart0_send_str("AT+CIPSTART=\"TCP\",\"a1deiQK0vng.iot-as-mqtt.cn-shanghai.aliyuncs.com\",1883\r\n");
			}
		break;
		case S3_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				rx_mode=AT_MODE;
				uart0_send_str("AT+CIPMODE=1\r\n");
			}
		break;
		case S4_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				rx_mode=ONE_MODE;
				uart0_send_str("AT+CIPSEND\r\n");
			}
		break;
		case S5_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				mode=MODE_CONNECT_CLOUD;
				LCD_clearScreen();
				LCD_displayString8x16(0,0," connect cloud  ");
			}
		break;
		default : key_flag=0;break;
	}
}

//连接物联网云平台
void ESP6266_connectIOTServer(void)
{
	switch(key_cval)
	{
		case S2_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				rx_mode=MQTT_MODE;
				MQTT_ConectPack();		//发送连接
			}
		break;
		case S3_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				rx_mode=MQTT_MODE;
				MQTT_Subscribe();		//开启订阅
			}
		break;
		case S4_KEY:
			if(key_flag==0)
			{
				key_flag=1;
			}
		break;
		case S5_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				mode=MODE_CONMMUNICATION;
				LCD_clearScreen();
				LCD_displayString8x16(0,0,"IoT conmmunicat ");
			}
		break;
		default : key_flag=0;break;
	}
	
}

//连接物联网云平台
void ESP6266_conmmunication(void)
{
	pingCnt++;
	if(pingCnt>=10000/MAIN_CYCTIME)
	{
		pingCnt=0;
		rx_mode=MQTT_MODE;
		MQTT_PingREQ();
	}
	switch(key_cval)
	{
		case S2_KEY:		//发送数据
			if(key_flag==0)
			{
				key_flag=1;
				pingCnt=0;	//心跳包计数归零
				rx_mode=MQTT_MODE;
				MQTT_PublishQs0(dataTemp[2],guangdu);
			}
		break;
		case S3_KEY:		//
			if(key_flag==0)
			{
				key_flag=1;
				pingCnt=0;	//心跳包计数归零
			}
		break;
		case S4_KEY:
			if(key_flag==0)
			{
				key_flag=1;
				pingCnt=0;	//心跳包计数归零
			}
		break;
		case S5_KEY:		//断开连接物联网云平台
			if(key_flag==0)
			{
				key_flag=1;
				pingCnt=0;	//心跳包计数归零
				rx_mode=MQTT_MODE;
				MQTT_disconect();
				mode=MODE_EXITTRANSP;
				LCD_clearScreen();
				LCD_displayString8x16(0,0," IoT experiment ");
			}
		break;
		default : key_flag=0;break;
	}
}

//uint8_t compareStr(uint8_t *str)
//{
//	while()
//}

/*----------------------------
Uart1_ISR 中断服务程序
-----------------------------*/
void Uart1_ISR() interrupt 4
{
	//uint8_t temp;
    if (RI)
    {
        RI = 0;                 //清除RI位
		ESP8266_rxBuff[rx_cnt] = SBUF;
		uartRxTimeout=1;
		if(rx_mode==MQTT_MODE)
		{
			if(rx_cnt>=1)
			{
				if((rx_cnt-1)>=ESP8266_rxBuff[1])
				{
					rx_over=1;		//结束
					rx_cnt++;
					ESP8266_rxBuff[rx_cnt] = 0x00;
					rx_cnt=0;
				}
				else rx_cnt++;
			}
			else rx_cnt++;
		}
		else if(rx_mode==AT_MODE)
		{
			if((ESP8266_rxBuff[rx_cnt-1] == 0x0d)&&(ESP8266_rxBuff[rx_cnt] == 0x0a))
			{
				rx_over=1;		//结束
				rx_cnt++;
				ESP8266_rxBuff[rx_cnt] = 0x00;
				rx_cnt=0;
			}
			else
			{
				rx_cnt++;
			}
		}
		else//(rx_mode==ONE_MODE)
		{
			rx_over=1;		//结束
			rx_cnt++;
			ESP8266_rxBuff[rx_cnt] = 0x00;
			rx_cnt=0;
		}
    }
    if (TI)
    {
		TI = 0;			//清除TI位
//		if(ESP8266_rxBuff[tx_cnt])
//		{
//			ESP8266_rxBuff[tx_cnt]
//		}
    }
}

