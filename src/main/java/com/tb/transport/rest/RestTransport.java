package com.tb.transport.rest;

import com.tb.common.ServiceEnum.TransportPacket;
import com.tb.common.eventDriven.TransportListener;
import com.tb.transport.Transport;
import com.tb.common.eventDriven.Payload;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class RestTransport implements Transport {
    private OkHttpClient client;
    private StringBuilder baseUrl;
    RestSettings settings;
    List<TransportListener> publicListeners =new CopyOnWriteArrayList<>();
    public RestTransport(RestSettings settings,
                         List<TransportListener> publicListeners, String baseUrl) {
        this.settings=settings;
        this.baseUrl= new StringBuilder(baseUrl);
        for (TransportListener publicListener : publicListeners) {
            this.publicListeners.add(publicListener);
        }
    }
    public void connectOrInit() {
        this.client = new OkHttpClient();
        /*this.client= new OkHttpClient.Builder()
                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                .build();*/
    }

    @Override
    public void addListener(TransportListener publicListener) {
        this.publicListeners.add(publicListener);
    }

    @Override
    public void sendMessage(Payload payload) {
        List<TransportListener> listeners= this.publicListeners;

        // Build the HTTP request with the URL, headers, and the XML payload
        String url= "";
        if (payload.getUrlSuffix()==null || payload.getUrlSuffix().isEmpty()){
            url=this.baseUrl.toString();
        }else{
            url=this.baseUrl.append("/").append(payload.getUrlSuffix()).toString();
        }
//        RequestBody body = RequestBody.create(payload.getData(), MediaType.parse("text/xml; charset=utf-8"));
        RequestBody body = RequestBody.create(payload.getData().trim().getBytes(StandardCharsets.UTF_8),
                MediaType.parse("text/xml"));
        /*Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "text/xml")
                .post(body)
                .build();*/
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", "curl/7.74.0")  // Add the User-Agent header
                .addHeader("Accept", "*/*")               // Add Accept header
                .addHeader("Content-Type", "text/xml")    // Ensure this header is correct
                .build();

        try {
            // Synchronously execute the request
            Response response = this.client.newCall(request).execute();

            // Check if the response is successful
            if (response.isSuccessful()) {
                // Notify listeners with the response payload
                for (TransportListener listener : listeners) {
                    listener.onTransportMessage(new Payload(UUID.randomUUID().toString(),
                            response.body().string(), TransportPacket.Payload));
                }
                System.out.println("REST message sent...");
            } else {
                // Handle unsuccessful response
                System.err.println("Request failed: " + response.code());
                for (TransportListener listener : listeners) {
                    listener.onTransportError(new Payload(UUID.randomUUID().toString(),
                            "Error: " + response.code(), TransportPacket.TransportError));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Notify listeners in case of failure
            for (TransportListener listener : listeners) {
                listener.onTransportError(new Payload(UUID.randomUUID().toString(),
                        e.getMessage(), TransportPacket.TransportError));
            }
        }

        /*this.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                for (TransportListener listener : listeners) {
                    listener.onTransportError(new Payload(UUID.randomUUID().toString(),
                            e.getMessage(), TransportPacket.TransportError));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Check if the response is successful
                for (TransportListener listener : listeners) {
                    listener.onTransportMessage(new Payload(UUID.randomUUID().toString(),
                            response.body().toString(), TransportPacket.Payload));
                }
                System.out.println("rest msg sent...");
            }
        });*/

    }

}
