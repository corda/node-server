package net.corda.explorer.service;

import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.NetworkParameters;
import net.corda.core.node.NodeDiagnosticInfo;

import java.util.UUID;

public interface DashboardService {
    NodeDiagnosticInfo nodeDiagnosticInfo(CordaRPCOps proxy);
    NetworkParameters networkParameters(CordaRPCOps proxy);
}
