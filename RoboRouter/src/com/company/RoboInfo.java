package com.company;


import java.util.ArrayList;
import java.util.List;

class AllRoboBEEs {
    private static List<RoboInfo> robo = new ArrayList<RoboInfo>();
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
    public String Status;
    public String secretToken;
}

enum RoboStatus
{
    FIND(0),
    WAITING(1),
    SEARCHING(2),
    MOVING(3),
    RETURNING(4),
    ERROR(5);
    public final int Value;
    private RoboStatus(int value)
    {
        Value = value;
    }

}
