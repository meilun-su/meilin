package com.meilun.su;
/**
 * 用户端
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
 * 用户端界面
 */
	
private JFrame frame;//客户端窗口
private JTextArea textArea;

private JTextField textField;

private JTextField jf_port;//端口号框
private JTextField jf_Ip;//IP地址框
private JTextField jf_name;//昵称框

private JButton jb_start;//连接按钮
private JButton jb_stop;//断开按钮
private JButton jb_send;//发送按钮
private JButton jb_btnclear;//清屏按钮

private JRadioButton groupchat;//群聊按钮
private JRadioButton privatechat;//私聊按钮
private ButtonGroup   buttongroup;//按钮组
private JPanel buttonPanel;//按钮面板
private JPanel northPanel;//连接信息面板
private JPanel southPanel;//发送消息面板

private JScrollPane rightScroll;//消息显示区滑动窗格
private JScrollPane leftScroll;//在线用户滚动窗格
private JSplitPane centerSplit;


private JList<String> userList;
private DefaultListModel<String> listModel;

private boolean isConnected = false;

private Socket socket;
private PrintWriter writer;
private BufferedReader reader;
private MessageThread messageThread;// 负责接收消息的线程
private Map<String, User> onLineUsers = new HashMap<String, User>();// 所有在线用户，可以和他们私聊


