package com.flarecraft.xrptipper.transactions.XUMM;

import com.flarecraft.xrptipper.XRPTipper;
import jakarta.websocket.*;
import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

@ClientEndpoint
public class XUMMWebSocketClient {

    // TODO: Run the socket connection on a different thread
    // TODO: have the onMessage poll untill it gets a "Right back at you" then wait 5 seconds

    private static CountDownLatch latch;
    private static Long startingTime;
    private static Long time;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private static Boolean isTimeout = false;

    @OnOpen
    public void onOpen(Session session) {

        try {

            session.getBasicRemote().sendText("start");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {

        handleWebsocketMessage(message, session);
    }

    public void handleWebsocketMessage(String message, Session session) {

        try {

            JSONParser parser = new JSONParser();
            JSONObject responseObject = (JSONObject) parser.parse(message);

            Boolean opened = (Boolean) responseObject.get("opened");
            Boolean signed = (Boolean) responseObject.get("signed");
            Boolean user_token = (Boolean) responseObject.get("user_token");
            time = (Long) responseObject.get("expires_in_seconds");
            if (startingTime == null && time != null) {

                startingTime = time;
            }
            else if(opened != null && opened) {

                XRPTipper.p.getLogger().info("Opened!");
            }
            else if (signed != null && signed && user_token != null && user_token) {

                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Registration Complete"));
            }
            else if (time < (startingTime - 300)) {

                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Request Timeout"));
            }
        } catch (ParseException | IOException | NullPointerException ignored) {}
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {

        if(closeReason.getReasonPhrase().equals("Request Timeout")) {

            isTimeout = true;
        }
        latch.countDown();
    }

    public static Boolean watchForASign(String signUUID) {

        latch = new CountDownLatch(1);
        ClientManager client = ClientManager.createClient();

        try {

            client.connectToServer(XUMMWebSocketClient.class, new URI("wss://xumm.app/sign/" + signUUID));
            latch.await();
        } catch (DeploymentException | URISyntaxException | IOException | InterruptedException e) {

            throw new RuntimeException(e);
        }

        if (isTimeout) {

            return true;
        }
        client.shutdown();
        return false;
    }
}
