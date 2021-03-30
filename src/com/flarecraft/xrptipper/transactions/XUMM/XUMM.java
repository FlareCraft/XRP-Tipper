package com.flarecraft.xrptipper.transactions.XUMM;

import com.flarecraft.xrptipper.config.*;
import okhttp3.*;
import java.io.IOException;
import java.math.BigInteger;

public class XUMM {

    private String apiKey;
    private String apiSecret;

    public XUMM() {

        setApiKey();
        setApiSecret();
    }

    private void setApiKey() {

        System.out.println(Config.getInstance().getXUMMApiKey());
        this.apiKey = Config.getInstance().getXUMMApiKey();
    }

    private void setApiSecret() {

        System.out.println(Config.getInstance().getXUMMApiSecret());
        this.apiSecret = Config.getInstance().getXUMMApiSecret();
    }

    private String getApiKey() {

        return apiKey;
    }

    private String getApiSecret() {

        return apiSecret;
    }

    public void paymentRequest(String paymentDestination, BigInteger paymentAmount, String userToken) {

        System.out.println("I got to the payment request");
        System.out.println(paymentAmount);
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

        System.out.println("Set the request body");
        System.out.println("ApiKey is :" + this.apiKey);
        System.out.println(getApiKey());
        System.out.println(getApiSecret());

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://xumm.app/api/v1/platform/payload")
                .method("POST", body)
                .addHeader("x-api-key", this.apiKey)
                .addHeader("x-api-secret",this.apiSecret)
                .addHeader("content-type","application/json")
                .build();

        try {
            System.out.println("Making the payment request");
            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            System.out.println("Exception?");
            e.printStackTrace();
        }
    }

    public Response signRequest(String userAddress) {

        System.out.println("I got to the sign request");
        String signRequestJSONBody = "{\n" +
                "   \"txjson\":{\n" +
                "       \"TransactionType\":\"SignIn\",\n" +
                "       \"Destination\":\"" + userAddress + "\"\n" +
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
            System.out.println("Making the sign request");
            return client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println("Exception?");
            e.printStackTrace();
        }

        return null;
    }

    public Response getUserTokenRequest(String registrationUUID) {

        System.out.println("I got to the get token request");

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
            System.out.println("Making the sign request");
            return client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println("Exception?");
            e.printStackTrace();
        }

        return null;
    }
}
