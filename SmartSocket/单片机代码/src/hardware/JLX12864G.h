#ifndef __JLX12864G_H__
#define __JLX12864G_H__

#define SCK_PORT	P2
#define SCK_PIN		0
#define SDA_PORT	P2
#define SDA_PIN		1
#define RS_PORT		P2
#define RS_PIN		2
#define RST_PORT	P2
#define RST_PIN		3
#define CS_PORT		P2
#define CS_PIN		4



void LCD_init(void);
void lcd_setAddress(uint8_t page,uint8_t column);
void LCD_clearScreen(void);
void LCD_fullDisplay(uint8_t data_left,uint8_t data_right);
void LCD_displayGraphic8x16(uint8_t page,uint8_t column,uint8_t *dp);
void LCD_displayString8x16(uint8_t page,uint8_t column,uint8_t *text);
void LCD_send_command(uint8_t data1);
void LCD_send_data(uint8_t data1);

void LCD_display8x16(uint8_t page,uint8_t column,uint8_t text);
void LCD_displayMQTTArray8x16(uint8_t page,uint8_t column,uint8_t *text);

#endif //__JLX12864G_H__
