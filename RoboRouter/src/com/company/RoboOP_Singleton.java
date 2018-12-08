package com.company;



public class RoboOP_Singleton {

    private static RoboOP_Singleton INSTANCE;
    public RoboMission roboMission;
    public String Info;
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
