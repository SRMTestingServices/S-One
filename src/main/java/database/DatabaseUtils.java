package database;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


/*
 * utils to handle the different database utils
 * 
 * @author Selva 
 */
public class DatabaseUtils {

    // Method to establish a connection to any database
    public static Connection getConnection(String url, String username, String password, String driverClass) throws SQLException {
        try {
            Class.forName(driverClass);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found", e);
        }
    }

    // Method to close the connection and other resources
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to execute a SELECT query and return results as a List of Maps (key-value pairs)
    public static List<Map<String, Object>> executeSelectQuery(String url, String username, String password, String driverClass, String query) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection(url, username, password, driverClass);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    row.put(columnName, columnValue);
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return resultList;
    }

    // Method to execute an INSERT, UPDATE, or DELETE query (DML operations)
    public static int executeUpdateQuery(String url, String username, String password, String driverClass, String query) {
        int rowsAffected = 0;
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection(url, username, password, driverClass);
            stmt = conn.createStatement();
            rowsAffected = stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        return rowsAffected;
    }

    // Method to execute a batch of INSERT, UPDATE, DELETE queries
    public static int[] executeBatchUpdate(String url, String username, String password, String driverClass, List<String> queries) {
        int[] updateCounts = null;
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection(url, username, password, driverClass);
            stmt = conn.createStatement();
            for (String query : queries) {
                stmt.addBatch(query);
            }
            updateCounts = stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        return updateCounts;
    }

    // Method to execute a PreparedStatement with parameters
    public static int executePreparedStatementUpdate(String url, String username, String password, String driverClass, String query, Object[] params) {
        int rowsAffected = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection(url, username, password, driverClass);
            pstmt = conn.prepareStatement(query);

            // Set the parameters in the PreparedStatement
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        return rowsAffected;
    }

    // Example method to fetch connection URL, username, password, and driver class
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "password";
        String driverClass = "com.mysql.cj.jdbc.Driver";

        String selectQuery = "SELECT * FROM users";
        List<Map<String, Object>> result = executeSelectQuery(url, username, password, driverClass, selectQuery);
        for (Map<String, Object> row : result) {
            System.out.println(row);
        }

        String insertQuery = "INSERT INTO users (name, email) VALUES ('nameValue', 'emailValue')";
        int rowsInserted = executeUpdateQuery(url, username, password, driverClass, insertQuery);
        System.out.println("Rows inserted: " + rowsInserted);
    }
}
