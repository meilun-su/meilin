#include "main.h"
#include "mqtt.h"
#include "kaiguan.h"

//code uint8_t  PRODUCTKEY[]={"a1z6Igb2mrC"};                                 //��ƷID
//#define  PRODUCTKEY_LEN		(sizeof(PRODUCTKEY)-1)                            //��ƷID����
//code uint8_t  DEVICENAME[]={"my_switch"};                                  //�豸��  
//#define  DEVICENAME_LEN       (sizeof(DEVICENAME)-1)                            //�豸������
//code uint8_t  DEVICESECRE[]={"5e3eccdb76b47ee913a51fab8b29c758"};            //�豸��Կ   
//#define  DEVICESECRE_LEN      (sizeof(DEVICESECRE)-1)                           //�豸��Կ����

code uint8_t  TOPIC_Subscribe[]={"/a1deiQK0vng/hardwaresu/user/servertohardware"};			//��Ҫ���ĵ�����  (�����´�)
#define SUBSCRIBE_LEN (sizeof(TOPIC_Subscribe)-1)
code uint8_t  TOPIC_Publish[]={"/a1deiQK0vng/hardwaresu/user/hardwaretoserver"};        //��Ҫ����������   (�����ϴ�)
#define PUBLISH_LEN (sizeof(TOPIC_Publish)-1)

code uint8_t CLIENTID[]={"sjj1|securemode=3,signmethod=hmacsha1|"};			//�ͻ�ID
#define CLIENTID_LEN (sizeof(CLIENTID)-1)

code uint8_t USERNAME[]={"hardwaresu&a1deiQK0vng"};			//�̶���ʽ   ${YourDeviceName}&${YourProductKey}
#define USERNAME_LEN (sizeof(USERNAME)-1)

code uint8_t PASSWARD[]={"497A5E4C33A2A423467A53871E4F6291407D2B90"};	//���룬��Ҫmqttǩ�����߼���
#define PASSWARD_LEN (sizeof(PASSWARD)-1)

#define FIXED_LEN		2		//���ӱ����У��̶���ͷ����=2
#define VARIABLE_LEN 	10		//���ӱ����У��ɱ䱨ͷ����=10

uint8_t Payload_len;
uint8_t ClientID_len,Username_len,Passward_len;
xdata uint8_t MQTT_Buff[200];

