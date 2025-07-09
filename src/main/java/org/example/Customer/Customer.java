package org.example.Customer;

import org.example.Manager.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer extends Manager {

    public static Boolean LoginCustomer(String Cname,String Cemail,String pass)
    {
        String query="SELECT 1 FROM Customer WHERE (CustomerName=? AND CustomerEmail=?) AND Password=?";
        try(Connection connect=db.getConnection(); PreparedStatement present=connect.prepareStatement(query))
        {
            present.setString(1,Cname);
            present.setString(2,Cemail);
            present.setString(3,pass);
            ResultSet result=present.executeQuery();
            return result.next();

        }
        catch(SQLException e)
        {
            System.out.println(e);
            return false;
        }
    }

    public void ViewAccountDetails(int CustID) {
        int accid = GetAccountIDFromCustomerId(CustID);
        String Query = "SELECT A.AccountType, A.InitialBalance, C.CustomerName, C.CustomerEmail, C.CustomerphoneNumber " +
                "FROM Account A JOIN Customer C ON A.CustomerID = C.CustomerID " +
                "WHERE C.CustomerID = ?";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(Query)) {
            ps.setInt(1, CustID);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                System.out.println("Customer Details:");
                System.out.println("-----------------");
                System.out.println("Customer Name        : " + result.getString("CustomerName"));
                System.out.println("Customer Email       : " + result.getString("CustomerEmail"));
                System.out.println("Customer Phone Number: " + result.getLong("CustomerphoneNumber"));
                System.out.println("Account Type         : " + result.getString("AccountType"));
                System.out.println("Account Balance      : ₹" + result.getDouble("InitialBalance"));
            } else {
                System.out.println("No details found for Customer ID: " + CustID);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
