package net.corda.explorer.controller;

import net.corda.explorer.constants.MessageConstants;
import net.corda.explorer.exception.AuthenticationException;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.common.FlowInfo;
import net.corda.explorer.model.request.PageRequest;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.model.response.TransactionList;
import net.corda.explorer.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class TransactionController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/flow-list")
    public MessageResponseEntity<?> getRegisteredFlows(@RequestHeader(value="clienttoken") String clienttoken) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        try{
            return new MessageResponseEntity<>(transactionService.getFlowList());
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping("/transaction-list")
    public MessageResponseEntity<?> transactionList(@RequestHeader(value="clienttoken") String clienttoken, @RequestBody PageRequest pageRequest) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        try {
            TransactionList transactionList = transactionService.getTransactionList(pageRequest.getPageSize(), pageRequest.getOffset());
            return new MessageResponseEntity<>(transactionList);
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping("/start-flow")
    public MessageResponseEntity<?> startFlow(@RequestHeader(value="clienttoken") String clienttoken, @RequestBody FlowInfo flowInfo) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        try {
            Object response = transactionService.triggerFlow(flowInfo);
            if(response == null){
                return new MessageResponseEntity<>("Flow Executed Successfully");
            }
            return new MessageResponseEntity<>(response.toString());
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
}