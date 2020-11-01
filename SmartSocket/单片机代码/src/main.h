#ifndef	__MAIN_H__
#define	__MAIN_H__

#define STM8S003	1
#define	STM8S005	2
#define	STM8S103	3
#define	STM8S105	4

#define	STC8A4K32   5
#define	CH547   	6

#define	MCU CH547

#if((MCU==STM8S003)||(MCU==STM8S103))
	#include <iostm8s103.h>
#elif((MCU==STM8S005)||(MCU==STM8S105))
	#include <iostm8s105.h>
#elif(MCU==STC8A4K32)
	#include <STC8.H>
	#include <intrins.h>
	#include <stdlib.h>	//�����������
#elif(MCU==CH547)
	#include "CH547.H"
	#include <intrins.h>
	#include <stdlib.h>	//�����������
	#include "string.h"
#endif	//MCU
#include "stdint.h"
#include "gpio.h"
#include "clksource.h"
#include "tim0.h"
#include "tim1.h"
#include "tim2.h"
//#include "wdt.h"
#include "delay.h"
#include "spi.h"
#include "flashrom.h"
#include "adc.h"
#include "pwm.h"
#include "uart0.h"

#define	enableInterrupts() EA=1
#define	disableInterrupts() EA=0

#define MAIN_CYCTIME	1		//1MS
#define MAINSTATE_CYCTIME (12*MAIN_CYCTIME)

#define IAP_FUNCTION

#define CLOCK_CHIP_ERR 1	//ʱ��ģ��ʶ�����
#define THERMISTOR_ERR 2	//���µ�·����
#define PHOTORESISTOR_ERR 3	//����·����
#define FLASH_CHIP_ERR 4	//�޷�ʶ���FLASHоƬ
#define FLASH_WAV_ERR 5		//WAV�ļ�����
#define MCU_CHECK_ERR 6		//MCU������

extern uint8_t main_TM,main_state,mode;
extern bit switch_mode;

void WaitMainTime(void);

#endif	//__MAIN_H__
