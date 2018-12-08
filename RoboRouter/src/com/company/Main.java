/*
FILE            : Main.java
PROGRAMMER      : Kevin Park
DATE            : 2018 - 11 - 18
PROGRAM         : NAD - A04
DESCRIPTION     : This java file is the main file for each RoboBEE Router . it will connect to the RoboBEE.
 */


package com.company;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

// Server class
public class Main
{
 //   final static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) throws IOException
    {
        BasicConfigurator.configure();
        if(args.length == 0 )
        {
            System.out.println("ROUTER NEEDS SECRET CODE FOR ACTIVATION.");
            // TODO LOG
            return;
        }
        System.out.println("RECEIVED SECRET TOKEN VALUE = " + args[0]);
       // logger.info("ROBO ROUTER START");


        Date date = new Date();
        RoboMission roboMission = new RoboMission();
        roboMission.Description = "FIND CHILD";
        roboMission.SearchAreaStart = 1;
        roboMission.SearchAreaEnd = 100;
        roboMission.SearchFindNew = roboMission.SearchAreaStart;
        roboMission.SecretToken = "LEAGUE OF LEGENDS";
        roboMission.DateTime = date.toString();

        RoboOP_Singleton roboOP_singleton = RoboOP_Singleton.getInstance();
        roboOP_singleton.Info = "ROBOBEE ROUTER OPERATION SINGLETON TESTING";
        roboOP_singleton.roboMission = roboMission;     // Storing Robo Mission into Singleton.


        // Creating TCP IP Connection for connecting to server.
        RouterServer routerServer = new RouterServer("127.0.0.1", 5000);
        // Creating TCP IP Connection for connecting to main Infrastructure.
        RouterClient routerClient = new RouterClient("127.0.0.1", 4000);
        // Create while loof for checking.
        if(!routerServer.Running() && !routerClient.Running())
        {
            System.out.println("RUNNING FAIL.");
            // ERROR LOG NEED
        }
        System.out.printf("ROBO ROUTER EXIT.");
        //TODO LOG
    }
}
