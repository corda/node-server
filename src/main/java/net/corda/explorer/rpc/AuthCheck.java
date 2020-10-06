package net.corda.explorer.rpc;

import net.corda.explorer.constants.HeaderConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class AuthCheck {

    @Value("${servertoken}")
    private String servertoken;

    private static String SERVERTOKEN_STATIC;

    @Value("${servertoken}")
    public void setServertokenStatic(String servertoken) {
        AuthCheck.SERVERTOKEN_STATIC = servertoken;
    }

    public static boolean notAuthorizedToLogin(Map<String, String> headers) {
        if (!headers.get(HeaderConstants.CLIENTTOKEN).equals("")) {
            String clienttoken = headers.get(HeaderConstants.CLIENTTOKEN);
            return !SERVERTOKEN_STATIC.equals(clienttoken);
        }
        return true;
    }

    public static boolean notAuthorizedToProxy(Map<String, String> headers) {
        // client token and rpcconnid must both exist and match valid
        // for node endpoints
        if (!headers.get(HeaderConstants.CLIENTTOKEN).equals("")) {
            if (!headers.get(HeaderConstants.RPC_CONNECTION_ID).equals("")) {
                String clienttoken = headers.get(HeaderConstants.CLIENTTOKEN);
                UUID rpcConnId = UUID.fromString(headers.get(HeaderConstants.RPC_CONNECTION_ID));

                return (!SERVERTOKEN_STATIC.equals(clienttoken) ||
                        NodeRPCClient.getRpcProxy(rpcConnId) == null);
            }
        }
        return true;
    }
}
