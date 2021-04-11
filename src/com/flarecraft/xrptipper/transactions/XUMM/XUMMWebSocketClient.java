package com.flarecraft.xrptipper.transactions.XUMM;



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

@ClientEndpoint
public class XUMMWebSocketClient {

    // TODO: Run the socket connection on a different thread
    // TODO: have the onMessage poll untill it gets a "Right back at you" then wait 5 seconds

    private static CountDownLatch latch;
    private Logger logger = Logger.getLogger(this.getClass().getName());

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

        //I will eventually need to handle if the sign transaction is rejected
        Boolean opened = false;
        Boolean signed = false;
        Boolean user_token = false;
        try {

            JSONParser parser = new JSONParser();
            JSONObject responseObject = (JSONObject) parser.parse(message);
            opened = (Boolean) responseObject.get("opened");
            signed = (Boolean) responseObject.get("signed");
            user_token = (Boolean) responseObject.get("user_token");
        } catch (ParseException ignored) {}
        try {

            if (opened) {

                System.out.println("Opened!");
                //Log out to the player that they need to accept the transaction to register
            }
        } catch (NullPointerException e) {

            try {
                if (signed && user_token) {

                    latch.countDown();
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Registration Complete"));
                }
            } catch (NullPointerException | IOException ignored) {}
        }

    }


    @OnClose
    public void onClose(Session session, CloseReason closeReason) {

        latch.countDown();
    }

    public static void watchForASign(String signUUID ) {

        latch = new CountDownLatch(1);
        ClientManager client = ClientManager.createClient();

        try {

            client.connectToServer(XUMMWebSocketClient.class, new URI("wss://xumm.app/sign/" + signUUID));
            latch.await();
        } catch (DeploymentException | URISyntaxException | IOException | InterruptedException e) {

            throw new RuntimeException(e);
        }

        client.shutdown();
    }
}
