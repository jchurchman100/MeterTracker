package metertrack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class DashboardDbUtil {
	
	private DataSource dataSource;
	
	public DashboardDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Meter> getMeters(String meterFlag) throws Exception{
		List<Meter> meters = new ArrayList<>();
		String sql = null;
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		MySqlSetter setter = null;
		//SqlSetter setter = null;
		
		try {
			//get a connection
			myConn = dataSource.getConnection();
			//prepare and create sql statement
			setter = new MySqlSetter();
			sql = setter.getSql(meterFlag.toLowerCase());
			myStmt = myConn.createStatement();
			//execute query 
			myRs = myStmt.executeQuery(sql.toUpperCase());
			
			//process result set
			while (myRs.next()) {
				//retrieve data from result set row
				int id = myRs.getInt("meter_id");
				String program = myRs.getString("program_id");
				String form = myRs.getString("form");
				String info = "meterflag".toUpperCase();
				String date = "";
				try {date = myRs.getString("error_date");}
				catch(java.sql.SQLException s) {}
				double volA = myRs.getDouble("voltage_a");		
				double volB = myRs.getDouble("voltage_b");
				double volC = myRs.getDouble("voltage_c");
				double curA = myRs.getDouble("current_a");
				double curB = myRs.getDouble("current_b");
				double curC = myRs.getDouble("current_c");
				
				//create new student object
				Meter tempMeter = new Meter(id, program, form, meterFlag,
						volA, volB, volC, curA, curB, curC);
				
				if(date != null && !date.equals(""))
					tempMeter.setDate(date);
				else
					tempMeter.setDate("-");
				//add it to the list of students
				meters.add(tempMeter);
			}
			return meters;
		}
		//close JDBC objects
		finally {
			close(myConn, myStmt, myRs);
		}	
	}

	public List<Pair<String, Integer>> getTotals() throws Exception{
		//create connection and set vars
		List<Pair<String, Integer>> tots = new ArrayList<>();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		ResultSetMetaData rsmd = null;
		MySqlSetter setter = new MySqlSetter();
		//SqlSetter setter = new SqlSetter();

		try { 
			//get connection
			myConn = dataSource.getConnection();
			//prepare a SQL statement
			String sql = setter.bigSQL();
			myStmt = myConn.createStatement();
			//execute query and get meta data
			myRs = myStmt.executeQuery(sql);
			rsmd = myRs.getMetaData();
			int colcount = rsmd.getColumnCount();
			//extract from ResultSet
			while(myRs.next()) {
				//Fill the list
				for(int i = 1; i <= colcount;  i++) {
					String name = rsmd.getColumnName(i);
					Pair<String, Integer> entry = new Pair<>(name,myRs.getInt(name));
					tots.add(entry);
				 }
			}
			return tots;
		}
		finally {
			close(myConn, myStmt, myRs);
		}
	}
	
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		// TODO Auto-generated method stub
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
