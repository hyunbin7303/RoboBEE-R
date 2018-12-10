package com.company;

public class RoboMission {
    public int RoboRouterID;
    public int numberOfBees;
    public int SearchAreaStart;
    public int SearchAreaEnd;
    public int SearchFindNew;


    public int Status;
    public String Description;
    public String DateTime;
    public String SecretToken;
    public String RouterLocation;
    public RoboMission(){};
    public void DisPlayRouterInfo()
    {
        System.out.println("ROBO ROUTER ID : " + RoboRouterID);
        System.out.println("NUM ROBOBEES : " + numberOfBees + " - Search Area Start : " + SearchAreaStart + " - Search Area End : " + SearchAreaEnd);
        System.out.println("DESCRIPTION : " + Description);
        System.out.println("STATUS : " + Status);
        System.out.println("SECRET TOKEN VALUE SETTING UP : " + SecretToken);
    }

}
