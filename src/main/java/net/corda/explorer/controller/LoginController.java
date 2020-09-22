package net.corda.explorer.controller;

import net.corda.explorer.exception.AuthenticationException;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.request.LoginRequest;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.model.response.Profile;
import net.corda.explorer.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private LoginService loginService;

    @GetMapping("/server_awake")
    public MessageResponseEntity<String> server_awake(@RequestHeader(value="clienttoken") String clienttoken) throws AuthenticationException {
        // auth check
        if (!servertoken.equals(clienttoken)) throw new AuthenticationException("No valid client token");
        return new MessageResponseEntity<>();
    }

    @PostMapping("/login")
    public MessageResponseEntity<Profile> login(@RequestHeader(value="clienttoken") String clienttoken, @RequestBody LoginRequest loginRequest) throws AuthenticationException {
        // auth check
        if (!servertoken.equals(clienttoken)) throw new AuthenticationException("No valid client token");
        try {
            return new MessageResponseEntity<>(loginService.loginToNode(loginRequest));
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public MessageResponseEntity<Profile> getProfile(@RequestHeader(value="clienttoken") String clienttoken) throws AuthenticationException {
        // auth check
        if (!servertoken.equals(clienttoken)) throw new AuthenticationException("No valid client token");
        return new MessageResponseEntity<>(loginService.getProfile());
    }
}