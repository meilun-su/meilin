#ifndef __MQTT_H__
#define __MQTT_H__

#define MQTT_PINGTIME_MAX	10000

void MQTT_ConectPack(void);
void MQTT_Subscribe(void);

void MQTT_PingREQ(void);
void MQTT_PublishQs0(uint8_t wendu, uint8_t guang);
void MQTT_disconect(void);
void MQTT_DealPushdata_Qs0(unsigned char *redata);

#define MQTT_sendBuff uart0_send_data

#endif //__MQTT_H__
