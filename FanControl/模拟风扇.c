#include "reg52.h"
#include "onewire.h"

#define uchar unsigned char
#define uint unsigned int

uchar tab[] = {0xc0,0xf9,0xa4,0xb0,0x99,0x92,0x82,0xf8,0x80,0x90,0xbf,0xff,0xc6};
uchar yi,er,san,si,wu,liu,qi,ba;
uchar Time=0;
uchar moshi=1;
uint count=0;
uchar pwm=0;
bit wendu=0;
bit fla=0;

sbit S4 = P3^3;
sbit S5 = P3^2;
sbit S6 = P3^1;
sbit S7 = P3^0;
sbit P34 = P3^4;

void Delayms(uint t)
{
		uint i,j;
		for(i=t;i>0;i--)
			for(j=845;j>0;j--);
}

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

void TimerInit(void)		
{
	TMOD = 0x01;		//设置定时器模式
	TL0 = (65535 - 100)%256;		//设置定时初值
	TH0 = (65535 - 100)/256;		//设置定时初值
	TF0 = 0;		//清除TF0标志
	TR0 = 1;		//定时器0开始计时

	ET0 = 1;
	EA = 1;
}

void ServiceTimer0() interrupt 1
{
	TL0 = (65535 - 100)%256;		//设置定时初值
	TH0 = (65535 - 100)/256;		//设置定时初值

	count++;
//==================没有电机，暂时无法实现该功能==================	
//	pwm++;
//	if(pwm==10)pwm=0;
//	
//	if(Time>0)
//	{
//		if(moshi==1)
//		{
//			if(pwm<3)P34=1;
//			else P34=0;
//		}
//		else if(moshi==2)
//		{
//			if(pwm<4)P34=1;
//			else P34=0;
//		}
//		else if(moshi==3)
//		{
//			if(pwm<8)P34=1;
//			else P34=0;
//		}
//	}
	
	if(count==10000)
	{
		count=0;
		if(Time>0)Time--;
	}
}

void display_gongzuo()
{
		if(wendu==0)
		{
				yi=10;er=moshi;san=10;si=11;wu=Time/1000;liu=Time/100;qi=Time/10;ba=Time%10;
		}
		else if(wendu==1)
		{
				yi=10;er=4;san=10;si=11;wu=11;liu=ds18b20_get()/10;qi=ds18b20_get()%10;ba=12;
		}
}

void KeyScan()
{
		if(S4==0)
		{
				Delayms(5);
				if(S4==0)
				{
						if(moshi==1){moshi=2;}
						else if(moshi==2){moshi=3;}
						else if(moshi==3){moshi=1;}
				}
				while(!S4);
		}
		else if(S5==0)
		{
				Delayms(5);
				if(S5==0)
				{
						if(Time==0)Time=60;
						else if(Time>0){Time=120;fla=1;}
						else if(fla==1){Time=0;fla=0;}
				}
				while(!S5);
		}
		else if(S6==0)
		{
				Delayms(5);
				if(S6==0)
				{
						Time=0;fla=0;
				}
		}
		else if(S7==0)
		{
				Delayms(5);
				if(S7==0)
				{
						wendu=~wendu;
				}
		while(!S7);
		}
}

void display1(uchar yi,uchar er)
{
		P2 = 0xc0;
		P0 = 0x01;
		P2 = 0xff;
		P0 = tab[yi];
		Delayms(1);
		
		P2 = 0xc0;
		P0 = 0x02;
		P2 = 0xff;
		P0 = tab[er];
		Delayms(1);
}

void display2(uchar san,uchar si)
{
		P2 = 0xc0;
		P0 = 0x04;
		P2 = 0xff;
		P0 = tab[san];
		Delayms(1);
		
		P2 = 0xc0;
		P0 = 0x08;
		P2 = 0xff;
		P0 = tab[si];
		Delayms(1);
}

void display3(uchar wu,uchar liu)
{
		P2 = 0xc0;
		P0 = 0x10;
		P2 = 0xff;
		P0 = tab[wu];
		Delayms(1);
		
		P2 = 0xc0;
		P0 = 0x20;
		P2 = 0xff;
		P0 = tab[liu];
		Delayms(1);
}

void display4(uchar qi,uchar ba)
{
		P2 = 0xc0;
		P0 = 0x40;
		P2 = 0xff;
		P0 = tab[qi];
		Delayms(1);
		
		P2 = 0xc0;
		P0 = 0x80;
		P2 = 0xff;
		P0 = tab[ba];
		Delayms(1);
	
		P2 = 0xc0;
		P0 = 0xff;
		P2 = 0xff;
		P0 = 0xff;
}

void main()
{
		InitSystem();
		TimerInit();
		while(1)
		{
				display_gongzuo();
				
				if(Time>0)
				{
						if(moshi==1){P2=0x80;P0=0xfe;}
						else if(moshi==2){P2=0x80;P0=0xfd;}
						else if(moshi==3){P2=0x80;P0=0xfb;}
				}
				else {P2=0x80;P0=0xff;}			//倒计时结束，关闭LED灯显示
				
				KeyScan();
				display1(yi,er);
				display2(san,si);
				display3(wu,liu);
				display4(qi,ba);
		}
}