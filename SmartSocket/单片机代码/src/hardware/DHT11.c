#include "main.h"
#include "DHT11.h"
#include "JLX12864G.h"

uint8_t dataTemp[4];

//void DHT11_Delay_ms(uint16_t ms)
//{
//	
//}

void DHT11_init(void)
{
	SetPINQuasiBidirectional(DHT11_PORT,DHT11_PIN);
	SET_PIN_H(DHT11_PORT,DHT11_PIN);
}

void DHT11_readData(void)
{
	uint8_t eorrCnt,i;
	
	SET_PIN_L(DHT11_PORT,DHT11_PIN);
	Delay_ms(20);
	SET_PIN_H(DHT11_PORT,DHT11_PIN);
	//Delay_us(30);
	eorrCnt=0;
	while(READ_PIN(DHT11_PORT,DHT11_PIN)==1)
	{
		eorrCnt++;
		Delay_us(1);
		if(eorrCnt>200)
		{
			return;
		}
	}
	while(READ_PIN(DHT11_PORT,DHT11_PIN)==0);
	while(READ_PIN(DHT11_PORT,DHT11_PIN)==1);
	for(i=0;i<5;i++)
	{
		dataTemp[i]=DHT11_readOneByte();
	}
	SET_PIN_H(DHT11_PORT,DHT11_PIN);
}
uint8_t DHT11_readOneByte(void)
{
	uint8_t i,temp;
	for(i=0;i<8;i++)
	{
		while(READ_PIN(DHT11_PORT,DHT11_PIN)==0);
		Delay_us(50);
		temp<<=1;
		if(READ_PIN(DHT11_PORT,DHT11_PIN)==1)
		{
			temp|=0x01;
			while(READ_PIN(DHT11_PORT,DHT11_PIN)==1);
		}
		else
		{
			
		}
	}
	return temp;
}

