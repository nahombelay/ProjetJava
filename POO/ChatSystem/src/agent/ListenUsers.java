package agent;

import java.io.IOException;
import java.net.*;

public class ListenUsers extends Thread {
	
	final static int port = 20000; 
	private DatagramSocket server;

	public ListenUsers()  {
		try {
			this.server = new DatagramSocket(port);
		} catch (SocketException e) {
            e.printStackTrace();
        }
	}
	
	public void run() {
		System.out.println("[Listening...]");
		try {
			
			while(true) {
				
				//On s'occupe maintenant de l'objet paquet
                byte[] buffer = new byte[8192];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
                //Cette méthode permet de récupérer le datagramme envoyé par le client
                //Elle bloque le thread jusqu'à ce que celui-ci ait reçu quelque chose.
                server.receive(packet);
                
              //nous récupérons le contenu de celui-ci et nous l'affichons
                String str = new String(packet.getData());
                System.out.print("Reçu de la part de " + packet.getAddress() 
                                  + " sur le port " + packet.getPort() + " : ");
                System.out.println(str);
                
                /**
                 * trois possibilités : 
                 * 		1er broadcast 
                 * 		update activeUsers
                 * 		remove from activeUsers
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
