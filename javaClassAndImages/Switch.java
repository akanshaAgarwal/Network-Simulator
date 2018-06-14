package mainCode;

import java.util.ArrayList;
import java.util.HashMap;

public class Switch {
	int switchNumber;
	ArrayList<Hub> hubs;
	EndDevices devicesDirectlyConnected[];
	ArrayList<EndDevices> connectedDirect;
	HashMap<EndDevices, Hub> connectedViaHub;
	String data;
	
	Switch(int num) {
		connectedDirect = new ArrayList<>();
		connectedViaHub = new HashMap<>();
		hubs=new ArrayList<>();
		switchNumber=num;
	}
	
	void getData(String data) {
		this.data=data;
	}
	
	void storeDirectlyConnectedDevices(EndDevices devices[]) {
		devicesDirectlyConnected=devices;
	}
	
	void storeConnectedHubs(ArrayList<Hub> hubs) {
		this.hubs=hubs;
	}

	void addToDirectConnectionTable(EndDevices device) {
		connectedDirect.add(device);
	}

	void addToHubConnectedTable(Hub hub, EndDevices device) {
		connectedViaHub.put(device, hub);
	}

	void sendDirectData(EndDevices sender_device, EndDevices reciever_device) {
		String data = sender_device.getData();
		System.out.println("Data recieved by the switch");
		System.out.println("Data sent to the reciever");
		reciever_device.setRecieverData(data);
		System.out.println("Data recieved by the reciever");

	}

	void sendDataViaHub(Hub sender_hub, Hub reciever_hub, EndDevices sender, EndDevices reciever) {
		addToHubConnectedTable(sender_hub, sender);

		System.out.println("Sender added to the switch table");
		reciever_hub.recieveDataFromSender(sender_hub.data);
		System.out.println("Data sent to the reciever hub");
		reciever_hub.sendDataToReciever(reciever);
		System.out.println("Data recieved by the reciever");

	}

	void sendACKorNAK() {

	}
}
