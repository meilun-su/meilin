#include <REGX52.H>
#include "DEPUTY.H"

sbit DQ = P1^4;
void Delay_OneWire(unsigned int t)  
{
	while(t--);
}

//??????DS18B20?????
void Write_DS18B20(unsigned char dat)
{
	unsigned char i;
	for(i=0;i<8;i++)
	{
		DQ = 0;
		DQ = dat&0x01;
		Delay_OneWire(50);
		DQ = 1;
		dat >>= 1;
	}
	Delay_OneWire(50);
}

//?DS18B20??????
unsigned char Read_DS18B20(void)
{
	unsigned char i;
	unsigned char dat;
  
	for(i=0;i<8;i++)
	{
		DQ = 0;
		dat >>= 1;
		DQ = 1;
		if(DQ)
		{
			dat |= 0x80;
		}	    
		Delay_OneWire(50);
	}
	return dat;
}

//DS18B20?????
bit init_ds18b20(void)
{
  	bit initflag = 0;
  	
  	DQ = 1;
  	Delay_OneWire(120);
  	DQ = 0;
  	Delay_OneWire(800);
  	DQ = 1;
  	Delay_OneWire(100); 
    initflag = DQ;     
  	Delay_OneWire(50);
  
  	return initflag;
}

unsigned char get_wendu(void)
{
	unsigned char low,high,temp;
	init_ds18b20();
	Write_DS18B20(0xcc);
	Write_DS18B20(0x44);
	Delay_OneWire(200);
	
	init_ds18b20();
	Write_DS18B20(0xcc);
	Write_DS18B20(0xBE);
	
	low=Read_DS18B20();
	high=Read_DS18B20();
	
	temp=high << 4;
	temp|=(low >> 4);
}
