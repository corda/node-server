package net.corda.explorer.controller;

import net.corda.explorer.constants.MessageConstants;
import net.corda.explorer.exception.GenericException;
import net.corda.explorer.model.request.LoginRequest;
import net.corda.explorer.model.response.MessageResponseEntity;
import net.corda.explorer.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    @Value("${servertoken}")
    private String servertoken;

    @Autowired
    private LoginService loginService;



    @GetMapping("/server_awake")
    public MessageResponseEntity<?> server_awake(@RequestHeader(value="clienttoken") String clienttoken) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        return new MessageResponseEntity<>();
    }

    @PostMapping("/login")
    public MessageResponseEntity<?> login(@RequestHeader(value="clienttoken") String clienttoken, @RequestBody LoginRequest loginRequest) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        try {
            return new MessageResponseEntity<>(loginService.loginToNode(loginRequest));
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public MessageResponseEntity<?> getProfile(@RequestHeader(value="clienttoken") String clienttoken) {
        // auth check
        if (!servertoken.equals(clienttoken)) {
            return MessageConstants.UNAUTHORIZED;
        }
        return new MessageResponseEntity<>(loginService.getProfile());
    }
}