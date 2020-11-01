#include <STC15F2K60S2.H>
#include "intrins.h"

#define uchar unsigned char
#define uint unsigned int

#define Y4 P2 = P2 & 0x1f | 0x80
#define Y5 P2 = P2 & 0x1f | 0xa0
#define Y6 P2 = P2 & 0x1f | 0xc0
#define Y7 P2 = P2 & 0x1f | 0xe0

#define somenop Delay5us()

//总线引脚定义
sbit SDA = P2^1;  /* 数据线 */
sbit SCL = P2^0;  /* 时钟线 */
uchar code Duanma[]={0XC0,0XF9,0XA4,0XB0,0X99,0X92,0X82,0XF8,0X80,0X90,0XBF,0XFF};
uchar code Duanma_Dot[]={0x40,0x79,0x24,0x30,0x19,0x12,0x02,0xff};
uchar SMG1,SMG2,SMG3,SMG4,SMG5,SMG6,SMG7,SMG8;
uchar Data,T,price;
uint ML;
void Delay1ms()		//@11.0592MHz
{
	unsigned char i, j;

	_nop_();
	_nop_();
	_nop_();
	i = 11;
	j = 190;
	do
	{
		while (--j);
	} while (--i);
}

void Delay5us()		//@11.0592MHz
{
	unsigned char i;

	_nop_();
	i = 11;
	while (--i);
}

void Delay100ms()		//@11.0592MHz
{
	unsigned char i, j, k;

	_nop_();
	_nop_();
	i = 5;
	j = 52;
	k = 195;
	do
	{
		do
		{
			while (--k);
		} while (--j);
	} while (--i);
}

void SysInit()
{
 	Y4; P0 = 0xff;
	Y5; P0 = 0x00;
}
void SMG(uchar SMG1,SMG2,SMG3,SMG4,SMG5,SMG6,SMG7,SMG8)
{
	Y6; P0 = 0x01;
	Y7; P0 = Duanma[SMG1];
	Delay1ms();

 	Y6; P0 = 0x02;
	Y7; P0 = Duanma_Dot[SMG2];
	Delay1ms();

	Y6; P0 = 0x04;
	Y7; P0 = Duanma[SMG3];
	Delay1ms();

 	Y6; P0 = 0x08;
	Y7; P0 = Duanma[SMG4];
	Delay1ms();

	Y6; P0 = 0x10;
	Y7; P0 = Duanma[SMG5];
	Delay1ms();

 	Y6; P0 = 0x20;
	Y7; P0 = Duanma_Dot[SMG6];
	Delay1ms();

	Y6; P0 = 0x40;
	Y7; P0 = Duanma[SMG7];
	Delay1ms();

 	Y6; P0 = 0x80;
	Y7; P0 = Duanma[SMG8];
	Delay1ms();
}

void key()
{
 	if(P30 == 0)
	{
	 	Delay100ms();
		if(P30 == 0)
		{
		  Y5; P0 = 0x10;
		  ET0 = 1;
		  EA = 1;
		  ML = 0;	
		}
		while(!30);	
	}
	else if(P31 == 0)
	{
	 	Delay100ms();
		if(P31 == 0)
		{
		  Y5; P0 = 0x00;
		  ET0 = 0;
		  EA = 0;
		  
		  price = ML * 0.5;
		  SMG5 = price/1000; SMG6 = (price%1000)/100; SMG7 = (price%100)/10; SMG8 = price%10;	
		}
		while(!31);	
	}
}


//总线启动条件
void IIC_Start(void)
{
	SDA = 1;
	SCL = 1;
	somenop;
	SDA = 0;
	somenop;
	SCL = 0;	
}

//总线停止条件
void IIC_Stop(void)
{
	SDA = 0;
	SCL = 1;
	somenop;
	SDA = 1;
}

//应答位控制
void IIC_Ack(unsigned char ackbit)
{
	if(ackbit) 
	{	
		SDA = 0;
	}
	else 
	{
		SDA = 1;
	}
	somenop;
	SCL = 1;
	somenop;
	SCL = 0;
	SDA = 1; 
	somenop;
}

//等待应答
bit IIC_WaitAck(void)
{
	SDA = 1;
	somenop;
	SCL = 1;
	somenop;
	if(SDA)    
	{   
		SCL = 0;
		IIC_Stop();
		return 0;
	}
	else  
	{ 
		SCL = 0;
		return 1;
	}
}

//通过I2C总线发送数据
void IIC_SendByte(unsigned char byt)
{
	unsigned char i;
	for(i=0;i<8;i++)
	{   
		if(byt&0x80) 
		{	
			SDA = 1;
		}
		else 
		{
			SDA = 0;
		}
		somenop;
		SCL = 1;
		byt <<= 1;
		somenop;
		SCL = 0;
	}
}

//从I2C总线上接收数据
unsigned char IIC_RecByte(void)
{
	unsigned char da;
	unsigned char i;
	
	for(i=0;i<8;i++)
	{   
		SCL = 1;
		somenop;
		da <<= 1;
		if(SDA) 
		da |= 0x01;
		SCL = 0;
		somenop;
	}
	return da;
}

uchar ADC(uchar address)
{
     uchar Data;

 	 IIC_Start();
	 IIC_SendByte(0x90);
	 IIC_WaitAck();
	 IIC_SendByte(address);
	 IIC_WaitAck();
	 IIC_Stop();

	 IIC_Start();
	 IIC_SendByte(0x91);
	 IIC_WaitAck();
	 Data = IIC_RecByte();
	 IIC_Ack(0);
	 IIC_Stop();

	 return Data;
}

void Timer0Init(void)		//5毫秒@11.0592MHz
{
	AUXR |= 0x80;		//定时器时钟1T模式
	TMOD &= 0xF0;		//设置定时器模式
	TL0 = 0x00;		//设置定时初值
	TH0 = 0x28;		//设置定时初值
	TF0 = 0;		//清除TF0标志
	TR0 = 1;		//定时器0开始计时
}

void Timer0Service() interrupt 1
{
   T++;
   if(T == 20)
   {
		T = 0;
		ML = ML + 1;
		SMG5 = ML/1000; SMG6 = (ML%1000)/100; SMG7 = (ML%100)/10; SMG8 = ML%10;
		if(ML>9999)
		{
			  ET0 = 0;
			  EA = 0;
			  Y5; P0 = 0x00;
			  price = ML * 0.5;
			  SMG5 = price/1000; SMG6 = (price%1000)/100; SMG7 = (price%100)/10; SMG8 = price%10;
		}
   }
}
void main()
{
	SysInit();
	Timer0Init();
	SMG1 = 11; SMG2 = 0; SMG3 = 5; SMG4 = 0;
	SMG5 = 0; SMG6 = 0; SMG7 = 0;	SMG8 = 0;
 	while(1)
	{
		key();
		Data = ADC(1);
		if(Data/51 > 1.25) 
		{
		 	Y4; P0 = 0xfe;
		}
		else
		{
		 	Y4; P0 = 0xff;
		}
	    SMG(SMG1,SMG2,SMG3,SMG4,SMG5,SMG6,SMG7,SMG8);	
	}

}

