package metertrack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * Helper Java class to assist the Meter Controller Servlet in communicating with and querying the database.
 * @author Jared Churchman
 * @version 5.4
 */
public class DashboardDbUtil {
	
	private DataSource dataSource;
	
	/**
	 * Constructor. Sets the DataSource for the DashboardDbUtil object to create connections with. 
	 * @param theDataSource Connection pool object
	 */
	public DashboardDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	/**
	 * Queries the database and generates an ArrayList of meters based on the flag passed from MeterControllerServlet.
	 * @param meterFlag The flag name passed from the MCS
	 * @return A list of meters for the specified flag
	 * @throws Exception
	 */
	public List<Meter> getMeters(String meterFlag) throws Exception{
		List<Meter> meters = new ArrayList<>();
		String sql = null;
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		MySqlSetter setter = null;
		//SqlSetter setter = null;
		
		try {
			myConn = dataSource.getConnection();			//get a connection
			setter = new MySqlSetter();
			//setter = new SqlSetter();
			
			sql = setter.getSql(meterFlag.toLowerCase());	//create SQL String based on meter flag value
			myStmt = myConn.createStatement();				//create the statement object
			myRs = myStmt.executeQuery(sql.toUpperCase());	//execute the query
			
			//Grab meter data from each row of the Result Set
			while (myRs.next()) {
				int id = myRs.getInt("meter_id");
				String program = myRs.getString("program_id");
				String form = myRs.getString("form");
				String info = meterFlag.toUpperCase();
				String date = "";
				try {date = myRs.getString("error_date");}
				catch(java.sql.SQLException s) {}
				double volA = myRs.getDouble("voltage_a");		
				double volB = myRs.getDouble("voltage_b");
				double volC = myRs.getDouble("voltage_c");
				double curA = myRs.getDouble("current_a");
				double curB = myRs.getDouble("current_b");
				double curC = myRs.getDouble("current_c");
				
				//Create a meter object to add to the list
				Meter tempMeter = new Meter(id, program, form, info, volA, volB, volC, curA, curB, curC);
				
				//If a date value was found in the result set add it to the meter else '-'
				if(date != null && !date.equals("")) {
					tempMeter.setDate(date);
				}
				else {
					tempMeter.setDate("-");
				}
				
				meters.add(tempMeter);	//Add the meter to the meter list
			}
			return meters;
		}
		finally {
			close(myConn, myStmt, myRs);	//close JDBC objects
		}	
	}
	
	/**
	 * Queries the database and generates an ArrayList of Pair objects with the flag type as the key and the
	 * value as an Integer representing the occurrences of the flag.
	 * @return An ArrayList of Pair objects of the form (Flag, Count)
	 * @throws Exception
	 */
	public List<Pair<String, Integer>> getTotals() throws Exception{
		List<Pair<String, Integer>> counts = new ArrayList<>();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		ResultSetMetaData rsmd = null;
		MySqlSetter setter = new MySqlSetter();
		//SqlSetter setter = new SqlSetter();

		try { 
			myConn = dataSource.getConnection();	//get a connection
			String sql = setter.bigSQL();			//set the SQL String using SqlSetter object
			myStmt = myConn.createStatement();		//create statement object
			myRs = myStmt.executeQuery(sql);		//execute SQL query
			rsmd = myRs.getMetaData();				//retrieve ResultSet MetaData to get number of columns and column names
			int colcount = rsmd.getColumnCount();	//retrieve number of columns
			
			//Extract the column name and the count value for each column in the ResultSet row
			while(myRs.next()) {
				for(int i = 1; i <= colcount;  i++) {
					String name = rsmd.getColumnName(i).toUpperCase();
					Pair<String, Integer> entry = new Pair<>(name,myRs.getInt(name));
					counts.add(entry);
				 }
			}
			return counts;
		}
		finally {
			close(myConn, myStmt, myRs);	//Close result set, statement, and connection
		}
	}
	
	/**
	 * Finds the meter information searched for in the Search Bar.
	 * @param searchId The ID of the meter being searched
	 * @return Meter object containing database information
	 * @throws Exception
	 */
	public Meter findMeter(String searchId) throws Exception {
		Meter m = null; 
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		//Create an array of error table names
		String errors[] = {"nvm_errors", "hot_socket_alerts", "phase_out_errors", "elc_alarms_total"};	
		
		try {
			myConn = dataSource.getConnection();
			
			//Querying for the specified meter
			String sql = "select * from meters as m where m.meter_id = ?";
			myStmt = myConn.prepareStatement(sql.toUpperCase());
			myStmt.setInt(1, Integer.parseInt(searchId));
			myRs = myStmt.executeQuery();
				
			while(myRs.next()) {
				int id = myRs.getInt("meter_id");
				String program = myRs.getString("program_id");
				String form = myRs.getString("form");
				double volA = myRs.getDouble("voltage_a");		
				double volB = myRs.getDouble("voltage_b");
				double volC = myRs.getDouble("voltage_c");
				double curA = myRs.getDouble("current_a");
				double curB = myRs.getDouble("current_b");
				double curC = myRs.getDouble("current_c");
				String info = "";
				
				//Checking to see which errors the specified meter has flagged
				for(String error: errors) {
					sql = "select * from " + error + " where " + error + ".meter_id = ?";
					myStmt = myConn.prepareStatement(sql.toUpperCase());
					myStmt.setInt(1, Integer.parseInt(searchId));
					if(myStmt.executeQuery() == null) {		//if meter exists in flag table add flag to info
						info += error + " ";
					}
				}
				
				//Create the meter object
				m = new Meter(id, program, form, info, volA, volB, volC, curA, curB, curC);
			}
			return m;
		}
		finally {
			close(myConn, myStmt, myRs);	//Close JDBC objects
		}
	}
	
	/**
	 * 
	 * @param myConn Connection from the connection pool to the data source
	 * @param myStmt SQL statement object for communicating with database
	 * @param myRs ResultSet containing returned data
	 */
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if(myRs != null)
				myRs.close();
			if(myStmt != null) {
				myStmt.close();
			}
			if(myConn != null) {
				myConn.close(); //doesn't really close, puts back in connection pool
			}
		}
		catch(Exception exc) {
			exc.printStackTrace();	
		}
	}
}
