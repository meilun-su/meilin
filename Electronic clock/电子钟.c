#include <STC15F2K60S2.H>
#include "ds1302.h"
#include "onewire.h"

#define uchar unsigned char
#define uint unsigned int

uchar code tab[]={0xc0,0xf9,0xa4,0xb0,0x99,0x92,0x82,0xf8,0x80,0x90,0xbf,0xff,0xc6}; 
uchar yi,er,san,si,wu,liu,qi,ba;		//数码管位选相关应用
extern unsigned char shijian[];			//extern的作用表示变量或函数定义在别的文件中，此处的shijian[]数组在ds1302.c文件中定义
uchar Alarm_clock[3] = {0,0,0};			//闹钟数组，记录闹钟的时间
uchar Time_set=5,Alarm_set=0;				
/**************************************************************
两个变量用来表示时钟（Time_set）和闹钟(Alarm_set)的状态
Time_set=5    表示正常显示当前时钟，Time_set和Alarm_set不同时为5
Time_set=1    表示显示时钟，数码管显示小时的两位数码管闪烁
Time_set=2		表示显示时钟，数码管显示分钟的两位数码管闪烁
Time_set=3    表示显示时钟，数码管显示秒钟的两位数码管闪烁
Time_set=0		表示不显示时钟
时钟显示（Time_set=5）和闹钟显示（Alarm_set=5）不能同时工作，Time_set=5时Alarm_set=0；Time_set=0时Alarm_set=5
***************************************************************/
bit wendu=0;												//温度状态，0为不显示温度，1为显示温度
bit fla=0;													//led灯状态，1为亮，0为灭
uchar s4=0;													//在温度显示相关操作使用
uint miao=0;												//用来计数，计数led灯闪烁时间5s
uchar tt=0;													//用来计数，led灯以0.2s闪烁
uchar LED_shan=0;										//判断led灯闪烁状态，1为闪烁，0为不闪烁
uchar SMG=0,SMG_shan=0;							//SMG用于计数，计数一秒，使得数码管闪烁，SMG_shan表示数码管亮灭状态，0为亮，1为灭

sbit S7 = P3^0;
sbit S6 = P3^1;
sbit S5 = P3^2;
sbit S4 = P3^3;

//======================初始化函数=================================
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
//======================时间显示函数=================================
void Display_Time(uchar n)
{
		switch(n)
		{
			case 5:
					yi=shijian[2]/10;er=shijian[2]%10;san=10;si=shijian[1]/10;wu=shijian[1]%10;liu=10;qi=shijian[0]/10;ba=shijian[0]%10;
			break;
			
			case 1:
					DS1302_tingzhi();		//停止时钟振荡
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
//======================闹钟显示函数=================================
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

//==================定时器0中断函数=========================
void Timer0Init(void)		//5毫秒@11.0592MHz
{
	AUXR |= 0x80;		//定时器时钟1T模式
	TMOD &= 0xF0;		//设置定时器模式
	TL0 = 0x00;		//设置定时初值
	TH0 = 0x28;		//设置定时初值
	TF0 = 0;		//清除TF0标志
	TR0 = 1;		//定时器0开始计时
	
	ET0 = 1;
	EA = 1;
}
//=================定时器0中断服务函数=======================
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

//======================数码管显示函数============================
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

//==============================按键控制函数================================
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
			else if(wendu==1)		//温度
			{
					yi=11;er=11;san=11;si=11;wu=11;liu=GetTemp()/10;qi=GetTemp()%10;ba=12;
			}
			//闹钟
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