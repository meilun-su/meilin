#include "main.h"
#include "adc.h"
#include "delay.h"

/********************************************************************************************************
*  Function: ADC_Config						                                                           
*  Object: ����ADCģ�� ִ�г�ʼ��      
*  ���룺 ��
*  ����� ��								                         	                                     
*  ��ע�� ��                                  
********************************************************************************************************/
void ADC_Config(void)
{
	ADC_CFG |= bADC_EN;					//����ADCģ���Դ
	ADC_CFG = ADC_CFG & ~(bADC_CLK0 | bADC_CLK1);    //ѡ��ADC�ο�ʱ�� 750KHz
	ADC_CFG |= bADC_AIN_EN;                          //�����ⲿͨ��
	if(ADC_DAT);
	ADC_CTRL = bADC_IF;                              //���ADCת����ɱ�־��д1����
}


/********************************************************************************************************
*  Function: ADC_Read_Value
*  Object: ��ȡADC��ֵ
*  ���룺 ͨ��ֵ
*  ����� ADCֵ
*  ��ע�� ��
********************************************************************************************************/
uint16_t ADC_Read_Value(uint8_t channel)
{
	ADC_CHAN = (ADC_CHAN & ~MASK_ADC_CHAN) | channel;
	ADC_CTRL = bADC_START;
	while((ADC_CTRL&bADC_IF) == 0);	//���жϷ�ʽ���ȴ��ɼ����
	ADC_CTRL = bADC_IF;
	return ADC_DAT>>2;	//ת��10λ����
}

