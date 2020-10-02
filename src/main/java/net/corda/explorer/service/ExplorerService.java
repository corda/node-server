package net.corda.explorer.service;

import net.corda.core.messaging.CordaRPCOps;
import net.corda.explorer.model.response.NetworkMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExplorerService {

    NetworkMap getNetworkMap(CordaRPCOps proxy);
    Map<String, String> getPartyKeyMap(CordaRPCOps proxy);
    List<String> getParties(CordaRPCOps proxy);

}
