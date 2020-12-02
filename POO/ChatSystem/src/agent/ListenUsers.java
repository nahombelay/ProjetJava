package agent;
 
import java.io.IOException;
import java.net.*;

import communications.SendUDP;
import database.ActiveUsers;
import user.Login;

public class ListenUsers extends Thread {
	
	final static int port = 20000; 
	private DatagramSocket server;
	private Login login;
	private ActiveUsers usersDatabase;

	public ListenUsers(Login login, ActiveUsers usersDatabase)  {
		this.login = login;
		this.usersDatabase = usersDatabase;
		try {
			this.server = new DatagramSocket(port);
		} catch (SocketException e) {
            e.printStackTrace();
        }
	}
	
	public void run() {
		String[] udpinfo;
		System.out.println("[Listening...]");
		try {
			
			while(true) {
				
				//On s'occupe maintenant de l'objet paquet
                byte[] buffer = new byte[8192];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
                //Cette méthode permet de récupérer le datagramme envoyé par le client
                //Elle bloque le thread jusqu'à ce que celui-ci ait reçu quelque chose.
                server.receive(packet);
                
                /**
                 * UDP packet format
                 * [HDR]:login:ip
                 */
                
              //nous récupérons le contenu de celui-ci et nous l'affichons
                String str = new String(packet.getData());
                System.out.print("Reçu de la part de " + packet.getAddress() 
                                  + " sur le port " + packet.getPort() + " : ");
                System.out.println(str);
                
                udpinfo = getUDPinfo(str);
                System.out.println(udpinfo[0] + " -- " +udpinfo[1] + " -- " + udpinfo[2]);
                
                if ((udpinfo[0]).equals("[1BD]")) { 
                	//send login and ip address
                	
                	SendUDP.send("[UAU]:"+login.toString(), InetAddress.getByName(udpinfo[2]), 20000, false);
                	

                } else if ((udpinfo[0]).equals("[UAU]")) { 
                	//add new user or update previous username
                	System.out.println("[UAU]");
                	usersDatabase.addUser(udpinfo[2], udpinfo[1]);
                	System.out.println(usersDatabase.toString());
                	
				  
                }else if ((udpinfo[0]).equals("[RAU]")){ 
					//remove from active users 
                	usersDatabase.removeUser(udpinfo[2], udpinfo[1]);
			 
                } else {
				  		System.out.println("Error header"); 
				  
                }
				 
                
                /**
                 * trois possibilités : 
                 * 		1er broadcast [1BD]
                 * 		update activeUsers [UAU]
                 * 		remove from activeUsers [RAU]
                 */
                
              //On réinitialise la taille du datagramme, pour les futures réceptions
           	  packet.setLength(buffer.length);
                
			}
			
		
			
		} catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}
		  
		                
	public String[] getUDPinfo(String packet) {
		String header = packet.substring(0, 5);
		String username = ""; 
		String ipAddr = "";
		int i;
		for (i = 6; i < packet.length(); i++) {
			if (packet.charAt(i) == ':') {
				break;
			} else {
				username += packet.charAt(i);
			}
		}
		
		ipAddr = packet.substring(i+1);
		
		String[] ret = {header, username, ipAddr};

		return ret;
	}
		                  
	/*
	 * //On réinitialise la taille du datagramme, pour les futures réceptions
	 * packet.setLength(buffer.length);
	 * 
	 * //et nous allons répondre à notre client, donc même principe byte[] buffer2 =
	 * new String("Réponse du serveur à " + str + "! ").getBytes(); DatagramPacket
	 * packet2 = new DatagramPacket( buffer2, //Les données buffer2.length, //La
	 * taille des données packet.getAddress(), //L'adresse de l'émetteur
	 * packet.getPort() //Le port de l'émetteur );
	 * 
	 * //Et on envoie vers l'émetteur du datagramme reçu précédemment
	 * server.send(packet2); packet2.setLength(buffer2.length);
	 */
		     
		      


}
