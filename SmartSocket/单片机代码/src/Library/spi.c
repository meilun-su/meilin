#include"main.h"
#include"spi.h"

void SPI_Config(void)
{
    SPI0_SETUP &=~(bS0_MODE_SLV | bS0_BIT_ORDER);	/*���ó�����ģʽ*/
    SPI0_CTRL = bS0_SCK_OE | bS0_MOSI_OE;			/*����д��Ĭ�ϲ�����д���䣬���ʹ��bS0_DATA_DIR*/
													/*��ô�������ݺ��Զ�����һ���ֽڵ�ʱ�ӣ����ڿ��������շ�*/

	SPI0_CK_SE = 2;									//2��Ƶ
}
uint8_t SPI_TXRX_Byte(uint8_t dat)
{
	SPI0_DATA = dat;				//��������
	while(S0_FREE == 0);	//�ȴ��������
	return SPI0_DATA;
}
