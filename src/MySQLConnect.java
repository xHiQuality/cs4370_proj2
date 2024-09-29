    import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

    public class MySQLConnect {
    /**
     * @author Samantha Macaluso
     */
        public static void main(String[] args) {
            String url = "jdbc:mysql://localhost:3306/employees";  // Update with your DB URL
            String user = "root";  // Update with your DB username
            String password = "Sm25229!";  // Update with your DB password

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connection successful!");
                executeQuery1(connection);
                executeQuery2(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
/**
     *  This method prints out a table that corresponds to the query listed below.
     *  This method corresponds with Q1 on the project assignment
     * 
     * @author Samantha Macaluso
     * @param url jdbcUrl for connection
     * @param user MySQL database user
     * @param pass MySQL databse password
     */
        private static void executeQuery1(Connection connection) throws SQLException {
            String query1 = "SELECT\n" +
                    "\tdept_no,\n" +
                    "    dept_name,\n" +
                    "    (avg_female_salary / avg_male_salary) AS \"salary_ratio_female_to_male\"\n" +
                    "FROM\n" +
                    "\t(\n" +
                    "\t\tSELECT\n" +
                    "        d.dept_no,\n" +
                    "        d.dept_name,\n" +
                    "        (\n" +
                    "\t\t\tSELECT AVG(salary)\n" +
                    "\t\t\tFROM employees.salaries s \n" +
                    "\t\t\t\tINNER JOIN employees.employees e ON e.emp_no = s.emp_no\n" +
                    "\t\t\t\tINNER JOIN employees.dept_emp de ON de.emp_no = e.emp_no AND de.dept_no = d.dept_no\n" +
                    "\t\t\tWHERE e.gender = 'F'\n" +
                    "\t\t\tGROUP BY de.dept_no\n" +
                    "\t\t) AS \"avg_female_salary\",\n" +
                    "\t\t(\n" +
                    "\t\t\tSELECT AVG(salary)\n" +
                    "\t\t\tFROM employees.salaries s\n" +
                    "\t\t\t\tINNER JOIN employees.employees e ON e.emp_no = s.emp_no\n" +
                    "\t\t\t\tINNER JOIN employees.dept_emp de ON de.emp_no = e.emp_no AND de.dept_no = d.dept_no\n" +
                    "\t\t\tWHERE e.gender = 'M'\n" +
                    "\t\t\tGROUP BY de.dept_no\n" +
                    "\t\t) AS \"avg_male_salary\"\n" +
                    "\tFROM\n" +
                    "\t\temployees.departments d\n" +
                    ") t1\n" +
                    "\tORDER BY\n" +
                    "\t\tsalary_ratio_female_to_male DESC\n" +
                    "\tLIMIT 1";
            
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                        ResultSet resultSet1 = preparedStatement1.executeQuery()) {

                System.out.println("Query 1:");
                if (resultSet1.next()) {
                    String deptNo = resultSet1.getString("dept_no");
                    String deptName = resultSet1.getString("dept_name");
                    double salaryRatio = resultSet1.getDouble("salary_ratio_female_to_male");

                    System.out.println("Department No: " + deptNo);
                    System.out.println("Department Name: " + deptName);
                    System.out.println("Female to Male Salary Ratio: " + salaryRatio);
                }
            }
        }
        /*  This method prints out a table that corresponds to the query listed below.
        *  This method corresponds with Q2 on the project assignment
        * @author Samantha Macaluso
        * @param url jdbcUrl for connection
        * @param user MySQL database user
        * @param pass MySQL databse password
        */
        private static void executeQuery2(Connection connection) throws SQLException {
            String query2 = "SELECT\n" +
                    "\te.emp_no,\n" +
                    "    e.birth_date,\n" +
                    "    e.first_name,\n" +
                    "    e.last_name,\n" +
                    "    e.gender,\n" +
                    "    e.hire_date,\n" +
                    "    MAX(((CASE WHEN m.to_date = '9999-01-01' THEN CURRENT_DATE() ELSE m.to_date END) - m.from_date)) AS \"max_manager_tenure\"\n" +
                    "FROM\n" +
                    "\temployees.employees e\n" +
                    "\tINNER JOIN employees.dept_manager m ON e.emp_no = m.emp_no\n" +
                    "GROUP BY\n" +
                    "\te.emp_no\n" +
                    "ORDER BY\n" +
                    "\tmax_manager_tenure DESC";

            try (PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                    ResultSet resultSet2 = preparedStatement2.executeQuery()) {

                System.out.println("Query 2:");
                if (resultSet2.next()) {
                    int empNo = resultSet2.getInt("emp_no");
                    String birthDate = resultSet2.getString("birth_date");
                    String firstName = resultSet2.getString("first_name");
                    String lastName = resultSet2.getString("last_name");
                    String gender = resultSet2.getString("gender");
                    String hireDate = resultSet2.getString("hire_date");
                    int maxTenure = resultSet2.getInt("max_manager_tenure");

                    System.out.println("Employee No: " + empNo);
                    System.out.println("Birth Date: " + birthDate);
                    System.out.println("First Name: " + firstName);
                    System.out.println("Last Name: " + lastName);
                    System.out.println("Gender: " + gender);
                    System.out.println("Hire Date: " + hireDate);
                    System.out.println("Max Manager Tenure: " + maxTenure + " days");
                }
            }
        }
    }

