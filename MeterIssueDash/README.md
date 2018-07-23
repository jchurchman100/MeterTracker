#MeterIssueDash(CMO Dashboard)

#Note
A brief JavaDoc documentation can be found at "MeterIssueDash/doc/index.html".
# = General Discussion
* = To Do Discussion

#Summary
MeterIssueDash is a Java Web Application running on a Servlet(MeterControllerServlet.java) and a JSP, or Java Servers Page(dashboard.jsp). The main objective of the application is to display tables containing different meter alarms, flags, and error names along with the number of occurrances within the Command Center AMI database. The number of occurances are listed in the tables as hyperlinks and when clicked will display a table which lists all the meters currently experiencing the flag or alarm.

*To Do*
The software required to finish the build has not been acquired yet, and as such the application needs to be updated to query the Command Center database. A file containing queries that should work for this is included as "***************.txt".

Below is a rundown of each components functionality and what needs to be updated for a completed build:
1. MeterControllerServlet.java
2. DashboardDbUtil.java
3. SqlSetter.java/MySqlSetter.java
4. Meter.java
5. dashboard.jsp
6. context.xml
7. WebContent

######################################################################################################
=================================== MeterControllerServlet.java ======================================
######################################################################################################

This is the Java Servlet controlling the other components in the application. You may think of it as the "C" in MVC while "dashboard.jsp" can be viewed as the "V". 

#@Resource#
The Resource tag at the top of the page "@Resource(name="jdbc/********")" tells the Server Application where to find your database and sets your DataSource object to point to it. The value set for name should be the name of your resource as set in your context.xml.

#init()#
The first method run by the app. This will point your "dashboardDbUtil" object to the connection pool using the dataSource object.

#doGet()#
Interaction with the database is performed through the doGet method which performs as a HTTP GET method. Upon receiving a GET request from the dashboard.jsp the method will read the command parameter passed to it in the request parameter will perform one of two methods(aggregateMeters(), listMeters()) based on the value through the switch() statement. By default doGet will run aggregateMeters(request,response). 

*For the Servlet to perform new commands a new case should be added to the switch() statement.*

#aggregateMeters()#
The default command of the doGet method. aggregateMeters() will be called on page load and generates a list of pairs of all flag names and their flag count values using the dashboardDbUtil object. The list is broken into smaller lists by the general flag type and each list is set as an attribute and passed to the JSP.

*When adding new flags to be tracked a new list should be created to hold pairs of that general type.*

#listMeters()#
LIST command. Utilizes dashboardDbUtil to query the database based on the "flag_id" parameter sent to the MCS by the associated hyperlink on the JSP page. If no results are found the request attribute is set to the "NO_RESULTS" attribute, else the "FLAGGED_METERS" attribute is set with the ArrayList of Meter objects. aggregateMeters() is then called to reset the totals hyperlinks and dispatch back to the JSP.

#searchMeter()#
SEARCH command. Retrieves "SearchId" parameter sent from the search bar in the JSP and uses the dashboardDbUtil to query the database for the specified meter. If the meter is not found the "NO_RESULTS" attribute is set, else the "FOUND_METER" attribute is set with the meter data. aggregateMeters() is then called to reset the totals hyperlinks and dispatch back to the JSP.

#doPost()#
Currently calls to the doGet method. Should be used for requests for login authentication. 

######################################################################################################
======================================= DashboardDbUtil.java =========================================
######################################################################################################

This is the main helper class and executes all of the interactions between the application and the database. Constructed with a DataSource object pointing to the database, sent from the MCS. 

#getMeters()#
Queries the database for the specified flag type, "meterFlag", with the help of a SqlSetter class object and puts the data into an ArrayList of Meter objects. The String "date" variable is set individually using its setter method as not all flags have a date. The list of meters is then returned to the MCS and the JDBC objects are closed.

#getTotals()#
Queries the database for the SQL count values for every flag/alarm type with the help of a SqlSetter  object's biqSQL() method. The data is then put into an ArrayList of Pair objects with the flag name as the left-hand String value and the count as the right-hand Integer value. The list of pairs is returned to the MCS and the JDBC objects are closed.

#findMeter()#
Queries the database for the specified meter ID, "searchID", and puts the data into a meter object. The information field of the meter object is set to contain the names of the flag tables the meter is found in.

*When adding new flags to be queried, the new flag table names should be added to the errors[] array if there are tables containing the specified flags. Otherwise with the current schema structure the sql String will need to be replaced with a switch statement of sql queries to run for determining what flags the meter contains. This can likely be implemented as another method in the SqlSetter class.*

#close()#
Closes all the JDBC objects

######################################################################################################
==================================== SqlSetter.java/MySqlSetter.java =================================
######################################################################################################

This is a helper class for use with the DashboardDbUtil class. Contains a String sql field for holding the queries returned by the class methods. 

*This is the main class to be modified for use with the Command Center database. The queries for each case in the setSql switch statement and the query segments in the bigSql method need to be modified for the correct schema structure. A file containing queries that should work for this is included as "***************.txt".*

#getSql()/setSql#
The getSql method calls setSql with the flag_id variable sent from DashboardDbUtil and returns the sql field. 

In setSql the sql field is set based on the value of the flag_id variable passed in the switch statement. Each case represents a specific alarm or error to to be displayed in the HTML tables in dashboard.jsp. 

*This is where a large portion of rewriting should be done. The String that sql is set to should work for the Command Center database.*

#bigSql()#
This method sets the sql field to the large SQL query that will return all of the count aggregates for each flag. The general form of each count is the corresponding "case query" in a count wrapper with an alias, eg: *(select count(*) from (/*CASE QUERY*/)) flag_column_name,* . 

*Another portion of rewriting will of course need to be done to match the setSql cases.*

######################################################################################################
============================================= Meter.java =============================================
######################################################################################################

Class defining Meter objects. Should represent the data from a meter pulled from the database such as meter ID, program ID, and event date. The constructor does not currently incude the date field as a parameter as not all flags contain an event date in the current database. 

*It was only important to include this file in the readme as you will need to edit the fields in the class to represent the data to be represented in the HTML tables in dashboard.jsp.*

######################################################################################################
=========================================== dashboard.jsp ============================================
######################################################################################################

This file will dynmically generate an HTML page based on actions performed by MeterControllerServlet.java. There is not much logic being performed as most operations are done within the Servlet itself. Each tab on the page will display the table of aggregated flag counts for that alarm. Clicking the hyperlinked aggregate will display the list of meters under that flag. You may also search an individual meter ID in the search input in the navigation bar. 

*For new alarms you will need to add a div of class "etabcontent" and an id of the general error type with a nested table matching the form of the tables currently there. You will also need to add a new button in the "etabs" div. The table fields for the meter display tables will need to be modified to accommodate the fields set in the Meter class.*

######################################################################################################
=========================================== context.xml =============================================
######################################################################################################

The context.xml file defines the Resource information for your DataSource object for the server application that the web app is deployed on. 

The name is what you will call in your MeterControllerServlet to access the database. The username, password, and url should be regular credentials for accessing the database. 

The user must have permissions for access to all tables in the CENTRALSERVICES schema. The Resource under the Oracle Database comment should be modified and used for the command center database.