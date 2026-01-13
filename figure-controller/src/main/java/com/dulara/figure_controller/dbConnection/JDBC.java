package com.dulara.figure_controller.dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBC {
    static String  url="jdbc:oracle:thin:@172.20.10.46:1521:COOPGSTB";
    public static Connection con() throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");

        Connection c = DriverManager.getConnection(url, "FIGUREVIWER", "Figure2025");

        return c;
    }

}
