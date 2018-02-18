/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facerecognition;

import java.util.ArrayList;

/**
 *
 * @author sharathkashyap
 */
public class VisitDetails
{
    private String sID;
    private ArrayList<String> reasonForVisit = new ArrayList<>();
    private ArrayList<String> visitDate = new ArrayList<>();
    public VisitDetails(String sID) 
    {
        this.sID = sID;
        this.reasonForVisit = reasonForVisit;
        this.visitDate = visitDate;
    }
    public String getsID()
    {
        return sID;
    }
    public ArrayList<String> getReasonForVisit() 
    {
        return reasonForVisit;
    }
    public ArrayList<String> getVisitDate()
    {
        return visitDate;
    }    
}
