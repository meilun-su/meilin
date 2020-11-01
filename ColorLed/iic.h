#ifndef _IIC_H
#define _IIC_H

void IIC_Start(void); 
void IIC_Stop(void);  
bit IIC_WaitAck(void);  
void IIC_SendAck(bit ackbit); 
void IIC_SendByte(unsigned char byt); 
unsigned char IIC_RecByte(void); 
unsigned char iicread(unsigned char add);
void operate_delay(unsigned char t);
void write_eeprom(unsigned char add,unsigned char val);
unsigned char read_eeprom(unsigned char add);
void Init_pcf8591(void);
unsigned char ADC_pcf8591(void);
#endif