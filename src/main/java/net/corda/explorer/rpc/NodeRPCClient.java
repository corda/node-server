package net.corda.explorer.rpc;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCClientConfiguration;
import net.corda.client.rpc.RPCException;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import net.corda.explorer.exception.ConnectionException;
import net.corda.explorer.model.request.LoginRequest;
import net.corda.explorer.model.response.Profile;
import net.corda.explorer.service.SSHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/***
 *
 */
@Component
public class NodeRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(NodeRPCClient.class);

    private static Session sshSession;
    private static Map<UUID, CordaRPCOps> nodeConnIdToProxy = new HashMap<>();

    public static CordaRPCOps getRpcProxy(UUID nodeConnId) {
        return nodeConnIdToProxy.get(nodeConnId);
    }

    public Profile doLogin(LoginRequest loginRequest) throws ConnectionException {

        // depends if the connect is direct or through SSH forwarding
        String hostForRPCClient = loginRequest.getHostName();

        // Check for ssh secure request
        if (loginRequest.getSsh() != null) {
            hostForRPCClient = "localhost";
            try {
                sshSession = SSHService.setupSSHTunnel(loginRequest);
                logger.info("SSH connection established to " + loginRequest.getHostName());
            } catch (JSchException je) {
                throw new ConnectionException(je.getMessage());
            }
        }

        try {
            CordaRPCClientConfiguration config = new CordaRPCClientConfiguration(Duration.ofMinutes(3), 4);
            CordaRPCOps rpcProxy = new CordaRPCClient(NetworkHostAndPort.parse(hostForRPCClient + ":" +
                    loginRequest.getPort()), config).start(loginRequest.getUsername(), loginRequest.getPassword()).getProxy();
            UUID nodeConnId = UUID.randomUUID();
            nodeConnIdToProxy.put(nodeConnId, rpcProxy);
            return getProfile(nodeConnId);
        } catch (RPCException re) {
            throw new ConnectionException(re.getMessage());
        }
    }

    public Profile getProfile(UUID nodeConnId) {
        Party party = nodeConnIdToProxy.get(nodeConnId).nodeInfo().getLegalIdentities().get(0);
        Profile profile = new Profile();
        profile.setName(party.getName().getOrganisation());
        profile.setCity(party.getName().getLocality());
        profile.setCountry(party.getName().getCountry());
        profile.setRpcConnectionId(nodeConnId.toString());
        return profile;
    }
}