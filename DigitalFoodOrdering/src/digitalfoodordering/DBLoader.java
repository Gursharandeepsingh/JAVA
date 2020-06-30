package digitalfoodordering;
import java.sql.*;
public class DBLoader {
    static ResultSet executeQuery(String sqlstatement)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("DRIVER ADDED SUCCESSFULLY");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/food","root","");
            System.out.println("CONNECTION BUILT");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            System.out.println("STATEMENT CREATED");
            ResultSet rs = stmt.executeQuery(sqlstatement);
            System.out.println("RESULTSET CREATED");
            return rs;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