/*----------------------------------------------------------*/
/*�����������ӷ���������                                    */
/*��  ������                                                */
/*����ֵ����                                                */
/*----------------------------------------------------------*/
void MQTT_ConectPack(void)
{	
//	Fixed_len = 2;                                                        //���ӱ����У��̶���ͷ����=2
//	Variable_len = 10;                                                    //���ӱ����У��ɱ䱨ͷ����=10
	
	Payload_len = 2 + CLIENTID_LEN + 2 + USERNAME_LEN + 2 + PASSWARD_LEN; //�ܱ��ĳ���       
	
	MQTT_Buff[0]=0x10;                              //��1���ֽ� ���̶�0x01	
	MQTT_Buff[1]=VARIABLE_LEN + Payload_len;        //��2���ֽ� ���ɱ䱨ͷ+��Ч���ɵĳ���
	MQTT_Buff[2]=0x00;          				    //��3���ֽ� ���̶�0x00	            
	MQTT_Buff[3]=0x04;                              //��4���ֽ� ���̶�0x04
	MQTT_Buff[4]=0x4D;								//��5���ֽ� ���̶�0x4D
	MQTT_Buff[5]=0x51;								//��6���ֽ� ���̶�0x51
	MQTT_Buff[6]=0x54;								//��7���ֽ� ���̶�0x54
	MQTT_Buff[7]=0x54;								//��8���ֽ� ���̶�0x54
	MQTT_Buff[8]=0x04;								//��9���ֽ� ���̶�0x04
	MQTT_Buff[9]=0xC2;								//��10���ֽڣ�ʹ���û���������У�飬��ʹ���������������Ự
	MQTT_Buff[10]=0x00; 						    //��11���ֽڣ�����ʱ����ֽ� 0x00
	MQTT_Buff[11]=0x3c;								//��12���ֽڣ�����ʱ����ֽ� 0x64   100s
	
	/*     CLIENT_ID      */
	MQTT_Buff[12] = 0;//CLIENTID_LEN/256;                			  			//�ͻ���ID���ȸ��ֽ�
	MQTT_Buff[13] = CLIENTID_LEN%256;               			  			//�ͻ���ID���ȵ��ֽ�
	memcpy(&MQTT_Buff[14],CLIENTID,CLIENTID_LEN);                 			//���ƹ����ͻ���ID�ִ�	
	/*     �û���        */
	MQTT_Buff[14+CLIENTID_LEN] = 0;//USERNAME_LEN/256; 				  		    //�û������ȸ��ֽ�
	MQTT_Buff[15+CLIENTID_LEN] = USERNAME_LEN%256; 				 		    //�û������ȵ��ֽ�
	memcpy(&MQTT_Buff[16+CLIENTID_LEN],USERNAME,USERNAME_LEN);              //���ƹ����û����ִ�	
	/*      ����        */
	MQTT_Buff[16+CLIENTID_LEN+USERNAME_LEN] = 0;//PASSWARD_LEN/256;			    //���볤�ȸ��ֽ�
	MQTT_Buff[17+CLIENTID_LEN+USERNAME_LEN] = PASSWARD_LEN%256;			    //���볤�ȵ��ֽ�
	memcpy(&MQTT_Buff[18+CLIENTID_LEN+USERNAME_LEN],PASSWARD,PASSWARD_LEN); //���ƹ��������ִ�

	MQTT_sendBuff(MQTT_Buff, FIXED_LEN + VARIABLE_LEN + Payload_len);      //���뷢�����ݻ�����
}
/*----------------------------------------------------------*/
/*��������SUBSCRIBE����topic����                            */
/*��  ����QoS�����ĵȼ�                                     */
/*��  ����topic_name������topic��������                     */
/*����ֵ����                                                */
/*----------------------------------------------------------*/
void MQTT_Subscribe(void)
{	
	//Fixed_len = 2;                              //SUBSCRIBE�����У��̶���ͷ����=2
	//Variable_len = 2;                           //SUBSCRIBE�����У��ɱ䱨ͷ����=2	
	Payload_len = 2 + SUBSCRIBE_LEN + 1;   //������Ч���ɳ��� = 2�ֽ�(topic_name����)+ topic_name�ַ����ĳ��� + 1�ֽڷ���ȼ�
	
	MQTT_Buff[0]=0x82;                                    //��1���ֽ� ���̶�0x82                      
	MQTT_Buff[1]=VARIABLE_LEN + Payload_len;              //��2���ֽ� ���ɱ䱨ͷ+��Ч���ɵĳ���	
	MQTT_Buff[2]=0x00;                                    //��3���ֽ� �����ı�ʶ�����ֽڣ��̶�ʹ��0x00
	MQTT_Buff[3]=0x01;		                              //��4���ֽ� �����ı�ʶ�����ֽڣ��̶�ʹ��0x01
	MQTT_Buff[4]=SUBSCRIBE_LEN/256;                  //��5���ֽ� ��topic_name���ȸ��ֽ�
	MQTT_Buff[5]=SUBSCRIBE_LEN%256;		          //��6���ֽ� ��topic_name���ȵ��ֽ�
	memcpy(&MQTT_Buff[6],TOPIC_Subscribe,SUBSCRIBE_LEN);  //��7���ֽڿ�ʼ �����ƹ���topic_name�ִ�		
	MQTT_Buff[6+SUBSCRIBE_LEN]=0;                  //���1���ֽڣ����ĵȼ�Ϊ0
	
	MQTT_sendBuff(MQTT_Buff, FIXED_LEN + VARIABLE_LEN + Payload_len);  //���뷢�����ݻ�����
}

