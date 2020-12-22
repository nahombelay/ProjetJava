package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServer extends HttpServlet {
	
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		String URLtype = request.getParameter("type");
		
		//suppose qu'on va traiter la partie remove user dans update user en mettant status offline
		if (URLtype.equals("Forward")) {
			//TODO: lancer le bon jsp
		} else if (URLtype.equals("NewInternalUser")) {
			this.getServletContext().getRequestDispatcher("/WEB-INF/NewInternalUser.jsp").forward(request, response);
			//TODO: lancer le bon jsp qui renvoie à tout utilisateurs externs
			//TODO: stocker dans BDD
		} else if (URLtype.equals("NewExternalUser")) {
			//TODO: lancer le bon jsp qui renvoie à tout utilisateurs externes et internes
			//TODO: stocker dans BDD
			//TODO: envoyer toute sa BDD à cet utilisateur
		} else if (URLtype.equals("NewInternalUser")) {
			//TODO: lancer le bon jsp qui renvoie à tout utilisateurs externs
			//TODO: stocker dans BDD
		} else if (URLtype.equals("UpdateInternalUser")) {
			//TODO: lancer le bon jsp qui renvoie à tout utilisateurs externs
			//TODO: update dans BDD
		} else if (URLtype.equals("UpdateExeternalUser")) {
			//TODO: lancer le bon jsp qui renvoie à tout utilisateurs externs et internes
			//TODO: update dans BDD
		} else {
			System.out.println("Erreur");
		}
		
		
	}

}
