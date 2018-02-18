/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facerecognition;

import java.sql.Timestamp;

/**
 *
 * @author hp
 */
public class Student {

    private String sid;
    private String firstName;
    private String lastName;
    private String gender;
    private String track;
    private String imagePath;
    private int age;
    private int timesVisited;
    private java.sql.Timestamp lastVisit;
    public Student(String sid, String firstName, String lastName, String gender, String track, String imagePath, int age, int timesVisited, Timestamp lastVisit) 
    {
        this.sid = sid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.track = track;
        this.imagePath = imagePath;
        this.age = age;
        this.timesVisited = timesVisited;
        this.lastVisit = lastVisit;
    }
    public String getSid() 
    {
        return sid;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName() 
    {
        return lastName;
    }
    public String getGender()
    {
        return gender;
    }
    public String getTrack() 
    {
        return track;
    }
    public int getAge() 
    {
        return age;
    }
    public int getTimesVisited() 
    {
        return timesVisited;
    }
    public java.sql.Timestamp getLastVisit() 
    {
        return lastVisit;
    }
    public String getImagePath() 
    {
        return imagePath;
    }
}
