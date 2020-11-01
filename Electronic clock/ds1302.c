/*
  ����˵��: DS1302��������
  �������: Keil uVision 4.10 
  Ӳ������: CT107��Ƭ���ۺ�ʵѵƽ̨ 8051��12MHz
  ��    ��: 2011-8-9
*/

#include <STC15F2K60S2.H>
#include <intrins.h>

sbit SCK=P1^7;		
sbit SDA=P2^3;		
sbit RST = P1^3;   // DS1302��λ												

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
 	Write_Ds1302((dat/10 << 4)|dat%10);				//�޸Ĵ�������ʮ������ת��Ϊʮ��������
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
	dat1=temp/16;									//�޸Ĵ�����ȡʮ��������ת��Ϊʮ������
	dat2=temp%16;                 //�޸Ĵ�����ȡʮ��������ת��Ϊʮ������
	temp=dat1*10+dat2;            //�޸Ĵ������Ϊһ����λ����ʮ����
	return (temp);
}

//=======================������������=======================
void DS1302_Init(void)
{
		unsigned char i,add;
		add = 0x80;
		Write_Ds1302_Byte(0x8e,0x00);										//�������ڴ�д������
		for(i=0; i<7; i++)															//д��7�ֽڵ�ʱ�����
		{
				Write_Ds1302_Byte(add,shijian[i]);
				add = add + 2;
		}
		Write_Ds1302_Byte(0x8e,0x80);										//��ֹ���ڴ�д������
}

//=======================������ȡ����=======================
void DS1302_get(void)
{
		unsigned char i,add;
		add = 0x81;
		Write_Ds1302_Byte(0x8e,0x00);
		for(i=0; i<7; i++)															//��ȡ7���ֽڵ�ʵʱʱ��
		{
				shijian[i]=Read_Ds1302_Byte(add);
				add = add + 2;
		}
		Write_Ds1302_Byte(0x8e,0x80);
}

//=====================����ʱ����ͣ==========================
void DS1302_tingzhi(void)
{
		Write_Ds1302_Byte(0x8e,0x00);										//�������ڴ�д������

		Write_Ds1302_Byte(0x80,shijian[0]&0x7f);				//����λ��λΪ0,ֹͣʱ�Ӽ�ʱ
	
		Write_Ds1302_Byte(0x8e,0x80);										//��ֹ���ڴ�д������
}