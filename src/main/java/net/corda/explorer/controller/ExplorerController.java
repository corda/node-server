package net.corda.explorer.controller;

import net.corda.core.messaging.CordaRPCOps;
import net.corda.explorer.constants.HeaderConstants;
import net.corda.explorer.constants.MessageConstants;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.model.response.NetworkMap;
import net.corda.explorer.rpc.AuthCheck;
import net.corda.explorer.rpc.NodeRPCClient;
import net.corda.explorer.service.ExplorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class ExplorerController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private ExplorerService explorerService;

    @GetMapping("/network-map")
    public MessageResponseEntity<?> networkMap(@RequestHeader Map<String, String> headers) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        CordaRPCOps proxy = NodeRPCClient.getRpcProxy(UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID)));
        try {
            NetworkMap networkMap = explorerService.getNetworkMap(proxy);
            return new MessageResponseEntity<>(networkMap);
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping("/party-list")
    public MessageResponseEntity<?> partyList(@RequestHeader Map<String, String> headers) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        CordaRPCOps proxy = NodeRPCClient.getRpcProxy(UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID)));
        try {
            List<String> parties = explorerService.getParties(proxy);
            return new MessageResponseEntity<>(parties);
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
}
