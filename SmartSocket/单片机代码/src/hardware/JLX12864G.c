#include "main.h"
#include "JLX12864G.h"
#include "ASCII8x16.h"

//===========================================================
//		 LCD初始化
//===========================================================
void LCD_init(void)
{
	//IO口初始化
	SetPINQuasiBidirectional(SCK_PORT,SCK_PIN);
	SetPINQuasiBidirectional(SDA_PORT,SDA_PIN);
	SetPINQuasiBidirectional(RS_PORT,RS_PIN);
	SetPINQuasiBidirectional(RST_PORT,RST_PIN);
	SetPINQuasiBidirectional(CS_PORT,CS_PIN);
	
	SET_PIN_L(RST_PORT,RST_PIN);//reset=0;	//低电平复位 
	Delay_ms(100);
	SET_PIN_H(RST_PORT,RST_PIN);//reset=1;	//复位完毕 
	Delay_ms(100);
	LCD_send_command(0xe2);	//软复位
	Delay_ms(5);
	LCD_send_command(0x2c); //升压步骤 1
	Delay_ms(5);
	LCD_send_command(0x2e); //升压步骤 2
	Delay_ms(5);
	LCD_send_command(0x2f); //升压步骤 3
	Delay_ms(5);
	LCD_send_command(0x24); //粗调对比度，可调范围 0x20~0x27
	LCD_send_command(0x81); //微调对比度
	LCD_send_command(0x2a); //0x1a,微调对比度的值，可设置范围 0x00~0x3f
	LCD_send_command(0xa2); //1/9偏压比(bias)
	LCD_send_command(0xc8); //行扫描顺序：从上到下
	LCD_send_command(0xa0); //列扫描顺序：从左到右 
	LCD_send_command(0x40); //开始行：第一行开始 
	LCD_send_command(0xaf); //开显示
}
//===========================================================
//		 设置显示地址
//===========================================================
void lcd_setAddress(uint8_t page,uint8_t column)
{
	//column=column-1;	
	//page = page-1;
	LCD_send_command(0xb0+page);	//设置页地址，从0开始
	LCD_send_command(((column>>4)&0x0f)+0x10);	//设置列地址，从0开始
	LCD_send_command(column&0x0f);		//设置地址的低4位
}
//===========================================================
//		 全屏清屏
//===========================================================
void LCD_clearScreen(void)
{
	unsigned char i,j; 
	for(i=0;i<8;i++)
	{
		lcd_setAddress(i,0);

		for(j=0;j<132;j++)
		{
			LCD_send_data(0x00);
		}
	}
}
////===========================================================
////		 显示128*64的内容
////===========================================================
//void LCD_fullDisplay(uint8_t data_left,uint8_t data_right)
//{
//	uint8_t i,j; 
//	for(i=0;i<8;i++)
//	{
//		SET_PIN_L(CS_PORT,CS_PIN);//cs1=0; 
//		lcd_setAddress(i,0); 
//		for(j=0;j<64;j++)
//		{
//			LCD_send_data(data_left); 
//			LCD_send_data(data_right);
//		}
//	}
//}
////===========================================================
////显示 8x16 点阵图像、ASCII, 或 8x16 点阵的自造字符、其他图标
////===========================================================
//void LCD_displayGraphic8x16(uint8_t page,uint8_t column,uint8_t *dp)
//{
//	uint8_t i,j; 
//	for(j=0;j<2;j++)
//	{
//		lcd_setAddress(page+j,column); 
//		for (i=0;i<8;i++)
//		{
//			LCD_send_data(*dp);	//写数据到LCD,每写完一个8位的数据后地址自动加1
//			dp++;
//		}
//	}
//}
//===========================================================
//显示 8x16 ASCII的字符
//===========================================================
void LCD_display8x16(uint8_t page,uint8_t column,uint8_t text)
{
	uint8_t i=0,j,k,n; 

		if((text>=0x20)&&(text<=0x7e))
		{
			j=text-0x20; 
			for(n=0;n<2;n++)
			{
				lcd_setAddress(page+n,column); 
				for(k=0;k<8;k++)
				{
					LCD_send_data(ascii_table_8x16[j][k+8*n]);//查表读数据
				}
			}
			//column+=8;
		}
}
//===========================================================
//显示 8x16 ASCII的字符串
//===========================================================
void LCD_displayString8x16(uint8_t page,uint8_t column,uint8_t *text)
{
	uint8_t i=0,j,k,n,m=page; 
	while(text[i]>0x00)
	{
		if((text[i]>=0x20)&&(text[i]<=0x7e))
		{
			j=text[i]-0x20; 
			for(n=0;n<2;n++)
			{
				lcd_setAddress(page+n,column); 
				for(k=0;k<8;k++)
				{
					LCD_send_data(ascii_table_8x16[j][k+8*n]);//查表读数据
				}
			} 
			i++;
			column+=8;
			if(column>=128)
			{
				column=0;
				
				page+=2;	
				if(page>=8)					//写到最后一行后直接退出显示
				{
					page=m;
				}
			}
		}
		else i++;
	}
}
//===========================================================
//显示 8x16 ASCII的数据
//===========================================================
void LCD_displayMQTTArray8x16(uint8_t page,uint8_t column,uint8_t *text)
{
	uint8_t i,j,k,n,m=page; 
	for(i=0;i<(text[1]+1);i++)
	{
		if((text[i]<=0x20)||(text[i]>=0x7e))
		{
			text[i]='.';
		}
		
		//if((text[i]>=0x20)&&(text[i]<=0x7e))
		{
			j=text[i]-0x20; 
			for(n=0;n<2;n++)
			{
				lcd_setAddress(page+n,column); 
				for(k=0;k<8;k++)
				{
					LCD_send_data(ascii_table_8x16[j][k+8*n]);//查表读数据
				}
			}
			//i++;
			column+=8;
			if(column>=128)
			{
				column=0;
				
				page+=2;	
				if(page>=8)					//写到最后一行后直接退出显示
				{
					page=m;
				}
			}
		}
		//else i++;
	}
}
//===========================================================
//		 发送命令给LCD
//===========================================================
void LCD_send_command(uint8_t data1)
{
	uint8_t i; 
	SET_PIN_L(CS_PORT,CS_PIN);//cs=0; 
	SET_PIN_L(RS_PORT,RS_PIN);//rs=0;
	for(i=0;i<8;i++)
	{
		SET_PIN_L(SCK_PORT,SCK_PIN);//sclk=0; 
		Delay_us(2);
		if(data1&0x80) SET_PIN_H(SDA_PORT,SDA_PIN);//sid=1;
		else SET_PIN_L(SDA_PORT,SDA_PIN);//sid=0; 
		SET_PIN_H(SCK_PORT,SCK_PIN);//sclk=1; 
		Delay_us(2); 
		data1=data1<<=1;
	}
	SET_PIN_H(CS_PORT,CS_PIN);//cs1=1;
}

//===========================================================
//		 发送数据给LCD
//===========================================================
void LCD_send_data(uint8_t data1)
{
	uint8_t i; 
	SET_PIN_L(CS_PORT,CS_PIN);//cs1=0; 
	SET_PIN_H(RS_PORT,RS_PIN);//rs=1;
	for(i=0;i<8;i++)
	{
		SET_PIN_L(SCK_PORT,SCK_PIN);//sclk=0; 
		Delay_us(1);
		if(data1&0x80) SET_PIN_H(SDA_PORT,SDA_PIN);//sid=1; 
		else SET_PIN_L(SDA_PORT,SDA_PIN);//sid=0;
		SET_PIN_H(SCK_PORT,SCK_PIN);//sclk=1; 
		Delay_us(1);                        
		data1<<=1;
	}
	SET_PIN_H(CS_PORT,CS_PIN);//cs1=1;
}
