package oen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {
        private String accountNumber;
        private String pin;

        private double balance;

        Customer() {
            this.accountNumber = generateAccountNumber();
            this.pin = generatePin();
            this.balance = 0;
        }

        Customer(String accountNumber, String pin) {
            this.accountNumber = accountNumber;
            this.pin = pin;
        }

        public String generateAccountNumber() {
            String ac = "";
            String s = "";
            for (int i = 0; i < 10; i++) {
                int n = (int) Math.round((Math.random() * 1e16) % 9);
                s += n;
            }
            ac += s;
            return ac;
        }

        public String generatePin() {
            String pin = "";
            String s = "";
            for (int i = 0; i < 5; i++) {
                int n = (int) Math.round((Math.random() * 1e16) % 9);
                s += n;
            }
            pin += s;
            return pin;
        }

        public String getAccountNumber() {
            return this.accountNumber;
        }

        public String getPin() {
            return this.pin;
        }

        public static void createTable(Connection conn) throws SQLException {
            String create = "CREATE TABLE IF NOT EXISTS customer (id INTEGER NOT NULL AUTO_INCREMENT, accountNumber VARCHAR(10),pin VARCHAR(5),balance DOUBLE, PRIMARY KEY(id))";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(create);
        }
        
        public static void dropTable(Connection conn) throws SQLException {
            String sql = "DROP TABLE IF EXISTS customer";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        }

        public boolean verify(Connection conn) throws SQLException {
            // String sql = "SELECT * FROM customer WHERE pin = 43608 AND accountNumber = 'AC17200'";
            // //         + this.accountNumber;
            String sql = String.format( "SELECT * FROM customer WHERE pin = %s AND accountNumber = %s",this.pin,this.accountNumber);
            boolean verified = false;
            ResultSet rs = returnRecord(conn, sql);
            if (rs.isBeforeFirst()) {
                verified = true;
            }
            return verified;
        }
        public void store(Connection conn) throws SQLException {
            String sql = "INSERT INTO customer (accountNumber,pin,balance) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.accountNumber);
            ps.setString(2, this.pin);
            ps.setDouble(3, this.balance);
            ps.execute();
        }

        public double getBalance(Connection conn) throws SQLException {
            String sql = "SELECT balance FROM customer WHERE pin = " + this.pin + " AND accountNumber = "
                    + this.accountNumber;
            ResultSet rs = returnRecord(conn, sql);
            double balance = 0;
            while (rs.next()) {
                balance = rs.getDouble("balance");
            }
            return balance;
        }
        
        public void deposit(Connection conn, double amount) throws SQLException {
            double current = this.getBalance(conn) + amount;
            String sql = "UPDATE customer SET balance = " + current + " WHERE pin = " + this.pin
                    + " AND accountNumber = "
                    + this.accountNumber;
            updateRecord(conn, sql);
        }
        
        public void withdraw(Connection conn, double amount) throws SQLException {
            double current = this.getBalance(conn) - amount;
            String sql = "UPDATE customer SET balance = " + current + " WHERE pin = " + this.pin
                    + " AND accountNumber = "
                    + this.accountNumber;
            updateRecord(conn, sql);
        }
        public void updateRecord(Connection conn, String sql) throws SQLException {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }

        public  ResultSet returnRecord(Connection conn,String sql) throws SQLException {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        }
      

    }

