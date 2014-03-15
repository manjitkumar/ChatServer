
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class RClient {
	
	JFrame frame;
	JPanel mainPanel, chatPanel;
	Socket sock;
	BufferedReader reader;
	PrintWriter writer;
	JTextArea incoming;
	JTextField outgoing;
	JButton connect = new JButton("Connect");
	JTextField ipad = new JTextField(10);
	JTextField port = new JTextField(4);
	String ipad1;
	int port1;
	Thread readerThread;
	
	public static void main(String []arg){
		RClient client = new RClient();
		client.go();
	}
	
	public void go(){
		frame = new JFrame("RARC Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.orange);
		chatPanel = new JPanel();
		chatPanel.setBackground(Color.LIGHT_GRAY);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		frame.add(BorderLayout.CENTER, mainPanel);
		frame.add(BorderLayout.EAST, chatPanel);
		JLabel ipl = new JLabel("IP address");
		JLabel portl =  new JLabel("Port Number");
		mainPanel.add(ipl);
		mainPanel.add(ipad);
		mainPanel.add(portl);
		mainPanel.add(port);
		mainPanel.add(connect);
		
		incoming = new JTextArea(40,30);
		JScrollPane inscroll = new JScrollPane(incoming);
		inscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		inscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		incoming.setWrapStyleWord(true);
		incoming.setLineWrap(true);
		incoming.setEditable(false);
		incoming.setBackground(Color.getHSBColor(219, 102, 24));
		
		outgoing = new JTextField(10);
		outgoing.setText("Type your message here");
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		//outgoing.addFocusListener();
		chatPanel.add(inscroll);
		chatPanel.add(outgoing);
		chatPanel.add(sendButton);
		
		connect.addActionListener(new ConnectListener());	
				
		frame.setSize(1000,600);
		frame.setVisible(true);
		
	}
	
	class ConnectListener implements ActionListener{

		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ipad1 = ipad.getText();
			port1 = Integer.parseInt(port.getText());
			System.out.println("Establishing a Connection at IP "+ipad1 +" on Port "+ port1);
			setUpNetworking();
			readerThread = new Thread(new IncomingReader());
			readerThread.start();
		}	
	}
	
	class outgoingListener implements FocusListener{
	@Override
		public void focusGained(FocusEvent arg0) {
			outgoing.setText("");
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			outgoing.setText("Type your message here...");
		}		
	}
	
	class IncomingReader implements Runnable{
		@Override
		public void run() {
			String msg;
			try
			{
				while((msg = reader.readLine()) != null){
				System.out.println("Read : "+msg);
				incoming.append(msg+"\n");
			}//end while
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	class SendButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			String msg = outgoing.getText();
			System.out.println(msg);
			try{
				if((msg != "") || (msg != " ") || (msg != "  ")){
				writer.println(msg);
				writer.flush();
			}
			else
				System.out.println("You shouldn't send Empty Message");
		}catch(Exception ex){
			ex.printStackTrace();
		}
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}
			public void setUpNetworking(){
			try{
				sock = new Socket(ipad1, port1);
				InputStreamReader is = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(is);
				writer = new PrintWriter(sock.getOutputStream());
				System.out.println("Connection Established at Server IP "+ipad1+" on Port "+port1);
			}catch(Exception ex){ 
				System.out.println("Error in Establishing a Connection...");
				ex.printStackTrace();
				}
		}	
}
