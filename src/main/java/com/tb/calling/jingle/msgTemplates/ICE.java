package com.tb.calling.jingle.msgTemplates;

import com.tb.common.UUIDGen;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;

public class ICE {
    public static String createMessage(String apartyWithId, String bpartyWithId, String sid, String pwd,String priority,String ip,int port, String ufrag) {
        String xmlPayload = String.format(
                """
                <iq from="%s" to="%s" type="set" id="%s">
                    <jingle action="transport-info" sid="%s" xmlns="urn:xmpp:jingle:1">
                        <content creator="initiator" name="0">
                            <transport xmlns="urn:xmpp:jingle:transports:ice-udp:1" ufrag="%s" pwd="%s">
                                <candidate foundation="1" component="1" protocol="udp" priority="%s" ip="%s" port="%d" type="host" generation="0" id="%s"/>
                            </transport>
                        </content>
                    </jingle>
                </iq>
                """, bpartyWithId, apartyWithId, ShortIdGenerator.getNext(), sid, ufrag, pwd,priority,ip,port, UUIDGen.getNextAsStr());
        return xmlPayload;
    }
}
