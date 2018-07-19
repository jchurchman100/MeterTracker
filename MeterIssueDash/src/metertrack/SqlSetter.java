package metertrack;

/**
 * Helper Java class meant to assist DashboardDbUtil with querying an Oracle database. Contains methods which will provide the DashboardDbUtil
 * with the correct SQL needed to fulfill a request.
 * @author Jared Churchman
 * @version 4.3
 */
public class SqlSetter {
	private String sql;
	
	/**
	 * Calls setSql with flag_id and then returns sql  field
	 * @param flag_id Flag ID to query for in the database
	 * @return SQL String to query for the specified flag with
	 */
	public String getSql(String flag_id) {
		setSql(flag_id);
		return sql;
	}

	/**
	 * Sets sql field to the correct query String using the Switch statement
	 * @param flag_id Flag ID to query for in the database
	 */
	public void setSql(String flag_id) {	
		switch(flag_id){
			/*Non-Volatile Memory Errors*/
			case "nvm_total":
				this.sql ="select n.error_date, n.error_type, m.* from nvm_errors n " 
				+"join meters m on m.meter_id = n.METER_ID order by n.error_date DESC";
				break;
			case "nvm_test":
				this.sql = "select n.Error_date, n.error_type, m.* from nvm_errors n "
				+"join meters m on m.meter_id = n.METER_ID "
				+"where n.error_type = 'TEST' order by n.error_date DESC";
				break;
			case "nvm_arm":
				this.sql = "select n.Error_date, n.error_type, m.* from nvm_errors n "
				+"join meters m on m.meter_id = n.METER_ID "
				+"where n.error_type = 'ARM' order by n.error_date DESC";
				break;
			case "nvm_any":
				this.sql = "select n.error_date, n.error_type, m.* from nvm_errors n "
				+"join meters m on m.meter_id = n.METER_ID "
				+"where n.error_type = 'GENERAL' AND (m.form = '9S' OR m.form = '16S') "
				+"AND (m.VOLTAGE_A < 108 OR m.voltage_b < 108 OR m.voltage_c < 108) "
				+"order by n.error_date DESC";
				break;
				
			/*Hot Socket Alerts*/
			case "hsa_3":
				this.sql = "select hcounts.voltage_outages, m.* from meters m inner join "
						+ "(select h.meter_id,count(*) as voltage_outages "
						+ "from hot_socket_alerts h where h.error_date >= (sysdate-6) "
						+ "group by h.meter_id) hcounts "
						+ "on hcounts.meter_id = m.meter_id "
						+ "where hcounts.voltage_outages >= 3 and hcounts.voltage_outages < 5";
				break;
			case "hsa_5":
				this.sql = "select hcounts.voltage_outages, m.* from meters m inner join "
						+ "(select h.meter_id,count(*) as voltage_outages "
						+ "from hot_socket_alerts h where h.error_date >= (sysdate-6) "
						+ "group by h.meter_id) hcounts "
						+ "on hcounts.meter_id = m.meter_id "
						+ "where hcounts.voltage_outages >= 5 and hcounts.voltage_outages < 10";
				break;
			case "hsa_10":
				this.sql = "select hcounts.voltage_outages, m.* from meters m inner join "
						+ "(select h.meter_id,count(*) as voltage_outages "
						+ "from hot_socket_alerts h where h.error_date >= (sysdate-6) "
						+ "group by h.meter_id) hcounts "
						+ "on hcounts.meter_id = m.meter_id "
						+ "where hcounts.voltage_outages >= 10";
				break;
			
			/*Wiring Errors*/
			case "wire9_3v":
				this.sql = "select * from meters where form = '9S' AND VOLTAGE_A < 3";
				break;
			case "wire9_3c":
				this.sql = "select * from meters where form = '9S' AND CURRENT_A < 3";
				break;
			case "wire9_108v":
				this.sql = "select * from meters where form = '9S' "
						+ "AND (VOLTAGE_A < 108 OR VOLTAGE_B < 108 OR VOLTAGE_C < 108)";
				break;
			case "wire16_3v":
				this.sql = "select * from meters where form = '16S' AND VOLTAGE_A < 3";
				break;
			case "wire16_3c":
				this.sql = "select * from meters where form = '16S' AND CURRENT_A < 3";
				break;
			case "wire16_108v":
				this.sql = "select * from meters where form = '16S' "
						+ "AND (VOLTAGE_A < 108 OR VOLTAGE_B < 108 OR VOLTAGE_C < 108)";
				break;
			case "wire2_high":
				this.sql = "select * from meters where form = '2S' AND VOLTAGE_A > 255";
				break;
			case "wire2_low":
				this.sql = "select * from meters where form = '2S' AND VOLTAGE_A < 216";
				break;
			case "wire12_high":
				this.sql = "select * from meters where form = '12S' AND VOLTAGE_A > 255";
				break;
			case "wire12_low":
				this.sql = "select * from meters where form = '12S' AND VOLTAGE_A < 216";
				break;
			
			/*Phase Out Errors*/
			case "phase_total":
				this.sql = "select p.error_date, p.error_type, m.* from meters m "
						+ "join phase_out_errors p on p.meter_id = m.meter_id "
						+ "order by p.error_date DESC, m.meter_id";
				break;
			case "phase_a":
				this.sql = "select p.error_date, p.error_type, m.* from meters m "
						+ "join phase_out_errors p on p.meter_id = m.meter_id "
						+ "where p.error_type = 'A' order by p.error_date DESC, m.meter_id";
				break;
			case "phase_b":
				this.sql = "select p.error_date, p.error_type, m.* from meters m "
						+ "join phase_out_errors p on p.meter_id = m.meter_id "
						+ "where p.error_type = 'B' order by p.error_date DESC, m.meter_id";
				break;
			case "phase_c":
				this.sql = "select p.error_date, p.error_type, m.* from meters m "
						+ "join phase_out_errors p on p.meter_id = m.meter_id "
						+ "where p.error_type = 'C' order by p.error_date DESC, m.meter_id";
				break;
			case "phase_ab":
				this.sql = "select p.error_date, p.error_type, m.* from meters m "
				+"join phase_out_errors p on p.meter_id = m.meter_id "
				+"where p.error_type = 'A' OR error_type ='B' "
				+"order by m.meter_id, p.ERROR_DATE DESC";
				break;
			case "phase_ac":
				this.sql = "select p.error_date, p.error_type, m.* from meters m "
				+"join phase_out_errors p on p.meter_id = m.meter_id "
				+"where p.error_type = 'A' OR error_type ='C' "
				+"order by m.meter_id, p.ERROR_DATE DESC";
				break;
			case "phase_bc":
				this.sql = "select p.error_date, p.error_type, m.* from meters m "
				+"join phase_out_errors p on p.meter_id = m.meter_id "
				+"where p.error_type = 'B' OR error_type ='C' "
				+"order by m.meter_id, p.ERROR_DATE DESC";
				break;
			
			/*Excessive Leading Current Alarms*/
			case "elc_total":
				this.sql = "select e.error_date, m.* from meters m join elc_alarms_total e "
						+ "on e.meter_id = m.meter_id order by e.error_date DESC";
		}
	}
	
