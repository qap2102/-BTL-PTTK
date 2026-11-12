package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Common JDBC connection holder for data access classes.
 * Opens a MySQL connection by default, or accepts a caller-owned connection.
 * The caller is responsible for closing the connection when finished.
 */
public abstract class DAO4 implements AutoCloseable {

    protected static final String URL = "jdbc:mysql://localhost:3306/libman_db1";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    protected Connection connection;
    private boolean ownsConnection;

    protected DAO4() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    System.getProperty("libman.db.url", URL),
                    System.getProperty("libman.db.username", USERNAME),
                    System.getProperty("libman.db.password", PASSWORD));
            ownsConnection = true;
        } catch (ClassNotFoundException | SQLException ex) {
            throw new IllegalStateException("Unable to connect to the LibMan database.", ex);
        }
    }

    protected DAO4(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "connection");
    }

    public void setConnection(Connection connection) {
        Objects.requireNonNull(connection, "connection");
        close();
        this.connection = Objects.requireNonNull(connection, "connection");
        ownsConnection = false;
    }

    public Connection getConnection() { return requireConnection(); }

    @Override
    public void close() {
        if (ownsConnection && connection != null) {
            try { connection.close(); }
            catch (SQLException ex) { throw new IllegalStateException("Unable to close the database connection.", ex); }
            finally { connection = null; ownsConnection = false; }
        }
    }

    protected Connection requireConnection() {
        if (connection == null) {
            throw new IllegalStateException("A JDBC connection must be provided before accessing the database.");
        }
        return connection;
    }
}
