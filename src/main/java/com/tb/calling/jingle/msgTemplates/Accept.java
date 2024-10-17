package com.tb.calling.jingle.msgTemplates;

import okhttp3.*;
import java.io.IOException;

public class Accept {

    // Reuse the OkHttpClient instance for all requests
    private static final OkHttpClient client = new OkHttpClient();

    public String createMessage(String serverIp,String bpartyWithId,String bPartyWithoutId, String id) {

        // Define the URL where the REST request will be sent
        String url = String.format("http://%s:5280/rest", serverIp);

        // Construct the XML payload
        String xmlPayload = String.format(
                """
                   <message from="%s" to="%s" type="chat">
                        <accept
                            xmlns="urn:xmpp:jingle-message:0" id="%s"/>
                            <store
                                xmlns="urn:xmpp:hints"/>
                   </message>
                """, bpartyWithId,bPartyWithoutId,id);


        return xmlPayload;

    }

}
