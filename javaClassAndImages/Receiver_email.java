package mainCode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Receiver_email{
	String emailToBeReceived="";
	public Receiver_email(String senderEmail,String receiverEmail,String data) {
		emailToBeReceived=data;
		JFrame f = new JFrame();
		f.setLayout(null);
		f.setBackground(Color.white);
		JLabel label = new JLabel("RECEIVER");
		label.setBounds(200,10,100,30);
		JLabel image = new JLabel(new ImageIcon("src/mainCode/email.png"));
		image.setBackground(Color.WHITE);
		//image.setSize(400, 200);
		image.setBounds(10, 0, 450, 250);
		JLabel sender = new JLabel  ("Sender   : "+senderEmail);
		JLabel receiver = new JLabel("Receiver : "+receiverEmail);
		sender.setBounds(20,270,300,50);
		receiver.setBounds(20,300,300,50);
		JTextArea mailContent = new JTextArea(emailToBeReceived);
		mailContent.setBounds(20,350,460,120);
		JLabel displayStatus = new JLabel("Data Received");
		displayStatus.setBounds(180, 500, 140, 30);
		f.add(label);
		f.add(displayStatus);
		f.add(mailContent);
		f.add(sender);
		f.add(receiver);
		f.add(image);
		f.setSize(500, 600);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
}
