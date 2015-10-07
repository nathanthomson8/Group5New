package project.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

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
		System.out.println("Strategy Servlet Entered");
		String company = request.getParameter("company");
		String chosenStrat = request.getParameter("strategy");
		System.out.println("Company name = " + company);
		System.out.println("Chosen Strat = " + chosenStrat);
		try {
			if(chosenStrat.equals("TwoMovingAvg")&&(Dal.getStrategyByCompany(company)!=1)){		
				TwoMovingAvg newAvg = new TwoMovingAvg();
				newAvg.setSymbol(company);
				newAvg.run();
				
			}
			// At the Minute Only have 1 Strategy, will add in else if for other strats
			// whenever they are implemented
			else{
				//implement new strat
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("Index.jsp");
		rd.forward(request, response);
	}	
}
