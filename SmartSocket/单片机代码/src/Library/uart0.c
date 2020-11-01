#include "main.h"
#include "uart0.h"

//===========================================================
//		����UART0 
//	ʹ�ö�ʱ��1�������ʷ�����
//===========================================================
void uart0_Config(void)
{
	SCON = 0x50;		//8λ����,�ɱ䲨����
	PCON |= SMOD;
	T2MOD |= bT1_CLK;
	TMOD &= 0x0F;
	TMOD |= 0x20;		//�趨��ʱ��1Ϊ8λ�Զ���װ��ʽ
	TL1 = TH1 = (256-FOSC/4/16/UART0_BAUDRATE);			//�趨��ʱ��ֵ
	ET1 = 0;			//��ֹ��ʱ��1�ж�
	TR1 = 1;			//������ʱ��1
	ES = 1;				//��ʹ�ܴ����ж�
    
    //TXD��RXD��Ϊ׼˫���
    SetPINQuasiBidirectional(UART0_TXD_PORT,UART0_TXD_PIN);
	//EnablePullupResistor(UART0_TXD_PORT,UART0_TXD_PIN);
    SetPINQuasiBidirectional(UART0_RXD_PORT,UART0_RXD_PIN);
	//EnablePullupResistor(UART0_RXD_PORT,UART0_RXD_PIN);
	
}
//===========================================================
//		����UART1 ��������
//	ʹ�ö�ʱ��1�������ʷ�����
//===========================================================
void uart0_send_byte(uint8_t dat)
{
	TI = 0;
	SBUF = dat;
	while(TI==0);
}
void uart0_send_data(uint8_t *databuf, uint16_t n)
{
	while(n--)
	{
		uart0_send_byte(*databuf);
		databuf++;
	}
}

///*-------------------------------------------------*/
///*������������0���ͻ������е�����                  */
///*��  ����data������                               */
///*����ֵ����                                       */
///*-------------------------------------------------*/
//void uart0_TxData(unsigned char *dataTem)
//{
//	uint8_t i;
//	for(i = 1;i <= dataTem[0];i ++){			
//		uart0_send_byte(dataTem[1]);
//	}
//}

void uart0_send_str(uint8_t *str)
{
	ES = 0;
	TI = 0;
	while(*str)
	{
		uart0_send_byte(*str);
		str++;
	}
	ES = 1;
}
///*----------------------------
//Uart1_ISR �жϷ������
//-----------------------------*/
//void Uart1_ISR() interrupt 4
//{
//    if (RI)
//    {
//        RI = 0;                 //���RIλ
//        //P0 = SBUF;              //P0??????
//        //P22 = RB8;              //P2.2?????
//    }
//    if (TI)
//    {
//        TI = 0;                 //���TIλ
//    }
//}

