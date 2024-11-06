package com.tb.calling.jingle.msgTemplates;

import com.tb.common.UUIDGen;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;

public class Presence {
    public static String createMessage(String from, String to) {
        String xmlPayload = String.format(
                """
                <presence type="presence" from="%s" to="%s" xmlns="jabber:client">
                    <signal xmlns="http://jabber.org/protocol/caps"/>
                </presence>
                """, from, to);
        return xmlPayload;
    }
}