/*----------------------------------------------------------*/
/*���������ȼ�0 ������Ϣ����                                */
/*��  ����topic_name��topic����                             */
/*��  ����data������                                        */
/*��  ����data_len�����ݳ���                                */
/*����ֵ����                                                */
/*----------------------------------------------------------*/
//xdata uint8_t Json_WenduTab[]={"{\"temperature\":25}"};
//#define Json_WenduTab_Len (sizeof(Json_WenduTab)-1)
//xdata uint8_t Json_LightTab[]={"{\"light\":125}"};
//#define Json_LightTab_Len (sizeof(Json_LightTab)-1)

xdata uint8_t Json_Tab[]={"{\"temperature\":25,\"light\":125}"};
#define Json_Tab_Len (sizeof(Json_Tab)-1)

void MQTT_PublishQs0(uint8_t wendu, uint8_t guang)
{	
	uint8_t Variable_len;
	//Fixed_len = 2;                             //�̶���ͷ���ȣ�2�ֽ�
	Variable_len = 2 + PUBLISH_LEN;          //�ɱ䱨ͷ���ȣ�2�ֽ�(topic����)+ topic�ַ����ĳ���
	Payload_len = Json_Tab_Len;                    //��Ч���ɳ��ȣ�����data_len
	
	MQTT_Buff[0]=0x30;                         //��1���ֽ� ���̶�0xC0                
	MQTT_Buff[1]=Variable_len + Payload_len;   //��2���ֽ� ���ɱ䱨ͷ+��Ч���ɵĳ���	
	MQTT_Buff[2]=PUBLISH_LEN/256;            //��3���ֽ� ��topic���ȸ��ֽ�
	MQTT_Buff[3]=PUBLISH_LEN%256;		       //��4���ֽ� ��topic���ȵ��ֽ�
	memcpy(&MQTT_Buff[4],TOPIC_Publish,PUBLISH_LEN); //��5���ֽڿ�ʼ ������topic�ַ���	
	
	Json_Tab[15]=wendu/10%10+'0';
	Json_Tab[16]=wendu%10+'0';
	
	Json_Tab[26]=guang/100%10+'0';
	Json_Tab[27]=guang/10%10+'0';
	Json_Tab[28]=guang%10+'0';
	
	memcpy(&MQTT_Buff[4+PUBLISH_LEN],Json_Tab,Json_Tab_Len);   //����data����
	
//	Json_WenduTab[15]=wendu/10%10+'0';
//	Json_WenduTab[16]=wendu%10+'0';
//	
//	Json_LightTab[9]=guang/100%10+'0';
//	Json_LightTab[10]=guang/10%10+'0';
//	Json_LightTab[11]=guang%10+'0';
	
	
	
	//memcpy(&MQTT_Buff[4+PUBLISH_LEN+Json_WenduTab_Len],Json_LightTab,Json_LightTab_Len);
	
	MQTT_sendBuff(MQTT_Buff, FIXED_LEN + Variable_len + Payload_len);  //���뷢�����ݻ�����
}
/*----------------------------------------------------------*/
/*����������������������ĵȼ�0������                       */
/*��  ����redata�����յ�����                                */
/*����ֵ����                                                */
/*----------------------------------------------------------*/
void MQTT_DealPushdata_Qs0(unsigned char *redata)
{
	//uint8_t  re_len;               	           //����һ����������Ž��յ������ܳ���
	//uint8_t  pack_num;                         //����һ�����������������һ�����ʱ���������͵ĸ���
    //uint8_t  temp_len;                         //����һ���������ݴ�����
    uint8_t  totle_len;                        //����һ������������Ѿ�ͳ�Ƶ����͵���������
	uint8_t  topic_len;              	       //����һ���������������������ĳ���
	//uint8_t  cmd_len;                          //����һ����������������а������������ݵĳ���
	//uint8_t  cmd_loca;                         //����һ����������������а������������ʼλ��
	//uint8_t  i;                                //����һ������������forѭ��
	xdata uint8_t temp[64];	       //��ʱ������
	//uint8_t *data1;                   //redata������ʱ�򣬵�һ���ֽ�������������data����ָ��redata�ĵ�2���ֽڣ����������ݿ�ʼ�ĵط�
		
	if(redata[0]!=0x30)return;			//�������������ݵ��豸�Ĺ̶���ͷ
	totle_len =	redata[1];
	topic_len = redata[3];				//Topic����С��256ʱ��ֱ�Ӷ�ȡ��λ��ֵ
	memcpy(temp,&redata[2+2+topic_len],totle_len-1-topic_len);
	if(strstr(temp,"{\"switch\":1}"))	//�򿪿���
	{
		//switch_mode=1;
		switch_open();
	}
	else if(strstr(temp,"{\"switch\":0}"))	//�رտ���
	{
		//switch_mode=0;
		switch_close();
	}
	
	
//	re_len = redata[0];                                   			//��ȡ���յ������ܳ���
//	data1 = &redata[1];                                              //dataָ��redata�ĵ�2���ֽڣ����������ݿ�ʼ�� 
//	pack_num = temp_len = totle_len = 0;                			//������������
//	do{
//		pack_num++;                                     			//��ʼѭ��ͳ�����͵ĸ�����ÿ��ѭ�����͵ĸ���+1
//		temp_len = data1[1+totle_len]+2;                 			//���㱾��ͳ�Ƶ����͵����ݳ���
//		totle_len += temp_len;                          			//�ۼ�ͳ�Ƶ��ܵ����͵����ݳ���
//		re_len -= temp_len ;                            			//���յ������ܳ��� ��ȥ ����ͳ�Ƶ����͵��ܳ���      
//	}while(re_len!=0);                                  			//������յ������ܳ��ȵ���0�ˣ�˵��ͳ�������
//	//if(pack_num!=1)u1_printf("���ν�����%d����������\r\n",pack_num);//���������Ϣ
//	temp_len = totle_len = 0;                		            	//������������
//	for(i=0;i<pack_num;i++){                                        //�Ѿ�ͳ�Ƶ��˽��յ����͸�������ʼforѭ����ȡ��ÿ�����͵����� 
//		topic_len = data1[2+totle_len]*256+data1[3+totle_len] + 2;    //���㱾����������������ռ�õ�������
//		cmd_len = data1[1+totle_len]-topic_len;                      //���㱾��������������������ռ�õ�������
//		cmd_loca = totle_len + 2 +  topic_len;                      //���㱾�������������������ݿ�ʼ��λ��
//		memcpy(temp,&data1[cmd_loca],cmd_len);                       //�������ݿ�������		                 
//		CMDBuf_Deal(temp, cmd_len);                                 //�������������
//		temp_len = data1[1+totle_len]+2;                             //��¼�������͵ĳ���
//		totle_len += temp_len;                                      //�ۼ��Ѿ�ͳ�Ƶ����͵����ݳ���
//	}	
}
/*----------------------------------------------------------*/
/*��������PING���ģ�������                                  */
/*��  ������                                                */
/*����ֵ����                                                */
/*----------------------------------------------------------*/
void MQTT_PingREQ(void)
{
	MQTT_Buff[0]=0xC0;              //��1���ֽ� ���̶�0xC0                      
	MQTT_Buff[1]=0x00;              //��2���ֽ� ���̶�0x00 

	MQTT_sendBuff(MQTT_Buff, 2);   //�������ݵ�������
}
/*----------------------------------------------------------*/
/*������������ƽ̨�Ͽ�����                                  */
/*��  ������                                                */
/*����ֵ����                                                */
/*----------------------------------------------------------*/
void MQTT_disconect(void)
{
	MQTT_Buff[0]=0xe0;              //��1���ֽ� ���̶�0xC0                      
	MQTT_Buff[1]=0x00;              //��2���ֽ� ���̶�0x00 

	MQTT_sendBuff(MQTT_Buff, 2);   //�������ݵ�������
}


//void memcpy(uint8_t *targetData,uint8_t *sourceData,uint8_t len)
//{
//	uint8_t i;
//	for(i=0;i<len;i++)
//	{
//		targetData[i] = sourceData[i];
//	}
//}



