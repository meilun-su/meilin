#ifndef	__GPIO_H__
#define	__GPIO_H__

#define	SET_PIN_H_(P,B) P ## B=1
#define	SET_PIN_L_(P,B) P ## B=0
#define	SET_PIN_TOGGLE_(P,B) P ## B^=1
	
#define	SET_PIN_H(P,B) SET_PIN_H_(P,B)
#define	SET_PIN_L(P,B) SET_PIN_L_(P,B)
#define	SET_PIN_TOGGLE(P,B) SET_PIN_TOGGLE_(P,B)


#define	READ_PIN_(P,B) (P ## B)
#define	READ_PIN(P,B) READ_PIN_(P,B)
#define	IO_PIN(P,B) READ_PIN_(P,B)

#define	READ_PORT_(P) (P)
#define	READ_PORT(P) READ_PORT_(P)

#define		SET_PIN_PX_MOD_OC_(P,B) P ## _MOD_OC|=(1<<B)
#define		CLR_PIN_PX_MOD_OC_(P,B) P ## _MOD_OC&=~(1<<B)

#define		SET_PIN_PX_DIR_PU_(P,B) P ## _DIR_PU|=(1<<B)
#define		CLR_PIN_PX_DIR_PU_(P,B) P ## _DIR_PU&=~(1<<B)

//#define EnablePullupResistor_(P,B) P ## PU |= (1<<B)
//#define EnablePullupResistor(P,B) EnablePullupResistor_(P,B)
//#define DisablePullupResistor_(P,B) P ## PU &= ~(1<<B)
//#define DisablePullupResistor(P,B) DisablePullupResistor_(P,B)

//push-pull
#define	SetPINOutPushPull(P,B)	SET_PIN_PX_DIR_PU_(P,B);	\
								CLR_PIN_PX_MOD_OC_(P,B)

//Open Drain
#define	SetPINOutOpenDrain(P,B)		CLR_PIN_PX_DIR_PU_(P,B);	\
								SET_PIN_PX_MOD_OC_(P,B)

//Floating
#define	SetPINInFloating(P,B)		CLR_PIN_PX_DIR_PU_(P,B);	\
								CLR_PIN_PX_MOD_OC_(P,B)

//Quasi-bidirectional
#define	SetPINQuasiBidirectional(P,B)		SET_PIN_PX_DIR_PU_(P,B);	\
								SET_PIN_PX_MOD_OC_(P,B)

#define SetPORT(P,D)		P = D
#define Set_PX_MOD_OC(P,D)		P ## _MOD_OC = D
#define Set_PX_DIR_PU(P,D)		P ## _DIR_PU = D

#endif	//__GPIO_H__
