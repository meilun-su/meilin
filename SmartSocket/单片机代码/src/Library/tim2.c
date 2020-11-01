#include "main.h"
#include "tim2.h"

void TIM2_Config(uint16_t tim2val)		//
{
	T2MOD |= bT2_CLK;		//定时器时钟1/4T模式
	RCLK = 0;    //16????????
	TCLK = 0;
	CP_RL2 = 0;
	RCAP2L = TL2 = tim2val%256;			//设置定时器初值
	RCAP2H = TH2 = tim2val/256;			//设置定时器初值
	ET2 = 1;		//开定时器中断
}
//void TIM2_Enable(void)
//{
//	AUXR |= 0x10;		//定时器2开始计时
//}
//void TIM2_Disable(void)
//{
//	AUXR &= ~0x10;		//定时器2开始计时
//}


