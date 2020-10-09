package net.corda.explorer.service.impl;

import net.corda.explorer.config.AutoLoginConfig;
import net.corda.explorer.exception.ConnectionException;
import net.corda.explorer.model.request.LoginRequest;
import net.corda.explorer.model.response.Profile;
import net.corda.explorer.rpc.NodeRPCClient;
import net.corda.explorer.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AutoLoginConfig autoLoginConfig;

    @Autowired
    private NodeRPCClient rpcClient;

    @PostConstruct
    public void autoLogin() throws ConnectionException {
        if (autoLoginConfig.isEnabled()) {
            loginToNode(autoLoginConfig);
        }
    }

    @Override
    public Profile loginToNode(LoginRequest loginRequest) throws ConnectionException {
        Profile profile = rpcClient.doLogin(loginRequest);
        return profile;
    }

    @Override
    public Profile getProfile(UUID nodeConnId) {
        return rpcClient.getProfile(nodeConnId);
    }
}