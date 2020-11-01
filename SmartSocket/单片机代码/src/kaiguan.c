#include "main.h"
#include "kaiguan.h"

void switch_init(void)
{
	SetPINQuasiBidirectional(SWITCH_PORT,SWITCH_PIN);
	SET_PIN_H(SWITCH_PORT,SWITCH_PIN);
}

void switch_open(void)
{
	SET_PIN_L(SWITCH_PORT,SWITCH_PIN);
}
void switch_close(void)
{
	SET_PIN_H(SWITCH_PORT,SWITCH_PIN);
}
