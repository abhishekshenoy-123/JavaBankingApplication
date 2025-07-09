package org.example;

import org.example.Admin.Admin;
import org.example.Customer.Customer;
import org.example.Manager.Manager;

import java.sql.*;
import java.util.*;

import static java.lang.System.exit;
import static org.example.Admin.Admin.ManagerExists;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Admin a = new Admin();
        Manager b = new Manager();
        Customer c=new Customer();
        while (true) {
            System.out.println("1.Admin\t2.Manager\t3.Customer\t4.Exit");
            System.out.println("Enter the choice");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter the username");
                    sc.nextLine();
                    String username = sc.nextLine();
                    System.out.println("Enter the password");
                    String Password = sc.nextLine();
                    Boolean resSuccess = a.Login(username, Password);
                    if (resSuccess == false) {
                        continue;
                    }
                    while (true) {
                        System.out.println("1. Want to create new Branch");
                        System.out.println("2. Want to create new Manager");
                        System.out.println("3. Logout");
                        System.out.println("Choose the option");
                        int option = sc.nextInt();
                        switch (option) {
                            case 1:
                                System.out.println("Enter the branch name");
                                sc.nextLine();
                                String BranchName = sc.nextLine();
                                System.out.println("Enter the branch Id");
                                int BranchId = sc.nextInt();
                                System.out.println("Enter the branch Location");
                                sc.nextLine();
                                String BranchLocation = sc.nextLine();
                                if (a.BranchExists(BranchName, BranchId) == true) {
                                    System.out.println("Branch Name or ID already exists");
                                    continue;
                                }
                                a.addBranch(BranchName, BranchId, BranchLocation);
                                System.out.println("Sucessfully added to database");
                                continue;
                            case 2:
                                System.out.println("Enter the Manager name");
                                sc.nextLine();
                                String Mname = sc.nextLine();
                                System.out.println("Enter the Manger Id");
                                int Mid = sc.nextInt();
                                System.out.println("Enter the Manager email");
                                sc.nextLine();
                                String Memail = sc.nextLine();
                                System.out.println("Enter the Branch id");
                                int Bid = sc.nextInt();
                                int resultBranch = ManagerExists(Mname, Mid, Memail, Bid);
                                if (resultBranch == -1) {
                                    System.out.println("Branch doesnot exist");
                                    continue;
                                } else if (resultBranch == 1) {
                                    System.out.println("Id already exists");
                                    continue;
                                } else if (resultBranch == 2) {
                                    System.out.println("Email already Registered");
                                    continue;
                                } else {
                                    System.out.println("Manager registered successfully");
                                }

                                a.addManager(Mname, Mid, Memail, Bid);
                                continue;
                            case 3:
                                break;
                        }
                        break;
                    }
                    break;
                case 2:
                    System.out.println("Enter the Manager name");
                    sc.nextLine();
                    String Mname = sc.nextLine();
                    System.out.println("Enter the Manager email");
                    String Nemail = sc.nextLine();
                    System.out.println("Enter the password");
                    String pass = sc.nextLine();
                    int managerID = b.getManagerID(Nemail, pass);
                    if (b.LoginManager(Mname, Nemail, pass)) {
                        System.out.println("Login successfull");
                        while (true) {
                            System.out.println("1.AddCustomers\t2.UpdateCustomer\t3Transaction\t4.Report\t5.Logout");
                            int choose = sc.nextInt();
                            switch (choose) {
                                case 1:
                                    System.out.println("Enter the customer name");
                                    sc.nextLine();
                                    String cname = sc.nextLine();
                                    System.out.println("Enter the email");
                                    String email = sc.nextLine();
                                    System.out.println("Enter the phone number");
                                    long number = sc.nextLong();
                                    System.out.println("Enter the password");
                                    sc.nextLine();
                                    String password=sc.nextLine();
                                    if (b.CheckCustomers(email) == true) {
                                        System.out.println("Email already registered");
                                    } else {
                                        int cid = b.addCustomer(cname, email, number, managerID,password);
                                        System.out.println("Details retreived succesfully");
                                        System.out.println("To create account please enter the initial balance to be deposited");
                                        Double initialb = sc.nextDouble();
                                        b.createAccount(cid, "Savings", initialb);

                                    }
                                    continue;
                                case 2:
                                        sc.nextLine();
                                        System.out.println("Enter the customer name ");
                                        String customname;
                                        customname = sc.nextLine();
                                        System.out.println("Enter the email ");
                                        String customemail;
                                        customemail = sc.nextLine();
                                        System.out.println("Enter the password");
                                        String customPassword=sc.nextLine();
                                        if(b.checkCustomerExists(customname,customemail,customPassword)==false)
                                        {
                                            System.out.println("Wrong credentials Try Again");
                                            continue;
                                        }
                                        int customID = b.getCustomerIDFromEmail(customemail);
                                    System.out.println("Enter the field which you want to update(1.Email,2.PhoneNumber)");
                                    int fieldnumber = sc.nextInt();
                                    if (fieldnumber == 1) {
                                        System.out.println("Enter the new email");
                                        sc.nextLine();
                                        String newEmail = sc.nextLine();
                                        b.updateEmail(customID, newEmail);
                                    } else {
                                        System.out.println("Enter new PhoneNumber");
                                        sc.nextLine();
                                        long newPhoneNumber = sc.nextLong();
                                        b.updatePhoneNumber(customID, newPhoneNumber);
                                    }
                                    continue;
                                case 3:
                                    System.out.println("Enter the customer email");
                                    sc.nextLine();
                                    String Cemail = sc.nextLine();
                                    int custId = b.getCustomerIDFromEmail(Cemail);
                                    int AccID = b.GetAccountIDFromCustomerId(custId);
                                    System.out.println("Select the Transaction type(1.withdraw,2.Deposit)");
                                    int selection = sc.nextInt();
                                    if (selection == 1) {
                                        System.out.println("Enter the amount to be withdrawn");
                                        Double Amount = sc.nextDouble();
                                        Boolean withdrawres = b.Transaction(AccID, "Withdraw", Amount);
                                        if (withdrawres == false) {
                                            continue;
                                        } else {
                                            System.out.println("Withdraw succesfull");
                                        }
                                    } else if (selection == 2) {
                                        System.out.println("Enter the amount to be Deposit");
                                        Double Amount = sc.nextDouble();
                                        b.Transaction(AccID, "Deposit", Amount);
                                    }

                                    continue;
                                case 4:
                                    System.out.println("Generate report:\n1. Daily Report \n2. Monthly report");
                                    System.out.println("Enter your choice");
                                    int ch = sc.nextInt();
                                    System.out.print("Enter Account ID: ");
                                    int accId = sc.nextInt();
                                    sc.nextLine();
                                    String repo_type = (ch == 1) ? ("Daily") : (ch == 2) ? ("Monthly") : null;
                                    if (repo_type == null) {
                                        System.out.println("Invalid choice");
                                        break;
                                    }
                                    b.generateReport(repo_type, Mname, accId);
                                    continue;
                                case 5:
                                    break;

                            }
                            break;
                        }
                    } else {
                        System.out.println("Login unsuccesfull try again");
                        continue;
                    }
                    break;
                case 3:

                    while(true)
                    {
                        System.out.println("Enter your name");
                        sc.nextLine();  // Clear buffer
                        String yourName = sc.nextLine();

                        System.out.println("Enter your email");
                        String yourEmail = sc.nextLine();

                        System.out.println("Enter your password");
                        String yourPassword = sc.nextLine();

                        Boolean possibility = c.LoginCustomer(yourName, yourEmail, yourPassword);

                        if(possibility)
                        {
                            System.out.println("Login successful");
                            System.out.println("Account details are: ");
                            int yourID=b.getCustomerIDFromEmail(yourEmail);
                            c.ViewAccountDetails(yourID);
                            break;
                        }
                        else
                        {
                            System.out.println("Try again");
                        }
                    }



                    break;
                case 4:
                    exit(0);

            }

        }


    }
}
