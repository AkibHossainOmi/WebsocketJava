package com.tb.calling.jingle.message.signaling;

import com.tb.common.AbstractSignalingMessage;
import com.tb.common.StringUtil;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;

public class Propose extends AbstractSignalingMessage<String> {
    String aParty;
    String aPartyDevice;
    String bParty;
    String phoneNumber;
    public Propose(String msg, boolean isFullyParsed) {
        super(StringUtil.Parser
                        .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "id=jm-propose-","propose")
                        .trim(),CallEventType.SESSION_START,isFullyParsed, msg);
        if(isFullyParsed){
            String aPartyWithDevice = StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "from=", ",");
            String[] tempArr = aPartyWithDevice.split("/");
            aParty= tempArr[0];
            aPartyDevice= tempArr[1];
            String bPartyWithDevice= StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "to=", ",");
            tempArr = bPartyWithDevice.split("/");
            String bParty= tempArr[0].trim();
            if ((aParty.isEmpty() || !aPartyDevice.isEmpty()))
                throw new AssertionError("Either a party or the device id is empty in jingle-propose.");
            if (bParty.isEmpty())
                throw new AssertionError("Either b party or the device id is empty in jingle-propose.");
            String sessionID=StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "id=jm-propose-","propose")
                    .trim();
            if (sessionID.isEmpty())
                throw new AssertionError("SessionId cannot be empty in jingle-propose.");
            phoneNumber="asdfsadf"; //todo
        }
    }

    public String getaParty() {
        return aParty;
    }

    public void setaParty(String aParty) {
        this.aParty = aParty;
    }

    public String getaPartyDevice() {
        return aPartyDevice;
    }

    public void setaPartyDevice(String aPartyDevice) {
        this.aPartyDevice = aPartyDevice;
    }

    public String getbParty() {
        return bParty;
    }

    public void setbParty(String bParty) {
        this.bParty = bParty;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
