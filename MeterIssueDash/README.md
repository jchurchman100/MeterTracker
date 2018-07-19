###MeterIssueDash(CMO Dashboard)###

#Summary#
MeterIssueDash is a Java Web Application running on a Servlet(MeterControllerServlet.java) and a JSP, or Java Servers Page(dashboard.jsp). The main objective of the application is to display tables containing different meter alarms, flags, and error names along with the number of occurrances within the Command Center AMI database. The number of occurances are listed in the tables as hyperlinks and when clicked will display a table which lists all the meters currently experiencing the flag or alarm.

#To Do#
The software required to finish the build has not been acquired yet, and as such the application needs to be updated to query the Command Center database. A file containing queries that should work for this is included as "***************.txt".

Below is a rundown of each components functionality and what needs to be updated for a completed build:
1. MeterControllerServlet.java
2. DashboardDbUtil.java
3. dashboard.jsp
4. context.xml
5. Helper Java files
6. WebContent

######################################################################################################
====================================MeterControllerServlet.java=======================================
######################################################################################################

This is the Java Servlet controlling the other components in the application. You may think of it as the "C" in MVC while "dashboard.jsp" can be viewed as the "V". 

#@Resource#
The Resource tag at the top of the page "@Resource(name="jdbc/********")" tells the Server Application where to find your database. The value set for name should be the name of your resource as set in your context.xml.

#init()#
The first method run by the app. This will point your dashboardDbUtil object to the connection pool. 

#doGet()#
Interaction with the database is performed through the doGet method which performs as a HTTP GET method. Upon receiving a GET request from the dashboard.jsp the method will read the command parameter passed to it in the request parameter will perform one of two methods(aggregateMeters(), listMeters()) based on the value through the switch() statement. By default doGet will run aggregateMeters(request,response). 

-For the Servlet to perform new commands a new case should be added to the switch() statement. 

#aggregateMeters()#
The default command of the doGet method. aggregateMeters() will be called on page load and generates a list of pairs of all flag names and their flag count values using the dashboardDbUtil object. The list is broken into smaller lists by the general flag type and each list is set as an attribute and passed to the JSP.

-When adding new flags to be tracked a new list should be created to hold pairs of that general type.

######################################################################################################
