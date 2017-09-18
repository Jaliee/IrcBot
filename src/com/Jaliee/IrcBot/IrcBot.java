package com.Jaliee.IrcBot;

import org.jibble.pircbot.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Jocke on 2017-09-15.
 */
public class IrcBot extends PircBot {

    private String server;
    private ArrayList<String> channels;
    private String password;
    private String name;
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
                for (String c : channels) {
                    joinChannel(c);
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

        String lcMessage = message.toLowerCase();
        switch (lcMessage) {
            case "time":
                String time = new java.util.Date().toString();
                sendMessage(channel, sender + ": The time is now " + time);
                break;

        }
    }

    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        switch (message) {
            case "disconnect":
                disconnect();
                System.exit(0);
        }
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
