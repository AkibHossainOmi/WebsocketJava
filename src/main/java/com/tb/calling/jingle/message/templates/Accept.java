package com.tb.calling.jingle.message.templates;

public class Accept {

    public static String createMessage(String bpartyWithId, String bPartyWithoutId, String id) {

        // Define the URL where the REST request will be sent

        // Construct the XML payload
        String xmlPayload = String.format(
                """
                <message from="%s" to="%s" type="chat"><accept xmlns="urn:xmpp:jingle-message:0" id="%s"/><store xmlns="urn:xmpp:hints"/></message>
                """, bpartyWithId,bPartyWithoutId,id);

        return xmlPayload.trim();

    }

}

