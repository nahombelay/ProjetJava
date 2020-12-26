package com.servlet;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Set;

public class HttpServer extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			doPost(request,response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
			this.getServletContext().getRequestDispatcher("/WEB-INF/NewExternalUser.jsp").forward(request, response);
			//TODO: lancer le bon jsp qui renvoie à tout utilisateurs externes et internes
			//TODO: stocker dans BDD
			//TODO: envoyer toute sa BDD à cet utilisateur
		} else if (URLtype.equals("UpdateInternalUser")) {
			this.getServletContext().getRequestDispatcher("/WEB-INF/UpdateInternalUser.jsp").forward(request, response);
			//TODO: lancer le bon jsp qui renvoie à tout utilisateurs externs
			//TODO: update dans BDD
		} else if (URLtype.equals("UpdateExternalUser")) {
			this.getServletContext().getRequestDispatcher("/WEB-INF/UpdateExternalUser.jsp").forward(request, response);
			//TODO: lancer le bon jsp qui renvoie à tout utilisateurs externs et internes
			//TODO: update dans BDD
		} else {
			System.out.println("Erreur");
		}
		
		
	}

}
