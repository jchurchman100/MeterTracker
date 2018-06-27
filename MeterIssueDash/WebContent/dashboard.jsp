<%@ page import = "java.util.*, metertrack.*" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<html lang="en">
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Metering Issues | Main</title>
    <link rel = "stylesheet" href = "css/bootstrap.css" type = "text/css" />
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,400i,700" rel="stylesheet">
    <link rel = "stylesheet" href = "css/styles.css" type = "text/css" />
    <script src="https://code.jquery.com/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="js/tabs.js"></script>
</head>

<body>
	<nav class="navbar navbar-inverse">
	  <div class="container-fluid">
	    <div class="navbar-header">
	      <a class="navbar-brand" href="MeterControllerServlet">Meter Tracking</a>
	    </div>
	    <ul class="nav navbar-nav">
	      <li class="active"><a href="MeterControllerServlet">Home</a></li>
	    </ul>
	    <form class="navbar-form navbar-right" action="/search.java">
	      <div class="form-group">
	        <input type="text" class="form-control" placeholder="Search">
	      </div>
	      <button type="submit" class="btn btn-default">Submit</button>
	    </form>
	  </div>
	</nav>

<div class = "container">
	<div id = "etitle" class = "jumbotron">
		<h1>Metering Issue Tracking</h1>
	</div>
	

	<div class = "etabs">
		<div class = "row">
			<div class ="col-md-1"></div>
			<button id = "nvmtag" class="etablinks btn col-md-2 col-12" onclick="openErrors(event, 'nvm')">
			NVM Errors</button>
	 		<button id = "hsatag" class="etablinks btn col-md-2 col-12 " onclick="openErrors(event, 'hotsocket')">
	 		Hot Socket Alerts</button>
	  		<button id = "wiringtag" class="etablinks btn col-md-2 col-12" onclick="openErrors(event, 'wiring')">
	  		Wiring Errors</button>
	  		<button id = "phasetag"class="etablinks btn col-md-2 col-12" onclick="openErrors(event, 'phase')">
	  		Phase Out Errors</button>
	  		<button id = "elctag" class="etablinks btn col-md-2 col-12" onclick="openErrors(event, 'elc')">
	  		ELC Alarms</button>
	  		<div class ="col-md-1"></div>
  		</div>
	</div>

	<div id="nvm" class="etabcontent">
		<h2>Non-Volatile Memory Errors</h2>
		<div class = "row">
			<div class = "col-md-5">	
				<table class = "table-totals table table-hover">
					<tr>
				  		<th><h3>Total NVM Errors</h3></th>
				  		<th></th>
				  	</tr>
				  	<c:forEach var = "flag_count" items = "${NVM_TOTALS}">
						<c:url var = "flag_link" value = "MeterControllerServlet">
							<c:param name = "command" value = "LIST"/>
							<c:param name = "flag_id" value = "${flag_count.key}"/>
						</c:url>
						<c:set var = "len" value = "${fn:length(flag_count.key)}"/>
						<tr>
			  				<td>NVM ${fn:substring(flag_count.key,4,len)}</td>
			  				<td><a href = "${flag_link}">${flag_count.value}</a></td>
			  			</tr>
					</c:forEach>
				</table>
			</div>
			<div class = "col-md-7"></div>
		</div>
	</div>

	<div id="hotsocket" class="etabcontent">
		<h2>HOT SOCKET ALERTS</h2>
		<div class = "row">
			<div class = "col-md-5">
				<table class = "table-totals table table-hover">
			  		<tr>
			  			<th><h3>Total Hot Socket Alerts</h3></th>
			  			<th></th>
			  		</tr>
			  		<c:forEach var = "flag_count" items = "${HSA_TOTALS}" varStatus ="loop">
						<c:url var = "flag_link" value = "MeterControllerServlet">
							<c:param name = "command" value = "LIST"/>
							<c:param name = "flag_id" value = "${flag_count.key}"/>
						</c:url>
				
						<tr>
				  			<td> ${fn:substring(flag_count.key,4,5)}<c:if test = "${loop.index == 2}">0</c:if>
				  			+ voltage outages in last 5 days </td>
				  			<td><a href = "${flag_link}">${flag_count.value}</a></td>
				  		</tr>
					</c:forEach>
			  	</table>
			  </div>
			<div class = "col-md-7"></div>
		</div>
	</div>

	<div id="wiring" class="etabcontent">
		<h2>WIRING ALERTS</h2>
		<div class = "row">
			<div class = "col-md-5">
				<table class = "table-totals table table-hover">
			  		<tr>
			  			<th><h3>Total Wiring Errors</h3></th>
			  			<th></th>
			  		</tr>
			  		<c:forEach var = "flag_count" items = "${WIRING_TOTALS}" varStatus = "loop">
						<c:url var = "flag_link" value = "MeterControllerServlet">
							<c:param name = "command" value = "LIST"/>
							<c:param name = "flag_id" value = "${flag_count.key}"/>
						</c:url>
						<c:set var = "len" value = "${fn:length(flag_count.key)}"/>
					
						<c:if test="${loop.index == 3 or loop.index == 6 or loop.index == 8}">
				        	<tr>
				        		<td><td>
				        	</tr>
				    	</c:if>
				    
					    <c:choose>
						    <c:when test="${loop.index < 3}">
								<tr>
								 	<td>9S Meters &lt; ${fn:substring(flag_count.key,6,len)}</td>
								 	<td><a href = "${flag_link}">${flag_count.value}</a></td>
								</tr>	
							</c:when>
						
							<c:when test="${loop.index >= 3 and loop.index < 6}">
								<tr>
							  		<td>16S Meters &lt; ${fn:substring(flag_count.key,7,len)}</td>
							  		<td><a href = "${flag_link}">${flag_count.value}</a></td>
							  	</tr>
							</c:when>
						
							<c:when test="${loop.index >= 6 and loop.index < 8}">
								<tr>
							  		<td>2S Meters ${fn:substring(flag_count.key,6,len)}</td>
							 		<td><a href = "${flag_link}">${flag_count.value}</a></td>
								</tr>
							</c:when>
							
							<c:when test="${loop.index >= 8}">
								<tr>
							  		<td>12S Meters ${fn:substring(flag_count.key,7,len)}</td>
							 		<td><a href = "${flag_link}">${flag_count.value}</a></td>
								</tr>
							</c:when>
						</c:choose>
					</c:forEach>	  
				</table>
			</div>
			<div class = "col-md-7"></div>
		</div>
	</div>

	<div id="phase" class="etabcontent">
		<h2>PHASE OUT ERRORS</h2>
		<div class = "row">
			<div class = "col-md-5">
				<table class = "table-totals table table-hover">
			  		<tr>
			  			<th><h3>Total Phase Out Errors</h3></th>
			  			<th></th>
			  		</tr>
				  	<c:forEach var = "flag_count" items = "${PHASE_TOTALS}" varStatus = "loop">
						<c:url var = "flag_link" value = "MeterControllerServlet">
							<c:param name = "command" value = "LIST"/>
							<c:param name = "flag_id" value = "${flag_count.key}"/>
						</c:url>
					
						<c:if test="${not loop.first and loop.index % 4 == 0}">
					        <tr>
					        	<td><td>
					        </tr>
					    </c:if>
						
						<tr>
					  		<td>Phase ${fn:substring(flag_count.key,6,len)}<c:if test = "${loop.first}">L</c:if> Outages</td>
					  		<td><a href = "${flag_link}">${flag_count.value}</a></td>
						</tr>
					</c:forEach>
				  </table>
			  </div>
			  <div class = "col-md-7"></div>
		</div>
	</div>

	<div id="elc" class="etabcontent">
		<h2>EXCESSIVE LEADING CURRENT ALARMS</h2>
		<div class = "row">
			<div class = "col-md-5">
				<table class = "table-totals table table-hover">
				  	<tr>
				  		<th><h3>Total ELC Alarms</h3></th>
				  		<th></th>
				  	</tr>
				  	<c:url var = "flag_link" value = "MeterControllerServlet">
							<c:param name = "command" value = "LIST"/>
							<c:param name = "flag_id" value = "${ELC_TOTALS.key}"/>
					</c:url>
					<tr>
					  	<td>Total ELC Alarms</td>
						<td><a href = "${flag_link}">${ELC_TOTALS.value}</a></td>
					</tr>
				 </table>
			</div>
			<div class = "col-md-7"></div>
		</div>
	</div>
	
	<div class = "etabcontent-meters" id = meters-table>
		<c:if test = "${empty FLAGGED_METERS}">
			<h2 id = "NR">${NO_RESULTS}</h2>
		</c:if>
		<c:if test = "${not empty FLAGGED_METERS}">
			<h2>${FLAGGED_METERS[0].info}</h2>
			<table class = "table table-hover table-striped meter-list">
				<tr>
					<th>Posting Date</th>
					<th>Meter ID</th>
					<th>Form ID</th>
					<th>Program ID</th>
					<th>Voltage A</th>
					<th>Voltage B</th>
					<th>Voltage C</th>
					<th>Current A</th>
					<th>Current B</th>
					<th>Current C</th>
				</tr>
		</c:if>
			<c:forEach var = "temp_meter" items = "${FLAGGED_METERS}">
				<tr>
					<td>${temp_meter.date}</td>
					<td>${temp_meter.meterId}</td>
					<td>${temp_meter.form}</td>
					<td>${temp_meter.program}</td>
					<td>${temp_meter.volA}</td>
					<td>${temp_meter.volB}</td>
					<td>${temp_meter.volC}</td>
					<td>${temp_meter.curA}</td>
					<td>${temp_meter.curB}</td>
					<td>${temp_meter.curC}</td>
				</tr>
			</c:forEach>
		</table>
	</div>

</div>
</body>
</html>


