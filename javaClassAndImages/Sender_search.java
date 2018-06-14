package mainCode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Sender_search{
	String keyToBeSent="";
	public Sender_search(String searchWebsite) {
		
		JFrame f = new JFrame();
		f.setLayout(null);
		f.setBackground(Color.white);
		JLabel label = new JLabel("SENDER");
		label.setBounds(200,10,100,30);
		JLabel image = new JLabel(new ImageIcon("src/mainCode/search.png"));
		image.setBackground(Color.WHITE);
		//image.setSize(400, 200);
		image.setBounds(10, 0, 450, 250);
		JLabel sender = new JLabel  ("URL   : "+searchWebsite);
		sender.setBounds(20,270,300,50);
		JTextField searchKey = new JTextField();
		searchKey.setBounds(20,320,460,30);
		JLabel advice = new JLabel("Better to choose words like :");
		advice.setBounds(20,360,460,20);
		JLabel advice1 = new JLabel("apple, boy, cat, dog, egg, fish, girl, house.");
		advice1.setBounds(20,380,460,20);
		JLabel advice2 = new JLabel(" If not , it is your choice, but a result may unavailable.");
		advice2.setBounds(20,400,460,20);
		JButton send = new JButton("Search");
		send.setBounds(160, 460, 120, 30);
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				keyToBeSent=searchKey.getText();
				//System.out.println(keyToBeSent);
				f.dispose();
			}
		});
		f.add(advice);
		f.add(advice1);
		f.add(advice2);
		f.add(label);
		f.add(send);
		f.add(searchKey);
		f.add(sender);
		f.add(image);
		f.setSize(500, 600);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
}
