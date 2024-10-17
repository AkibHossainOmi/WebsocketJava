package com.tb.calling.jingle.msgTemplates;

import okhttp3.*;
import java.io.IOException;

public class Accept {

    // Reuse the OkHttpClient instance for all requests
    private static final OkHttpClient client = new OkHttpClient();

    public static void sendMessage(String server_ip,String B_party_with_id,String B_party_without_id, String id) {
        try {
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
