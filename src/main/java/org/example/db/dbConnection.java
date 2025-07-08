package org.example.db;
import java.sql.*;
public class dbConnection {
    static private  final  String User="root";
    static private final String Password="Abhi@1234";
    static private final String Url="jdbc:mysql://localhost:3306/Bank";
    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(Url,User,Password);
    }




}
