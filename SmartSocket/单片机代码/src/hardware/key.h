#ifndef	__KEY_H__
#define	__KEY_H__

//typedef struct {
//	uint8_t DnNum;
//	uint8_t val1;
//	uint8_t val2;
//	uint8_t flag;
//} KEY_DESC;
//extern KEY_DESC key;

extern uint8_t idata key_cval;

#define KEY_LINE_NUM 4

#define KEY_PORT P3
#define S2_KEY_PIN 4
#define S3_KEY_PIN 5
#define S4_KEY_PIN 6
#define S5_KEY_PIN 7

#define NULL_KEY 0x00
#define S2_KEY (1<<S2_KEY_PIN)
#define S3_KEY (1<<S3_KEY_PIN)
#define S4_KEY (1<<S4_KEY_PIN)
#define S5_KEY (1<<S5_KEY_PIN)

#define KEY_ALL (S2_KEY|(S3_KEY)|(S4_KEY)|(S5_KEY))


void Key_Init(void);
void keyscan(void);
void WaitKeyUp(void);

#endif	//__KEY_H__
