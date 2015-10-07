package project.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import data.access.Dal;

/**
 * Servlet implementation class UpdateStrategyServlet
 */
@WebServlet("/UpdateStrategyServlet")
public class UpdateStrategyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(UpdateStrategyServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateStrategyServlet() {
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
		
		if (company!=null) {			
			try {
				Dal.updateStrategy(company, 0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("SQLException" + e.getMessage());
				e.printStackTrace();
			}
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("Index.jsp");
		rd.forward(request, response);
	}

}
