package com.tb.calling.jingle.msgTemplates;

import com.tb.common.UUIDGen;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;

public class FINISH {
    public static String createMessage(String apartyWithId, String bpartyWithId, String sid) {
        String xmlPayload = String.format(
                """
                   <message type='chat' from='%s' to='%s'><finish xmlns='urn:xmpp:jingle-message:0' id='%s'><reason xmlns='urn:xmpp:jingle:1'><success/></reason></finish><store xmlns='urn:xmpp:hints'/></message>                     
                """, bpartyWithId,apartyWithId,sid);
        return xmlPayload;
    }
}
