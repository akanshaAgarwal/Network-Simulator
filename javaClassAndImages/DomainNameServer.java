package mainCode;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class DomainNameServer {
	static void storeDNSforEmail(String receiverIP) {
		String emails[]= {"akansha08agarwal@gmail.com","akansha26agarwal@gmail.com","bhartitak11@gmail.com",
				"zakiakmal13@gmail.com"};
		String mailNames[]=new String[emails.length];
		for(int i=0;i<emails.length;i++) {
			mailNames[i]=emails[i].substring(0, emails[i].indexOf('@'));
		}
		String IPparts[]=new String[4];
		IPparts[0]=receiverIP.substring(0,receiverIP.indexOf(".") );
		IPparts[1]="0";
		IPparts[2]=receiverIP.substring(receiverIP.lastIndexOf(".")+1 , receiverIP.length());
		HashMap<String, String> dns = new HashMap<>();
		dns.put("com", IPparts[2]);
		dns.put("gmail",IPparts[1]);
		dns.put("@", IPparts[1]);
		for(int i=0;i<emails.length;i++) {
			dns.put(mailNames[i], IPparts[0]);
		}
		
		System.out.println("\nThe DNS mappings for g-mail are as follows:");
		Set<Entry<String, String>> dnsForEmail = dns.entrySet();
		for (Entry<String, String> ent : dnsForEmail) {
			System.out.println(ent.getKey()+" -> "+ent.getValue());
		}
		System.out.println();
	}
	static void storeDNSforSearchEngines(String receiverIP) {
		String websites[]= {"www.google.com","www.duckduckgo.com","www.bing.com"};
		String websiteNames[]=new String[websites.length];
		for(int i=0;i<websites.length;i++) {
			websiteNames[i]=websites[i].substring(websites[i].indexOf('.')+1, websites[i].lastIndexOf('.'));
		}
		String IPparts[]=new String[4];
		
		IPparts[0]=receiverIP.substring(0,receiverIP.indexOf(".") );
		IPparts[1]="0";
		IPparts[2]=receiverIP.substring(receiverIP.lastIndexOf(".")+1 , receiverIP.length());

		HashMap<String, String> dns = new HashMap<>();
		dns.put(".", IPparts[1]);
		dns.put("com",IPparts[2]);
		dns.put("www", IPparts[1]);
		for(int i=0;i<websites.length;i++) {
			dns.put(websiteNames[i], IPparts[0]);
		}
		
		System.out.println("\nThe DNS mappings for search engine are as follows:");
		Set<Entry<String, String>> dnsForEmail = dns.entrySet();
		for (Entry<String, String> ent : dnsForEmail) {
			System.out.println(ent.getKey()+" -> "+ent.getValue());
		}
		System.out.println();
	}
}
