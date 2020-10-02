package net.corda.explorer.controller;

import net.corda.client.rpc.RPCException;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.explorer.constants.HeaderConstants;
import net.corda.explorer.constants.MessageConstants;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.rpc.AuthCheck;
import net.corda.explorer.rpc.NodeRPCClient;
import net.corda.explorer.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("dashboard/node-diagnostics")
    public MessageResponseEntity<?> getNodeDiagnostics(@RequestHeader Map<String, String> headers) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        CordaRPCOps proxy = NodeRPCClient.getRpcProxy(UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID)));
        try {
            return new MessageResponseEntity<>(dashboardService.nodeDiagnosticInfo(proxy));
        }catch (RPCException e){
            if(e.getMessage().contains("Received RPC for unknown method nodeDiagnosticInfo")){
                return new MessageResponseEntity<>();
            }else{
                throw new GenericException(e.getMessage());
            }
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping("dashboard/network-parameters")
    public MessageResponseEntity<?> getNetworkParameters(@RequestHeader Map<String, String> headers) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        CordaRPCOps proxy = NodeRPCClient.getRpcProxy(UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID)));
        try {
            return new MessageResponseEntity<>(dashboardService.networkParameters(proxy));
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
}
