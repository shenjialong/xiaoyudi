package modle;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBUtils;

public class DataCommitServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		req.setCharacterEncoding("GBK");
		try { 
			String answer=req.getParameter("answer");
		    String responseString=DBUtils.dataCommit(answer);
		    if(responseString==null||responseString.equals("")){
		    	responseString="{'result':'fail'}";
		    }
		    
		    PrintWriter out =resp.getWriter();
		    resp.getWriter().println(responseString);
		    out.flush(); 
		    out.close(); 
		   } catch (Exception e) { 	
		    e.printStackTrace(); 
		   } 
	}
		
		
			
	}


