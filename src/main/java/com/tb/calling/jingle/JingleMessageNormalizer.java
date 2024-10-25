package com.tb.calling.jingle;
import com.tb.calling.jingle.ConversationsRequests.JingleMsgType;
import com.tb.calling.jingle.ConversationsRequests.JingleSDP;
import com.tb.common.CallSignalingMessage;
import com.tb.common.Delay;
import com.tb.common.StringUtil;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallMsgType;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
import org.jetbrains.annotations.NotNull;

public class JingleMessageNormalizer {

    private final JingleChannelDriver jingleChannelDriver;
    private final JingleConnector jingleConnector;

    public JingleMessageNormalizer(JingleChannelDriver jingleChannelDriver, JingleConnector jingleConnector) {
        this.jingleChannelDriver = jingleChannelDriver;
        this.jingleConnector = jingleConnector;
    }

    public void handleIncomingMessage(Payload payload) {
        String rawMessage=payload.getData();
        CallMsgType messageType = parseMessageType(rawMessage);

        switch (messageType) {
            case SESSION_START:

                this.ringing();
                this.proposeResponse(msg);
                Delay.sleep(2000);
                this.accept();
                this.proceed();
                jingleChannelDriver.addJingleCall(rawMessage); // Assuming rawMessage contains necessary data
                break;
            case TRYING:
                // Handle trying state
                break;
            case RINGING:
                jingleChannelDriver.onRing(rawMessage);
                break;
            case ANSWER:
                jingleChannelDriver.onAnswer(rawMessage);
                break;
            case HANGUP:
                // Handle hangup state
                break;
            default:
                // Handle unknown message type
                break;
        }
    }

    private CallSignalingMessage parseMessageType(String msg) {
        if (msg.contains("jm-propose")) {
            ProposeParams proposeParams = parseProposeParams(msg);
            CallSignalingMessage callStartMsg=
                    new CallSignalingMessage(CallMsgType.SESSION_START,
                            proposeParams.aParty(), proposeParams.bParty(), proposeParams.sessionID());
            return callStartMsg;

        } else if (msg.contains("session-initiate")) {
            JingleSDP jingleSDP = new JingleSDP(msg, JingleMsgType.SDP);
            assert (!this.getaParty().isEmpty() && !this.getaPartyDeviceId().isEmpty());
            assert (!this.getbParty().isEmpty() && !this.getbPartyDeviceId().isEmpty());
            jingleSDP.getMetadata().put("bParty", this.getbParty() + "/" + this.getbPartyDeviceId());
            jingleSDP.getMetadata().put("aParty", this.getaParty() + "/" + this.getaPartyDeviceId());
            this.multiThreadedRequestHandler.sendResponse(jingleSDP);
        } else if (msg.contains("ringing")) {
            return CallMsgType.RINGING;
        } else if (msg.contains("answer")) {
            return CallMsgType.ANSWER;
        } else if (msg.contains("hangup")) {
            return CallMsgType.HANGUP;
        }
        return null; // Or some default value/exception
    }

    @NotNull
    private static ProposeParams parseProposeParams(String msg) {
        String aPartyWithDevice = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "from=", ",");
        String[] tempArr = aPartyWithDevice.split("/");
        String aParty= tempArr[0];
        String aPartyDeviceId= tempArr[1];

        String bPartyWithDevice= StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "to=", ",");
        tempArr = bPartyWithDevice.split("/");
        String bParty= tempArr[0].trim();
        String bPartyDeviceId= tempArr[1].trim();
        if ((aParty.isEmpty() || !aPartyDeviceId.isEmpty()))
            throw new AssertionError("Either a party or the device id is empty in jingle-propose.");
        if ((bParty.isEmpty() || bPartyDeviceId.isEmpty()))
            throw new AssertionError("Either b party or the device id is empty in jingle-propose.");
        String sessionID=StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "id=jm-propose-","propose")
                .trim();
        if (sessionID.isEmpty())
            throw new AssertionError("SessionId cannot be empty in jingle-propose.");
        ProposeParams result = new ProposeParams(aParty, bParty, sessionID);
        return result;
    }

    private record ProposeParams(String aParty, String bParty, String sessionID) {
    }
}
