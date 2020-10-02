package net.corda.explorer.controller;

import net.corda.core.messaging.CordaRPCOps;
import net.corda.explorer.constants.HeaderConstants;
import net.corda.explorer.constants.MessageConstants;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.common.FlowInfo;
import net.corda.explorer.model.request.PageRequest;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.model.response.TransactionList;
import net.corda.explorer.rpc.AuthCheck;
import net.corda.explorer.rpc.NodeRPCClient;
import net.corda.explorer.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class TransactionController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/flow-list")
    public MessageResponseEntity<?> getRegisteredFlows(@RequestHeader Map<String, String> headers) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        CordaRPCOps proxy = NodeRPCClient.getRpcProxy(UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID)));
        try{
            return new MessageResponseEntity<>(transactionService.getFlowList(proxy));
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping("/transaction-list")
    public MessageResponseEntity<?> transactionList(@RequestHeader Map<String, String> headers, @RequestBody PageRequest pageRequest) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        CordaRPCOps proxy = NodeRPCClient.getRpcProxy(UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID)));
        try {
            TransactionList transactionList = transactionService.getTransactionList(proxy, pageRequest.getPageSize(), pageRequest.getOffset());
            return new MessageResponseEntity<>(transactionList);
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping("/start-flow")
    public MessageResponseEntity<?> startFlow(@RequestHeader Map<String, String> headers, @RequestBody FlowInfo flowInfo) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        CordaRPCOps proxy = NodeRPCClient.getRpcProxy(UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID)));
        try {
            Object response = transactionService.triggerFlow(proxy, flowInfo);
            if(response == null){
                return new MessageResponseEntity<>("Flow Executed Successfully");
            }
            return new MessageResponseEntity<>(response.toString());
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
}