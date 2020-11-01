#ifndef __FLASHROM_H__
#define __FLASHROM_H__
/*********************** Flash�ռ仮�� *****************************
*   0xFFFD  ---
*            |                   Code Flash(BOOT����3K)
*   0xF400  ---
*   0xF3FF  ---
*            |                   Data Flash��1K��
*   0xF000  ---
*   0xEFFF  ---
*            |                   Code Flash���û�������60K��
*   0x0000  ---
******************************************************************/
#define FLASH_PAGE_LEN	64

extern xdata uint8_t otpData[4];
/* �Ӻ������� */
/* Code Flash��Data Flash���� */
extern void flashErase(uint16_t addr, uint16_t len);
extern uint8_t FlashErasePage( uint16_t Addr ) ;
extern uint8_t FlashProgByte( uint16_t Addr,uint8_t *Data,uint8_t len );
extern uint8_t FlashProgPage( uint16_t Addr, PUINT8X Buf,uint8_t len );
extern void FlashReadBuf(uint16_t Addr,uint8_t *buf,uint16_t len);
/* OTP����ReadOnly������ */
extern uint8_t  FlashProgOTPbyte( uint8_t Addr, uint8_t Data );
extern void FlashReadOTPword( uint8_t Addr );
#endif //__FLASHROM_H__
