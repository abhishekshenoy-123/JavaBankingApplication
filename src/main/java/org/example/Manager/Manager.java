package org.example.Manager;

import org.example.Admin.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Manager extends Admin {
    public static Boolean LoginManager(String Mname,String Memail,String pass)
    {
        String query="SELECT 1 FROM Manager WHERE (ManagerName=? AND ManagerEmail=?) AND Password=?";
        try(Connection connect=db.getConnection(); PreparedStatement present=connect.prepareStatement(query))
        {
            present.setString(1,Mname);
            present.setString(2,Memail);
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
    public int getCustomerId()
    {
        String Query="SELECT MAX(CustomerID) FROM Customer";
        try(Connection conn=db.getConnection();PreparedStatement ps=conn.prepareStatement(Query);ResultSet result=ps.executeQuery();)
        {
            if (result.next())
            {
                return result.getInt(1)+1;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        return 1;
    }
    public int getCustomerIDFromEmail(String email)
    {
        String Query="SELECT CustomerID FROM Customer WHERE CustomerEmail=?";
        try(Connection co=db.getConnection();PreparedStatement ps=co.prepareStatement(Query))
        {
            ps.setString(1,email.trim());
            ResultSet result=ps.executeQuery();
            if(result.next())
            {
                return result.getInt("CustomerID");
            }
            else {
                System.out.println("customer not registered with "+email);
                return -1;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        return -1;
    }
    public void UpdateSinglefield(int customerID,String Field,String newValue)
    {
        String QUERY="UPDATE Customer SET "+Field+" =? WHERE CustomerID=?";
        try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(QUERY))
        {
            ps.setString(1,newValue);
            ps.setInt(2,customerID);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(Field + " updated successfully.");
            } else {
                System.out.println("Customer not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateEmail(int customerID,String newEmail)
    {
        UpdateSinglefield(customerID,"CustomerEmail",newEmail);
    }
    public void updatePhoneNumber(int customerID,long newPhoneNumber)
    {
        UpdateSinglefield(customerID,"CustomerphoneNumber",String.valueOf(newPhoneNumber));
    }

    public int addCustomer(String CustomerName,String CustomerEmail,long CustomerphoneNumber,int ManagerID)
    {
        String Query="INSERT INTO Customer(CustomerID,CustomerName,CustomerEmail,CustomerphoneNumber,ManagerID) VALUES(?,?,?,?,?)";
        int CustomerID=getCustomerId();
        try(Connection conn=db.getConnection();PreparedStatement ps=conn.prepareStatement(Query))
        {
                ps.setInt(1,CustomerID);
                ps.setString(2,CustomerName);
                ps.setString(3,CustomerEmail.trim());
                ps.setLong(4,CustomerphoneNumber);
                ps.setInt(5,ManagerID);
                ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return CustomerID;
    }
    public Boolean CheckCustomers(String Email)
    {
        String Query="SELECT 1 FROM Customer WHERE CustomerEmail=?";
        try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(Query))
        {
            ps.setString(1,Email);
            ResultSet RES=ps.executeQuery();
            return RES.next();
        }
        catch(SQLException e)
        {
            System.out.println(e);
            return false;
        }
    }
    public int getManagerID(String Nemail,String Password)
    {
        String Query="SELECT ManagerID FROM Manager WHERE ManagerEmail=? AND Password=?";
        try(Connection conn=db.getConnection();PreparedStatement ps=conn.prepareStatement(Query))
        {
            ps.setString(1,Nemail);
            ps.setString(2,Password);
           ResultSet result= ps.executeQuery();
           if(result.next())
           {
              return result.getInt(1);
           }

        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return -1;
    }

    public int getAccountID()
    {
        String Query="SELECT MAX(AccountID) FROM Account";
        try(Connection conn=db.getConnection();PreparedStatement ps=conn.prepareStatement(Query);ResultSet result=ps.executeQuery();)
        {
            if (result.next())
            {
                return result.getInt(1)+1;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        return 1;
    }

    public void createAccount(int CustomerID,String type,Double initialBalance)
    {
         String Query="INSERT INTO Account(AccountID,AccountType,InitialBalance,CustomerID) VALUES (?,?,?,?)";
         int AccountID=getAccountID();
         try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(Query))
         {
             ps.setInt(1,AccountID);
             ps.setString(2,type);
             ps.setDouble(3,initialBalance);
             ps.setInt(4,CustomerID);
             ps.executeUpdate();
             System.out.println("Account created succesfully");
         }
         catch (SQLException e)
         {
             System.out.println(e);
         }
    }

    public int GetAccountIDFromCustomerId(int CustomerID)
    {
        String Query="SELECT AccountID FROM Account WHERE CustomerID=?";
        try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(Query))
        {
            ps.setInt(1,CustomerID);
            ResultSet result=ps.executeQuery();
            if(result.next())
            {
                return result.getInt(1);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        return -1;
    }

    public Double getBalance(int AccID)
    {
        String Query="SELECT InitialBalance FROM Account WHERE AccountID=?";
        try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(Query))
        {
            ps.setInt(1,AccID);
            ResultSet result=ps.executeQuery();
            if(result.next())
            {
                return result.getDouble("InitialBalance");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1.0;
    }
    public Boolean Transaction(int AccID,String Type,Double amount)
    {
        Double balance=getBalance(AccID);
        Boolean res=false;
        if(Type.equals("Withdraw"))
        {
            if(amount>balance)
            {
                System.out.println("Withdrawal amount is greater than balance");
                System.out.println("The current balance is "+balance);
                res=false;
            }
            else {
                Double newBalance=balance-amount;
                String Query="INSERT INTO Transaction (TransactionType,AccountID,Amount) VALUES(?,?,?)";
                String Query1="UPDATE Account SET InitialBalance=? WHERE AccountID=?";
                try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(Query))
                {
                    ps.setString(1,Type);
                    ps.setInt(2,AccID);
                    ps.setDouble(3,amount);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try(Connection con1=db.getConnection();PreparedStatement ps1=con1.prepareStatement(Query1))
                {
                    ps1.setDouble(1,newBalance);
                    ps1.setInt(2,AccID);
                    ps1.executeUpdate();
                    System.out.println("Current balance is "+newBalance);
                    res=true;
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                }

            }
        }
        else if(Type.equals("Deposit"))
        {
            Double AddBalance=balance+amount;
            String Query2="INSERT INTO Transaction (TransactionType,AccountID,Amount) VALUES(?,?,?)";
            String Query3="UPDATE Account SET InitialBalance=? WHERE AccountID=?";
            try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(Query2))
            {
                ps.setString(1,Type);
                ps.setInt(2,AccID);
                ps.setDouble(3,amount);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try(Connection con1=db.getConnection();PreparedStatement ps1=con1.prepareStatement(Query3))
            {
                ps1.setDouble(1,AddBalance);
                ps1.setInt(2,AccID);
                ps1.executeUpdate();
                System.out.println("Current balance is "+AddBalance);
                res=true;
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
        }
        return res;
    }







}
