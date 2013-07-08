package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.*;

import com.baidu.bae.api.util.BaeEnv;

public class DBUtils {
	
	public static Connection getConnection() throws Exception{
		String host = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_SQL_IP);
		String port = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_SQL_PORT);
		String username = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_AK);
		String password = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_SK);
		String driverName = "com.mysql.jdbc.Driver";
		String dbUrl = "jdbc:mysql://";
		String serverName = host + ":" + port + "/";
		String databaseName = "ObrUyXrAUiFSfTGMThTX";
		String connName = dbUrl + serverName + databaseName;
	 
		Connection connection = null;
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(connName, username,
					password);
		} catch (Exception ex) {
			System.out.println("Throw a exception when attempting to get a connection");
		} finally {
			
		}
		return connection;
	}
	
	
public static String dataCommit(String jsonData) throws Exception{
		
		
	
//	jsonData="{'question1':1,'question2':1,'question3':1,'question4':1,'question5':1,'question6':1," +
//			"'question7':1,'question8':1,'question9':1,'question10':1,'question11':1,'question12':1," +
//			"'question13':1,'question14':1,'question15':1,'question16':1,'question17':1,'question18':1," +
//			"'question19':1,'question20':1,'question21':1,'question22':1,'question23':1,'userid':'2322'," +
//			"'terminal':'pc'}";	
	
	
		JSONObject json=JSONObject.fromObject(jsonData);
		String sql = "INSERT INTO screenanswer(question1,question2,question3,question4,question5,question6," +
				"question7,question8,question9,question10,question11,question12," +
				"question13,question14,question15,question16,question17,question18," +
				"question19,question20,question21,question22,question23,userid,terminal)VALUES ("
				+json.getInt("question1")+", "+json.getInt("question2")+", "+json.getInt("question3")+", "+
		json.getInt("question4")+","+json.getInt("question5")+","+json.getInt("question6")+","+json.getInt("question7")+","+json.getInt("question8")+"," +
				""+json.getInt("question9")+", "+json.getInt("question10")+", "+json.getInt("question11")+", "+json.getInt("question12")+", "+
		json.getInt("question13")+", "+json.getInt("question14")+", "+json.getInt("question15")+", "+json.getInt("question16")+", "+json.getInt("question17")+", "+json.getInt("question18")+"," +
				""+json.getInt("question19")+", "+json.getInt("question20")+", "+json.getInt("question21")+", "+json.getInt("question22")+", "+
		json.getInt("question23")+", '"+json.getString("userid")+"', '"+json.getString("terminal")+"')" ;
//		,"+json.getInt("id")+"
		
		
		Connection connection = getConnection();
		Statement stmt = null;
		int result;
		try {
			stmt = connection.createStatement();
			result = stmt.executeUpdate(sql);
			JSONObject jsonresult;
			jsonresult=new JSONObject();
			if(result>0){
				jsonresult.put("result", "success");
			}else{
				jsonresult.put("result", "fail");
			}
			return jsonresult.toString();
		} catch (Exception ex) {
	      throw ex;
		} finally {	
			try {
				if (connection != null) {
					connection.close();
				}
				} catch (SQLException e) {
	 				throw e;
			}
		}
	}
	public static String excuteSQL() throws Exception{
	 String sql="SELECT * FROM screenanswer limit 0,2 ";
		Connection connection = getConnection() ;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			JSONArray jsonarray=new JSONArray();
			JSONObject jsonobj;
			while(rs.next()){
				jsonobj=new JSONObject();
				jsonobj.put("question1", rs.getInt("question1"));
				jsonobj.put("question2", rs.getInt("question2"));
				jsonobj.put("question3", rs.getInt("question3"));
				jsonobj.put("question4", rs.getInt("question4"));
				jsonobj.put("question5", rs.getInt("question5"));
				jsonobj.put("question6", rs.getInt("question6"));
				jsonobj.put("question7", rs.getInt("question7"));
				jsonobj.put("question8", rs.getInt("question8"));
				jsonobj.put("question9", rs.getInt("question9"));
				jsonobj.put("question10", rs.getInt("question10"));
				jsonobj.put("question11", rs.getInt("question11"));
				jsonobj.put("question12", rs.getInt("question12"));
				jsonobj.put("question13", rs.getInt("question13"));
				jsonobj.put("question14", rs.getInt("question14"));
				jsonobj.put("question15", rs.getInt("question15"));
				jsonobj.put("question16", rs.getInt("question16"));
				jsonobj.put("question17", rs.getInt("question17"));
				jsonobj.put("question18", rs.getInt("question18"));
				jsonobj.put("question19", rs.getInt("question19"));
				jsonobj.put("question20", rs.getInt("question20"));
				jsonobj.put("question21", rs.getInt("question21"));
				jsonobj.put("question22", rs.getInt("question22"));
				jsonobj.put("question23", rs.getInt("question23"));
				jsonobj.put("id", rs.getInt("id"));
				jsonobj.put("userid", rs.getString("userid"));
				jsonobj.put("terminal", rs.getString("terminal"));
				jsonarray.add(jsonobj);
			}
			System.out.println(jsonarray.toString());
			return jsonarray.toString();
		} catch (Exception ex) {
	      throw ex;
		}  finally {
			try {
				if (connection != null) {
					connection.close();
				}
				} catch (SQLException e) {
	 				throw e;
			}
		}
		
		
		
	}
	
	
	
	

}
