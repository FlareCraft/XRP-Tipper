package com.flarecraft.xrptipper.transactions.XUMM;

import com.flarecraft.xrptipper.XRPTipper;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ResponseParser {

    // TODO: Response.body().string() can only be run once and then the response closes
    // Maybe this should be an object and so I only grab the response body.string once?
    public static JSONObject getResponseJSONObject(Response response) {

        JSONParser parser = new JSONParser();
        JSONObject responseObject = null;
        try {

            responseObject = (JSONObject) parser.parse(response.body().string());
        } catch (Exception e) {

        }

        return responseObject;
    }

    //TODO: this should be a single method that takes an array. Loop through the array to get to the level of the JSON body you want
    public static long checkResponseCode(JSONObject response) {
        JSONParser parser = new JSONParser();
        long responseCode = 0;
        try {
            JSONObject refsObject = (JSONObject) response.get("error");
            responseCode = (long) refsObject.get("code");
        } catch (Exception e) {
            XRPTipper.p.getLogger().info("I have an exception on the checkResponseCode method");
        }

        return responseCode;
    }

    //TODO: this should be a single method that takes an array. Loop through the array to get to the level of the JSON body you want
    public static String extractXUMMRegistrationURL(JSONObject response) {

        JSONParser parser = new JSONParser();
        String registrationLink = null;
        try {
            JSONObject refsObject = (JSONObject) response.get("refs");
            registrationLink = (String) refsObject.get("qr_png");
        } catch (Exception e) {
            XRPTipper.p.getLogger().info("I have an exception on the extractXUMMRegistrationURL");
        }

        return registrationLink;
    }

    //TODO: See above
    public static String extractXUMMRegistrationUUID(JSONObject response) {

        JSONParser parser = new JSONParser();
        String registrationUUID = null;
        try {
            registrationUUID = (String) response.get("uuid");
        } catch (Exception e) {
            XRPTipper.p.getLogger().info("I have an exception on the extractXUMMRegistrationUUID: " + e);
        }

        return registrationUUID;
    }

    public static String extractXUMMUserToken(JSONObject response) {

        JSONParser parser = new JSONParser();
        String userToken = null;
        try {
            JSONObject applicationObject = (JSONObject) response.get("application");
            userToken = (String) applicationObject.get("issued_user_token");
        } catch (Exception e) {
            XRPTipper.p.getLogger().info("I have an exception on the extractXUMMUserToken: " + e);
        }

        return userToken;
    }
    
    public static boolean extractXUMMPushedStatus(JSONObject response) {

        JSONParser parser = new JSONParser();
        boolean pushed = true;
        try {
            pushed = (boolean) response.get("pushed");
        } catch (Exception e) {
            XRPTipper.p.getLogger().info("I have an exception on the extractXUMMPushedStatus: " + e);
        }

        return pushed;
    }
}
