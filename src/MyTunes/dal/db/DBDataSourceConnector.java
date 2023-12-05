package MyTunes.dal.db;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;

/**
 * Object used to gain connection to sql database
 * using JDBC.
 */

public class DBDataSourceConnector {

    private SQLServerDataSource dataSource;

    public DBDataSourceConnector() {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName("10.176.111.31");
        dataSource.setUser("CSe20B_24");
        dataSource.setPassword("hunden123");
        dataSource.setDatabaseName("MyTunes2020");
    }


    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
