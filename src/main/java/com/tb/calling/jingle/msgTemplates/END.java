package com.tb.calling.jingle.msgTemplates;

import com.tb.common.UUIDGen;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;

public class END {
    public static String createMessage(String apartyWithId, String bpartyWithId, String sid) {
        String xmlPayload = String.format(
                """
                   <iq to='%s' type='set' from='%s' id='%s'><jingle sid='%s' xmlns='urn:xmpp:jingle:1' action='session-terminate'><reason><success/></reason></jingle></iq>
                                        
                """, apartyWithId,bpartyWithId,  ShortIdGenerator.getNext(), sid);
        return xmlPayload;
    }
}
