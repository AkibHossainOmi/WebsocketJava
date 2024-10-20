package com.tb.calling.jingle.msgTemplates;


import com.tb.common.uniqueIdGenerator.ShortIdGenerator;

public class Ice {
    public static String createMessage( String apartyWithId, String bpartyWithId, String id, String deviceIp, int port) {


        // Construct the XML payload
        String xmlPayload = String.format(
                """
                        <iq to="%s" from="%s" type="set" id="%s">
                            <jingle action="transport-info" sid="%s" xmlns="urn:xmpp:jingle:1">
                                <content creator="initiator" name="0">
                                    <transport ufrag="UHHK" xmlns="urn:xmpp:jingle:transports:ice-udp:1" pwd="OlZzeiJbn2gZ7rvIjCufVzbE">
                                        <candidate foundation="22601511" protocol="udp" id="8ed2d5ff-5683-4760-b8ff-b66a5d52193c" ip="%s" component="1" type="host" generation="0" port="%d" priority="2122260223"/>
                                    </transport>
                                </content>
                            </jingle>
                        </iq>
                        """, apartyWithId, bpartyWithId, ShortIdGenerator.getNext()
                , id, deviceIp,port);
        return xmlPayload;
    }
}