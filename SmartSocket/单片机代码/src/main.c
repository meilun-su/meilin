#include "main.h"
#include "JLX12864G.h"
#include "key.h"
#include "guang.h"
#include "DHT11.h"
#include "ESP8266.h"
#include "mqtt.h"
#include "kaiguan.h"

uint8_t main_TM,mode;
uint16_t pingTimeCnt,DHT11_startCnt;
bit switch_mode;

void main(void)
{
	Delay_ms(200);
	Clock_Config();
	switch_init();
	
	LCD_init();
	LCD_clearScreen();
	
	TIM0_Config();
	Key_Init();
	
	ADC_Config();
	DHT11_init();
	uart0_Config();
	
	LCD_displayString8x16(0,0," IoT experiment ");
	
	EA=1;
	TIM0_Enable();
	mode = MODE_EXITTRANSP;
	rx_mode = AT_MODE;
	uartRxTimeout=0;
	while(1)
	{
		while(main_TM==0);
		main_TM=0;
		
		DHT11_readData();
		guangdu=ADC_Read_Value(1);
		
		switch(mode)
		{
			case MODE_EXITTRANSP:
				ESP8266_exitTransparentMode();	//退出透传模式
			break;
			case MODE_CONNECT_WIFI:		//连接WIFI
				ESP8266_connectWifi();
			break;
			case MODE_CONNECT_SERVER:	//连接服务器
				ESP8266_connectServer();
			break;
			case MODE_CONNECT_CLOUD:		//连接物联网云平台
				ESP6266_connectIOTServer();
			break;
			case MODE_CONMMUNICATION:		//与物联网云平台通信
				ESP6266_conmmunication();
			break;
		}
		if(uartRxTimeout>=20)
		{
			rx_over=1;
			rx_cnt=0;
			uartRxTimeout=0;
		}
		if(rx_over)
		{
			rx_over=0;
			if(rx_mode==AT_MODE)
				LCD_displayString8x16(2,0,ESP8266_rxBuff);
			else if(rx_mode==MQTT_MODE)
			{
				MQTT_DealPushdata_Qs0(ESP8266_rxBuff);
				LCD_displayMQTTArray8x16(2,0,ESP8266_rxBuff);
			}
		}
		
		
//		DHT11_startCnt++;
//		if((DHT11_startCnt>1000))
//		{
//			DHT11_startCnt = 2000;
//			DHT11_readData();
//			
//			LCD_displayString8x16(2,0,"wendu:");
//			LCD_display8x16(2,8*8,dataTemp[2]/100%10+0x30);
//			LCD_display8x16(2,9*8,dataTemp[2]/10%10+0x30);
//			LCD_display8x16(2,10*8,dataTemp[2]%10+0x30);
//			'
//			LCD_displayString8x16(4,0,"shidu:");
//			LCD_display8x16(4,8*8,dataTemp[0]/100%10+0x30);
//			LCD_display8x16(4,9*8,dataTemp[0]/10%10+0x30);
//			LCD_display8x16(4,10*8,dataTemp[2]%10+0x30);
//		}
//		
		keyscan();
		readGuang();			//读取光照度
//		
//		LCD_displayString8x16(0,0,"guang du:");
//		LCD_display8x16(0,9*8,guangdu/1000%10+0x30);
//		LCD_display8x16(0,10*8,guangdu/100%10+0x30);
//		LCD_display8x16(0,11*8,guangdu/10%10+0x30);
//		LCD_display8x16(0,12*8,guangdu%10+0x30);
//		
//		switch(key_cval)
//		{
//			case S2_KEY:
//				LCD_displayString8x16(0,0,"key1");
//			break;
//			case S3_KEY:
//				LCD_displayString8x16(2,0,"key2");
//			break;
//			case S4_KEY:
//				LCD_displayString8x16(4,0,"key3");
//			break;
//			case S5_KEY:
//				LCD_displayString8x16(6,0,"key4");
//			break;
//			default : break;
//		}

	}
}

//===========================================================
//    TIMER 0 interrupt subroutine
//===========================================================
void Timer0_ISR (void) interrupt 1  //interrupt address is 0x000B
{
    TH0=TH0VAL;
	TL0=TL0VAL;
    main_TM++;
	if(uartRxTimeout)uartRxTimeout++;
//    P17^=1;
}
