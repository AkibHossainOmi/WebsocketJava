package com.tb.calling.jingle.msgTemplates;

import okhttp3.*;

import java.io.IOException;

public class Proceed {

    // Reuse the OkHttpClient instance for all requests
    private static final OkHttpClient client = new OkHttpClient();

    public String createMessage(String server_ip,String A_party_with_id,String B_party_with_id, String id) {

            // Define the URL where the REST request will be sent
            String url = String.format("http://%s:5280/rest", server_ip);

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
                    """,B_party_with_id,id,A_party_with_id, id);

            return xmlPayload;
    }
}
