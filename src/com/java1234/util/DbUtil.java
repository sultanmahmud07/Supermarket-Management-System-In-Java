package com.java1234.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
    private String dbUrl = "jdbc:mysql://localhost:3306/supermarket_ms?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    private String dbUserName = "root";
    private String dbPassword = "MdShimul@Molla007";
    private String jdbcName = "com.mysql.cj.jdbc.Driver";

    /**
     * Get Database Connection
     * 
     * @return Connection
     * @throws Exception
     */
    public Connection getCon() throws Exception {
        Class.forName(jdbcName);
        Connection con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        return con;
    }

    /**
     * Close Database Connection
     * 
     * @param con Connection
     * @throws Exception
     */
    public void closeCon(Connection con) throws Exception {
        if (con != null) {
            con.close();
        }
    }
}
