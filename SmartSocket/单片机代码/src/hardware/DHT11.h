#ifndef __DHT11_H__
#define __DHT11_H__

#define DHT11_PORT	P0
#define DHT11_PIN		7

extern uint8_t dataTemp[4];

void DHT11_init(void);
void DHT11_readData(void);
uint8_t DHT11_readOneByte(void);
#endif //__DHT11_H__
