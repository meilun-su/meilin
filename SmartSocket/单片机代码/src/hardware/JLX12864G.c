#include "main.h"
#include "JLX12864G.h"
#include "ASCII8x16.h"

//===========================================================
//		 LCD��ʼ��
//===========================================================
void LCD_init(void)
{
	//IO�ڳ�ʼ��
	SetPINQuasiBidirectional(SCK_PORT,SCK_PIN);
	SetPINQuasiBidirectional(SDA_PORT,SDA_PIN);
	SetPINQuasiBidirectional(RS_PORT,RS_PIN);
	SetPINQuasiBidirectional(RST_PORT,RST_PIN);
	SetPINQuasiBidirectional(CS_PORT,CS_PIN);
	
	SET_PIN_L(RST_PORT,RST_PIN);//reset=0;	//�͵�ƽ��λ 
	Delay_ms(100);
	SET_PIN_H(RST_PORT,RST_PIN);//reset=1;	//��λ��� 
	Delay_ms(100);
	LCD_send_command(0xe2);	//��λ
	Delay_ms(5);
	LCD_send_command(0x2c); //��ѹ���� 1
	Delay_ms(5);
	LCD_send_command(0x2e); //��ѹ���� 2
	Delay_ms(5);
	LCD_send_command(0x2f); //��ѹ���� 3
	Delay_ms(5);
	LCD_send_command(0x24); //�ֵ��Աȶȣ��ɵ���Χ 0x20~0x27
	LCD_send_command(0x81); //΢���Աȶ�
	LCD_send_command(0x2a); //0x1a,΢���Աȶȵ�ֵ�������÷�Χ 0x00~0x3f
	LCD_send_command(0xa2); //1/9ƫѹ��(bias)
	LCD_send_command(0xc8); //��ɨ��˳�򣺴��ϵ���
	LCD_send_command(0xa0); //��ɨ��˳�򣺴����� 
	LCD_send_command(0x40); //��ʼ�У���һ�п�ʼ 
	LCD_send_command(0xaf); //����ʾ
}
//===========================================================
//		 ������ʾ��ַ
//===========================================================
void lcd_setAddress(uint8_t page,uint8_t column)
{
	//column=column-1;	
	//page = page-1;
	LCD_send_command(0xb0+page);	//����ҳ��ַ����0��ʼ
	LCD_send_command(((column>>4)&0x0f)+0x10);	//�����е�ַ����0��ʼ
	LCD_send_command(column&0x0f);		//���õ�ַ�ĵ�4λ
}
//===========================================================
//		 ȫ������
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
////		 ��ʾ128*64������
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
////��ʾ 8x16 ����ͼ��ASCII, �� 8x16 ����������ַ�������ͼ��
////===========================================================
//void LCD_displayGraphic8x16(uint8_t page,uint8_t column,uint8_t *dp)
//{
//	uint8_t i,j; 
//	for(j=0;j<2;j++)
//	{
//		lcd_setAddress(page+j,column); 
//		for (i=0;i<8;i++)
//		{
//			LCD_send_data(*dp);	//д���ݵ�LCD,ÿд��һ��8λ�����ݺ��ַ�Զ���1
//			dp++;
//		}
//	}
//}
//===========================================================
//��ʾ 8x16 ASCII���ַ�
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
					LCD_send_data(ascii_table_8x16[j][k+8*n]);//��������
				}
			}
			//column+=8;
		}
}
//===========================================================
//��ʾ 8x16 ASCII���ַ���
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
					LCD_send_data(ascii_table_8x16[j][k+8*n]);//��������
				}
			} 
			i++;
			column+=8;
			if(column>=128)
			{
				column=0;
				
				page+=2;	
				if(page>=8)					//д�����һ�к�ֱ���˳���ʾ
				{
					page=m;
				}
			}
		}
		else i++;
	}
}
//===========================================================
//��ʾ 8x16 ASCII������
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
					LCD_send_data(ascii_table_8x16[j][k+8*n]);//��������
				}
			}
			//i++;
			column+=8;
			if(column>=128)
			{
				column=0;
				
				page+=2;	
				if(page>=8)					//д�����һ�к�ֱ���˳���ʾ
				{
					page=m;
				}
			}
		}
		//else i++;
	}
}
//===========================================================
//		 ���������LCD
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
//		 �������ݸ�LCD
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
