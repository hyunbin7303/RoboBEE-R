/*
FILE            : RoboStatus.java
PROGRAMMER      : Kevin Park, Cavan Biggs
DATE            : 2018 - 12 - 10
PROGRAM         : NAD - FINAL
DESCRIPTION     : This java file has Robobee's status information and respond corresponding value.
 */



package com.company;


public enum RoboStatus
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