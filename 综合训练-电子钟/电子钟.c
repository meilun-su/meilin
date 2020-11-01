#include <STC15F2K60S2.H>
#include "ds1302.h"
#include "onewire.h"

#define uchar unsigned char
#define uint unsigned int

uchar code tab[]={0xc0,0xf9,0xa4,0xb0,0x99,0x92,0x82,0xf8,0x80,0x90,0xbf,0xff,0xc6}; 
uchar yi,er,san,si,wu,liu,qi,ba;		//�����λѡ���Ӧ��
extern unsigned char shijian[];			//extern�����ñ�ʾ�������������ڱ���ļ��У��˴���shijian[]������ds1302.c�ļ��ж���
uchar Alarm_clock[3] = {0,0,0};			//�������飬��¼���ӵ�ʱ��
uchar Time_set=5,Alarm_set=0;				
/**************************************************************
��������������ʾʱ�ӣ�Time_set��������(Alarm_set)��״̬
Time_set=5    ��ʾ������ʾ��ǰʱ�ӣ�Time_set��Alarm_set��ͬʱΪ5
Time_set=1    ��ʾ��ʾʱ�ӣ��������ʾСʱ����λ�������˸
Time_set=2		��ʾ��ʾʱ�ӣ��������ʾ���ӵ���λ�������˸
Time_set=3    ��ʾ��ʾʱ�ӣ��������ʾ���ӵ���λ�������˸
Time_set=0		��ʾ����ʾʱ��
ʱ����ʾ��Time_set=5����������ʾ��Alarm_set=5������ͬʱ������Time_set=5ʱAlarm_set=0��Time_set=0ʱAlarm_set=5
***************************************************************/
bit wendu=0;												//�¶�״̬��0Ϊ����ʾ�¶ȣ�1Ϊ��ʾ�¶�
bit fla=0;													//led��״̬��1Ϊ����0Ϊ��
uchar s4=0;													//���¶���ʾ��ز���ʹ��
uint miao=0;												//��������������led����˸ʱ��5s
uchar tt=0;													//����������led����0.2s��˸
uchar LED_shan=0;										//�ж�led����˸״̬��1Ϊ��˸��0Ϊ����˸
uchar SMG=0,SMG_shan=0;							//SMG���ڼ���������һ�룬ʹ���������˸��SMG_shan��ʾ���������״̬��0Ϊ����1Ϊ��

sbit S7 = P3^0;
sbit S6 = P3^1;
sbit S5 = P3^2;
sbit S4 = P3^3;

//======================��ʼ������=================================
void InitSystem()
{
		P2 = 0xa0;
		P0 = 0x00;
	
		P2 = 0x80;
		P0 = 0xff;
	
		P2 = 0xc0;
		P0 = 0xff;
		P2 = 0xff;
		P0 = 0xff;
}
//======================ʱ����ʾ����=================================
void Display_Time(uchar n)
{
		switch(n)
		{
			case 5:
					yi=shijian[2]/10;er=shijian[2]%10;san=10;si=shijian[1]/10;wu=shijian[1]%10;liu=10;qi=shijian[0]/10;ba=shijian[0]%10;
			break;
			
			case 1:
					DS1302_tingzhi();		//ֹͣʱ����
					if(SMG_shan==0)
					{
							yi=shijian[2]/10;er=shijian[2]%10;san=10;si=shijian[1]/10;wu=shijian[1]%10;liu=10;qi=shijian[0]/10;ba=shijian[0]%10;
					}
					else if(SMG_shan==1)
					{
							yi=11;er=11;san=10;si=shijian[1]/10;wu=shijian[1]%10;liu=10;qi=shijian[0]/10;ba=shijian[0]%10;
					}
			break;
			
			case 2:
					DS1302_tingzhi();
					if(SMG_shan==0)
					{
							yi=shijian[2]/10;er=shijian[2]%10;san=10;si=shijian[1]/10;wu=shijian[1]%10;liu=10;qi=shijian[0]/10;ba=shijian[0]%10;
					}
					else if(SMG_shan==1)
					{
							yi=shijian[2]/10;er=shijian[2]%10;san=10;si=11;wu=11;liu=10;qi=shijian[0]/10;ba=shijian[0]%10;
					}
			break;
			
			case 3:
					DS1302_tingzhi();
					if(SMG_shan==0)
					{
							yi=shijian[2]/10;er=shijian[2]%10;san=10;si=shijian[1]/10;wu=shijian[1]%10;liu=10;qi=shijian[0]/10;ba=shijian[0]%10;
					}
					else if(SMG_shan==1)
					{
							yi=shijian[2]/10;er=shijian[2]%10;san=10;si=shijian[1]/10;wu=shijian[1]%10;liu=10;qi=11;ba=11;
					}
			break;
		}
}
//======================������ʾ����=================================
void Display_Alarm(uchar n)
{
		switch(n)
		{
			case 5:
					yi=Alarm_clock[2]/10;er=Alarm_clock[2]%10;san=10;si=Alarm_clock[1]/10;wu=Alarm_clock[1]%10;liu=10;qi=Alarm_clock[0]/10;ba=Alarm_clock[0]%10;
			break;
			
			case 1:
					if(SMG_shan==0)
					{
							yi=Alarm_clock[2]/10;er=Alarm_clock[2]%10;san=10;si=Alarm_clock[1]/10;wu=Alarm_clock[1]%10;liu=10;qi=Alarm_clock[0]/10;ba=Alarm_clock[0]%10;
					}
					else if(SMG_shan==1)
					{
							yi=11;er=11;san=10;si=Alarm_clock[1]/10;wu=Alarm_clock[1]%10;liu=10;qi=Alarm_clock[0]/10;ba=Alarm_clock[0]%10;
					}
			break;
			
			case 2:
					if(SMG_shan==0)
					{
							yi=Alarm_clock[2]/10;er=Alarm_clock[2]%10;san=10;si=Alarm_clock[1]/10;wu=Alarm_clock[1]%10;liu=10;qi=Alarm_clock[0]/10;ba=Alarm_clock[0]%10;
					}
					else if(SMG_shan==1)
					{
							yi=Alarm_clock[2]/10;er=Alarm_clock[2]%10;san=10;si=11;wu=11;liu=10;qi=Alarm_clock[0]/10;ba=Alarm_clock[0]%10;
					}
			break;
			
			case 3:
					if(SMG_shan==0)
					{
							yi=Alarm_clock[2]/10;er=Alarm_clock[2]%10;san=10;si=Alarm_clock[1]/10;wu=Alarm_clock[1]%10;liu=10;qi=Alarm_clock[0]/10;ba=Alarm_clock[0]%10;
					}
					else if(SMG_shan==1)
					{
							yi=Alarm_clock[2]/10;er=Alarm_clock[2]%10;san=10;si=Alarm_clock[1]/10;wu=Alarm_clock[1]%10;liu=10;qi=11;ba=11;
					}
			break;
		}
}

