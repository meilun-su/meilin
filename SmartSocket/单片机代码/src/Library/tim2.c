#include "main.h"
#include "tim2.h"

void TIM2_Config(uint16_t tim2val)		//
{
	T2MOD |= bT2_CLK;		//��ʱ��ʱ��1/4Tģʽ
	RCLK = 0;    //16????????
	TCLK = 0;
	CP_RL2 = 0;
	RCAP2L = TL2 = tim2val%256;			//���ö�ʱ����ֵ
	RCAP2H = TH2 = tim2val/256;			//���ö�ʱ����ֵ
	ET2 = 1;		//����ʱ���ж�
}
//void TIM2_Enable(void)
//{
//	AUXR |= 0x10;		//��ʱ��2��ʼ��ʱ
//}
//void TIM2_Disable(void)
//{
//	AUXR &= ~0x10;		//��ʱ��2��ʼ��ʱ
//}


