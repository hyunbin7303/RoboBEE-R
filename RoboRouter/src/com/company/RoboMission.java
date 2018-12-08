package com.company;

public class RoboMission {
    public int numberOfBees;
    public int SearchAreaStart;
    public int SearchAreaEnd;
    public int SearchFindNew;
    public String Description;
    public String DateTime;
    public String SecretToken;

    public RoboMission(){};
    public void DisPlayRouterInfo()
    {
        System.out.println("NUM ROBOBEES : " + numberOfBees + " - Search Area Start : " + SearchAreaStart + " - Search Area End : " + SearchAreaEnd);
        System.out.println("DESCRIPTION : ");
        System.out.println("SECRET TOKEN VALUE SETTING UP : " + SecretToken);
    }

}
