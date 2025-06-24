package cat.plexians.utils;

import java.sql.*;

public class SQLUtils {

    public SQLUtils() {
    }

    public String readValuesSQL(Connection conn, String sql, String field) throws SQLException {
        String s = null;
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData metadata = resultSet.getMetaData();
        int index = 0;

        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            if (metadata.getColumnName(i).equals(field)) {
                index = i;
            }
        }
        while (resultSet.next()) {
            s = resultSet.getString(index);
            System.out.println(field + ": " + s);
        }
        return s;
    }

    public ResultSet readAllValuesFromSQLStatement(Connection conn, String sql) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData metadata = resultSet.getMetaData();
        int columnCount = metadata.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.println(metadata.getColumnName(i) + " : " + resultSet.getString(i));
            }
        }
        return resultSet;
    }

    public ResultSet getResultSetFromQuery(Connection conn, String sql) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public String getFirstValuesFromSQLStatement(Connection conn, String sql, String field) throws SQLException {
        String s = null;
        int index = 1;
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData metadata = resultSet.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            if (metadata.getColumnName(i).equals(field)) {
                index = i;
            }
        }
        while (resultSet.next()) {
            s = resultSet.getString(index);
            System.out.println(field + ": " + s);
            break;
        }
        return s;
    }

    public void executeQueryUntilIsFinished(Connection conn, String sql) throws SQLException {
        String s = null;
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.executeQuery();
    }
}
