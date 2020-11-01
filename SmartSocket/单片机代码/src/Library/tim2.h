#ifndef	__TIM2_H__
#define	__TIM2_H__
void TIM2_Config(uint16_t tim2val);
//void TIM2_Enable(void);

#define TIM2_Enable() TR2 = 1
#define TIM2_Disable() TR2 = 0

#endif	//__TIM3_H__
