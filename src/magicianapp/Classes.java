/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magicianapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;
import static magicianapp.WaitingList.AddWaitlistwithTimestamp;
import static magicianapp.WaitingList.AddtoBooking;

/**
 *
 * @author David
 */
class Magician
{
    static void removeMagician(String name)
    {
        Connection con;
        try{
            String URL = Database.databaseConn();
            con = DriverManager.getConnection(URL, "java", "java");
            String sql1 = "DELETE FROM JAVA.Magicians WHERE Magicians = ?";
            PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
            preparedStatement1.setString(1, name);
            preparedStatement1.executeUpdate();
            JOptionPane.showMessageDialog(null, name + " was removed!");
            BookingList.MagRemoved(name);
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error");
        }
    }
    static void addMagician(String name)
    {
        Connection con;
        try{
            String URL = Database.databaseConn();
            con = DriverManager.getConnection(URL, "java", "java");
            String sql1 = "INSERT INTO magicians VALUES (?)";
            PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
            preparedStatement1.setString(1, name);
            preparedStatement1.executeUpdate(); 
            JOptionPane.showMessageDialog(null, name + " was added to the Magicians List");
            con.close();
            AddtoBooking();
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error!");
        }
    }
    
    static ArrayList getAllMag()
    {
        ArrayList Mags = new ArrayList();
        Connection con;
        ResultSet Magicians;
        try{
            String URL = Database.databaseConn();
            con = DriverManager.getConnection(URL, "java", "java");
            String sql1 = "select magicians from magicians";
            PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
            Magicians = preparedStatement1.executeQuery(); 
            Mags.add("");
            while(Magicians.next()) Mags.add(Magicians.getString(1));
            return Mags;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error");
            return Mags;
        }
    }
    
    static String RandMag(String Holiday)
    {
        ResultSet mag;
        String output = "";
        try
        {
            String URL = Database.databaseConn();
            Connection conn = DriverManager.getConnection(URL,"java","java");
            System.out.println(URL);
            String sql = "Select magicians from magicians where magicians not in ( Select magician from bookings where bookings.holiday = ?)";
            System.out.println(sql);
            PreparedStatement preparedStatement = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, Holiday);
            mag = preparedStatement.executeQuery();
            System.out.println("Statement exectuted");
            if (!mag.isBeforeFirst()) 
            {
                output = "null";
                System.out.println(output);
            }   
            else
            {
            mag.next();
            output = (String) mag.getString(1);
            System.out.println(output);
            }
        return output;
        }
        catch (Exception e)
        {
         System.err.print("Error with Magician");
         return output;   
        }

    }
}

class Holiday
{
        static void addHol(String name)
    {
        Connection con;
        try{
            String URL = Database.databaseConn();
            con = DriverManager.getConnection(URL, "java", "java");
            String sql1 = "INSERT INTO holiday VALUES (?)";
            PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
            preparedStatement1.setString(1, name);
            preparedStatement1.executeUpdate(); 
            JOptionPane.showMessageDialog(null, name + " was added to the Holiday List");
            con.close(); 
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error");
        }
    }
    
    static ArrayList getAllHol()
    {
        ArrayList Hol = new ArrayList();
        Connection con;
        Statement st;
        ResultSet Holiday;
        try{
            String URL = Database.databaseConn();
            Connection conn = DriverManager.getConnection(URL,"java","java");
            String sql1 = "select holiday from holiday";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            Holiday = preparedStatement1.executeQuery(); 
            Hol.add("");
            while(Holiday.next()) Hol.add(Holiday.getString(1));
            return Hol;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error");
            return Hol;
        }
    }
}

class WaitingList
{
    static void removeWaitlist(String Customer, String Holiday, Timestamp timestamp)
    {
        Connection con;
        try{
            String URL = Database.databaseConn();
            con = DriverManager.getConnection(URL, "java", "java");
            String sql1 = "DELETE FROM JAVA.WAITLIST WHERE Customer = ? AND Holiday = ? AND Timestamp = ?";
            PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
            preparedStatement1.setString(1, Customer);
            preparedStatement1.setString(2, Holiday);
            preparedStatement1.setTimestamp(3, timestamp);
            preparedStatement1.executeUpdate();  
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error Remove Waitlist");
        }
    }
    static void AddtoBooking()
    {
        ResultSet customer;
        try{
            String URL = Database.databaseConn();
            Connection conn = DriverManager.getConnection(URL,"java","java");
            String sql1 = "SELECT * FROM JAVA.WAITLIST Order by timestamp ASC";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            customer = preparedStatement1.executeQuery(); 
            
            while(customer.next())
            {
            String Customer = customer.getString("Customer");
            String Holiday = customer.getString("Holiday");
            java.sql.Timestamp Timestamp = customer.getTimestamp("Timestamp");
            removeWaitlist(Customer,Holiday,Timestamp);
            int c = BookingList.AddBookingwithTimestamp(Customer,Holiday,Timestamp);
            if(c == 1) JOptionPane.showMessageDialog(null, Customer + " was removed from the Waitlist and booked");
            }

        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error");
            
        }
    }
    static void AddWaitlist(String Customer, String Holiday)
    {
        try
        {
            String URL = Database.databaseConn();
            Connection conn = DriverManager.getConnection(URL,"java","java");
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            String sql1 = "INSERT INTO Waitlist VALUES (?, ?, ?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            preparedStatement1.setString(1, Customer);
            preparedStatement1.setString(2, Holiday);
            preparedStatement1.setTimestamp(3, currentTimestamp);
            preparedStatement1.executeUpdate(); 
            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
        }

    }
    
