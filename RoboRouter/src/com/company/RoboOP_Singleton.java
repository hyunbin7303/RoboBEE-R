/*
FILE        : RoboOP_Singleton
PROGRAMMER  : Kevin Park
DATE        : 10th December, 2018
PROG        : NAD- FINAL
DESCRIPTION : THis file is singleton class which has robo mission information.



 */


package com.company;



public class RoboOP_Singleton {

    private static RoboOP_Singleton INSTANCE;
    public RoboMission roboMission;
    public String Info;
    public int RoboBee_Boundary;

    private RoboOP_Singleton()
    {
        System.out.println("SINGLETON ON");
    }
    public static synchronized RoboOP_Singleton getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new RoboOP_Singleton();
        }
        return INSTANCE;
    }


}
