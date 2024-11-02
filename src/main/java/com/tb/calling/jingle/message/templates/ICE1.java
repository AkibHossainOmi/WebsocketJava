package com.tb.calling.jingle.message.templates;

import com.tb.common.UUIDGen;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;

public class ICE1 {
    public static String createMessage(String apartyWithId, String bpartyWithId, String sid, String pwd, String ufrag) {
        String xmlPayload = String.format(
                """
                <iq from="%s" to="%s" type="set" id="%s">
                    <jingle action="transport-info" sid="%s" xmlns="urn:xmpp:jingle:1">
                        <content creator="initiator" name="0">
                            <transport xmlns="urn:xmpp:jingle:transports:ice-udp:1" ufrag="%s" pwd="%s">
                                <candidate foundation="1" component="1" protocol="udp" priority="2130706431" ip="192.168.0.101" port="49152" type="host" generation="0" id="%s"/>
                            </transport>
                        </content>
                    </jingle>
                </iq>
                """, bpartyWithId, apartyWithId, ShortIdGenerator.getNext(), sid, ufrag, pwd, UUIDGen.getNextAsStr());
        return xmlPayload;
    }
}
