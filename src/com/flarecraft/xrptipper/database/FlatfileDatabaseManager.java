package com.flarecraft.xrptipper.database;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.datatypes.player.PlayerProfile;

import java.io.*;
import java.util.UUID;

public class FlatfileDatabaseManager implements DatabaseManager {

    private final File playerPaymentPreferencesFile;

    private static final Object fileWritingLock = new Object();

    protected FlatfileDatabaseManager() {

        playerPaymentPreferencesFile = new File(XRPTipper.getPlayerPaymentPreferencesFilePath());
        fileChecks();
    }

    private void fileChecks() {

        if (playerPaymentPreferencesFile.exists()) {
            return;
        }

        System.out.println(playerPaymentPreferencesFile.getParentFile());
        playerPaymentPreferencesFile.getParentFile().mkdirs();

        try {

            System.out.println("Creating payment preferences file...");
            new File(XRPTipper.getPlayerPaymentPreferencesFilePath()).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Investigate to see if a new user should be created immediately or on logout (are preference held in memory at first?)
    public void newUser(String playerName, UUID uuid, String xrplAddress) {

        System.out.println("PlayerName: " + playerName);
        System.out.println("UUID: " + uuid);
        BufferedWriter out = null;
        synchronized (fileWritingLock) {

            try {

                out = new BufferedWriter(new FileWriter(XRPTipper.getPlayerPaymentPreferencesFilePath(), true));

                out.append(playerName).append(":");
                out.append(xrplAddress).append(":"); // XRPL Address
                out.append(":"); // XUMM Token
                out.append(uuid != null ? uuid.toString() : "NULL").append(":"); //UUID

                out.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // Ignore
                    }
                }
            }
        }
    }

    public PlayerProfile loadPlayerProfile(String playerName, UUID uuid, boolean createNew) {

        BufferedReader in = null;
        String usersFilePath = XRPTipper.getPlayerPaymentPreferencesFilePath();

        synchronized (fileWritingLock) {
            try {

                in = new BufferedReader(new FileReader(usersFilePath));
                String line;

                while ((line = in.readLine()) != null) {

                    // Find the line that contains the player
                    String[] character = line.split(":");

                    //Currently I'm assuming every player has a UUID.
                    // I don't know when that wouldn't be the case, but I may need to learn more
                    System.out.println(character[UUID_INDEX]);
                    if (uuid != null && !character[UUID_INDEX].equalsIgnoreCase(uuid.toString())) {
                        continue;
                    }

                    //We can add in code to detect namechanges here

                    return loadFromLine(character);
                }

                //Didn't find the player
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // I have no idea what an inline tryClose() is here, but the comments suggest
                // there will be a resource leak warning, and I'm trusting nossr50 on this one.
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // Ignore
                    }
                }
            }
        }

        return null;
    }

    private PlayerProfile loadFromLine(String[] character) {

        String xrplAddress;
        try {

            xrplAddress = character[XRPL_ADDRESS];
        } catch (Exception e) {
            xrplAddress = null;
        }

        String xummToken;
        try {

            xummToken = character[XUMM_TOKEN_INDEX];
        } catch (Exception e) {
            xummToken = null;
        }

        UUID uuid;
        try {

            uuid = UUID.fromString(character[UUID_INDEX]);
        } catch (Exception e) {
            uuid = null;
        }

        return new PlayerProfile(character[USERRNAME], uuid, xrplAddress, xummToken);
    }

    public boolean saveUser(PlayerProfile profile) {

        String playerName = profile.getPlayerName();
        UUID uuid = profile.getUniqueId();

        BufferedReader in = null;
        FileWriter out = null;
        String usersFilePath = XRPTipper.getPlayerPaymentPreferencesFilePath();

        synchronized (fileWritingLock) {

            try {

                in = new BufferedReader(new FileReader(usersFilePath));
                StringBuilder writer = new StringBuilder();
                String line;

                boolean wroteUser = false;
                while ((line = in.readLine()) != null) {

                    String[] character = line.split(":");
                    if(!(uuid != null && character[UUID_INDEX].equalsIgnoreCase(uuid.toString())) && !character[USERRNAME].equalsIgnoreCase(playerName)) {
                        writer.append(line).append("\r\n");
                    } else {

                        writeUserToLine(profile, playerName, uuid, writer);
                        wroteUser = true;
                    }
                }

                if(!wroteUser) {

                    writeUserToLine(profile, playerName, uuid, writer);
                }

                out = new FileWriter(usersFilePath);
                out.write(writer.toString());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }             finally {
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (IOException e) {
                        // Ignore
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    }
                    catch (IOException e) {
                        // Ignore
                    }
                }
            }
        }
    }

    private void writeUserToLine(PlayerProfile profile, String playerName, UUID uuid, StringBuilder writer) {

        writer.append(playerName).append(":");
        writer.append(profile.getXrplAddress()).append(":");
        writer.append(profile.getXummToken()).append(":");
        writer.append(uuid != null ? uuid.toString() : "NULL").append(":");
        writer.append("\r\n");
    }

    public static int USERRNAME = 0;
    public static int XRPL_ADDRESS = 1;
    public static int XUMM_TOKEN_INDEX = 2;
    public static int UUID_INDEX = 3;
}
