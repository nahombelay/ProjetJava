<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.net.URL, com.database.UsersDatabaseServer, com.database.UsersDatabaseServer, java.sql.Connection, java.sql.ResultSet" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>New User</title>
	</head>
	
	<body>
		<%! String ip; 
			String username;
			String status;
			String URL;
			Connection c;
			ResultSet rs;
		%>
		
		<p>Inserstion des elements dans la base de données</p>
		
		<p> 
		<% 
		
		ip = (String) request.getParameter("ip");
		username = (String) request.getParameter("username");
		status = (String) request.getParameter("status");
		URL = new URL(request.getRequestURL().toString()).getHost(); 
		
		UsersDatabaseServer database =  new UsersDatabaseServer();
		database.addUser(ip, username, status, URL, "ExternalUsers");
		
		out.println("OK");
		%>
		</p>
		
		<p> Renvoie de la liste des utilisateurs deja present</p>
		
		<table >
			<tr>
				<th>ip</th>
				<th>username</th>
				<th>status</th>
			</tr>
			<%
			c = database.getConnection("InternalUsers");
			rs = c.createStatement().executeQuery("SELECT * FROM ExternalUsers");
			
			while (rs.next()) {
				String ip = rs.getString(1);
				String username = rs.getString(2);
				String status = rs.getString(3);
				
				out.println("<tr>");
				out.println("<td> " + ip + "</td>");
				out.println("<td> " + username + "</td>");
				out.println("<td> " + status + "</td>");
				out.println("</tr>");
			}
			rs.close();
			
			rs = c.createStatement().executeQuery("SELECT * FROM InternalUsers");
			
			while (rs.next()) {
				String ip = rs.getString(1);
				String username = rs.getString(2);
				String status = rs.getString(3);
				
				out.println("<tr>");
				out.println("<td> " + ip + "</td>");
				out.println("<td> " + username + "</td>");
				out.println("<td> " + status + "</td>");
				out.println("</tr>");
			}
			
			c.close();
			rs.close();
			%>
		</table>
		
	</body>
</html>