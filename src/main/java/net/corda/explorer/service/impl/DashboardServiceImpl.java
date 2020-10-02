package net.corda.explorer.service.impl;

import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.NetworkParameters;
import net.corda.core.node.NodeDiagnosticInfo;
import net.corda.explorer.rpc.NodeRPCClient;
import net.corda.explorer.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Override
    public NodeDiagnosticInfo nodeDiagnosticInfo(CordaRPCOps proxy){
        return proxy.nodeDiagnosticInfo();
    }

    @Override
    public NetworkParameters networkParameters(CordaRPCOps proxy) {
        return proxy.getNetworkParameters();
    }
}
