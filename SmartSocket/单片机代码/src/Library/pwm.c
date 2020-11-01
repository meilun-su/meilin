#include "main.h"
#include "pwm.h"

void pwm_Config(void)
{
	PWM_CK_SE = 1;				//2分频
	PWM_CTRL |= bPWM_CLR_ALL;	//清除PWM计数
	//PWM_CTRL |= bPWM0_POLAR;	//PWM0默认输出高电平，低电平有效
	//PWM_CTRL &= ~bPWM 0_MOD_6BIT;//8位PWM模式
	PWM_CTRL &= ~bPWM_CLR_ALL;	//关闭清除PWM计数
	PWM_DATA0 = 0x80;
	PWM_CTRL |= bPWM0_OUT_EN;	//使能PWM0输出
	
}
void pwm_Enable(void)
{
	PWM_CTRL |= bPWM0_OUT_EN;	//使能PWM0输出
}
void pwm_Disable(void)
{
	PWM_CTRL &= ~bPWM0_OUT_EN;	//不使能PWM0输出
}

