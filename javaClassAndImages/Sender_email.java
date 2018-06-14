package mainCode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Sender_email{
	String emailToBeSent="";
	public Sender_email(String senderEmail,String receiverEmail) {
		
		JFrame f = new JFrame();
		f.setLayout(null);
		f.setBackground(Color.white);
		JLabel label = new JLabel("SENDER");
		label.setBounds(200,10,100,30);
		JLabel image = new JLabel(new ImageIcon("src/mainCode/email.png"));
		image.setBackground(Color.WHITE);
		//image.setSize(400, 200);
		image.setBounds(10, 0, 450, 250);
		JLabel sender = new JLabel  ("Sender   : "+senderEmail);
		JLabel receiver = new JLabel("Receiver : "+receiverEmail);
		sender.setBounds(20,270,300,50);
		receiver.setBounds(20,300,300,50);
		JTextArea mailContent = new JTextArea("Enter your content here.");
		mailContent.setBounds(20,350,460,120);
		JButton send = new JButton("Send E-Mail");
		send.setBounds(180, 500, 120, 30);
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				emailToBeSent=mailContent.getText();
				f.dispose();
			}
		});
		f.add(label);
		f.add(send);
		f.add(mailContent);
		f.add(sender);
		f.add(receiver);
		f.add(image);
		f.setSize(500, 600);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
}
