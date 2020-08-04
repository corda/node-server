package net.corda.explorer.controller;

import net.corda.core.contracts.ContractState;
import net.corda.core.node.services.Vault;
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
    public MessageResponseEntity<Vault.Page<ContractState>> getVaultStates(@RequestHeader(value="clienttoken") String clienttoken, @RequestBody VaultFilterSelection filter) throws AuthenticationException {
        // auth check
        if (!servertoken.equals(clienttoken)) throw new AuthenticationException("No valid client token");
        try{
            return new MessageResponseEntity<>(vaultService.getVaultStates(filter));
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping("/vault-filter")
    public MessageResponseEntity<VaultFilter> getVaultFilter(@RequestHeader(value="clienttoken") String clienttoken) throws AuthenticationException {
        // auth check
        if (!servertoken.equals(clienttoken)) throw new AuthenticationException("No valid client token");
        try{
            return new MessageResponseEntity<>(vaultService.getVaultFilters());
        }catch (Exception e){
            e.printStackTrace();
            throw new GenericException(e.getMessage());
        }
    }
}
