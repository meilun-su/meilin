/*
  ����˵��: IIC������������
  �������: Keil uVision 4.10 
  Ӳ������: CT107��Ƭ���ۺ�ʵѵƽ̨ 8051��12MHz
  ��    ��: 2011-8-9
*/

#include "reg52.h"
#include "intrins.h"

#define DELAY_TIME 5

#define SlaveAddrW 0xA0
#define SlaveAddrR 0xA1

//�������Ŷ���
sbit SDA = P2^1;  /* ������ */
sbit SCL = P2^0;  /* ʱ���� */

void IIC_Delay(unsigned char i)
{
    do{_nop_();}
    while(i--);        
}
//������������
void IIC_Start(void)
{
    SDA = 1;
    SCL = 1;
    IIC_Delay(DELAY_TIME);
    SDA = 0;
    IIC_Delay(DELAY_TIME);
    SCL = 0;	
}

//����ֹͣ����
void IIC_Stop(void)
{
    SDA = 0;
    SCL = 1;
    IIC_Delay(DELAY_TIME);
    SDA = 1;
    IIC_Delay(DELAY_TIME);
}

//����Ӧ��
void IIC_SendAck(bit ackbit)
{
    SCL = 0;
    SDA = ackbit;  					// 0��Ӧ��1����Ӧ��
    IIC_Delay(DELAY_TIME);
    SCL = 1;
    IIC_Delay(DELAY_TIME);
    SCL = 0; 
    SDA = 1;
    IIC_Delay(DELAY_TIME);
}

//�ȴ�Ӧ��
bit IIC_WaitAck(void)
{
    bit ackbit;
	
    SCL  = 1;
    IIC_Delay(DELAY_TIME);
    ackbit = SDA;
    SCL = 0;
    IIC_Delay(DELAY_TIME);
    return ackbit;
}

//ͨ��I2C���߷�������
void IIC_SendByte(unsigned char byt)
{
    unsigned char i;

    for(i=0; i<8; i++)
    {
        SCL  = 0;
        IIC_Delay(DELAY_TIME);
        if(byt & 0x80) SDA  = 1;
        else SDA  = 0;
        IIC_Delay(DELAY_TIME);
        SCL = 1;
        byt <<= 1;
        IIC_Delay(DELAY_TIME);
    }
    SCL  = 0;  
}

//��I2C�����Ͻ�������
unsigned char IIC_RecByte(void)
{
    unsigned char i, da;
    for(i=0; i<8; i++)
    {   
    	SCL = 1;
	IIC_Delay(DELAY_TIME);
	da <<= 1;
	if(SDA) da |= 1;
	SCL = 0;
	IIC_Delay(DELAY_TIME);
    }
    return da;    
}

//��д������Ҫ����ʱ����
void operate_delay(unsigned char t)
{
		unsigned char i;
	
		while(t--){
			for(i=0;i<112;i++);
		}
}
//========================EEPROM AT24C02��������==========================
//EEPROM AT24c02д��������
void write_eeprom(unsigned char add,unsigned char val)
{
	IIC_Start();
	IIC_SendByte(0xa0);
	IIC_WaitAck();
	IIC_SendByte(add);
	IIC_WaitAck();
	IIC_SendByte(val);
	IIC_WaitAck();
	IIC_Stop();
	operate_delay(20);
}

//EEPROM AT24c02����������
unsigned char read_eeprom(unsigned char add)
{
	unsigned char date;
	
	IIC_Start();
	IIC_SendByte(0xa0);
	IIC_WaitAck();
	IIC_SendByte(add);
	IIC_WaitAck();
	IIC_Start();
	IIC_SendByte(0xa1);
	IIC_WaitAck();
	date = IIC_RecByte();
	IIC_SendAck(0);
	IIC_Stop();
	
	return date;
}

//=================ADCоƬPCF8591��������===========
//PCF8591��ʼ������
void Init_pcf8591(void)
{
	IIC_Start();
	IIC_SendByte(0x90);
	IIC_WaitAck();
	IIC_SendByte(0x03);		//ADCͨ��3
	IIC_WaitAck();
	IIC_Stop();
	operate_delay(10);
}

//ͨ��I2C���߶�ȡADC���
unsigned char ADC_pcf8591(void)
{
	unsigned char temp;
	
	EA = 0;
	IIC_Start();
	IIC_SendByte(0x91);
	IIC_WaitAck();
	temp = IIC_RecByte();
	IIC_SendAck(1);
	IIC_Stop();
	EA = 1;
		
	return temp;
}