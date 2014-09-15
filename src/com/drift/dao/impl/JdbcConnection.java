package com.drift.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JdbcConnection {
	
	static DataSource ds = null;
	
	static {
		/*try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError("Initialize mysql jdbc driver error!");
		}

		//System.out.println(new File(".").getAbsolutePath());
		//System.out.println(new File("/").getAbsolutePath());
		//System.out.println(System.getProperty("user.dir"));
		//System.out.println(getClass().getResource("/"));

		Properties props = new Properties();
		//String path = getClass().getResource("/") + DB_PROPS_PATH;
		InputStream in = DAO.class.getClassLoader().getResourceAsStream(DB_PROPS_PATH);
		//System.out.println("path is: " + path);
		//File file = new File(path);
		try {
			//props.load(new FileInputStream(file));
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbUrl = props.getProperty("dbUrl");
		dbUser = props.getProperty("dbUser");
		dbPwd = props.getProperty("dbPwd");
		//System.out.println("dbUrl: " + dbUrl + "; dbUser: " + dbUser + "; dbPwd: " + dbPwd);
		*/
		Context ctx;
		try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("No Context");		
		}			
		
		try {
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DateDB");
		} catch (NamingException e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Data source look up failure!");	
		}		
	}
	
	static public Connection getConnection()throws Exception{
		//return java.sql.DriverManager.getConnection(dbUrl,dbUser,dbPwd);
		return ds.getConnection();
	}

	static public void closeConnection(Connection con){
		try{
			if(con != null) con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	static public void closePrepStmt(PreparedStatement prepStmt){
		try{
			if(prepStmt != null) prepStmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	static public void closeResultSet(ResultSet rs){
		try{
			if(rs != null) rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
