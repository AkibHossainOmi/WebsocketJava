package com.tb.calling;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CallBridge {
    AbstractCallLeg origLeg ;
    List<AbstractCallLeg> otherLegs;
    List<AbstractCallLeg> allLegs = new ArrayList<>();
    public CallBridge(AbstractCallLeg origLeg, List<AbstractCallLeg> otherLegs) {
        this.origLeg = origLeg;
        this.otherLegs = otherLegs;
        allLegs.add(origLeg);
        //all other legs subscribe to origLeg.onStart() here.
        //and each otherLeg calls own startCall()
        allLegs.addAll(otherLegs);
    }
    /*public void onStart(AbstractCallLeg senderLeg) {

    }*/
    public void onStart(AbstractCallLeg origLeg) {
        for (AbstractCallLeg otherLeg :
                getOtherLegs(origLeg).toList()) {
            otherLeg.startCall();
        }
    }
    public void onRinging(AbstractCallLeg origLeg) {
        for (AbstractCallLeg otherLeg :
                getOtherLegs(origLeg).toList()) {
            otherLeg.startRing();
        }
    }


    private Stream<AbstractCallLeg> getOtherLegs(AbstractCallLeg origLeg) {
        return this.allLegs.stream().filter(leg -> !origLeg.getUniqueId().equals(leg.getUniqueId()));
    }
}