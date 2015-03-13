package org.apollo.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 *
 * @author Stuart
 */
public final class DatabaseUtil {

    /**
     *
     */
    private static final ComboPooledDataSource pool = new ComboPooledDataSource();

    static {
        try(InputStream is = new FileInputStream("data/database.xml")) {
            XmlNode config = new XmlParser().parse(is);
            if(!config.getName().equals("database")) {
                throw new IOException("Root node is not 'database'.");
            }

            XmlNode url = config.getChild("url"), username = config.getChild("username"), password = config.getChild("password");
            if(url == null || username == null || password == null) {
                throw new IOException("Root node must contain these three children - 'url', 'username', 'password'.");
            }

            pool.setJdbcUrl(url.getValue());
            pool.setUser(username.getValue());
            pool.setPassword(password.getValue());

            // optional params
            XmlNode initialPoolSize = config.getChild("initial_pool_size"), acquireIncrement = config.getChild("acquire_increment"),
                    maxPoolSize = config.getChild("max_pool_size"), minPoolSize = config.getChild("min_pool_size");

            if(initialPoolSize != null) {
                pool.setInitialPoolSize(Integer.parseInt(initialPoolSize.getValue()));
            }
            if(acquireIncrement != null) {
                pool.setAcquireIncrement(Integer.parseInt(acquireIncrement.getValue()));
            }
            if(maxPoolSize != null) {
                pool.setMaxPoolSize(Integer.parseInt(maxPoolSize.getValue()));
            }
            if(minPoolSize != null) {
                pool.setMinPoolSize(Integer.parseInt(minPoolSize.getValue()));
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     *
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

}