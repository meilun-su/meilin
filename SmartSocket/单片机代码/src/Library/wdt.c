#include "main.h"
#include "wdt.h"

void wdt_init(void)
{
//  WDT_CONTR = 0x23;                           //ʹ�ܿ��Ź������Ź����ʱ��Ϊ0.5s
    //WDT_CONTR = 0x24;                           //ʹ�ܿ��Ź������Ź����ʱ��Ϊ1s
//  WDT_CONTR = 0x27;                           //ʹ�ܿ��Ź������Ź����ʱ��Ϊ8s
}
//void wdt_reset(void)
//{
//	WDT_CONTR |= 0x10;                      //�忴�Ź�
//}

