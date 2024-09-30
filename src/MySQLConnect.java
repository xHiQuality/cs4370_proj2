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
            // executeQuery3(connection);  
            executeQuery4(connection);    

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /* This function executes query 3 from project 2
    *   
    * @author Sean Malavet
    * @param A valid JDBC Connection to the employees database to use to execute the query
    *
    */
    private static void executeQuery3(Connection connection) throws SQLException {
            String query3 = "SELECT DISTINCT DE.dept_no, dept_name, AVG(salary), COUNT(CASE WHEN birth_date < '1950-01-01' and birth_date > '1939-12-31' then 1 END) as 'Born in 40s', COUNT(CASE WHEN birth_date < '1960-01-01' and birth_date > '1949-12-31' then 1 END) as 'Born in 50s', COUNT(CASE WHEN birth_date < '1970-01-01' and birth_date > '1959-12-31' then 1 END) as 'Born in 60s', COUNT(CASE WHEN birth_date < '1980-01-01' and birth_date > '1969-12-31' then 1 END) as 'Born in 70s' FROM  DEPT_EMP DE JOIN SALARIES S ON DE.emp_no = S.emp_no JOIN EMPLOYEES E ON DE.emp_no = E.emp_no JOIN DEPARTMENTS D ON DE.dept_no = D.dept_no GROUP BY DE.dept_no ORDER BY AVG(salary) DESC";
            try {
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

                    System.out.println();
                    
                    System.out.print("dept_no: " + dept_no);
                    System.out.printf(", dept_name: %-22s", dept_name);
                    System.out.print("AVG(salary): " + avg_salary + ", Born in 40s: " + bornIn40s + ", Born in 50s: " + bornIn50s + ", Born in 60s: " + bornIn60s + ", Born in 70s: " + bornIn70s);

                }
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    /* This function executes query 4 from project 2
    *   
    * @author Sean Malavet
    * @param A valid JDBC Connection to the employees database to use to execute the query
    *
    */
    private static void executeQuery4(Connection connection) throws SQLException {
        String query4 = "SELECT DISTINCT first_name, last_name, gender, birth_date, salary, title, T.from_date, T.to_date FROM DEPT_MANAGER DM JOIN TITLES T ON DM.emp_no = T.emp_no and T.title LIKE '%Manager%' and T.to_date LIKE '9999%' JOIN EMPLOYEES E ON DM.emp_no = E.emp_no and E.gender = 'F' and E.birth_date < '1990-01-01' JOIN SALARIES S ON DM.emp_no = S.emp_no and S.to_date = T.to_date and S.salary > 80000";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query4);

            while (resultSet.next()) {
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String gender = resultSet.getString("gender");
                String birth_date = resultSet.getString("birth_date");
                String salary = resultSet.getString("salary");
                String title = resultSet.getString("title");
                String from_date = resultSet.getString("from_date");
                String to_date = resultSet.getString("to_date");


                System.out.println();
                System.out.printf("first_name: %-15s ", first_name);
                System.out.printf("last_name: %-15s ", last_name);
                System.out.printf("gender: %-2s ", gender);
                System.out.printf("birth_date: %-12s ", birth_date);
                System.out.printf("salary: %-8s ", salary);
                System.out.printf("title: %-10s ", title);
                System.out.printf("from_date: %-12s ", from_date);
                System.out.printf("to_date: %-12s ", to_date);

            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }

}

}
