#ifndef	__CLKSOURCE_H__
#define	__CLKSOURCE_H__

#define FOSC              48000000UL	//Ö÷Ê±ÖÓÆµÂÊ
#if (MCU==CH547)
#define FOSC187K5	0
#define FOSC750K	1
#define FOSC3M		2
#define FOSC12M		3
#define FOSC16M		4
#define FOSC24M		5
#define FOSC32M		6
#define FOSC48M		7
#endif //MCU

#if (MCU==STC8A4K32)
#define Clock_Config() {};
#else
void Clock_Config(void);
#endif //MCU

#endif	//__CLKSOURCE_H__