	/**
	 * Returns a large SQL String used to query for the counts of all the flags/alarms
	 * @return SQL String to for aggregating flag counts
	 */
	public String bigSQL() {
			String bSql = "select "
					/*Non-Volatile Memory Errors*/
				    + "(select count(*) from(select n.error_date, n.error_type, m.*" 
				    + "from nvm_errors n join meters m on m.meter_id = n.METER_ID)) nvm_total, "
				    	    
				    +"(select count(*) from(select n.Error_date, n.error_type, m.* "
				    +"from nvm_errors n join meters m on m.meter_id = n.METER_ID "
				    +"where n.error_type = 'TEST')) nvm_test, "
				    	    
				    +"(select count(*) from(select n.Error_date, n.error_type, m.* "
				    +"from nvm_errors n join meters m on m.meter_id = n.METER_ID "
				    +"where n.error_type = 'ARM' order by n.error_date)) nvm_arm, "
				    	    
				    +"(select count(*) from(select n.error_date, n.error_type, m.* "
				    +"from nvm_errors n join meters m on m.meter_id = n.METER_ID "
				    +"where n.error_type = 'GENERAL' AND (m.form = '9S' OR m.form = '16S') "
				    +"AND (m.VOLTAGE_A < 108 OR m.voltage_b < 108 OR m.voltage_c < 108) "
				    +"order by n.error_date)) nvm_any, "
				    
				    /*Hot Socket Alerts*/
				    +"(select count(*) from(select hcounts.voltage_outages, m.* from meters m inner join"
				    +"(select h.meter_id,count(*) as voltage_outages "
				    +"from hot_socket_alerts h where h.error_date >= (sysdate-6) "
				  	+"group by meter_id) hcounts "
				    +"on hcounts.meter_id = m.meter_id "
				    +"where hcounts.voltage_outages >= 3 and hcounts.voltage_outages < 5)) hsa_3, "
				        
				    +"(select count(*) from(select hcounts.voltage_outages, m.* from meters m inner join "
				    +"(select h.meter_id,count(*) as voltage_outages "
				    +"from hot_socket_alerts h where h.error_date >= (sysdate-6) "
				    +"group by meter_id) hcounts "
				    +"on hcounts.meter_id = m.meter_id "
				    +"where hcounts.voltage_outages >= 5 and hcounts.voltage_outages < 10)) hsa_5, "
				        
				    +"(select count(*) from(select hcounts.voltage_outages, m.* from meters m inner join "
				    +"(select h.meter_id,count(*) as voltage_outages "
				    +"from hot_socket_alerts h where h.error_date >= (sysdate-6) "
				    +"group by meter_id) hcounts "
				    +"on hcounts.meter_id = m.meter_id "
				    +"where hcounts.voltage_outages >= 10)) hsa_10, "
				        
				    /*Wiring Errors*/
				    +"(select count(*) from (select * from meters where form = '9S' AND VOLTAGE_A < 3)) wire9_3V, "
				    +"(select count(*) from (select * from meters where form = '9S' AND CURRENT_A < 3)) wire9_3C, "
				    +"(select count(*) from (select * from meters where form = '9S' AND (VOLTAGE_A < 108 "
				    +"OR VOLTAGE_B < 108 OR VOLTAGE_C < 108))) wire9_108V, "
				        
				    +"(select count(*) from (select * from meters where form = '16S' AND VOLTAGE_A < 3)) wire16_3V, "
				    +"(select count(*) from (select * from meters where form = '16S' AND CURRENT_A < 3)) wire16_3C, "
				    +"(select count(*) from (select * from meters where form = '16S' AND (VOLTAGE_A < 108 "
				    +"OR VOLTAGE_B < 108 OR VOLTAGE_C < 108))) wire16_108V, "
				    	    
				    +"(select count(*) from (select * from meters where form = '2S' AND VOLTAGE_A > 255 )) wire2_High, "
				    +"(select count(*) from (select * from meters where form = '2S' AND VOLTAGE_A < 216 )) wire2_Low, "
				    +"(select count(*) from (select * from meters where form = '12S' AND VOLTAGE_A > 255 )) wire12_High, "
				    +"(select count(*) from (select * from meters where form = '12S' AND VOLTAGE_A < 216 )) wire12_Low, "
				    
				    /*Phase Out Errors*/
				    +"(select count(*) from (select p.error_date, p.error_type, m.* "
				    +"from meters m join phase_out_errors p on p.meter_id = m.meter_id "
				    +"order by p.error_date, m.meter_id)) phase_Total, "
				    	    
				    +"(select count(*) from (select p.error_date, p.error_type, m.* "
				    +"from meters m join phase_out_errors p on p.meter_id = m.meter_id "
				    +"where p.error_type = 'A' order by p.error_date, m.meter_id)) phase_A, "
				    	    
				    +"(select count(*) from (select p.error_date, p.error_type, m.* "
				    +"from meters m join phase_out_errors p on p.meter_id = m.meter_id "
				    +"where p.error_type = 'B' order by p.error_date, m.meter_id)) phase_B, "
				    	    
				    +"(select count(*) from (select p.error_date, p.error_type, m.* "
				    +"from meters m join phase_out_errors p on p.meter_id = m.meter_id "
				    +"where p.error_type = 'C' order by p.error_date, m.meter_id)) phase_C, "
				    	    
				    +"(select count(*) from (select p.error_date, p.error_type, m.* "
				    +"from meters m join phase_out_errors p on p.meter_id = m.meter_id "
				    +"where p.error_type = 'A' OR p.error_type = 'B' "
				    +"order by p.error_date, m.meter_id)) phase_AB, "
				    	    
				    +"(select count(*) from (select p.error_date, p.error_type, m.* "
				    +"from meters m join phase_out_errors p on p.meter_id = m.meter_id "
				    +"where p.error_type = 'A' OR p.error_type = 'C' "
				    +"order by p.error_date, m.meter_id)) phase_AC, "
				    	    
				    +"(select count(*) from (select p.error_date, p.error_type, m.* "
				    +"from meters m join phase_out_errors p on p.meter_id = m.meter_id "
				    +"where p.error_type = 'B' OR p.error_type = 'C' "
				    +"order by p.error_date, m.meter_id)) phase_BC, "
				    
				    /*Excessive Leading Current Alarms*/
				    +"(select count(*) from (select e.error_date, m.* from meters m "
				    +"join elc_alarms_total e on e.meter_id = m.meter_id order by e.error_date)) elc_total "
				    	    
				+"from DUAL";

			return bSql;
	}
}
