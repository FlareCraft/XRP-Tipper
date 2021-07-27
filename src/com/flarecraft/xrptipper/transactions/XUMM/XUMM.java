package com.flarecraft.xrptipper.transactions.XUMM;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.config.*;
import okhttp3.*;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.math.BigInteger;

public class XUMM extends Exception {

    private String apiKey;
    private String apiSecret;

    public XUMM() throws XUMM {

        setApiKey();
        setApiSecret();
    }

    private void setApiKey() throws XUMM {

        this.apiKey = Config.getInstance().getXUMMApiKey();
        if(this.apiKey == null) {
            throw new XUMM("XUMM API Key is empty. Follow config.yml instructions to setup a key");
        }
    }

    private void setApiSecret() throws XUMM {

        this.apiSecret = Config.getInstance().getXUMMApiSecret();
        if(this.apiSecret == null) {
            throw new XUMM("XUMM API Secret is empty. Follow config.yml instructions to setup a key");
        }
    }

    private String getApiKey() {

        return apiKey;
    }

    private String getApiSecret() {

        return apiSecret;
    }

    public Response paymentRequest(String paymentDestination, BigInteger paymentAmount, String userToken) {

        String paymentRequestJsonBody = "{ \n" +
                "  \"txjson\": {\n" +
                "    \"TransactionType\": \"Payment\",\n" +
                "    \"Destination\": \"" + paymentDestination + "\",\n" +
                "    \"Amount\": \"" + paymentAmount.toString() + "\"\n" +
                "  },\n" +
                "  \"user_token\": \"" + userToken + "\"\n" +
                "}";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), paymentRequestJsonBody
        );


        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://xumm.app/api/v1/platform/payload")
                .method("POST", body)
                .addHeader("x-api-key", this.apiKey)
                .addHeader("x-api-secret",this.apiSecret)
                .addHeader("content-type","application/json")
                .build();

        try {
            XRPTipper.p.getLogger().info("Making the payment request");
            Response response = client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            XRPTipper.p.getLogger().info("Exception?");
            e.printStackTrace();
            return null;
        }
    }

    public Response signRequest() {

        String signRequestJSONBody = "{\n" +
                "   \"txjson\":{\n" +
                "       \"TransactionType\":\"SignIn\"\n" +
                "   }\n" +
                "}";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), signRequestJSONBody
        );

        // I can probably pull the code below out into a separate method to avoid duplication
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://xumm.app/api/v1/platform/payload")
                .method("POST", body)
                .addHeader("x-api-key", this.apiKey)
                .addHeader("x-api-secret",this.apiSecret)
                .addHeader("content-type","application/json")
                .build();

        try {
            XRPTipper.p.getLogger().info("Making the sign request");
            return client.newCall(request).execute();
        } catch (IOException e) {
            XRPTipper.p.getLogger().info("Exception?");
            e.printStackTrace();
        }

        return null;
    }

    public Response getUserTokenRequest(String registrationUUID) {

        XRPTipper.p.getLogger().info("I got to the get token request");

        // I can probably pull the code below out into a separate method to avoid duplication
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://xumm.app/api/v1/platform/payload/" + registrationUUID)
                .method("GET", null)
                .addHeader("x-api-key", this.apiKey)
                .addHeader("x-api-secret",this.apiSecret)
                .addHeader("content-type","application/json")
                .build();

        try {
            XRPTipper.p.getLogger().info("Making the sign request");
            return client.newCall(request).execute();
        } catch (IOException e) {
            XRPTipper.p.getLogger().info("Exception?");
            e.printStackTrace();
        }

        return null;
    }

    public Boolean validatePaymentAmount(String currencyType, Double paymentAmount, Player player ) {

        if(currencyType.equals("XRP") && paymentAmount < .000001) {

            player.sendMessage("Your tip is too small! Try a tip larger than 6 decimal places");
            return false;
        }

        return true;
    }

    public XUMM(String errorMessage) {
        super(errorMessage);
    }
}
