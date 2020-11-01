#include "reg52.h"
#include "intrins.h"
#include "iic.h"

#define uchar unsigned char
#define uint unsigned int

sfr AUXR = 0x8e;

uchar code table[]={   //流水灯四种运行模式
	0xfe,0xfd,0xfb,0xf7,0xef,0xdf,0xbf,0x7f,
	0x7f,0xbf,0xdf,0xef,0xf7,0xfb,0xfd,0xfe,
	0x7e,0xbd,0xdb,0xe7,
	0xe7,0xdb,0xbd,0x7e,
};
uchar code tab[]={0xc0,0xf9,0xa4,0xb0,0x99,
									0x92,0x82,0xf8,0x80,0x90,
									0xbf,0xff};
uchar yi,er,san,si,wu,liu,qi,ba;
uchar mode=0;
uchar time=80;
uchar led_wei=0;
uchar led=0;
uchar count=0;
uchar shezhi=0;
uchar fla=0;
uint tt=0,pwm_tt=0;
uchar U=0;
uchar s4=0;
uchar mm=0;
uchar PWM=0;
									
sbit S7=P3^0;
sbit S6=P3^1;
sbit S5=P3^2;
sbit S4=P3^3;

void InitSystem()
{
		P2=0xa0;
		P0=0x00;
	
		P2=0x80;
		P0=0xff;
		
		P2=0xc0;
		P0=0xff;
		P2=0xff;
		P0=0xff;
}

void delay(uint t)
{
		while(t--);
}

void keyscan()
{
		if(S7==0)
		{
				delay(100);
				if(S7==0)
				{
						if(mode==0)
						{
								mode=1;led=1;
						}
						else if(mode==1||mode==2||mode==3||mode==4)
						{
								led=!led;
						}
						ET1=0;count=0;
						while(!S7);
				}
		}
		else if(S6==0)
		{
				delay(100);
				if(S6==0)
				{
						if(mode==0)mode=1;
						shezhi++;
						if(shezhi==3){shezhi=0;write_eeprom(0x00,time);}
						
						ET1=0;count=0;
						while(!S6);
				}
		}
		if(S5==0)
		{
				delay(100);
				if(S5==0)
				{
					if(shezhi==1)
					{
						if(time<240)
							{
									time+=20;
							}
					}
					else if(shezhi==2)
					{
							if(mode<4)
						mode++;
						if(mode==1)led_wei=0;
						else if(mode==2)led_wei=8;
						else if(mode==3)led_wei=16;
						else if(mode==4)led_wei=20;
					}
					ET1=0;count=0;
					while(!S5);
				}
		}
		else if(S4==0)
		{
				delay(100);
				if(S4==0)
				{
						s4=1;
						if(shezhi!=1&&shezhi!=2)
						{
								shezhi=3;
						}
				}
		}
		
		if(S4==1&&s4==1)
		{
			s4=0;
			if(shezhi==3)shezhi=0;
			else if(shezhi==1)
			{
				
				if(time>80)
					{
							time-=20;
					}
			}
			else if(shezhi==2)
			{
				if(mode>1)
				mode--;
				if(mode==1)led_wei=0;
				else if(mode==2)led_wei=8;
				else if(mode==3)led_wei=16;
				else if(mode==4)led_wei=20;
			}
			ET1=0;count=0;
		}
}

void moshi()
{
		if(led==1)
		{
				ET1=1;
				if(PWM<mm)
							{						
								P2=0x80;
								P0=table[led_wei];						
							}
							else
							{
								P2=0x80;
								P0=0xff;
							}									

							if(PWM > 7)
								PWM = 0;
		}
		else if(led==0)
		{
				ET1=0;
				P2=0x80;P0=0xff;
				P2=0xc0;P0=0xff;P2=0xff;P0=0xff;
		}
}

//=================数码管显示函数=======================
void delayms(uint n)
{
		unsigned int i,j;
		for(i=n;i>0;i--)
			for(j=845;j>0;j--);
}

void display1(uchar yi,uchar er)
{
		P2=0xc0;
		P0=0x01;
		P2=0xff;
		P0=tab[yi];
		delayms(1);
	
		P2=0xc0;
		P0=0x02;
		P2=0xff;
		P0=tab[er];
		delayms(1);
}

