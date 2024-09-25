import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//create compile and run scripts and main function
public class MySQLConnect {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/employees"; // Replace with your DB name
        String username = "root"; // Replace with your MySQL username
        String password = "admin"; // Replace with your MySQL password

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
