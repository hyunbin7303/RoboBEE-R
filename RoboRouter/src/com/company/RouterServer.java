package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouterServer {
    ServerSocket ss = null;
    RouterLog routerLog = null;
    DateFormat fordate = null;
    DateFormat fortime = null;
    List<RoboInfo> allroboBees;                 // All robobee information

    public void accessToAllRoboBEEs()
    {
        

    }



 //   private static int numAllReceives;          // the number of robobees will be run.
 //   private static int WholeSearchAreaStart;    // The thole search area start
 //   private static int WholeSearchAreaEnd;      // The whole Search area end.
 //   private static int SearchStartPointReset;   // The value of starting point reset. - this value will be reset all the times.

    public RouterServer(String ipAddress, int Port) throws IOException
    {
        Date date = new Date();
        fordate = new SimpleDateFormat("yyyy/MM/dd");
        fortime = new SimpleDateFormat("hh:mm:ss");
        routerLog = new RouterLog(); // Create logging.
        ss = new ServerSocket(Port);
        InetAddress inet = null;
        allroboBees = new ArrayList<RoboInfo>();
    }
    public RoboInfo CreateNewRoboBEE(int SearchStartArea, int SearchFinishArea)
    {
        // Create new RoboBEE
        RoboInfo newRobo = new RoboInfo();
        newRobo.roboID = allroboBees.size()+1;
        newRobo.IsActive = true;
        newRobo.DateTime = fordate.toString();
        // Search Area received from server.
        // Assuming that there will be 5 RoboBEEs.
        newRobo.SearchingArea = SearchStartArea;
        newRobo.SearchingAreaEnd = SearchFinishArea;
        return newRobo;
    }

    public boolean Running() throws IOException {
        while (true)
        {
            Socket soc = null;
            try
            {
                // socket object to receive incoming client requests
                soc = ss.accept();
                routerLog.WriteLogFile();
                System.out.println("A new RoboBEE Connected : " + soc);
                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(soc.getInputStream());
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
                System.out.println("Assigning new thread for this client");

                int SearchEndPoint = RoboOP_Singleton.getInstance().roboMission.SearchFindNew+ 20; // 20 is the boundary.
                RoboInfo newRobo = CreateNewRoboBEE(RoboOP_Singleton.getInstance().roboMission.SearchFindNew, SearchEndPoint);
                System.out.println("ROBOBEE CREATED : " + newRobo.roboID + " - " + newRobo.SearchingArea+ " - " + newRobo.SearchingAreaEnd);
                allroboBees.add(newRobo);

                RoboOP_Singleton.getInstance().roboMission.SearchFindNew =SearchEndPoint + 1; // Next Area to search setting.

                // Send Client information
                Thread t = new ClientHandler(soc, dis, dos, newRobo);
                // Invoking the start() method
                t.start();
                // Create New Thread to send information to RoboServer.
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }

            catch (Exception e){
                soc.close();
                e.printStackTrace();
            }
        }
    }




}

