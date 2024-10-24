package com.tb.calling.jingle.msgTemplates;

public class ProposeResponse {

    public static String createMessage(String from, String to, String id) {
        String xmlPayload = String.format(
                """
                <message type='chat' to='%s' from='%s'>
                    <received xmlns='urn:xmpp:receipts' id='%s'/>
                    <store xmlns='urn:xmpp:hints'/>
                </message>
                """, to, from, id);

        return xmlPayload;
    }
}

