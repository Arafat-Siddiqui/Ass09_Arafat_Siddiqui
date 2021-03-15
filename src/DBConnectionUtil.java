import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {
	static Connection con = null;
	private DBConnectionUtil() {
		super();
	}
	static String driverClass = "com.mysql.cj.jdbc.Driver";
	static String url = "jdbc:mysql://localhost/moviesdb";
	static String username = "root";
	static String password = "Khushboo@123";
	
	public static Connection getConnection() {
		try {
			Class.forName(driverClass);
			con = DriverManager.getConnection(url,username,password);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	public static void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
