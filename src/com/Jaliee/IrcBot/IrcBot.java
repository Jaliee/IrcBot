package com.Jaliee.IrcBot;

import org.jibble.pircbot.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jocke on 2017-09-15.
 */
public class IrcBot extends PircBot {

    private String server;
    private ArrayList<String> channels;
    private String password;
    private String name;
    private String owner;
    private int port;

    public IrcBot(String serverConfig, String channelsFile) {
        super();
        channels = new ArrayList<>();
        loadServerConfig(serverConfig);
        loadChannels(channelsFile);

        if (server != null && !channels.isEmpty()) {
            try {
                setName(name);
                setLogin(name);
                connect(server, port, password);
                System.out.println("Connection to server successful.");
                for (String c : channels) {
                    joinChannel(c);
                    System.out.println("Joined channel " + c);
                }
            } catch (IOException ioe) {
                System.out.println("Connection failed: " + ioe);
            } catch (IrcException ie) {
                System.out.println("Server denied connection: " + ie);
            }
        } else {
            System.out.println("Error in server or channel config");
        }

    }

    protected void onMessage(String channel, String sender, String login, String hostname, String message) {

        if (sender.equals(owner)) {
            String lcMessage = message.toLowerCase();
            switch (lcMessage) {
                case "time":
                    String time = new java.util.Date().toString();
                    sendMessage(channel, sender + ": The time is now " + time);
                    break;
                case "hello jalieebot":
                    sendMessage(channel, "Hello!");
                    break;
                case "goodbye jalieebot":
                    sendMessage(channel, "Goodbye!");
                    disconnect();
                    System.exit(0);
                    break;
                case "attend me squire":
                    sendMessage(channel, "!play");

            }
            if (lcMessage.contains("join")) {
                Pattern p = Pattern.compile("#\\w*");
                Matcher m = p.matcher(message);
                String matchedChan = null;
                while (m.find()) {
                    matchedChan = m.group();
                    joinChannel(matchedChan);
                    System.out.println("Joined: " + matchedChan);
                }

            }
        }

    }


    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        switch (message) {
            case "disconnect":
                disconnect();
                System.exit(0);
        }
    }

    protected void onJoin(String channel, String sender, String login, String hostname) {
        //sendMessage(channel, "Hello");
    }

    private void loadServerConfig(String file) {
        InputStream input = null;
        Properties cfg = new Properties();

        try {
            input = new FileInputStream(file);
            cfg.load(input);
            server = cfg.getProperty("server");
            password = cfg.getProperty("password");
            name = cfg.getProperty("name");
            port = Integer.parseInt(cfg.getProperty("port"));
            owner = cfg.getProperty("owner");
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

    private void loadChannels(String file) {
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