        static void AddWaitlistwithTimestamp(String Customer, String Holiday, Timestamp Time)
    {
        System.out.println(Holiday);
        try
        {
            String URL = Database.databaseConn();
            Connection conn = DriverManager.getConnection(URL,"java","java");
            String sql1 = "INSERT INTO Waitlist VALUES (?, ?, ?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            preparedStatement1.setString(1, Customer);
            preparedStatement1.setString(2, Holiday);
            preparedStatement1.setTimestamp(3, Time);
            preparedStatement1.executeUpdate(); 
            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
        }

    }
    
    static ArrayList ShowWaitlist()
    {
                ArrayList result = new ArrayList();
        try
        {
        String URL = Database.databaseConn();
        Connection conn = DriverManager.getConnection(URL,"java","java"); 
        String sql = "Select * FROM Waitlist";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        
        int columns = rs.getMetaData().getColumnCount(); 
        ArrayList Columns = new ArrayList();
        for(int i =1; i<= columns;i++)
        {
            Columns.add(rs.getMetaData().getColumnName(i));
        }
        result.add(Columns);
        
        while(rs.next())
        {
        ArrayList row = new ArrayList();      
        String Customer = rs.getString("Customer");
        String Holiday =  rs.getString("Holiday");
        java.sql.Timestamp Timestamp = rs.getTimestamp("Timestamp");
        String TimeStamp = Timestamp.toString();
        row.add(Customer);
        row.add(Holiday);
        row.add(TimeStamp);
        result.add(row);
        }
        System.out.println("successful ShowBooks");
        return result;
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!! "); 
            System.err.println(e.getMessage()); 
            return result;
        }
        
    }
    
}

