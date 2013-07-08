package test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBUtils;

public class HelloWorldServlet extends HttpServlet {

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
		// TODO Auto-generated method stub
		resp.setContentType("text/plain");
		req.setCharacterEncoding("GBK");
		resp.getWriter().println("HelloWorldServlet!");
		try { 
		    String responseString=DBUtils.excuteSQL();
		    if(responseString==null||responseString.equals("")){
		    	responseString="Error access to database";
		    }
		    PrintWriter out =resp.getWriter();
		    resp.getWriter().println("Hello 222sjl World!"+responseString);
		    out.flush(); 
		    out.close(); 
		   } catch (Exception e) { 	
		    e.printStackTrace(); 
		   } 
	}
		
		
			
	}


