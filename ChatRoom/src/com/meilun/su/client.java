package com.meilun.su;
/**
 * �û���
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;


public class client {
	
/**
 * �û��˽���
 */
	
private JFrame frame;//�ͻ��˴���
private JTextArea textArea;

private JTextField textField;

private JTextField jf_port;//�˿ںſ�
private JTextField jf_Ip;//IP��ַ��
private JTextField jf_name;//�ǳƿ�

private JButton jb_start;//���Ӱ�ť
private JButton jb_stop;//�Ͽ���ť
private JButton jb_send;//���Ͱ�ť
private JButton jb_btnclear;//������ť

private JRadioButton groupchat;//Ⱥ�İ�ť
private JRadioButton privatechat;//˽�İ�ť
private ButtonGroup   buttongroup;//��ť��
private JPanel buttonPanel;//��ť���
private JPanel northPanel;//������Ϣ���
private JPanel southPanel;//������Ϣ���

private JScrollPane rightScroll;//��Ϣ��ʾ����������
private JScrollPane leftScroll;//�����û���������
private JSplitPane centerSplit;


private JList<String> userList;
private DefaultListModel<String> listModel;

private boolean isConnected = false;

private Socket socket;
private PrintWriter writer;
private BufferedReader reader;
private MessageThread messageThread;// ���������Ϣ���߳�
private Map<String, User> onLineUsers = new HashMap<String, User>();// ���������û������Ժ�����˽��


