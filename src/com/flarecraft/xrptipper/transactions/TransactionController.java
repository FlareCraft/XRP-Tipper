package com.flarecraft.xrptipper.transactions;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.datatypes.player.PlayerProfile;
import com.flarecraft.xrptipper.transactions.XUMM.ResponseParser;
import com.flarecraft.xrptipper.transactions.XUMM.XUMM;
import com.flarecraft.xrptipper.transactions.XUMM.XUMMWebSocketClient;
import com.flarecraft.xrptipper.transactions.util.PaymentUtils;
import com.flarecraft.xrptipper.config.*;
import okhttp3.Response;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;


public class TransactionController {

    public static void tipHandler(PlayerProfile profile, double paymentAmount) {

        System.out.println("I'm inside the tipHandler");
        System.out.println("Server wallet address is: " + Config.getInstance().getServerWalletAddress());
        XUMM xummController = XRPTipper.getXumm();
        // Should the server wallet address be pulled from config every time? Or should it be loaded on startup?
        xummController.paymentRequest(Config.getInstance().getServerWalletAddress(), PaymentUtils.convertIntToDrops(paymentAmount), profile.getXummToken());
    }

    public static String handleRegistration(String address, Player player) {

        System.out.println("generateRegistrationLink entered");

        XUMM xummController = XRPTipper.getXumm();
        Response response = xummController.signRequest(address);
        JSONObject responseObject = ResponseParser.getResponseJSONObject(response);
        String registrationLink = ResponseParser.extractXUMMRegistrationURL(responseObject);
        String registrationUUID = ResponseParser.extractXUMMRegistrationUUID(responseObject);

        player.sendMessage("Click the link below. Scan the provided QR code with XUMM to register");
        player.sendMessage("Link: " + registrationLink);
        System.out.println(registrationLink);

        XUMMWebSocketClient.watchForASign(registrationUUID);
        System.out.println("I saw the sign!");

        Response userTokenResponse = xummController.getUserTokenRequest(registrationUUID);
        JSONObject userTokenResponseObject = ResponseParser.getResponseJSONObject(userTokenResponse);

        return ResponseParser.extractXUMMUserToken(userTokenResponseObject);
    }
}