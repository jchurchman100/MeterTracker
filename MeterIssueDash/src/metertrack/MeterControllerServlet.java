
package metertrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import metertrack.DashboardDbUtil;

/**
 * Servlet implementation class MeterControllerServlet. Generates the tables of Meters data displayed on the JSP
 * @author Jared Churchman
 * @version 5.4
 */
@WebServlet("/MeterControllerServlet")
public class MeterControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DashboardDbUtil dashboardDbUtil;
	
    /*
    @Resource(name="jdbc/metering")
	private DataSource dataSource;*/
    
    //Sets the DataSource to extract from as defined in context.xml
    @Resource(name="jdbc/ebdb")
    private DataSource dataSource;
    
    /**
     * The first method called in the application. Initializes the dashboardDbUtil to point at the connection pool.
     */
	public void init() throws ServletException {
		super.init();
		
		//create dashboardDbUtil and pass it the connection pool/data source
		try {
			dashboardDbUtil = new DashboardDbUtil(dataSource);
		}
		catch(Exception exc) {
			throw new ServletException(exc);
		}
	}
	
	/**
	 * Handles HTTP GET requests. Executes based on the value of the command parameter sent.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String theCommand = request.getParameter("command");	//read the "command" parameter

			//if command is missing then default to meter aggregation
			if(theCommand == null) {
				theCommand = "AGG";
			}
			
			//route to the appropriate method
			switch(theCommand) {
				case "AGG":
					aggregateMeters(request,response);
					break;
				case "LIST":
					listMeters(request,response);
					break;
				case "SEARCH":
					searchMeter(request,response);
					break;
					
				default:
					aggregateMeters(request, response);
			}
		}
		catch(Exception exc) {
			throw new ServletException(exc);
		}
	}

	/**
	 * Generates total flag values for each flag type in a list of pairs and breaks the list into
	 * smaller lists for each general flag type. Each list is set as an attribute and then all are
	 * dispatched to the JSP page.
	 * @param request HTTPRequest object
	 * @param response HTTPResponse object
	 * @throws Exception Issue with DataSource
	 */
	private void aggregateMeters(HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		List<Pair<String,Integer>> totals = new ArrayList<>();
		int i = 0;
		
		//Create a list of all the pairs of issues with their respective flag counts
		totals = dashboardDbUtil.getTotals();
		
		//Breaking up the big list of pairs to accommodate the forEach loops in JSP
		List<Pair<String, Integer>> nvm_totals = new ArrayList<>();
		List<Pair<String, Integer>> hsa_totals = new ArrayList<>();
		List<Pair<String, Integer>> wiring_totals = new ArrayList<>();
		List<Pair<String, Integer>> phase_totals = new ArrayList<>();
		
		for(Pair<String, Integer> entry: totals) {
	        
	        if(i < 4) {
	        	nvm_totals.add(entry);
	        }
	        else if(i < 7) {
	        	hsa_totals.add(entry);
	        }
	        else if(i < 17) {
	        	wiring_totals.add(entry);
	        }
	        else if(i < 24) {
	        	phase_totals.add(entry);
	        }
	        else {
	        	//There is only one row in the ELC Alarms table
	    		request.setAttribute("ELC_TOTALS", entry);
	        }
	        i++;
	    }
		//Set the attributes and dispatch 
		request.setAttribute("NVM_TOTALS", nvm_totals);
		request.setAttribute("HSA_TOTALS", hsa_totals);
		request.setAttribute("WIRING_TOTALS", wiring_totals);
		request.setAttribute("PHASE_TOTALS", phase_totals);
		
		request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
	}

	
	/**
	 * Generates the list of meters corresponding to the respective flag, sets list as a request
	 * attribute, then calls aggregateMeters(). If no meters can be found with the flag type the
	 * attribute is set to "No Results Found." 
	 * @param request HTTPRequest object
	 * @param response HTTP response object
	 * @throws Exception Issue with DataSource
	 */
	private void listMeters(HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
		List<Meter> meters = new ArrayList<>();
		String flag = request.getParameter("flag_id");	//read flag from dashboard.jsp
		meters = dashboardDbUtil.getMeters(flag);	//retrieve list of meters for the specified flag
		
		//Add meter list to the request unless no meters found
		if(meters.isEmpty()) {
			request.setAttribute("NO_RESULTS", "No Results Found");
		}
		else {
			request.setAttribute("FLAGGED_METERS", meters);
		}
		
		aggregateMeters(request, response);	//run meter aggregation
	}
	
	/**
	 * Calls the dashboardDbUtil to query for the specified meter and then runs aggregateMeters().
	 * @param request HTTP request object
	 * @param response HTTP response object
	 * @throws Exception Issue with DataSource
	 */
	private void searchMeter(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		String searchID = request.getParameter("SearchId");		//retrieve meter ID
		Meter meter = dashboardDbUtil.findMeter(searchID);		//call dashboardDbUtil to query for meter
		
		//If the meter is found set it as a request attribute otherwise set the NO_RESULTS attribute
		if(meter == null) {
			request.setAttribute("NO_RESULTS", "No Results Found");
		}
		else {
			request.setAttribute("FOUND_METER", meter);
		}
		
		aggregateMeters(request, response);
	}

	/**
	 * Calls doGet method.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);	
	}

}