//构造函数
public static void main(String[] args) {
	new client();
}
public client(){
	textArea = new JTextArea();
	textArea.setEditable(false);

	textField = new JTextField();
	jf_port = new JTextField("8888");
	jf_Ip = new JTextField("127.0.0.1");
	jb_start = new JButton("连接");
	jb_stop = new JButton("断开");
	jb_send = new JButton("发送");
	listModel = new DefaultListModel<String>();
	userList = new JList<String>(listModel);
	
	Random rand = new Random();
	jf_name = new JTextField("匆匆过客" + rand.nextInt(100));//随机生成昵称为匆匆过客1-100的用户
	
	//连接消息面板
	northPanel = new JPanel();
	northPanel.setLayout(new GridLayout(1, 10));
	northPanel.add(new JLabel("端口"));
	northPanel.add(jf_port);
	northPanel.add(new JLabel("服务器IP"));
	northPanel.add(jf_Ip);
	northPanel.add(new JLabel("昵称"));
	northPanel.add(jf_name);
	northPanel.add(jb_start);
	northPanel.add(jb_stop);
	northPanel.setBorder(new TitledBorder("连接信息"));

	//在线用户、消息显示面板
	rightScroll = new JScrollPane(textArea);
	rightScroll.setBorder(new TitledBorder("消息显示区"));
	leftScroll = new JScrollPane(userList);
	leftScroll.setBorder(new TitledBorder("在线用户"));
	centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				  rightScroll, leftScroll);//实现面板的上下划分
	centerSplit.setDividerLocation(600);//按照一定比例划分
	
	//群聊私聊按钮组
	groupchat=new JRadioButton("群聊");
	privatechat=new JRadioButton("私聊");
	privatechat.setSelected(true);
	buttongroup=new ButtonGroup();
	buttongroup.add(groupchat);
	buttongroup.add(privatechat);
	buttonPanel=new JPanel();
	buttonPanel.setLayout(new FlowLayout());
	buttonPanel.add(groupchat);
	buttonPanel.add(privatechat);
	
	//清屏按钮,实现清屏功能
	jb_btnclear = new JButton("清屏");
	jb_btnclear.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent arg0) {
	textArea.setText("");
	}
	});
	buttonPanel.add(jb_btnclear);

	//消息面板
	southPanel = new JPanel(new BorderLayout());
	southPanel.add(buttonPanel, "North");
	southPanel.add(textField, "Center");
	southPanel.add(jb_send, "East");
	southPanel.setBorder(new TitledBorder("消息"));

	frame = new JFrame("客户端");
	
	frame.setLayout(new BorderLayout());
	
	frame.add(northPanel, "North");
	frame.add(centerSplit, "Center");
	frame.add(southPanel, "South");
	frame.setSize(800, 500);
	frame.setVisible(true);
	try {
		// 更新窗体样式
		String style = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		UIManager.setLookAndFeel(style);
		SwingUtilities.updateComponentTreeUI(this.frame);
		} catch (Exception e) {
		e.printStackTrace();
		}
	//swing窗口出现在屏幕中央
	int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
	frame.setLocation((screen_width - frame.getWidth()) / 2,
			(screen_height - frame.getHeight()) / 2);
	frame.setVisible(true);

	// 写消息的文本框中按回车键时事件
	textField.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			send();
		}
	});
			// 单击发送按钮事件
			jb_send.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					send();
				}
			});
	 
			// 单击连接按钮事件
			jb_start.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int port;
					if (isConnected) {
						JOptionPane.showMessageDialog(frame, "已处于连接上状态，不要重复连接!",
								"错误", JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						try {
							port = Integer.parseInt(jf_port.getText().trim());
						} catch (NumberFormatException e1) {
							throw new Exception("端口号不符合要求!端口为整数!");
						}
						String hostIp = jf_Ip.getText().trim();
						String name = jf_name.getText().trim();
						if (name.equals("") || hostIp.equals("")) {
							throw new Exception("昵称、服务器IP不能为空!");
						}
						boolean flag = connectServer(port, hostIp, name);					
						if (flag == false) {
							throw new Exception("与服务器连接失败!");
						}
						frame.setTitle(name);
						textArea.setText("已连接服务器!");
						JOptionPane.showMessageDialog(frame, "成功连接!");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(),
								"错误", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			// 单击断开按钮时事件
			jb_stop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isConnected) {
						JOptionPane.showMessageDialog(frame, "已处于断开状态，不要重复断开!",
								"错误", JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						boolean flag = closeConnection();// 断开连接
						if (flag == false) {
							throw new Exception("断开连接发生异常！");
						}
						JOptionPane.showMessageDialog(frame, "成功断开!");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(),
								"错误", JOptionPane.ERROR_MESSAGE);
					}
					listModel.removeAllElements();
				}
			});
			
			// 关闭窗口时事件
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					if (isConnected) {
						closeConnection();// 关闭服务器
					}
					System.exit(0);// 退出程序
				}
			});
			

		}

	// 执行发送
	public void send() {
		if (!isConnected) {
			//用户未连接服务器报错
			JOptionPane.showMessageDialog(frame, "还没有连接服务器，无法发送消息！", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String message=textField.getText().trim();
		String selectUser="所有人";
			
		if (message == null || message.equals("")) {
			JOptionPane.showMessageDialog(frame, "消息不能为空！", "错误",
				JOptionPane.ERROR_MESSAGE);
		}
		if(groupchat.isSelected()){

			sendMessage("TOALL@"+frame.getTitle()+"@"+selectUser+"@"+message);
			
		//	System.out.println("群聊发送");
			textField.setText("");
		}
		if(privatechat.isSelected()){

			selectUser=(String)userList.getSelectedValue();
			if(selectUser==null){
				JOptionPane.showMessageDialog(frame, "请选择想私聊的用户!");
				return;
			}
			sendMessage("TOONE@"+frame.getTitle()+"@"+selectUser+"@"+message);
			String t="我对"+selectUser+"说:"+message+"（私聊）\r\n";
			
			textArea.append(User.getTimeStr()+" "+t);
			//textArea.setForeground(Color.BLUE);私聊字体为蓝色
			textField.setText("");
			
	
	}

	}	
	/**连接服务器
	 * 
	 * @param jf_port
	 * @param jf_Ip
	 * @param name
	 * @return
	 */
	public boolean connectServer(int jf_port, String jf_Ip, String name) {
		// 连接服务器
		try {
			socket = new Socket(jf_Ip, jf_port);// 根据端口号和服务器ip建立连接
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 发送客户端用户基本信息(用户名和ip地址)
			sendMessage(name + "@" + socket.getLocalAddress().toString());
			// 开启接收消息的线程
			messageThread = new MessageThread(reader, textArea);
			messageThread.start();
			isConnected = true;// 已经连接上了
			return true;
			
		} catch (Exception e) {
			textArea.append("连接端口号为：" + jf_port + "    IP地址为：" + jf_Ip
					+ "   的服务器连接失败!" + "\r\n");
			isConnected = false;// 未连接上
			return false;
		}
	}
	

	//发送消息
			public void sendMessage(String message) {
				
					writer.println(message);
					writer.flush();
				
			}
	//客户端主动关闭
	@SuppressWarnings("deprecation")
	public synchronized boolean closeConnection() {
		try {
			sendMessage("CLOSE");// 发送断开连接命令给服务器
			messageThread.stop();// 停止接受消息线程
			// 释放资源
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


	// 不断接收消息的线程
	class MessageThread extends Thread {
		private BufferedReader reader;
		private JTextArea textArea;
		String username=textField.getName();
		// 接收消息线程的构造方法
		public MessageThread(BufferedReader reader, JTextArea textArea) {
			this.reader = reader;
			this.textArea = textArea;
		}
 
		// 被动的关闭连接
		public synchronized void closeCon() throws Exception {
			// 清空用户列表
			listModel.removeAllElements();
			// 被动的关闭连接释放资源
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;// 修改状态为断开
		}
		public void run() {
			String message = null;
			while (true) {
				try {
					message = reader.readLine();
					StringTokenizer st=new StringTokenizer(message,"/@");
					
					String parts=st.nextToken();//命令
					switch(parts){
					case "CLOSE":{
						textArea.append("服务器已关闭!\r\n");
						closeCon();// 被动的关闭连接
						return;// 结束线程
					}
					case "USERLIST":{
						
						//用户信息储存
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
							textArea.append("系统提示："+username+"上线!\r\n");
						}

						break;
					}
					case "DELETE":{
						String username=st.nextToken();
					//	System.out.println(username+"下线");
						User user = (User) onLineUsers.get(username);
						onLineUsers.remove(user);
						listModel.removeElement(username);
						textArea.append("系统提示:"+username+"下线!\r\n");
						userList.setModel(listModel);
						
						break;
					}
					case "TOALL":{
						textArea.append(User.getTimeStr()+" "+st.nextToken()+"\r\n");
						//System.out.println("群聊");
						break;
					}
					case "TOONE":{
					
						textArea.append(User.getTimeStr()+" "+st.nextToken()+"\r\n");
						//System.out.println("私聊");
//						textArea.setForeground(Color.red);//私聊的消息为红色
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