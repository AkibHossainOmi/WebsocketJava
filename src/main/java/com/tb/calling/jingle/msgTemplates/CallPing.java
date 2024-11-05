package com.tb.calling.jingle.msgTemplates;

public class CallPing {

    public static String createMessage(String apartyWithId, String bpartyWithOutId, String id) {
        String xmlPayload = String.format(
                """
                    <message from='%s' type='chat' to='' id='jm-propose-"%s"'><propose xmlns='urn:xmpp:jingle-message:0' id='"%s"'><description media='audio' xmlns='urn:xmpp:jingle:apps:rtp:1' callping='true'/></propose><request xmlns='urn:xmpp:receipts'/><store xmlns='urn:xmpp:hints'/></message>
                                        
                """, apartyWithId,bpartyWithOutId,id,id);

        return xmlPayload;
    }
}
