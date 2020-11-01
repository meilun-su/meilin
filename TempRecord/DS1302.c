#include <intrins.h>
#include <REGX52.H>
#include "DEPUTY.H"
sbit SCK=P1^7;		
sbit SDA=P2^3;		
sbit RST = P1^3;
sbit DQ = P1^4;

unsigned char shijian[]={50,59,23,0,0,0,0};
unsigned char set_time[] = {0};

//****************??????:???????*********************//
void Write_Ds1302_Byte(unsigned  char temp) 
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
//DS1302???????
void Write_Ds1302( unsigned char address,unsigned char dat )     
{
 	RST=0;	_nop_();
 	SCK=0;	_nop_();
 	RST=1; 	_nop_();  
 	Write_Ds1302_Byte(address);	
 	Write_Ds1302_Byte((dat/10<<4)|(dat%10));	/////////////////////////////////////////////////	
 	RST=0; 
}
//DS1302???????
unsigned char Read_Ds1302 ( unsigned char address )
{
 	unsigned char i,temp=0x00,dat1,dat2;/////////////////////////////////////
 	RST=0;	_nop_();
 	SCK=0;	_nop_();
 	RST=1;	_nop_();
 	Write_Ds1302_Byte(address);
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
	
	dat1=temp/16;
	dat2=temp%16;
	temp=dat1*10+dat2;/////////////////////////////////////////////////////////////////
	
	return (temp);			
}

void Init_DS(void)
{
	unsigned char n,add;
	add = 0x80;
	Write_Ds1302(0x8E,0x00);
	for(n=0;n<7;n++)
	{
		Write_Ds1302(add,shijian[n]);
		add = add+2;
	}
	Write_Ds1302(0x8E,0x80);
}

void Get_DS(void)
{
	unsigned char n,add;
	add = 0x81;
	Write_Ds1302(0x8E,0x00);
	for(n=0;n<7;n++)
	{
		set_time[n]=Read_Ds1302(add);
		add=add+2;
	}
	Write_Ds1302(0x8E,0x80);
}