//==================��ʱ��0�жϺ���=========================
void Timer0Init(void)		//5����@11.0592MHz
{
	AUXR |= 0x80;		//��ʱ��ʱ��1Tģʽ
	TMOD &= 0xF0;		//���ö�ʱ��ģʽ
	TL0 = 0x00;		//���ö�ʱ��ֵ
	TH0 = 0x28;		//���ö�ʱ��ֵ
	TF0 = 0;		//���TF0��־
	TR0 = 1;		//��ʱ��0��ʼ��ʱ
	
	ET0 = 1;
	EA = 1;
}
//=================��ʱ��0�жϷ�����=======================
void Time0() interrupt 1
{
		SMG++;miao++;tt++;
		if((tt==40)&&(LED_shan==1)) 	
		{
				tt=0;
				if(fla==0){fla=1;P2=0x80;P0=0xfe;}
				else if(fla==1){fla=0;P2=0x80;P0=0xff;}
		}
		
		if(miao==1000)
		{
				miao=0;
				LED_shan=0;P2=0x80;P0=0xff;
		}
	
		if(SMG == 200)
		{
				SMG=0;
				SMG_shan=!SMG_shan;
		}
}

void delayms(uint t)
{
		uint i,j;
		for(i=t;i>0;i--)
			for(j=845;j>0;j--);
}

//======================�������ʾ����============================
void Display1(uchar yi,uchar er)
{
		P2 = 0xc0;
		P0 = 0x01;
		P2 = 0xff;
		P0 = tab[yi];
		delayms(1);
	
		P2 = 0xc0;
		P0 = 0x02;
		P2 = 0xff;
		P0 = tab[er];
		delayms(1);
}

void Display2(uchar san,uchar si)
{
		P2 = 0xc0;
		P0 = 0x04;
		P2 = 0xff;
		P0 = tab[san];
		delayms(1);
	
		P2 = 0xc0;
		P0 = 0x08;
		P2 = 0xff;
		P0 = tab[si];
		delayms(1);
}

void Display3(uchar wu,uchar liu)
{
		P2 = 0xc0;
		P0 = 0x10;
		P2 = 0xff;
		P0 = tab[wu];
		delayms(1);
	
		P2 = 0xc0;
		P0 = 0x20;
		P2 = 0xff;
		P0 = tab[liu];
		delayms(1);
}

void Display4(uchar qi,uchar ba)
{
		P2 = 0xc0;
		P0 = 0x40;
		P2 = 0xff;
		P0 = tab[qi];
		delayms(1);
	
		P2 = 0xc0;
		P0 = 0x80;
		P2 = 0xff;
		P0 = tab[ba];
		delayms(1);
	
		P2 = 0xc0;
		P0 = 0xff;
		P2 = 0xff;
		P0 = 0xff;
		delayms(1);
}

