package mainCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Receiver_search{
	String keyToBeSent="";
	public Receiver_search(String searchWebsite,String key,String meaning) {
		
		JFrame f = new JFrame();
		f.setLayout(null);
		f.setBackground(Color.white);
		JLabel label = new JLabel("SEARCH RESULT");
		label.setBounds(160,10,200,30);
		JLabel image = new JLabel(new ImageIcon("src/mainCode/search.png"));
		image.setBackground(Color.WHITE);
		//image.setSize(400, 200);
		image.setBounds(10, 0, 450, 250);
		JLabel sender = new JLabel  ("URL   : "+searchWebsite);
		sender.setBounds(20,270,300,50);
		JLabel searchKey = new JLabel("Key : "+key);
		searchKey.setBounds(20,320,460,30);
		searchKey.setForeground(Color.RED);
		JLabel advice = new JLabel("Meaning :");
		advice.setBounds(20,360,460,20);
		JLabel advice1 = new JLabel(meaning);
		advice1.setBounds(20,390,460,20);
		JLabel advice2 = new JLabel(" Result Received Successfully");
		advice2.setBounds(20,450,460,20);
		advice1.setFont(new Font("Serif", Font.BOLD, 12));
		advice1.setForeground(Color.BLUE);
		f.add(advice);
		f.add(advice1);
		f.add(advice2);
		f.add(label);
		f.add(searchKey);
		f.add(sender);
		f.add(image);
		f.setSize(500, 600);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
}
