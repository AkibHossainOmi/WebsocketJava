package com.tb.calling.jingle.msgTemplates;

import okhttp3.*;
import java.io.IOException;

public class Accept {

    // Reuse the OkHttpClient instance for all requests
    private static final OkHttpClient client = new OkHttpClient();

    public String createMessage(String server_ip,String B_party_with_id,String B_party_without_id, String id) {

            // Define the URL where the REST request will be sent
            String url = String.format("http://%s:5280/rest", server_ip);

            // Construct the XML payload
            String xmlPayload = String.format(
                    """
                       <message from="%s" to="%s" type="chat">
                            <accept
                            	xmlns="urn:xmpp:jingle-message:0" id="%s"/>
                            	<store
                            		xmlns="urn:xmpp:hints"/>
                       </message>
                    """, B_party_with_id,B_party_without_id,id);


               return xmlPayload;

    }

}
