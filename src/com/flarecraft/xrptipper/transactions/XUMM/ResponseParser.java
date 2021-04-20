package com.flarecraft.xrptipper.transactions.XUMM;

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

    public static String extractXUMMRegistrationURL(JSONObject response) {

        JSONParser parser = new JSONParser();
        String registrationLink = null;
        try {
            JSONObject refsObject = (JSONObject) response.get("refs");
            registrationLink = (String) refsObject.get("qr_png");
        } catch (Exception e) {
            System.out.println("I have an exception on the extractXUMMRegistrationURL");
        }

        return registrationLink;
    }

    public static String extractXUMMRegistrationUUID(JSONObject response) {

        JSONParser parser = new JSONParser();
        String registrationUUID = null;
        try {
            registrationUUID = (String) response.get("uuid");
        } catch (Exception e) {
            System.out.println("I have an exception on the extractXUMMRegistrationUUID: " + e);
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
            System.out.println("I have an exception on the extractXUMMUserToken: " + e);
        }

        return userToken;
    }
    
    public static boolean extractXUMMPushedStatus(JSONObject response) {

        JSONParser parser = new JSONParser();
        boolean pushed = true;
        try {
            pushed = (boolean) applicationObject.get("pushed");
        } catch (Exception e) {
            System.out.println("I have an exception on the extractXUMMPushedStatus: " + e);
        }

        return pushed;
    }
}
