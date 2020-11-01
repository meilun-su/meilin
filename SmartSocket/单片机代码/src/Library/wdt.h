#ifndef	__WDT_H__
#define	__WDT_H__

void wdt_init(void);

//void wdt_reset(void);
#define	wdt_reset() WDOG_COUNT = 0x00

#endif	//__WDT_H__
