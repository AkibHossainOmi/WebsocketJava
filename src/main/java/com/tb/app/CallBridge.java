//package telcobright.app;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import telcobright.app.models.JanusResponse;
//
//public class CallBridge{
//    AbstractPhoneCall origLeg ;
//    List<AbstractPhoneCall> otherLegs;
//    List<AbstractPhoneCall> allLegs = new ArrayList<>();
//    public CallBridge(AbstractPhoneCall origLeg, List<AbstractPhoneCall> otherLegs) {
//        this.origLeg = origLeg;
//        this.otherLegs = otherLegs;
//        allLegs.add(origLeg);
//        //all other legs subscribe to origLeg.onStart() here.
//        //and each otherLeg calls own startCall()
//        allLegs.addAll(otherLegs);
//    }
//    private void onStart(AbstractPhoneCall senderLeg) {
//
//    }
//    public void bridgeCalls(){
//        for (AbstractPhoneCall otherLeg:
//                otherLegs) {
//            if (!this.origLeg.getUniqueId().equals(otherLeg.getUniqueId())){
//                otherLeg.startCall();
//            }
//        }
//    }
//}
