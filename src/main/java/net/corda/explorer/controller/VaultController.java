package net.corda.explorer.controller;

import net.corda.core.messaging.CordaRPCOps;
import net.corda.explorer.constants.HeaderConstants;
import net.corda.explorer.constants.MessageConstants;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.request.VaultFilterSelection;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.rpc.AuthCheck;
import net.corda.explorer.rpc.NodeRPCClient;
import net.corda.explorer.service.VaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class VaultController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private VaultService vaultService;

    @PostMapping("/vault-query")

    public MessageResponseEntity<?> getVaultStates(@RequestHeader Map<String, String> headers, @RequestBody VaultFilterSelection filter) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        CordaRPCOps proxy = NodeRPCClient.getRpcProxy(UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID)));
        try {
            return new MessageResponseEntity<>(vaultService.getVaultStates(proxy, filter));
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping("/vault-filter")
    public MessageResponseEntity<?> getVaultFilter(@RequestHeader Map<String, String> headers) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        CordaRPCOps proxy = NodeRPCClient.getRpcProxy(UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID)));
        try{
            return new MessageResponseEntity<>(vaultService.getVaultFilters(proxy));
        }catch (Exception e){
            e.printStackTrace();
            throw new GenericException(e.getMessage());
        }
    }
}
