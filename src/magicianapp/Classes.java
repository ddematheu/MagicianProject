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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author David
 */
class Magician
{
    static ArrayList getAllMag()
    {
        ArrayList Mags = new ArrayList();
        Connection con;
        Statement st;
        ResultSet Magicians;
        try{
            String URL = Database.databaseConn();
            //System.out.println(URL);
            con = DriverManager.getConnection(URL, "java", "java");
            String sql1 = "select magicians from magicians";
            //System.out.println(sql1);
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
    static void AddWaitlist(String Customer, String Holiday)
    {
        try
        {
            String URL = Database.databaseConn();
            Connection conn = DriverManager.getConnection(URL);
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
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
            return result;
        }
        
    }
    
}

class BookingList
{
    static void AddBooking(String Holiday, String Customer)
    {
        try{
        String URL = Database.databaseConn();
        Connection conn = DriverManager.getConnection(URL,"java","java");
        String mag = Magician.RandMag(Holiday);
        System.out.println(mag + "!");
        if (mag == "null" ) 
            {    
            System.out.println("No data"); 
            WaitingList.AddWaitlist(Customer, Holiday);
            }
            else
            {    

            String sql1 = "INSERT INTO Bookings VALUES (?, ?, ?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
            preparedStatement1.setString(1, Customer);
            preparedStatement1.setString(2, mag);
            preparedStatement1.setString(3, Holiday);
            preparedStatement1.executeUpdate(); 
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
        row.add(Customer);
        row.add(Magician);
        row.add(Holiday);
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
        String Magician = rs.getString("Magician");
        String Holiday =  rs.getString("Holiday");
        row.add(Customer);
        row.add(Magician);
        row.add(Holiday);
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
        String Magician = rs.getString("Magician");
        String Holiday =  rs.getString("Holiday");
        row.add(Customer);
        row.add(Magician);
        row.add(Holiday);
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