/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facerecognition;

/**
 * 
 * @author sharathkashyap
 */
import java.sql.*;

public class JDBC {
    // JDBC driver name and database URL
    static final String DB_URL = "jdbc:mysql://localhost:3306/students";
    static final String USER = "root";
    static final String PASS = "";
    static
    {
        try 
        {
            Class.forName("com.mysql.jdbc.Driver"); 
        }
        catch(Exception e)
        {
             System.out.println(e.getMessage());
        }
    }
    static String registerNewStudent(String firstName, String lastName, String imgLocation, int age, String gender, String track) 
    {
        PreparedStatement stmt = null;
        String sID = firstName.toLowerCase() + lastName.toLowerCase().charAt(0);
        String currentTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        String status = "Unable to register this student";
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS)) 
        {
            String sql = "INSERT INTO REGISTER VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sID);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, imgLocation);
            stmt.setInt(5, age);
            stmt.setString(6, gender);
            stmt.setString(7, track);
            stmt.setInt(8, 1);
            stmt.setString(9, currentTime);
            try 
            {
                System.out.println("No. of rows impacted = " + stmt.executeUpdate());
                status = "Student registered";
            } 
            catch (SQLException e) 
            {
                status = "Cannot register. Re-check input.";
            }

        } 
        catch (Exception e) 
        {
            status = "Cannot register. Re-check connection.";
        }
        return status;
    }
    static Student getStudentDetails(String sID) 
    {
        PreparedStatement stmt = null;
        Student currStudent = null;
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS)) 
        {
            String sql = "SELECT * FROM REGISTER WHERE sid=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sID);
            String firstName = "";
            String lastName = "";
            String imagePath = "";
            int age = 0;
            String gender = "";
            String track = "";
            java.sql.Timestamp lastVisited;
            int timesVisited = 0;
            try (ResultSet rs = stmt.executeQuery()) 
            {
                if (rs == null) 
                {
                    return currStudent;
                }
                while (rs.next()) 
                {
                    firstName = rs.getString("FIRSTNAME");
                    lastName = rs.getString("LASTNAME");
                    imagePath = rs.getString("IMAGE");
                    age = rs.getInt("AGE");
                    gender = rs.getString("GENDER");
                    track = rs.getString("TRACK");
                    lastVisited = rs.getTimestamp("LASTVISIT");
                    timesVisited = rs.getInt("TIMESVISITED");
                    System.out.println(firstName + lastName);
                    currStudent = new Student(sID, firstName, lastName, gender, track, imagePath, age, timesVisited, lastVisited);
                }
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
        return currStudent;
    }
    static void updateVisitNumberAndDate(String sID, int timesVisited, Timestamp currentTime)
    {

        PreparedStatement stmt = null;
        ++timesVisited;
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS)) 
        {
            String sql = "UPDATE REGISTER set TIMESVISITED = ?, LASTVISIT = ? WHERE SID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, timesVisited);
            stmt.setTimestamp(2, currentTime);
            stmt.setString(3, sID);
            System.out.println(stmt.executeUpdate() + " rows were impacted.");
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }
    static String addVisitDetails(String reasonForVisit, String sID, Timestamp currentTime)
    {
        PreparedStatement stmt = null;
        String status = "Visit details updation failed.";
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS))
        {
            String sql = "INSERT INTO VISITS (REASON, VISITDATE, STUDID) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, reasonForVisit);
            stmt.setTimestamp(2, currentTime);
            stmt.setString(3, sID);
            System.out.println(stmt.executeUpdate()+" rows were impacted.");
            status = "Visit details updated.";

        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return status;
    }
    static VisitDetails getVisitDetails(String sID) 
    {
        PreparedStatement stmt = null;
        VisitDetails vd = new VisitDetails(sID);
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS))
        {
            String sql = "SELECT * FROM VISITS WHERE studID=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sID);
            try (ResultSet rs = stmt.executeQuery()) 
            {
                while (rs.next()) 
                {
                    vd.getReasonForVisit().add(rs.getString("REASON"));
                    vd.getVisitDate().add(rs.getString("VISITDATE"));
                }
            }
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return vd;
    }
    static int getStudentByCategoryandMonth(String s,int m)
    {
        PreparedStatement stmt = null;
        int count=0;
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS))
        {
            String sql="SELECT COUNT(MONTH(VISITDATE)) as Total from VISITS where REASON =? AND month(VISITDATE)=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,s);
            stmt.setInt(2,m);
            try (ResultSet rs = stmt.executeQuery()) 
            {
                 rs.next();
                 count=rs.getInt("Total");
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
        return count;
    }
    static int getNumberOfRows(String s)
    {
        int count=0;
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS)) 
        {
            String sql = "SELECT COUNT(*) AS Total FROM ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, s); 
            try (ResultSet rs = stmt.executeQuery()) 
            {
                rs.next();
                count=rs.getInt("Total");
            }

        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
        return count;
    }
    static int getStudentByDateMale(String s,String from,String to) 
    {
        PreparedStatement stmt = null;
        int count=0;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS))
        {
            String sql = "SELECT COUNT(SID) AS TOTAL FROM REGISTER WHERE GENDER='FEMALE' AND SID IN (SELECT studID FROM VISITS WHERE REASON=? AND DATE(VISITDATE)>? AND DATE(VISITDATE)<?);";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, s);
            stmt.setString(2, from);
            stmt.setString(3, to);
            try (ResultSet rs = stmt.executeQuery()) 
            {
                 rs.next();
                 count=rs.getInt("Total");
            }

        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return count;
    }
    static int getStudentByDateFemale(String s,String from,String to)
    {
        PreparedStatement stmt = null;
        int count=0;
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS)) 
        {
            String sql = "SELECT COUNT(SID) AS TOTAL FROM REGISTER WHERE GENDER='FEMALE' AND SID IN (SELECT studID FROM VISITS WHERE REASON=? AND DATE(VISITDATE)>=? AND DATE(VISITDATE)<?);";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, s);
            stmt.setString(2, from);
            stmt.setString(3, to);
            try (ResultSet rs = stmt.executeQuery())
            {
                rs.next();
                count=rs.getInt("Total");
            }
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return count;
    }
}
