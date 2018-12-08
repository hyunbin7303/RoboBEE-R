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
            dos.writeUTF(roboInfo.roboID + "|" + roboInfo.SearchingArea + "|" + roboInfo.SearchingAreaEnd + "|" + 10);
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
                received = dis.readUTF();
                System.out.println("MESSAGE RECEIVED FROM ROBOBEE : " + received);
                if (received.contains("[DONE]")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    dos.writeUTF("[EXIT ROBOBEE]");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }


                List<String> MsgEach = Arrays.asList(received.split("|"));
                int StatusOfRoboBEE = Integer.parseInt(MsgEach.get(3)); // status index value from robobee.

                if(StatusOfRoboBEE == RoboStatus.FIND.Value)
                {

                }

               /*
                if(StatusOfRoboBEE == RoboStatus.WAITING.Value)
                {

                }
                else if(StatusOfRoboBEE == RoboStatus.SEARCHING.Value)
                {

                }
                else if(StatusOfRoboBEE == RoboStatus.FIND.Value)
                {

                }*/

                // creating Date object
                Date date = new Date();

                // Current roboBee information.
                dos.writeUTF(roboInfo.roboID + "|" + roboInfo.SearchingArea + "|" + roboInfo.SearchingAreaEnd + "|" + roboInfo.Status);

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


}