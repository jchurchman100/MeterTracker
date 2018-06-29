package metertrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import metertrack.DashboardDbUtil;

/**
 * Servlet implementation class MeterControllerServlet
 */
@WebServlet("/MeterControllerServlet")
public class MeterControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DashboardDbUtil dashboardDbUtil;
	
    @Resource(name="jdbc/ebdb")
	private DataSource dataSource;
    
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		//create out student db util and pass in the conn pool / datasource 
		try {
			dashboardDbUtil = new DashboardDbUtil(dataSource);
		}
		catch(Exception exc) {
			throw new ServletException(exc);
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//read the "command" parameter
			String theCommand = request.getParameter("command");
			//if command is missing then default to listing students
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
					
				default:
					aggregateMeters(request, response);
			}
			//aggregateMeters(request,response);
		}
		catch(Exception exc) {
			throw new ServletException(exc);
		}
	}

	private void listMeters(HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
		List<Meter> meters = new ArrayList<>();
		String tab = "";
		//read flag from jsp
		String flag = request.getParameter("flag_id");
		//get meter list from DB util
		meters = dashboardDbUtil.getMeters(flag);
		//add meter list to the request
		if(meters.isEmpty()) {
			request.setAttribute("NO_RESULTS", "No Results Found");
		}
		request.setAttribute("FLAGGED_METERS", meters);
		
		aggregateMeters(request, response);
	}
	
	private void aggregateMeters(HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		//Create integer list
		List<Pair<String,Integer>> totals = new ArrayList<>();
		totals = dashboardDbUtil.getTotals(); //BREAK THIS UP
		int i = 0;
		
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
	        	//Set the attribute and dispatch 
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		
	}

}
