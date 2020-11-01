#ifndef	__TIM1_H__
#define	__TIM1_H__

#define TIM1_Enable() TR1=1
#define TIM1_Disable() TR1=0

void TIM1_Config(uint16_t VAL);
//void TIM1_Enable(void);
//void TIM1_Disable(void);

#endif //__TIM1_H__
