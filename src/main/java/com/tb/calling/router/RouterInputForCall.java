package com.tb.calling.router;

public class RouterInputForCall {
    String aParty;
    String bParty;
    String phoneNumber;
    String didNumber;
    public RouterInputForCall(String aParty, String bParty,
                              String phoneNumber, String didNumber) {
        this.aParty = aParty;
        this.bParty = bParty;
        this.phoneNumber = phoneNumber;
        this.didNumber=didNumber;
    }
}
