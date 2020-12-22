<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.net.URL, com.database.UsersDatabaseServer, java.sql.*"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>New Internal User</title>
	</head>
	
	<body>
		<%! String ip = ""; 
			String username = "";
			String status = "";
			String URL = "";
		%>
		<p>Inserstion des elements dans la base de données</p>
		
		<p> 
			<%
			ip = (String) request.getParameter("ip");
			username = (String) request.getParameter("username");
			status = (String) request.getParameter("status");
			URL = new URL(request.getRequestURL().toString()).getHost(); 
			
			out.println("ip = " + ip);
			out.println("username = " + username);
			out.println("status = " + status);
			out.println("URL = " + URL);
			
			
			UsersDatabaseServer ud = new UsersDatabaseServer();
			//ud.addUser(ip, username, status, "InternalUsers", URL.toString());
			%>
		</p>
		
	</body>
</html>