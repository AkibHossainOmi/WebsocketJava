package com.tb.calling.jingle.msgTemplates;

import com.tb.common.uniqueIdGenerator.ShortIdGenerator;

public class ICE2 {
    public static String createMessage(String apartyWithId, String bpartyWithId, String sid, String pwd, String ufrag) {
        String xmlPayload = String.format(
                """
                <iq from="%s" to="%s" type="set" id="%s">
                    <jingle action="transport-info" sid="%s" xmlns="urn:xmpp:jingle:1">
                        <content creator="initiator" name="0">
                            <transport xmlns="urn:xmpp:jingle:transports:ice-udp:1" ufrag="%s" pwd="%s">
                                <candidate foundation="2" component="1" protocol="udp" priority="1694498815" ip="203.0.113.2" port="49155" type="srflx" raddr="192.168.0.102" rport="49154" generation="0" id="4"/>
                            </transport>
                        </content>
                    </jingle>
                </iq>
                """, bpartyWithId, apartyWithId, ShortIdGenerator.getNext(), sid, ufrag, pwd);
        return xmlPayload;
    }
}
