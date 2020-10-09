package net.corda.explorer.service;

import net.corda.core.identity.Party;
import net.corda.explorer.exception.ConnectionException;
import net.corda.explorer.model.request.LoginRequest;
import net.corda.explorer.model.response.Profile;

import java.io.IOException;
import java.util.UUID;

public interface LoginService {
    Profile loginToNode(LoginRequest loginRequest) throws ConnectionException;
    Profile getProfile(UUID nodeConnId);
}
