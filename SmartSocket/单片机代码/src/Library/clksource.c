#include "main.h"
#include "clksource.h"
//===========================================================
//		≈‰÷√ ±÷”‘¥
//===========================================================
#if (MCU==STC8A4K32)

#elif (MCU==CH547)
void Clock_Config(void)
{
	
#if(FOSC==24000000UL)
	uint8_t temp;
	SAFE_MOD = 0x55;
	SAFE_MOD = 0xaa;
	temp = CLOCK_CFG & 0xe0;
	CLOCK_CFG = temp | 5;
	SAFE_MOD = 0x00;
#elif(FOSC==48000000UL)
	uint8_t temp;
	SAFE_MOD = 0x55;
	SAFE_MOD = 0xaa;
	temp = CLOCK_CFG & 0xe0;
	CLOCK_CFG = temp | 7;
	SAFE_MOD = 0x00;
#elif(FOSC==16000000UL)
//	//CKDIV=0x00;//
#endif //FOSE
}
#endif //MCU
