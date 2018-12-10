/*
FILE        : RouterServer
PROGRAMMER  : Kevin Park
DATE        : 10th December, 2018
PROG        : NAD- FINAL
DESCRIPTION : THis file is the server of the Robobee Router.
            It controlls all Robobee threads.

 */


package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RouterServer {
    ServerSocket ss = null;
    RouterLog routerLog = null;

    DataInputStream dis = null;
    DataOutputStream dos = null;
    List<RoboInfo> allroboBees;                 // All robobee
    AllRoboBEEs allRoboBEEs = new AllRoboBEEs();

    public RouterServer(String ipAddress, int Port) throws IOException
    {
        Date date = new Date();
        routerLog = new RouterLog(); // Create logging.
        ss = new ServerSocket(Port);
        InetAddress inet = null;
        allroboBees = new ArrayList<RoboInfo>();



    }

    /*
    METHOD          : CreateNewRoboBEE
    PARAMETER       : SearchStartArea - the area of searching
                      SearchFinishArea - The area of searching End
    RETURN          : RoboInfo struct - has robobee information that just created.
    DESCRIPTION     : THis method is used for creating new robobee information when the first time it connects.
     */
    public RoboInfo CreateNewRoboBEE(int SearchStartArea, int SearchFinishArea)
    {
        Date date = new Date();
        // Create new RoboBEE
        RoboInfo newRobo = new RoboInfo();
        newRobo.roboID = allroboBees.size()+1; // Generating robobee ID value.
        newRobo.IsActive = true;
        newRobo.DateTime = date.toString();
        // Search Area received from server.
        // Assuming that there will be 5 RoboBEEs.
        newRobo.SearchingArea = SearchStartArea;
        newRobo.SearchingAreaEnd = SearchFinishArea;
        newRobo.Status = RoboStatus.SEARCHING.Value;    // The default setting is Searching.
        return newRobo;
    }

    // METHOD       :GenerateInfo
    // PARAMETER    : -
    // RETURN       : -
    // Description : Generating all robobee information as text file.
    public void GenerateInfo()
    {
        GenerateData generateData = new GenerateData();
        List<String> roboRouterList = new ArrayList<>();
        Date date = new Date();
        // All robobees in this router. // Generate as text file.
        for (int i = 0; i < allroboBees.size(); i++)
        {
            String routerOneStr = allroboBees.get(i).roboID + " - " + date.toString() + " - Searching Area : ["
                                + allroboBees.get(i).SearchingArea + " - " + allroboBees.get(i).SearchingAreaEnd + "] - Status : "
                                + allroboBees.get(i).Status + "\n";
            roboRouterList.add(routerOneStr);
        }
        generateData.Create_RoboRouterInfo(roboRouterList);
    }

    // METHOD       : Running
    // PARAMETER    : -
    // RETURN       : Boolean - true (Valid)
    // DESCRIPTION  : This method is used for running all robobees in the Threads.
    public boolean Running() throws IOException {

        Timer time = new Timer();
        while (true)
        {
            System.out.println("WAITING IN THE SERVER SIDE . ");
            Socket soc = null;
            ss.setSoTimeout(7000);
            try
            {


                // socket object to receive incoming client requests
                soc = ss.accept();
               // routerLog.WriteLogFile();
                System.out.println("A new RoboBEE Connected : " + soc);
                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(soc.getInputStream());
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
                System.out.println("Assigning new thread for this client");

                int SearchEndPoint = RoboOP_Singleton.getInstance().roboMission.SearchFindNew+ 20; // 20 is the boundary.
                RoboInfo newRobo = CreateNewRoboBEE(RoboOP_Singleton.getInstance().roboMission.SearchFindNew, SearchEndPoint);
                System.out.println("ROBOBEE CREATED : " + newRobo.roboID + " - " + newRobo.SearchingArea + " - " + newRobo.SearchingAreaEnd);
                allroboBees.add(newRobo);
                AllRoboBEEs.robo.add(newRobo);
                RoboOP_Singleton.getInstance().roboMission.SearchFindNew =SearchEndPoint + 1; // Next Area to search setting.

                // Send Client information
                Thread t = new ClientHandler(soc, dis, dos, newRobo);
                // Invoking the start() method
                t.start();
                // Create New Thread to send information to RoboServer.


                if(RoboOP_Singleton.getInstance().roboMission.Status == RoboStatus.FIND.Value)
                {
                    /*
                    System.out.println("ALL ROBOBEE OFF.");
                    for (int i = 0; i<allroboBees.size(); i++)
                    {
                        allroboBees.get(i).Status = RoboStatus.EXIT.Value;
                        dos.writeUTF("HELLO");
                    }*/
                }
                // Used for Generating information every 5 seconds.
                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // Generating File.
                        System.out.println("GENERATE INFORMATION -");
                        GenerateInfo();
                    }
                }, 0, 5500);

            }

            catch (java.io.InterruptedIOException interrupt)
            {

                System.out.println("TIME OUT! ");
                break;
            }
            catch(SocketException socex)
            {
                System.out.println("Socket Exception Happen in the RoboRouter Server Side. ");
                socex.printStackTrace();
                return false;
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            } catch (Exception e){
                soc.close();
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }




}

