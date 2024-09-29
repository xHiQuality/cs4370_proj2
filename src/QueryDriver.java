import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

        /**
         * CREATE THE CMD LINE LOGIC FOR INPUT AND QUERY SELECTION
         */

        //Turn these into keyboard input
        int empNoD1 = 13141;
        int empNoD2 = 13111;
        int E1 = 10009;
        int E2 = 11545;
        OneDegree(E1,E2,jdbcUrl,username,password);
        TwoDegrees(empNoD1, empNoD2, jdbcUrl, username, password); //Query 6
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
}
