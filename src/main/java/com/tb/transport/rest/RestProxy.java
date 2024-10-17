package com.tb.transport.rest;

import com.tb.common.ServiceEnum.PayloadType;
import com.tb.common.ServiceEnum.TransportPacket;
import com.tb.common.eventDriven.TransportListener;
import com.tb.common.eventDriven.Transport;
import com.tb.common.eventDriven.Payload;
import okhttp3.*;
import org.java_websocket.client.WebSocketClient;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class RestProxy implements Transport {
    private OkHttpClient client;
    private StringBuilder baseUrl;
    RestSettings settings;
    List<TransportListener> publicListeners =new CopyOnWriteArrayList<>();
    public RestProxy(RestSettings settings,
                          List<TransportListener> publicListeners,String baseUrl) {
        this.settings=settings;
        /*for (TransportListener publicListener : publicListeners) {
            this.publicListeners.add(publicListener);
        }*/
        //this.webSocketClient= createWebSocketClient(publicListeners);

    }
    public void connect(Transport transport) {
        this.client = new OkHttpClient();
    }

    public OkHttpClient getrestClient() {
        return this.client;
    }



    @Override
    public void addListener(TransportListener transportListener) {

    }

    @Override
    public void sendMessage(Payload payload) {
        List<TransportListener> listeners= this.publicListeners;
        RequestBody body = RequestBody.create(payload.getData(), MediaType.parse("text/xml; charset=utf-8"));

        // Build the HTTP request with the URL, headers, and the XML payload
        String url= "";
        if (payload.getUrlSuffix()==null || payload.getUrlSuffix().isEmpty()){
            url=this.baseUrl.toString();
        }else{
            url=this.baseUrl.append("/").append(payload.getUrlSuffix()).toString();
        }
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "text/xml")
                .post(body)
                .build();
        this.client.newCall(request).enqueue(new Callback() {
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
            }
        });

    }

}
