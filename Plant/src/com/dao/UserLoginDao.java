package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utils.DButil;

public class UserLoginDao {
	
	public boolean checkLogin(String username, String password){
		boolean flag = false;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		/*
		创建Connnection, Statement, ResultSet对象
		调用验证登录的方法
		*/
		System.out.println("name"+username+"password"+password);
		String sql ="select * from user where account= '"+ username +"'";
		conn = DButil.getConnection();
		try {
		    st = conn.createStatement();
		    rs = st.executeQuery(sql);
		    while (rs.next()){
		        if(rs.getString("pwd").equals(password)){
		            flag = true;
		        }
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		} finally {
		    DButil.close(rs, st, conn);
		}
		return flag;
    }
	 public boolean addUser(String id,String username,String password){
		 boolean flag = false; 
		 Connection conn = null;
		  PreparedStatement pst = null;
		  ResultSet res = null;
	      try {
	          conn = DButil.getConnection();
	          String sql = "insert into user values (?,?,?)";
	          pst =conn.prepareStatement ( sql );
	         pst.setObject ( 1,id );
	         pst.setObject ( 2,username);
	         pst.setObject ( 3,password);
	        int i = pst.executeUpdate ( );
	        if (i>0){
	              flag=true;
	         }
	      }catch (Exception e){
	          e.printStackTrace ();
	      }finally {
	    	  DButil.close( res,pst,conn );
	      }
	      return flag;
	  }
	     
}

