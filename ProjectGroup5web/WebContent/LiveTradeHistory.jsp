<%@page import="data.access.Dal"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%List<String> trades = Dal.getAllTrades(); %>
<table class="table table-striped">
				<tr>
					<th>Symbol</th>
					<th>Bid Price</th>
					<th>Ask Price</th>
					<th>Bought or Sold</th>
					<th>Time</th>
				</tr>
				
				<tr>
					<%for (String s : trades) {
						String[]trade = s.split(",");
						if (trade[3].equals("true")) {
							trade[3]= "Bought";
						}
						else {
							trade[3]= "Sold";
						}
					%>
				
					<td> <%=trade[0] %> </td>
					<td> <%=trade[1] %> </td>
					<td> <%=trade[2] %> </td>
					<td> <%=trade[3] %> </td>
					<td> <%=trade[4] %> </td>
				</tr>
				<%} %>
</table>
</body>
</html>