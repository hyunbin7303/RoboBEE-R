package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("RoboClinet Starts!");
        InetAddress ip = InetAddress.getByName("localhost");
        System.out.println(ip.getHostName().toString());

        try{
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket(ip, 7000);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String msg = scanner.nextLine();
                        try{
                            dos.writeUTF(msg);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            Thread receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){

                        try{
                            String msg = dis.readUTF();
                            System.out.println(msg);

                        }catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });
            sendThread.start();
            receiveThread.start();

        }catch (IOException ioe)
        {

            ioe.printStackTrace();
        }
    }


}



