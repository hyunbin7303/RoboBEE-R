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
        Date date = new Date();

        // Need to receive this information from GUI.
        // Default mission setting


        RoboMission roboMission = new RoboMission();
        roboMission.numberOfBees = 5;
        roboMission.Description = "FIND CHILD";
        roboMission.SearchAreaStart = 1;
        roboMission.SearchAreaEnd = 100;
        roboMission.SearchFindNew = roboMission.SearchAreaStart;
        roboMission.SecretToken = "LEAGUE OF Lasdf";
        roboMission.DateTime = date.toString();
        roboMission.Status = RoboStatus.SEARCHING.Value;

        RoboOP_Singleton roboOP_singleton = RoboOP_Singleton.getInstance();
        roboOP_singleton.Info = "ROBOBEE ROUTER OPERATION SINGLETON TESTING";
        roboOP_singleton.roboMission = roboMission;     // Storing Robo Mission into Singleton.


        // Creating TCP IP Connection for connecting to server.
        RouterServer routerServer = new RouterServer("127.0.0.1", 5500);
        // Creating TCP IP Connection for connecting to main Infrastructure.
        RouterClient routerClient = new RouterClient("10.113.21.165", 3300);
        // Create while loof for checking.

        Runnable run1 = new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("CLIENT RUN");
                    routerClient.Start();

                }catch(IOException ioe)
                {
                    System.out.println("CLIENT ERROR");
                }
            }
        };



        Runnable run2 = new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("SERVER RUN ");
                    routerServer.Running();

                }catch(IOException ioe)
                {
                    System.out.println("IOE");
                }
            }
        };
        run1.run();
        run2.run();

        System.out.printf("ROBO ROUTER EXIT.");
        //TODO LOG
    }
}


class RouterServerTest implements Runnable
{
    // Creating TCP IP Connection for connecting to server.
    RouterServer routerServer;
    public RouterServerTest(){
        try {
            routerServer = new RouterServer("127.0.0.1", 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {

            routerServer.GenerateInfo();
            if(!routerServer.Running())
            {
                System.out.println("RUNNNING");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class RouterClientTest implements Runnable
{
    RouterClient routerClient;
    public RouterClientTest()
    {
        routerClient = new RouterClient("10.113.21.164", 2000);
    }
    @Override
    public void run() {
        try {
            if(!routerClient.Start())
            {
                System.out.println("ERROR RUNNING");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

