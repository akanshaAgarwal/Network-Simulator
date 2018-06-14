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
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SampleMain {

	private static BufferedImage endDeviceImage1, endDeviceImage2, hubImage, switchImage, messageImage;
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
	private static char recieverDevice;
	private static int senderNumber;
	private static int recieverNumber;
	private static String data;
	private static Hub senderHub;
	private static Hub recieverHub;
	private static EndDevices sender_device;
	private static EndDevices reciever_device;
	private static EndDevices[] devices;
	private int x_sender, y_sender, x_reciever, y_reciever, x_switch, y_switch, x_shub, y_shub, x_rhub, y_rhub;
	static int messageNumber = 0;

	private int i, j, a, b, e, d, f, z;
	private int i1, j1, a1, b1, e1, d1, f1, z1;
	private static boolean checkError;
	private static String ACKorNAK = "";
	

	public static void main(String[] args) {
		Scanner sn = new Scanner(System.in);

		SampleMain m = new SampleMain();
		DrawPanel animation = m.new DrawPanel();
		System.out.println("Enter the number of routers you want in your network");
		numberOfRouters = sn.nextInt();
		for (int r=0;r<numberOfRouters;r++) {
			routers.add(new Router(r,((r+1)*10)+".0.0.0"));
			System.out.println("Enter the number of switches in ROUTER "+r);
			numberOfSwitches=sn.nextInt();
			switches=new ArrayList<>();
			for(int s=0;s<numberOfSwitches;s++) {
				switches.add(new Switch(s));
			}
			routers.get(r).storeConnectedSwitches(switches);
		}
		for(int r=0;r<numberOfRouters;r++) {
			System.out.println("ROUTER "+r);
			switches=routers.get(0).getConnectedSwitches();
			if(switches!=null || switches.size()>0) {
				for(int s=0;s<switches.size();s++) {
					System.out.println("SWITCH "+s);
					hubs = new ArrayList<>();
					System.out.println("Enter the number of hubs you want to connect to the switch "+s+" of router "+r+" (enter 0 if no hub)");
					numberOfHubs = sn.nextInt();

					// for hubs>0
					if (numberOfHubs > 0) {
						System.out.println("Now lets enter the devices for each hub");
						int total_devices = 0;
						for (int i = 0; i < numberOfHubs; i++) {
							Hub newHub = new Hub(i);
							System.out.println("Enter the number of devices to be connected to Hub " + i);
							numberOfDevices = sn.nextInt();
							objectOfDevices = new EndDevices[numberOfDevices];
							for (int j = total_devices + 0; j < total_devices + numberOfDevices; j++) {
								objectOfDevices[j - total_devices] = new EndDevices(j, (char) (65 + j), (r+1)*10+".0.0."+(j+2));
							}
							total_devices += numberOfDevices;
							newHub.storeDevicesConnected(objectOfDevices);
							hubs.add(newHub);
						}
						
						System.out.println("Devices created");
						System.out.println();
						System.out.println("The list of IP Addresses of devices are");
						for (int i = 0; i < numberOfHubs; i++) {
							System.out.print("For hub " + i + " : ");
							EndDevices[] devices = hubs.get(i).getConnectedDevices();
							for (int j = 0; j < devices.length; j++) {
								System.out.print(devices[j].getDeviceName() + " ");
							}
							System.out.println();
						}

						System.out.println();
						System.out.println("Select the sender device from following (Case sensitive)");
						senderDevice = sn.next().charAt(0);
						System.out.println("Select the reciever device from following");
						recieverDevice = sn.next().charAt(0);
						System.out.println();
						senderNumber = (int) senderDevice - 65;
						recieverNumber = (int) recieverDevice - 65;
						System.out.println("The data you want to send is :");
						data = "101001001001000011110101";
						System.out.println();
						// encoderOnlyPulse e = new encoderOnlyPulse(data);

						System.out.println("Now the internal working");
						senderHub = null;
						recieverHub = null;
						sender_device = null;
						reciever_device = null;
						for (int i = 0; i < numberOfHubs; i++) {
							devices = hubs.get(i).getConnectedDevices();
							for (int j = 0; j < devices.length; j++) {
								if (devices[j].getDeviceName() == senderDevice) {
									senderHub = hubs.get(i);
									sender_device = devices[j];
								}
								if (devices[j].getDeviceName() == recieverDevice) {
									recieverHub = hubs.get(i);
									reciever_device = devices[j];
								}
							}
						}
						switch_new = switches.get(s);

						int i;
						messageNumber = 0;
						for (i = 0; i + 16 <= data.length(); i = i + 16) {
							String sxtBit = data.substring(i, i + 16);
							System.out.println("\nThe sixteen bits are " + sxtBit);
							long start = System.currentTimeMillis();
							messageNumber++;
							while (true) {
								sxtBit = (new CRCforDataLink()).senderCode(sxtBit);
								sender_device.setData(sxtBit);
								senderHub.sendDataToSwitch(switch_new, senderHub, recieverHub, sender_device, reciever_device);
								CRCforDataLink crc = new CRCforDataLink();
								checkError = crc.iscorrect(reciever_device.getData());
								if (checkError == true) {
									System.out.println();
									System.out.println("Packet will be discarded, Sending NAK");
									ACKorNAK = "NAK";
									animation.play();
								} else {
									System.out.println();
									System.out.println("Sending ACK");
									ACKorNAK = "ACK";
									animation.play();
								}
								reciever_device.sendACKorNAK(checkError, sender_device);
								if (sender_device.ACKorNAK.equals("ACK")) {
									System.out.println("ACK successfully recieved\n");
									break;
								}

							}
							long end = System.currentTimeMillis();
							System.out.println();

							System.out.println("Total time = " + (end - start));
							System.out.println();

						}
						if (data.length() % 16 != 0) {
							String bits = data.substring(i, data.length());
							for (int j = bits.length(); j < 16; j++) {
								bits = "0" + bits;
							}
							System.out.println("\nThe sixteen bits are " + bits);
							long start = System.currentTimeMillis();
							messageNumber++;
							while (true) {
								bits = (new CRCforDataLink()).senderCode(bits);
								sender_device.setData(bits);
								senderHub.sendDataToSwitch(switch_new, senderHub, recieverHub, sender_device, reciever_device);
								CRCforDataLink crc = new CRCforDataLink();
								boolean checkError = crc.iscorrect(reciever_device.getData());
								if (checkError == true) {
									System.out.println();
									System.out.println("Packet will be discarded, Sending NAK");
									ACKorNAK = "NAK";
									animation.play();
								} else {
									System.out.println();
									System.out.println("Sending ACK");
									ACKorNAK = "ACK";
									animation.play();

								}
								reciever_device.sendACKorNAK(checkError, sender_device);
								if (sender_device.ACKorNAK.equals("ACK")) {
									System.out.println("ACK successfully recieved\n");
									break;
								}
							}
							long end = System.currentTimeMillis();
							System.out.println();

							System.out.println("Total time = " + (end - start) + " ms");
							System.out.println();
						}

					}

					if (numberOfHubs == 0) {
						System.out.println("Enter the number of devices you want to connect to the switch");
						numberOfDevices = sn.nextInt();
						objectOfDevices = new EndDevices[numberOfDevices];
						for (int j = 0; j < numberOfDevices; j++) {
							objectOfDevices[j] = new EndDevices(j, (char) (65 + j), (r+1)*10+".0.0."+(j+2));
						}
						System.out.println("The list of devices are");
						for (int i = 0; i < numberOfDevices; i++) {
							System.out.print(objectOfDevices[i].getDeviceName() + " ");
						}
						System.out.println();

						System.out.println();
						System.out.println("Select the sender device from following (Case sensitive)");
						senderDevice = sn.next().charAt(0);
						System.out.println("Select the reciever device from following");
						recieverDevice = sn.next().charAt(0);
						System.out.println();
						senderNumber = (int) senderDevice - 65;
						recieverNumber = (int) recieverDevice - 65;
						System.out.println("The data you want to send is :");
						data = "101001001001000011110101";
						System.out.println(data);
						objectOfDevices[senderNumber].setData(data);
						System.out.println();
						System.out.println("Now the internal working");
						switch_new = switches.get(s);

						int i = 0;
						messageNumber = 0;
						for (i = 0; i + 16 <= data.length(); i = i + 16) {
							String sxtBit = data.substring(i, i + 16);
							System.out.println("\nThe sixteen bits are " + sxtBit);
							long start = System.currentTimeMillis();
							messageNumber++;
							while (true) {
								sxtBit = (new CRCforDataLink()).senderCode(sxtBit);
								objectOfDevices[senderNumber].setData(sxtBit);
								switch_new.addToDirectConnectionTable(objectOfDevices[senderNumber]);
								System.out.println("Device added to the table in switch");
								switch_new.sendDirectData(objectOfDevices[senderNumber], objectOfDevices[recieverNumber]);
								CRCforDataLink crc = new CRCforDataLink();
								boolean checkError = crc.iscorrect(objectOfDevices[recieverNumber].getData());
								if (checkError == true) {
									System.out.println();
									System.out.println("Packet will be discarded, Sending NAK");
									ACKorNAK = "NAK";
									animation.play();

								} else {
									System.out.println();
									System.out.println("Sending ACK");
									ACKorNAK = "ACK";
									animation.play();

								}
								objectOfDevices[recieverNumber].sendACKorNAK(checkError, objectOfDevices[senderNumber]);
								if (objectOfDevices[senderNumber].ACKorNAK.equals("ACK")) {
									System.out.println("ACK successfully recieved\n");
									break;
								}

							}
							long end = System.currentTimeMillis();
							System.out.println();

							System.out.println("Total time = " + (end - start));
							System.out.println();

						}
						if (data.length() % 16 != 0) {
							String bits = data.substring(i, data.length());
							for (int j = bits.length(); j < 16; j++) {
								bits = "0" + bits;
							}
							System.out.println("\nThe sixteen bits are " + bits);
							long start = System.currentTimeMillis();
							messageNumber++;
							while (true) {
								bits = (new CRCforDataLink()).senderCode(bits);
								objectOfDevices[recieverNumber].setData(bits);
								switch_new.addToDirectConnectionTable(objectOfDevices[senderNumber]);
								System.out.println("Device added to the table in switch");
								switch_new.sendDirectData(objectOfDevices[senderNumber], objectOfDevices[recieverNumber]);
								CRCforDataLink crc = new CRCforDataLink();
								boolean checkError = crc.iscorrect(objectOfDevices[recieverNumber].getData());
								if (checkError == true) {
									System.out.println();
									System.out.println("Packet will be discarded, Sending NAK");
									ACKorNAK = "NAK";
									animation.play();
								} else {
									System.out.println();
									System.out.println("Sending ACK");
									ACKorNAK = "ACK";
									animation.play();
								}
								objectOfDevices[recieverNumber].sendACKorNAK(checkError, objectOfDevices[senderNumber]);
								if (objectOfDevices[senderNumber].ACKorNAK.equals("ACK")) {
									System.out.println("ACK successfully recieved\n");
									break;
								}

							}
							long end = System.currentTimeMillis();
							System.out.println();

							System.out.println("Total time = " + (end - start) + " ms");
							System.out.println();
						}

					}

				}
			}

		}
		
		
	}

	class DrawPanel extends JPanel {

		private BufferedImage routerImage;

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
				draw.repaint();
			} catch (IOException ex) {
				// handle exception...
				ex.printStackTrace();
				System.exit(0);
			}

			if (numberOfHubs > 0) {

				if (x_shub == x_rhub) {
					for (f = y_sender; f >= y_shub; f--) {
						draw.repaint();
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					f=0;
					for (z = y_shub; z <= y_reciever; z++) {
						draw.repaint();
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					z=0;
					for (z1 = y_reciever; z1 >= y_rhub; z1--) {
						draw.repaint();
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					z1=0;
					for (f1 = y_shub; f1 <= y_sender; f1++) {
						draw.repaint();
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					f1=0;

				} else {
					for (a = y_sender; a >= y_shub; a--) {
						draw.repaint();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					a=0;
					for (b = y_shub; b >= y_switch; b--) {
						draw.repaint();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					b=0;
					for (d = y_switch; d <= y_rhub; d++) {
						draw.repaint();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					d=0;
					for (e = y_rhub; e <= y_reciever; e++) {
						draw.repaint();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					e=0;
					for (e1 = y_reciever; e1 >= y_rhub; e1--) {
						draw.repaint();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					e1=0;
					for (d1 = y_rhub; d1 >= y_switch; d1--) {
						draw.repaint();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					d1=0;
					for (b1 = y_switch; b1 <= y_shub; b1++) {
						draw.repaint();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					b1=0;
					for (a1 = y_shub; a1 <= y_sender; a1++) {
						draw.repaint();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					a1=0;
				}

			}

			if (numberOfHubs == 0) {
				for (i = y_sender; i >= y_switch; i--) {
					draw.repaint();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				i=0;

				for (j = y_switch; j <= y_reciever; j++) {
					draw.repaint();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				j=0;

				for (j1 = y_reciever; j1 >= y_switch; j1--) {
					draw.repaint();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				j1=0;
				for (i1 = y_switch; i1 <= y_sender; i1++) {
					draw.repaint();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				i1=0;
			}
		}

		public void calculations() {
			if (numberOfHubs > 0) {
				x_switch = this.getWidth() / 2;
				y_switch = 140;
				int hubDistance = (this.getWidth()) / (numberOfHubs + 1);
				for (int i = 1; i <= numberOfHubs; i++) {
					if (senderHub.getHubNumber() + 1 == i) {
						x_shub = hubDistance * i;
						y_shub = 280;
						int sum = x_shub + y_shub;
					}
					if (recieverHub.getHubNumber() + 1 == i) {
						x_rhub = hubDistance * i;
						y_rhub = 280;
						int sum = x_rhub + y_rhub;
					}
				}
				int count = 0;
				for (Hub h : hubs) {
					EndDevices[] devices = h.getConnectedDevices();
					int devNum = devices.length;
					for (int j = 1; j <= devNum; j++) {
						if (sender_device.getMac() == devices[j - 1].getMac()) {
							x_sender = ((((hubDistance) / (devNum + 1)) * j) + hubDistance * count);
							y_sender = 480;
							int sum = x_sender + y_sender;
						}
						if (reciever_device.getMac() == devices[j - 1].getMac()) {
							x_reciever = ((((hubDistance) / (devNum + 1)) * j) + hubDistance * count);
							y_reciever = 480;
							// System.out.println(y_reciever+" "+x_reciever);
							int sum = x_reciever + y_reciever;
						}
					}
					count++;

				}

			}
			if (numberOfHubs == 0) {
				// int x_sender=0,y_sender=0,x_reciever=0,y_reciever=0;
				for (int i = 1; i <= numberOfDevices; i++) {

					if (i == senderNumber + 1) {
						x_sender = ((this.getWidth()) / (numberOfDevices + 1)) * i;
						y_sender = 280;
						int sum = x_sender + y_sender;
					}
					if (i == recieverNumber + 1) {
						x_reciever = ((this.getWidth()) / (numberOfDevices + 1)) * i;
						y_reciever = 280;
						int sum = x_reciever + y_reciever;
					}
				}
				x_switch = this.getWidth() / 2;
				y_switch = 140;

			}
		}

		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.BLACK);
			
			int routerDistance = (this.getWidth())/(numberOfRouters);
			for (int r=0;r<numberOfRouters;r++) {
				g.drawImage(routerImage, (routerDistance) * i, 100, 40, 40, this);
				if (i<numberOfRouters-1)
					g.drawLine((routerDistance) * i + 20, 120, routerDistance * (i+1) + 20, 120);
				
			}
			
			g.drawImage(switchImage, this.getWidth() / 2, 100, 50, 50, this);

			if (numberOfHubs > 0) {
				x_switch = this.getWidth() / 2;
				y_switch = 140;
				int hubDistance = (this.getWidth()) / (numberOfHubs + 1);
				for (int i = 1; i <= numberOfHubs; i++) {
					g.drawImage(routerImage, (hubDistance) * i, 300, 50, 50, this);
					g.drawLine(this.getWidth() / 2 + 20, 140, hubDistance * i + 20, 280);
					if (senderHub.getHubNumber() + 1 == i) {
						x_shub = hubDistance * i;
						y_shub = 280;
						int sum = x_shub + y_shub;
					}
					if (recieverHub.getHubNumber() + 1 == i) {
						x_rhub = hubDistance * i;
						y_rhub = 280;
						int sum = x_rhub + y_rhub;
					}
				}
				int count = 0;
				for (Hub h : hubs) {
					EndDevices[] devices = h.getConnectedDevices();
					int devNum = devices.length;
					for (int j = 1; j <= devNum; j++) {
						g.drawImage(endDeviceImage1, (((hubDistance) / (devNum + 1)) * j) + hubDistance * count, 500,
								50, 50, this);
						g.drawString(devices[j - 1].getDeviceName() + "",
								(((hubDistance) / (devNum + 1)) * j) + hubDistance * count, 510);
						g.drawLine(hubDistance * (count + 1) + 20, 340,
								((((hubDistance) / (devNum + 1)) * j) + hubDistance * count) + 30, 480);
						if (sender_device.getMac() == devices[j - 1].getMac()) {
							x_sender = ((((hubDistance) / (devNum + 1)) * j) + hubDistance * count);
							y_sender = 480;
							int sum = x_sender + y_sender;
						}
						if (reciever_device.getMac() == devices[j - 1].getMac()) {
							x_reciever = ((((hubDistance) / (devNum + 1)) * j) + hubDistance * count);
							y_reciever = 480;
							// System.out.println(y_reciever+" "+x_reciever);
							int sum = x_reciever + y_reciever;
						}
					}
					count++;

				}

				if (x_shub == x_rhub) {
					double m = (double) (y_shub - y_sender) / (double) (x_shub - x_sender);
					double c = y_sender - m * x_sender;
					// System.out.println(m+" "+c);
					if (f != 0) {
						g.drawImage(messageImage, (int) ((f - c) / m), f, 40, 30, this);
						g.drawString(messageNumber + "", (int) ((f - c) / m), f);
					}
					m = (double) (y_shub - y_reciever) / (double) (x_shub - x_reciever);
					c = y_shub - m * x_shub;
					// System.out.println(m+" "+c);
					if (z != 0) {
						g.drawImage(messageImage, (int) ((z - c) / m), z, 40, 30, this);
						g.drawString(messageNumber + "", (int) ((z - c) / m), z);
					}
					
					m = (double) (y_shub - y_sender) / (double) (x_shub - x_sender);
					c = y_sender - m * x_sender;
					// System.out.println(m+" "+c);
					if (f1 != 0) {
						g.drawImage(messageImage, (int) ((f1 - c) / m), f1, 40, 30, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((f1 - c) / m), f1);
						
					}
					m = (double) (y_shub - y_reciever) / (double) (x_shub - x_reciever);
					c = y_shub - m * x_shub;
					// System.out.println(m+" "+c);
					if (z1 != 0) {
						g.drawImage(messageImage, (int) ((z1 - c) / m), z1, 40, 30, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((z1 - c) / m), z1);
					}

				} else {
					double m = (double) (y_shub - y_sender) / (double) (x_shub - x_sender);
					double c = y_sender - m * x_sender;
					// System.out.println(m+" "+c);
					if (a != 0) {
						g.drawImage(messageImage, (int) ((a - c) / m), a, 40, 30, this);
						g.drawString(messageNumber + "", (int) ((a - c) / m), a);
					}
					m = (double) (y_shub - y_switch) / (double) (x_shub - x_switch);
					c = y_switch - m * x_switch;
					// System.out.println(m+" "+c);
					if (b != 0) {
						g.drawImage(messageImage, (int) ((b - c) / m), b, 40, 30, this);
						g.drawString(messageNumber + "", (int) ((b - c) / m), b);
					}
					m = ((double) (y_rhub - y_switch)) / (double) (x_rhub - x_switch);
					c = y_rhub - m * x_rhub;
					// System.out.println(m+" "+c);
					if (d != 0) {
						g.drawImage(messageImage, (int) ((d - c) / m), d, 40, 30, this);
						g.drawString(messageNumber + "", (int) ((d - c) / m), d);
					}
					m = (double) (y_rhub - y_reciever) / (double) (x_rhub - x_reciever);
					c = y_rhub - m * x_rhub;
					// System.out.println(m+" "+c);
					if (e != 0) {
						g.drawImage(messageImage, (int) ((e - c) / m), e, 40, 30, this);
						g.drawString(messageNumber + "", (int) ((e - c) / m), e);
					}

					m = (double) (y_shub - y_sender) / (double) (x_shub - x_sender);
					c = y_sender - m * x_sender;
					if (a1 != 0) {
						g.drawImage(messageImage, (int) ((a1 - c) / m), a1, 40, 30, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((a1 - c) / m), a1);
					}
					m = (double) (y_shub - y_switch) / (double) (x_shub - x_switch);
					c = y_switch - m * x_switch;
					// System.out.println(m+" "+c);
					if (b1 != 0) {
						g.drawImage(messageImage, (int) ((b1 - c) / m), b1, 40, 30, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((b1 - c) / m), b1);
					}
					m = ((double) (y_rhub - y_switch)) / (double) (x_rhub - x_switch);
					c = y_rhub - m * x_rhub;
					// System.out.println(m+" "+c);
					if (d1 != 0) {
						g.drawImage(messageImage, (int) ((d1 - c) / m), d1, 40, 30, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((d1 - c) / m), d1);
					}
					m = (double) (y_rhub - y_reciever) / (double) (x_rhub - x_reciever);
					c = y_rhub - m * x_rhub;
					// System.out.println(m+" "+c);
					if (e1 != 0) {
						g.drawImage(messageImage, (int) ((e1 - c) / m), e1, 40, 30, this);
						g.drawString(messageNumber + " " + ACKorNAK, (int) ((e1 - c) / m), e1);
					}
				}

			}
			if (numberOfHubs == 0) {
				// int x_sender=0,y_sender=0,x_reciever=0,y_reciever=0;
				for (int i = 1; i <= numberOfDevices; i++) {
					g.drawImage(endDeviceImage2, ((this.getWidth()) / (numberOfDevices + 1)) * i, 300, 50, 50, this);
					g.drawString(objectOfDevices[i - 1].getDeviceName() + "",
							((this.getWidth()) / (numberOfDevices + 1)) * i, 310);
					g.drawLine(this.getWidth() / 2 + 20, 140, ((this.getWidth()) / (numberOfDevices + 1)) * i + 20,
							280);
					if (i == senderNumber + 1) {
						x_sender = ((this.getWidth()) / (numberOfDevices + 1)) * i;
						y_sender = 280;
						int sum = x_sender + y_sender;
					}
					if (i == recieverNumber + 1) {
						x_reciever = ((this.getWidth()) / (numberOfDevices + 1)) * i;
						y_reciever = 280;
						int sum = x_reciever + y_reciever;
					}
				}
				x_switch = this.getWidth() / 2;
				y_switch = 140;
				double m = (double) (y_switch - y_sender) / (double) (x_switch - x_sender);
				double c = y_sender - m * x_sender;
				// System.out.println(m+" "+c);
				if (i != 0) {
					g.drawImage(messageImage, (int) ((i - c) / m), i, 40, 30, this);
					g.drawString(messageNumber + "", (int) ((i - c) / m), i);
				}
				// System.out.println();
				double m1 = (double) (y_switch - y_reciever) / (double) (x_switch - x_reciever);
				double c1 = y_reciever - m1 * x_reciever;
				if (j != 0) {
					g.drawImage(messageImage, (int) ((j - c1) / m1), j, 40, 30, this);
					g.drawString(messageNumber + "", (int) ((j - c1) / m1), j);
				}
				m = (double) (y_switch - y_sender) / (double) (x_switch - x_sender);
				c = y_sender - m * x_sender;
				// System.out.println(m+" "+c);
				if (i1 != 0) {
					g.drawImage(messageImage, (int) ((i1 - c) / m), i1, 40, 30, this);
					g.drawString(messageNumber + " "+ACKorNAK, (int) ((i1 - c) / m), i1);
				}
				// System.out.println();
				m1 = (double) (y_switch - y_reciever) / (double) (x_switch - x_reciever);
				c1 = y_reciever - m1 * x_reciever;
				if (j1 != 0) {
					g.drawImage(messageImage, (int) ((j1 - c1) / m1), j1, 40, 30, this);
					g.drawString(messageNumber +  " "+ACKorNAK, (int) ((j1 - c1) / m1), j1);
				}

			}

			/*
			 * g.setColor(Color.GREEN); g.fillOval(x, y, 50, 50);
			 */
		}
	}

}
