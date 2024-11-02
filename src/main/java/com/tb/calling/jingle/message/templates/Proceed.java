package com.tb.calling.jingle.message.templates;



public class Proceed {

    public static String createMessage(String apartyWithId, String bpartyWithId, String id) {

        String xmlPayload = String.format(
                """
                <message from="%s" id="jm-proceed-%s" to="%s" type="chat"><proceed xmlns="urn:xmpp:jingle-message:0" id="%s"><device xmlns="http://gultsch.de/xmpp/drafts/omemo/dlts-srtp-verification" id="1285073097"/></proceed><store xmlns="urn:xmpp:hints"/></message>
                """,bpartyWithId,id,apartyWithId, id);

        return xmlPayload.trim();
    }
}
