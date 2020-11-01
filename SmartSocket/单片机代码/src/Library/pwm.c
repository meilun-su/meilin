#include "main.h"
#include "pwm.h"

void pwm_Config(void)
{
	PWM_CK_SE = 1;				//2��Ƶ
	PWM_CTRL |= bPWM_CLR_ALL;	//���PWM����
	//PWM_CTRL |= bPWM0_POLAR;	//PWM0Ĭ������ߵ�ƽ���͵�ƽ��Ч
	//PWM_CTRL &= ~bPWM 0_MOD_6BIT;//8λPWMģʽ
	PWM_CTRL &= ~bPWM_CLR_ALL;	//�ر����PWM����
	PWM_DATA0 = 0x80;
	PWM_CTRL |= bPWM0_OUT_EN;	//ʹ��PWM0���
	
}
void pwm_Enable(void)
{
	PWM_CTRL |= bPWM0_OUT_EN;	//ʹ��PWM0���
}
void pwm_Disable(void)
{
	PWM_CTRL &= ~bPWM0_OUT_EN;	//��ʹ��PWM0���
}

