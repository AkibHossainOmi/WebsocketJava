package com.tb.common;

import java.util.UUID;

public class UUIDGen {
    public static UUID getNext(){
        return UUID.randomUUID();
    }
    public static String getNextAsStr(){
        return UUID.randomUUID().toString();
    }
}
