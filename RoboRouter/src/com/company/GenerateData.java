package com.company;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class GenerateData {



    /*
    METHOD      : Create_RoboRouterInfo
    PARAMETER   : List<String> AllRobobees - all robobee information as string.
    RETURN      : -
    DESCRIPTION : This method is used for generating text file about all robobees in this router.
     */
    public void Create_RoboRouterInfo(List<String> AllRobobees)
    {
        try{
            PrintWriter writer = new PrintWriter("RoboRouters.txt", "UTF-8");
            Writer output = new BufferedWriter(new FileWriter("RoboRouters.txt"));
            for(int i = 0; i< AllRobobees.size(); i++)
            {
                output.append(AllRobobees.get(i));
            }
            output.append('\n');
            output.close();
        }
        catch(Exception e)
        {
            System.out.println("FAILED TO CREATE FILLE ROBOROUTER : " + e.getMessage());
        }
    }


    /*
    METHOD      : Create_MissionInfoFile
    PARAMETER   : String str - the mssage detail information wants to create.
                  int missionNum - the number of the mission
    RETURN      : -
    DESCRIPTION : This method is used for creating Mission text file so that it can be shared inside of router.
     */
    public static void Create_MissionInfoFile(String str, int missionNum)
    {
        String fileName = "";
        fileName = "Mission_" + missionNum + ".txt";
        try
        {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true), "UTF-8"));
            writer.write(str);
        }catch(Exception e)
        {
            System.out.println("MISSION FILE CREATE FAIL. " + e.getMessage());
        }
    }


    /*
    METHOD          : Create_SendMsg_Main
    PARAMETER       : the number of the message.
    RETURN          : -
    DESCRIPTION     : This method is used for creating message of summary.
     */
    public void Create_SendMsg_Main(int msgNum)
    {
        Date date = new Date();
        try{
            PrintWriter writer = new PrintWriter("CurrentSummary" + msgNum + ".txt", "UTF-8");
            writer.write(RoboOP_Singleton.getInstance().roboMission.RoboRouterID+ "|"
                    + date.toString() + "|"
                    + RoboOP_Singleton.getInstance().roboMission.Status + "|");
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /*
    METHOD          : AllRobobeeLogs
    PARAMETER       : info - the string of the robobee infomration
    RETURN          : -
    DESCRIPTION     : this method is used for logging information about the robobees.
     */
    public static void AllRobobeeLogs(String info)
    {
        try
        {
            String filename= "AllRoboBEEsInfo.log";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            fw.write( info + "\n");//appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }



}
