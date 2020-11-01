#include "main.h"
#include "delay.h"

#if (FOSC==24000000UL)	//Ö÷Ê±ÖÓÆµÂÊ
void Delay_ms(uint16_t n)
{
	uint8_t i,j;
	while(n--)
	{
		i = 32;
		j = 40;
		do
		{
			while (--j);
		} while (--i);
	}
}
void Delay_us(uint16_t n)
{
	while(n--)
	{
		_nop_();
		_nop_();
		_nop_();
		_nop_();
		_nop_();
		_nop_();
	}
}
#elif (FOSC==48000000UL)
void Delay_ms(uint16_t n)
{
	uint8_t i,j;
	while(n--)
	{
		i = 31;
		j = 250;
		do
		{
			_nop_();
			while (--j);
		} while (--i);
	}
}
void Delay_us(uint16_t n)
{
	uint8_t i;
	while(n--)
	{
		i = 5;
		do
		{
			_nop_();
		} while (--i);
	}
//	while(n--)
//	{
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//		
//		_nop_();
//		_nop_();
//		_nop_();
//		_nop_();
//	}
}
#else
#error "No Delay at this FOSC!!!"
#endif //FOSC
