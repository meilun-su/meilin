#include "main.h"
#include "adc.h"
#include "delay.h"

/********************************************************************************************************
*  Function: ADC_Config						                                                           
*  Object: 启动ADC模块 执行初始化      
*  输入： 无
*  输出： 无								                         	                                     
*  备注： 无                                  
********************************************************************************************************/
void ADC_Config(void)
{
	ADC_CFG |= bADC_EN;					//开启ADC模块电源
	ADC_CFG = ADC_CFG & ~(bADC_CLK0 | bADC_CLK1);    //选择ADC参考时钟 750KHz
	ADC_CFG |= bADC_AIN_EN;                          //开启外部通道
	if(ADC_DAT);
	ADC_CTRL = bADC_IF;                              //清除ADC转换完成标志，写1清零
}


/********************************************************************************************************
*  Function: ADC_Read_Value
*  Object: 读取ADC的值
*  输入： 通道值
*  输出： ADC值
*  备注： 无
********************************************************************************************************/
uint16_t ADC_Read_Value(uint8_t channel)
{
	ADC_CHAN = (ADC_CHAN & ~MASK_ADC_CHAN) | channel;
	ADC_CTRL = bADC_START;
	while((ADC_CTRL&bADC_IF) == 0);	//非中断方式，等待采集完成
	ADC_CTRL = bADC_IF;
	return ADC_DAT>>2;	//转成10位数据
}

