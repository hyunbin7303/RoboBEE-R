/*
FILE            : Main.java
PROGRAMMER      : Kevin Park
DATE            : 2018 - 11 - 18
PROGRAM         : NAD - A04
DESCRIPTION     : This java file is the main file for each RoboBEE. it basically has functionalities for finding and sending the data that they got.
 */



package com.company;
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
        int RoboBEE_RouterHOST = 5000;
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
                        System.out.println("KEPP WAITING SERVER SIDE");
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
                System.out.println("MY ROBOBEE name : " + robobee.ID);
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
                            if(robobee.status == RoboStatus.WAITING.Value)
                            {
                                System.out.println("[WAITING]");
                                dos.writeUTF("[WAITING]");
                            }
                            if(SearchingCounting == robobee.mySearchEnd)
                            {
                                System.out.println("SEARCHING DONE. Area Between [" + robobee.mySearchStart + " ~ " + robobee.mySearchEnd+"]");
                                dos.writeUTF("SEARCHING DONE. Area Between [" + robobee.mySearchStart + " ~ " + robobee.mySearchEnd +"]");
                                dos.writeUTF(robobee.ID + "|" + msg + RoboStatus.SEARCHING_END.Value);
                                robobee.status = RoboStatus.SEARCHING_END.Value;
                                break;
                            }
                            else {
                                dos.writeUTF(robobee.ID + "|" + msg + RoboStatus.SEARCHING.Value);
                                System.out.println("CHECK" + robobee.ID + "|" +RoboStatus.SEARCHING.Value);
                                SearchingCounting++;
                            }


                            if(SearchingCounting == OurTempGoal) // Meaning that Searching is Done.
                            {
                                IsMissionCompleted = true;
                             //   dos.writeUTF(robobee.ID  + "|" + RoboStatus.FIND.Value);
                             //   System.out.println(robobee.ID + "|" + RoboStatus.FIND.Value);
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
                        if(statusOrder == RoboStatus.RETURNING.Value) {

                        }
                        else if(statusOrder == RoboStatus.SEARCHING.Value)
                        {

                        }
                        else if(statusOrder == RoboStatus.WAITING.Value)
                        {

                        }
                        else if(statusOrder == RoboStatus.ERROR.Value)
                        {
                            System.out.println("ERROR HAPPEN IN THE ROBOBEE.");

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



