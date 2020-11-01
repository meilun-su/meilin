package com.meilun.su;
/**
 * ��������
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class Server extends JFrame {

private static final long serialVersionUID = 1L;
private ServerSocket serveSocket;
private ServerThread serverThread;
private ArrayList<ClientThread> clients;
	
private JFrame frame; //���������
private JTextField jf_ServerIP;//������IP��ַ
private JTextArea jf;
private JTextField jf_message;
private JTextField jf_max;
private JTextField jf_port;
private JButton jb_start;//������ť
private JButton jb_send;//���Ͱ�ť
private JButton jb_stop;//ֹͣ��ť
private JLabel j1;
private JPanel northPanle;//������Ϣ���
private JPanel southPanle;//д��Ϣ���
private JScrollPane leftPanle;//�����û���������
private JScrollPane rightPanle;//��Ϣ��ʾ����������
private JSplitPane centerSplit;

private JList<String> userList;
private DefaultListModel<String> listModel;

private boolean isStart = false;

//	������
public static void main(String[] args) {
	new Server();

}
//���캯��
public Server(){
	frame=new JFrame("������");
	
	jf=new JTextArea();
	jf.setEditable(false);
	jf.setForeground(Color.blue);
	jf_message=new JTextField();
	jf_ServerIP = new JTextField("0.0.0.0");
	jf_max = new JTextField("30");
	jf_port=new JTextField("8888");
	jb_start=new JButton("����");
	jb_stop=new JButton("ֹͣ");
	jb_send=new JButton("����");
	listModel=new DefaultListModel<String>();
	userList=new JList<String>(listModel);
	
	southPanle=new JPanel(new BorderLayout());
	j1 = new JLabel("����������");//��������������ǩ

	southPanle.add(j1,BorderLayout.NORTH);
	southPanle.setBorder(new TitledBorder("��Ϣ"));
	southPanle.add(jf_message,BorderLayout.CENTER);
	southPanle.add(jb_send,BorderLayout.EAST);//ʹ������ťλ�����������
	
	leftPanle=new JScrollPane(userList);
	leftPanle.setBorder(new TitledBorder("�����û�"));
	
	rightPanle=new JScrollPane(jf);
	rightPanle.setBorder(new TitledBorder("��Ϣ��ʾ��"));
	
	centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			rightPanle, leftPanle);//ʵ���������»���
	centerSplit.setDividerLocation(600);//����һ����������
	
	northPanle=new JPanel();
	northPanle.setLayout(new GridLayout(1,8));//���񲼾�
	northPanle.add(new JLabel("IP��ַ:"));//��ӡ�IP��ַ����ǩ
	northPanle.add(jf_ServerIP);//���IP��ַ��
	northPanle.add(new JLabel("��������:"));//��ӡ��������ޡ���ǩ
	northPanle.add(jf_max);//����������޿�
	northPanle.add(new JLabel("�˿�:"));//��ӡ��˿ڡ���ǩ
	northPanle.add(jf_port);//��Ӷ˿ڿ�
	northPanle.add(jb_start);//���������ť
	northPanle.add(jb_stop);//���ֹͣ��ť
	northPanle.setBorder(new TitledBorder("������Ϣ"));
	
	frame.setLayout(new BorderLayout());
	frame.add(northPanle, BorderLayout.NORTH);//����northPanle��������������Ķ���
	frame.add(centerSplit, BorderLayout.CENTER);//����centerSplit����������������м�
	frame.add(southPanle, BorderLayout.SOUTH);//����southPanle��������������ĵײ�
	frame.setSize(800, 500);//���ڴ�С
	frame.setVisible(true);
	
	try {
		String style = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		UIManager.setLookAndFeel(style);
		// ���´�����ʽ
		SwingUtilities.updateComponentTreeUI(this.frame);
		} catch (Exception e) {
		e.printStackTrace();
		}
	// ʹ���ڳ�������Ļ������
	int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
	frame.setLocation((screen_width - frame.getWidth()) / 2,
			(screen_height - frame.getHeight()) / 2);
	frame.setVisible(true);


	// �رմ���ʱ�¼�
	frame.addWindowListener(new WindowAdapter() {
		
		public void windowClosing(WindowEvent e) {
			
			//��������������¼������"D:\\Server_copy.txt"·���������¼�����ı��ĵ�
			String fileName = "D:\\Server_copy.txt";
			try {
				FileWriter writer =new FileWriter(fileName,true);
				writer.write(jf.getText()+"\r\n\r\n");
				writer.close();
			} catch (IOException e1) {
				// TODO: handle exception
			}
			
			if (isStart) {
				closeServer();// �رշ�����
			}
			System.exit(0);// �˳�����

		}
	});
	// �ı��򰴻س���ʱ�¼�
	jf_message.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			send();
		}
	});
	
	@SuppressWarnings("unused")
	int port=Integer.parseInt(jf_port.getText());
	//	�����˿��¼���������������
	jb_start.addActionListener(new ActionListener() {
		
	@Override
	public void actionPerformed(ActionEvent e) {
	if(isStart){
	JOptionPane.showMessageDialog(frame, "�������Ѵ�������״̬","����",JOptionPane.ERROR_MESSAGE);
	return;
	}
	int port ;
	try{
		try{
		port= Integer.parseInt(jf_port.getText());
		}catch(Exception e1){
		throw new Exception("�˿ں� Ϊ��������");
		}
		if (port<= 0) {
		throw new Exception("�˿ں� Ϊ��������");
		
						}
						
						serverStart(port);
						jf.append("������������,IP��ַΪ:" + jf_ServerIP.getText() +" " +"�˿�:"+port+",�ȴ��ͻ���������...\r\n");
						JOptionPane.showMessageDialog(frame, "�������ɹ�����!");
						jb_start.setEnabled(false);
						jf_port.setEnabled(false);
						jb_stop.setEnabled(true);
				} catch (Exception ee) {
		JOptionPane.showMessageDialog(frame, ee.getMessage(),"����",JOptionPane.ERROR_MESSAGE);
				}
					
			}

		});
	// ����ֹͣ��������ťʱ�¼�
		jb_stop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!isStart){
						JOptionPane.showMessageDialog(frame, "��������δ������", "����",JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						closeServer();
						jb_start.setEnabled(true);
						jf_port.setEnabled(true);
						jb_stop.setEnabled(false);
						jf.append("�������ɹ�ֹͣ!\r\n");
						JOptionPane.showMessageDialog(frame, "�������Ѿ�ֹͣ��");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(frame, "ֹͣ�����������쳣��", "����",
								JOptionPane.ERROR_MESSAGE);
					}
					//��������������¼������"D:\\Server_copy.txt"·���������¼�����ı��ĵ�
					String fileName = "D:\\Server_copy.txt";
					try {
						FileWriter writer =new FileWriter(fileName,true);
						writer.write(jf.getText()+"\r\n\r\n");
						writer.close();
					} catch (IOException e1) {
						// TODO: handle exception
					}
				}
			});
			// �������Ͱ�ťʱ�¼�
			jb_send.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					send();
					
					//��������������¼������"D:\\Server.txt"�ı��ĵ�
					String fileName = "D:\\Server.txt";
					try {
						FileWriter writer =new FileWriter(fileName);
						writer.write(jf.getText()+"\r\n");
						writer.close();
					} catch (IOException e) {
						// TODO: handle exception
					}
				}
			});
				
}




	//����������
	public void serverStart(int port)throws java.net.BindException {
		try {
			clients=new ArrayList<ClientThread>();
			serveSocket = new ServerSocket(port);
			serverThread = new ServerThread(serveSocket);
			serverThread.start();
			isStart=true;
		}catch(BindException e){
			isStart=false;
			throw new BindException("�˿ں��ѱ�ռ�ã���һ��");
		}
		catch (Exception e) {
			e.printStackTrace();
			isStart=false;
			throw new BindException("�����������쳣");
		}
		
	}
	// �رշ�����
		@SuppressWarnings("deprecation")
	public void closeServer() {
			try {
				if (serverThread != null)
					serverThread.stop();// ֹͣ�������߳�
	 
				for (int i = clients.size() - 1; i >= 0; i--) {
					// �����������û����͹ر�����
					clients.get(i).getWriter().println("CLOSE");
					clients.get(i).getWriter().flush();
					// �ͷ���Դ
					clients.get(i).stop();// ֹͣ����Ϊ�ͻ��˷�����߳�
					clients.get(i).reader.close();
					clients.get(i).writer.close();
					clients.get(i).socket.close();
					clients.remove(i);
				}
				if (serveSocket != null) {
					serveSocket.close();// �رշ�����������
				}
				listModel.removeAllElements();// ����û��б�
				isStart=false;
				} catch (IOException e) {
					e.printStackTrace();
					isStart=true;
			}
		}
	
	// ִ����Ϣ����
	public void send() {
		if(!isStart){
						JOptionPane.showMessageDialog(frame, "������δ���������ܷ�����Ϣ��","����",
							JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (clients.size() == 0) {
						JOptionPane.showMessageDialog(frame, "û���û�����,���ܷ�����Ϣ��", "����",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					String message = jf_message.getText().trim();
					if (message == null || message.equals("")) {
						JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ�գ�", "����",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					sendServerMessage(message);// Ⱥ����������Ϣ
					jf.append(User.getTimeStr()+" "+"��������ʾ��" + jf_message.getText() + "\r\n");//������˵�Ļ���ʾ�ڷ���������
					jf_message.setText(null);
				}
	
	// �Ѻ�̨��Ϣ���͸������ͻ���
	public void sendServerMessage(String message) {
			for (int i = clients.size() - 1; i >= 0; i--) {
				clients.get(i).getWriter().println(User.getTimeStr()+" "+"ϵͳ��ʾ��" + message+"(Ⱥ��)");//��������õ���������͸��ͻ��˽���
				clients.get(i).getWriter().flush();


			}

		}
	
	
	
		 //ÿ�����ӵ��������Ŀͻ��ˣ�������֮��Ӧ��һ���߳������������շ���Ϣ
		 class ClientThread extends Thread{
	
			 Socket socket;
			 BufferedReader reader;
			 PrintWriter writer;
	
			private User user;
			
			public BufferedReader getReader(){
				return reader;
				
			}
			
			public PrintWriter getWriter(){
				return writer;
				
			}
			public User getUser(){
				return user;
				
			}
			
			//ÿ���ͻ��˶�Ӧһ���ͻ����̴߳���
			public ClientThread(Socket socket){
				try {
					this.socket=socket;
					reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
					writer=new PrintWriter(socket.getOutputStream());
					//���տͻ��˵Ļ�����Ϣ
					String line=reader.readLine();
					String[] st=line.split("@");
					
	
					user=new User(st[0],st[1]);
					//�������ӳɹ���Ϣ
					writer.println("ϵͳ��ʾ��"+user.getName()+user.getIp()+"����������ӳɹ�!");
					//System.out.println(user.getName()+".."+user.getIp());
					  //������ǰ�û���Ϣ
					if(clients.size()>0){
						String temp="";
						for(int i=clients.size()-1;i>=0;i--){
							temp+=(clients.get(i).getUser().getName()+"/"+clients.get(i).getUser().getIp())+"@";
							
						}
						
						writer.println("USERLIST@"+clients.size()+"@"+temp);
						writer.flush();
						
					}
					System.out.println(st[0]+",��������ʾ����"+st[1]);
					//�����������û����͸��û���������,���������ߵ��û�����������û��б���
					for(int i=clients.size()-1;i>=0;i--){
						clients.get(i).getWriter().println("ADD@"+user.getName()+"@"+user.getIp());
						clients.get(i).getWriter().flush();
					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				
			}
			@SuppressWarnings("deprecation")
			public void run(){//���Ͻ��ܿͻ��˵���Ϣ���д���
				String message=null;
				while(true){
					try {
					  message=reader.readLine();//���տͻ�����Ϣ
				
						if (message.equals("CLOSE"))// ��������
						{
							 jf.append(this.getUser().getName()+ this.getUser().getIp() + "����!\r\n");
							 
							// �Ͽ������ͷ���Դ
							reader.close();
							writer.close();
							socket.close();
	
							// �����������û����͸��û�����������
							for (int i = clients.size() - 1; i >= 0; i--) {
								clients.get(i).getWriter().println("DELETE@" + user.getName());
							clients.get(i).getWriter().flush();
							}
	
							listModel.removeElement(user.getName());// ���������б�
							j1.setText("Ŀǰ��������"+userList.getModel().getSize()+"��");//������������

							// ɾ�������ͻ��˷����߳�
							for (int i = clients.size() - 1; i >= 0; i--) {
								if (clients.get(i).getUser() == user) {
									ClientThread temp = clients.get(i);
									clients.remove(i);// ɾ�����û��ķ����߳�
									temp.stop();// ֹͣ���������߳�
									return;
								}
							}
							
						}else{
							dispatcherMessage(message);// ת����Ϣ
						}
	
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
			}
	
			private void dispatcherMessage(String message) {
	
	
				String[] parts=message.split("@");
				String string=parts[1]+"��"+parts[2]+"˵:"+parts[3];
				if (parts[0].equals("TOALL")) {// Ⱥ��
					for (int i = clients.size() - 1; i >= 0; i--) {
						clients.get(i).getWriter().println("TOALL@"+string);
						clients.get(i).getWriter().flush();
						//System.out.println("Ⱥ����Ϣ����");
					}
					jf.append(User.getTimeStr()+" "+string + "\r\n");
				}
				if(parts[0].equals("TOONE")){//˽��
					for(int i=0;i<clients.size();i++){
						if(parts[2].equals((clients.get(i).getUser().getName())))
						{  
							string=parts[1]+"����˵:"+parts[3]+"��˽�ģ�";
							clients.get(i).getWriter().println("TOONE@"+string);
							clients.get(i).getWriter().flush();
							//System.out.println("˽����Ϣ����");
						}
					}
						jf.append(User.getTimeStr()+" "+ parts[1]+"��"+parts[2]+"˵:"+parts[3]+ "��˽�ģ�\r\n");

		}	
				//��������������¼������"D:\\Server.txt"�ı��ĵ�
				String fileName = "D:\\Server.txt";
				try {
					FileWriter writer =new FileWriter(fileName);
					writer.write(jf.getText()+"\r\n");
					writer.close();
				} catch (IOException e) {
					// TODO: handle exception
				}
	}	
 }
	
	//	�������߳�
	class ServerThread extends Thread {
	private ServerSocket serverSocket;
	
				// �������̵߳Ĺ��췽��
				public ServerThread(ServerSocket serverSocket) {
					this.serverSocket = serverSocket;
					
				}
	
				public void run() {
					while (true) {// ��ͣ�ĵȴ��ͻ��˵�����
						try {
							Socket socket = serverSocket.accept();
							ClientThread client = new ClientThread(socket);
							client.start();// �����Դ˿ͻ��˷�����߳�
							clients.add(client);
							listModel.addElement(client.getUser().getName());// ���������б�
							jf.append(client.getUser().getName()+ client.getUser().getIp() + "����!\r\n");
						}catch (IOException e) {
							e.printStackTrace();
					}
						j1.setText("Ŀǰ��������"+userList.getModel().getSize()+"��");//������������

				}
			}
		}
}