package com.tb.calling.router;

import com.tb.common.Router;

public class ConversationVertoRouter implements Router<RouterInputForCall,
        RouterOutputForCall> {
    @Override
    public RouterOutputForCall route(RouterInputForCall input) {
        return new RouterOutputForCall(input.aParty,input.bParty,input.phoneNumber,input.didNumber);
    }
}
