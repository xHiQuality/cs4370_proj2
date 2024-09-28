    import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

    public class MySQLConnect {
        public static void main(String[] args) {
            String url = "jdbc:mysql://localhost:3306/employees";  // Update with your DB URL
            String user = "root";  // Update with your DB username
            String password = "Sm25229!";  // Update with your DB password

            try {
                // Establish a database connection
                Connection connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connection successful!");

                String query1 = "select\n" +
                        "\tdept_no,\n" +
                        "    dept_name,\n" +
                        "    (avg_female_salary / avg_male_salary) as \"salary_ratio_female_to_male\"\n" +
                        "from\n" +
                        "\t(\n" +
                        "\t\tselect\n" +
                        "        d.dept_no,\n" +
                        "        d.dept_name,\n" +
                        "        (\n" +
                        "\t\t\tselect avg(salary)\n" +
                        "\t\t\tfrom employees.salaries s \n" +
                        "\t\t\t\tinner join employees.employees e on e.emp_no = s.emp_no\n" +
                        "\t\t\t\tinner join employees.dept_emp de on de.emp_no = e.emp_no and de.dept_no = d.dept_no\n" +
                        "\t\t\twhere e.gender = 'F'\n" +
                        "\t\t\tgroup by de.dept_no\n" +
                        "\t\t) AS \"avg_female_salary\",\n" +
                        "\t\t(\n" +
                        "\t\t\tselect avg(salary)\n" +
                        "\t\t\tfrom employees.salaries s\n" +
                        "\t\t\t\tinner join employees.employees e on e.emp_no = s.emp_no\n" +
                        "\t\t\t\tinner join employees.dept_emp de on de.emp_no = e.emp_no and de.dept_no = d.dept_no\n" +
                        "\t\t\twhere e.gender = 'M'\n" +
                        "\t\t\tgroup by de.dept_no\n" +
                        "\t\t) AS \"avg_male_salary\"\n" +
                        "\tfrom\n" +
                        "\t\temployees.departments d\n" +
                        ")t1\n" +
                        "\torder by\n" +
                        "\t\tsalary_ratio_female_to_male desc\n" +
                        "\tlimit 1";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                System.out.println("Query 1:");
                if (resultSet1.next()) {
                    String deptNo = resultSet1.getString("dept_no");
                    String deptName = resultSet1.getString("dept_name");
                    double salaryRatio = resultSet1.getDouble("salary_ratio_female_to_male");

                    System.out.println("Department No: " + deptNo);
                    System.out.println("Department Name: " + deptName);
                    System.out.println("Female to Male Salary Ratio: " + salaryRatio);
                }
                resultSet1.close();
                preparedStatement1.close();
                String query2 = "select\n" +
                        "\te.emp_no,\n" +
                        "    e.birth_date,\n" +
                        "    e.first_name,\n" +
                        "    e.last_name,\n" +
                        "    e.gender,\n" +
                        "    e.hire_date,\n" +
                        "    max(((case when m.to_date= '9999-01-01' THEN current_date() ELSE m.to_date END) - m.from_date)) as \"max_manager_tenure\"\n" +
                        "from\n" +
                        "\temployees.employees e\n" +
                        "\t\tinner join employees.dept_manager m on e.emp_no = m.emp_no\n" +
                        "group by\n" +
                        "\te.emp_no\n" +
                        "order by\n" +
                        "\tmax_manager_tenure desc";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                ResultSet resultSet2 = preparedStatement2.executeQuery();

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
                resultSet2.close();
                preparedStatement2.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}
