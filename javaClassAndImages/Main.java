package mainCode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

	private static BufferedImage endDeviceImage1, endDeviceImage2, hubImage, switchImage, messageImage, routerImage, emailImage;
	private static ArrayList<Hub> hubs;
	private static ArrayList<Switch> switches;
	private static ArrayList<Router> routers;
	private static Switch switch_new;
	private static int numberOfRouters;
	private static int numberOfSwitches;
	private static int numberOfHubs;
	private static int numberOfDevices;
	private static EndDevices objectOfDevices[];
	private static char senderDevice;
	private static char receiverDevice;
	private static int senderNumber;
	private static int receiverNumber;
	private static String dataToBeSent = "";
	private static Router sender_router, receiver_router;
	private static Switch sender_switch, receiver_switch;
	private static Hub sender_hub, receiver_hub;
	private static EndDevices sender_device, receiver_device;
	private static EndDevices[] devices;
	private int x_Sender, y_Sender, x_Receiver, y_Receiver, x_Srouter, y_Srouter, x_Rrouter, y_Rrouter, x_Sswitch,
			y_Sswitch, x_Rswitch, y_Rswitch, x_Shub, y_Shub, x_Rhub, y_Rhub;
	private static String senderIP, receiverIP;
	static int messageNumber = 0;
	private int a1, b1, c1, d1, e1, f1, g1, h1, i1, j1, k1, l1, m1, n1, o1, p1, q1;
	private int a2, b2, c2, d2, e2, f2, g2, h2, i2, j2, k2, l2, m2, n2, o2, p2, q2;
	private static boolean checkError;
	private static String ACKorNAK = "";
	private static int numberOfHops;
	static int routingProtocolChoice = 0;
	private static int choiceOfProcess;
	private static String senderEmailID;
	private static String receiverEmailID;
	private static int processPortNumber;
	private static int senderPortNumber;
	private static String targetSearchEngine;
	private static String emailContentFromSender;
	private static String meaningForSearchKey;
	private static String searchKey;

	public static void main(String[] args) {
		Scanner sn = new Scanner(System.in);

		Main m = new Main();
		//Sender_ftp ft=new Sender_ftp("10.0.0.0","10.0.0.0");
		DrawPanel animation = m.new DrawPanel();
		routers = new ArrayList();
		System.out.println("Enter the number of routers you want in your network");
		numberOfRouters = sn.nextInt();
		for (int r = 0; r < numberOfRouters; r++) {
			routers.add(new Router(r, ((r + 1) * 10) + ".0.0.0"));
			System.out.println();
			System.out.println("Enter the number of switches in ROUTER " + r + " (Enter 0 for no switch)");
			numberOfSwitches = sn.nextInt();
			switches = new ArrayList<>();
			int total_devices = 0;
			for (int s = 0; s < numberOfSwitches; s++) {
				switches.add(new Switch(s));
				hubs = new ArrayList<>();
				System.out.println("Enter the number of hubs you want to connect to the switch " + s + " of router " + r
						+ " (enter 0 if no hub)");
				numberOfHubs = sn.nextInt();

				// for hubs>0
				if (numberOfHubs > 0) {
					System.out.println("Now lets enter the devices for each hub");

					for (int i = 0; i < numberOfHubs; i++) {
						Hub newHub = new Hub(i);
						System.out.println("Enter the number of devices to be connected to Hub " + i);
						numberOfDevices = sn.nextInt();
						objectOfDevices = new EndDevices[numberOfDevices];
						for (int j = total_devices + 0; j < total_devices + numberOfDevices; j++) {
							objectOfDevices[j - total_devices] = new EndDevices(j, (char) (65 + j),
									(r + 1) * 10 + ".0.0." + (j + 2));
						}
						total_devices += numberOfDevices;
						newHub.storeDevicesConnected(objectOfDevices);
						hubs.add(newHub);
					}
					switch_new = switches.get(s);
					switch_new.storeConnectedHubs(hubs);
				} else {
					System.out.println("Enter the number of devices you want to directly connect to Switch " + s);
					numberOfDevices = sn.nextInt();
					objectOfDevices = new EndDevices[numberOfDevices];
					for (int j = total_devices + 0; j < total_devices + numberOfDevices; j++) {
						objectOfDevices[j - total_devices] = new EndDevices(j, (char) (65 + j),
								(r + 1) * 10 + ".0.0." + (j + 2));
					}
					total_devices += numberOfDevices;
					switch_new = switches.get(s);
					switch_new.storeDirectlyConnectedDevices(objectOfDevices);
				}
			}
			routers.get(r).storeConnectedSwitches(switches);
		}
		for (int r = 0; r < numberOfRouters; r++) {
			System.out.println("\n");
			System.out.println("ROUTER " + r + " .......................................................");
			switches = routers.get(r).getConnectedSwitches();
			if (switches.size() == 0)
				System.out.println("No devices connected");
			for (int s = 0; s < switches.size(); s++) {
				System.out.println("\nSWITCH " + s + " ------------------");
				hubs = switches.get(s).hubs;
				if (hubs.size() > 0) {
					for (int h = 0; h < hubs.size(); h++) {
						System.out.println("HUB " + h);
						System.out.println("IP Address of devices connected to hub " + h);
						objectOfDevices = hubs.get(h).devicesConnected;
						for (int d = 0; d < objectOfDevices.length; d++) {
							System.out.print(objectOfDevices[d].deviceName + " " + objectOfDevices[d].IP + "\t");
						}
						System.out.println();
					}
				} else {
					objectOfDevices = switches.get(s).devicesDirectlyConnected;
					System.out.println("IP Address of devices connected to switch " + s);
					for (int d = 0; d < objectOfDevices.length; d++) {
						System.out.print(objectOfDevices[d].deviceName + " " + objectOfDevices[d].IP + "\t");
					}
					System.out.println();
				}
			}
		}
		System.out.println();
		System.out.println("------------APPLICATION LAYER-------------");

		
		System.out.println("\nThe processes available for use are :");
		System.out.println("1. Send an e-mail ");
		System.out.println("2. Search for some word on the search engine of your choice");
		//System.out.println("3. Send a file using FTP");

		System.out.println("Enter your choice");
		choiceOfProcess = sn.nextInt();

		if (choiceOfProcess == 1) {
			senderPortNumber = 1234;
			processPortNumber = 25;
			System.out.println("\nSELECTED THE PROCESS FOR SENDING E-MAIL ");
			senderEmailID = "akansha08agarwal@gmail.com";
			System.out.println("Enter the IP Address of sender device");
			senderIP = sn.next();
			System.out.println("The available e-mail id\'s are:");
			System.out.println("1. akansha26agarwal@gmail.com");
			System.out.println("2. zakiakmal13@gmail.com");
			System.out.println("3. bhartitak11@gmail.com");
			System.out.println("Select the receiver e-mail id");
			int receiverIdChoice = sn.nextInt();
			if (receiverIdChoice == 1) {
				receiverEmailID = "akansha26agarwal@gmail.com";
			}
			else if (receiverIdChoice == 2) {
				receiverEmailID = "zakiakmal13@gmail.com";
			}
			else {
				receiverEmailID = "bhartitak11@gmail.com";
			}
			receiverIP=objectOfDevices[objectOfDevices.length-1].IP;
			//System.out.println(receiverIP);
		} else if (choiceOfProcess == 2) {
			senderPortNumber = 5678;
			processPortNumber = 80;
			System.out.println("\nSELECTED THE OPTION FOR SEARCHING VIA SOME SEARCH ENGINE ");
			System.out.println("Enter the IP Address of sender device");
			senderIP = sn.next();
			System.out.println("The available search engines are:");
			System.out.println("1. www.google.co.in");
			System.out.println("2. www.duckduckgo.com");
			System.out.println("3. www.bing.com");
			System.out.println("Select the search engine you want to use");
			int receiverIdChoice = sn.nextInt();
			if (receiverIdChoice == 1) {
				targetSearchEngine = "www.google.co.in";
			}
			else if (receiverIdChoice == 2) {
				targetSearchEngine = "www.duckduckgo.com";
			}
			else {
				targetSearchEngine = "www.bing.com";
			}
			receiverIP=objectOfDevices[objectOfDevices.length-1].IP;
			
		} else {
			senderPortNumber = 3456;
			processPortNumber = 21;
			System.out.println("\nSELECTED THE OPTION FOR FILE TRANSFER USING FTP ");
			System.out.println("Enter the IP Address of sender device");
			senderIP = sn.next();
			System.out.println("Enter the IP Address of receiver device");
			receiverIP = sn.next();
		}
		
		
		
		dataToBeSent = "10100100100101010010000010110";
		// System.out.println(dataToBeSent);
		System.out.println();
		
		
		
		
		
		if(processPortNumber==25) {
			Sender_email se = new Sender_email(senderEmailID, receiverEmailID);
			int check = sn.nextInt();
			emailContentFromSender = se.emailToBeSent;
			/*dataToBeSent = "";
			//System.out.println("Here");
			for(int loop=0;loop<senderEmailID.length();loop++) {
				String binaryCode = Integer.toBinaryString((int)(senderEmailID.charAt(loop)));
				dataToBeSent+=binaryCode;
				//System.out.println(binaryCode);
			}
			
*/			//System.out.println(receiverIP);
			
		}
		if(processPortNumber==80) {
			Sender_search search = new Sender_search(targetSearchEngine);
			searchKey = search.keyToBeSent;
			int check = sn.nextInt();
			for(int loop=0;loop<searchKey.length();loop++) {
				String binaryCode = Integer.toBinaryString((int)searchKey.charAt(loop));
				dataToBeSent+=binaryCode;
			}
			meaningForSearchKey=SearchEngineServer.returnKeySearch(search.keyToBeSent);
			//DomainNameServer.storeDNSforSearchEngines(receiverIP);
		}
		
		System.out.println("The data to be sent is :");
		System.out.println(dataToBeSent+"\n");
		
		
		System.out.println();
		System.out.println("------------TRANSPORT LAYER-------------");
		System.out.println("\nThe sender port number used is : "+senderPortNumber);
		System.out.println("The process port number used is : "+processPortNumber);
		System.out.println("Forwarding data to network layer\n");
		
		
		System.out.println();

		System.out.println();
		System.out.println("------------NETWORK LAYER-------------");
		System.out.println("Select the routing protocol you want to use");
		System.out.println("1. Static Routing");
		System.out.println("2. RIP (Routing Information Protocol)");
		System.out.println("3. OSPF (Open Shortest Path First)");
		System.out.println("Enter your choice");
		routingProtocolChoice = sn.nextInt();

		if (routingProtocolChoice == 2) {
			System.out.println("\n USING RIP");
			Router currentR = routers.get(0);
			if (numberOfRouters > 1) {
				currentR.routingTable.put(routers.get(1).NID, new String[] { "1", "255.0.0.0", "20.0.0.1" });
				for (int r = 1; r < numberOfRouters - 1; r++) {
					currentR = routers.get(r);
					currentR.routingTable.put(routers.get(r - 1).NID,
							new String[] { "1", "255.0.0.0", (r) * 10 + ".0.0.1" });
					currentR.routingTable.put(routers.get(r + 1).NID,
							new String[] { "1", "255.0.0.0", (r + 2) * 10 + ".0.0.1" });
				}
				currentR = routers.get(numberOfRouters - 1);
				currentR.routingTable.put(routers.get(numberOfRouters - 2).NID,
						new String[] { "1", "255.0.0.0", (numberOfRouters - 1) * 10 + ".0.0.1" });

				System.out.println(
						"\n\nThe initial ROUTING TABLES of routers with entries of neighbouring routers  are :\n");
				for (int r = 0; r < numberOfRouters; r++) {
					System.out.println("For Router " + r);
					Set<Entry<String, String[]>> entires = routers.get(r).routingTable.entrySet();
					for (Entry<String, String[]> ent : entires) {
						System.out.println(ent.getKey() + " ==> " + ent.getValue()[0] + " " + ent.getValue()[1] + " "
								+ ent.getValue()[2]);
					}
					System.out.println();
				}
			}

			System.out.println("\n\nPROTOCOL USED IS ROUTING INFORMATION PROTOCOL");
			System.out.println("NUMBER OF HOPS IS COUNTED, THE PATH WITH MINIMUM HOPS IS TAKEN AS OPTIMAL PATH\n\n");

			if (numberOfRouters > 1) {
				for (int r = 0; r < numberOfRouters; r++) {
					HashMap<String, String[]> table = routers.get(r).routingTable;
					for (int r1 = 0; r1 < numberOfRouters; r1++) {
						if (r != r1 && table.containsKey(routers.get(r1).NID) == false) {
							table.put(routers.get(r1).NID, new String[] { "1", "255.0.0.0", (r1 + 1) * 10 + ".0.0.1" });
						}
					}
				}
				System.out.println("\n\nThe FINAL ROUTING TABLES of routers are :\n");
				for (int r = 0; r < numberOfRouters; r++) {
					System.out.println("For Router " + r);
					Set<Entry<String, String[]>> entires = routers.get(r).routingTable.entrySet();
					for (Entry<String, String[]> ent : entires) {
						System.out.println(ent.getKey() + " ==> " + ent.getValue()[0] + " " + ent.getValue()[1] + " "
								+ ent.getValue()[2]);
					}
					System.out.println();
				}
			}

		} else if (routingProtocolChoice == 3) {
			System.out.println("\n USING OSPF");
			if (numberOfRouters > 1) {
				for (int r = 0; r < numberOfRouters; r++) {
					HashMap<String, String[]> table = routers.get(r).routingTable;
					for (int r1 = 0; r1 < numberOfRouters; r1++) {
						if (r != r1 && table.containsKey(routers.get(r1).NID) == false) {
							table.put(routers.get(r1).NID,
									new String[] { (r1 + 1) + "", "255.0.0.0", (r1 + 1) * 10 + ".0.0.1" });
						}
					}
				}
				System.out.println("\n\nThe FINAL ROUTING TABLES of routers are :\n");
				for (int r = 0; r < numberOfRouters; r++) {
					System.out.println("For Router " + r);
					Set<Entry<String, String[]>> entires = routers.get(r).routingTable.entrySet();
					for (Entry<String, String[]> ent : entires) {
						System.out.println(ent.getKey() + " ==> " + ent.getValue()[0] + " " + ent.getValue()[1] + " "
								+ ent.getValue()[2]);
					}
					System.out.println();
				}
			}

		} else {
			System.out.println("\n USING STATIC ROUTING");
			if (numberOfRouters > 1) {
				for (int r = 0; r < numberOfRouters; r++) {
					HashMap<String, String[]> table = routers.get(r).routingTable;
					for (int r1 = 0; r1 < numberOfRouters; r1++) {
						if (r != r1 && table.containsKey(routers.get(r1).NID) == false) {
							table.put(routers.get(r1).NID, new String[] { "1", "255.0.0.0", (r1 + 1) * 10 + ".0.0.1" });
						}
					}
				}
				System.out.println("\n\nThe FINAL ROUTING TABLES of routers are :\n");
				for (int r = 0; r < numberOfRouters; r++) {
					System.out.println("For Router " + r);
					Set<Entry<String, String[]>> entires = routers.get(r).routingTable.entrySet();
					for (Entry<String, String[]> ent : entires) {
						System.out.println(ent.getKey() + " ==> " + ent.getValue()[0] + " " + ent.getValue()[1] + " "
								+ ent.getValue()[2]);
					}
					System.out.println();
				}
			}

		}

		
		
		
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		
		animation.calculations();

		// for OSPF
		System.out.println("\n The total cost of selected path will be as follows :");
		int totalCostForOSPF = 0;
		if (routingProtocolChoice == 3 && numberOfRouters > 1) {
			int senderRouterNumber = sender_router.routerNumber;
			int receiverRouterNumber = receiver_router.routerNumber;
			// NID = ((r + 1) * 10) + ".0.0.0")
			Set<Entry<String, String[]>> entires = routers.get(senderRouterNumber).routingTable.entrySet();
			for (Entry<String, String[]> ent : entires) {
				if (((int) ent.getKey().charAt(0)) - 49 <= receiverRouterNumber
						&& ((int) ent.getKey().charAt(0)) - 49 >= senderRouterNumber) {
					System.out.println("The cost for Router " + ((ent.getKey().charAt(0)) - 49) + " to Router "
							+ ((ent.getKey().charAt(0)) - 48) + " is " + Integer.parseInt(ent.getValue()[0]));
					totalCostForOSPF += Integer.parseInt(ent.getValue()[0]);
				}
			}
			System.out.println("TOTAL COST = " + totalCostForOSPF);

		}

		System.out.println();
		System.out.println("The details of sender and receiver devices are :");
		System.out.println("ROUTER : " + sender_router.NID + " " + receiver_router.NID);
		System.out.println("SWITCH : " + sender_switch.switchNumber + " " + receiver_switch.switchNumber);
		// System.out.println("HUB : "+sender_hub.hubNumber+" "+receiver_hub.hubNumber);
		System.out.println("END DEVICES : " + sender_device.IP + " " + receiver_device.IP);

		System.out.println("\n\n");
		System.out.println("The ARP cache of sender contains initially : ");
		System.out.println(sender_device.getMac() + " " + sender_device.ARPcache.get(sender_device.getMac()));
		System.out.println("Sending ARP request to receiver");
		sender_device.sendARPrequest(receiver_device);
		System.out.println("ARP reply received from receiver (receiver MAC obtained)");
		System.out.println("New contents of ARP cache are :");
		System.out.println(sender_device.getMac() + " " + sender_device.ARPcache.get(sender_device.getMac()));
		System.out.println(receiver_device.getMac() + " " + sender_device.ARPcache.get(receiver_device.getMac()));
		System.out.println("\n\n");
		
		// animation.play();
		//System.out.println("\nThe data to be sent is ");
		
		System.out.println();
		System.out.println("------------DATA LINK AND PHYSICAL LAYER-------------");
		
		
		int i;
		messageNumber = 0;
		for (i = 0; i + 16 <= dataToBeSent.length(); i = i + 16) {
			String sxtBit = dataToBeSent.substring(i, i + 16);
			System.out.println("\nThe sixteen bits are " + sxtBit);
			long start = System.currentTimeMillis();
			messageNumber++;
			while (true) {
				sxtBit = (new CRCforDataLink()).senderCode(sxtBit);
				sender_device.data = sxtBit;
				if (sender_router.equals(receiver_router)) {
					if (sender_switch.equals(receiver_switch)) {
						if (sender_hub != null)
							sender_hub.sendDataToSwitch(switch_new, sender_hub, receiver_hub, sender_device,
									receiver_device);
						else
							sender_switch.sendDirectData(sender_device, receiver_device);
					} else {
						sender_switch.data = sxtBit;
						sender_router.getDataFromSenderSwitch(sender_switch.data);
						receiver_switch.data = sender_router.sendDataToReceiverSwitch();
						if (receiver_hub != null) {
							receiver_hub.data = receiver_switch.data;
							receiver_device.data = receiver_hub.data;
						} else
							receiver_device.data = receiver_switch.data;

					}
				} else {
					if (sender_hub != null) {
						sender_device.data = sxtBit;
						sender_hub.data = sender_device.data;
						sender_switch.data = sender_hub.data;
						sender_router.data = sender_switch.data;
					} else {
						sender_device.data = sxtBit;
						sender_switch.data = sender_device.data;
						sender_router.data = sender_switch.data;
					}
					receiver_router.data = sender_router.data;
					receiver_switch.data = receiver_router.data;
					if (receiver_hub != null) {
						receiver_hub.data = receiver_switch.data;
						receiver_device.data = receiver_hub.data;
					} else {
						receiver_device.data = receiver_switch.data;
					}
				}
				System.out.println("Data with sender and receiver " + sender_device.data + " " + receiver_device.data);
				CRCforDataLink crc = new CRCforDataLink();
				checkError = crc.iscorrect(receiver_device.getData());
				if (checkError == true) {
					System.out.println();
					System.out.println("Packet will be discarded, Sending NAK");
					ACKorNAK = "NAK";
					/*
					 * if(sender_router.routerNumber == receiver_router.routerNumber) ACKorNAK =
					 * "NAK_DL"; else ACKorNAK = "NAK_TL";
					 */
					animation.play();
				} else {
					System.out.println();
					System.out.println("Sending ACK");
					ACKorNAK = "ACK";
					/*
					 * if(sender_router.routerNumber == receiver_router.routerNumber) ACKorNAK =
					 * "ACK_DL"; else ACKorNAK = "ACK_TL";
					 */
					animation.play();
				}
				receiver_device.sendACKorNAK(checkError, sender_device);
				if (sender_device.ACKorNAK.equals("ACK")) {
					System.out.println("ACK successfully recieved\n");
					break;
				}

			}
			long end = System.currentTimeMillis();
			System.out.println();

			System.out.println("Total time = " + (end - start));
			System.out.println("TOTAL NUMBER OF HOPS FROM SENDER TO RECEIVER = "
					+ (numberOfHops + (receiver_router.routerNumber - sender_router.routerNumber - 1)));
			System.out.println();

		}
		if (dataToBeSent.length() % 16 != 0) {
			String bits = dataToBeSent.substring(i, dataToBeSent.length());
			for (int j = bits.length(); j < 16; j++) {
				bits = "0" + bits;
			}
			System.out.println("\nThe sixteen bits are " + bits);
			long start = System.currentTimeMillis();
			messageNumber++;
			while (true) {
				bits = (new CRCforDataLink()).senderCode(bits);
				sender_device.setData(bits);
				if (sender_router.equals(receiver_router)) {
					if (sender_switch.equals(receiver_switch)) {
						if (sender_hub != null)
							sender_hub.sendDataToSwitch(switch_new, sender_hub, receiver_hub, sender_device,
									receiver_device);
						else
							sender_switch.sendDirectData(sender_device, receiver_device);
					} else {
						sender_switch.data = bits;
						sender_router.getDataFromSenderSwitch(sender_switch.data);
						receiver_switch.data = sender_router.sendDataToReceiverSwitch();
						if (receiver_hub != null) {
							receiver_hub.data = receiver_switch.data;
							receiver_device.data = receiver_hub.data;
						} else
							receiver_device.data = receiver_switch.data;

					}
				} else {
					if (sender_hub != null) {
						sender_device.data = bits;
						sender_hub.data = sender_device.data;
						sender_switch.data = sender_hub.data;
						sender_router.data = sender_switch.data;
					} else {
						sender_device.data = bits;
						sender_switch.data = sender_device.data;
						sender_router.data = sender_switch.data;
					}
					receiver_router.data = sender_router.data;
					receiver_switch.data = receiver_router.data;
					if (receiver_hub != null) {
						receiver_hub.data = receiver_switch.data;
						receiver_device.data = receiver_hub.data;
					} else {
						receiver_device.data = receiver_switch.data;
					}
				}
				CRCforDataLink crc = new CRCforDataLink();
				boolean checkError = crc.iscorrect(receiver_device.getData());
				if (checkError == true) {
					System.out.println();
					System.out.println("Packet will be discarded, Sending NAK");
					ACKorNAK = "NAK";
					/*
					 * if(sender_router.routerNumber == receiver_router.routerNumber) ACKorNAK =
					 * "NAK_DL"; else ACKorNAK = "NAK_TL";
					 */
					animation.play();
				} else {
					System.out.println();
					System.out.println("Sending ACK");
					ACKorNAK = "ACK+DATA";
					/*
					 * if(sender_router.routerNumber == receiver_router.routerNumber) ACKorNAK =
					 * "ACK_DL"; else ACKorNAK = "ACK_TL";
					 */
					animation.play();

				}
				receiver_device.sendACKorNAK(checkError, sender_device);
				if (sender_device.ACKorNAK.equals("ACK")) {
					System.out.println("ACK successfully recieved\n");
					break;
				}
			}
			long end = System.currentTimeMillis();
			System.out.println();

			System.out.println("Total time = " + (end - start) + " ms");
			System.out.println("TOTAL NUMBER OF HOPS FROM SENDER TO RECEIVER = "
					+ (numberOfHops + (receiver_router.routerNumber - sender_router.routerNumber - 1)));
			System.out.println();
		}
		System.out.println("\n");
		
		
		
		
		if(processPortNumber==25) {
			Receiver_email re = new Receiver_email(senderEmailID, receiverEmailID,emailContentFromSender);
			DomainNameServer.storeDNSforEmail(receiverIP);
		}
		if(processPortNumber==80) {
			Receiver_search search = new Receiver_search(targetSearchEngine,searchKey,meaningForSearchKey);
			DomainNameServer.storeDNSforSearchEngines(receiverIP);
		}

	}

	class DrawPanel extends JPanel {

		private BufferedImage serverImage;

		public void play() {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			DrawPanel draw = new DrawPanel();
			draw.calculations();
			frame.getContentPane().add(draw);
			frame.setSize(1300, 680);
			frame.setVisible(true);

			try {
				endDeviceImage1 = ImageIO.read(new File("./src/mainCode/endDevice.jpg"));
				endDeviceImage2 = ImageIO.read(new File("./src/mainCode/endDevice2.png"));
				hubImage = ImageIO.read(new File("./src/mainCode/hub1.png"));
				switchImage = ImageIO.read(new File("./src/mainCode/switch.png"));
				routerImage = ImageIO.read(new File("./src/mainCode/router.png"));
				messageImage = ImageIO.read(new File("./src/mainCode/message.png"));
				serverImage = ImageIO.read(new File("./src/mainCode/webserver.png"));
				draw.repaint();
			} catch (IOException ex) {
				// handle exception...
				ex.printStackTrace();
				System.exit(0);
			}
			draw.repaint();

			numberOfHops = 0;
			if (y_Shub != 0) {
				for (a1 = y_Sender; a1 >= y_Shub; a1--) {
					draw.repaint();
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				numberOfHops++;
				a1 = 0;
				if (x_Shub == x_Rhub) {
					for (q1 = y_Shub; q1 <= y_Receiver; q1++) {
						draw.repaint();
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					numberOfHops++;
					q1 = 0;
				} else {
					for (b1 = y_Shub; b1 >= y_Sswitch; b1--) {
						draw.repaint();
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					numberOfHops++;
					b1 = 0;
				}
			} else {
				for (c1 = y_Sender; c1 >= y_Sswitch; c1--) {
					draw.repaint();
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				numberOfHops++;
				c1 = 0;
			}
			if (x_Shub != x_Rhub) {
				if (x_Sswitch == x_Rswitch) {
					if (y_Rhub != 0) {
						for (d1 = y_Sswitch; d1 <= y_Rhub; d1++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						numberOfHops++;
						d1 = 0;
						for (e1 = y_Rhub; e1 >= y_Receiver; e1++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						numberOfHops++;
						e1 = 0;
					} else {
						for (f1 = y_Sswitch; f1 <= y_Receiver; f1++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						numberOfHops++;
						f1 = 0;
					}
				} else {
					for (g1 = y_Sswitch; g1 >= y_Srouter; g1--) {
						draw.repaint();
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					numberOfHops++;
					g1 = 0;

					if (x_Srouter == x_Rrouter) {
						for (h1 = y_Srouter; h1 <= y_Rswitch; h1++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						numberOfHops++;
						h1 = 0;
						if (x_Rhub != 0) {
							for (i1 = y_Rswitch; i1 <= y_Rhub; i1++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							numberOfHops++;
							i1 = 0;
							for (j1 = y_Rhub; j1 <= y_Receiver; j1++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							numberOfHops++;
							j1 = 0;
						} else {
							for (k1 = y_Rswitch; k1 <= y_Receiver; k1++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							numberOfHops++;
							k1 = 0;
						}
					} else {
						for (l1 = x_Srouter; l1 <= x_Rrouter; l1++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						numberOfHops++;
						l1 = 0;
						for (m1 = y_Rrouter; m1 <= y_Rswitch; m1++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						numberOfHops++;
						m1 = 0;
						if (x_Rhub != 0) {
							for (n1 = y_Rswitch; n1 <= y_Rhub; n1++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							numberOfHops++;
							n1 = 0;
							for (o1 = y_Rhub; o1 <= y_Receiver; o1++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							numberOfHops++;
							o1 = 0;
						} else {
							for (p1 = y_Rswitch; p1 <= y_Receiver; p1++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							numberOfHops++;
							p1 = 0;
						}
					}
				}
			}

			// send back ACK or NAK
			if (y_Rhub != 0) {
				for (a2 = y_Receiver; a2 >= y_Rhub; a2--) {
					draw.repaint();
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				a2 = 0;
				if (x_Shub == x_Rhub) {
					for (q2 = y_Rhub; q2 <= y_Sender; q2++) {
						draw.repaint();
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					q2 = 0;
				} else {
					for (b2 = y_Rhub; b2 >= y_Rswitch; b2--) {
						draw.repaint();
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					b2 = 0;
				}
			} else {
				for (c2 = y_Receiver; c2 >= y_Rswitch; c2--) {
					draw.repaint();
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				c2 = 0;
			}
			if (x_Shub != x_Rhub) {
				if (x_Rswitch == x_Sswitch) {
					if (y_Shub != 0) {
						for (d2 = y_Rswitch; d2 <= y_Shub; d2++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						d2 = 0;
						for (e2 = y_Shub; e2 >= y_Sender; e2++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						e2 = 0;
					} else {
						for (f2 = y_Rswitch; f2 <= y_Sender; f2++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						f2 = 0;
					}
				} else {
					for (g2 = y_Rswitch; g2 >= y_Rrouter; g2--) {
						draw.repaint();
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					g2 = 0;

					if (x_Srouter == x_Rrouter) {
						for (h2 = y_Rrouter; h2 <= y_Sswitch; h2++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						h2 = 0;
						if (x_Shub != 0) {
							for (i2 = y_Sswitch; i2 <= y_Shub; i2++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							i2 = 0;
							for (j2 = y_Shub; j2 <= y_Sender; j2++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							j2 = 0;
						} else {
							for (k2 = y_Sswitch; k2 <= y_Sender; k2++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							k2 = 0;
						}
					} else {
						for (l2 = x_Rrouter; l2 >= x_Srouter; l2--) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						l2 = 0;
						for (m2 = y_Srouter; m2 <= y_Sswitch; m2++) {
							draw.repaint();
							try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						m2 = 0;
						if (x_Shub != 0) {
							for (n2 = y_Sswitch; n2 <= y_Shub; n2++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							n2 = 0;
							for (o2 = y_Shub; o2 <= y_Sender; o2++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							o2 = 0;
						} else {
							for (p2 = y_Sswitch; p2 <= y_Sender; p2++) {
								draw.repaint();
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							p2 = 0;
						}
					}
				}
			}

		}

		public void calculations() {
			for (int r = 0; r < numberOfRouters; r++) {
				switches = routers.get(r).getConnectedSwitches();
				for (int s = 0; s < switches.size(); s++) {
					hubs = switches.get(s).hubs;
					if (hubs.size() > 0) {
						for (int h = 0; h < hubs.size(); h++) {
							objectOfDevices = hubs.get(h).devicesConnected;
							for (int d = 0; d < objectOfDevices.length; d++) {
								if (senderIP.equals(objectOfDevices[d].IP)) {
									sender_router = routers.get(r);
									sender_switch = switches.get(s);
									sender_hub = hubs.get(h);
									sender_device = objectOfDevices[d];
								}
								if (receiverIP.equals(objectOfDevices[d].IP)) {
									receiver_router = routers.get(r);
									receiver_switch = switches.get(s);
									receiver_hub = hubs.get(h);
									receiver_device = objectOfDevices[d];
								}
							}
						}
					} else {
						objectOfDevices = switches.get(s).devicesDirectlyConnected;
						for (int d = 0; d < objectOfDevices.length; d++) {
							if (senderIP.equals(objectOfDevices[d].IP)) {
								sender_router = routers.get(r);
								sender_switch = switches.get(s);
								sender_hub = null;
								sender_device = objectOfDevices[d];
							}
							if (receiverIP.equals(objectOfDevices[d].IP)) {
								receiver_router = routers.get(r);
								receiver_switch = switches.get(s);
								receiver_hub = null;
								receiver_device = objectOfDevices[d];
							}
						}
					}
				}
			}
		}

		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.BLACK);

			int routerDistance = (this.getWidth()) / (numberOfRouters + 1);
			for (int r = 0; r < numberOfRouters; r++) {
				g.drawImage(routerImage, routerDistance * (r + 1), 100, 60, 60, this);
				if (r < numberOfRouters - 1)
					g.drawLine(routerDistance * (r + 1) + 20, 120, routerDistance * (r + 2), 120);
				if (routers.get(r).equals(sender_router)) {
					x_Srouter = routerDistance * (r + 1);
					y_Srouter = 100;
				}
				if (routers.get(r).equals(receiver_router)) {
					x_Rrouter = routerDistance * (r + 1);
					y_Rrouter = 100;
				}

				switches = routers.get(r).getConnectedSwitches();
				if (switches.size() != 0) {
					int switchDistance = routerDistance / (switches.size() + 1);
					for (int s = 0; s < switches.size(); s++) {
						g.drawImage(switchImage, routerDistance * (r) + switchDistance * (s + 1), 250, 50, 50, this);
						g.drawLine(routerDistance * (r + 1) + 20, 120,
								routerDistance * (r) + switchDistance * (s + 1) + 20, 230);
						if (switches.get(s).equals(sender_switch)) {
							x_Sswitch = routerDistance * (r) + switchDistance * (s + 1);
							y_Sswitch = 250;
						}
						if (switches.get(s).equals(receiver_switch)) {
							x_Rswitch = routerDistance * (r) + switchDistance * (s + 1);
							y_Rswitch = 250;
						}

						hubs = switches.get(s).hubs;
						if (hubs.size() > 0) {
							int hubDistance = switchDistance / (hubs.size() + 1);
							for (int h = 0; h < hubs.size(); h++) {
								g.drawImage(hubImage,
										routerDistance * (r) + switchDistance * (s) + hubDistance * (h + 1), 400, 50,
										50, this);
								g.drawLine(routerDistance * (r) + switchDistance * (s + 1) + 20, 270,
										routerDistance * (r) + switchDistance * (s) + hubDistance * (h + 1) + 20, 380);

								if (sender_hub != null && hubs.get(h).equals(sender_hub)) {
									x_Shub = routerDistance * (r) + switchDistance * (s) + hubDistance * (h + 1);
									y_Shub = 400;
								}
								if (receiver_hub != null && hubs.get(h).equals(receiver_hub)) {
									x_Rhub = routerDistance * (r) + switchDistance * (s) + hubDistance * (h + 1);
									y_Rhub = 400;
								}

								objectOfDevices = hubs.get(h).devicesConnected;
								int deviceDistance = hubDistance / (objectOfDevices.length + 1);
								for (int d = 0; d < objectOfDevices.length; d++) {
									g.drawImage(endDeviceImage1, routerDistance * (r) + switchDistance * (s)
											+ hubDistance * (h) + deviceDistance * (d + 1), 550, 30, 30, this);
									g.drawLine(routerDistance * (r) + switchDistance * (s) + hubDistance * (h + 1) + 20,
											420, routerDistance * (r) + switchDistance * (s) + hubDistance * (h)
													+ deviceDistance * (d + 1) + 20,
											530);
									g.drawString(objectOfDevices[d].deviceName + "", routerDistance * (r)
											+ switchDistance * (s) + hubDistance * (h) + deviceDistance * (d + 1), 600);
									if (objectOfDevices[d].equals(sender_device)) {
										x_Sender = routerDistance * (r) + switchDistance * (s) + hubDistance * (h)
												+ deviceDistance * (d + 1);
										y_Sender = 550;
										g.drawString(objectOfDevices[d].deviceName + "-> Sender",
												routerDistance * (r) + switchDistance * (s) + deviceDistance * (d + 1), 620);
									}
									if (objectOfDevices[d].equals(receiver_device)) {
										x_Receiver = routerDistance * (r) + switchDistance * (s) + hubDistance * (h)
												+ deviceDistance * (d + 1);
										y_Receiver = 550;
										g.drawString(objectOfDevices[d].deviceName + "-> Receiver/Server",
												routerDistance * (r) + switchDistance * (s) + deviceDistance * (d + 1), 620);
									}
								}
							}
						} else {
							objectOfDevices = switches.get(s).devicesDirectlyConnected;
							int deviceDistance = switchDistance / (objectOfDevices.length + 1);
							for (int d = 0; d < objectOfDevices.length; d++) {
								g.drawImage(endDeviceImage1,
										routerDistance * (r) + switchDistance * (s) + deviceDistance * (d + 1), 550, 30,
										30, this);
								g.drawLine(routerDistance * (r) + switchDistance * (s + 1) + 20, 270,
										routerDistance * (r) + switchDistance * (s) + deviceDistance * (d + 1) + 20,
										530);
								g.drawString(objectOfDevices[d].deviceName + "",
										routerDistance * (r) + switchDistance * (s) + deviceDistance * (d + 1), 600);
								if (objectOfDevices[d].equals(sender_device)) {
									x_Sender = routerDistance * (r) + switchDistance * (s) + deviceDistance * (d + 1);
									y_Sender = 550;
									g.drawString(objectOfDevices[d].deviceName + "-> Sender",
											routerDistance * (r) + switchDistance * (s) + deviceDistance * (d + 1), 620);
								}
								if (objectOfDevices[d].equals(receiver_device)) {
									x_Receiver = routerDistance * (r) + switchDistance * (s) + deviceDistance * (d + 1);
									y_Receiver = 550;
									g.drawString(objectOfDevices[d].deviceName + "-> Receiver/Server",
											routerDistance * (r) + switchDistance * (s) + deviceDistance * (d + 1), 620);
								}
							}
						}
					}

				}
			}

			double m, c;

			if (y_Shub != 0) {

				m = (double) (y_Shub - y_Sender) / (double) (x_Shub - x_Sender);
				c = y_Sender - m * x_Sender;
				// System.out.println(m+" "+c);
				if (a1 != 0) {
					g.drawImage(messageImage, (int) ((a1 - c) / m), a1, 30, 20, this);
					g.drawString(messageNumber + "", (int) ((a1 - c) / m), a1);
				}
				if (x_Shub == x_Rhub) {
					m = (double) (y_Shub - y_Receiver) / (double) (x_Shub - x_Receiver);
					c = y_Shub - m * x_Shub;
					// System.out.println(m+" "+c);
					if (q1 != 0) {
						g.drawImage(messageImage, (int) ((q1 - c) / m), q1, 30, 20, this);
						g.drawString(messageNumber + "", (int) ((q1 - c) / m), q1);
					}
				} else {
					m = (double) (y_Shub - y_Sswitch) / (double) (x_Shub - x_Sswitch);
					c = y_Shub - m * x_Shub;
					// System.out.println(m+" "+c);
					if (b1 != 0) {
						g.drawImage(messageImage, (int) ((b1 - c) / m), b1, 30, 20, this);
						g.drawString(messageNumber + "", (int) ((b1 - c) / m), b1);
					}
				}

			} else {
				m = (double) (y_Sender - y_Sswitch) / (double) (x_Sender - x_Sswitch);
				c = y_Sender - m * x_Sender;
				if (c1 != 0) {
					g.drawImage(messageImage, (int) ((c1 - c) / m), c1, 30, 20, this);
					g.drawString(messageNumber + "", (int) ((c1 - c) / m), c1);
				}
			}
			if (x_Sswitch == x_Rswitch) {
				if (y_Rhub != 0) {
					m = (double) (y_Rhub - y_Sswitch) / (double) (x_Rhub - x_Sswitch);
					c = y_Rhub - m * x_Rhub;
					if (d1 != 0) {
						g.drawImage(messageImage, (int) ((d1 - c) / m), d1, 30, 20, this);
						g.drawString(messageNumber + "", (int) ((d1 - c) / m), d1);
					}
					m = (double) (y_Rhub - y_Receiver) / (double) (x_Rhub - x_Receiver);
					c = y_Rhub - m * x_Rhub;
					if (e1 != 0) {
						g.drawImage(messageImage, (int) ((e1 - c) / m), e1, 30, 20, this);
						g.drawString(messageNumber + "", (int) ((e1 - c) / m), e1);
					}
				} else {
					m = (double) (y_Sswitch - y_Receiver) / (double) (x_Sswitch - x_Receiver);
					c = y_Sswitch - m * x_Sswitch;
					if (f1 != 0) {
						g.drawImage(messageImage, (int) ((f1 - c) / m), f1, 30, 20, this);
						g.drawString(messageNumber + "", (int) ((f1 - c) / m), f1);
					}
				}
			} else {
				m = (double) (y_Sswitch - y_Srouter) / (double) (x_Sswitch - x_Srouter);
				c = y_Sswitch - m * x_Sswitch;
				if (g1 != 0) {
					g.drawImage(messageImage, (int) ((g1 - c) / m), g1, 30, 20, this);
					g.drawString(messageNumber + "", (int) ((g1 - c) / m), g1);
				}
				if (x_Srouter == x_Rrouter) {
					m = (double) (y_Rswitch - y_Srouter) / (double) (x_Rswitch - x_Srouter);
					c = y_Srouter - m * x_Srouter;
					if (h1 != 0) {
						g.drawImage(messageImage, (int) ((h1 - c) / m), h1, 30, 20, this);
						g.drawString(messageNumber + "", (int) ((h1 - c) / m), h1);
					}
					if (x_Rhub != 0) {
						m = (double) (y_Rswitch - y_Rhub) / (double) (x_Rswitch - x_Rhub);
						c = y_Rhub - m * x_Rhub;
						if (i1 != 0) {
							g.drawImage(messageImage, (int) ((i1 - c) / m), i1, 30, 20, this);
							g.drawString(messageNumber + "", (int) ((i1 - c) / m), i1);
						}
						m = (double) (y_Receiver - y_Rhub) / (double) (x_Receiver - x_Rhub);
						c = y_Rhub - m * x_Rhub;
						if (j1 != 0) {
							g.drawImage(messageImage, (int) ((j1 - c) / m), j1, 30, 20, this);
							g.drawString(messageNumber + "", (int) ((j1 - c) / m), j1);
						}
					} else {
						m = (double) (y_Receiver - y_Rswitch) / (double) (x_Receiver - x_Rswitch);
						c = y_Rswitch - m * x_Rswitch;
						if (k1 != 0) {
							g.drawImage(messageImage, (int) ((k1 - c) / m), k1, 30, 20, this);
							g.drawString(messageNumber + "", (int) ((k1 - c) / m), k1);
						}
					}
				} else {
					c = y_Srouter;
					if (l1 != 0) {
						g.drawImage(messageImage, (int) l1, (int) c, 30, 20, this);
						g.drawString(messageNumber + "", l1, (int) c);
					}
					m = (double) (y_Rrouter - y_Rswitch) / (double) (x_Rrouter - x_Rswitch);
					c = y_Rswitch - m * x_Rswitch;
					if (m1 != 0) {
						g.drawImage(messageImage, (int) ((m1 - c) / m), m1, 30, 20, this);
						g.drawString(messageNumber + "", (int) ((m1 - c) / m), m1);
					}
					if (x_Rhub != 0) {
						m = (double) (y_Rswitch - y_Rhub) / (double) (x_Rswitch - x_Rhub);
						c = y_Rhub - m * x_Rhub;
						if (n1 != 0) {
							g.drawImage(messageImage, (int) ((n1 - c) / m), n1, 30, 20, this);
							g.drawString(messageNumber + "", (int) ((n1 - c) / m), n1);
						}
						m = (double) (y_Receiver - y_Rhub) / (double) (x_Receiver - x_Rhub);
						c = y_Rhub - m * x_Rhub;
						if (o1 != 0) {
							g.drawImage(messageImage, (int) ((o1 - c) / m), o1, 30, 20, this);
							g.drawString(messageNumber + "", (int) ((o1 - c) / m), o1);
						}
					} else {
						m = (double) (y_Receiver - y_Rswitch) / (double) (x_Receiver - x_Rswitch);
						c = y_Rswitch - m * x_Rswitch;
						if (p1 != 0) {
							g.drawImage(messageImage, (int) ((p1 - c) / m), p1, 30, 20, this);
							g.drawString(messageNumber + "", (int) ((p1 - c) / m), p1);
						}
					}
				}
			}

			// for ACK or NAK
			if (y_Rhub != 0) {
				m = (double) (y_Rhub - y_Receiver) / (double) (x_Rhub - x_Receiver);
				c = y_Receiver - m * x_Receiver;
				// System.out.println(m+" "+c);
				if (a2 != 0) {
					g.drawImage(messageImage, (int) ((a2 - c) / m), a2, 30, 20, this);
					g.drawString(messageNumber + " " + ACKorNAK, (int) ((a2 - c) / m), a2);
				}
				if (x_Rhub == x_Shub) {
					m = (double) (y_Rhub - y_Sender) / (double) (x_Rhub - x_Sender);
					c = y_Rhub - m * x_Rhub;
					// System.out.println(m+" "+c);
					if (q2 != 0) {
						g.drawImage(messageImage, (int) ((q2 - c) / m), q2, 30, 20, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((q2 - c) / m), q2);
					}
				} else {
					m = (double) (y_Rhub - y_Rswitch) / (double) (x_Rhub - x_Rswitch);
					c = y_Rhub - m * x_Rhub;
					// System.out.println(m+" "+c);
					if (b2 != 0) {
						g.drawImage(messageImage, (int) ((b2 - c) / m), b2, 30, 20, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((b2 - c) / m), b2);
					}
				}

			} else {
				m = (double) (y_Receiver - y_Rswitch) / (double) (x_Receiver - x_Rswitch);
				c = y_Receiver - m * x_Receiver;
				if (c2 != 0) {
					g.drawImage(messageImage, (int) ((c2 - c) / m), c2, 30, 20, this);
					g.drawString(messageNumber + " " + ACKorNAK, (int) ((c2 - c) / m), c2);
				}
			}
			if (x_Sswitch == x_Rswitch) {
				if (y_Shub != 0) {
					m = (double) (y_Shub - y_Rswitch) / (double) (x_Shub - x_Rswitch);
					c = y_Shub - m * x_Shub;
					if (d2 != 0) {
						g.drawImage(messageImage, (int) ((d2 - c) / m), d2, 30, 20, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((d2 - c) / m), d2);
					}
					m = (double) (y_Shub - y_Sender) / (double) (x_Shub - x_Sender);
					c = y_Shub - m * x_Shub;
					if (e2 != 0) {
						g.drawImage(messageImage, (int) ((e2 - c) / m), e2, 30, 20, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((e2 - c) / m), e2);
					}
				} else {
					m = (double) (y_Rswitch - y_Sender) / (double) (x_Rswitch - x_Sender);
					c = y_Rswitch - m * x_Rswitch;
					if (f2 != 0) {
						g.drawImage(messageImage, (int) ((f2 - c) / m), f2, 30, 20, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((f2 - c) / m), f2);
					}
				}
			} else {
				m = (double) (y_Rswitch - y_Rrouter) / (double) (x_Rswitch - x_Rrouter);
				c = y_Rswitch - m * x_Rswitch;
				if (g2 != 0) {
					g.drawImage(messageImage, (int) ((g2 - c) / m), g2, 30, 20, this);
					g.drawString(messageNumber + " " + ACKorNAK, (int) ((g2 - c) / m), g2);
				}
				if (x_Srouter == x_Rrouter) {
					m = (double) (y_Sswitch - y_Rrouter) / (double) (x_Sswitch - x_Rrouter);
					c = y_Rrouter - m * x_Rrouter;
					if (h2 != 0) {
						g.drawImage(messageImage, (int) ((h2 - c) / m), h2, 30, 20, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((h2 - c) / m), h2);
					}
					if (x_Shub != 0) {
						m = (double) (y_Sswitch - y_Shub) / (double) (x_Sswitch - x_Shub);
						c = y_Shub - m * x_Shub;
						if (i2 != 0) {
							g.drawImage(messageImage, (int) ((i2 - c) / m), i2, 30, 20, this);
							g.drawString(messageNumber + " " + ACKorNAK, (int) ((i2 - c) / m), i2);
						}
						m = (double) (y_Sender - y_Shub) / (double) (x_Sender - x_Shub);
						c = y_Shub - m * x_Shub;
						if (j2 != 0) {
							g.drawImage(messageImage, (int) ((j2 - c) / m), j2, 30, 20, this);
							g.drawString(messageNumber + " " + ACKorNAK, (int) ((j2 - c) / m), j2);
						}
					} else {
						m = (double) (y_Sender - y_Sswitch) / (double) (x_Sender - x_Sswitch);
						c = y_Sswitch - m * x_Sswitch;
						if (k2 != 0) {
							g.drawImage(messageImage, (int) ((k2 - c) / m), k2, 30, 20, this);
							g.drawString(messageNumber + " " + ACKorNAK, (int) ((k2 - c) / m), k2);
						}
					}
				} else {
					c = y_Rrouter;
					if (l2 != 0) {
						g.drawImage(messageImage, (int) l2, (int) c, 30, 20, this);
						g.drawString(messageNumber + " " + ACKorNAK, l2, (int) c);
					}
					m = (double) (y_Srouter - y_Sswitch) / (double) (x_Srouter - x_Sswitch);
					c = y_Sswitch - m * x_Sswitch;
					if (m2 != 0) {
						g.drawImage(messageImage, (int) ((m2 - c) / m), m2, 30, 20, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((m2 - c) / m), m2);
					}
					if (x_Shub != 0) {
						m = (double) (y_Sswitch - y_Shub) / (double) (x_Sswitch - x_Shub);
						c = y_Shub - m * x_Shub;
						if (n2 != 0) {
							g.drawImage(messageImage, (int) ((n2 - c) / m), n2, 30, 20, this);
							g.drawString(messageNumber + " " + ACKorNAK, (int) ((n2 - c) / m), n2);
						}
						m = (double) (y_Sender - y_Shub) / (double) (x_Sender - x_Shub);
						c = y_Shub - m * x_Shub;
						if (o2 != 0) {
							g.drawImage(messageImage, (int) ((o2 - c) / m), o2, 30, 20, this);
							g.drawString(messageNumber + " " + ACKorNAK, (int) ((o2 - c) / m), o2);
						}
					} else {
						m = (double) (y_Sender - y_Sswitch) / (double) (x_Sender - x_Sswitch);
						c = y_Sswitch - m * x_Sswitch;
						if (p2 != 0) {
							g.drawImage(messageImage, (int) ((p2 - c) / m), p2, 30, 20, this);
							g.drawString(messageNumber + " " + ACKorNAK, (int) ((p2 - c) / m), p2);
						}
					}
				}
			}
		}
	}

}
