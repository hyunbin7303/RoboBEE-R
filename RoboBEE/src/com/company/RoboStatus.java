package com.company;


public enum RoboStatus
{
    FIND(0),
    WAITING(1),
    SEARCHING(2),
    SEARCHING_END(3),
    RETURNING(4),
    ERROR(5);
    public final int Value;
    private RoboStatus(int value)
    {
        Value = value;
    }

}