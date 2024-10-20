package com.tb.common.eventDriven.RequestAndResponse;

import com.tb.transport.Transport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedRequestHandler {

    // Executor service with a fixed number of threads
    private final ExecutorService executorService;

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    private Transport transport;
    // Constructor that initializes the ExecutorService with the max number of threads
    public MultiThreadedRequestHandler(Transport transport) {
        // Max number of threads will be the available processors in the system
        int maxThreads = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(maxThreads);
        this.transport=transport;
    }

    // Method to handle incoming requests and dispatch them asynchronously
    public void dispatch(com.tb.common.eventDriven.RequestAndResponse.Request request) {
        // Dispatch the request asynchronously by submitting it to the ExecutorService
        executorService.submit(() -> {
            try {
                // Process the request here (this runs in a separate thread)
                processRequest(request);
            } catch (Exception e) {
                // Handle the exception, print stack trace, and continue execution
                System.err.println("Error processing request: " + request);
                e.printStackTrace();
            }
        });
    }

    // Process the request (this method runs in a separate thread)
    private void processRequest(com.tb.common.eventDriven.RequestAndResponse.Request request) throws Exception {
        //for now just send response
        Payload response= request.generateResponse();
        this.transport.sendMessage(response);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
