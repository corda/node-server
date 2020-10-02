package net.corda.explorer.service;

import net.corda.core.messaging.CordaRPCOps;
import net.corda.explorer.model.common.FlowInfo;
import net.corda.explorer.model.response.FlowData;
import net.corda.explorer.model.response.TransactionList;

public interface TransactionService {
    FlowData getFlowList(CordaRPCOps proxy);
    TransactionList getTransactionList(CordaRPCOps proxy, int pageSize, int offset);
    Object triggerFlow(CordaRPCOps proxy, FlowInfo flowInfo) throws Exception;
}
