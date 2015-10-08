<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" 
import = "data.access.*, java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Team 5 Trading</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href = "bootstrap/css/bootstrap.min.css" rel = "stylesheet">
<link href = "bootstrap/css/styles.css" rel = "stylesheet">
</head>

<body>

<!--********************** TOP NAVBAR **********************-->
<div class = "navbar navbar-default navbar-static-top">
	<div class = "container">
		<img src = "Images/team5logo.jpg" alt="TeamLogo" class=" navbar-brand img-rounded img-responsive pull-left">
		<a href = "#" class = "navbar-brand">Team 5 Trading</a>	
		<button class = "navbar-toggle" data-toggle = "collapse" data-target = ".navHeaderCollapse">
			<span class = "icon-bar"></span>
			<span class = "icon-bar"></span>
			<span class = "icon-bar"></span>
		</button>
	
		<div class = "collapse navbar-collapse navHeaderCollapse">
			<ul class = "nav navbar-nav navbar-right">
				<li class = "active"><a href = "/ProjectGroup5web/Index.jsp">Home</a></li>
				<!--<li><a href = "#">Positions</a></li>-->
		
				<li class = "dropdown">
					<a href = "#" class = "dropdown-toggle" data-toggle = "dropdown">Profile<b class = "caret"></b></a>
					<ul class = "dropdown-menu">
					<!-- <a href = "/BuildingBlock/addTCaseToQueue1.html">Trading History</a> -->
						<li><a href = "/ProjectGroup5web/TradingHistory.jsp">Trading History</a></li>
						<li><a href = "/ProjectGroup5web/Positions.jsp">Positions</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>
<!--********************** END OF TOP NAVBAR **********************-->

<div class="col-md-8 col-md-offset-2 panel panel-success">
<div class="panel-heading">Current Positions</div>
    <div class="panel-body">Please Select a company from the dropdown in order to view your current position with that company</div> 
        <div class = "row">
        	<div class="text center">
        		<label for="company">Company: </label>
        		<select class="form-control" name="company" id="company" onChange="refreshPositions()">
        			<option value="" selected disabled>Please select</option>
            		<%List<String> companyNames = Dal.getAllCompaniesWhereStrategyIsNotZero();
            			for(String company : companyNames){      	
            				out.println("<option value='" + company + "'>" + company + "</option>");
            			}
            		out.println("</select>");
            		out.println("</div>");
            		out.println("</div>");
					out.println("<div class = 'row' id='positionsTableHolder' style='display:none'>");
					out.println("<table class='table table-striped'>");
					out.println("<tr><th>Company Symbol</th><th>Bid/Ask Price</th><th>Bought/Sold</th><th>Time</th></tr>");
					if(request.getParameter("company") != null){ 
						List<String> histories = Dal.getTradeHistoryByCompany(request.getParameter("company"));
						for(String h : histories){
							String[]row = h.split(", ");
						//CompanySymbol
						
						//BidPrice
						//AskPrice
						//Buy
						//TradeTime	
							out.println("<tr><td>" + row[0] + "</td>");
							if(row[3].equals("TRUE")){
								out.println("<td>" + row[2] + "</td>");
							}
							else{
								out.println("<td>" + row[1] + "</td>");
							}
							out.println("<td>" + row[4] + "</td><td>" + row[5] + "</td></tr>");
						}
					} %>
			</table>	
		</div>
</div>