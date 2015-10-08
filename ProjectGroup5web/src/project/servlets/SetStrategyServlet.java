package project.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

import project.businesslogic.TwoMovingAvg;
import data.access.Dal;
import data.dataObjects.StockObject;

/**
 * Servlet implementation class SetStrategyServlet
 */
@WebServlet("/SetStrategyServlet")
public class SetStrategyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(SetStrategyServlet.class);   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetStrategyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		List<String> companies = new ArrayList<>();
		try {
			companies = Dal.getAllCompaniesString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("Error getting Company List"+e.getMessage());
		}
		HashMap<String, TwoMovingAvg> companyThreads = new HashMap<>();
		for (String s : companies) {
			companyThreads.put(s, new TwoMovingAvg(s));
			System.out.println(s);
		}
		//yf.setMaverage(10);
		//yf.start();
		ServletContext ctx = getServletContext();
		ctx.setAttribute("HashMap", companyThreads);
	}
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		System.out.println("Strategy Servlet Entered");
		String company = request.getParameter("company");
		//String chosenStrat = request.getParameter("strategy");
		System.out.println("Company name = " + company);
		//System.out.println("Chosen Strat = " + chosenStrat);
		
		ServletContext ctx = getServletContext();
		
		HashMap<String, TwoMovingAvg> tradeThreads = (HashMap<String, TwoMovingAvg>) ctx.getAttribute("HashMap");

		TwoMovingAvg twoMovingAvg = tradeThreads.get(company);
		
			if(request.getParameter("strategy").equals("stop")){
				twoMovingAvg.suspend();
			}
			else{
				if(twoMovingAvg.isRunning()){
					twoMovingAvg.resume();
				}
				else{					
					twoMovingAvg.start();
				}

			}
		
			//out.print("");
		RequestDispatcher rd = request.getRequestDispatcher("Index.jsp");
		rd.forward(request, response);
	}	
}
