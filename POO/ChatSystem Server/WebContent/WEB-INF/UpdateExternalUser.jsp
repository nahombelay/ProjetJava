<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.net.URL, com.database.UsersDatabaseServer"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
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
		
		if (status.equals("offline")){
			new UsersDatabaseServer().deleteUser(ip, username, URL, "ExternalUsers");
			out.println("User has been deleted");
		} else {
			new UsersDatabaseServer().updateUser(ip, username, status, URL, "ExternalUsers");
			out.println("User has been updated");
		}
		%>
		</p>
	</body>
</html>