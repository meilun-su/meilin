#include"main.h"
#include"spi.h"

void SPI_Config(void)
{
    SPI0_SETUP &=~(bS0_MODE_SLV | bS0_BIT_ORDER);	/*设置成主机模式*/
    SPI0_CTRL = bS0_SCK_OE | bS0_MOSI_OE;			/*主机写，默认不启动写传输，如果使能bS0_DATA_DIR*/
													/*那么发送数据后自动产生一个字节的时钟，用于快速数据收发*/

	SPI0_CK_SE = 2;									//2分频
}
uint8_t SPI_TXRX_Byte(uint8_t dat)
{
	SPI0_DATA = dat;				//发送数据
	while(S0_FREE == 0);	//等待发送完成
	return SPI0_DATA;
}