class BookingList
{
    static void MagRemoved(String Mag)
    {
        Connection con;
        try{
            String URL = Database.databaseConn();
            con = DriverManager.getConnection(URL, "java", "java");
            String sql1 = "SELECT * FROM JAVA.BOOKINGS WHERE Magician = ?";
            PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
            preparedStatement1.setString(1, Mag);
            ResultSet Ditched = preparedStatement1.executeQuery();
            while(Ditched.next())
            {
            ArrayList row = new ArrayList();      
            String Customer = Ditched.getString("Customer");
            String Holiday =  Ditched.getString("Holiday");
            String Magician = Ditched.getString("Magician");
            java.sql.Timestamp Timestamp = Ditched.getTimestamp("Timestamp");
            RemoveCustomer(Customer,Holiday); //removes from bookings
            int c = AddBookingwithTimestamp(Customer,Holiday,Timestamp); //try to rebook
            if(c == 1) JOptionPane.showMessageDialog(null, Customer + " was successfully rebooked");
            else if (c == 2) JOptionPane.showMessageDialog(null, Customer + " was placed in waitlist");
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error");
        }
    }
    static ArrayList getAllNames()
    {
        ArrayList Names = new ArrayList();
        Connection con;
        Statement st;
        ResultSet Holiday;
        try{
            String URL = Database.databaseConn();
            Connection conn = DriverManager.getConnection(URL,"java","java");
            String sql1 = "select distinct Customer from Bookings";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            Holiday = preparedStatement1.executeQuery(); 
            Names.add("");
            while(Holiday.next()) Names.add(Holiday.getString(1));
            return Names;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error");
            return Names;
        }
    }
    static void RemoveCustomer(String Customer, String Holiday)
    {
        Connection con;
        try{
            String URL = Database.databaseConn();
            con = DriverManager.getConnection(URL, "java", "java");
            String sql1 = "DELETE FROM Bookings WHERE Customer = ? AND Holiday = ?";
            PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
            preparedStatement1.setString(1, Customer);
            preparedStatement1.setString(2, Holiday);
            preparedStatement1.executeUpdate();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error Remove Waitlist");
        }
    }
    static void CancelBooking(String Customer, String Holiday)
    {
        Connection con;
        try{
            String URL = Database.databaseConn();
            con = DriverManager.getConnection(URL, "java", "java");
            String sql1 = "DELETE FROM Bookings WHERE Customer = ? AND Holiday = ?";
            PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
            preparedStatement1.setString(1, Customer);
            preparedStatement1.setString(2, Holiday);
            preparedStatement1.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sucessfully Cancelled Booking");
            AddtoBooking();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connection Error");
        }
    }
    
    static int AddBookingwithTimestamp(String Customer, String Holiday,Timestamp Time)
    {
        
        try{
        String URL = Database.databaseConn();
        Connection conn = DriverManager.getConnection(URL,"java","java");
        String mag = Magician.RandMag(Holiday);
        System.out.println(mag + "!");
        if (mag == "null" ) 
            {    
            System.out.println("No data"); 
            WaitingList.AddWaitlistwithTimestamp(Customer, Holiday,Time);
            return 2;
            }
            else
            {    

            String sql1 = "INSERT INTO Bookings VALUES (?, ?, ?,?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            preparedStatement1.setString(1, Customer);
            preparedStatement1.setString(2, mag);
            preparedStatement1.setString(3, Holiday);
            preparedStatement1.setTimestamp(4, Time);
            preparedStatement1.executeUpdate(); 
            conn.close(); 
            return 1;
            }
        }
        catch(Exception e)
        {
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
            return 0;
        }
    }
    
    
    static void AddBooking(String Holiday, String Customer)
    {
        
        try{
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        String URL = Database.databaseConn();
        Connection conn = DriverManager.getConnection(URL,"java","java");
        String mag = Magician.RandMag(Holiday);
        System.out.println(mag + "!");
        if (mag == "null" ) 
            {    
            System.out.println("No data"); 
            WaitingList.AddWaitlist(Customer, Holiday);
            JOptionPane.showMessageDialog(null, Customer + " was placed in waitlist");
            }
            else
            {    

            String sql1 = "INSERT INTO Bookings VALUES (?, ?, ?,?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            preparedStatement1.setString(1, Customer);
            preparedStatement1.setString(2, mag);
            preparedStatement1.setString(3, Holiday);
            preparedStatement1.setTimestamp(4, currentTimestamp);
            preparedStatement1.executeUpdate(); 
            JOptionPane.showMessageDialog(null, Customer + " was successfully booked!");
            conn.close(); 
            }
        }
        catch(Exception e)
        {
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
        }
    }

    static ArrayList showBooks()
    {
        ArrayList result = new ArrayList();
        try
        {
        String URL = Database.databaseConn();
        Connection conn = DriverManager.getConnection(URL,"java","java"); 
        String sql = "Select * FROM Bookings";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        
        int columns = rs.getMetaData().getColumnCount(); 
        ArrayList Columns = new ArrayList();
        for(int i =1; i<= columns;i++)
        {
            Columns.add(rs.getMetaData().getColumnName(i));
        }
        result.add(Columns);
        while(rs.next())
        {
        ArrayList row = new ArrayList();      
        String Customer = rs.getString("Customer");
        String Holiday =  rs.getString("Holiday");
        String Magician = rs.getString("Magician");
        java.sql.Timestamp Timestamp = rs.getTimestamp("Timestamp");
        String TimeStamp = Timestamp.toString();
        row.add(Customer);
        row.add(Magician);
        row.add(Holiday);
        row.add(TimeStamp);
        result.add(row);
        }
        System.out.println("successful ShowBooks");
        return result;
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
            return result;
        }
    }
    
    static ArrayList StatusMagician(String Mag)
    {
        ArrayList result = new ArrayList();
        try
        {
        String URL = Database.databaseConn();
        Connection conn = DriverManager.getConnection(URL,"java","java"); 
        String sql = "Select * FROM Bookings where Magician = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, Mag);
        ResultSet rs = preparedStatement.executeQuery();
        
        int columns = rs.getMetaData().getColumnCount(); 
        ArrayList Columns = new ArrayList();
        for(int i =1; i<= columns;i++)
        {
            Columns.add(rs.getMetaData().getColumnName(i));
        }
        result.add(Columns);
        while(rs.next())
        {
        ArrayList row = new ArrayList();      
        String Customer = rs.getString("Customer");
        String Holiday =  rs.getString("Holiday");
        String Magician = rs.getString("Magician");
        java.sql.Timestamp Timestamp = rs.getTimestamp("Timestamp");
        String TimeStamp = Timestamp.toString();
        row.add(Customer);
        row.add(Magician);
        row.add(Holiday);
        row.add(TimeStamp);
        result.add(row);
        }
        return result;
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
            return result;
        }
    }
    
    static ArrayList StatusHoliday(String Hol)
    {
                ArrayList result = new ArrayList();
        try
        {
        String URL = Database.databaseConn();
        Connection conn = DriverManager.getConnection(URL,"java","java"); 
        String sql = "Select * FROM Bookings where Holiday = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, Hol);
        ResultSet rs = preparedStatement.executeQuery();
        
        int columns = rs.getMetaData().getColumnCount(); 
        ArrayList Columns = new ArrayList();
        for(int i =1; i<= columns;i++)
        {
            Columns.add(rs.getMetaData().getColumnName(i));
        }
        result.add(Columns);
        while(rs.next())
        {
        ArrayList row = new ArrayList();      
        String Customer = rs.getString("Customer");
        String Holiday =  rs.getString("Holiday");
        String Magician = rs.getString("Magician");
        java.sql.Timestamp Timestamp = rs.getTimestamp("Timestamp");
        String TimeStamp = Timestamp.toString();
        row.add(Customer);
        row.add(Magician);
        row.add(Holiday);
        row.add(TimeStamp);
        result.add(row);
        }
        return result;
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
            return result;
        }
    }
}

class Database
{
    static String databaseConn()
    {
        return "jdbc:derby://localhost:1527/Magicians";
    }
}