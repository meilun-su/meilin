/*
  程序说明: DS1302驱动程序
  软件环境: Keil uVision 4.10 
  硬件环境: CT107单片机综合实训平台 8051，12MHz
  日    期: 2011-8-9
*/

#include <STC15F2K60S2.H>
#include <intrins.h>

sbit SCK=P1^7;		
sbit SDA=P2^3;		
sbit RST = P1^3;   // DS1302复位												

unsigned char shijian[] = {50,59,23,0,0,0,0};

void Write_Ds1302(unsigned  char temp) 
{
	unsigned char i;
	for (i=0;i<8;i++)     	
	{ 
		SCK=0;
		SDA=temp&0x01;
		temp>>=1; 
		SCK=1;
	}
}   

void Write_Ds1302_Byte( unsigned char address,unsigned char dat )     
{
 	RST=0;	_nop_();
 	SCK=0;	_nop_();
 	RST=1; 	_nop_();  
 	Write_Ds1302(address);	
 	Write_Ds1302((dat/10 << 4)|dat%10);				//修改处，输入十进制数转化为十六进制数
 	RST=0; 
}

unsigned char Read_Ds1302_Byte ( unsigned char address )
{
 	unsigned char i,temp=0x00,dat1,dat2;
 	RST=0;	_nop_();
 	SCK=0;	_nop_();
 	RST=1;	_nop_();
 	Write_Ds1302(address);
 	for (i=0;i<8;i++) 	
 	{		
		SCK=0;
		temp>>=1;	
 		if(SDA)
 		temp|=0x80;	
 		SCK=1;
	} 
 	RST=0;	_nop_();
 	SCK=0;	_nop_();
	SCK=1;	_nop_();
	SDA=0;	_nop_();
	SDA=1;	_nop_();
	dat1=temp/16;									//修改处，读取十六进制数转化为十进制数
	dat2=temp%16;                 //修改处，读取十六进制数转化为十进制数
	temp=dat1*10+dat2;            //修改处，组合为一个两位数的十进制
	return (temp);
}

//=======================日历参数配置=======================
void DS1302_Init(void)
{
		unsigned char i,add;
		add = 0x80;
		Write_Ds1302_Byte(0x8e,0x00);										//允许向内存写入数据
		for(i=0; i<7; i++)															//写入7字节的时间参数
		{
				Write_Ds1302_Byte(add,shijian[i]);
				add = add + 2;
		}
		Write_Ds1302_Byte(0x8e,0x80);										//禁止向内存写入数据
}

//=======================日历读取函数=======================
void DS1302_get(void)
{
		unsigned char i,add;
		add = 0x81;
		Write_Ds1302_Byte(0x8e,0x00);
		for(i=0; i<7; i++)															//读取7个字节的实时时间
		{
				shijian[i]=Read_Ds1302_Byte(add);
				add = add + 2;
		}
		Write_Ds1302_Byte(0x8e,0x80);
}

//=====================设置时钟暂停==========================
void DS1302_tingzhi(void)
{
		Write_Ds1302_Byte(0x8e,0x00);										//允许向内存写入数据

		Write_Ds1302_Byte(0x80,shijian[0]&0x7f);				//置秒位高位为0,停止时钟计时
	
		Write_Ds1302_Byte(0x8e,0x80);										//禁止向内存写入数据
}