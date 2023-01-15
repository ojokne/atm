package oen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        try {
            boolean exit = false;
            Scanner input = new Scanner(System.in);
                        
            // load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");

            //  Connect to the database with name atm
            Connection conn = createConnection();
            System.out.println("Database connection made successfully");

            // Create customer table
            // Customer.createTable(conn);
            // System.out.println("Customer Table Created Successfully");

            // Drop customer table
            // Customer.dropTable(conn);
            // System.out.println("Customer table dropped successfully");

            // Add a customer to database
            // Customer ctm = new Customer();
            // ctm.store(conn);
            // System.out.printf("Customer created with \n");
            // System.out.printf("Account Number: %s\n", ctm.getAccountNumber());
            // System.out.printf("Pin: %s\n", ctm.getPin());
            // System.out.println("*****************************************************");
            // System.out.println();
            // System.out.println("Please keep your details well inorder to access the account");
            // System.out.println();
            // System.out.println("*****************************************************"); 
            
            // // verify customer
            Customer customer = new Customer("1840013834", "43358");
            if (customer.verify(conn)) {
                while (!exit) {
                    System.out.println();
                    System.out.println();
                    System.out.println("*********************************");
                    System.out.println("Welcome to the ATM");
                    System.out.println("Please Choose an option");
                    System.out.println();

                    System.out.println("1: Withdraw");
                    System.out.println("2: Deposit");
                    System.out.println("3: Check balance");
                    System.out.println("4: exit");
                    System.out.println("*********************************");
                    System.out.print("Enter your option: ");

                    int option = input.nextInt();
                    System.out.println();

                    switch (option) {
                        case 1:
                            System.out.println("Enter amount to withdraw");
                            double withdraw = input.nextDouble();
                            if (withdraw < customer.getBalance(conn)) {
                                customer.withdraw(conn, withdraw);
                                System.out.printf("A withdraw of %g has been successful\n", withdraw);
                                System.out.printf("Your balance is %g",customer.getBalance(conn));
                            } else {
                                System.out.println("You have insufficient balance");
                            }
                            break;
                        case 2:
                            System.out.print("Enter amount to deposit: ");
                            double deposit = input.nextDouble();
                            if (deposit > 0) {
                                customer.deposit(conn, deposit);
                                System.out.printf("Deposit of %g successfull\n", deposit);
                                System.out.printf("Your balance is %g", customer.getBalance(conn));
                            }
                            else {
                                System.out.println("The deposit amount is not sufficient ");
                            }
                            break;
                        case 3:
                            double balance = customer.getBalance(conn);
                            System.out.printf("Balance is %g\n",balance);
                            break;
                        case 4:
                            System.out.println("exit");
                            exit = true;
                            System.out.println("Thank you, Please come back again");
                            break;
                        default:
                            break;
                    }
                }
            }
            input.close();
        
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static Connection createConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/atm";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

}


    
