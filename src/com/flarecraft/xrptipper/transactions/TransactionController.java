package com.flarecraft.xrptipper.transactions;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.datatypes.player.PlayerProfile;
import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import com.flarecraft.xrptipper.transactions.XUMM.ResponseParser;
import com.flarecraft.xrptipper.transactions.XUMM.XUMM;
import com.flarecraft.xrptipper.transactions.XUMM.XUMMWebSocketClient;
import com.flarecraft.xrptipper.transactions.util.PaymentUtils;
import com.flarecraft.xrptipper.config.*;
import com.flarecraft.xrptipper.util.player.UserManager;
import okhttp3.Response;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;


public class TransactionController extends Exception {

    public static void tipHandler(PlayerProfile profile, double paymentAmount, Player player) throws TransactionController {

        XRPTipper.p.getLogger().info("Server wallet address is: " + Config.getInstance().getServerWalletAddress());
        XUMM xummController = XRPTipper.getXumm();
        // Should the server wallet address be pulled from config every time? Or should it be loaded on startup?
        Boolean isValidAmount = xummController.validatePaymentAmount("XRP", paymentAmount, player);

        if(isValidAmount) {

            Response response = xummController.paymentRequest(Config.getInstance().getServerWalletAddress(), PaymentUtils.convertIntToDrops(paymentAmount), profile.getXummToken());
            JSONObject responseObject = ResponseParser.getResponseJSONObject(response);
            boolean isPushed = ResponseParser.extractXUMMPushedStatus(responseObject);
            if(!isPushed) {

                throw new TransactionController("Push failed");
            }
        }
        else {
            throw new TransactionController("Invalid Amount for XRP");
        }
    }

    public static String handleRegistration(String address, Player player) throws TransactionController {

        XUMM xummController = XRPTipper.getXumm();
        Response response = xummController.signRequest(address);
        JSONObject responseObject = ResponseParser.getResponseJSONObject(response);
        long responseCode = ResponseParser.checkResponseCode(responseObject);
        if(responseCode == 603) {

            throw new TransactionController("Bad Address");
        }
        String registrationLink = ResponseParser.extractXUMMRegistrationURL(responseObject);
        String registrationUUID = ResponseParser.extractXUMMRegistrationUUID(responseObject);

        player.sendMessage("Click the link below. Scan the provided QR code with XUMM to register");
        player.sendMessage("Link: " + registrationLink);

        Boolean isTimeout = XUMMWebSocketClient.watchForASign(registrationUUID);
        if(isTimeout) {

            throw new TransactionController("Expired registration link");
        }
        Response userTokenResponse = xummController.getUserTokenRequest(registrationUUID);
        JSONObject userTokenResponseObject = ResponseParser.getResponseJSONObject(userTokenResponse);

        return ResponseParser.extractXUMMUserToken(userTokenResponseObject);
    }

    public static void sendHandler(PlayerProfile senderProfile, double paymentAmount, Player sendingPlayer, Player receivingPlayer) throws TransactionController {

        XRPTipper.p.getLogger().info("Server wallet address is: " + Config.getInstance().getServerWalletAddress());
        XUMM xummController = XRPTipper.getXumm();
        // Should the server wallet address be pulled from config every time? Or should it be loaded on startup?
        Boolean isValidAmount = xummController.validatePaymentAmount("XRP", paymentAmount, sendingPlayer);

        if(isValidAmount) {

            XRPTipperPlayer tipperReceivingPlayer = UserManager.getPlayer(receivingPlayer);

            Response response = xummController.paymentRequest(tipperReceivingPlayer.getProfile().getXrplAddress(),
                    PaymentUtils.convertIntToDrops(paymentAmount),
                    senderProfile.getXummToken()
                    );
            JSONObject responseObject = ResponseParser.getResponseJSONObject(response);
            boolean isPushed = ResponseParser.extractXUMMPushedStatus(responseObject);
            if(!isPushed) {

                throw new TransactionController("Push failed");
            }
        }
        else {
            throw new TransactionController("Invalid Amount for XRP");
        }
    }

    public TransactionController(String errorMessage) {

        super(errorMessage);
    }

}
