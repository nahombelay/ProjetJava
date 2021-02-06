<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.database.UsersDatabaseServer, java.util.ArrayList, java.io.PrintWriter"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>ChatSystem Server</title>
	</head>
	
	<body>
		<%!
	
			public String getTable(ArrayList<ArrayList<String>> array){
				String table = "<table border=1>\n" + 
						"			<tr>\n" + 
						"				<th>ip</th>\n" + 
						"				<th>username</th>\n" + 
						"				<th>status</th>\n" + 
						"			</tr>";
				if (array != null) {
					ArrayList<String> ipArray = array.get(0);
					ArrayList<String> usernameArray = array.get(1);
					ArrayList<String> statusArray = array.get(2);
					int size = ipArray.size();
					for (int i = 0 ; i < size ; i++) {
						table += ("<tr>");
						table += ("<td> " + ipArray.get(i) + "</td>");
						table += ("<td> " + usernameArray.get(i) + "</td>");
						table += ("<td> " + statusArray.get(i) + "</td>");
						table += ("</tr>");
					}
				}
						
				return table;
			}
		
		%>
		<% 
			UsersDatabaseServer database =  new UsersDatabaseServer();
			
			ArrayList<ArrayList<String>> internalUsers = database.createTable("InternalUsers");
			ArrayList<ArrayList<String>> externalUsers = database.createTable("ExternalUsers");
			
			String internalTable = getTable(internalUsers);
			String externalTable = getTable(externalUsers);
		
	
			out.println("<h1> Connected Internal Users </h1>");
			out.println(internalTable);
		%>
		
		<%
		
			
			out.println(externalTable);
			out.println("<h1> Connected External Users </h1>");

		%>
	</body>
</html>