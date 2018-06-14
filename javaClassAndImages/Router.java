package mainCode;

import java.util.ArrayList;
import java.util.HashMap;

public class Router {
	String NID;
	String data;
	HashMap<String, String[]> routingTable;
	int routerNumber;
	private ArrayList<Switch> switches;
	Router(int number,String NID){
		this.NID=NID;
		routerNumber=number;
		switches=new ArrayList<>();
		routingTable = new HashMap<>();
	}
	void getDataFromSenderSwitch(String data) {
		this.data=data;
	}
	String sendDataToReceiverSwitch() {
		return data;
	}
	void storeConnectedSwitches(ArrayList<Switch> switches) {
		this.switches=switches;
	}
	ArrayList<Switch> getConnectedSwitches(){
		return switches;
	}
	
}
