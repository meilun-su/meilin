/********************************** (C) COPYRIGHT *******************************
* File Name          : DataFlash.C
* Author             : WCH
* Version            : V1.0
* Date               : 2018/08/09
* Description        : CH549 DataFlash字节读写函数定义
*******************************************************************************/
#include "main.h"
#include "flashrom.h"

bit e_all;
uint8_t status;                                    /* 返回操作状态 */
uint8_t FlashType;                                 /* Flash 类型标志 */

/*******************************************************************************
* Function Name  : flashErase(uint16_t addr, uint16_t len)
* Description    : 用于页擦除，每64字节为一页。将页内所有数据变为0x00
* Input          : addr:需擦除的地址      len:擦除的长度
* Output         : None
* Return         : None
*******************************************************************************/
void flashErase(uint16_t addr, uint16_t len)
{
	uint8_t addrA = addr&(FLASH_PAGE_LEN-1);
	if(addrA)		//没有对齐64字节
	{
		addr &= (0xFFFF&(FLASH_PAGE_LEN-1));
		addrA = FLASH_PAGE_LEN-addrA;
		if(len>=addrA)
			len=len-addrA+FLASH_PAGE_LEN;
	}
FLASH_ERASE_LOOP:
	while(FlashErasePage(addr)!=0x00);
	
	if(len>=FLASH_PAGE_LEN)
	{
		len -= FLASH_PAGE_LEN;
		addr += FLASH_PAGE_LEN;
		goto FLASH_ERASE_LOOP;
	}
}
/*******************************************************************************
* Function Name  : ErasePage( UINT16 Addr )
* Description    : 用于页擦除，每64字节为一页。将页内所有数据变为0x00
* Input          : Addr:擦除地址所在页
* Output         : None
* Return         : 返回操作状态，0x00:成功  0x01:地址无效  0x02:未知命令或超时
*******************************************************************************/
uint8_t FlashErasePage( uint16_t Addr )
{
	e_all = EA;
	EA = 0;                                          /* 关闭全局中断,防止Flash操作被打断 */
	Addr &= 0xFFC0;                                  /* 64字节对齐 */
	if((Addr>=DATA_FLASH_ADDR) && (Addr<BOOT_LOAD_ADDR))/* DataFlash区域 */
	{
		FlashType = bDATA_WE;
	}
	else                                             /* CodeFlash区域 */
	{
		FlashType = bCODE_WE;
	}
	SAFE_MOD = 0x55;                                 /* 进入安全模式 */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG |= FlashType;
	ROM_ADDR = Addr;                                 /* 写入目标地址 */
	ROM_BUF_MOD = bROM_BUF_BYTE;                     /* 选择块擦除模式或单字节编程模式 */
	ROM_DAT_BUF = 0;                                 /* 擦写数据缓冲区寄存器为0 */
	if ( ROM_STATUS & bROM_ADDR_OK )                 /* 操作地址有效 */
	{
		ROM_CTRL = ROM_CMD_ERASE;                    /* 启动擦除 */
		if(ROM_STATUS & bROM_CMD_ERR)
		{
			status = 0x02;    /* 未知命令或超时 */
		}
		else
		{
			status = 0x00;    /* 操作成功 */
		}
	}
	else
	{
		status = 0x01;    /* 地址无效 */
	}
	SAFE_MOD = 0x55;                                 /* 进入安全模式 */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG &= ~FlashType;                        /* 开启写保护 */
	EA = e_all;                                      /* 恢复全局中断状态 */
	return status;
}
/*******************************************************************************
* Function Name  : FlashProgByte(UINT16 Addr,UINT8 Data)
* Description    : Flash 字节编程
* Input          : Addr：写入地址
*                  Data：字节编程的数据
* Output         : None
* Return         : 编程状态返回 0x00:成功  0x01:地址无效  0x02:未知命令或超时
*******************************************************************************/
uint8_t FlashProgByte( uint16_t Addr,uint8_t *Data,uint8_t len )
{
//	bit e_all;
//	uint8_t status;                                    /* 返回操作状态 */
//	uint8_t FlashType;                                 /* Flash 类型标志 */
	e_all = EA;
	EA = 0;                                          /* 关闭全局中断,防止Flash操作被打断 */
	if((Addr>=DATA_FLASH_ADDR) && (Addr<BOOT_LOAD_ADDR))/* DataFlash区域 */
	{
		FlashType = bDATA_WE;
	}
	else                                             /* CodeFlash区域 */
	{
		FlashType = bCODE_WE;
	}
	SAFE_MOD = 0x55;                                 /* 进入安全模式 */
	SAFE_MOD = 0xAA;
	while(len--)
	{
		GLOBAL_CFG |= FlashType;
		ROM_ADDR = Addr;                                 /* 写入目标地址 */
		ROM_BUF_MOD = bROM_BUF_BYTE;                     /* 选择块擦除模式或单字节编程模式 */
		ROM_DAT_BUF = *Data;                              /* 数据缓冲区寄存器 */
		if ( ROM_STATUS & bROM_ADDR_OK )                 /* 操作地址有效 */
		{
			ROM_CTRL = ROM_CMD_PROG ;                    /* 启动编程 */
			if(ROM_STATUS & bROM_CMD_ERR)
			{
				status = 0x02;    /* 未知命令或超时 */
			}
			else
			{
				status = 0x00;    /* 操作成功 */
			}
		}
		else
		{
			status = 0x01;    /* 地址无效 */
		}
		Addr++;
		Data++;
	}
	SAFE_MOD = 0x55;                                 /* 进入安全模式 */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG &= ~FlashType;                        /* 开启写保护 */
	EA = e_all;                                      /* 恢复全局中断状态 */
	return status;
}
///*******************************************************************************
//* Function Name  : FlashProgPage( UINT16 Addr, PUINT8X Buf,UINT8 len )
//* Description    : 页编程,仅编程当前Addr所在页。
//* Input          : Addr：写入地址
//*                  Buf：Buf地址的低6位要与Addr地址低6位相等，也就是（Buf地址%64）与页内偏移地址要相同
//*                  len: 写入长度，最大64
//* Output         : None
//* Return         : 编程状态返回 0x00:成功  0x01:地址无效  0x02:未知命令或超时
//*******************************************************************************/
//uint8_t FlashProgPage( uint16_t Addr, PUINT8X Buf,uint8_t len )
//{
////	bit e_all;
////	uint8_t status;                                    /* 返回操作状态 */
////	uint8_t FlashType;                                 /* Flash 类型标志 */
//	uint8_t page_offset;                               /* Addr在当前页的偏移地址 */
////	e_all = EA;
////	EA = 0;                                          /* 关闭全局中断,防止Flash操作被打断 */
//	if((Addr>=DATA_FLASH_ADDR) && (Addr<BOOT_LOAD_ADDR))/* DataFlash区域 */
//	{
//		FlashType = bDATA_WE;
//	}
//	else                                             /* CodeFlash区域 */
//	{
//		FlashType = bCODE_WE;
//	}
//	SAFE_MOD = 0x55;                                 /* 进入安全模式 */
//	SAFE_MOD = 0xAA;
//	GLOBAL_CFG |= FlashType;
//	page_offset = Addr & MASK_ROM_ADDR;
//	if ( len > (ROM_PAGE_SIZE - page_offset) )
//	{
//		return( 0xFC );    /* 起始地址加上本次写的字节数不能超出当前页, 每64字节为一页, 单次操作不得超出当前页 */
//	}
//	if ( ( (UINT8)Buf & MASK_ROM_ADDR ) != page_offset )
//	{
//		return( 0xFB );    /* xdata缓冲区地址低6位必须与起始地址低6位相等 */
//	}
//	ROM_ADDR = Addr;
//	ROM_BUF_MOD = page_offset + len - 1;             /* 页编程结束地址低6位，含改地址 */
//	DPL = (UINT8)Buf;
//	DPH = (UINT8)( (UINT16)Buf >> 8 );
//	if ( ROM_STATUS & bROM_ADDR_OK )                 /* 操作地址有效 */
//	{
//		ROM_CTRL = ROM_CMD_PROG ;                    /* 启动编程 */
//		if(ROM_STATUS & bROM_CMD_ERR)
//		{
//			status = 0x02;    /* 未知命令或超时 */
//		}
//		else
//		{
//			status = 0x00;    /* 操作成功 */
//		}
//	}
//	else
//	{
//		status = 0x01;    /* 地址无效 */
//	}
//	SAFE_MOD = 0x55;                                 /* 进入安全模式 */
//	SAFE_MOD = 0xAA;
//	GLOBAL_CFG &= ~FlashType;                        /* 开启写保护 */
////	EA = e_all;                                      /* 恢复全局中断状态 */
//	return status;
//}
/*******************************************************************************
* Function Name  : FlashReadBuf(UINT16 Addr,PUINT8 buf,UINT16 len)
* Description    : 读Flash（包含data和code）
* Input          : UINT16 Addr,PUINT8 buf,UINT16 len
* Output         : None
* Return         : 返回实际读出长度
*******************************************************************************/
void FlashReadBuf(uint16_t Addr,uint8_t *buf,uint16_t len)
{
	while(len--)
    {
		*buf = *(PUINT8C)Addr;
		Addr++;
		buf++;
	}
	//return i;
}
///*******************************************************************************
//* Function Name  : FlashProgOTPbyte( UINT8 Addr, UINT8 Data )
//* Description    : 单字节写OTP区域，只能0变成1,且不可擦除
//* Input          : Addr：0x20~0x3F
//*                  Data:
//* Output         : None
//* Return         : 操作状态 0x00:成功  0x02:未知命令或超时
//*******************************************************************************/
uint8_t FlashProgOTPbyte( uint8_t Addr, uint8_t Data )
{
	e_all = EA;
	EA = 0;                                          /* 关闭全局中断,防止Flash操作被打断 */
	SAFE_MOD = 0x55;                                 /* 进入安全模式 */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG |= bDATA_WE;
	ROM_ADDR = Addr;
	ROM_BUF_MOD = bROM_BUF_BYTE;
	ROM_DAT_BUF = Data;
	ROM_CTRL = ROM_CMD_PG_OTP;
	if(ROM_STATUS & bROM_CMD_ERR)
	{
		status = 0x02;    /* 未知命令或超时 */
	}
	else
	{
		status = 0x00;    /* 操作成功 */
	}
	SAFE_MOD = 0x55;                                 /* 进入安全模式 */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG &= ~bDATA_WE;                         /* 开启写保护 */
	EA = e_all;                                      /* 恢复全局中断状态 */
	SAFE_MOD = 0x00;
	return status;
}
///*******************************************************************************
//* Function Name  : FlashReadOTPword( UINT8 Addr )
//* Description    : 4字节为单位读取ReadOnly区或者OTP区
//* Input          : Addr：0x00~0x3F
//* Output         : None
//* Return         : 读取的四字节数据
//*******************************************************************************/
xdata uint8_t otpData[4];
void FlashReadOTPword( uint8_t Addr )
{
    ROM_ADDR = (uint16_t)Addr;
    ROM_CTRL = ROM_CMD_RD_OTP;
	otpData[0] = ROM_DATA_LL;
	otpData[1] = ROM_DATA_LH;
	otpData[2] = ROM_DATA_HL;
	otpData[3] = ROM_DATA_HH;
}
