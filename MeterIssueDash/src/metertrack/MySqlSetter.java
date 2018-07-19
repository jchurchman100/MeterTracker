package metertrack;

/**
 * Helper Java class meant to assist DashboardDbUtil with querying a MySQL database. Contains methods which will provide the DashboardDbUtil
 * with the correct SQL needed to fulfill a request.
 * @author Jared Churchman
 * @version 4.3
 */
public class MySqlSetter {
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
				this.sql ="select n.error_date, n.error_type, m.* from nvm_errors as n " 
				+"join meters as m on m.meter_id = n.METER_ID order by n.error_date DESC";
				break;
			case "nvm_test":
				this.sql = "select n.Error_date, n.error_type, m.* from nvm_errors as n "
				+"join meters as m on m.meter_id = n.METER_ID "
				+"where n.error_type = 'TEST' order by n.error_date DESC";
				break;
			case "nvm_arm":
				this.sql = "select n.Error_date, n.error_type, m.* from nvm_errors as n "
				+"join meters as m on m.meter_id = n.METER_ID "
				+"where n.error_type = 'ARM' order by n.error_date DESC";
				break;
			case "nvm_any":
				this.sql = "select n.error_date, n.error_type, m.* from nvm_errors as n "
				+"join meters as m on m.meter_id = n.METER_ID "
				+"where n.error_type = 'GENERAL' AND (m.form = '9S' OR m.form = '16S') "
				+"AND (m.VOLTAGE_A < 108 OR m.voltage_b < 108 OR m.voltage_c < 108) "
				+"order by n.error_date DESC";
				break;
			
			/*Hot Socket Alerts*/
			case "hsa_3":
				this.sql = "select hcounts.voltage_outages, m.* from meters as m inner join "
						+ "(select h.meter_id,count(*) as voltage_outages "
						+ "from hot_socket_alerts as h where h.error_date >= (CURDATE() - INTERVAL 6 DAY ) "
						+ "group by h.meter_id) hcounts "
						+ "on hcounts.meter_id = m.meter_id "
						+ "where hcounts.voltage_outages >= 3 and hcounts.voltage_outages < 5";
				break;
			case "hsa_5":
				this.sql = "select hcounts.voltage_outages, m.* from meters as m inner join "
						+ "(select h.meter_id,count(*) as voltage_outages "
						+ "from hot_socket_alerts as h where h.error_date >= (CURDATE() - INTERVAL 6 DAY ) "
						+ "group by h.meter_id) hcounts "
						+ "on hcounts.meter_id = m.meter_id "
						+ "where hcounts.voltage_outages >= 5 and hcounts.voltage_outages < 10";
				break;
			case "hsa_10":
				this.sql = "select hcounts.voltage_outages, m.* from meters as m inner join "
						+ "(select h.meter_id,count(*) as voltage_outages "
						+ "from hot_socket_alerts as h where h.error_date >= (CURDATE() - INTERVAL 6 DAY ) "
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
				this.sql = "select p.error_date, p.error_type, m.* from meters as m "
						+ "join phase_out_errors as p on p.meter_id = m.meter_id "
						+ "order by p.error_date DESC, m.meter_id";
				break;
			case "phase_a":
				this.sql = "select p.error_date, p.error_type, m.* from meters as m "
						+ "join phase_out_errors as p on p.meter_id = m.meter_id "
						+ "where p.error_type = 'A' order by p.error_date DESC, m.meter_id";
				break;
			case "phase_b":
				this.sql = "select p.error_date, p.error_type, m.* from meters as m "
						+ "join phase_out_errors as p on p.meter_id = m.meter_id "
						+ "where p.error_type = 'B' order by p.error_date DESC, m.meter_id";
				break;
			case "phase_c":
				this.sql = "select p.error_date, p.error_type, m.* from meters as m "
						+ "join phase_out_errors as p on p.meter_id = m.meter_id "
						+ "where p.error_type = 'C' order by p.error_date DESC, m.meter_id";
				break;
			case "phase_ab":
				this.sql = "select p.error_date, p.error_type, m.* from meters as m "
				+"join phase_out_errors as p on p.meter_id = m.meter_id "
				+"where p.error_type = 'A' OR error_type ='B' "
				+"order by m.meter_id, p.ERROR_DATE DESC";
				break;
			case "phase_ac":
				this.sql = "select p.error_date, p.error_type, m.* from meters as m "
				+"join phase_out_errors as p on p.meter_id = m.meter_id "
				+"where p.error_type = 'A' OR error_type ='C' "
				+"order by m.meter_id, p.ERROR_DATE DESC";
				break;
			case "phase_bc":
				this.sql = "select p.error_date, p.error_type, m.* from meters as m "
				+"join phase_out_errors as p on p.meter_id = m.meter_id "
				+"where p.error_type = 'B' OR error_type ='C' "
				+"order by m.meter_id, p.ERROR_DATE DESC";
				break;
			
