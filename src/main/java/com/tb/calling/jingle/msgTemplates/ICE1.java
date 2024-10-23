package com.tb.calling.jingle.msgTemplates;

import com.tb.common.uniqueIdGenerator.ShortIdGenerator;

public class ICE1 {
    public static String createMessage(String apartyWithId, String bpartyWithId, String sid, String pwd, String ufrag) {
        String xmlPayload = String.format(
                """
                <iq from="%s" to="%s" type="set" id="%s">
                    <jingle action="transport-info" sid="%s" xmlns="urn:xmpp:jingle:1">
                        <content creator="initiator" name="0">
                            <transport xmlns="urn:xmpp:jingle:transports:ice-udp:1" ufrag="%s" pwd="%s">
                                <candidate foundation="1" component="1" protocol="udp" priority="2130706431" ip="192.168.0.101" port="49152" type="host" generation="0" id="1"/>
                                <candidate foundation="2" component="1" protocol="udp" priority="1694498815" ip="203.0.113.1" port="49153" type="srflx" raddr="192.168.0.101" rport="49152" generation="0" id="2"/>
                                <fingerprint xmlns="urn:xmpp:jingle:apps:dtls:0" hash="sha-256" setup="active">
                                    36:83:11:24:F7:72:3C:8F:FF:F8:7F:4E:52:01:94:6F:40:47:AB:FA:9D:C8:85:E1:AA:3C:CC:10:93:8F:3F:7A
                                </fingerprint>
                                <trickle xmlns="http://gultsch.de/xmpp/drafts/jingle/transports/ice-udp/option"/>
                            </transport>
                        </content>
                    </jingle>
                </iq>
                """, bpartyWithId, apartyWithId, ShortIdGenerator.getNext(), sid, ufrag, pwd);
        return xmlPayload;
    }
}
