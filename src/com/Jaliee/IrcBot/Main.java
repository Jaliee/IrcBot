package com.Jaliee.twitchbot;


import org.jibble.pircbot.IrcException;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Jocke on 2017-09-15.
 */
public class Main {

    private static String server;
    private static ArrayList<String> channels;
    private static String password;
    private static String serverConfig = "server.cfg";
    private static String channelsFile = "channels.txt";

    public static void main(String[] args) {

        channels = new ArrayList<>();
        loadServerConfig(serverConfig);
        loadChannels(channelsFile);

        if (server != null && !channels.isEmpty()) {
            // Start bot.
            TwitchBot bot = new TwitchBot();

            // Enable debugging.
            bot.setVerbose(true);

            try {
                // Connect to IRC.
                bot.connect(server);

                // Join the channel
                for (String c : channels) {
                    bot.joinChannel(c);
                }
            } catch (IOException ioe) {
                System.out.println("Connection failed");
            } catch (IrcException ie) {
                System.out.println("Connection failed");
            }
        }
    }

    private static void loadServerConfig(String file) {

        InputStream input = null;
        Properties cfg = new Properties();

        try {
            input = new FileInputStream(file);
            cfg.load(input);
            server = cfg.getProperty("server");
            password = cfg.getProperty("password");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Error closing file: " + e);
                }
            }
        }
    }

    private static void loadChannels(String file) {
        try {
            Scanner s = new Scanner(new File(file));
            while (s.hasNext()) {
                channels.add(s.next());
            }
            s.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println(file + " not found. " + fnfe);
        }
    }
}
