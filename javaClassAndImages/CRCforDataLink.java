package mainCode;

import java.math.BigInteger;
import java.util.Scanner;

public class CRCforDataLink {
	String binaryData;
	String recvdData;
	String divisor="100000111";
	private String rem;
	private String sxtCopy;
	
	
	public String senderCode(String sxtBit) {
		// TODO Auto-generated method stub
		
		sxtCopy = sxtBit;
		sxtBit+="00000000";
		rem = binaryXorDivision(sxtBit, divisor);
		sxtCopy+=rem;
		System.out.println("The remainder is "+rem);
		System.out.println("The sender codeword is "+sxtCopy);
		//System.out.println("Enter the probability of first flip");
		return sxtCopy;
		
		
		
	}
	
	public String recieverCode(String sxtBit, double probability) {
		double flipProb=probability;
		double randProb=0;
		//sxtCopy=sxtBit;
		int arr[]=new int[24];
		for(int i=0;i<24;i++) {
			arr[i]=(int)sxtBit.charAt(i)-48;
		}
		
		int bitToFlip = (int)Math.ceil(Math.random()*23);
		randProb=Math.random();
		if(randProb<0.5) {
		if(arr[bitToFlip]==1) {
			arr[bitToFlip]=0;
		}else {
			arr[bitToFlip]=1;
		}
		}
		
		sxtBit="";
		System.out.print("The recieved codeword is ");
		for(int i=0;i<=23;i++) {
			System.out.print(arr[i]);
			sxtBit+=arr[i];
		}System.out.println();
		recvdData=sxtBit;
		return sxtBit;
	}

    static boolean iscorrect(String d)
    {
    	String rem=binaryXorDivision(d,"100000111" );
		if(rem.equals("00000000")==false) {
			System.out.println("Discard");
			return true;
		}else {
			System.out.println("No error");
			System.out.println("The recieced data is "+d);
			return false;
		}
		

    }
	public static String binaryXorDivision(String dividend, String divisor) {
		String temp=dividend.substring(0,divisor.length()-1);
		for(int i=divisor.length()-1;i<dividend.length();i++) {
			temp=temp+dividend.charAt(i);
			if(temp.charAt(0)=='1') {
				temp=xorOp(temp,divisor);
			}else {
				temp=xorOp(temp, "000000000");
			}
			temp=temp.substring(1,temp.length());
			
		}
		
		return temp;
	}
	public static String xorOp(String str1,String str2) {
		String res="";
		for(int i=0;i<str1.length();i++) {
			if (str1.charAt(i)==str2.charAt(i)) {
				res+="0";
			}
			else {
				res+="1";
			}
		}
		return res;
	}

}
