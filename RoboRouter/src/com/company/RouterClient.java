/*
FILE            : RouterClient.java
PROGRAMMER      : Hyunbin Park
PROG            : NAD - FInal
DATE            : 10 December , 2018
DESCRIPTION     : This file is used for running the socket for router to connect with Robo building.
 */



package com.company;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RouterClient {

    private static String ipAddressMain;
    private static int portMain;
  //  private Socket socket;
    private Scanner scanner;
    private byte server[];
    public RouterClient()
    {
        System.out.println("ROUTER CLIENT ---");
    }
    public RouterClient(String ipaddress, int port)
    {
        ipAddressMain = ipaddress;
        portMain = port;


    }
    public static String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    public boolean Start() throws IOException
    {
        boolean MainBuildingOn = true;
        Socket socket= null;
        // Mainbuilding online checking. keep waiting.
        while(MainBuildingOn)
        {
            try{

                socket = new Socket(ipAddressMain, portMain);
                MainBuildingOn = false;
            }
            catch(ConnectException ce)
            {
                try{
                    System.out.println("KEEP WAITING MAIN INFRASTRUCTURE SIDE");
                    Thread.sleep(3000);
                }catch(InterruptedException ie)
                {
                    ie.printStackTrace();
                }
            }
        }
        DataOutputStream outToMain = new DataOutputStream(socket.getOutputStream());
        DataInputStream InToMain = new DataInputStream(socket.getInputStream());
        if(socket.isConnected()) // When the first time it connects, gave information from it.
        {
            // When the first time it connect, assigned robobee router name.
            // Received Default information.
            String roboRouterInfo = InToMain.readUTF();
            System.out.printf("GOT MESSAGE FROM BUILDING : " + roboRouterInfo);
            List<String> MsgEach = Arrays.asList(roboRouterInfo.split("\\|"));
            RoboOP_Singleton.getInstance().roboMission.RoboRouterID = Integer.parseInt(MsgEach.get(0));
            RoboOP_Singleton.getInstance().roboMission.DateTime = MsgEach.get(1);
            RoboOP_Singleton.getInstance().roboMission.SearchAreaStart = Integer.parseInt(MsgEach.get(2));
            RoboOP_Singleton.getInstance().roboMission.SearchAreaEnd = Integer.parseInt(MsgEach.get(3));
            RoboOP_Singleton.getInstance().roboMission.numberOfBees = Integer.parseInt(MsgEach.get(4));
            RoboOP_Singleton.getInstance().roboMission.Description = MsgEach.get(5);

            // Create RoboRouter Information as text file.
            GenerateData generateText = new GenerateData();
            generateText.Create_RoboRouterInfo(MsgEach);
        }

        Thread sendThread; // Thread For sending
        sendThread = new Thread(() -> {

            boolean IsMissionCompleted = false;

            System.out.println("COMMUNICATION START");
            while (true) {

                try{
                    String msg = "";
                    TimeUnit.SECONDS.sleep(7); // Sending Information every 7 seconds.

                    // Sending Router information
                    String readLines = readFileAsString("RoboRouters.txt");
                    System.out.printf("[CLIENT SIDE] READ RoboRouter.txt File : " + readLines + "\n");
                    outToMain.writeUTF(readLines);

                }

                // Error Handling
                catch(UTFDataFormatException utfEx)
                {
                    System.out.println("UTF DATA FORMAT PROBLEM.");
                    System.out.printf(utfEx.getMessage());
                    break;
                }
                catch( SocketException socEx)
                {
                    System.out.println("Socket Exception happened in the sending Thread : " + socEx.getMessage());
                    break;
                }
                catch (IOException e) {
                    System.out.println("Exception happen in the Sender thread. : " + e.getMessage());
                    e.printStackTrace();

                }
                catch(Exception ex)
                {
                    System.out.println("EXCEPTIOON HANDLING." + ex.getMessage());
                    ex.printStackTrace();

                }
            }
        });

        Thread receiveThread; // Thread For sending
        receiveThread = new Thread(() -> {

            int num = 1;
            try{
                while (true) {
                    try{

                        String msg = InToMain.readUTF();
                        System.out.println("GOT MESSAGE FROM SERVER SIDE : " + msg);
                        GenerateData.Create_MissionInfoFile(msg, num);
                        // got information from the main building.
                        List<String> MsgEach = Arrays.asList(msg.split("\\|"));
                        System.out.println("RECEVIED MESSAGE FROM MAIN BUILDING : " + msg + "\n");
                        
                        // Parsing Lines.

                    }
                    catch(UTFDataFormatException utfEx)
                    {
                        System.out.println("UTF DATA FORMAT PROBLEM.");
                        System.out.printf(utfEx.getMessage());
                        break;
                    }
                    catch( SocketException socEx)
                    {
                        System.out.println("Socket Exception happened in the Receiving Thread in RouterClinet " + socEx.getMessage());
                        socEx.printStackTrace();
                        break;
                    }
                    catch (IOException e) {
                        System.out.println("Exception happen in the Receiving thread in RouterClient : " + e.getMessage());
                        e.printStackTrace();
                    }
                    catch(Exception ex)
                    {
                        System.out.println("EXCEPTIOON HANDLING." + ex.getMessage());
                        ex.printStackTrace();

                    }
                }
            }
            catch(Exception e)
            {
                e.getMessage();

            }

        });
        receiveThread.start();
        sendThread.start();


        return true;


    }

}
