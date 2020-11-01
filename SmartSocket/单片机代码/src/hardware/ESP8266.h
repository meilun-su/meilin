#ifndef __ESP8266_H__
#define __ESP8266_H__

#define MODE_EXITTRANSP			0
#define MODE_CONNECT_WIFI		1
#define MODE_CONNECT_SERVER		2
#define MODE_CONNECT_CLOUD		3
#define MODE_CONMMUNICATION		4

//rx_mode
#define AT_MODE		1
#define MQTT_MODE	2
#define ONE_MODE	3


extern xdata uint8_t ESP8266_rxBuff[128];
extern uint8_t rx_cnt;
extern bit rx_over;
extern uint8_t rx_mode,uartRxTimeout;

void ESP8266_Init(void);
void ESP8266_exitTransparentMode(void);
void ESP6266_connectIOTServer(void);
void ESP8266_connectWifi(void);
void ESP8266_connectServer(void);
void ESP6266_conmmunication(void);

#endif //__ESP8266_H__
