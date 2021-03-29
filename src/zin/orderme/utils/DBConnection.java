package zin.orderme.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
	
	private final static String DRIVER = "com.mysql.jdbc.Driver";
	private final static String URL = "jdbc:mysql://localhost:3306/dbMember";
	private final static String USER = "root";
	private final static String PASSWORD = "apple";
	private String strSQL = null;
	
	public enum ConnectType{
		Query, Update, Table
	}
	
	public ResultSet getDBContents(ConnectType type, String sql){
		Connection conn;
		Statement stmt;
		ResultSet rs = null;
		strSQL = sql;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.createStatement();
			switch(type) {
			case Query:
				rs = stmt.executeQuery(strSQL);
				break;
			default:
				stmt.executeUpdate(strSQL);
				break;
			}
		}catch(Exception e) {
			if(type != ConnectType.Table) System.out.println("execute error >>>"+e.getMessage());
			return rs;
		}
		return rs;
	}
}
