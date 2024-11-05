package com.tb.calling.jingle.msgTemplates;

public class RETRACT {
    public static String createMessage(String apartyWithOutId, String bpartyWithId, String id) {
        String xmlPayload = String.format(
                """
                    <message from='%s'
                                     to='%s'
                                     type='chat'>
                              <retract xmlns='urn:xmpp:jingle-message:0' id='%s'>
                                <reason xmlns="urn:xmpp:jingle:1">
                                  <cancel/>
                                  <text>Retracted</text>
                                </reason>
                              </retract>
                              <store xmlns="urn:xmpp:hints"/>
                            </message>
                """, bpartyWithId,apartyWithOutId,id);
        return xmlPayload;
    }
}