			/*Excessive Leading Current Alarms*/
			case "elc_total":
				this.sql = "select e.error_date, m.* from meters as m join elc_alarms_total as e "
						+ "on e.meter_id = m.meter_id order by e.error_date DESC";
		}
	}
	
	/**
	 * Returns a large SQL String used to query for the counts of all the flags/alarms
	 * @return SQL String to for aggregating flag counts
	 */
	public String bigSQL() {
			String bSql = "SELECT " 
							/*Non-Volatile Memory Errors*/
							+"(select count(*) from(select n.ERROR_DATE, n.ERROR_TYPE, m.* "
							+"from NVM_ERRORS as n join METERS as m on m.METER_ID = n.METER_ID)as tot) as nvm_total, "
											    	    
							+"(select count(*) from(select n.Error_date, n.ERROR_TYPE, m.* "
							+"from NVM_ERRORS as n join METERS as m on m.METER_ID = n.METER_ID " 
							+"where n.ERROR_TYPE = 'TEST')as tot) as nvm_test, "

							+"(select count(*) from(select n.Error_date, n.ERROR_TYPE, m.* "
							+"from NVM_ERRORS as n join METERS as m on m.METER_ID = n.METER_ID "
							+"where n.ERROR_TYPE = 'ARM' order by n.ERROR_DATE)as tot) as nvm_arm, "
											    	    
							+"(select count(*) from(select n.ERROR_DATE, n.ERROR_TYPE, m.* "
							+"from NVM_ERRORS as n join METERS as m on m.METER_ID = n.METER_ID "
							+"where n.ERROR_TYPE = 'GENERAL' AND (m.FORM = '9S' OR m.FORM = '16S') "
							+"AND (m.VOLTAGE_A < 108 OR m.voltage_b < 108 OR m.voltage_c < 108) "
							+"order by n.ERROR_DATE)as tot) as nvm_any, "
							
							/*Hot Socket Alerts*/

							+"(select count(*) from(select hcounts.voltage_outages, m.* from METERS as m inner join "
							+"(select h.METER_ID,count(*) as voltage_outages "
							+"from HOT_SOCKET_ALERTS as h where h.ERROR_DATE >= (CURDATE() - INTERVAL 6 DAY ) "
							+"group by METER_ID) as hcounts "
							+"on hcounts.METER_ID = m.METER_ID " 
							+"where hcounts.voltage_outages >= 3 and hcounts.voltage_outages < 5)as tot) as hsa_3, "
							        
							+"(select count(*) from(select hcounts.voltage_outages, m.* from METERS as m inner join "
							+"(select h.METER_ID,count(*) as voltage_outages "
							+"from HOT_SOCKET_ALERTS as h where h.ERROR_DATE >= (CURDATE() - INTERVAL 6 DAY ) "
							+"group by METER_ID) as hcounts "
							+"on hcounts.METER_ID = m.METER_ID "
							+"where hcounts.voltage_outages >= 5 and hcounts.voltage_outages < 10)as tot) as hsa_5, "
								        
							+"(select count(*) from(select hcounts.voltage_outages, m.* from METERS as m inner join "
							+"(select h.METER_ID,count(*) as voltage_outages "
							+"from HOT_SOCKET_ALERTS as h where h.ERROR_DATE >= (CURDATE() - INTERVAL 6 DAY ) "
							+"group by METER_ID) as hcounts "
							+"on hcounts.METER_ID = m.METER_ID "
							+"where hcounts.voltage_outages >= 10) as tot) as hsa_10, "
							
							/*Wiring Errors*/

							+"(select count(*) from (select * from METERS where FORM = '9S' AND VOLTAGE_A < 3)as tot) as wire9_3V, "
							+"(select count(*) from (select * from METERS where FORM = '9S' AND CURRENT_A < 3)as tot) as wire9_3C, "
							+"(select count(*) from (select * from METERS where FORM = '9S' AND (VOLTAGE_A < 108 "
							+"OR VOLTAGE_B < 108 OR VOLTAGE_C < 108))as tot) as wire9_108V, "
									        
							+"(select count(*) from (select * from METERS where FORM = '16S' AND VOLTAGE_A < 3)as tot) as wire16_3V, "
							+"(select count(*) from (select * from METERS where FORM = '16S' AND CURRENT_A < 3)as tot) as wire16_3C, "
							+"(select count(*) from (select * from METERS where FORM = '16S' AND (VOLTAGE_A < 108 "
							+"OR VOLTAGE_B < 108 OR VOLTAGE_C < 108))as tot) as wire16_108V, "
								    	    
							+"(select count(*) from (select * from METERS where FORM = '2S' AND VOLTAGE_A > 255 )as tot) as wire2_High, "
							+"(select count(*) from (select * from METERS where FORM = '2S' AND VOLTAGE_A < 216 )as tot) as wire2_Low, "
							+"(select count(*) from (select * from METERS where FORM = '12S' AND VOLTAGE_A > 255 )as tot) as wire12_High, "
							+"(select count(*) from (select * from METERS where FORM = '12S' AND VOLTAGE_A < 216 )as tot) as wire12_Low, "
							
							/*Phase Out Errors*/
							
							+"(select count(*) from (select p.ERROR_DATE, p.ERROR_TYPE, m.* "
							+"from METERS as m join PHASE_OUT_ERRORS as p on p.METER_ID = m.METER_ID "
							+"order by p.ERROR_DATE, m.METER_ID)as tot) as phase_Total, "
								    	    
							+"(select count(*) from (select p.ERROR_DATE, p.ERROR_TYPE, m.* "
							+"from METERS as m join PHASE_OUT_ERRORS as p on p.METER_ID = m.METER_ID "
							+"where p.ERROR_TYPE = 'A' order by p.ERROR_DATE, m.METER_ID)as tot) as phase_A, "
									    	    
							+"(select count(*) from (select p.ERROR_DATE, p.ERROR_TYPE, m.* "
							+"from METERS as m join PHASE_OUT_ERRORS as p on p.METER_ID = m.METER_ID "
							+"where p.ERROR_TYPE = 'B' order by p.ERROR_DATE, m.METER_ID)as tot) as phase_B, "
								    	    
							+"(select count(*) from (select p.ERROR_DATE, p.ERROR_TYPE, m.* "
							+"from METERS as m join PHASE_OUT_ERRORS as p on p.METER_ID = m.METER_ID "
							+"where p.ERROR_TYPE = 'C' order by p.ERROR_DATE, m.METER_ID)as tot) as phase_C, "
								    	    
							+"(select count(*) from (select p.ERROR_DATE, p.ERROR_TYPE, m.* "
							+"from METERS as m join PHASE_OUT_ERRORS as p on p.METER_ID = m.METER_ID "
							+"where p.ERROR_TYPE = 'A' OR p.ERROR_TYPE = 'B' "
							+"order by p.ERROR_DATE, m.METER_ID)as tot) as phase_AB, "
										    	    
							+"(select count(*) from (select p.ERROR_DATE, p.ERROR_TYPE, m.* "
							+"from METERS as m join PHASE_OUT_ERRORS as p on p.METER_ID = m.METER_ID "
							+"where p.ERROR_TYPE = 'A' OR p.ERROR_TYPE = 'C' "
							+"order by p.ERROR_DATE, m.METER_ID)as tot) as phase_AC, "
											    	    
							+"(select count(*) from (select p.ERROR_DATE, p.ERROR_TYPE, m.* "
							+"from METERS as m join PHASE_OUT_ERRORS as p on p.METER_ID = m.METER_ID "
							+"where p.ERROR_TYPE = 'B' OR p.ERROR_TYPE = 'C' "
							+"order by p.ERROR_DATE, m.METER_ID)as tot) as phase_BC, "
							
							/*Excessive Leading Current Alarms*/
											    	    
							+"(select count(*) from (select e.ERROR_DATE, m.* from METERS as m "
							+"join ELC_ALARMS_TOTAL as e on e.METER_ID = m.METER_ID order by e.ERROR_DATE)as tot) as elc_total";

			return bSql;
	}
}
