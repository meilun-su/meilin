#include "main.h"
#include "guang.h"

uint16_t guangdu;

void readGuang(void)
{
	
	guangdu=ADC_Read_Value(1);
}
