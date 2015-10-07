<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
import="data.access.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<% if(request.getParameter("company") != null){ 
		// do pull of strategy using company name
		int strategy = Dal.getStrategyByCompany(request.getParameter("company"));
		if(strategy == 0){ 
	%>
	<div id="strategyDiv">
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
		<%
		}
		else if(strategy == 1)
		{	
		%>
		<form id="strategy">
		<h1>Current Strategy in place: <strong>Two Moving Averages</strong></h1>
		<div class="checkbox">
	    	<label>
	      	<input type="checkbox" name="removeStrategy"> Do you want to remove this Strategy?
	    	</label>
	  	</div>
	  	<button  type="submit" id="submitButton1" class="btn btn-success">Submit</button>
	  	</form>
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
	</div>
	<div class = "text-center">
	<p>Please Select a Company to View or Select a Current Strategy</p>
	</div>
	<%
		}
	%>
</body>
<script src= "http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src= "bootstrap/js/bootstrap.js"></script>
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
</html>