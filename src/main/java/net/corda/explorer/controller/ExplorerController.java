package net.corda.explorer.controller;

import net.corda.explorer.constants.MessageConstants;
import net.corda.explorer.exception.AuthenticationException;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.model.response.NetworkMap;
import net.corda.explorer.service.ExplorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ExplorerController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private ExplorerService explorerService;

    @GetMapping("/network-map")
    public MessageResponseEntity<?> networkMap(@RequestHeader(value="clienttoken") String clienttoken) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        try {
            NetworkMap networkMap = explorerService.getNetworkMap();
            return new MessageResponseEntity<>(networkMap);
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping("/party-list")
    public MessageResponseEntity<?> partyList(@RequestHeader(value="clienttoken") String clienttoken) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        try {
            List<String> parties = explorerService.getParties();
            return new MessageResponseEntity<>(parties);
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
}
