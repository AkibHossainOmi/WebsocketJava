package com.tb.common.eventDriven;
public class KeepAliveTracker implements EventListener {
    private final int numOfFailedRequestToMarkDown;
    private final int numOfSuccessfulResponseToMarkUp;
    private int failedRequestsCount = 0;
    private int successfulResponsesCount = 0;
    private boolean serviceDown = false;

    public KeepAliveTracker(int numOfFailedRequestToMarkDown, int numOfSuccessfulResponseToMarkUp) {
        this.numOfFailedRequestToMarkDown = numOfFailedRequestToMarkDown;
        this.numOfSuccessfulResponseToMarkUp = numOfSuccessfulResponseToMarkUp;
    }

    @Override
    public synchronized void onResponseReceived(ReqRespPair reqRespPair) {
        if (reqRespPair.getRequest().getEventType() == EventType.KEEP_ALIVE) {
            successfulResponsesCount++;
            failedRequestsCount = 0; // Reset failed count on success

            if (serviceDown && successfulResponsesCount >= numOfSuccessfulResponseToMarkUp) {
                serviceDown = false;
                System.out.println("Service is now UP!");
            }
        }
    }

    @Override
    public synchronized void onEventExpired(ReqRespPair reqRespPair) {
        if (reqRespPair.getRequest().getEventType() == EventType.KEEP_ALIVE) {
            failedRequestsCount++;
            successfulResponsesCount = 0; // Reset success count on failure

            if (!serviceDown && failedRequestsCount >= numOfFailedRequestToMarkDown) {
                serviceDown = true;
                System.out.println("Service is now DOWN!");
            }
        }
    }

    public boolean isServiceDown() {
        return serviceDown;
    }
}
