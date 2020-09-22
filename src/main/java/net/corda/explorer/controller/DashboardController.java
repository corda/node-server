package net.corda.explorer.controller;

import net.corda.client.rpc.RPCException;
import net.corda.core.node.NetworkParameters;
import net.corda.core.node.NodeDiagnosticInfo;
import net.corda.explorer.exception.AuthenticationException;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class DashboardController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("dashboard/node-diagnostics")
    public MessageResponseEntity<NodeDiagnosticInfo> getNodeDiagnostics(@RequestHeader(value="clienttoken") String clienttoken) throws AuthenticationException {
        // auth check
        if (!servertoken.equals(clienttoken)) throw new AuthenticationException("No valid client token");
        try {
            return new MessageResponseEntity<>(dashboardService.nodeDiagnosticInfo());
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
    public MessageResponseEntity<NetworkParameters> getNetworkParameters(@RequestHeader(value="clienttoken") String clienttoken) throws AuthenticationException {
        // auth check
        if (!servertoken.equals(clienttoken)) throw new AuthenticationException("No valid client token");
        try {
            return new MessageResponseEntity<>(dashboardService.networkParameters());
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
}
