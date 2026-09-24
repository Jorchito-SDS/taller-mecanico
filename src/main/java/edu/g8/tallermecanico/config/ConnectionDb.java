package main.java.edu.g8.tallermecanico.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author informatica
 */
public class ConnectionDb {
       private static Connection connection;
    
    private ConnectionDb(){
        
    }
    
    public static Connection getConnection() throws SQLException{
        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(CredentialsDb.URL_DB, CredentialsDb.USER_DB, CredentialsDb.PASS_DB);
        }
        return connection;
    }
}
