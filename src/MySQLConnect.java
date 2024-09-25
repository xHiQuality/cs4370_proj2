
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnect {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/employees"; // Replace with your DB name
        String username = "root"; // Replace with your MySQL username
        String password = "Sm25229!"; // Replace with your MySQL password

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            System.out.println("Connection successful!");
            try (Statement statement=connection.createStatement()) {
                System.out.println("Statement successful!");
                try (ResultSet resultSet = statement.executeQuery("Select * from Departments")) {
                    System.out.println("ResultSet successful!");
                    while (resultSet.next()) {
                        System.out.println("Found a Row!"+resultSet.getRow());
                        String dept_no =resultSet.getString(1);
                        String dept_name =resultSet.getString(2);
                        System.out.println(dept_no + "\t" + dept_name);
                    }
                }
            }
            try (PreparedStatement preparedStatement=connection.prepareStatement("select * from departments where dept_no = ?")) {
                System.out.println("preparedStatement successful!");
                preparedStatement.setString(1, "d005");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("ResultSet successful!");
                    while (resultSet.next()) {
                        System.out.println("Found a Row!"+resultSet.getRow());
                        String dept_no =resultSet.getString(1);
                        String dept_name =resultSet.getString(2);
                        System.out.println(dept_no + "\t" + dept_name);
                    }
                }
                preparedStatement.setString(1, "d003");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("ResultSet successful!");
                    while (resultSet.next()) {
                        System.out.println("Found a Row!"+resultSet.getRow());
                        String dept_no =resultSet.getString(1);
                        String dept_name =resultSet.getString(2);
                        System.out.println(dept_no + "\t" + dept_name);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
