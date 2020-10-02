package net.corda.explorer.controller;

import net.corda.explorer.constants.MessageConstants;
import net.corda.explorer.exception.AuthenticationException;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.response.VaultFilter;
import net.corda.explorer.model.request.VaultFilterSelection;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.service.VaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class VaultController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private VaultService vaultService;

    @PostMapping("/vault-query")

    public MessageResponseEntity<?> getVaultStates(@RequestHeader(value = "clienttoken") String clienttoken, @RequestBody VaultFilterSelection filter) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        try {
            return new MessageResponseEntity<>(vaultService.getVaultStates(filter));
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping("/vault-filter")
    public MessageResponseEntity<?> getVaultFilter(@RequestHeader(value="clienttoken") String clienttoken) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        try{
            return new MessageResponseEntity<>(vaultService.getVaultFilters());
        }catch (Exception e){
            e.printStackTrace();
            throw new GenericException(e.getMessage());
        }
    }
}
