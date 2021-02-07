package src.agent;
 
import java.io.IOException;
import java.net.*;

import src.communications.SendUDP;
import src.database.ActiveUsersDB;
import src.user.Login;

public class ListenUsers extends Thread {
	
	final static int port = 20000; 
	private DatagramSocket server;
	private Login login;
	private ActiveUsersDB activeUsers;
	private String ipOrig = null;
	public static boolean stop = false;

	public ListenUsers(Login login, ActiveUsersDB activeUsers)  {
		this.login = login;
		this.activeUsers = activeUsers;
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
			
			while(!stop) {
				
				//On s'occupe maintenant de l'objet paquet
                byte[] buffer = new byte[8192];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
                //Cette méthode permet de récupérer le datagramme envoyé par le client
                //Elle bloque le thread jusqu'à ce que celui-ci ait reçu quelque chose.
                server.receive(packet);
                String ipD = packet.getAddress().toString().substring(1);
                
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
                System.out.println(udpinfo[0] + " -- " + udpinfo[1] + " -- " + ipD);
                
                if ((udpinfo[0]).equals("[1BD]")) { 
                	//send login and ip address
                	SendUDP.send("[UAU]:"+login.toString(), packet.getAddress(), 20000, false);
                	activeUsers.addUser(ipD, udpinfo[1]);
                	

                } else if ((udpinfo[0]).equals("[UAU]")) { 
                	//add new user or update previous username
                	ipOrig = (String) ipD;

                	if (activeUsers.hasKey(ipOrig)) {
                		activeUsers.updateUser(ipD, udpinfo[1]);
                	} else {
                		activeUsers.addUser(ipD, udpinfo[1]);
                	}
                	
                	activeUsers.printUsers();
                	
				  
                }else if ((udpinfo[0]).equals("[RAU]")){ 
					//remove from active users 
                	activeUsers.deleteUser(ipD, udpinfo[1]);
			 
                } else if ((udpinfo[0]).equals("[[OFF]]")){
                	//Change status to Offline
                	activeUsers.changeStatus(udpinfo[1], "Offline");
                } if ((udpinfo[0]).equals("[[DND]]")){
                	//Change status to Offline
                	activeUsers.changeStatus(udpinfo[1], "Do Not Disturb");
                } else {
				  		System.out.println("[ListenUsers] Error header"); 
				  
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
			System.out.println("[ListenUsers] End of Thread");
			
		
			
		} catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		  
	
		     
	public String[] getUDPinfo(String packet) {
		String[] info = packet.split(":");
		String[] ret = {info[0], info[1], info[2]};
		return ret;
	}



}
