import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QueryDriver {

    /**
     * @author Cason Pittman
     */
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/employees"; // Replace with your DB name
        String username = "root"; // Cason = "root"
        String password = "admin"; // Cason = "admin"

        Scanner in = new Scanner(System.in);
        
            while (true) {
                System.out.println("\n\nSelect query to run: ");
                System.out.println("1. List department(s) with maximum ratio of average female salaries to average men salaries");
                System.out.println("2. List manager(s) who holds office for the longest duration. A person can be a manager for multiple departments at different time frames");
                System.out.println("3. For each department, list number of employees born in each decade and their average salaries");
                System.out.println("4. List employees, who are female, born before Jan 1, 1990, makes more than 80K annually and hold a manager postion");
                System.out.println("5. Find 1 degree of separation between 2 given employees E1 & E2");
                System.out.println("6. Find 2 degrees of separation between 2 given employees E1 & E2");
                System.out.println("0. QUIT\n\n");
                System.out.print("Enter # of query you would like to execute: ");
                try {
                    int queryNum = in.nextInt();
                    if (queryNum == 0) {
                        break;
                    } else if(queryNum == 1) {
                        System.out.println();
                        executeQuery1(jdbcUrl,username,password);
                    } else if (queryNum == 2) {
                        System.out.println();
                        executeQuery2(jdbcUrl,username,password);
                    } else if (queryNum == 3) {
                        System.out.println("\nSean query 3");
                    } else if (queryNum == 4) {
                        System.out.println("\nSean query 4");
                    } else if (queryNum == 5) {
                        Boolean askAgain = true;
                        while(askAgain) {
                            try {
                                System.out.print("\nEnter Employee 1 ID: "); //test case 10009
                                int E1 = in.nextInt();
                                System.out.print("\nEnter Employee 2 ID: "); //test case 11545
                                int E2 = in.nextInt();
                                System.out.println();
                                if (E1 == E2) {
                                    System.out.println("Same employee. Enter 2 different ID's");
                                } else {
                                    OneDegree(E1, E2, jdbcUrl, username, password);
                                    askAgain = false;
                                }
                                }catch(Exception e) {
                                    System.out.println("Please enter valid ID e.g.(10009,11545,etc.)");
                                    in.next();
                                }
                        }
                    } else if (queryNum == 6) {
                        Boolean askAgain = true;
                        while(askAgain) {
                            try {
                                System.out.print("\nEnter Employee 1 ID: "); //test case 13141
                                int E1 = in.nextInt();
                                System.out.print("\nEnter Employee 2 ID: "); //test case 13111
                                int E2 = in.nextInt();
                                System.out.println();
                                if (E1 == E2) {
                                    System.out.println("Same employee. Enter 2 different ID's");
                                } else {
                                    TwoDegrees(E1, E2, jdbcUrl, username, password);
                                    askAgain = false;
                                }
                                
                                }catch(Exception e) {
                                    System.out.println("Please enter valid ID e.g.(13141,13111,etc.)");
                                    in.next();
                                }
                        }
                    }
                } catch(Exception e) {
                    System.out.println("\nIncorrect Usage, please enter # e.g(1,2,3,etc.)");
                    in.next();
                }
        }
        in.close();
    }

    /**
     *  This method prints out a table that corresponds to the query listed below.
     *  This method corresponds with Q6 on the homework
     * 
     * @author Cason Pittman
     * @param empNoD1 Employee 1 id for comparison
     * @param empNoD2 Employee 2 id for comparison
     * @param url jdbcUrl for connection
     * @param user MySQL database user
     * @param pass MySQL databse password
     */
    private static void TwoDegrees(int empNoD1, int empNoD2, String url, String user, String pass) {
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String query =
         "SELECT " +
        "    E1.emp_no AS E1, " +
        "    E1.department AS D1, " +
        "    E1.emp_no_colleague AS E3, " +
        "    E2.department AS D2, " +
        "    E2.emp_no AS E2 " +
        "FROM ( " +
        "    SELECT " +
        "        D1.emp_no AS emp_no, " +
        "        D3.emp_no AS emp_no_colleague, " +
        "        D1.dept_no AS department " +
        "    FROM " +
        "        dept_emp D1 " +
        "    JOIN " +
        "        dept_emp D3 " +
        "    ON D1.dept_no = D3.dept_no " +
        "    AND D1.emp_no <> D3.emp_no " +
        "    AND D1.from_date <= D3.to_date " +
        "    AND D1.to_date >= D3.from_date " +
        "    WHERE " +
        "        D1.emp_no = ? " +
        ") AS E1 " +
        "JOIN ( " +
        "    SELECT " +
        "        D2.emp_no AS emp_no, " +
        "        D3.emp_no AS emp_no_colleague, " +
        "        D2.dept_no AS department " +
        "    FROM " +
        "        dept_emp D2 " +
        "    JOIN " +
        "        dept_emp D3 " +
        "    ON D2.dept_no = D3.dept_no " +
        "    AND D2.emp_no <> D3.emp_no " +
        "    AND D2.from_date <= D3.to_date " +
        "    AND D2.to_date >= D3.from_date " +
        "    WHERE " +
        "        D2.emp_no = ? " +
        ") AS E2 " +
        "ON " +
        "    E1.emp_no_colleague = E2.emp_no_colleague " +
        "WHERE " +
        "    E1.emp_no_colleague <> ? " +
        "    AND E2.emp_no_colleague <> ? " +
        "    AND E1.department <> E2.department " +
        "LIMIT 100";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Set the variable values in the PreparedStatement
            preparedStatement.setInt(1, empNoD1); 
            preparedStatement.setInt(2, empNoD2); 
            preparedStatement.setInt(3, empNoD1); 
            preparedStatement.setInt(4, empNoD2); 

            ResultSet resultSet = preparedStatement.executeQuery();
            
            System.out.println("Query Result: \nE1 ---  D1 --- E3 ---- D2 ---- E2");
            while (resultSet.next()) {
                int E1 = resultSet.getInt("E1");
                String D1 = resultSet.getString("D1");
                int E3 = resultSet.getInt("E3");
                String D2 = resultSet.getString("D2");
                int E2 = resultSet.getInt("E2");

                // Print or process the results
                System.out.printf("%d   %s   %d   %s   %d%n", 
                                  E1, D1, E3, D2, E2);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method prints out a table corresponding to the query below.
     * This method is for Query 5 on the HW
     * @author Cason Pittman
     * @param empNoD1 Employee 1 id for comparison
     * @param empNoD2 Employee 2 id for comparison
     * @param url jdbcUrl for connection
     * @param user MySQL database user
     * @param pass MySQL databse password
     */
    private static void OneDegree(int empNo1, int empNo2, String url, String user, String pass) {
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT "
                    + "D1.emp_no AS emp_no_1, "
                    + "D1.dept_no AS department, "
                    + "D2.emp_no AS emp_no_2 "
                    + "FROM dept_emp D1 "
                    + "JOIN dept_emp D2 "
                    + "ON D1.dept_no = D2.dept_no "
                    + "AND D1.emp_no <> D2.emp_no "
                    + "AND D1.from_date <= D2.to_date "
                    + "AND D1.to_date >= D2.from_date "
                    + "WHERE D1.emp_no = ? "
                    + "AND D2.emp_no = ?";

            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                //set vars
                preparedStatement.setInt(1, empNo1);
                preparedStatement.setInt(2, empNo2);

                
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    
                    System.out.println("Query Result: ");
                    while (resultSet.next()) {
                        int E1 = resultSet.getInt("emp_no_1");
                        String D1 = resultSet.getString("department");
                        int E2 = resultSet.getInt("emp_no_2");

                        System.out.println(E1 + "-" + D1 + "-" + E2);
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
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
    private static void executeQuery1(String url, String user, String pass) {
        try(Connection connection = DriverManager.getConnection(url, user, pass)) {
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
        } catch (SQLException e) {
            e.getMessage();
        }
        
    }
    /*  This method prints out a table that corresponds to the query listed below.
    *  This method corresponds with Q2 on the project assignment
    * @author Samantha Macaluso
    * @param url jdbcUrl for connection
    * @param user MySQL database user
    * @param pass MySQL databse password
    */
    private static void executeQuery2(String url, String user, String pass) {
        try(Connection connection = DriverManager.getConnection(url, user, pass)) {
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
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
