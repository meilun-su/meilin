#ifndef	__TIM4_H__
#define	__TIM4_H__

#define	TH0VAL ((65536-FOSC/4/1000*MAIN_CYCTIME)/256)
#define	TL0VAL ((65536-FOSC/4/1000*MAIN_CYCTIME)%256)

void TIM0_Config(void);
//void TIM0_Enable(void);
//void TIM0_Disable(void);

#define TIM0_Enable()	TR0=1
#define TIM0_Disable()	TR0=0

#endif //__TIM4_H__
