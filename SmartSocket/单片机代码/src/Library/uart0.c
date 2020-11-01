#include "main.h"
#include "uart0.h"

//===========================================================
//		配置UART0 
//	使用定时器1做波特率发生器
//===========================================================
void uart0_Config(void)
{
	SCON = 0x50;		//8位数据,可变波特率
	PCON |= SMOD;
	T2MOD |= bT1_CLK;
	TMOD &= 0x0F;
	TMOD |= 0x20;		//设定定时器1为8位自动重装方式
	TL1 = TH1 = (256-FOSC/4/16/UART0_BAUDRATE);			//设定定时初值
	ET1 = 0;			//禁止定时器1中断
	TR1 = 1;			//启动定时器1
	ES = 1;				//不使能串口中断
    
    //TXD和RXD设为准双向口
    SetPINQuasiBidirectional(UART0_TXD_PORT,UART0_TXD_PIN);
	//EnablePullupResistor(UART0_TXD_PORT,UART0_TXD_PIN);
    SetPINQuasiBidirectional(UART0_RXD_PORT,UART0_RXD_PIN);
	//EnablePullupResistor(UART0_RXD_PORT,UART0_RXD_PIN);
	
}
//===========================================================
//		配置UART1 发送数据
//	使用定时器1做波特率发生器
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
///*函数名：串口0发送缓冲区中的数据                  */
///*参  数：data：数据                               */
///*返回值：无                                       */
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
//Uart1_ISR 中断服务程序
//-----------------------------*/
//void Uart1_ISR() interrupt 4
//{
//    if (RI)
//    {
//        RI = 0;                 //清除RI位
//        //P0 = SBUF;              //P0??????
//        //P22 = RB8;              //P2.2?????
//    }
//    if (TI)
//    {
//        TI = 0;                 //清除TI位
//    }
//}

