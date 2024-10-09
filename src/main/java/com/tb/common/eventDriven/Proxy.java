package com.tb.common.eventDriven;

import com.tb.common.Communicator.Transport;

public abstract class Proxy<TOpenDataS, TCloseDataS,TOnMsgDataS, TSentDataS, TOnErrorDataS, TOnStatusDataS,
        TOpenDataC, TCloseDataC,TOnMsgDataC, TSentDataC, TOnErrorDataC, TOnStatusDataC>
        implements
        Transport<TOpenDataC, TCloseDataC,TOnMsgDataC, TSentDataC, TOnErrorDataC, TOnStatusDataC>
{

}
