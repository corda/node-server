package net.corda.explorer.controller;

import net.corda.explorer.constants.HeaderConstants;
import net.corda.explorer.constants.MessageConstants;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.request.LoginRequest;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.rpc.AuthCheck;
import net.corda.explorer.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private LoginService loginService;



    @GetMapping("/server_awake")
    public MessageResponseEntity<?> server_awake(@RequestHeader Map<String, String> headers) {
        // auth check
        if (AuthCheck.notAuthorizedToLogin(headers)) return MessageConstants.UNAUTHORIZED;
        return new MessageResponseEntity<>();
    }

    @PostMapping("/login")
    public MessageResponseEntity<?> login(@RequestHeader Map<String, String> headers, @RequestBody LoginRequest loginRequest) {
        // auth check
        if (AuthCheck.notAuthorizedToLogin(headers)) return MessageConstants.UNAUTHORIZED;
        try {
            return new MessageResponseEntity<>(loginService.loginToNode(loginRequest));
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public MessageResponseEntity<?> getProfile(@RequestHeader Map<String, String> headers) {
        // auth check
        if (AuthCheck.notAuthorizedToProxy(headers)) return MessageConstants.UNAUTHORIZED;
        UUID nodeConnId = UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID));
        return new MessageResponseEntity<>(loginService.getProfile(nodeConnId));
    }
}