/********************************** (C) COPYRIGHT *******************************
* File Name          : DataFlash.C
* Author             : WCH
* Version            : V1.0
* Date               : 2018/08/09
* Description        : CH549 DataFlash�ֽڶ�д��������
*******************************************************************************/
#include "main.h"
#include "flashrom.h"

bit e_all;
uint8_t status;                                    /* ���ز���״̬ */
uint8_t FlashType;                                 /* Flash ���ͱ�־ */

/*******************************************************************************
* Function Name  : flashErase(uint16_t addr, uint16_t len)
* Description    : ����ҳ������ÿ64�ֽ�Ϊһҳ����ҳ���������ݱ�Ϊ0x00
* Input          : addr:������ĵ�ַ      len:�����ĳ���
* Output         : None
* Return         : None
*******************************************************************************/
void flashErase(uint16_t addr, uint16_t len)
{
	uint8_t addrA = addr&(FLASH_PAGE_LEN-1);
	if(addrA)		//û�ж���64�ֽ�
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
* Description    : ����ҳ������ÿ64�ֽ�Ϊһҳ����ҳ���������ݱ�Ϊ0x00
* Input          : Addr:������ַ����ҳ
* Output         : None
* Return         : ���ز���״̬��0x00:�ɹ�  0x01:��ַ��Ч  0x02:δ֪�����ʱ
*******************************************************************************/
uint8_t FlashErasePage( uint16_t Addr )
{
	e_all = EA;
	EA = 0;                                          /* �ر�ȫ���ж�,��ֹFlash��������� */
	Addr &= 0xFFC0;                                  /* 64�ֽڶ��� */
	if((Addr>=DATA_FLASH_ADDR) && (Addr<BOOT_LOAD_ADDR))/* DataFlash���� */
	{
		FlashType = bDATA_WE;
	}
	else                                             /* CodeFlash���� */
	{
		FlashType = bCODE_WE;
	}
	SAFE_MOD = 0x55;                                 /* ���밲ȫģʽ */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG |= FlashType;
	ROM_ADDR = Addr;                                 /* д��Ŀ���ַ */
	ROM_BUF_MOD = bROM_BUF_BYTE;                     /* ѡ������ģʽ���ֽڱ��ģʽ */
	ROM_DAT_BUF = 0;                                 /* ��д���ݻ������Ĵ���Ϊ0 */
	if ( ROM_STATUS & bROM_ADDR_OK )                 /* ������ַ��Ч */
	{
		ROM_CTRL = ROM_CMD_ERASE;                    /* �������� */
		if(ROM_STATUS & bROM_CMD_ERR)
		{
			status = 0x02;    /* δ֪�����ʱ */
		}
		else
		{
			status = 0x00;    /* �����ɹ� */
		}
	}
	else
	{
		status = 0x01;    /* ��ַ��Ч */
	}
	SAFE_MOD = 0x55;                                 /* ���밲ȫģʽ */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG &= ~FlashType;                        /* ����д���� */
	EA = e_all;                                      /* �ָ�ȫ���ж�״̬ */
	return status;
}
/*******************************************************************************
* Function Name  : FlashProgByte(UINT16 Addr,UINT8 Data)
* Description    : Flash �ֽڱ��
* Input          : Addr��д���ַ
*                  Data���ֽڱ�̵�����
* Output         : None
* Return         : ���״̬���� 0x00:�ɹ�  0x01:��ַ��Ч  0x02:δ֪�����ʱ
*******************************************************************************/
uint8_t FlashProgByte( uint16_t Addr,uint8_t *Data,uint8_t len )
{
//	bit e_all;
//	uint8_t status;                                    /* ���ز���״̬ */
//	uint8_t FlashType;                                 /* Flash ���ͱ�־ */
	e_all = EA;
	EA = 0;                                          /* �ر�ȫ���ж�,��ֹFlash��������� */
	if((Addr>=DATA_FLASH_ADDR) && (Addr<BOOT_LOAD_ADDR))/* DataFlash���� */
	{
		FlashType = bDATA_WE;
	}
	else                                             /* CodeFlash���� */
	{
		FlashType = bCODE_WE;
	}
	SAFE_MOD = 0x55;                                 /* ���밲ȫģʽ */
	SAFE_MOD = 0xAA;
	while(len--)
	{
		GLOBAL_CFG |= FlashType;
		ROM_ADDR = Addr;                                 /* д��Ŀ���ַ */
		ROM_BUF_MOD = bROM_BUF_BYTE;                     /* ѡ������ģʽ���ֽڱ��ģʽ */
		ROM_DAT_BUF = *Data;                              /* ���ݻ������Ĵ��� */
		if ( ROM_STATUS & bROM_ADDR_OK )                 /* ������ַ��Ч */
		{
			ROM_CTRL = ROM_CMD_PROG ;                    /* ������� */
			if(ROM_STATUS & bROM_CMD_ERR)
			{
				status = 0x02;    /* δ֪�����ʱ */
			}
			else
			{
				status = 0x00;    /* �����ɹ� */
			}
		}
		else
		{
			status = 0x01;    /* ��ַ��Ч */
		}
		Addr++;
		Data++;
	}
	SAFE_MOD = 0x55;                                 /* ���밲ȫģʽ */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG &= ~FlashType;                        /* ����д���� */
	EA = e_all;                                      /* �ָ�ȫ���ж�״̬ */
	return status;
}
///*******************************************************************************
//* Function Name  : FlashProgPage( UINT16 Addr, PUINT8X Buf,UINT8 len )
//* Description    : ҳ���,����̵�ǰAddr����ҳ��
//* Input          : Addr��д���ַ
//*                  Buf��Buf��ַ�ĵ�6λҪ��Addr��ַ��6λ��ȣ�Ҳ���ǣ�Buf��ַ%64����ҳ��ƫ�Ƶ�ַҪ��ͬ
//*                  len: д�볤�ȣ����64
//* Output         : None
//* Return         : ���״̬���� 0x00:�ɹ�  0x01:��ַ��Ч  0x02:δ֪�����ʱ
//*******************************************************************************/
//uint8_t FlashProgPage( uint16_t Addr, PUINT8X Buf,uint8_t len )
//{
////	bit e_all;
////	uint8_t status;                                    /* ���ز���״̬ */
////	uint8_t FlashType;                                 /* Flash ���ͱ�־ */
//	uint8_t page_offset;                               /* Addr�ڵ�ǰҳ��ƫ�Ƶ�ַ */
////	e_all = EA;
////	EA = 0;                                          /* �ر�ȫ���ж�,��ֹFlash��������� */
//	if((Addr>=DATA_FLASH_ADDR) && (Addr<BOOT_LOAD_ADDR))/* DataFlash���� */
//	{
//		FlashType = bDATA_WE;
//	}
//	else                                             /* CodeFlash���� */
//	{
//		FlashType = bCODE_WE;
//	}
//	SAFE_MOD = 0x55;                                 /* ���밲ȫģʽ */
//	SAFE_MOD = 0xAA;
//	GLOBAL_CFG |= FlashType;
//	page_offset = Addr & MASK_ROM_ADDR;
//	if ( len > (ROM_PAGE_SIZE - page_offset) )
//	{
//		return( 0xFC );    /* ��ʼ��ַ���ϱ���д���ֽ������ܳ�����ǰҳ, ÿ64�ֽ�Ϊһҳ, ���β������ó�����ǰҳ */
//	}
//	if ( ( (UINT8)Buf & MASK_ROM_ADDR ) != page_offset )
//	{
//		return( 0xFB );    /* xdata��������ַ��6λ��������ʼ��ַ��6λ��� */
//	}
//	ROM_ADDR = Addr;
//	ROM_BUF_MOD = page_offset + len - 1;             /* ҳ��̽�����ַ��6λ�����ĵ�ַ */
//	DPL = (UINT8)Buf;
//	DPH = (UINT8)( (UINT16)Buf >> 8 );
//	if ( ROM_STATUS & bROM_ADDR_OK )                 /* ������ַ��Ч */
//	{
//		ROM_CTRL = ROM_CMD_PROG ;                    /* ������� */
//		if(ROM_STATUS & bROM_CMD_ERR)
//		{
//			status = 0x02;    /* δ֪�����ʱ */
//		}
//		else
//		{
//			status = 0x00;    /* �����ɹ� */
//		}
//	}
//	else
//	{
//		status = 0x01;    /* ��ַ��Ч */
//	}
//	SAFE_MOD = 0x55;                                 /* ���밲ȫģʽ */
//	SAFE_MOD = 0xAA;
//	GLOBAL_CFG &= ~FlashType;                        /* ����д���� */
////	EA = e_all;                                      /* �ָ�ȫ���ж�״̬ */
//	return status;
//}
/*******************************************************************************
* Function Name  : FlashReadBuf(UINT16 Addr,PUINT8 buf,UINT16 len)
* Description    : ��Flash������data��code��
* Input          : UINT16 Addr,PUINT8 buf,UINT16 len
* Output         : None
* Return         : ����ʵ�ʶ�������
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
//* Description    : ���ֽ�дOTP����ֻ��0���1,�Ҳ��ɲ���
//* Input          : Addr��0x20~0x3F
//*                  Data:
//* Output         : None
//* Return         : ����״̬ 0x00:�ɹ�  0x02:δ֪�����ʱ
//*******************************************************************************/
uint8_t FlashProgOTPbyte( uint8_t Addr, uint8_t Data )
{
	e_all = EA;
	EA = 0;                                          /* �ر�ȫ���ж�,��ֹFlash��������� */
	SAFE_MOD = 0x55;                                 /* ���밲ȫģʽ */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG |= bDATA_WE;
	ROM_ADDR = Addr;
	ROM_BUF_MOD = bROM_BUF_BYTE;
	ROM_DAT_BUF = Data;
	ROM_CTRL = ROM_CMD_PG_OTP;
	if(ROM_STATUS & bROM_CMD_ERR)
	{
		status = 0x02;    /* δ֪�����ʱ */
	}
	else
	{
		status = 0x00;    /* �����ɹ� */
	}
	SAFE_MOD = 0x55;                                 /* ���밲ȫģʽ */
	SAFE_MOD = 0xAA;
	GLOBAL_CFG &= ~bDATA_WE;                         /* ����д���� */
	EA = e_all;                                      /* �ָ�ȫ���ж�״̬ */
	SAFE_MOD = 0x00;
	return status;
}
///*******************************************************************************
//* Function Name  : FlashReadOTPword( UINT8 Addr )
//* Description    : 4�ֽ�Ϊ��λ��ȡReadOnly������OTP��
//* Input          : Addr��0x00~0x3F
//* Output         : None
//* Return         : ��ȡ�����ֽ�����
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
