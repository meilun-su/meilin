#include "main.h"
#include "key.h"
#include "delay.h"

idata uint8_t key_cval;
idata uint8_t PortTmpBak;
idata uint8_t key_db;


void Key_Init(void)
{
	SetPINQuasiBidirectional(KEY_PORT,S2_KEY_PIN);
	SetPINQuasiBidirectional(KEY_PORT,S3_KEY_PIN);
	SetPINQuasiBidirectional(KEY_PORT,S4_KEY_PIN);
	SetPINQuasiBidirectional(KEY_PORT,S5_KEY_PIN);
	SET_PIN_H(KEY_PORT,S2_KEY_PIN);
	SET_PIN_H(KEY_PORT,S3_KEY_PIN);
	SET_PIN_H(KEY_PORT,S4_KEY_PIN);
	SET_PIN_H(KEY_PORT,S5_KEY_PIN);
}
void keyscan(void)
{
	uint8_t PortTmp;
	
	PortTmp=READ_PORT(KEY_PORT)|(~KEY_ALL);
	PortTmp=~PortTmp;
	
	if(PortTmpBak == PortTmp)
	{
		key_db++;
		if(key_db>=(2/MAIN_CYCTIME))
		{
			//更新按键值
			key_cval = PortTmpBak;
		}
	}
	else
	{
		key_db=0;
		PortTmpBak = PortTmp;
	}
}


