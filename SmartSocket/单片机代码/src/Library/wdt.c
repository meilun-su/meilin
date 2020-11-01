#include "main.h"
#include "wdt.h"

void wdt_init(void)
{
//  WDT_CONTR = 0x23;                           //使能看门狗，看门狗溢出时间为0.5s
    //WDT_CONTR = 0x24;                           //使能看门狗，看门狗溢出时间为1s
//  WDT_CONTR = 0x27;                           //使能看门狗，看门狗溢出时间为8s
}
//void wdt_reset(void)
//{
//	WDT_CONTR |= 0x10;                      //清看门狗
//}

