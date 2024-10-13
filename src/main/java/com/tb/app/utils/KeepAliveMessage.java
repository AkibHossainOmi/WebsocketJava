//package com.tb.app.utils;
//
//
//import com.tb.app.Websocket;
//
//import java.util.Random;
//public  class KeepAliveMessage  {
//
//    public static Websocket websocket;
//    public static void setWebsocket(Websocket websocket)
//    {
//        KeepAliveMessage.websocket =websocket;
//    }
//
//    private String TID()
//    {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        int length = 12;
//        Random random = new Random();
//        String transactionID = new String();
//        for (int i = 0; i < length; i++) {
//            int index = random.nextInt(characters.length());
//            transactionID+=(characters.charAt(index));
//        }
//        return transactionID;
//    }
//
//}
