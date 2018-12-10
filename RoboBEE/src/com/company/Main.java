/*
FILE            : Main.java
PROGRAMMER      : Kevin Park, CavanBiggs
DATE            : 2018 - 12 - 10
PROGRAM         : NAD - FINAL
DESCRIPTION     : This java file is the main file for each RoboBEE. it basically has all functionalities for finding and sending the data that they got.
 */



package com.company;
import com.sun.jdi.Value;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    static Random randnum;
    private static int OurTempGoal;
    public int random(int i){
        return randnum.nextInt(i);
    }

    public static void main(String[] args) throws IOException {
        OurTempGoal = 75;
    //    if(args.length < 1) return;
     //   String RoboBEE_name = args[0];
        RoboBee robobee = new RoboBee();
        int RoboBEE_RouterHOST = 5500;
        randnum = new Random();

        System.out.println("RoboClient Starts!");
        InetAddress ip = InetAddress.getByName("localhost");
        System.out.println(ip.getHostName());
        // creating Date object
        DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
        boolean ScanRouter = true;
        try{

            Socket socket = null;
            // RoboBEE keeps waiting.
            while(ScanRouter)
            {
                try{
                    socket = new Socket(ip, RoboBEE_RouterHOST);
                    ScanRouter = false;

                }
                catch(ConnectException ce)
                {
                    try{
                        System.out.println("KEEP WAITING SERVER SIDE");
                        Thread.sleep(2000);
                    }catch(InterruptedException ie)
                    {
                        ie.printStackTrace();
                    }
                }
            }

            DataInputStream dis =  new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            // When the socket is connected, get the roboBee number.

            // Socket Connection.
            if(socket.isConnected())
            {
                // Sending Message Format
                // RoboBEE ID|StartSearchinArea(Int)|StartingSearchingArea(Int)|Description|
                String MsgFromRouter = dis.readUTF();
                System.out.println("RECEIVED MSG protocol : " + MsgFromRouter);
                List<String> MsgEach = Arrays.asList(MsgFromRouter.split("\\|"));
                // got information from objects.
                robobee.ID            = Integer.parseInt(MsgEach.get(0));
                robobee.mySearchStart = Integer.parseInt(MsgEach.get(1));
                robobee.mySearchEnd   = Integer.parseInt(MsgEach.get(2));
                robobee.SecretAccessValue = "SECRET_TEMP";
                robobee.status = RoboStatus.SEARCHING.Value;
                // TODO roboBee name validation checking
            }
            else{
                System.out.println("NOT CONNECTED. SERVER Alive Checking.");
            }

            Date date = new Date();
            Thread sendThread; // Thread For sending
            sendThread = new Thread(() -> {
                int SearchingCounting = robobee.mySearchStart;
                boolean IsMissionCompleted = false;
                System.out.println("COMMUNICATION START");
                while (true) {

                    int order = randnum.nextInt(10);
                    try{
                        String dateStr = fordate.format(date);
                        String msg = "";
                        TimeUnit.SECONDS.sleep(3);
                        msg = date.toString() + "|" +SearchingCounting + "|";

                        if(IsMissionCompleted)
                        {
                            dos.writeUTF(robobee.ID  + "|" + msg + RoboStatus.FIND.Value);
                            System.out.println(robobee.ID + "|" + msg + RoboStatus.FIND.Value);
                            break;
                        }
                        else
                        {
                            // Robobee Msg protocol Order : Status
                            if(robobee.status == RoboStatus.WAITING.Value)
                            {
                                System.out.println("[WAITING]");
                                dos.writeUTF(robobee.ID + "|" + msg + RoboStatus.WAITING.Value); // Send message that Robobee is currently waiting.
                            }
                            else if(robobee.status == RoboStatus.EXIT.Value)
                            {
                                System.out.println("One of robobees found Objective. Exit this robobee.");
                                robobee.status = RoboStatus.EXIT.Value;
                            }
                            else if(SearchingCounting == robobee.mySearchEnd)
                            {
                                System.out.println("SEARCHING DONE. Area Between [" + robobee.mySearchStart + " ~ " + robobee.mySearchEnd+"]");
                                robobee.status = RoboStatus.SEARCHING_END.Value;
                                dos.writeUTF(robobee.ID + "|" + msg + robobee.status);
                                break;
                            }
                            else {
                                dos.writeUTF(robobee.ID + "|" + msg + RoboStatus.SEARCHING.Value);
                                System.out.println("SENDING MESSAGE : " + robobee.ID + "|" + msg + RoboStatus.SEARCHING.Value);
                                SearchingCounting++;
                            }
                            if(SearchingCounting == OurTempGoal) // Meaning that Searching is Done.
                            {
                                IsMissionCompleted = true;
                                dos.writeUTF(robobee.ID  + "|" + msg + RoboStatus.FIND.Value);
                                System.out.println(robobee.ID + "|" + msg + RoboStatus.FIND.Value);
                            }
                        }
                    }
                    catch(UTFDataFormatException utfEx)
                    {
                        System.out.println("UTF DATA FORMAT PROBLEM.");
                        System.out.printf(utfEx.getMessage());
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


            // Getting order from the router.
            Thread receiveThread = new Thread(() -> {
                while(true){
                    try{
                        String msg = dis.readUTF();
                        if(msg.contains("[EXIT ROBOBEE]"))
                        {
                            System.out.println("MESSAGE FROM ROUTER : Exist Robobee.");
                            break;
                        }
                        List<String> MSGprotocol = Arrays.asList(msg.split("\\|"));
                        System.out.println("RECEIVED MESSAGE : " + msg); // Verifying message protocol.

                        int statusOrder = Integer.parseInt(MSGprotocol.get(3));
                        System.out.println("STATUS PRODER VALUE : " + statusOrder);
                        if(statusOrder == RoboStatus.RETURNING.Value) {
                            System.out.println("RECEIVED STATUS ORDER : RETURNING");
                        }
                        else if(statusOrder == RoboStatus.SEARCHING.Value)
                        {
                            System.out.println("RECEIVED STATUS ORDER : SEARCHING");
                        }
                        else if(statusOrder == RoboStatus.WAITING.Value)
                        {
                        }
                        else if(statusOrder == RoboStatus.EXIT.Value)
                        {
                            System.out.println("GOT MESSAGE FROM ROUTER: EXIT THIS ROBOBEE");
                            break;
                        }
                    }
                    catch(EOFException eof)
                    {
                        System.out.println("RECEIVING PROPER MESSAGE ERROR. ");
                        System.out.println("Exception happen during the reading UTF( Receiving THREAD)." + eof.getMessage());
                        break;
                    }
                    catch (IOException e)
                    {
                        System.out.println("Exception Happen in the Receiver Thread. : " + e.getMessage());
                        e.printStackTrace();
                        break;
                    }
                }
            });
            sendThread.start();
            receiveThread.start();



        }
        catch(ConnectException ce)
        {
            System.out.println("Connection Fail. Keep Searching Robobee Router.");

        }
        catch(SocketException exce)
        {
            System.out.println("Can't Connect");
            // Generate Log file.
        }
        catch (IOException ioe)
        {

            ioe.printStackTrace();
        }

    }


}



