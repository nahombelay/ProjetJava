package ChatSystem.scrapper;

import ChatSystem.database.ActiveUsersDB;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLscrapper {

	private static Document document;
	
	private ActiveUsersDB usersDatabase;
	
	public HTMLscrapper(String table){
		this.usersDatabase = new ActiveUsersDB();
		
		//File htmlFile = new File("/Users/nahombelay/Desktop/test.html");
		
		document = Jsoup.parse(table);
	}
	
	 
	
	public static Document getDocument() {
		return document;
	}

	public Elements getTable() {
		return getDocument().select("table.Users.table tr");
	}
	
	public String getMessage() {
		return getDocument().select("p.message").text();
	}
	
	public void addRowsToDatabase(Elements table) {
		String ip;
		String username;
		String status;
		
		for (Element row : document.select("table.Users.table tr")) {
			if (row.select("th:nth-of-type(1)").text().equals("ip")) {
				continue;
			} else {
				ip = row.select("td:nth-of-type(1)").text();
				username = row.select("td:nth-of-type(2)").text();
				status = row.select("td:nth-of-type(3)").text();
				//TODO: replace with insert to database
				System.out.println(ip + "-" + username + "-" + status);
				usersDatabase.addUser(ip, username);
				if (!status.equals("Online")) {
					try {
						usersDatabase.changeStatus(username, status);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main (String[] argv) {
		
		HTMLscrapper hc = new HTMLscrapper("<table class=\"Users Table\"><tr><th>ip</th><th>username</th><th>status</th></tr><tr><td> 1.1.0.1</td><td> user1</td><td> Online</td></tr><tr><td> 11.1.2.1</td><td> lolo</td><td> Online</td></tr><tr><td> 1.0.2.1</td><td> user2</td><td> Online</td></tr>");
		Elements e = hc.getTable();
		hc.addRowsToDatabase(e);
		
		System.out.println(hc.getMessage());
	}
}