//���캯��
public static void main(String[] args) {
	new client();
}
public client(){
	textArea = new JTextArea();
	textArea.setEditable(false);

	textField = new JTextField();
	jf_port = new JTextField("8888");
	jf_Ip = new JTextField("127.0.0.1");
	jb_start = new JButton("����");
	jb_stop = new JButton("�Ͽ�");
	jb_send = new JButton("����");
	listModel = new DefaultListModel<String>();
	userList = new JList<String>(listModel);
	
	Random rand = new Random();
	jf_name = new JTextField("�Ҵҹ���" + rand.nextInt(100));//��������ǳ�Ϊ�Ҵҹ���1-100���û�
	
	//������Ϣ���
	northPanel = new JPanel();
	northPanel.setLayout(new GridLayout(1, 10));
	northPanel.add(new JLabel("�˿�"));
	northPanel.add(jf_port);
	northPanel.add(new JLabel("������IP"));
	northPanel.add(jf_Ip);
	northPanel.add(new JLabel("�ǳ�"));
	northPanel.add(jf_name);
	northPanel.add(jb_start);
	northPanel.add(jb_stop);
	northPanel.setBorder(new TitledBorder("������Ϣ"));

	//�����û�����Ϣ��ʾ���
	rightScroll = new JScrollPane(textArea);
	rightScroll.setBorder(new TitledBorder("��Ϣ��ʾ��"));
	leftScroll = new JScrollPane(userList);
	leftScroll.setBorder(new TitledBorder("�����û�"));
	centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				  rightScroll, leftScroll);//ʵ���������»���
	centerSplit.setDividerLocation(600);//����һ����������
	
	//Ⱥ��˽�İ�ť��
	groupchat=new JRadioButton("Ⱥ��");
	privatechat=new JRadioButton("˽��");
	privatechat.setSelected(true);
	buttongroup=new ButtonGroup();
	buttongroup.add(groupchat);
	buttongroup.add(privatechat);
	buttonPanel=new JPanel();
	buttonPanel.setLayout(new FlowLayout());
	buttonPanel.add(groupchat);
	buttonPanel.add(privatechat);
	
	//������ť,ʵ����������
	jb_btnclear = new JButton("����");
	jb_btnclear.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent arg0) {
	textArea.setText("");
	}
	});
	buttonPanel.add(jb_btnclear);

	//��Ϣ���
	southPanel = new JPanel(new BorderLayout());
	southPanel.add(buttonPanel, "North");
	southPanel.add(textField, "Center");
	southPanel.add(jb_send, "East");
	southPanel.setBorder(new TitledBorder("��Ϣ"));

	frame = new JFrame("�ͻ���");
	
	frame.setLayout(new BorderLayout());
	
	frame.add(northPanel, "North");
	frame.add(centerSplit, "Center");
	frame.add(southPanel, "South");
	frame.setSize(800, 500);
	frame.setVisible(true);
	try {
		// ���´�����ʽ
		String style = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		UIManager.setLookAndFeel(style);
		SwingUtilities.updateComponentTreeUI(this.frame);
		} catch (Exception e) {
		e.printStackTrace();
		}
	//swing���ڳ�������Ļ����
	int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
	frame.setLocation((screen_width - frame.getWidth()) / 2,
			(screen_height - frame.getHeight()) / 2);
	frame.setVisible(true);

	// д��Ϣ���ı����а��س���ʱ�¼�
	textField.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			send();
		}
	});
			// �������Ͱ�ť�¼�
			jb_send.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					send();
				}
			});
	 
			// �������Ӱ�ť�¼�
			jb_start.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int port;
					if (isConnected) {
						JOptionPane.showMessageDialog(frame, "�Ѵ���������״̬����Ҫ�ظ�����!",
								"����", JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						try {
							port = Integer.parseInt(jf_port.getText().trim());
						} catch (NumberFormatException e1) {
							throw new Exception("�˿ںŲ�����Ҫ��!�˿�Ϊ����!");
						}
						String hostIp = jf_Ip.getText().trim();
						String name = jf_name.getText().trim();
						if (name.equals("") || hostIp.equals("")) {
							throw new Exception("�ǳơ�������IP����Ϊ��!");
						}
						boolean flag = connectServer(port, hostIp, name);					
						if (flag == false) {
							throw new Exception("�����������ʧ��!");
						}
						frame.setTitle(name);
						textArea.setText("�����ӷ�����!");
						JOptionPane.showMessageDialog(frame, "�ɹ�����!");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(),
								"����", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			// �����Ͽ���ťʱ�¼�
			jb_stop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isConnected) {
						JOptionPane.showMessageDialog(frame, "�Ѵ��ڶϿ�״̬����Ҫ�ظ��Ͽ�!",
								"����", JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						boolean flag = closeConnection();// �Ͽ�����
						if (flag == false) {
							throw new Exception("�Ͽ����ӷ����쳣��");
						}
						JOptionPane.showMessageDialog(frame, "�ɹ��Ͽ�!");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(),
								"����", JOptionPane.ERROR_MESSAGE);
					}
					listModel.removeAllElements();
				}
			});
			
			// �رմ���ʱ�¼�
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					if (isConnected) {
						closeConnection();// �رշ�����
					}
					System.exit(0);// �˳�����
				}
			});
			

		}

	// ִ�з���
	public void send() {
		if (!isConnected) {
			//�û�δ���ӷ���������
			JOptionPane.showMessageDialog(frame, "��û�����ӷ��������޷�������Ϣ��", "����",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String message=textField.getText().trim();
		String selectUser="������";
			
		if (message == null || message.equals("")) {
			JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ�գ�", "����",
				JOptionPane.ERROR_MESSAGE);
		}
		if(groupchat.isSelected()){

			sendMessage("TOALL@"+frame.getTitle()+"@"+selectUser+"@"+message);
			
		//	System.out.println("Ⱥ�ķ���");
			textField.setText("");
		}
		if(privatechat.isSelected()){

			selectUser=(String)userList.getSelectedValue();
			if(selectUser==null){
				JOptionPane.showMessageDialog(frame, "��ѡ����˽�ĵ��û�!");
				return;
			}
			sendMessage("TOONE@"+frame.getTitle()+"@"+selectUser+"@"+message);
			String t="�Ҷ�"+selectUser+"˵:"+message+"��˽�ģ�\r\n";
			
			textArea.append(User.getTimeStr()+" "+t);
			//textArea.setForeground(Color.BLUE);˽������Ϊ��ɫ
			textField.setText("");
			
	
	}

	}	
	/**���ӷ�����
	 * 
	 * @param jf_port
	 * @param jf_Ip
	 * @param name
	 * @return
	 */
	public boolean connectServer(int jf_port, String jf_Ip, String name) {
		// ���ӷ�����
		try {
			socket = new Socket(jf_Ip, jf_port);// ���ݶ˿ںźͷ�����ip��������
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// ���Ϳͻ����û�������Ϣ(�û�����ip��ַ)
			sendMessage(name + "@" + socket.getLocalAddress().toString());
			// ����������Ϣ���߳�
			messageThread = new MessageThread(reader, textArea);
			messageThread.start();
			isConnected = true;// �Ѿ���������
			return true;
			
		} catch (Exception e) {
			textArea.append("���Ӷ˿ں�Ϊ��" + jf_port + "    IP��ַΪ��" + jf_Ip
					+ "   �ķ���������ʧ��!" + "\r\n");
			isConnected = false;// δ������
			return false;
		}
	}
	

	//������Ϣ
			public void sendMessage(String message) {
				
					writer.println(message);
					writer.flush();
				
			}
	//�ͻ��������ر�
	@SuppressWarnings("deprecation")
	public synchronized boolean closeConnection() {
		try {
			sendMessage("CLOSE");// ���ͶϿ����������������
			messageThread.stop();// ֹͣ������Ϣ�߳�
			// �ͷ���Դ
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			isConnected = true;
			return false;
		}
	}


	// ���Ͻ�����Ϣ���߳�
	class MessageThread extends Thread {
		private BufferedReader reader;
		private JTextArea textArea;
		String username=textField.getName();
		// ������Ϣ�̵߳Ĺ��췽��
		public MessageThread(BufferedReader reader, JTextArea textArea) {
			this.reader = reader;
			this.textArea = textArea;
		}
 
		// �����Ĺر�����
		public synchronized void closeCon() throws Exception {
			// ����û��б�
			listModel.removeAllElements();
			// �����Ĺر������ͷ���Դ
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;// �޸�״̬Ϊ�Ͽ�
		}
		public void run() {
			String message = null;
			while (true) {
				try {
					message = reader.readLine();
					StringTokenizer st=new StringTokenizer(message,"/@");
					
					String parts=st.nextToken();//����
					switch(parts){
					case "CLOSE":{
						textArea.append("�������ѹر�!\r\n");
						closeCon();// �����Ĺر�����
						return;// �����߳�
					}
					case "USERLIST":{
						
						//�û���Ϣ����
						int size=Integer.parseInt(st.nextToken());
						String username=null;
						String  userIp=null;
						
						for(int i=0;i<size;i++){
						username=st.nextToken();
						userIp=st.nextToken();
						User user=new User(username,userIp);
						onLineUsers.put(username, user);
						listModel.addElement(username);
						}
						break;
					}
					case "ADD":{
						String username = "";
						String userIp = "";
						if ((username = st.nextToken()) != null
								&& (userIp = st.nextToken()) != null) {

							User user = new User(username, userIp);
							onLineUsers.put(username, user);							
							listModel.addElement(username);
							textArea.append("ϵͳ��ʾ��"+username+"����!\r\n");
						}

						break;
					}
					case "DELETE":{
						String username=st.nextToken();
					//	System.out.println(username+"����");
						User user = (User) onLineUsers.get(username);
						onLineUsers.remove(user);
						listModel.removeElement(username);
						textArea.append("ϵͳ��ʾ:"+username+"����!\r\n");
						userList.setModel(listModel);
						
						break;
					}
					case "TOALL":{
						textArea.append(User.getTimeStr()+" "+st.nextToken()+"\r\n");
						//System.out.println("Ⱥ��");
						break;
					}
					case "TOONE":{
					
						textArea.append(User.getTimeStr()+" "+st.nextToken()+"\r\n");
						//System.out.println("˽��");
//						textArea.setForeground(Color.red);//˽�ĵ���ϢΪ��ɫ
						break;
					}
					default: 
						textArea.append(message+"\r\n");
						break;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}

	}
}