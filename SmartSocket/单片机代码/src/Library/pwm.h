#ifndef __PCA_PWM_H__
#define __PCA_PWM_H__

//#define Pcapwm_Enable() CR=1
//#define Pcapwm_Disable() CR=0
#define pwm_Updat(D) PWM_DATA0 = D

void pwm_Config(void);
void pwm_Enable(void);
void pwm_Disable(void);

#endif //__PCA_PWM_H__
