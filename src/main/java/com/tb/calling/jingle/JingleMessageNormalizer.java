package com.tb.calling.jingle;
import com.tb.common.CallEvent;
import com.tb.common.Delay;
import com.tb.common.StringUtil;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;
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
        CallEvent signalingMessage = parseRawMessage(rawMessage);
        CallEventType messageType=signalingMessage.getEventType();

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

    private CallEvent parseRawMessage(String msg) {
        if (msg.contains("jm-propose")) {
            ProposeParams proposeParams = parseProposeParams(msg);
            CallEvent propose=
                    new CallEvent(CallEventType.SESSION_START,proposeParams.sessionID(),msg);
            return propose;

        } else if (msg.contains("session-initiate")) {
            String sessionId= StringUtil.Parser.getFirstOccuranceOfParamValueByIndexAndTerminatingStr
                    (msg,"jingle sid=","xmlns").trim();
            if (sessionId.isEmpty())
                throw new RuntimeException("Found empty Session id in jingle SDP");
            CallEvent sdp= new CallEvent(CallEventType.SDP,sessionId,msg);
            return sdp;
        } else if (msg.contains("transport-info")) {
            String sessionId = StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "priority=&apos;", "&apos;");
            if (sessionId.isEmpty())
                throw new RuntimeException("Found empty Session id in jingle SDP");
            CallEvent iceCandidate= new CallEvent(CallEventType.ICE_ACK,sessionId,msg);
            return iceCandidate;
        }
        return new CallEvent(CallEventType.UNKNOWN,"",msg);
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