//==============================�������ƺ���================================
void KeyScan(void)
{
		if(S7 == 0)
		{
				delayms(5);
				if(S7 == 0)
				{
						if(LED_shan==0)
						{
								if(Time_set==0){Time_set=5;Alarm_set=0;}
								else if(Time_set==5)Time_set=1;
								else if(Time_set==1)Time_set=2;
								else if(Time_set==2)Time_set=3;
								else if(Time_set==3)Time_set=5;
						}
						else if(LED_shan==1)
						{	
								LED_shan=0;P2=0x80;P0=0xff;
						}
				while(!S7);
				}
		}
		
		else if(S6 == 0)
		{
				delayms(5);
				if(S6 == 0)
				{
						if(LED_shan==0)
						{
								if(Alarm_set==0){Alarm_set=5;Time_set=0;}
								else if(Alarm_set==5)Alarm_set=1;
								else if(Alarm_set==1)Alarm_set=2;
								else if(Alarm_set==2)Alarm_set=3;
								else if(Alarm_set==3)Alarm_set=5;
						}
						else if(LED_shan==1)
						{	
								LED_shan=0;P2=0x80;P0=0xff;
						}
				while(!S6);
				}
		}
		
		else if(S5 == 0)
		{
				delayms(5);
				if(S5 == 0)
				{
						if(LED_shan==0)
						{
								if(Time_set==1)
								{
										if(shijian[2]==23)shijian[2]=0;
										else shijian[2]++;
										DS1302_Init();
								}
								else if(Time_set==2)
								{
										if(shijian[1]==59)shijian[1]=0;
										else shijian[1]++;
										DS1302_Init();
								}
								else if(Time_set==3)
								{
										if(shijian[0]==59)shijian[0]=0;
										else shijian[0]++;
										DS1302_Init();
								}
								else if(Alarm_set==1)
								{
										if(Alarm_clock[2]==23)Alarm_clock[2]=0;
										else Alarm_clock[2]++;
								}
								else if(Alarm_set==2)
								{
										if(Alarm_clock[1]==59)Alarm_clock[1]=0;
										else Alarm_clock[1]++;
								}
								else if(Alarm_set==3)
								{
										if(Alarm_clock[0]==59)Alarm_clock[0]=0;
										else Alarm_clock[0]++;
								}
						}
						else if(LED_shan==1)
						{	
								LED_shan=0;P2=0x80;P0=0xff;
						}
				while(!S5);
				}
		}
		else if(S4==0)
		{
			delayms(5);
				if(S4==0)
				{
						s4=1;
						if(Time_set==5)wendu=1;
						if(S4==0&&LED_shan==1)
						{
							LED_shan=0;P2=0x80;P0=0xff;
							while(!S4);
						}
				}
		}
		if((S4 == 1)&&(s4==1))
		{
				s4=0;
				if(LED_shan==0)
				{
						wendu=0;
						if(Time_set==1)
						{
								if(shijian[2]==0)shijian[2]=23;
								else shijian[2]--;
								DS1302_Init();
						}
						else if(Time_set==2)
						{
								if(shijian[1]==0)shijian[1]=59;
								else shijian[1]--;
								DS1302_Init();
						}
						else if(Time_set==3)
						{
								if(shijian[0]==0)shijian[0]=59;
								else shijian[0]--;
								DS1302_Init();
						}
						else if(Alarm_set==1)
						{
								if(Alarm_clock[2]==0)Alarm_clock[2]=23;
								else Alarm_clock[2]--;
						}
						else if(Alarm_set==2)
						{
								if(Alarm_clock[1]==0)Alarm_clock[1]=59;
								else Alarm_clock[1]--;
						}
						else if(Alarm_set==3)
						{
								if(Alarm_clock[0]==0)Alarm_clock[0]=59;
								else Alarm_clock[0]--;
						}
				}
				else if(LED_shan==1)
				{	
						LED_shan=0;P2=0x80;P0=0xff;
				}
		}
}
void main()
{
		InitSystem();
		Timer0Init();
		DS1302_Init();
		while(1)
		{
			DS1302_get();
			
			if(wendu==0)
			{
					Display_Time(Time_set);
		
					Display_Alarm(Alarm_set);
			}
			else if(wendu==1)		//�¶�
			{
					yi=11;er=11;san=11;si=11;wu=11;liu=GetTemp()/10;qi=GetTemp()%10;ba=12;
			}
			//����
			if((Time_set==1||Time_set==2||Time_set==3)&&(shijian[0]==Alarm_clock[0])&&(shijian[1]==Alarm_clock[1])&&(shijian[2]==Alarm_clock[2]))
			{
					KeyScan();
			}
			else if((shijian[0]==Alarm_clock[0])&&(shijian[1]==Alarm_clock[1])&&(shijian[2]==Alarm_clock[2]))
			{
					LED_shan=1;tt=0;miao=0;
			}
			
			KeyScan();
			Display1(yi,er);
			Display2(san,si);
			Display3(wu,liu);
			Display4(qi,ba);
		}
}