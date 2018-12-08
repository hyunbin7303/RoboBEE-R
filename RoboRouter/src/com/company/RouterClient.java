package com.company;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class RouterClient {

    private static String ipAddressMain;
    private static int portMain;


    public RouterClient()
    {
        System.out.println("ROUTER CLIENT ---");
    }
    public RouterClient(String ipaddress, int port)
    {
        ipAddressMain = ipaddress;
        portMain = port;
    }

    public boolean Running() throws IOException {
        while (true)
        {
            Socket soc = null;
            Scanner scanner = new Scanner(System.in);
            try
            {

                Thread.sleep(2000);
                soc = new Socket(ipAddressMain, portMain);
                DataInputStream dis = new DataInputStream(soc.getInputStream());
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
                System.out.println("Assigning new thread for this client");
                if(soc.isConnected())
                {
                    System.out.println("Connected");
                }
                else{

                }
                Date date = new Date();
                // Send Client information
                //Thread t = new ClientHandler(soc, dis, dos, newRobo);
                // Invoking the start() method
                //t.start();
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
