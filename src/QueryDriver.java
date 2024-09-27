import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class QueryDriver {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/employees"; // Replace with your DB name
        String username = "root"; // Replace with your MySQL username
        String password = "admin"; // Cason="admin"

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
