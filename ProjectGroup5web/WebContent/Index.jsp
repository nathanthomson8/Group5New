<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="data.dataObjects.StockObject , 
javax.naming.InitialContext, java.util.List, yahooFeed.Feed, data.access.*"%>
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
						<li><a href = "/ProjectGroup5web/TradingHistory.jsp">Trading History</a></li>
						<li><a href = "/ProjectGroup5web/Positions.jsp">Positions</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>
<!--********************** END OF TOP NAVBAR **********************-->
<!--********************** Jumbotron **********************-->
<div class = "container">
	<div class = "jumbotron">
		<h2 class ="text-center"> Welcome to the Team 5 Trading Hub</h2>
		<p>Please select a relevant Market from the Market dropdown and then a company to view live market data for that Company. </p>
		<div class = "text-center">
			<form class="form-inline" id="liveData" action="CompanySymbolServlet" method="POST">
	  			<div class="form-group">
	    			<label for="company">Company: </label>
	   			 	<select class="form-control" name="company" id="company" onChange="callStrategy()">
						<option value="" selected disabled>Please select</option>
						<option value ="AAPL">Apple(AAPL)</option>
						<option value ="MSFT">Microsoft(MSFT)</option>
						<option value ="CSCO">Cisco(CSCO)</option>
						<option value ="IBM">IBM(IBM)</option>
						<option value ="LSE.L">London Stock Exchange</option>
					</select>
	  			</div>
			</form>
		</div>
	</div>	
</div>
<!--******************** End of Jumbotron *****************-->
<!--********************** Columns Fluid Grid - Article Entries **********************-->
<div class = "container">
<div class = "row">
	<div class="col-md-6 panel panel-success">
  		<div class="panel-heading">Live Market Data</div>
		  <div class="panel-body" id="tableHolder">
			
		</div>
	</div>

	<div class="col-md-6 panel panel-success">
		<div class="panel-heading">Strategy</div>
		  <div class="panel-body text-center" id="strategyHolder">
		  
		<% if(request.getParameter("company") != null){ 
		int strategy = Dal.getStrategyByCompany(request.getParameter("company"));
		if(strategy == 0){ 
		%>
	<div id="strategyDiv">
	<div class="text-center">
			<form class="form-inline" id="strategyform1" action="SetStrategyServlet">
				<input type="hidden" name="company" value="<%=request.getParameter("company")%>">
			  	<div class="form-group">
			    	<label for="strategy">Strategy: </label>
			   		<select class="form-control" name="strategy">
						<option value="" selected disabled>Please select</option>
						<option value = "TwoMovingAvg">Two Moving Averages</option>
					</select>
				</div>
				<button  type="submit" id="submitButton" class="btn btn-success">Submit</button>
			</form>
	</div>
			<%
			}
			else if(strategy == 1)
			{	
			%>
			<div class="text-center">
				<form id="strategy" action="UpdateStrategyServlet">
				<h3>Current Strategy in place: </h3>
				<h3><strong>Two Moving Averages</strong></h3>
				<input type="hidden" name="company" value="<%=request.getParameter("company")%>">
				<input type="hidden" name="stop" value="stop">
			    <label for="submitButton1">
			    	 Do you want to remove this Strategy?
			   	</label>
			  	
			  	<button  type="submit" id="submitButton1" class="btn btn-success">Yes</button>
			  	</form>
		  	</div>
			<%
			}
			else if(strategy == 2){
				
			}
			else{ // strategy = 3
			
			}
	}
			else{
				// Parameter Not Selected
		%>
		
		<div class = "text-center">
		<p>Please Select a Company to View or Select a Current Strategy</p>
		</div>
		</div>
	<%
		}
	%>
		  
		  
		  </div>
	</div>
</div>
</div>

<!--********************** Fixed Bottom NAVBAR **********************-->
<!--  <div class = "container">
	<div class = "navbar navbar-default navbar-fixed-bottom">
		<img src = "Resources/images/citi_200_year.gif" alt="Citi200Years" class=" navbar-brand img-rounded img-responsive pull-right">
	</div>
</div>-->
<script src= "http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src= "bootstrap/js/bootstrap.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
      refreshTable();
    });

    function refreshTable(){
        $('#tableHolder').load('LiveTableData.jsp', function(){
           setTimeout(refreshTable, 1000);
        });
    }
</script>
<script type="text/javascript">
  	function callStrategy(){
        $('#strategyHolder').load('Strategy.jsp?company='+document.getElementById("company").value, function(){
        });
    }
</script>
<script>
$('#submitButton').click( function() {
    $.ajax({
        url: 'SetStrategyServlet',
        type: 'post',
        dataType: 'json',
        data: $('#strategy').serialize(),
        success: function(data) {
        	 $("#strategyDiv").load(window.location + " #strategy'");
                 }
    }) 
   // document.getElementById("strategyform1").submit();
    });
</script>
<script>
$('#submitButton1').click( function() {
    $.ajax({
        url: 'UpdateStrategyServlet',
        type: 'post',
        dataType: 'json',
        data: $('#strategy').serialize(),
        success: function(data) {
        	 $("#strategyDiv").load(window.location + " #strategy'");
                 }
    }) 
   // document.getElementById("strategyform1").submit();
    });
</script>
</body>
</html>

</body>
</html>