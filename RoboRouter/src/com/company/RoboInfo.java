package com.company;


import java.util.ArrayList;
import java.util.List;

class AllRoboBEEs {
    public static List<RoboInfo> robo = new ArrayList<RoboInfo>();
    public int getTotalSize()
    {
        return robo.size();
    }
}

public class RoboInfo {
    public int roboID;
    public int SearchingArea;
    public int SearchingAreaEnd;
    public String DateTime;
    public boolean IsActive;
    public int Status;
    public String secretToken;
}

enum RoboStatus
{
    FIND(0),
    WAITING(1),
    SEARCHING(2),
    SEARCHING_END(3),
    RETURNING(4),
    EXIT(5),
    ERROR(6);
    public final int Value;
    private RoboStatus(int value)
    {
        Value = value;
    }

}
