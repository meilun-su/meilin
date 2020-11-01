package com.meilun.su;
/**
 * 服务器端
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
	
private JFrame frame; //服务器面板
private JTextField jf_ServerIP;//服务器IP地址
private JTextArea jf;
private JTextField jf_message;
private JTextField jf_max;
private JTextField jf_port;
private JButton jb_start;//启动按钮
private JButton jb_send;//发送按钮
private JButton jb_stop;//停止按钮
private JLabel j1;
private JPanel northPanle;//配置信息面板
private JPanel southPanle;//写消息面板
private JScrollPane leftPanle;//在线用户滑动窗口
private JScrollPane rightPanle;//消息显示区滑动窗口
private JSplitPane centerSplit;

private JList<String> userList;
private DefaultListModel<String> listModel;

private boolean isStart = false;

//	主方法
public static void main(String[] args) {
	new Server();

}
//构造函数
public Server(){
	frame=new JFrame("服务器");
	
	jf=new JTextArea();
	jf.setEditable(false);
	jf.setForeground(Color.blue);
	jf_message=new JTextField();
	jf_ServerIP = new JTextField("0.0.0.0");
	jf_max = new JTextField("30");
	jf_port=new JTextField("8888");
	jb_start=new JButton("启动");
	jb_stop=new JButton("停止");
	jb_send=new JButton("发送");
	listModel=new DefaultListModel<String>();
	userList=new JList<String>(listModel);
	
	southPanle=new JPanel(new BorderLayout());
	j1 = new JLabel("在线人数：");//创建在线人数标签

	southPanle.add(j1,BorderLayout.NORTH);
	southPanle.setBorder(new TitledBorder("消息"));
	southPanle.add(jf_message,BorderLayout.CENTER);
	southPanle.add(jb_send,BorderLayout.EAST);//使发生按钮位于容器的左边
	
	leftPanle=new JScrollPane(userList);
	leftPanle.setBorder(new TitledBorder("在线用户"));
	
	rightPanle=new JScrollPane(jf);
	rightPanle.setBorder(new TitledBorder("消息显示区"));
	
	centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			rightPanle, leftPanle);//实现面板的上下划分
	centerSplit.setDividerLocation(600);//按照一定比例划分
	
	northPanle=new JPanel();
	northPanle.setLayout(new GridLayout(1,8));//网格布局
	northPanle.add(new JLabel("IP地址:"));//添加“IP地址”标签
	northPanle.add(jf_ServerIP);//添加IP地址框
	northPanle.add(new JLabel("人数上限:"));//添加“人数上限”标签
	northPanle.add(jf_max);//添加人数上限框
	northPanle.add(new JLabel("端口:"));//添加“端口”标签
	northPanle.add(jf_port);//添加端口框
	northPanle.add(jb_start);//添加启动按钮
	northPanle.add(jb_stop);//添加停止按钮
	northPanle.setBorder(new TitledBorder("配置信息"));
	
	frame.setLayout(new BorderLayout());
	frame.add(northPanle, BorderLayout.NORTH);//设置northPanle组件出现在容器的顶部
	frame.add(centerSplit, BorderLayout.CENTER);//设置centerSplit组件出现在容器的中间
	frame.add(southPanle, BorderLayout.SOUTH);//设置southPanle组件出现在容器的底部
	frame.setSize(800, 500);//窗口大小
	frame.setVisible(true);
	
	try {
		String style = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		UIManager.setLookAndFeel(style);
		// 更新窗体样式
		SwingUtilities.updateComponentTreeUI(this.frame);
		} catch (Exception e) {
		e.printStackTrace();
		}
	// 使窗口出现在屏幕正中央
	int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
	frame.setLocation((screen_width - frame.getWidth()) / 2,
			(screen_height - frame.getHeight()) / 2);
	frame.setVisible(true);


	// 关闭窗口时事件
	frame.addWindowListener(new WindowAdapter() {
		
		public void windowClosing(WindowEvent e) {
			
			//保存服务器聊天记录到本地"D:\\Server_copy.txt"路径的聊天记录备份文本文档
			String fileName = "D:\\Server_copy.txt";
			try {
				FileWriter writer =new FileWriter(fileName,true);
				writer.write(jf.getText()+"\r\n\r\n");
				writer.close();
			} catch (IOException e1) {
				// TODO: handle exception
			}
			
			if (isStart) {
				closeServer();// 关闭服务器
			}
			System.exit(0);// 退出程序

		}
	});
	// 文本框按回车键时事件
	jf_message.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			send();
		}
	});
	
	@SuppressWarnings("unused")
	int port=Integer.parseInt(jf_port.getText());
	//	监听端口事件，即启动服务器
	jb_start.addActionListener(new ActionListener() {
		
	@Override
	public void actionPerformed(ActionEvent e) {
	if(isStart){
	JOptionPane.showMessageDialog(frame, "服务器已处于启动状态","错误",JOptionPane.ERROR_MESSAGE);
	return;
	}
	int port ;
	try{
		try{
		port= Integer.parseInt(jf_port.getText());
		}catch(Exception e1){
		throw new Exception("端口号 为正整数！");
		}
		if (port<= 0) {
		throw new Exception("端口号 为正整数！");
		
						}
						
						serverStart(port);
						jf.append("服务器已启动,IP地址为:" + jf_ServerIP.getText() +" " +"端口:"+port+",等待客户端连接中...\r\n");
						JOptionPane.showMessageDialog(frame, "服务器成功启动!");
						jb_start.setEnabled(false);
						jf_port.setEnabled(false);
						jb_stop.setEnabled(true);
				} catch (Exception ee) {
		JOptionPane.showMessageDialog(frame, ee.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
					
			}

		});
	// 单击停止服务器按钮时事件
		jb_stop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!isStart){
						JOptionPane.showMessageDialog(frame, "服务器还未启动！", "错误",JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						closeServer();
						jb_start.setEnabled(true);
						jf_port.setEnabled(true);
						jb_stop.setEnabled(false);
						jf.append("服务器成功停止!\r\n");
						JOptionPane.showMessageDialog(frame, "服务器已经停止！");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(frame, "停止服务器发生异常！", "错误",
								JOptionPane.ERROR_MESSAGE);
					}
					//保存服务器聊天记录到本地"D:\\Server_copy.txt"路径的聊天记录备份文本文档
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
			// 单击发送按钮时事件
			jb_send.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					send();
					
					//保存服务器聊天记录到本地"D:\\Server.txt"文本文档
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




	//启动服务器
	public void serverStart(int port)throws java.net.BindException {
		try {
			clients=new ArrayList<ClientThread>();
			serveSocket = new ServerSocket(port);
			serverThread = new ServerThread(serveSocket);
			serverThread.start();
			isStart=true;
		}catch(BindException e){
			isStart=false;
			throw new BindException("端口号已被占用，换一个");
		}
		catch (Exception e) {
			e.printStackTrace();
			isStart=false;
			throw new BindException("启动服务器异常");
		}
		
	}
	// 关闭服务器
		@SuppressWarnings("deprecation")
	public void closeServer() {
			try {
				if (serverThread != null)
					serverThread.stop();// 停止服务器线程
	 
				for (int i = clients.size() - 1; i >= 0; i--) {
					// 给所有在线用户发送关闭命令
					clients.get(i).getWriter().println("CLOSE");
					clients.get(i).getWriter().flush();
					// 释放资源
					clients.get(i).stop();// 停止此条为客户端服务的线程
					clients.get(i).reader.close();
					clients.get(i).writer.close();
					clients.get(i).socket.close();
					clients.remove(i);
				}
				if (serveSocket != null) {
					serveSocket.close();// 关闭服务器端连接
				}
				listModel.removeAllElements();// 清空用户列表
				isStart=false;
				} catch (IOException e) {
					e.printStackTrace();
					isStart=true;
			}
		}
	
	// 执行消息发送
	public void send() {
		if(!isStart){
						JOptionPane.showMessageDialog(frame, "服务器未启动，不能发送消息！","错误",
							JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (clients.size() == 0) {
						JOptionPane.showMessageDialog(frame, "没有用户在线,不能发送消息！", "错误",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					String message = jf_message.getText().trim();
					if (message == null || message.equals("")) {
						JOptionPane.showMessageDialog(frame, "消息不能为空！", "错误",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					sendServerMessage(message);// 群发服务器消息
					jf.append(User.getTimeStr()+" "+"服务器提示：" + jf_message.getText() + "\r\n");//服务器说的话显示在服务器界面
					jf_message.setText(null);
				}
	
	// 把后台消息发送给各个客户端
	public void sendServerMessage(String message) {
			for (int i = clients.size() - 1; i >= 0; i--) {
				clients.get(i).getWriter().println(User.getTimeStr()+" "+"系统提示：" + message+"(群发)");//服务器获得的输出流发送给客户端界面
				clients.get(i).getWriter().flush();


			}

		}
	
	
	
		 //每个连接到服务器的客户端，又有与之对应的一个线程来单独处理，收发消息
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
			
			//每个客户端对应一个客户端线程处理
			public ClientThread(Socket socket){
				try {
					this.socket=socket;
					reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
					writer=new PrintWriter(socket.getOutputStream());
					//接收客户端的基本信息
					String line=reader.readLine();
					String[] st=line.split("@");
					
	
					user=new User(st[0],st[1]);
					//反馈连接成功消息
					writer.println("系统提示："+user.getName()+user.getIp()+"与服务器连接成功!");
					//System.out.println(user.getName()+".."+user.getIp());
					  //反馈当前用户信息
					if(clients.size()>0){
						String temp="";
						for(int i=clients.size()-1;i>=0;i--){
							temp+=(clients.get(i).getUser().getName()+"/"+clients.get(i).getUser().getIp())+"@";
							
						}
						
						writer.println("USERLIST@"+clients.size()+"@"+temp);
						writer.flush();
						
					}
					System.out.println(st[0]+",服务器显示上线"+st[1]);
					//向所有在线用户发送该用户上线命令,即把新上线的用户添加在在线用户列表中
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
			public void run(){//不断接受客户端的消息进行处理
				String message=null;
				while(true){
					try {
					  message=reader.readLine();//接收客户端消息
				
						if (message.equals("CLOSE"))// 下线命令
						{
							 jf.append(this.getUser().getName()+ this.getUser().getIp() + "下线!\r\n");
							 
							// 断开连接释放资源
							reader.close();
							writer.close();
							socket.close();
	
							// 向所有在线用户发送该用户的下线命令
							for (int i = clients.size() - 1; i >= 0; i--) {
								clients.get(i).getWriter().println("DELETE@" + user.getName());
							clients.get(i).getWriter().flush();
							}
	
							listModel.removeElement(user.getName());// 更新在线列表
							j1.setText("目前在线人数"+userList.getModel().getSize()+"人");//更新在线人数

							// 删除此条客户端服务线程
							for (int i = clients.size() - 1; i >= 0; i--) {
								if (clients.get(i).getUser() == user) {
									ClientThread temp = clients.get(i);
									clients.remove(i);// 删除此用户的服务线程
									temp.stop();// 停止这条服务线程
									return;
								}
							}
							
						}else{
							dispatcherMessage(message);// 转发消息
						}
	
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
			}
	
			private void dispatcherMessage(String message) {
	
	
				String[] parts=message.split("@");
				String string=parts[1]+"对"+parts[2]+"说:"+parts[3];
				if (parts[0].equals("TOALL")) {// 群发
					for (int i = clients.size() - 1; i >= 0; i--) {
						clients.get(i).getWriter().println("TOALL@"+string);
						clients.get(i).getWriter().flush();
						//System.out.println("群聊消息发送");
					}
					jf.append(User.getTimeStr()+" "+string + "\r\n");
				}
				if(parts[0].equals("TOONE")){//私发
					for(int i=0;i<clients.size();i++){
						if(parts[2].equals((clients.get(i).getUser().getName())))
						{  
							string=parts[1]+"对我说:"+parts[3]+"（私聊）";
							clients.get(i).getWriter().println("TOONE@"+string);
							clients.get(i).getWriter().flush();
							//System.out.println("私聊消息发送");
						}
					}
						jf.append(User.getTimeStr()+" "+ parts[1]+"对"+parts[2]+"说:"+parts[3]+ "（私聊）\r\n");

		}	
				//保存服务器聊天记录到本地"D:\\Server.txt"文本文档
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
	
	//	服务器线程
	class ServerThread extends Thread {
	private ServerSocket serverSocket;
	
				// 服务器线程的构造方法
				public ServerThread(ServerSocket serverSocket) {
					this.serverSocket = serverSocket;
					
				}
	
				public void run() {
					while (true) {// 不停的等待客户端的链接
						try {
							Socket socket = serverSocket.accept();
							ClientThread client = new ClientThread(socket);
							client.start();// 开启对此客户端服务的线程
							clients.add(client);
							listModel.addElement(client.getUser().getName());// 更新在线列表
							jf.append(client.getUser().getName()+ client.getUser().getIp() + "上线!\r\n");
						}catch (IOException e) {
							e.printStackTrace();
					}
						j1.setText("目前在线人数"+userList.getModel().getSize()+"人");//更新在线人数

				}
			}
		}
}