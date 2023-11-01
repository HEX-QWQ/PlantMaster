package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.UserLoginDao;
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("UTF-8");
	    resp.setContentType("text/html;charset=UTF-8");
		 String id = (String)req.getParameter ( "id" );
	     String username = req.getParameter ( "username" );
	     String password = req.getParameter ( "password" );
	   
	     System.out.println("ID"+id);	
	     System.out.println("账号"+username);
		 System.out.println("密码"+password);
	
	     UserLoginDao dao = new UserLoginDao();
	     String result="fail";
	     if(dao.addUser(id,username, password)) {
				result="success";}
			PrintWriter out = resp.getWriter();  
	       		out.write(result); 
	}

}
