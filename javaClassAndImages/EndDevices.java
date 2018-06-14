package mainCode;

import java.util.ArrayList;
import java.util.HashMap;

public class EndDevices {
	private int MAC;
	char deviceName;
	String data;
	String ACKorNAK="ACK";
	String IP;
	HashMap<Integer, String> ARPcache;
	
	public EndDevices(int MAC,char name, String IP) {
		this.MAC=MAC;
		deviceName=name;
		this.IP=IP;
		ARPcache=new HashMap<>();
		ARPcache.put(MAC, IP);		
	}
	void sendARPrequest(EndDevices receiver) {
		ARPcache.put(receiver.getMac(), receiver.IP);
	}
	
	int getMac() {
		return MAC;
	}
	void setData(String d) {
		data=d;
	}
	String getData() {
		return data;
	}
	void setRecieverData(String d) {
		data=(new CRCforDataLink()).recieverCode(d, Math.random());
	}
	
	public void sendDataAndAddressToHub(Hub hub) {
		hub.senderAddress=MAC;
		hub.recieveDataFromSender(data);		
	}
	public void sendDataToReciever(EndDevices reciever) {
		System.out.println("Data sent by sender");
		reciever.setData(data);
		System.out.println("Data recieved by reciever");
	}
	char getDeviceName() {
		return deviceName;
	}
	public void sendACKorNAK(boolean checkError, EndDevices sender_device) {
		if(checkError==true) {
			sender_device.ACKorNAK="NAK";
		}
		if(checkError==false) {
			sender_device.ACKorNAK="ACK";
		}
		
	}
	
	
}
