/*
FILE        : ClientHandler
PROGRAMMER  : Kevin Park
DATE        : 10th December, 2018
PROG        : NAD- FINAL
DESCRIPTION : THis file is the clientHandler for individual robobee in the robo Router. multi-thread.
 */


package com.company;
import jdk.jshell.Snippet;
import org.apache.log4j.varia.ExternallyRolledFileAppender;

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

/*
CLASS : ClientHandler
Description : This handler is used for passing data to the Each robobee.
 */
public class ClientHandler extends Thread
{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    final RoboInfo roboInfo;
    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, RoboInfo roboInfo)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.roboInfo = roboInfo;
        try{

            //RoboBEE Setting UP.
            dos.writeUTF(roboInfo.roboID + "|" + roboInfo.SearchingArea + "|" + roboInfo.SearchingAreaEnd + "|" + RoboStatus.SEARCHING.Value);
        }catch(Exception e)
        {
            System.out.println("Fail to create Client Handler. " + e.getMessage());
        }
    }


    @Override
    public void run()
    {
        String received = "";
        String SendMessage = "";
        while (!this.isInterrupted())
        {
            try {

                // Reading Message Protocol
                received = dis.readUTF();
                GenerateData.AllRobobeeLogs(received);
                System.out.println("MESSAGE RECEIVED FROM ROBOBEE : " + received);
                List<String> MsgEach = Arrays.asList(received.split("\\|"));
                int StatusOfRoboBEE = 0;
                if(isNumeric(MsgEach.get(3)))
                {
                    StatusOfRoboBEE = Integer.parseInt(MsgEach.get(3));
                }
                if(StatusOfRoboBEE == RoboStatus.FIND.Value)
                {
                    System.out.println("ROBOBEE " + this.s + "SENDS EXIX...");
                    // Change the whole RoboBee router system to [FIND] Protocol
                    RoboOP_Singleton.getInstance().roboMission.Status = RoboStatus.FIND.Value;
                    dos.writeUTF("[EXIT ROBOBEE]");
                    System.out.println("CLOSING CONNECTION : " + this.s);
                    this.s.close();
                    break;
                }
                else if(StatusOfRoboBEE == RoboStatus.SEARCHING.Value)
                {
                    System.out.println("SEARCHING IN PROGRASS..\n");
                }
                else if(StatusOfRoboBEE == RoboStatus.SEARCHING_END.Value)
                {
                    System.out.println("MESSAGE RECEIVED : SEARCHING END.\n");
                    dos.writeUTF(roboInfo.roboID + "|" + roboInfo.SearchingArea + "|" + roboInfo.SearchingAreaEnd + "|" + RoboStatus.EXIT.Value);
                }
                else
                {
                    dos.writeUTF(roboInfo.roboID + "|" + roboInfo.SearchingArea + "|" + roboInfo.SearchingAreaEnd + "|" + RoboStatus.WAITING.Value);
                }



            }catch (SocketException socEx){
                socEx.printStackTrace();
                break;

            } catch(EOFException eof) {
                System.out.println("ERROR : GETTING OUT OF ROUTER!");
                eof.getMessage();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;

            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();
            this.s.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}