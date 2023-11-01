package com.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.UserLoginDao;

import java.io.PrintWriter;
@WebServlet("/login1")
public class LoginServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username=req.getParameter("username");
		String password=req.getParameter("password");
		System.out.println("账号"+username);
		System.out.println("密码"+password);
		String result="fail";
		UserLoginDao dao = new UserLoginDao();
		if(dao.checkLogin(username, password)) {
			result="success";}
		PrintWriter out = resp.getWriter();  
       		out.write(result); 
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
