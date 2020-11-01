#ifndef	__UART0_H__
#define	__UART0_H__

#define	UART0_BAUDRATE	9600UL

#define	UART0_TXD_PORT	P3
#define	UART0_TXD_PIN	1

#define	UART0_RXD_PORT	P3
#define	UART0_RXD_PIN	0

void uart0_Config(void);
void uart0_send_byte(uint8_t dat);
void uart0_send_data(uint8_t *databuf, uint16_t n);
void uart0_send_str(uint8_t *str);

#endif	//__UART0_H__
