package org.example.Admin;
import java.sql.*;
import org.example.db.*;
import static java.lang.System.exit;

public class Admin {

    String Username="Abhishek";
    public static String Password="Password";
    public static dbConnection db=new dbConnection();
    public Boolean Login(String User,String Pass)
    {
        Boolean success=false;
        if(User.equals(Username)&&Pass.equals(Password))
        {
            System.out.println("Login successfull");
            success=true;
        }
        else {
            System.out.println("Wrong credentials Try again!");
            success=false;
        }
        return success;
    }
     public void addBranch(String Bname,int id,String Location)
     {
         String query="INSERT INTO Branch (BranchName,BranchID,BranchLocation) VALUES (?,?,?)";

        try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(query)){

            ps.setString(1,Bname);
            ps.setInt(2,id);
            ps.setString(3,Location);
            ps.executeUpdate();

        }
        catch(SQLException e)
        {
            System.out.println(e);
        }

     }
     public Boolean BranchExists(String Bname,int id)
     {
         String query="SELECT 1 FROM Branch WHERE BranchName=? OR BranchID=?";
         try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(query))
         {
             ps.setString(1,Bname);
             ps.setInt(2,id);
             ResultSet r=ps.executeQuery();
             return r.next();
         }
         catch (SQLException e)
         {
             System.out.println(e);
             return true;
         }
     }
    public void addManager(String Mname,int Mid,String Email,int bid){
        String query="INSERT INTO Manager (ManagerName,ManagerID,ManagerEmail,Password,BranchID) VALUES (?,?,?,?,?)";

        try(Connection con=db.getConnection();PreparedStatement ps=con.prepareStatement(query))
        {
            ps.setString(1,Mname);
            ps.setInt(2,Mid);
            ps.setString(3,Email);
            ps.setString(4,Password);
            ps.setInt(5,bid);
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
    public static int ManagerExists(String Mname,int Mid,String Email,int bid)
    {
        String query="SELECT 1 FROM Manager WHERE ManagerID=?";
        String query1="SELECT 1 FROM Manager WHERE ManagerEmail=?";
        String query2="SELECT 1 FROM Branch WHERE BranchID=?";
        try(Connection conn=db.getConnection();PreparedStatement ps1=conn.prepareStatement(query2))
        {
            ps1.setInt(1,bid);
            ResultSet r=ps1.executeQuery();
            Boolean branchid= r.next();
            if(!branchid)
            {
                return -1;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(Connection conn=db.getConnection();PreparedStatement ps1=conn.prepareStatement(query))
        {
            ps1.setInt(1,Mid);
            ResultSet r=ps1.executeQuery();
            Boolean errorMid= r.next();
            if(errorMid==true)
            {
                return 1;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(Connection co=db.getConnection();PreparedStatement ps1=co.prepareStatement(query1))
        {
            ps1.setString(1,Email);
            ResultSet r=ps1.executeQuery();
            Boolean errorEmail= r.next();
            if(errorEmail==true)
            {
                return 2;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