void display2(uchar san,uchar si)
{
		P2=0xc0;
		P0=0x04;
		P2=0xff;
		P0=tab[san];
		delayms(1);
	
		P2=0xc0;
		P0=0x08;
		P2=0xff;
		P0=tab[si];
		delayms(1);
}

void display3(uchar wu,uchar liu)
{
		P2=0xc0;
		P0=0x10;
		P2=0xff;
		P0=tab[wu];
		delayms(1);
	
		P2=0xc0;
		P0=0x20;
		P2=0xff;
		P0=tab[liu];
		delayms(1);
}

void display4(uchar qi,uchar ba)
{
		P2=0xc0;
		P0=0x40;
		P2=0xff;
		P0=tab[qi];
		delayms(1);
		
		P2=0xc0;
		P0=0x80;
		P2=0xff;
		P0=tab[ba];
		delayms(1);
	
		P2=0xc0;
		P0=0xff;
		P2=0xff;
		P0=0xff;
		delayms(1);
}

void smg_display(uchar n)
{
	ET0=1;
	
	U=ADC_pcf8591();
	if( U<64) mm=2;
	else if( U>63 && U<126) mm=4;
	else if( U>125 && U<188) mm=6;
	else if( U>187 && U<255) mm=8;
		
	switch(n)
	{
		case 0:
			yi=11;er=11;san=11;si=11;wu=11;liu=11;qi=11;ba=11;
		break;
		
		case 1:
			if(fla==1)
					{yi=10;er=mode;san=10;si=11;
					if((time*5/1000%10)==0)wu=11;
					else wu=time*5/1000%10;
					liu=time*5/100%10;qi=time*5/10%10;ba=time*5%10;}
			else 
					{yi=10;er=mode;san=10;si=11;wu=11;liu=11;qi=11;ba=11;}
		break;
					
		case 2:
			if(fla==1)
					{yi=10;er=mode;san=10;si=11;
					if((time*5/1000%10)==0)wu=11;
					else wu=time*5/1000%10;
					liu=time*5/100%10;qi=time*5/10%10;ba=time*5%10;}
			else {yi=11;er=11;san=11;si=11;
					if((time*5/1000%10)==0)wu=11;
					else wu=time*5/1000%10;
					liu=time*5/100%10;qi=time*5/10%10;ba=time*5%10;}
		break;
					
		case 3:
			yi=11;er=11;san=11;si=11;wu=11;liu=11;qi=11;ba=mm/2;
		break;
					
	}
}

void TimerInit(void)		//定时器1为5毫秒   定时器2为1毫秒@11.0592MHz
{
	AUXR |= 0xC0;		//定时器时钟1T模式
	TMOD &= 0xFF;		//设置定时器模式
	TL0 = 0xCD;		//设置定时初值
	TH0 = 0xD4;		//设置定时初值
	TF0 = 0;		//清除TF0标志
	TR0 = 1;		//定时器0开始计时
	
	TL1 = 0x00;		//设置定时初值
	TH1 = 0x28;		//设置定时初值
	TF1 = 0;		//清除TF1标志
	TR1 = 1;		//定时器1开始计时
	
	ET1 = 1;
	ET0 = 1;
	EA = 1;
}

void ServiceTimer0() interrupt 1
{

		
		tt++;
		if(tt==800)
		{
				tt=0;
				fla=!fla;
		}
		pwm_tt++;
		if(pwm_tt==5)
		{
		PWM++;pwm_tt=0;
		}
}

void ServiceTimer1() interrupt 3
{
		count++;
		if(count>=time)
		{
				count=0;
				led_wei++;
				if(led_wei==24)led_wei=0;
		}
}

void main()
{
		InitSystem();
		TimerInit();
		Init_pcf8591();
		yi=11;er=11;san=11;si=11;wu=11;liu=11;qi=11;ba=11;
		time=read_eeprom(0x00);
		while(1)
		{
				moshi();
				smg_display(shezhi);
				keyscan();
			
				display1(yi,er);
				display2(san,si);
				display3(wu,liu);
				display4(qi,ba);
		}
}