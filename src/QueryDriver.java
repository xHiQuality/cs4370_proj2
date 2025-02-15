import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
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
        
                while(true) {
                
                System.out.println("\n\nSelect query to run: ");
                System.out.println("1. List department(s) with maximum ratio of average female salaries to average men salaries");
                System.out.println("2. List manager(s) who holds office for the longest duration. A person can be a manager for multiple departments at different time frames");
                System.out.println("3. For each department, list number of employees born in each decade and their average salaries");
                System.out.println("4. List employees, who are female, born before Jan 1, 1990, makes more than 80K annually and hold a manager postion");
                System.out.println("5. Find 1 degree of separation between 2 given employees E1 & E2");
                System.out.println("6. Find 2 degrees of separation between 2 given employees E1 & E2");
                System.out.println("0. QUIT\n\n");
                System.out.print("Enter # of query you would like to execute: ");
                int queryNum = -1;
                try {
                    queryNum = in.nextInt();
                } catch(InputMismatchException ime) {
                    System.out.println("Not a # or Not a valid int 0-6");
                }

                
                in.nextLine();
                    //in.nextLine();
                    if (queryNum == 0) {
                       break;
                    } else if(queryNum == 1) {
                        System.out.println();
                        executeQuery1(jdbcUrl,username,password);
                    } else if (queryNum == 2) {
                        System.out.println();
                        executeQuery2(jdbcUrl,username,password);
                    } else if (queryNum == 3) {
                        System.out.println();
                        executeQuery3(jdbcUrl,username,password);
                    } else if (queryNum == 4) {
                        System.out.println();
                        executeQuery4(jdbcUrl,username,password);
                    } else if (queryNum == 5) {
                        
                                while (true) {
                                System.out.print("\nEnter Employee 1 Full Name: "); // test case Sumant Peac, same name test case Aimee Brookner multiple ID

                                String[] E1Name = in.nextLine().split(" ");
                                if (E1Name.length < 2) System.out.println("Please Enter First and Last Name");
                                else {
                                        String E1FN = E1Name[0];
                                        String E1LN = E1Name[1];
                                    
                                    int E1ID = searchByName(E1FN, E1LN, jdbcUrl, username, password,in);
                                    if (E1ID > 1) {
                                        System.out.print("\nEnter Employee 2 Full Name: "); //test case Bartek Lieblein
                                        String[] E2Name = in.nextLine().split(" ");
                                        if (E2Name.length < 2) System.out.println("Please Enter First and Last Name");
                                        else {
                                            String E2FN = E2Name[0];
                                            String E2LN = E2Name[1];

                                            int E2ID = searchByName(E2FN, E2LN, jdbcUrl, username, password,in);

                                            if (E1ID == E2ID) {
                                                System.out.println("Same employee. Enter 2 different IDs.");
                                            } else {
                                                OneDegree(E1ID, E2ID, jdbcUrl, username, password);
                                                break; 
                                            }   
                                        }
                                        
                                    }
                                }
                            }
                    } else if (queryNum == 6) {
                        while (true) {
                            System.out.print("\nEnter Employee 1 Full Name: "); // test case Guther Holburn

                            String[] E1Name = in.nextLine().split(" ");
                            if (E1Name.length < 2) System.out.println("Please Enter First and Last Name");
                            else {
                                    String E1FN = E1Name[0];
                                    String E1LN = E1Name[1];
                                
                                int E1ID = searchByName(E1FN, E1LN, jdbcUrl, username, password,in);
                                if (E1ID > 1) {
                                    System.out.print("\nEnter Employee 2 Full Name: "); //test case Anwar Krybus
                                    String[] E2Name = in.nextLine().split(" ");
                                    if (E2Name.length < 2) System.out.println("Please Enter First and Last Name");
                                    else {
                                        String E2FN = E2Name[0];
                                        String E2LN = E2Name[1];

                                        int E2ID = searchByName(E2FN, E2LN, jdbcUrl, username, password,in);

                                        if (E1ID == E2ID) {
                                            System.out.println("Same employee. Enter 2 different IDs.");
                                        } else {
                                            TwoDegrees(E1ID, E2ID, jdbcUrl, username, password);
                                            break; 
                                        }   
                                    }
                                }
                            }
                        }
                    }
                   
        }
        in.close();
    }

    /**
     * This searches the database by calling a simple select query, then adds all employees to list with
     * same name, if only one returns the id, if none returns -1
     * @author Cason Pittman
     * @param fn First name
     * @param ln Last name
     * @param url jdbc url
     * @param user username of connection
     * @param pass password of connection
     * @param in2  Input scanner from keyboard
     * @return emp_no of selected person
     */
    private static int searchByName(String fn, String ln, String url, String user, String pass, Scanner in2) {
       

        try(Connection connection = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT emp_no, first_name, last_name FROM employees WHERE first_name = ? AND last_Name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, fn);
            preparedStatement.setString(2, ln);

            ResultSet rs = preparedStatement.executeQuery();

            //add employees to list that match fistName lastName constraint
            List<Employee> employees = new ArrayList<>();
            while (rs.next()) {
                int emp_no = rs.getInt("emp_no");
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");

                employees.add(new Employee(emp_no, first, last));

            }

            if (employees.isEmpty()) {
                System.out.println("No employees found with first and last name combination.");
                
                return -1;
            }
            //these two check for employees in list
            if (employees.size() > 1) { //test case Aimee Brookner
                System.out.println("Please choose the employee desired by ID.");
                for (Employee e: employees) {
                    System.out.println("ID: " + e.getEmpNo() + " - Name: " + e.getFirstName() + " " + e.getLastName());
                }  
                //enter specific ID of name requested
                System.out.print("Enter ID: ");
                int eId = in2.nextInt();
                in2.nextLine();
                //if ID is in list assign ID, else return null
                Employee selected = employees.stream().filter(e -> e.getEmpNo() == eId).findFirst().orElse(null);
                
                if (selected == null) {
                    System.out.println("No ID found from list of employees");
                   
                    return -1;
                }
                //System.out.println(selected.getEmpNo() + " " + selected.getFirstName() + " " + selected.getLastName());
                return selected.getEmpNo();
            } else {
                Employee selected = employees.get(0);
                
                //System.out.println(selected.getEmpNo() + " " + selected.getFirstName() + " " + selected.getLastName());
                return selected.getEmpNo();
            }
        
        } catch (Exception e) {
            System.out.println("Incorrect ID or Names given. Try again");
        }
        return -1;
    }

    /**
     * @author Cason Pittman
     * Structure for retrieving Employees from database.
     */
    static class Employee {
        private int empNo;
        private String firstName;
        private String lastName;

        public Employee(int empNo, String firstName, String lastName) {
            this.empNo = empNo;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public int getEmpNo() {
            return empNo;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
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
    /** This function executes query 3 from project 2
    *   
    * @author Sean Malavet
    * @param url the database url
    * @param user the database username
    * @param pass the database password
    */
    private static void executeQuery3(String url, String user, String pass) {
        String query3 = "SELECT DISTINCT DE.dept_no, dept_name, AVG(salary), COUNT(CASE WHEN birth_date < '1950-01-01' and birth_date > '1939-12-31' then 1 END) as 'Born in 40s', COUNT(CASE WHEN birth_date < '1960-01-01' and birth_date > '1949-12-31' then 1 END) as 'Born in 50s', COUNT(CASE WHEN birth_date < '1970-01-01' and birth_date > '1959-12-31' then 1 END) as 'Born in 60s', COUNT(CASE WHEN birth_date < '1980-01-01' and birth_date > '1969-12-31' then 1 END) as 'Born in 70s' FROM  DEPT_EMP DE JOIN SALARIES S ON DE.emp_no = S.emp_no JOIN EMPLOYEES E ON DE.emp_no = E.emp_no JOIN DEPARTMENTS D ON DE.dept_no = D.dept_no GROUP BY DE.dept_no ORDER BY AVG(salary) DESC";
        try(Connection connection = DriverManager.getConnection(url, user, pass)) {
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
            e.getMessage();
        }

    }
    /** This function executes query 4 from project 2
    *   
    * @author Sean Malavet
    * @param url The url to the database
    * @param user the username for the database
    * @param password the password for the database
    *
    */
    private static void executeQuery4(String url, String user, String pass) {
        String query4 = "SELECT DISTINCT first_name, last_name, gender, birth_date, salary, title, T.from_date, T.to_date FROM DEPT_MANAGER DM JOIN TITLES T ON DM.emp_no = T.emp_no and T.title LIKE '%Manager%' and T.to_date LIKE '9999%' JOIN EMPLOYEES E ON DM.emp_no = E.emp_no and E.gender = 'F' and E.birth_date < '1990-01-01' JOIN SALARIES S ON DM.emp_no = S.emp_no and S.to_date = T.to_date and S.salary > 80000";

        try(Connection connection = DriverManager.getConnection(url, user, pass)) {
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
            e.getMessage();
        }
}
}
