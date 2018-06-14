package mainCode;

public class Hub {
	int senderAddress;
	int recieverAddress;
	int hubNumber;
	String data;
	EndDevices devicesConnected[];

	public Hub(int hubNumber) {
		this.hubNumber = hubNumber;
	}

	void recieveDataFromSender(String d) {
		data = d;
	}

	void sendDataToReciever(EndDevices recieverDevice) {
		recieverAddress = recieverDevice.getMac();
		recieverDevice.setRecieverData(data);
	}

	int getHubNumber() {
		return hubNumber;
	}

	int getRecieverAddress() {
		return recieverAddress;
	}

	int getSenderAddress() {
		return senderAddress;
	}

	void storeDevicesConnected(EndDevices dev[]) {
		devicesConnected = dev;
	}

	EndDevices[] getConnectedDevices() {
		return devicesConnected;
	}

	void sendDataToSwitch(Switch switch_new, Hub sender_hub,Hub reciever_hub,EndDevices sender,EndDevices reciever) {
		//broadcast 
		EndDevices[] broadcastDev = sender_hub.getConnectedDevices();
		int flag=0;
		for(int i=0;i<broadcastDev.length;i++) {
			if(broadcastDev[i].getMac() == reciever.getMac()) {
				System.out.println("Data recieved by reciever "+broadcastDev[i].getDeviceName());
				flag=1;
			}
			else {
				System.out.println("Data discarded by deviced "+broadcastDev[i].getDeviceName());
			}
		}
		
		if(flag == 1) {
			System.out.println("Reciever in the same hub, no need to send data to switch");
			sender_hub.recieveDataFromSender(sender.data);
			sender_hub.sendDataToReciever(reciever);
			System.out.println("Data transfer from hub by the reciever completed");
		}
		else {
			System.out.println("Reciever in another hub, send data to switch");
			sender_hub.recieveDataFromSender(sender.getData());
			System.out.println("Send data to switch");
			switch_new.sendDataViaHub(sender_hub,reciever_hub,sender,reciever);
		}
	}

	void sendACKorNAK() {
		
	}
	void recieveACKorNAK() {
		
	}

}
