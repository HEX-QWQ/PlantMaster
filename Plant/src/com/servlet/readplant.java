package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import utils.DButil;

/**
 * Servlet implementation class searchbook_name
 */
@WebServlet("/findplant")
public class readplant extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public readplant() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");
		String query;
		Connection connection =DButil.getConnection();
		// 连接数据库
        		query="select * from flora";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                // 创建 JSON 对象和数组
                ObjectMapper objectMapper = new ObjectMapper();
                ArrayNode jsonArray = objectMapper.createArrayNode();
                // 将数据库结果集转换为 JSON
                while (resultSet.next()) {
                    ObjectNode jsonObject = objectMapper.createObjectNode();
                    jsonObject.put("pname", resultSet.getString("pname"));//图书编号
                    jsonObject.put("type", resultSet.getString("type"));//图书名称
                    jsonObject.put("content", resultSet.getString("content"));//图书作者
                    jsonObject.put("env", resultSet.getString("env"));//图书出版社
                    jsonObject.put("eng", resultSet.getString("eng"));//图书价格
                    // 读取并处理图书图片数据
                    Blob imageBlob = resultSet.getBlob("img");
                    byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                    jsonObject.put("imgBase64", base64Image);
                    
                    jsonArray.add(jsonObject);
                }
                
                // 设置响应的内容类型为 JSON
                response.setContentType("application/json");
                // 将 JSON 数组作为响应输出
                PrintWriter out = response.getWriter();
                out.print(jsonArray.toString());
            } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
