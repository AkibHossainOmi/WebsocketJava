package com.tb.calling.jingle.msgTemplates;

import okhttp3.*;

import java.io.IOException;

public class Proceed {

    // Reuse the OkHttpClient instance for all requests
    private static final OkHttpClient client = new OkHttpClient();

    public String createMessage(String serverIp,String apartyWithId,String bpartyWithId, String id) {

        // Define the URL where the REST request will be sent
        String url = String.format("http://%s:5280/rest", serverIp);

        // Construct the XML payload
        String xmlPayload = String.format(
                """
                <message from="%s" id="jm-proceed-%s" to="%S" type="chat">
                    <proceed
                        xmlns="urn:xmpp:jingle-message:0" id="%s">
                        <device
                            xmlns="http://gultsch.de/xmpp/drafts/omemo/dlts-srtp-verification" id="1285073097"/>
                    </proceed>
                        <store
                            xmlns="urn:xmpp:hints"/>
                </message>
                """,bpartyWithId,id,apartyWithId, id);

        return xmlPayload;
    }
}
