package com.tb.calling.jingle.message.templates;

public class Ringing {

    public static String createMessage(String from, String to, String id) {
        String xmlPayload = String.format(
                """
                <message type='chat' from='%s' to='%s'>
                    <ringing xmlns='urn:xmpp:jingle-message:0' id='%s'/>
                    <store xmlns='urn:xmpp:hints'/> 
                </message>
                """, from, to, id);

        return xmlPayload;
    }
}
