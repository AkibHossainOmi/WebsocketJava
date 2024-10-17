package com.tb.calling.jingle.msgTemplates;

import okhttp3.*;
import java.io.IOException;

public class Ice {

    // Reuse the OkHttpClient instance for all requests
    private static final OkHttpClient client = new OkHttpClient();

    public static void sendMessage(String server_ip,String A_party_with_id,String B_party_with_id, String id, String device_ip) {
        try {
            // Define the URL where the REST request will be sent
            String url = String.format("http://%s:5280/rest", server_ip);

            // Construct the XML payload
            String xmlPayload = String.format(
                    """
                    <iq to="%s" from="%s" type="set" id="rE54FZrBFAKr">
                        <jingle action="transport-info" sid="%s" xmlns="urn:xmpp:jingle:1">
                            <content creator="initiator" name="0">
                                <transport ufrag="UHHK" xmlns="urn:xmpp:jingle:transports:ice-udp:1" pwd="OlZzeiJbn2gZ7rvIjCufVzbE">
                                    <candidate foundation="22601511" protocol="udp" id="8ed2d5ff-5683-4760-b8ff-b66a5d52193c" ip="%s" component="1" type="host" generation="0" port="58204" priority="2122260223"/>
                                </transport>
                            </content>
                        </jingle>
                    </iq>
                    """,A_party_with_id,B_party_with_id, id, device_ip);

            // Create request body with XML content
            RequestBody body = RequestBody.create(xmlPayload, MediaType.parse("text/xml; charset=utf-8"));

            // Build the HTTP request with the URL, headers, and the XML payload
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "text/xml")
                    .post(body)
                    .build();


            // Send the request asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Handle the error if the request fails
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Check if the response is successful
                    if (response.isSuccessful()) {
                        // Process the successful response
                        System.out.println("Response Code: " + response.code());
                        System.out.println("Response Body: " + response.body().string());
                    } else {
                        // Handle an error response
                        System.out.println("Error Response Code: " + response.code());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
