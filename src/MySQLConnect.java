import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConnect {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/employees"; // Replace with your DB name
        String username = "root"; // Replace with your MySQL username
        String password = "bigT3CHZ0N3"; // Replace with your MySQL password

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            
            System.out.println("Connection successful!");


            String query3 = "SELECT DISTINCT DE.dept_no, dept_name, AVG(salary), COUNT(CASE WHEN birth_date < '1950-01-01' and birth_date > '1939-12-31' then 1 END) as 'Born in 40s', COUNT(CASE WHEN birth_date < '1960-01-01' and birth_date > '1949-12-31' then 1 END) as 'Born in 50s', COUNT(CASE WHEN birth_date < '1970-01-01' and birth_date > '1959-12-31' then 1 END) as 'Born in 60s', COUNT(CASE WHEN birth_date < '1980-01-01' and birth_date > '1969-12-31' then 1 END) as 'Born in 70s' FROM  DEPT_EMP DE JOIN SALARIES S ON DE.emp_no = S.emp_no JOIN EMPLOYEES E ON DE.emp_no = E.emp_no JOIN DEPARTMENTS D ON DE.dept_no = D.dept_no GROUP BY DE.dept_no ORDER BY AVG(salary) DESC";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query3);
            while (resultSet.next()) {
                String dept_no = resultSet.getString("dept_no");
                String dept_name = resultSet.getString("dept_name");
                String avg_salary = resultSet.getString("AVG(salary)");
                String bornIn40s = resultSet.getString("Born in 40s");
                String bornIn50s = resultSet.getString("Born in 50s");
                String bornIn60s = resultSet.getString("Born in 60s");
                String bornIn70s = resultSet.getString("Born in 70s");


                System.out.println("dept_no: " + dept_no + ", dept_name: " + dept_name + ", AVG(salary): " + avg_salary + ", Born in 40s: " + bornIn40s + ", Born in 50s: " + bornIn50s + ", Born in 60s: " + bornIn60s + ", Born in 70s: " + bornIn70s);

            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
