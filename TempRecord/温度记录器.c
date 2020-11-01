#include <REGX52.H>
#include "DEPUTY.H"
unsigned char shuma[] = {0xc0,0xf9,0xa4,0xb0,0x99,0x92,0x82,0xf8,0x80,0x90,0xbf,0xff,0xc6}; 
unsigned char yi,er,san,si,wu,liu,qi,ba;
unsigned char model,time;//三个模式，定时时间
unsigned int count;//定时器计数
extern unsigned char shijian[]; //时钟时间继承
extern unsigned char set_time[]; //
unsigned char aa,xx;//记录温度记录次数和是否第一次计数
unsigned char wendu[];  //温度计数存放
unsigned char i;//温度显示计数

sfr AUXR = 0x8e;
sbit S7=P3^0;
sbit S6=P3^1;
sbit S5=P3^2;
sbit S4=P3^3;


void Delay(unsigned int t)
{
	while(t--);
}

void display()
{
	P2 = 0xc0;
	P0 = 0x01;
	P2 = 0xff;
	P0 = shuma[yi];
	Delay(100);
	
	P2 = 0xc0;
	P0 = 0x02;
	P2 = 0xff;
	P0 = shuma[er];
	Delay(100);
	
	P2 = 0xc0;
	P0 = 0x04;
	P2 = 0xff;
	P0 = shuma[san];
	Delay(100);
	
	P2 = 0xc0;
	P0 = 0x08;
	P2 = 0xff;
	P0 = shuma[si];
	Delay(100);
	
	P2 = 0xc0;
	P0 = 0x10;
	P2 = 0xff;
	P0 = shuma[wu];
	Delay(100);
	
	P2 = 0xc0;
	P0 = 0x20;
	P2 = 0xff;
	P0 = shuma[liu];
	Delay(100);
	
	P2 = 0xc0;
	P0 = 0x40;
	P2 = 0xff;
	P0 = shuma[qi];
	Delay(100);
	
	P2 = 0xc0;
	P0 = 0x80;
	P2 = 0xff;
	P0 = shuma[ba];
	Delay(100);
}

void InitTimer0() //100ms,定时器1t
{
	AUXR |= 0x80;
	TMOD &= 0xF0;
	TH0 = 0xAE;
	TL0 = 0xFB;
	
	TR0 = 1;
	TF0 = 0;
}

void ServiceTimer0() interrupt 1
{
	count++;
}
void jilu_wendu()
{
	if(time==1)
	{
		if(aa==0&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==1&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==2&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==3&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==4&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==5&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==6&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==7&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==8&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==9&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
	}
	if(time==5)
	{
		if(aa==0&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==1&&count==50){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==2&&count==50){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==3&&count==50){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==4&&count==50){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==5&&count==50){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==6&&count==50){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==7&&count==50){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==8&&count==50){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==9&&count==50){wendu[aa]=get_wendu();count=0;aa++;}
	}
	if(time==30)
	{
		if(aa==0&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==1&&count==300){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==2&&count==300){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==3&&count==300){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==4&&count==300){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==5&&count==300){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==6&&count==300){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==7&&count==300){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==8&&count==300){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==9&&count==300){wendu[aa]=get_wendu();count=0;aa++;}
	}
	if(time==60)
	{
		if(aa==0&&count==10){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==1&&count==600){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==2&&count==600){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==3&&count==600){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==4&&count==600){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==5&&count==600){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==6&&count==600){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==7&&count==600){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==8&&count==600){wendu[aa]=get_wendu();count=0;aa++;}
		else if(aa==9&&count==600){wendu[aa]=get_wendu();count=0;aa++;}
	}
}
void Key()
{
	if(model==0) //模式0，S4按键切换四个时间
	{
		if(S4==0)
		{
			if(time==1){time==5;}
			else if(time==5){time==30;}
			else if(time==30){time==60;}
			else if(time==60){time==1;}
			while(S4==0);
		}
		yi=11,er=11,san=11,si=11,wu=11,liu=10,qi=time/10,ba=time%10;
		if(S5==0){model=1;while(S5==0);}
	}
	else if(model==1)
	{
		EA=1;ET0=1;
		if(xx==0){Init_DS();xx=1;}
		
		jilu_wendu();
		
		Get_DS();
		yi=set_time[2]/10;er=set_time[2]%10;san=10,si=set_time[1]/10;wu=set_time[1]%10;liu=10;qi=set_time[0]/10;ba=set_time[0]%10;
		
		if(aa==10){model=2;aa=0;}
	}
	else if(model==2)
	{
		if(S7==0){model=0;while(S6==0);}
		if(count==5)
			{
				P2=0x80;P0=0xfe;
				if(count==10){count=0;}
			}
		else{P2=0x80;P0=0xff;}
		
		if(S6==0){EA=0;ET0=0;count=0;i++;while(S6==0);}
		if(i==10){i=0;}
		
		yi=10;er=i/10;san=i%10;si=11;wu=11;liu=10;qi=wendu[i]/10;ba=wendu[i]%10;
	}		
}


void allinit()
{
	P2 = 0xc0;
	P0 = 0x00;
	P2 = 0x80;
	P0 = 0xff;
	P2 = 0xc0;
	P0 = 0xff;
	P2 = 0xff;
	P0 = 0xff;
}

void mian()
{
	time=1;
	xx=0;
	InitTimer0();
	allinit();
	while(1)
	{
		Key();
		display();
	}
}